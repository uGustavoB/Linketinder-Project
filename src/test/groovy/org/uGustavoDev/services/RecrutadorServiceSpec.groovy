package org.uGustavoDev.services

import org.uGustavoDev.builder.EmpresaBuilder
import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dao.interfaces.IRecrutadorDAO
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.IRecrutadorMapper
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Recrutador
import org.uGustavoDev.service.RecrutadorService
import org.uGustavoDev.service.interfaces.IRecrutadorService
import spock.lang.Specification
import java.time.LocalDate

class RecrutadorServiceSpec extends Specification {

    IRecrutadorService recrutadorService
    IRecrutadorDAO mockRecrutadorDAO
    IEmpresaDAO mockEmpresaDAO
    IRecrutadorMapper mockRecrutadorMapper

    void setup() {
        mockRecrutadorDAO = Mock(IRecrutadorDAO)
        mockEmpresaDAO = Mock(IEmpresaDAO)
        mockRecrutadorMapper = Mock(IRecrutadorMapper)
        recrutadorService = new RecrutadorService(mockRecrutadorDAO, mockEmpresaDAO, mockRecrutadorMapper)
    }

    void "deve adicionar recrutador com sucesso quando dados sao validos e empresa existe"() {
        given: "um DTO de recrutador válido e uma empresa existente"
        Empresa empresaExistente = new EmpresaBuilder()
                .id(1)
                .nome("Pastelsoft")
                .email("contato@pastelsoft.com.br")
                .pais("Brasil")
                .cep("01000-000")
                .descricao("Software")
                .cnpj("11.111.111/0001-11")
                .build()

        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(
                empresaId: 1,
                nome: "Mariana",
                area: "TI",
                descricao: "Dev Recrutadora",
                email: "mariana@empresa.com",
                cpf: "111.222.333-44",
                dataNascimento: LocalDate.of(1995, 2, 10),
                senha: "senha123"
        )
        Recrutador recrutador = new RecrutadorBuilder()
                .empresaId(1)
                .nome("Mariana")
                .email("mariana@empresa.com")
                .cpf("111.222.333-44")
                .senha("senha123")
                .build()

        when: "o serviço adiciona o recrutador"
        Recrutador resultado = recrutadorService.adicionarRecrutador(dto)

        then: "a empresa é validada, o DAO é consultado para unicidade e o recrutador é salvo"
        1 * mockEmpresaDAO.buscarPorId(1) >> empresaExistente
        1 * mockRecrutadorDAO.buscarPorEmail("mariana@empresa.com") >> null
        1 * mockRecrutadorDAO.buscarPorCpf("111.222.333-44") >> null
        1 * mockRecrutadorMapper.paraEntidade(dto) >> recrutador
        1 * mockRecrutadorDAO.inserir(recrutador)
        resultado == recrutador
    }

    void "deve bloquear cadastro de recrutador se a empresa nao existir"() {
        given: "um DTO com ID de empresa inexistente"
        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(
                empresaId: 99,
                nome: "Mariana",
                email: "mariana@empresa.com",
                cpf: "111.222.333-44",
                senha: "senha123"
        )

        when: "tentamos adicionar o recrutador"
        recrutadorService.adicionarRecrutador(dto)

        then: "uma exceção de argumento inválido é lançada"
        1 * mockEmpresaDAO.buscarPorId(99) >> null
        IllegalArgumentException e = thrown()
        e.message.contains("Empresa informada não existe")
    }

    void "deve bloquear cadastro de recrutador se o email ja estiver em uso"() {
        given: "um DTO com email já cadastrado e empresa existente"
        Empresa empresaExistente = new EmpresaBuilder().id(1).build()
        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(
                empresaId: 1,
                email: "existente@empresa.com",
                cpf: "111.222.333-44",
                senha: "senha123"
        )
        Recrutador existente = new RecrutadorBuilder().build()

        when: "tentamos adicionar o recrutador"
        recrutadorService.adicionarRecrutador(dto)

        then: "uma exceção é lançada por duplicidade de email"
        1 * mockEmpresaDAO.buscarPorId(1) >> empresaExistente
        1 * mockRecrutadorDAO.buscarPorEmail("existente@empresa.com") >> existente
        IllegalArgumentException e = thrown()
        e.message.contains("Já existe um recrutador cadastrado com o email")
    }

