package org.uGustavoDev.builder

import groovy.transform.CompileStatic
import org.uGustavoDev.model.Recrutador
import java.time.LocalDate

@CompileStatic
class RecrutadorBuilder {

    private Integer id
    private Integer empresaId = 1
    private String nome = "Recrutador Teste"
    private String area = "TI"
    private String descricao = "Recrutador especialista em tecnologia"
    private String email = "recrutador@empresa.com"
    private String cpf = "123.456.789-00"
    private LocalDate dataNascimento = LocalDate.of(1990, 5, 20)
    private String senha = "senha123"

    RecrutadorBuilder id(Integer id) {
        this.id = id
        return this
    }

    RecrutadorBuilder empresaId(Integer empresaId) {
        this.empresaId = empresaId
        return this
    }

    RecrutadorBuilder nome(String nome) {
        this.nome = nome
        return this
    }

    RecrutadorBuilder area(String area) {
        this.area = area
        return this
    }

    RecrutadorBuilder descricao(String descricao) {
        this.descricao = descricao
        return this
    }

    RecrutadorBuilder email(String email) {
        this.email = email
        return this
    }

    RecrutadorBuilder cpf(String cpf) {
        this.cpf = cpf
        return this
    }

    RecrutadorBuilder dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento
        return this
    }

    RecrutadorBuilder senha(String senha) {
        this.senha = senha
        return this
    }

    Recrutador build() {
        return new Recrutador(id, empresaId, nome, area, descricao, email, cpf, dataNascimento, senha)
    }
}
