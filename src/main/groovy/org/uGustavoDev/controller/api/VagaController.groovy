package org.uGustavoDev.controller.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.interfaces.IVagaService

class VagaController extends BaseController {

    private IVagaService vagaService

    VagaController(IVagaService vagaService) {
        super()
        this.vagaService = vagaService
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo()

        if (pathInfo == null || pathInfo == "/") {
            List<Vaga> vagas = vagaService.listarVagas()

            enviarRespostaSucesso(resp, vagas ?: [])
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (!validarContentTypeJson(req, resp)) {
            return
        }

        try {
            VagaCadastroDTO dto = lerCorpoRequisicao(req, VagaCadastroDTO)
            Vaga vagaCriada = vagaService.adicionarVaga(dto)
            enviarRespostaCriado(resp, vagaCriada)
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }
}
