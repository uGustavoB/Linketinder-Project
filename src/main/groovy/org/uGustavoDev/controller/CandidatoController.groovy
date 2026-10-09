package org.uGustavoDev.controller

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IVagaService

class CandidatoController {

    private final ICandidatoService service
    private final IVagaService vagaService

    CandidatoController(ICandidatoService service, IVagaService vagaService) {
        this.service = service
        this.vagaService = vagaService
    }

    Candidato login(String email, String senha) {
        return service.loginCandidato(email, senha)
    }

    void cadastrarCandidato(CandidatoCadastroDTO dto) {
        service.adicionarCandidato(dto)
    }

    Candidato atualizarCandidato(int id, CandidatoCadastroDTO dto) {
        return service.atualizarCandidato(id, dto)
    }

    void deletarCandidato(int id) {
        service.deletarCandidato(id)
    }

    List<Vaga> listarVagas() {
        return vagaService.listarVagas()
    }

}
