package org.uGustavoDev.services

import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IVagaDAO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.IVagaMapper
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.service.interfaces.IVagaService
import spock.lang.Specification

class VagaServiceSpec extends Specification {

    IVagaDAO mockVagaDAO
    ICompetenciaDAO mockCompetenciaDAO
    IVagaMapper mockVagaMapper
    IVagaService vagaService

    void setup() {
        mockVagaDAO = Mock(IVagaDAO)
        mockCompetenciaDAO = Mock(ICompetenciaDAO)
        mockVagaMapper = Mock(IVagaMapper)
        vagaService = new VagaService(mockVagaDAO, mockCompetenciaDAO, mockVagaMapper)
    }

    void "deve adicionar uma vaga e registrar suas competencias no banco de dados"() {
        given: "uma vaga preenchida com as devidas competencias"
        VagaCadastroDTO dto = new VagaCadastroDTO(nome: "Desenvolvedor Backend", descricao: "Vaga para dev java", estado: "SP", cidade: "Sao Paulo", competencias: ["Java", "Spring"])
        Vaga vaga = new Vaga(1, "Desenvolvedor Backend", "Vaga para dev java", "SP", "Sao Paulo")
        vaga.id = 100
        vaga.competencias = ["Java", "Spring"]

        when: "o servico adicionar for acionado"
        vagaService.adicionarVaga(1, dto)

        then: "o DAO salva a vaga e as competencias sao inseridas e vinculadas no banco"
        1 * mockVagaMapper.paraEntidade(dto, 1) >> vaga
        1 * mockVagaDAO.inserir(vaga)
        1 * mockCompetenciaDAO.buscarOuInserir("Java") >> 10
        1 * mockCompetenciaDAO.vincularAVaga(100, 10)
        1 * mockCompetenciaDAO.buscarOuInserir("Spring") >> 20
        1 * mockCompetenciaDAO.vincularAVaga(100, 20)
    }

    void "deve retornar a lista global de todas as vagas cadastradas"() {
        given: "algumas vagas no sistema"
        Vaga v1 = new Vaga(1, "Vaga A", "Desc A", "SP", "Sao Paulo")
        Vaga v2 = new Vaga(2, "Vaga B", "Desc B", "RJ", "Rio de Janeiro")
        List<Vaga> vagasSalvas = [v1, v2]

        when: "o servico pedir todas as vagas"
        List<Vaga> retorno = vagaService.listarVagas()

        then: "todas sao repassadas com sucesso"
        1 * mockVagaDAO.listar() >> vagasSalvas
        retorno == vagasSalvas
    }

    void "deve retornar a lista de vagas restrita a uma empresa especifica"() {
        given: "duas vagas da empresa X e uma da empresa Y"
        Vaga v1 = new Vaga(1, "Vaga A", "Desc A", "SP", "Sao Paulo")
        Vaga v2 = new Vaga(1, "Vaga B", "Desc B", "RJ", "Rio de Janeiro")
        List<Vaga> vagasDaEmpresa = [v1, v2]

        when: "buscar vagas passando o ID da empresa X"
        List<Vaga> retorno = vagaService.listarVagasDaEmpresa(1)

        then: "apenas as vagas correspondentes a ela sao retornadas"
        1 * mockVagaDAO.listarPorEmpresa(1) >> vagasDaEmpresa
        retorno == vagasDaEmpresa
    }

    void "deve encontrar e retornar uma vaga pesquisando pelo seu id"() {
        given: "uma vaga cadastrada com um id alvo"
        Vaga vagaEsperada = new Vaga(1, "Vaga A", "Desc A", "SP", "Sao Paulo")
        vagaEsperada.id = 100

        when: "pedir a vaga pelo seu ID ao servico"
        Vaga retorno = vagaService.buscarVagaPorId(100)

        then: "o DAO busca corretamente"
        1 * mockVagaDAO.buscarPorId(100) >> vagaEsperada
        retorno == vagaEsperada
    }

    void "deve bloquear tentativa de edicao de vaga caso ela nao exista no banco"() {
        given: "uma DTO tentando editar uma vaga fantasma"
        VagaCadastroDTO dto = new VagaCadastroDTO(nome: "Editada")

        when: "o servico da empresa tentar atualizar"
        vagaService.atualizarVagaDaEmpresa(1, 999, dto)

        then: "uma excecao e disparada e nenhum update ocorre"
        1 * mockVagaDAO.buscarPorId(999) >> null
        0 * mockVagaDAO.atualizar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message == "Vaga não encontrada."
    }

