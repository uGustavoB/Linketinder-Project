package org.uGustavoDev.dto

import java.time.LocalDate

class CandidatoCadastroDTO {

    String nome
    String sobrenome
    String email
    String senha
    String estado
    String pais
    String cep
    String descricao
    String cpf
    LocalDate dataNascimento
    List<String> competencias = []

}
