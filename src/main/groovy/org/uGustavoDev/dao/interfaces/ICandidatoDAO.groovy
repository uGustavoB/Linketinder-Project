package org.uGustavoDev.dao.interfaces

import org.uGustavoDev.model.Candidato

interface ICandidatoDAO {

    void inserir(Candidato candidato)

    List<Candidato> listar()

    Candidato buscarPorId(int id)

    Candidato buscarPorEmail(String email)

    Candidato buscarPorCpf(String cpf)

    void atualizar(Candidato candidato)

    void deletar(int id)

}
