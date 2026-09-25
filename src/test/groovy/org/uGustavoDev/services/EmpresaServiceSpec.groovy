package org.uGustavoDev.services

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.EmpresaService
import spock.lang.Specification

class EmpresaServiceSpec extends Specification {

    EmpresaService empresaService

    void setup() {
        empresaService = new EmpresaService()
        GroovyMock(EmpresaDAO, global: true)
        GroovyMock(CompetenciaDAO, global: true)
    }

    void "deve salvar empresa e vincular competencias quando dados sao validos e nao existem no banco"() {
        given: "uma empresa com dados ineditos"
        Empresa novaEmpresa = new Empresa("Google", "google@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")
        novaEmpresa.id = 1
        novaEmpresa.competencias = ["Java", "Python"]

        when: "o servico e chamado para adicionar a empresa"
        empresaService.adicionarEmpresa(novaEmpresa)

        then: "verifica as validacoes passando e as insercoes ocorrendo corretamente no banco"
        1 * EmpresaDAO.buscarPorEmail(novaEmpresa.email) >> null
        1 * EmpresaDAO.buscarPorCnpj(novaEmpresa.cnpj) >> null
        1 * EmpresaDAO.inserir(novaEmpresa)
        1 * CompetenciaDAO.buscarOuInserir("Java") >> 10
        1 * CompetenciaDAO.vincularAEmpresa(1, 10)
        1 * CompetenciaDAO.buscarOuInserir("Python") >> 20
        1 * CompetenciaDAO.vincularAEmpresa(1, 20)
    }

    void "deve bloquear a criacao de empresa quando o email ja estiver em uso"() {
        given: "uma empresa cujo email ja pertence a outra cadastrada"
        Empresa novaEmpresa = new Empresa("Google", "usado@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")
        Empresa empresaExistente = new Empresa("Antiga", "usado@google.com", "Brasil", "00000", "Startup", "22.222.222/0001-22")

        when: "tentar adicionar"
        empresaService.adicionarEmpresa(novaEmpresa)

        then: "uma excecao de validacao e lancada e nenhuma insercao e feita"
        1 * EmpresaDAO.buscarPorEmail(novaEmpresa.email) >> empresaExistente
        0 * EmpresaDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("email 'usado@google.com'")
    }

    void "deve bloquear a criacao de empresa quando o cnpj ja estiver em uso"() {
        given: "uma empresa com CNPJ ja cadastrado no sistema"
        Empresa novaEmpresa = new Empresa("Google", "nova@google.com", "USA", "12345", "Tecnologia", "99.999.999/0001-99")
        Empresa empresaExistente = new Empresa("Antiga", "antiga@google.com", "Brasil", "00000", "Startup", "99.999.999/0001-99")

        when: "tentar adicionar"
        empresaService.adicionarEmpresa(novaEmpresa)

        then: "uma excecao e lancada avisando sobre o conflito do CNPJ"
        1 * EmpresaDAO.buscarPorEmail(novaEmpresa.email) >> null
        1 * EmpresaDAO.buscarPorCnpj(novaEmpresa.cnpj) >> empresaExistente
        0 * EmpresaDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("CNPJ '99.999.999/0001-99'")
    }

    void "deve envelopar erro do banco de dados em um runtime exception ao inserir empresa"() {
        given: "uma empresa valida mas com banco fora do ar"
        Empresa novaEmpresa = new Empresa("Google", "google@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")

        when: "tentar adicionar"
        empresaService.adicionarEmpresa(novaEmpresa)

        then: "o erro SQL ou de Conexao e capturado e transformado na excecao de servico correta"
        1 * EmpresaDAO.buscarPorEmail(novaEmpresa.email) >> null
        1 * EmpresaDAO.buscarPorCnpj(novaEmpresa.cnpj) >> null
        1 * EmpresaDAO.inserir(novaEmpresa) >> { throw new Exception("Falha de conexao") }
        RuntimeException erro = thrown(RuntimeException)
        erro.message == "Não foi possível salvar a empresa no banco de dados."
    }