    void "deve bloquear cadastro de recrutador se a senha for menor que 6 caracteres"() {
        given: "um DTO com senha curta e empresa existente"
        Empresa empresaExistente = new EmpresaBuilder().id(1).build()
        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(
                empresaId: 1,
                email: "novo@empresa.com",
                cpf: "111.222.333-44",
                senha: "123"
        )

        when: "tentamos adicionar o recrutador"
        recrutadorService.adicionarRecrutador(dto)

        then: "uma exceção de senha curta é lançada"
        1 * mockEmpresaDAO.buscarPorId(1) >> empresaExistente
        1 * mockRecrutadorDAO.buscarPorEmail("novo@empresa.com") >> null
        1 * mockRecrutadorDAO.buscarPorCpf("111.222.333-44") >> null
        IllegalArgumentException e = thrown()
        e.message.contains("no mínimo 6 caracteres")
    }

    void "deve listar recrutadores por empresa com sucesso"() {
        given: "um ID de empresa"
        int empresaId = 1
        List<Recrutador> lista = [new RecrutadorBuilder().build()]

        when: "solicitamos a listagem por empresa"
        List<Recrutador> resultado = recrutadorService.listarRecrutadoresPorEmpresa(empresaId)

        then: "o DAO é chamado corretamente"
        1 * mockRecrutadorDAO.listarPorEmpresa(empresaId) >> lista
        resultado == lista
    }

    void "deve deletar recrutador com sucesso quando ele existe"() {
        given: "um ID de recrutador existente"
        int recrutadorId = 1
        Recrutador existente = new RecrutadorBuilder().id(1).build()

        when: "solicitamos a exclusão"
        recrutadorService.deletarRecrutador(recrutadorId)

        then: "o DAO busca o recrutador e executa a deleção"
        1 * mockRecrutadorDAO.buscarPorId(1) >> existente
        1 * mockRecrutadorDAO.deletar(1)
    }

    void "deve lançar exceção ao tentar deletar recrutador inexistente"() {
        given: "um ID de recrutador que não existe"
        int recrutadorId = 999

        when: "tentamos deletar"
        recrutadorService.deletarRecrutador(recrutadorId)

        then: "uma exceção é lançada"
        1 * mockRecrutadorDAO.buscarPorId(999) >> null
        IllegalArgumentException e = thrown()
        e.message.contains("Recrutador não encontrado para exclusão")
    }

    void "deve autenticar recrutador com sucesso quando email e senha forem validos"() {
        given: "um recrutador existente com credenciais válidas"
        Recrutador recrutadorEsperado = new RecrutadorBuilder()
                .id(1)
                .empresaId(1)
                .nome("Mariana")
                .email("mariana@empresa.com")
                .senha("senha123")
                .build()

        when: "o login é executado com as credenciais corretas"
        Recrutador resultado = recrutadorService.loginRecrutador("mariana@empresa.com", "senha123")

        then: "o DAO busca pelo email e o recrutador autenticado é retornado"
        1 * mockRecrutadorDAO.buscarPorEmail("mariana@empresa.com") >> recrutadorEsperado
        resultado == recrutadorEsperado
    }

    void "deve retornar null quando o recrutador nao for encontrado pelo email informado"() {
        when: "o login é executado com um email não cadastrado"
        Recrutador resultado = recrutadorService.loginRecrutador("naoexiste@empresa.com", "senha123")

        then: "o DAO não encontra o registro e o resultado é nulo"
        1 * mockRecrutadorDAO.buscarPorEmail("naoexiste@empresa.com") >> null
        resultado == null
    }

    void "deve retornar null quando a senha informada for incorreta"() {
        given: "um recrutador com senha definida"
        Recrutador recrutador = new RecrutadorBuilder()
                .id(1)
                .email("mariana@empresa.com")
                .senha("senhaCorreta123")
                .build()

        when: "o login é executado com senha divergente"
        Recrutador resultado = recrutadorService.loginRecrutador("mariana@empresa.com", "senhaIncorreta")

        then: "o DAO busca pelo email mas a autenticação falha retornando nulo"
        1 * mockRecrutadorDAO.buscarPorEmail("mariana@empresa.com") >> recrutador
        resultado == null
    }

    void "deve lancar DatabaseOperationException quando ocorrer erro de banco no login do recrutador"() {
        when: "ocorre uma falha no acesso a dados durante a busca pelo email"
        recrutadorService.loginRecrutador("mariana@empresa.com", "senha123")

        then: "uma DatabaseOperationException é propagada"
        1 * mockRecrutadorDAO.buscarPorEmail("mariana@empresa.com") >> {
            throw new DatabaseOperationException("Falha de conexão com o banco.")
        }
        thrown(DatabaseOperationException)
    }

}
