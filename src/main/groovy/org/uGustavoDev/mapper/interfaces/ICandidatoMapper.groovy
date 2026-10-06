package org.uGustavoDev.mapper.interfaces

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.model.Candidato

interface ICandidatoMapper {

    Candidato paraEntidade(CandidatoCadastroDTO dto)

}