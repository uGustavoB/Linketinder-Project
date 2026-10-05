package org.uGustavoDev.dao.interfaces

import org.uGustavoDev.model.Vaga

interface IVagaDAO {

    void inserir(Vaga vaga)

    List<Vaga> listar()

    List<Vaga> listarPorEmpresa(int empresaId)

    Vaga buscarPorId(int id)

    void atualizar(Vaga vaga)

    void deletar(int id)

}
