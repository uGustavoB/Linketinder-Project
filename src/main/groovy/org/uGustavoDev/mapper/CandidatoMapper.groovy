package org.uGustavoDev.mapper

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.model.Candidato

class CandidatoMapper implements ICandidatoMapper {

    @Override
    Candidato paraEntidade(CandidatoCadastroDTO dto) {
        Candidato candidato = new Candidato(
                dto.nome,
                dto.sobrenome,
                dto.email,
                dto.estado,
                dto.pais,
                dto.cep,
                dto.descricao,
                dto.cpf,
                dto.dataNascimento
        )
        candidato.competencias = dto.competencias
        return candidato
    }

}
