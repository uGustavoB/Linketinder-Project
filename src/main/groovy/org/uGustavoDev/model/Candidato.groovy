package org.uGustavoDev.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Candidato extends Pessoa {
    String sobrenome
    String cpf
    LocalDate dataNascimento

    Candidato(String nome, String sobrenome, String email, String estado, String pais, String CEP, String descricao, String cpf, LocalDate dataNascimento) {
        super(nome, email, estado, pais, CEP, descricao)
        this.sobrenome = sobrenome
        this.cpf = cpf
        this.dataNascimento = dataNascimento
    }

    @Override
    String obterDocumento() {
        return this.cpf
    }

    @Override
    String toString() {
        String exibicaoData = dataNascimento != null ? dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Data não informada"
        return """\
CANDIDATO: $nome $sobrenome ($exibicaoData)
Email: $email
Local: $estado, $pais - CEP: $CEP
CPF: $cpf
Descrição: $descricao
Competências: ${competencias.join(', ')}
----------------------------------------"""
    }
}
