package org.uGustavoDev.dto

import groovy.transform.CompileStatic
import java.time.LocalDate

@CompileStatic
class RecrutadorCadastroDTO {

    Integer empresaId
    String nome
    String area
    String descricao
    String email
    String cpf
    LocalDate dataNascimento
    String senha

}
