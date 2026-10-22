package org.uGustavoDev.mapper

import groovy.transform.CompileStatic
import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.mapper.interfaces.IRecrutadorMapper
import org.uGustavoDev.model.Recrutador

@CompileStatic
class RecrutadorMapper implements IRecrutadorMapper {

    @Override
    Recrutador paraEntidade(RecrutadorCadastroDTO dto) {
        return new RecrutadorBuilder()
                .empresaId(dto.empresaId)
                .nome(dto.nome)
                .area(dto.area)
                .descricao(dto.descricao)
                .email(dto.email)
                .cpf(dto.cpf)
                .dataNascimento(dto.dataNascimento)
                .senha(dto.senha)
                .build()
    }

}
