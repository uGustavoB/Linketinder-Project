package org.uGustavoDev.model

class Empresa extends Pessoa {
  String cnpj

  Empresa(String nome, String email, String estado, String pais, String CEP, String descricao, String cnpj) {
    super(nome, email, estado, pais, CEP, descricao)
    this.cnpj = cnpj
  }

  @Override
  String obterDocumento() {
    return this.cnpj
  }
}
