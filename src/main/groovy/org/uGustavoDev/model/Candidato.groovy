package org.uGustavoDev.model

class Candidato extends Pessoa {
    String cpf
    Integer idade

    Candidato(String nome, String email, String estado, String pais, String CEP, String descricao, String cpf, Integer idade) {
        super(nome, email, estado, pais, CEP, descricao)
        this.cpf = cpf
        this.idade = idade
    }

    @Override
    String obterDocumento() {
        return this.cpf
    }

    @Override
    String toString() {
        return """\
CANDIDATO: $nome ($idade anos)
Email: $email
Local: $estado, $pais - CEP: $CEP
CPF: $cpf
Descrição: $descricao
Competências: ${competencias.join(', ')}
----------------------------------------"""
    }
}
