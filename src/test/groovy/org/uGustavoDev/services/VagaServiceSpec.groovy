package org.uGustavoDev.services

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.VagaService
import spock.lang.Specification

class VagaServiceSpec extends Specification {

    VagaService vagaService

    void setup() {
        vagaService = new VagaService()
        GroovyMock(VagaDAO, global: true)
        GroovyMock(CompetenciaDAO, global: true)
    }

    void "deve adicionar uma vaga e registrar suas competencias no banco de dados"() {
        given: "uma vaga com competencias definidas"
        Vaga novaVaga = new Vaga(1, "Engenheiro de Software", "Remoto", "SP", "São Paulo")
        novaVaga.id = 100
        novaVaga.competencias = ["Groovy", "SQL"]

        when: "tentamos adicionar essa vaga pelo service"
        vagaService.adicionarVaga(novaVaga)

        then: "o DAO eh acionado corretamente para persistir a vaga e seus vinculos"
        1 * VagaDAO.inserir(novaVaga)
        1 * CompetenciaDAO.buscarOuInserir("Groovy") >> 10
        1 * CompetenciaDAO.vincularAVaga(100, 10)
        1 * CompetenciaDAO.buscarOuInserir("SQL") >> 20
        1 * CompetenciaDAO.vincularAVaga(100, 20)
    }

    void "deve retornar a lista global de todas as vagas cadastradas"() {
        given: "vagas existentes no banco"
        Vaga v1 = new Vaga(1, "Vaga 1", "Desc", "SP", "Capital")
        Vaga v2 = new Vaga(2, "Vaga 2", "Desc 2", "RJ", "Capital")
        List<Vaga> listaGlobal = [v1, v2]

        when: "solicitamos a listagem geral"
        List<Vaga> retorno = vagaService.listarVagas()

        then: "todas as vagas sao trazidas fielmente"
        1 * VagaDAO.listar() >> listaGlobal
        retorno.size() == 2
    }

    void "deve retornar a lista de vagas restrita a uma empresa especifica"() {
        given: "vagas apenas da empresa 5"
        Vaga v1 = new Vaga(5, "Vaga Empresa 5", "Desc", "SP", "Campinas")
        List<Vaga> vagasEmpresa = [v1]

        when: "pedimos as vagas da empresa 5"
        List<Vaga> retorno = vagaService.listarVagasDaEmpresa(5)

        then: "o DAO recebe o filtro por id da empresa"
        1 * VagaDAO.listarPorEmpresa(5) >> vagasEmpresa
        retorno.size() == 1
    }

    void "deve encontrar e retornar uma vaga pesquisando pelo seu id"() {
        given: "uma vaga qualquer"
        Vaga v1 = new Vaga(1, "Backend", "Home Office", "SC", "Florianopolis")

        when: "buscamos a vaga 10"
        Vaga retorno = vagaService.buscarVagaPorId(10)

        then: "a vaga correspondente e retornada"
        1 * VagaDAO.buscarPorId(10) >> v1
        retorno == v1
    }

    void "deve bloquear tentativa de edicao de vaga caso ela nao exista no banco"() {
        given: "dados de uma vaga para atualizacao"
        Vaga vagaEditada = new Vaga(1, "Falso Backend", "Desc", "SP", "SP")

        when: "tentamos forcar uma edicao de uma vaga inexistente"
        vagaService.atualizarVagaDaEmpresa(1, 999, vagaEditada)

        then: "o banco retorna nulo e a operacao e barrada imediatamente"
        1 * VagaDAO.buscarPorId(999) >> null
        0 * VagaDAO.atualizar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
    }

    void "deve proibir uma empresa de editar ou tomar posse de uma vaga que pertence a um concorrente"() {
        given: "uma vaga que sabidamente pertence a empresa 2"
        Vaga vagaDoConcorrente = new Vaga(2, "Vaga Original", "Desc", "RJ", "Rio")
        Vaga tentativaDeEdicao = new Vaga(1, "Vaga Hackeada", "Nova", "SP", "SP")

        when: "a empresa 1 tenta editar a vaga da empresa 2"
        vagaService.atualizarVagaDaEmpresa(1, 10, tentativaDeEdicao)

        then: "a regra de negocio impede e estampa o erro"
        1 * VagaDAO.buscarPorId(10) >> vagaDoConcorrente
        0 * VagaDAO.atualizar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
    }

    void "deve atualizar os detalhes de uma vaga existente desde que ela pertenca legitimamente a empresa"() {
        given: "uma vaga legitima da empresa 1"
        Vaga vagaLegitima = new Vaga(1, "Vaga Original", "Desc", "SP", "SP")
        vagaLegitima.id = 50
        
        Vaga edicaoPermitida = new Vaga(1, "Nova Vaga", "Nova Desc", "SP", "Campinas")
        edicaoPermitida.competencias = ["Go"]

        when: "a empresa solicita edicao de sua propria vaga"
        vagaService.atualizarVagaDaEmpresa(1, 50, edicaoPermitida)

        then: "os dados sao passados ao DAO para regravacao juntamente as competencias"
        1 * VagaDAO.buscarPorId(50) >> vagaLegitima
        1 * VagaDAO.atualizar(edicaoPermitida)
        1 * CompetenciaDAO.removerVinculosVaga(50)
        1 * CompetenciaDAO.buscarOuInserir("Go") >> 5
        1 * CompetenciaDAO.vincularAVaga(50, 5)
    }

    void "deve bloquear delecao de vaga se a mesma nao existir"() {
        when: "tentar deletar algo fantasma"
        vagaService.deletarVagaDaEmpresa(1, 999)

        then: "receber block imediato"
        1 * VagaDAO.buscarPorId(999) >> null
        0 * VagaDAO.deletar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
    }

    void "deve impedir ativamente que uma empresa apague a vaga de outra"() {
        given: "uma vaga pertencente a empresa 2"
        Vaga vagaConcorrente = new Vaga(2, "Vaga Alheia", "Desc", "MG", "BH")

        when: "a empresa 1 manda um comando de drop na vaga"
        vagaService.deletarVagaDaEmpresa(1, 10)

        then: "o perigo e interceptado e ninguem se machuca"
        1 * VagaDAO.buscarPorId(10) >> vagaConcorrente
        0 * VagaDAO.deletar(_)
        IllegalArgumentException erro = thrown(IllegalArgumentException)
    }

    void "deve concluir a delecao da vaga desde que seja proprietaria da mesma"() {
        given: "uma vaga legitima"
        Vaga vagaLegitima = new Vaga(1, "Minha Vaga", "Desc", "SP", "SP")

        when: "a proprietaria pede exclusao"
        vagaService.deletarVagaDaEmpresa(1, 10)

        then: "a vontade e respeitada e o id e repassado pro banco"
        1 * VagaDAO.buscarPorId(10) >> vagaLegitima
        1 * VagaDAO.deletar(10)
    }

    void "deve envelopar erro misterioso de banco de dados e repassar como runtime exception"() {
        given: "uma falha grave de infra"
        Vaga novaVaga = new Vaga(1, "X", "Y", "Z", "W")
        
        when: "ocorre interacao com o servico"
        vagaService.adicionarVaga(novaVaga)

        then: "a falha grossa vira uma RuntimeException polida"
        1 * VagaDAO.inserir(_) >> { throw new Exception("Tabela nao existe") }
        RuntimeException erro = thrown(RuntimeException)
    }
}