    void "deve proibir uma empresa de editar ou tomar posse de uma vaga que pertence a um concorrente"() {
        given: "uma vaga existente que pertence a empresa 2"
        VagaCadastroDTO dto = new VagaCadastroDTO(nome: "Editada")
        Vaga vagaDeOutra = new Vaga(2, "Vaga B", "Desc B", "RJ", "Rio de Janeiro")
        vagaDeOutra.id = 50

        when: "a empresa 1 tentar sorrateiramente atualiza-la"
        vagaService.atualizarVagaDaEmpresa(1, 50, dto)

        then: "acesso negado"
        1 * mockVagaDAO.buscarPorId(50) >> vagaDeOutra
        0 * mockVagaDAO.atualizar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message == "Esta vaga não pertence à sua empresa."
    }

    void "deve atualizar os detalhes de uma vaga existente desde que ela pertenca legitimamente a empresa"() {
        given: "uma vaga da propria empresa e uma edicao solicitada"
        VagaCadastroDTO dto = new VagaCadastroDTO(competencias: ["C#"])
        Vaga vagaAntiga = new Vaga(1, "Vaga C#", "Velha", "SP", "Sao Paulo")
        vagaAntiga.id = 50
        
        Vaga vagaEditada = new Vaga(1, "Vaga C#", "Velha", "SP", "Sao Paulo")
        vagaEditada.id = 50
        vagaEditada.competencias = ["C#"]

        when: "a empresa pedir para salvar as alteracoes"
        vagaService.atualizarVagaDaEmpresa(1, 50, dto)

        then: "as validacoes de posse passam e os dados sao repassados para update no banco"
        1 * mockVagaDAO.buscarPorId(50) >> vagaAntiga
        1 * mockVagaMapper.paraEntidade(dto, 1) >> vagaEditada
        1 * mockVagaDAO.atualizar(vagaEditada)
        1 * mockCompetenciaDAO.removerVinculosVaga(50)
        1 * mockCompetenciaDAO.buscarOuInserir("C#") >> 77
        1 * mockCompetenciaDAO.vincularAVaga(50, 77)
    }

    void "deve bloquear delecao de vaga se a mesma nao existir"() {
        when: "uma empresa tentar deletar uma vaga fantasma"
        vagaService.deletarVagaDaEmpresa(1, 999)

        then: "erro validado e delecao abortada"
        1 * mockVagaDAO.buscarPorId(999) >> null
        0 * mockVagaDAO.deletar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message == "Vaga não encontrada."
    }

    void "deve impedir ativamente que uma empresa apague a vaga de outra"() {
        given: "uma vaga cadastrada pela empresa concorrente"
        Vaga vagaDeOutra = new Vaga(2, "Vaga", "Desc", "SP", "Sao Paulo")
        vagaDeOutra.id = 50

        when: "a empresa tentar deleta-la"
        vagaService.deletarVagaDaEmpresa(1, 50)

        then: "a exclusao e negada sumariamente"
        1 * mockVagaDAO.buscarPorId(50) >> vagaDeOutra
        0 * mockVagaDAO.deletar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
        erro.message == "Esta vaga não pertence à sua empresa."
    }

    void "deve concluir a delecao da vaga desde que seja proprietaria da mesma"() {
        given: "uma vaga licita da empresa"
        Vaga vagaPropria = new Vaga(1, "Vaga", "Desc", "SP", "Sao Paulo")
        vagaPropria.id = 50

        when: "empresa comandar a delecao"
        vagaService.deletarVagaDaEmpresa(1, 50)

        then: "a vaga e enviada para o escuro do DAO"
        1 * mockVagaDAO.buscarPorId(50) >> vagaPropria
        1 * mockVagaDAO.deletar(50)
    }

    void "deve envelopar erro misterioso de banco de dados e repassar como runtime exception"() {
        given: "uma vaga legitima pronta para salvar"
        VagaCadastroDTO dto = new VagaCadastroDTO(nome: "Vaga")
        Vaga vaga = new Vaga(1, "Vaga", "Desc", "SP", "Sao Paulo")

        when: "mandar adicionar"
        vagaService.adicionarVaga(1, dto)

        then: "banco morre e o erro e traduzido educadamente pelo service"
        1 * mockVagaMapper.paraEntidade(dto, 1) >> vaga
        1 * mockVagaDAO.inserir(vaga) >> { throw new DatabaseOperationException("SQL Timeout") }
        DatabaseOperationException erro = thrown(DatabaseOperationException)
    }

}