    void "deve retornar lista completa de empresas sem alteracoes"() {
        given: "duas empresas cadastradas"
        Empresa e1 = new Empresa("Google", "google@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")
        Empresa e2 = new Empresa("Meta", "meta@meta.com", "USA", "54321", "Redes Sociais", "22.222.222/0001-22")
        List<Empresa> listaSimulada = [e1, e2]

        when: "listar empresas pelo servico"
        List<Empresa> retorno = empresaService.listarEmpresas()

        then: "recebemos a lista integral repassada pelo DAO"
        1 * EmpresaDAO.listar() >> listaSimulada
        retorno.size() == 2
        retorno.contains(e1)
        retorno.contains(e2)
    }

    void "deve realizar o login com sucesso ao fornecer as credenciais corretas"() {
        given: "uma empresa com a senha correta registrada no sistema"
        Empresa e1 = new Empresa("Google", "google@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")
        e1.senha = "senhaSegura123"

        when: "tentar logar fornecendo a mesma senha"
        Empresa logada = empresaService.loginEmpresa("google@google.com", "senhaSegura123")

        then: "o login ocorre perfeitamente e devolve a entidade"
        1 * EmpresaDAO.buscarPorEmail("google@google.com") >> e1
        logada == e1
    }

    void "deve negar o login e retornar nulo quando a senha fornecida estiver incorreta"() {
        given: "uma empresa no banco possuindo senha especifica"
        Empresa e1 = new Empresa("Google", "google@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")
        e1.senha = "senhaSegura123"

        when: "tentar logar digitando a senha errada"
        Empresa logada = empresaService.loginEmpresa("google@google.com", "senhaErrada")

        then: "o sistema bloqueia retornando um objeto nulo"
        1 * EmpresaDAO.buscarPorEmail("google@google.com") >> e1
        logada == null
    }

    void "deve negar o login e retornar nulo quando o email informado nao existir no sistema"() {
        when: "tentar logar com um email fantasma que nao foi cadastrado"
        Empresa logada = empresaService.loginEmpresa("fantasma@empresa.com", "senha123")

        then: "nenhum usuario e encontrado"
        1 * EmpresaDAO.buscarPorEmail("fantasma@empresa.com") >> null
        logada == null
    }

    void "deve atualizar os dados da empresa e recriar vinculos de competencias no banco"() {
        given: "uma empresa modificando suas atribuicoes de competencias"
        Empresa e1 = new Empresa("Google", "google@google.com", "USA", "12345", "Tecnologia", "11.111.111/0001-11")
        e1.id = 5
        e1.competencias = ["Rust"]

        when: "solicitar a atualizacao atraves do servico"
        empresaService.atualizarEmpresa(e1)

        then: "as antigas competencias sao limpas e a nova e registrada adequadamente"
        1 * EmpresaDAO.atualizar(e1)
        1 * CompetenciaDAO.removerVinculosEmpresa(5)
        1 * CompetenciaDAO.buscarOuInserir("Rust") >> 99
        1 * CompetenciaDAO.vincularAEmpresa(5, 99)
    }

    void "deve repassar a instrucao de delecao de empresa de forma transparente para o dao"() {
        when: "solicitar exclusao permanente da conta da empresa"
        empresaService.deletarEmpresa(10)

        then: "a exclusao ocorre corretamente sem lancar excecoes imprevistas"
        1 * EmpresaDAO.deletar(10)
    }

    void "deve enrolar exceptions da operacao listar dentro de runtime exceptions controladas"() {
        when: "tentar listar e o banco estiver inoperante"
        empresaService.listarEmpresas()

        then: "o erro nao vaza exposto, mas sim sob uma exception prevista do service"
        1 * EmpresaDAO.listar() >> { throw new Exception("Timeout do banco") }
        RuntimeException erro = thrown(RuntimeException)
    }
}
