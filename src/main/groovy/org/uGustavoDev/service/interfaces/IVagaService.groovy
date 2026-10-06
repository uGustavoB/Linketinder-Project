package org.uGustavoDev.service.interfaces

import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Vaga

interface IVagaService {

    void adicionarVaga(VagaCadastroDTO vaga)
    List<Vaga> listarVagas()
    Vaga buscarVagaPorId(int id)
    void atualizarVaga(VagaCadastroDTO vaga)
    void deletarVaga(int id)
}