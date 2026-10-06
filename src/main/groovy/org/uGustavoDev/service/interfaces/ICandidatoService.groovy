package org.uGustavoDev.service.interfaces

import org.uGustavoDev.model.Candidato

interface ICandidatoService {

    void adicionarCandidato(Candidato candidato)
    List<Candidato> listarCandidatos()
    Candidato loginCandidato(String email, String senha)
    void atualizarCandidato(Candidato candidato)
    void deletarCandidato(int id)

}
