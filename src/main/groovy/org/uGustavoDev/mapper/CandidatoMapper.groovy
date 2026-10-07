package org.uGustavoDev.mapper

import org.uGustavoDev.builder.CandidatoBuilder
import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.model.Candidato

class CandidatoMapper implements ICandidatoMapper {

    @Override
    Candidato paraEntidade(CandidatoCadastroDTO dto) {
        return new CandidatoBuilder()
                .nome(dto.nome)
                .sobrenome(dto.sobrenome)
                .email(dto.email)
                .estado(dto.estado)
                .pais(dto.pais)
                .CEP(dto.cep)
                .descricao(dto.descricao)
                .cpf(dto.cpf)
                .dataNascimento(dto.dataNascimento)
                .competencias(dto.competencias)
                .build()
    }

}
