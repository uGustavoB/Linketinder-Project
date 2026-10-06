package org.uGustavoDev.mapper.interfaces

import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Vaga

interface IVagaMapper {

    Vaga paraEntidade(VagaCadastroDTO dto, int empresaId)

}
