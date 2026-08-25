package org.uGustavoDev.model

import spock.lang.Specification

class CandidatoSpec extends Specification {
    def "deve instanciar um novo candidato corretamente"() {
        given: "os dados do candidato"
        String nome = "Gustavo"
        String email = "gustavo@example.com"
        String estado = "PB"
        String pais = "Brasil"
        String cep = "58000-000"
        String descricao = "Desenvolvedor buscando aplicar skills em projetos reais."
        String cpf = "111.111.111-11"
        int idade = 21

        when: "o candidato é instanciado"
        Candidato candidato = new Candidato(nome, email, estado, pais, cep, descricao, cpf, idade)

        then: "os atributos do candidato devem ser corretamente atribuídos"
        candidato.nome == nome
        candidato.email == email
        candidato.estado == estado
        candidato.pais == pais
        candidato.CEP == cep
        candidato.descricao == descricao
        candidato.cpf == cpf
        candidato.idade == idade
        candidato.competencias.isEmpty()
    }

    def "deve permitir adicionar e atualizar a lista de competências"() {
        given: "um candidato e uma lista de competências"
        Candidato candidato = new Candidato("Gustavo", "gustavo@example.com", "PB", "Brasil", "58000-000", "Desenvolvedor buscando aplicar skills em projetos reais.", "111.111.111-11", 21)
        List<String> competencias = ["Java", "Python"]

        when: "as competências são adicionadas ao candidato"
        candidato.adicionarCompetencias(competencias)

        then: "a lista de competências do candidato deve ser atualizada"
        candidato.competencias.size() == competencias.size()
        candidato.competencias == competencias
    }

    def "deve permitir adicionar uma única competência"() {
        given: "um candidato"
        Candidato candidato = new Candidato("Gustavo", "gustavo@example.com", "PB", "Brasil", "58000-000", "Desenvolvedor buscando aplicar skills em projetos reais.", "111.111.111-11", 21)

        when: "uma competência é adicionada ao candidato"
        candidato.adicionarCompetencia("JavaScript")

        then: "a lista de competências do candidato deve ser atualizada"
        candidato.competencias.size() == 1
        candidato.competencias == ["JavaScript"]
    }

    def "deve permitir remover uma competência"() {
        given: "um candidato e uma lista de competências"
        Candidato candidato = new Candidato("Gustavo", "gustavo@example.com", "PB", "Brasil", "58000-000", "Desenvolvedor buscando aplicar skills em projetos reais.", "111.111.111-11", 21)
        List<String> competencias = ["Java", "Python"]

        when: "as competências são adicionadas e uma é removida"
        candidato.adicionarCompetencias(competencias)
        candidato.removerCompetencia("Python")

        then: "a lista de competências do candidato deve ser apenas as competências restantes"
        candidato.competencias.size() == 1
        !candidato.competencias.contains("Python")
    }

    def "deve retornar o CPF ao chamar obterDocumento()"() {
        given: "um candidato com CPF"
        Candidato candidato = new Candidato("Gustavo", "gustavo@example.com", "PB", "Brasil", "58000-000", "Dev", "111.111.111-11", 21)

        expect: "obterDocumento() retorna o CPF correto"
        candidato.obterDocumento() == "111.111.111-11"
    }

    def "deve formatar a representação textual no toString() com sucesso"() {
        given: "um candidato com competências"
        Candidato candidato = new Candidato("Gustavo", "gustavo@example.com", "PB", "Brasil", "58000-000", "Desenvolvedor", "111.111.111-11", 25)
        candidato.adicionarCompetencias(["Groovy", "Spock"])

        when: "o método toString é executado"
        String resultado = candidato.toString()

        then: "a string contém todos os dados formatados do candidato"
        resultado.contains("CANDIDATO: Gustavo (25 anos)")
        resultado.contains("Email: gustavo@example.com")
        resultado.contains("CPF: 111.111.111-11")
        resultado.contains("Competências: Groovy, Spock")
    }
}