package org.uGustavoDev.controller.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Recrutador
import org.uGustavoDev.service.interfaces.IRecrutadorService

class RecrutadorController extends BaseController {

    private final IRecrutadorService recrutadorService

    RecrutadorController(IRecrutadorService recrutadorService) {
        super()
        this.recrutadorService = recrutadorService
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo()

        try {
            if (pathInfo == null || pathInfo == "/") {
                String empresaIdParam = req.getParameter("empresaId")
                List<Recrutador> recrutadores = empresaIdParam != null ?
                        recrutadorService.listarRecrutadoresPorEmpresa(empresaIdParam as int) :
                        recrutadorService.listarRecrutadores()
                enviarRespostaSucesso(resp, recrutadores ?: [])
                return
            }

            if (pathInfo.startsWith("/")) {
                String idStr = pathInfo.substring(1)
                if (idStr.isInteger()) {
                    Recrutador recrutador = recrutadorService.buscarRecrutadorPorId(idStr as int)
                    enviarRespostaSucesso(resp, recrutador)
                    return
                }
            }

            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint não encontrado.")
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (!validarContentTypeJson(req, resp)) {
            return
        }

        try {
            RecrutadorCadastroDTO dto = lerCorpoRequisicao(req, RecrutadorCadastroDTO)
            Recrutador recrutadorCriado = recrutadorService.adicionarRecrutador(dto)
            enviarRespostaCriado(resp, recrutadorCriado)
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        if (!validarContentTypeJson(req, resp)) {
            return
        }

        String pathInfo = req.getPathInfo()

        try {
            if (pathInfo != null && pathInfo.startsWith("/")) {
                String idStr = pathInfo.substring(1)
                if (idStr.isInteger()) {
                    int id = idStr as int
                    RecrutadorCadastroDTO dto = lerCorpoRequisicao(req, RecrutadorCadastroDTO)
                    Recrutador recrutadorAtualizado = recrutadorService.atualizarRecrutador(id, dto)
                    enviarRespostaSucesso(resp, recrutadorAtualizado)
                    return
                }
            }
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de recrutador inválido para atualização.")
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo()

        try {
            if (pathInfo != null && pathInfo.startsWith("/")) {
                String idStr = pathInfo.substring(1)
                if (idStr.isInteger()) {
                    recrutadorService.deletarRecrutador(idStr as int)
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT)
                    return
                }
            }
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de recrutador inválido.")
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }

}
