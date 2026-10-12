package org.uGustavoDev.controller.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.service.interfaces.ICandidatoService

class CandidatoController extends BaseController {

    private ICandidatoService candidateService

    CandidatoController(ICandidatoService candidateService) {
        super()
        this.candidateService = candidateService
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo()

        if (pathInfo == null || pathInfo == "/") {
            List<Candidato> candidatos = candidateService.listarCandidatos()

            enviarRespostaSucesso(resp, candidatos ?: [])
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (!validarContentTypeJson(req, resp)) {
            return
        }

        try {
            CandidatoCadastroDTO dto = lerCorpoRequisicao(req, CandidatoCadastroDTO)
            Candidato candidatoCriado = candidateService.adicionarCandidato(dto)
            enviarRespostaCriado(resp, candidatoCriado)
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }

}
