package org.uGustavoDev.service.interfaces

import org.uGustavoDev.model.Vaga

interface IVagaService {

    void adicionarVaga(Vaga vaga)
    List<Vaga> listarVagas()
    Vaga buscarVagaPorId(int id)
    void atualizarVaga(Vaga vaga)
    void deletarVaga(int id)
}