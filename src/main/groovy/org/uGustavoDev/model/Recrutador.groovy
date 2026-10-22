package org.uGustavoDev.model

import groovy.transform.CompileStatic
import java.time.LocalDate

@CompileStatic
class Recrutador {

    Integer id
    Integer empresaId
    String nome
    String area
    String descricao
    String email
    String cpf
    LocalDate dataNascimento
    String senha

    Recrutador(
            Integer empresaId,
            String nome,
            String area,
            String descricao,
            String email,
            String cpf,
            LocalDate dataNascimento,
            String senha
    ) {
        this.empresaId = empresaId
        this.nome = nome
        this.area = area
        this.descricao = descricao
        this.email = email
        this.cpf = cpf
        this.dataNascimento = dataNascimento
        this.senha = senha
    }

    Recrutador(
            Integer id,
            Integer empresaId,
            String nome,
            String area,
            String descricao,
            String email,
            String cpf,
            LocalDate dataNascimento,
            String senha
    ) {
        this.id = id
        this.empresaId = empresaId
        this.nome = nome
        this.area = area
        this.descricao = descricao
        this.email = email
        this.cpf = cpf
        this.dataNascimento = dataNascimento
        this.senha = senha
    }
}
