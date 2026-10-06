package org.uGustavoDev.services

import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.service.interfaces.ICandidatoService
import spock.lang.Specification

import java.time.LocalDate

class CandidatoServiceSpec extends Specification {

    ICandidatoService candidatoService
    ICandidatoDAO mockCandidatoDAO
    ICompetenciaDAO mockCompetenciaDAO
    ICandidatoMapper mockCandidatoMapper

    void setup() {
        mockCandidatoDAO = Mock(ICandidatoDAO)
        mockCompetenciaDAO = Mock(ICompetenciaDAO)
        mockCandidatoMapper = Mock(ICandidatoMapper)
        candidatoService = new CandidatoService(mockCandidatoDAO, mockCompetenciaDAO, mockCandidatoMapper)
    }

    void "deve salvar candidato e vincular competencias quando dados sao validos e nao existem no banco"() {
        given: "um candidato com dados ineditos"
        CandidatoCadastroDTO dto = new CandidatoCadastroDTO(nome: "Ana", sobrenome: "Silva", email: "ana@email.com", estado: "SP", pais: "Brasil", cep: "01000", descricao: "Dev Groovy", cpf: "111", dataNascimento: LocalDate.of(1990, 1, 1), competencias: ["Groovy", "Spock"])
        Candidato novoCandidato = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        novoCandidato.id = 1
        novoCandidato.competencias = ["Groovy", "Spock"]

        when: "o servico e chamado para adicionar o candidato"
        candidatoService.adicionarCandidato(dto)

        then: "verifica as validacoes passando e as insercoes ocorrendo corretamente no banco"
        1 * mockCandidatoDAO.buscarPorEmail(dto.email) >> null
        1 * mockCandidatoDAO.buscarPorCpf(dto.cpf) >> null
        1 * mockCandidatoMapper.paraEntidade(dto) >> novoCandidato
        1 * mockCandidatoDAO.inserir(novoCandidato)
        1 * mockCompetenciaDAO.buscarOuInserir("Groovy") >> 10
        1 * mockCompetenciaDAO.vincularAoCandidato(1, 10)
        1 * mockCompetenciaDAO.buscarOuInserir("Spock") >> 20
        1 * mockCompetenciaDAO.vincularAoCandidato(1, 20)
    }

    void "deve bloquear a criacao de candidato quando o email ja estiver em uso"() {
        given: "um candidato cujo email ja pertence a outro usuario"
        CandidatoCadastroDTO dto = new CandidatoCadastroDTO(email: "usado@email.com", cpf: "111")
        Candidato candidatoExistente = new Candidato("Antigo", "Usuario", "usado@email.com", "RJ", "Brasil", "20000", "Dev Java", "222", LocalDate.of(1985, 1, 1))

        when: "tentar adicionar"
        candidatoService.adicionarCandidato(dto)

        then: "uma excecao de validacao e lancada e nenhuma insercao e feita"
        1 * mockCandidatoDAO.buscarPorEmail(dto.email) >> candidatoExistente
        0 * mockCandidatoMapper.paraEntidade(_)
        0 * mockCandidatoDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("email 'usado@email.com'")
    }

    void "deve bloquear a criacao de candidato quando o cpf ja estiver em uso"() {
        given: "um candidato com CPF ja cadastrado no sistema"
        CandidatoCadastroDTO dto = new CandidatoCadastroDTO(email: "novo@email.com", cpf: "999")
        Candidato candidatoExistente = new Candidato("Antigo", "Usuario", "antigo@email.com", "RJ", "Brasil", "20000", "Dev Java", "999", LocalDate.of(1985, 1, 1))

        when: "tentar adicionar"
        candidatoService.adicionarCandidato(dto)

        then: "uma excecao e lancada avisando sobre o conflito do CPF"
        1 * mockCandidatoDAO.buscarPorEmail(dto.email) >> null
        1 * mockCandidatoDAO.buscarPorCpf(dto.cpf) >> candidatoExistente
        0 * mockCandidatoMapper.paraEntidade(_)
        0 * mockCandidatoDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("CPF '999'")
    }

