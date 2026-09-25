package org.uGustavoDev.model

import spock.lang.Specification

class EmpresaSpec extends Specification {
    void "deve instanciar uma nova empresa corretamente"() {
        given: "os dados da empresa"
        String nome = "Google"
        String email = "google@google.com"
        String pais = "USA"
        String cep = "12345-678"
        String descricao = "Empresa de tecnologia."
        String cnpj = "12.345.678/0001-90"

        when: "uma nova empresa é instanciada"
        Empresa empresa = new Empresa(nome, email, pais, cep, descricao, cnpj)

        then: "a empresa é instanciada com os dados corretos"
        empresa.nome == nome
        empresa.email == email
        empresa.pais == pais
        empresa.CEP == cep
        empresa.descricao == descricao
        empresa.cnpj == cnpj
        empresa.competencias.isEmpty()
    }

    void "deve permitir adicionar e atualizar a lista de competências"() {
        given: "uma empresa e uma lista de competências"
        Empresa empresa = new Empresa("Google", "google@google.com", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")
        List<String> competencias = ["Java", "Python", "JavaScript"]

        when: "as competências são adicionadas à empresa"
        empresa.adicionarCompetencias(competencias)

        then: "a lista de competências é atualizada corretamente"
        empresa.competencias.size() == competencias.size()
        empresa.competencias == competencias
    }

    void "deve permitir adicionar uma única competência"() {
        given: "uma empresa"
        Empresa empresa = new Empresa("Google", "google@google.com", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")

        when: "uma competência é adicionada à empresa"
        empresa.adicionarCompetencia("Go")

        then: "a lista de competências é atualizada corretamente"
        empresa.competencias.size() == 1
        empresa.competencias.contains("Go")
    }

    void "deve permitir remover uma competência"() {
        given: "uma empresa com competências e uma lista de competências"
        Empresa empresa = new Empresa("Google", "google@google.com", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")
        List<String> competencias = ["Java", "Python", "JavaScript"]
        empresa.adicionarCompetencias(competencias)

        when: "uma competência é removida da empresa"
        empresa.removerCompetencia("Python")

        then: "a lista de competências é atualizada corretamente"
        empresa.competencias.size() == competencias.size() - 1
        !empresa.competencias.contains("Python")
    }

    void "deve retornar o CNPJ ao chamar obterDocumento()"() {
        given: "uma empresa com CNPJ"
        Empresa empresa = new Empresa("Google", "google@google.com", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")

        expect: "obterDocumento() retorna o CNPJ correto"
        empresa.obterDocumento() == "12.345.678/0001-90"
    }

    void "nao deve falhar ao tentar remover uma competência inexistente"() {
        given: "uma empresa com algumas competências"
        Empresa empresa = new Empresa("Google", "google@google.com", "USA", "12345-678", "Empresa de tecnologia.", "12.345.678/0001-90")
        empresa.adicionarCompetencia("Java")

        when: "tentamos remover uma competência que nao esta na lista"
        empresa.removerCompetencia("C#")

        then: "a lista original permanece inalterada e nenhuma exceção é lançada"
        empresa.competencias.size() == 1
        empresa.competencias.contains("Java")
    }
}