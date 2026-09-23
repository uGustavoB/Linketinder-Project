package org.uGustavoDev.model

class Empresa extends Pessoa {
    String cnpj

    Empresa(String nome, String email, String pais, String CEP, String descricao, String cnpj) {
        super(nome, email, pais, CEP, descricao)
        this.cnpj = cnpj
    }

    @Override
    String obterDocumento() {
        return this.cnpj
    }

    @Override
    String toString() {
        return """\
EMPRESA: $nome
Email: $email
Local: $pais - CEP: $CEP
CNPJ: $cnpj
Descrição: $descricao
Competências: ${competencias.join(', ')}
----------------------------------------"""
    }
}