    void "deve envelopar erro do banco de dados em um runtime exception ao inserir candidato"() {
        given: "um candidato valido mas com banco fora do ar"
        CandidatoCadastroDTO dto = new CandidatoCadastroDTO(email: "ana@email.com", cpf: "111")
        Candidato novoCandidato = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))

        when: "tentar adicionar"
        candidatoService.adicionarCandidato(dto)

        then: "o erro SQL ou de Conexao e capturado e transformado na excecao de servico correta"
        1 * mockCandidatoDAO.buscarPorEmail(dto.email) >> null
        1 * mockCandidatoDAO.buscarPorCpf(dto.cpf) >> null
        1 * mockCandidatoMapper.paraEntidade(dto) >> novoCandidato
        1 * mockCandidatoDAO.inserir(novoCandidato) >> { throw new DatabaseOperationException("Falha de conexao") }
        DatabaseOperationException erro = thrown(DatabaseOperationException)
        erro.message.contains("Não foi possível salvar o candidato no banco de dados")
    }

    void "deve retornar lista completa de candidatos sem alteracoes"() {
        given: "dois candidatos cadastrados"
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        Candidato c2 = new Candidato("Beto", "Souza", "beto@email.com", "RJ", "Brasil", "20000", "Dev Java", "222", LocalDate.of(1992, 2, 2))
        List<Candidato> listaSimulada = [c1, c2]

        when: "listar candidatos pelo servico"
        List<Candidato> retorno = candidatoService.listarCandidatos()

        then: "recebemos a lista integral repassada pelo DAO"
        1 * mockCandidatoDAO.listar() >> listaSimulada
        retorno.size() == 2
        retorno.contains(c1)
        retorno.contains(c2)
    }

    void "deve realizar o login com sucesso ao fornecer as credenciais corretas"() {
        given: "um candidato com a senha correta registrada no sistema"
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        c1.senha = "senhaSegura123"

        when: "tentar logar fornecendo a mesma senha"
        Candidato logado = candidatoService.loginCandidato("ana@email.com", "senhaSegura123")

        then: "o login ocorre perfeitamente e devolve a entidade"
        1 * mockCandidatoDAO.buscarPorEmail("ana@email.com") >> c1
        logado == c1
    }

    void "deve negar o login e retornar nulo quando a senha fornecida estiver incorreta"() {
        given: "um candidato no banco possuindo senha especifica"
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        c1.senha = "senhaSegura123"

        when: "tentar logar digitando a senha errada"
        Candidato logado = candidatoService.loginCandidato("ana@email.com", "senhaErrada")

        then: "o sistema bloqueia retornando um objeto nulo"
        1 * mockCandidatoDAO.buscarPorEmail("ana@email.com") >> c1
        logado == null
    }

    void "deve negar o login e retornar nulo quando o email informado nao existir no sistema"() {
        when: "tentar logar com um email fantasma que nao foi cadastrado"
        Candidato logado = candidatoService.loginCandidato("fantasma@email.com", "senha123")

        then: "nenhum usuario e encontrado"
        1 * mockCandidatoDAO.buscarPorEmail("fantasma@email.com") >> null
        logado == null
    }

    void "deve atualizar os dados do candidato e recriar vinculos de competencias no banco"() {
        given: "um candidato modificando suas atribuicoes de competencias"
        CandidatoCadastroDTO dto = new CandidatoCadastroDTO(email: "ana@email.com", cpf: "111", competencias: ["Rust"])
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev", "111", LocalDate.of(1990, 1, 1))
        c1.id = 5
        c1.competencias = ["Rust"]

        when: "solicitar a atualizacao atraves do servico"
        candidatoService.atualizarCandidato(5, dto)

        then: "as antigas competencias sao limpas e a nova e registrada adequadamente"
        1 * mockCandidatoDAO.buscarPorId(5) >> c1
        1 * mockCandidatoDAO.buscarPorEmail(dto.email) >> null
        1 * mockCandidatoDAO.buscarPorCpf(dto.cpf) >> null
        1 * mockCandidatoMapper.paraEntidade(dto) >> c1
        1 * mockCandidatoDAO.atualizar(c1)
        1 * mockCompetenciaDAO.removerVinculosCandidato(5)
        1 * mockCompetenciaDAO.buscarOuInserir("Rust") >> 99
        1 * mockCompetenciaDAO.vincularAoCandidato(5, 99)
    }

    void "deve repassar a instrucao de delecao de candidato de forma transparente para o dao"() {
        when: "solicitar exclusao permanente da conta do candidato"
        candidatoService.deletarCandidato(10)

        then: "a exclusao ocorre corretamente sem lancar excecoes imprevistas"
        1 * mockCandidatoDAO.deletar(10)
    }

}
