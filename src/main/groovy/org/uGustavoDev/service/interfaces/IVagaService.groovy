package org.uGustavoDev.service.interfaces

import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Vaga

interface IVagaService {

    void adicionarVaga(int empresaId, VagaCadastroDTO dto)
    List<Vaga> listarVagas()
    List<Vaga> listarVagasDaEmpresa(int empresaId)
    Vaga buscarVagaPorId(int id)
    Vaga atualizarVaga(int vagaId, int empresaId, VagaCadastroDTO dto)
    void atualizarVagaDaEmpresa(int empresaId, int vagaId, VagaCadastroDTO dto)
    void deletarVaga(int id)
    void deletarVagaDaEmpresa(int empresaId, int vagaId)

}
