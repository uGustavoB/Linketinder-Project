package org.uGustavoDev.services

import org.uGustavoDev.builder.EmpresaBuilder
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.EmpresaService
import org.uGustavoDev.service.interfaces.IEmpresaService
import spock.lang.Specification

class EmpresaServiceSpec extends Specification {

    IEmpresaDAO mockEmpresaDAO
    ICompetenciaDAO mockCompetenciaDAO
    IEmpresaMapper mockEmpresaMapper
    IEmpresaService empresaService

    void setup() {
        mockEmpresaDAO = Mock(IEmpresaDAO)
        mockCompetenciaDAO = Mock(ICompetenciaDAO)
        mockEmpresaMapper = Mock(IEmpresaMapper)
        empresaService = new EmpresaService(mockEmpresaDAO, mockCompetenciaDAO, mockEmpresaMapper)
    }

    void "deve salvar empresa e vincular competencias quando dados sao validos e nao existem no banco"() {
        given: "uma empresa com dados ineditos"
        EmpresaCadastroDTO dto = new EmpresaCadastroDTO(nome: "Google", email: "google@google.com", pais: "USA", cep: "12345", descricao: "Tecnologia", cnpj: "11.111.111/0001-11", competencias: ["Java", "Python"])
        Empresa novaEmpresa = new EmpresaBuilder()
                .id(1)
                .nome("Google")
                .email("google@google.com")
                .pais("USA")
                .cep("12345")
                .descricao("Tecnologia")
                .cnpj("11.111.111/0001-11")
                .competencias(["Java", "Python"])
                .build()

        when: "o servico e chamado para adicionar a empresa"
        empresaService.adicionarEmpresa(dto)

        then: "verifica as validacoes passando e as insercoes ocorrendo corretamente no banco"
        1 * mockEmpresaDAO.buscarPorEmail(dto.email) >> null
        1 * mockEmpresaDAO.buscarPorCnpj(dto.cnpj) >> null
        1 * mockEmpresaMapper.paraEntidade(dto) >> novaEmpresa
        1 * mockEmpresaDAO.inserir(novaEmpresa)
        1 * mockCompetenciaDAO.buscarOuInserir("Java") >> 10
        1 * mockCompetenciaDAO.vincularAEmpresa(1, 10)
        1 * mockCompetenciaDAO.buscarOuInserir("Python") >> 20
        1 * mockCompetenciaDAO.vincularAEmpresa(1, 20)
    }

    void "deve bloquear a criacao de empresa quando o email ja estiver em uso"() {
        given: "uma empresa cujo email ja pertence a outra cadastrada"
        EmpresaCadastroDTO dto = new EmpresaCadastroDTO(email: "usado@google.com", cnpj: "11.111.111/0001-11")
        Empresa empresaExistente = new EmpresaBuilder()
                .nome("Antiga")
                .email("usado@google.com")
                .pais("Brasil")
                .cep("00000")
                .descricao("Startup")
                .cnpj("22.222.222/0001-22")
                .build()

        when: "tentar adicionar"
        empresaService.adicionarEmpresa(dto)

        then: "uma excecao de validacao e lancada e nenhuma insercao e feita"
        1 * mockEmpresaDAO.buscarPorEmail(dto.email) >> empresaExistente
        0 * mockEmpresaMapper.paraEntidade(_)
        0 * mockEmpresaDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("email 'usado@google.com'")
    }

    void "deve bloquear a criacao de empresa quando o cnpj ja estiver em uso"() {
        given: "uma empresa com CNPJ ja cadastrado no sistema"
        EmpresaCadastroDTO dto = new EmpresaCadastroDTO(email: "nova@google.com", cnpj: "99.999.999/0001-99")
        Empresa empresaExistente = new EmpresaBuilder()
                .nome("Antiga")
                .email("antiga@google.com")
                .pais("Brasil")
                .cep("00000")
                .descricao("Startup")
                .cnpj("99.999.999/0001-99")
                .build()

        when: "tentar adicionar"
        empresaService.adicionarEmpresa(dto)

        then: "uma excecao e lancada avisando sobre o conflito do CNPJ"
        1 * mockEmpresaDAO.buscarPorEmail(dto.email) >> null
        1 * mockEmpresaDAO.buscarPorCnpj(dto.cnpj) >> empresaExistente
        0 * mockEmpresaMapper.paraEntidade(_)
        0 * mockEmpresaDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("CNPJ '99.999.999/0001-99'")
    }

    void "deve envelopar erro do banco de dados em um runtime exception ao inserir empresa"() {
        given: "uma empresa valida mas com banco fora do ar"
        EmpresaCadastroDTO dto = new EmpresaCadastroDTO(email: "google@google.com", cnpj: "11.111.111/0001-11")
        Empresa novaEmpresa = new EmpresaBuilder()
                .nome("Google")
                .email("google@google.com")
                .pais("USA")
                .cep("12345")
                .descricao("Tecnologia")
                .cnpj("11.111.111/0001-11")
                .build()

        when: "tentar adicionar"
        empresaService.adicionarEmpresa(dto)

        then: "o erro SQL ou de Conexao e capturado e transformado na excecao de servico correta"
        1 * mockEmpresaDAO.buscarPorEmail(dto.email) >> null
        1 * mockEmpresaDAO.buscarPorCnpj(dto.cnpj) >> null
        1 * mockEmpresaMapper.paraEntidade(dto) >> novaEmpresa
        1 * mockEmpresaDAO.inserir(novaEmpresa) >> { throw new DatabaseOperationException("Falha de conexao") }
        DatabaseOperationException erro = thrown(DatabaseOperationException)
        erro.message == "Não foi possível salvar a empresa no banco de dados."
    }

