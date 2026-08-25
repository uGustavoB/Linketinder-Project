package org.uGustavoDev.services

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.LinketinderService
import spock.lang.Specification

class LinketinderServiceSpec extends Specification{
    LinketinderService service

    def setup() {
        service = new LinketinderService()
    }

    def "deve inicializar com as listas padrão de candidatos e empresas carregadas em memória"() {
        expect:
        service.candidatos.size() == 5
        service.empresas.size() == 5
        service.candidatos.first().nome == "Gustavo"
        service.empresas.first().nome == "Google"
    }

    def "deve adicionar um novo candidato à lista de candidatos"() {
        given: "um novo candidato e o tamanho inicial da lista de candidatos"
        int totalCandidatosInicial = service.candidatos.size()

        String nome = "João"
        String email = "joao@email.com"
        String estado = "SP"
        String pais = "Brasil"
        String cep = "12345-678"
        String descricao = "Descrição do João"
        String cpf = "123.456.789-00"
        int idade = 30

        Candidato novoCandidato = new Candidato(nome, email, estado, pais, cep, descricao, cpf, idade)
        List<String> competencias = ["Groovy", "Spock", "Grails"]
        novoCandidato.adicionarCompetencias(competencias)

        when: "o novo candidato é adicionado à lista"
        service.adicionarCandidato(novoCandidato)

        then: "a lista de candidatos deve conter o novo candidato"
        service.candidatos.size() == totalCandidatosInicial + 1
        def candidatoSalvo = service.candidatos.last()
        candidatoSalvo.nome == nome
        candidatoSalvo.email == email
        candidatoSalvo.estado == estado
        candidatoSalvo.pais == pais
        candidatoSalvo.CEP == cep
        candidatoSalvo.descricao == descricao
        candidatoSalvo.cpf == cpf
        candidatoSalvo.idade == idade
        candidatoSalvo.competencias.size() == competencias.size()
        candidatoSalvo.competencias.containsAll(competencias)
    }

    def "deve permitir a inserção consecutiva de múltiplos candidatos na lista"() {
        given: "uma lista com tamanho inicial conhecido"
        int tamanhoInicial = service.candidatos.size()
        def cand1 = new Candidato("Dev 1", "d1@email.com", "SP", "Brasil", "01000-000", "Desc 1", "111.111.111-01", 21)
        def cand2 = new Candidato("Dev 2", "d2@email.com", "RJ", "Brasil", "20000-000", "Desc 2", "222.222.222-02", 22)

        when: "dois novos candidatos são inseridos consecutivamente"
        service.adicionarCandidato(cand1)
        service.adicionarCandidato(cand2)

        then: "o total de candidatos na memória aumenta exatamente em 2"
        service.candidatos.size() == tamanhoInicial + 2
        service.candidatos.contains(cand1)
        service.candidatos.contains(cand2)
    }

    def "deve adicionar uma nova empresa à lista de empresas"() {
        given: "uma nova empresa e o tamanho inicial da lista de empresas"
        int totalEmpresasInicial = service.empresas.size()

        String nome = "Google"
        String email = "google@google.com"
        String estado = "CA"
        String pais = "USA"
        String cep = "12345-678"
        String descricao = "Empresa de tecnologia."
        String cnpj = "12.345.678/0001-90"
        List<String> competencias = ["Groovy", "Spock", "Grails"]

        Empresa novaEmpresa = new Empresa(nome, email, estado, pais, cep, descricao, cnpj)
        novaEmpresa.adicionarCompetencias(competencias)

        when: "a nova empresa é adicionada à lista"
        service.adicionarEmpresa(novaEmpresa)

        then: "a lista de empresas deve conter a nova empresa"
        service.empresas.size() == totalEmpresasInicial + 1
        service.empresas.contains(novaEmpresa)

        def empresaSalva = service.empresas.last()
        empresaSalva.nome == nome
        empresaSalva.email == email
        empresaSalva.estado == estado
        empresaSalva.pais == pais
        empresaSalva.CEP == cep
        empresaSalva.descricao == descricao
        empresaSalva.cnpj == cnpj
        empresaSalva.competencias.size() == competencias.size()
        empresaSalva.competencias.containsAll(competencias)
    }

    def "deve garantir que a inserção de candidato não altere a lista de empresas e vice-versa"() {
        given: "os tamanhos iniciais das listas de candidatos e empresas"
        int tamanhoInicialCandidatos = service.candidatos.size()
        int tamanhoInicialEmpresas = service.empresas.size()

        def novoCandidato = new Candidato("Dev Teste", "dev@teste.com", "SP", "Brasil", "01000-000", "Desc Teste", "111.111.111-01", 21)
        def novaEmpresa = new Empresa("Empresa Teste", "empresa@teste.com", "RJ", "Brasil", "20000-000", "Descrição Teste", "22.222.222/0001-90")

        when: "um novo candidato e uma nova empresa são adicionados"
        service.adicionarCandidato(novoCandidato)
        service.adicionarEmpresa(novaEmpresa)

        then: "as listas de candidatos e empresas devem refletir apenas suas respectivas adições"
        service.candidatos.size() == tamanhoInicialCandidatos + 1
        service.empresas.size() == tamanhoInicialEmpresas + 1
        service.candidatos.contains(novoCandidato)
        service.empresas.contains(novaEmpresa)

        when: "uma nova empresa é adicionada"
        def outraEmpresa = new Empresa("Outra Empresa", "outra@empresa.com", "MG", "Brasil", "30000-000", "Descrição Outra Empresa", "33.333.333/0001-90")
        service.adicionarEmpresa(outraEmpresa)

        then: "a lista de empresas deve conter a nova empresa"
        service.empresas.size() == tamanhoInicialEmpresas + 2
        service.empresas.contains(outraEmpresa)
    }
}