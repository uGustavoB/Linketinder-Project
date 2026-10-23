package org.uGustavoDev.model

import org.uGustavoDev.builder.RecrutadorBuilder
import spock.lang.Specification
import java.time.LocalDate

class RecrutadorSpec extends Specification {

    void "deve instanciar um recrutador corretamente utilizando o builder"() {
        given: "dados válidos para um recrutador"
        Integer empresaId = 2
        String nome = "Carlos Souza"
        String area = "RH"
        String descricao = "Recrutador Sênior de Talentos"
        String email = "carlos.souza@empresa.com"
        String cpf = "987.654.321-99"
        LocalDate dataNascimento = LocalDate.of(1985, 10, 15)
        String senha = "senhaSegura"

        when: "o recrutador é construído"
        Recrutador recrutador = new RecrutadorBuilder()
                .empresaId(empresaId)
                .nome(nome)
                .area(area)
                .descricao(descricao)
                .email(email)
                .cpf(cpf)
                .dataNascimento(dataNascimento)
                .senha(senha)
                .build()

        then: "todos os campos devem estar corretamente preenchidos"
        recrutador.empresaId == empresaId
        recrutador.nome == nome
        recrutador.area == area
        recrutador.descricao == descricao
        recrutador.email == email
        recrutador.cpf == cpf
        recrutador.dataNascimento == dataNascimento
        recrutador.senha == senha
    }

}