    void "deve retornar lista completa de empresas sem alteracoes"() {
        given: "duas empresas cadastradas"
        Empresa e1 = new EmpresaBuilder()
                .nome("Google")
                .email("google@google.com")
                .pais("USA")
                .cep("12345")
                .descricao("Tecnologia")
                .cnpj("11.111.111/0001-11")
                .build()
        Empresa e2 = new EmpresaBuilder()
                .nome("Meta")
                .email("meta@meta.com")
                .pais("USA")
                .cep("54321")
                .descricao("Redes Sociais")
                .cnpj("22.222.222/0001-22")
                .build()

        List<Empresa> listaSimulada = [e1, e2]

        when: "listar empresas pelo servico"
        List<Empresa> retorno = empresaService.listarEmpresas()

        then: "recebemos a lista integral repassada pelo DAO"
        1 * mockEmpresaDAO.listar() >> listaSimulada
        retorno.size() == 2
        retorno.contains(e1)
        retorno.contains(e2)
    }

    void "deve realizar o login com sucesso ao fornecer as credenciais corretas"() {
        given: "uma empresa com a senha correta registrada no sistema"
        Empresa e1 = new EmpresaBuilder()
                .nome("Google")
                .email("google@google.com")
                .senha("senhaSegura123")
                .pais("USA")
                .cep("12345")
                .descricao("Tecnologia")
                .cnpj("11.111.111/0001-11")
                .build()

        when: "tentar logar fornecendo a mesma senha"
        Empresa logada = empresaService.loginEmpresa("google@google.com", "senhaSegura123")

        then: "o login ocorre perfeitamente e devolve a entidade"
        1 * mockEmpresaDAO.buscarPorEmail("google@google.com") >> e1
        logada == e1
    }

    void "deve negar o login e retornar nulo quando a senha fornecida estiver incorreta"() {
        given: "uma empresa no banco possuindo senha especifica"
        Empresa e1 = new EmpresaBuilder()
                .nome("Google")
                .email("google@google.com")
                .senha("senhaSegura123")
                .pais("USA")
                .cep("12345")
                .descricao("Tecnologia")
                .cnpj("11.111.111/0001-11")
                .build()

        when: "tentar logar digitando a senha errada"
        Empresa logada = empresaService.loginEmpresa("google@google.com", "senhaErrada")

        then: "o sistema bloqueia retornando um objeto nulo"
        1 * mockEmpresaDAO.buscarPorEmail("google@google.com") >> e1
        logada == null
    }

    void "deve negar o login e retornar nulo quando o email informado nao existir no sistema"() {
        when: "tentar logar com um email fantasma que nao foi cadastrado"
        Empresa logada = empresaService.loginEmpresa("fantasma@empresa.com", "senha123")

        then: "nenhum usuario e encontrado"
        1 * mockEmpresaDAO.buscarPorEmail("fantasma@empresa.com") >> null
        logada == null
    }

    void "deve atualizar os dados da empresa e recriar vinculos de competencias no banco"() {
        given: "uma empresa modificando suas atribuicoes de competencias"
        EmpresaCadastroDTO dto = new EmpresaCadastroDTO(competencias: ["Rust"])
        Empresa e1 = new EmpresaBuilder()
                .nome("Google")
                .email("google@google.com")
                .pais("USA")
                .cep("12345")
                .descricao("Tecnologia")
                .cnpj("11.111.111/0001-11")
                .build()

        e1.id = 5
        e1.competencias = ["Rust"]

        when: "solicitar a atualizacao atraves do servico"
        empresaService.atualizarEmpresa(5, dto)

        then: "as antigas competencias sao limpas e a nova e registrada adequadamente"
        1 * mockEmpresaMapper.paraEntidade(dto) >> e1
        1 * mockEmpresaDAO.atualizar(e1)
        1 * mockCompetenciaDAO.removerVinculosEmpresa(5)
        1 * mockCompetenciaDAO.buscarOuInserir("Rust") >> 99
        1 * mockCompetenciaDAO.vincularAEmpresa(5, 99)
    }

    void "deve repassar a instrucao de delecao de empresa de forma transparente para o dao"() {
        when: "solicitar exclusao permanente da conta da empresa"
        empresaService.deletarEmpresa(10)

        then: "a exclusao ocorre corretamente sem lancar excecoes imprevistas"
        1 * mockEmpresaDAO.deletar(10)
    }

    void "deve enrolar exceptions da operacao listar dentro de runtime exceptions controladas"() {
        when: "tentar listar e o banco estiver inoperante"
        empresaService.listarEmpresas()

        then: "o erro nao vaza exposto, mas sim sob uma exception prevista do service"
        1 * mockEmpresaDAO.listar() >> { throw new DatabaseOperationException("Timeout do banco") }
        DatabaseOperationException erro = thrown(DatabaseOperationException)
    }

}
