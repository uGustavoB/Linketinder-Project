package org.uGustavoDev.model

import org.uGustavoDev.builder.CandidatoBuilder
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
        Candidato candidato = new CandidatoBuilder()
                .nome(nome)
                .sobrenome(sobrenome)
                .email(email)
                .estado(estado)
                .pais(pais)
                .CEP(cep)
                .descricao(descricao)
                .cpf(cpf)
                .dataNascimento(dataNascimento)
                .build()

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
        Candidato candidato = new CandidatoBuilder()
                .nome("Gustavo")
                .sobrenome("Silva")
                .email("gustavo@example.com")
                .estado("PB")
                .pais("Brasil")
                .CEP("58000-000")
                .descricao("Dev")
                .cpf("111.111.111-11")
                .dataNascimento(LocalDate.of(2005, 1, 1))
                .build()

        List<String> competencias = ["Java", "Python"]

        when: "as competências são adicionadas ao candidato"
        candidato.adicionarCompetencias(competencias)

        then: "a lista de competências do candidato deve ser atualizada"
        candidato.competencias.size() == competencias.size()
        candidato.competencias == competencias
    }

    void "deve permitir adicionar uma única competência"() {
        given: "um candidato"
        Candidato candidato = new CandidatoBuilder()
                .nome("Gustavo")
                .sobrenome("Silva")
                .email("gustavo@example.com")
                .estado("PB")
                .pais("Brasil")
                .CEP("58000-000")
                .descricao("Dev")
                .cpf("111.111.111-11")
                .dataNascimento(LocalDate.of(2005, 1, 1))
                .build()

        when: "uma competência é adicionada ao candidato"
        candidato.adicionarCompetencia("JavaScript")

        then: "a lista de competências do candidato deve ser atualizada"
        candidato.competencias.size() == 1
        candidato.competencias == ["JavaScript"]
    }

    void "deve permitir remover uma competência"() {
        given: "um candidato e uma lista de competências"
        Candidato candidato = new CandidatoBuilder()
                .nome("Gustavo")
                .sobrenome("Silva")
                .email("gustavo@example.com")
                .estado("PB")
                .pais("Brasil")
                .CEP("58000-000")
                .descricao("Dev")
                .cpf("111.111.111-11")
                .dataNascimento(LocalDate.of(2005, 1, 1))
                .build()
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
        Candidato candidato = new CandidatoBuilder()
                .nome("Gustavo")
                .sobrenome("Silva")
                .email("gustavo@example.com")
                .estado("PB")
                .pais("Brasil")
                .CEP("58000-000")
                .descricao("Dev")
                .cpf("111.111.111-11")
                .dataNascimento(LocalDate.of(2005, 1, 1))
                .build()

        expect: "obterDocumento() retorna o CPF correto"
        candidato.obterDocumento() == "111.111.111-11"
    }

    void "nao deve falhar ao tentar remover uma competência inexistente"() {
        given: "um candidato com algumas competências"
        Candidato candidato = new CandidatoBuilder()
                .nome("Gustavo")
                .sobrenome("Silva")
                .email("gustavo@example.com")
                .estado("PB")
                .pais("Brasil")
                .CEP("58000-000")
                .descricao("Dev")
                .cpf("111.111.111-11")
                .dataNascimento(LocalDate.of(2005, 1, 1))
                .competencias(["Java"])
                .build()

        when: "tentamos remover uma competência que nao esta na lista"
        candidato.removerCompetencia("C#")

        then: "a lista original permanece inalterada e nenhuma exceção é lançada"
        candidato.competencias.size() == 1
        candidato.competencias.contains("Java")
    }

    void "deve garantir que toAnonymousString oculte dados pessoais sensiveis preservando anonimato"() {
        given: "um candidato completo com dados pessoais sensíveis"
        Candidato candidato = new CandidatoBuilder()
                .id(42)
                .nome("Maria")
                .sobrenome("Santos")
                .email("maria.santos@email.com")
                .estado("SP")
                .pais("Brasil")
                .CEP("01001-000")
                .descricao("Desenvolvedora Backend")
                .cpf("222.333.444-55")
                .dataNascimento(LocalDate.of(1990, 5, 20))
                .competencias(["Groovy", "Java"])
                .build()

        when: "a representacao anonima e gerada"
        String anonimo = candidato.toAnonymousString()

        then: "informacoes tecnicas e localizacao geral estao presentes"
        anonimo.contains("[#42]")
        anonimo.contains("SP, Brasil")
        anonimo.contains("Desenvolvedora Backend")
        anonimo.contains("Groovy, Java")

        and: "nenhum dado pessoal identificavel (PII) e exposto"
        !anonimo.contains("Maria")
        !anonimo.contains("Santos")
        !anonimo.contains("maria.santos@email.com")
        !anonimo.contains("222.333.444-55")
        !anonimo.contains("01001-000")
        !anonimo.contains("1990")
        !anonimo.contains("20/05/1990")
    }

}

