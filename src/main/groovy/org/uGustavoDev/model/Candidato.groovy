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
}
