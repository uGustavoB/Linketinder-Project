package org.uGustavoDev.services

import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.service.CandidatoService
import spock.lang.Specification
import java.time.LocalDate

class CandidatoServiceSpec extends Specification {
    
    CandidatoService candidatoService

    void setup() {
        candidatoService = new CandidatoService()
        GroovyMock(CandidatoDAO, global: true)
        GroovyMock(CompetenciaDAO, global: true)
    }

    void "deve salvar candidato e vincular competencias quando dados sao validos e nao existem no banco"() {
        given: "um candidato com dados ineditos"
        Candidato novoCandidato = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        novoCandidato.id = 1
        novoCandidato.adicionarCompetencia("Groovy")
        novoCandidato.adicionarCompetencia("Spock")

        when: "o servico e chamado para adicionar o candidato"
        candidatoService.adicionarCandidato(novoCandidato)

        then: "verifica as validacoes passando e as insercoes ocorrendo corretamente no banco"
        1 * CandidatoDAO.buscarPorEmail(novoCandidato.email) >> null
        1 * CandidatoDAO.buscarPorCpf(novoCandidato.cpf) >> null
        1 * CandidatoDAO.inserir(novoCandidato)
        1 * CompetenciaDAO.buscarOuInserir("Groovy") >> 10
        1 * CompetenciaDAO.vincularAoCandidato(1, 10)
        1 * CompetenciaDAO.buscarOuInserir("Spock") >> 20
        1 * CompetenciaDAO.vincularAoCandidato(1, 20)
    }

    void "deve bloquear a criacao de candidato quando o email ja estiver em uso"() {
        given: "um candidato cujo email ja pertence a outro usuario"
        Candidato novoCandidato = new Candidato("Ana", "Silva", "usado@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        Candidato candidatoExistente = new Candidato("Antigo", "Usuario", "usado@email.com", "RJ", "Brasil", "20000", "Dev Java", "222", LocalDate.of(1985, 1, 1))

        when: "tentar adicionar"
        candidatoService.adicionarCandidato(novoCandidato)

        then: "uma excecao de validacao e lancada e nenhuma insercao e feita"
        1 * CandidatoDAO.buscarPorEmail(novoCandidato.email) >> candidatoExistente
        0 * CandidatoDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("email 'usado@email.com'")
    }

    void "deve bloquear a criacao de candidato quando o cpf ja estiver em uso"() {
        given: "um candidato com CPF ja cadastrado no sistema"
        Candidato novoCandidato = new Candidato("Ana", "Silva", "nova@email.com", "SP", "Brasil", "01000", "Dev Groovy", "999", LocalDate.of(1990, 1, 1))
        Candidato candidatoExistente = new Candidato("Antigo", "Usuario", "antigo@email.com", "RJ", "Brasil", "20000", "Dev Java", "999", LocalDate.of(1985, 1, 1))

        when: "tentar adicionar"
        candidatoService.adicionarCandidato(novoCandidato)

        then: "uma excecao e lancada avisando sobre o conflito do CPF"
        1 * CandidatoDAO.buscarPorEmail(novoCandidato.email) >> null
        1 * CandidatoDAO.buscarPorCpf(novoCandidato.cpf) >> candidatoExistente
        0 * CandidatoDAO.inserir(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message.contains("CPF '999'")
    }

    void "deve envelopar erro do banco de dados em um runtime exception ao inserir candidato"() {
        given: "um candidato valido mas com banco fora do ar"
        Candidato novoCandidato = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))

        when: "tentar adicionar"
        candidatoService.adicionarCandidato(novoCandidato)

        then: "o erro SQL ou de Conexao e capturado e transformado na excecao de servico correta"
        1 * CandidatoDAO.buscarPorEmail(novoCandidato.email) >> null
        1 * CandidatoDAO.buscarPorCpf(novoCandidato.cpf) >> null
        1 * CandidatoDAO.inserir(novoCandidato) >> { throw new Exception("Falha de conexao") }
        RuntimeException erro = thrown(RuntimeException)
        erro.message == "Não foi possível salvar o candidato no banco de dados."
    }

    void "deve retornar lista completa de candidatos sem alteracoes"() {
        given: "dois candidatos cadastrados"
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        Candidato c2 = new Candidato("Beto", "Souza", "beto@email.com", "RJ", "Brasil", "20000", "Dev Java", "222", LocalDate.of(1992, 2, 2))
        List<Candidato> listaSimulada = [c1, c2]

        when: "listar candidatos pelo servico"
        List<Candidato> retorno = candidatoService.listarCandidatos()

        then: "recebemos a lista integral repassada pelo DAO"
        1 * CandidatoDAO.listar() >> listaSimulada
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
        1 * CandidatoDAO.buscarPorEmail("ana@email.com") >> c1
        logado == c1
    }

    void "deve negar o login e retornar nulo quando a senha fornecida estiver incorreta"() {
        given: "um candidato no banco possuindo senha especifica"
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev Groovy", "111", LocalDate.of(1990, 1, 1))
        c1.senha = "senhaSegura123"

        when: "tentar logar digitando a senha errada"
        Candidato logado = candidatoService.loginCandidato("ana@email.com", "senhaErrada")

        then: "o sistema bloqueia retornando um objeto nulo"
        1 * CandidatoDAO.buscarPorEmail("ana@email.com") >> c1
        logado == null
    }

    void "deve negar o login e retornar nulo quando o email informado nao existir no sistema"() {
        when: "tentar logar com um email fantasma que nao foi cadastrado"
        Candidato logado = candidatoService.loginCandidato("fantasma@email.com", "senha123")

        then: "nenhum usuario e encontrado"
        1 * CandidatoDAO.buscarPorEmail("fantasma@email.com") >> null
        logado == null
    }

    void "deve atualizar os dados do candidato e recriar vinculos de competencias no banco"() {
        given: "um candidato modificando suas atribuicoes de competencias"
        Candidato c1 = new Candidato("Ana", "Silva", "ana@email.com", "SP", "Brasil", "01000", "Dev", "111", LocalDate.of(1990, 1, 1))
        c1.id = 5
        c1.adicionarCompetencia("Rust")

        when: "solicitar a atualizacao atraves do servico"
        candidatoService.atualizarCandidato(c1)

        then: "as antigas competencias sao limpas e a nova e registrada adequadamente"
        1 * CandidatoDAO.atualizar(c1)
        1 * CompetenciaDAO.removerVinculosCandidato(5)
        1 * CompetenciaDAO.buscarOuInserir("Rust") >> 99
        1 * CompetenciaDAO.vincularAoCandidato(5, 99)
    }

    void "deve repassar a instrucao de delecao de candidato de forma transparente para o dao"() {
        when: "solicitar exclusao permanente da conta do candidato"
        candidatoService.deletarCandidato(10)

        then: "a exclusao ocorre corretamente sem lancar excecoes imprevistas"
        1 * CandidatoDAO.deletar(10)
    }
}
