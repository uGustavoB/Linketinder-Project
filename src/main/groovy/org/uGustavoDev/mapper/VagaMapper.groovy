package org.uGustavoDev.mapper

import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.mapper.interfaces.IVagaMapper
import org.uGustavoDev.model.Vaga

class VagaMapper implements IVagaMapper {

    @Override
    Vaga paraEntidade(VagaCadastroDTO dto, int empresaId) {
        Vaga vaga = new Vaga(
                empresaId,
                dto.nome,
                dto.descricao,
                dto.estado,
                dto.cidade
        )
        vaga.competencias = dto.competencias
        return vaga
    }

}
