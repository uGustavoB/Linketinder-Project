package org.uGustavoDev.model

import spock.lang.Specification
import java.time.LocalDate

class CandidatoSpec extends Specification {
    void "deve instanciar um novo candidato corretamente"() {
        given: "os dados do candidato"
        String nome = "Gustavo"
        String sobrenome = "Silva"
        String email = "gustavo@example.com"
        String estado = "PB"
        String pais = "Brasil"
        String cep = "58000-000"
        String descricao = "Desenvolvedor buscando aplicar skills em projetos reais."
        String cpf = "111.111.111-11"
        LocalDate dataNascimento = LocalDate.of(2005, 1, 1)

        when: "o candidato é instanciado"
        Candidato candidato = new Candidato(nome, sobrenome, email, estado, pais, cep, descricao, cpf, dataNascimento)

        then: "os atributos do candidato devem ser corretamente atribuídos"
        candidato.nome == nome
        candidato.sobrenome == sobrenome
        candidato.email == email
        candidato.estado == estado
        candidato.pais == pais
        candidato.CEP == cep
        candidato.descricao == descricao
        candidato.cpf == cpf
        candidato.dataNascimento == dataNascimento
        candidato.competencias.isEmpty()
    }

    void "deve permitir adicionar e atualizar a lista de competências"() {
        given: "um candidato e uma lista de competências"
        Candidato candidato = new Candidato("Gustavo", "Silva", "gustavo@example.com", "PB", "Brasil", "58000-000", "Dev", "111.111.111-11", LocalDate.of(2005, 1, 1))
        List<String> competencias = ["Java", "Python"]

        when: "as competências são adicionadas ao candidato"
        candidato.adicionarCompetencias(competencias)

        then: "a lista de competências do candidato deve ser atualizada"
        candidato.competencias.size() == competencias.size()
        candidato.competencias == competencias
    }

    void "deve permitir adicionar uma única competência"() {
        given: "um candidato"
        Candidato candidato = new Candidato("Gustavo", "Silva", "gustavo@example.com", "PB", "Brasil", "58000-000", "Dev", "111.111.111-11", LocalDate.of(2005, 1, 1))

        when: "uma competência é adicionada ao candidato"
        candidato.adicionarCompetencia("JavaScript")

        then: "a lista de competências do candidato deve ser atualizada"
        candidato.competencias.size() == 1
        candidato.competencias == ["JavaScript"]
    }

    void "deve permitir remover uma competência"() {
        given: "um candidato e uma lista de competências"
        Candidato candidato = new Candidato("Gustavo", "Silva", "gustavo@example.com", "PB", "Brasil", "58000-000", "Dev", "111.111.111-11", LocalDate.of(2005, 1, 1))
        List<String> competencias = ["Java", "Python"]

        when: "as competências são adicionadas e uma é removida"
        candidato.adicionarCompetencias(competencias)
        candidato.removerCompetencia("Python")

        then: "a lista de competências do candidato deve ser apenas as competências restantes"
        candidato.competencias.size() == 1
        !candidato.competencias.contains("Python")
    }

    void "deve retornar o CPF ao chamar obterDocumento()"() {
        given: "um candidato com CPF"
        Candidato candidato = new Candidato("Gustavo", "Silva", "gustavo@example.com", "PB", "Brasil", "58000-000", "Dev", "111.111.111-11", LocalDate.of(2005, 1, 1))

        expect: "obterDocumento() retorna o CPF correto"
        candidato.obterDocumento() == "111.111.111-11"
    }

    void "nao deve falhar ao tentar remover uma competência inexistente"() {
        given: "um candidato com algumas competências"
        Candidato candidato = new Candidato("Gustavo", "Silva", "gustavo@example.com", "PB", "Brasil", "58000-000", "Dev", "111.111.111-11", LocalDate.of(2005, 1, 1))
        candidato.adicionarCompetencia("Java")

        when: "tentamos remover uma competência que nao esta na lista"
        candidato.removerCompetencia("C#")

        then: "a lista original permanece inalterada e nenhuma exceção é lançada"
        candidato.competencias.size() == 1
        candidato.competencias.contains("Java")
    }
}