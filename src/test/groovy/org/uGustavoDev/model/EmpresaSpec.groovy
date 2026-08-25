package org.uGustavoDev.model

import spock.lang.Specification

class EmpresaSpec extends Specification{
    def "deve instanciar uma nova empresa corretamente"() {
        given: "os dados da empresa"
        String nome = "Google"
        String email = "google@google.com"
        String estado = "CA"
        String pais = "USA"
        String cep = "12345-678"
        String descricao = "Empresa de tecnologia."
        String cnpj = "12.345.678/0001-90"

        when: "uma nova empresa é instanciada"
        Empresa empresa = new Empresa(nome, email, estado, pais, cep, descricao, cnpj)

        then: "a empresa é instanciada com os dados corretos"
        empresa.nome == nome
        empresa.email == email
        empresa.estado == estado
        empresa.pais == pais
        empresa.CEP == cep
        empresa.descricao == descricao
        empresa.cnpj == cnpj
        empresa.competencias.isEmpty()
    }

    def "deve permitir adicionar e atualizar a lista de competências"() {
        given: "uma empresa e uma lista de competências"
        Empresa empresa = new Empresa("Google", "google@google.com", "CA", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")
        List<String> competencias = ["Java", "Python", "JavaScript"]

        when: "as competências são adicionadas à empresa"
        empresa.adicionarCompetencias(competencias)

        then: "a lista de competências é atualizada corretamente"
        empresa.competencias.size() == competencias.size()
        empresa.competencias == competencias
    }

    def "deve permitir adicionar uma única competência"() {
        given: "uma empresa"
        Empresa empresa = new Empresa("Google", "google@google.com", "CA", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")

        when: "uma competência é adicionada à empresa"
        empresa.adicionarCompetencia("Go")

        then: "a lista de competências é atualizada corretamente"
        empresa.competencias.size() == 1
        empresa.competencias.contains("Go")
    }

    def "deve permitir remover uma competência"() {
        given: "uma empresa com competências e uma lista de competências"
        Empresa empresa = new Empresa("Google", "google@google.com", "CA", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")
        List<String> competencias = ["Java", "Python", "JavaScript"]
        empresa.adicionarCompetencias(competencias)

        when: "uma competência é removida da empresa"
        empresa.competencias.remove("Python")

        then: "a lista de competências é atualizada corretamente"
        empresa.competencias.size() == competencias.size() - 1
        !empresa.competencias.contains("Python")
    }

    def "deve retornar o CNPJ ao chamar obterDocumento()"() {
        given: "uma empresa com CNPJ"
        Empresa empresa = new Empresa("Google", "google@google.com", "CA", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")

        expect: "obterDocumento() retorna o CNPJ correto"
        empresa.obterDocumento() == "12.345.678/0001-90"
    }

    def "deve formatar a representação textual no toString() com sucesso"() {
        given: "uma empresa com competências"
        Empresa empresa = new Empresa("Google", "google@google.com", "CA", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")
        empresa.adicionarCompetencias(["Java", "Go"])

        when: "o método toString é executado"
        String resultado = empresa.toString()

        then: "a string contém todos os dados formatados da empresa"
        resultado.contains("EMPRESA: Google")
        resultado.contains("Email: google@google.com")
        resultado.contains("CNPJ: 12.345.678/0001-90")
        resultado.contains("Competências: Java, Go")
    }
}