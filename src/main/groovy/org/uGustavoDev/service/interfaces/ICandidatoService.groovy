package org.uGustavoDev.service.interfaces

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.model.Candidato

interface ICandidatoService {

    void adicionarCandidato(CandidatoCadastroDTO candidato)
    List<Candidato> listarCandidatos()
    Candidato loginCandidato(String email, String senha)
    Candidato atualizarCandidato(int id, CandidatoCadastroDTO dto)
    void deletarCandidato(int id)

}
