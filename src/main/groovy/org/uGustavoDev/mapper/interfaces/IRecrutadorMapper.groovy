package org.uGustavoDev.mapper.interfaces

import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Recrutador

interface IRecrutadorMapper {

    Recrutador paraEntidade(RecrutadorCadastroDTO dto)

}
