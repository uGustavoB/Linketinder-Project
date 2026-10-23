package org.uGustavoDev.dao.interfaces

import org.uGustavoDev.model.Recrutador

interface IRecrutadorDAO {

    void inserir(Recrutador recrutador)

    List<Recrutador> listar()

    List<Recrutador> listarPorEmpresa(int empresaId)

    Recrutador buscarPorId(int id)

    Recrutador buscarPorEmail(String email)

    Recrutador buscarPorCpf(String cpf)

    void atualizar(Recrutador recrutador)

    void deletar(int id)

}
