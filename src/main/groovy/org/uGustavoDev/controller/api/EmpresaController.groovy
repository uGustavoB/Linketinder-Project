package org.uGustavoDev.controller.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.interfaces.IEmpresaService

class EmpresaController extends BaseController {

    private IEmpresaService empresaService

    EmpresaController(IEmpresaService empresaService) {
        super()
        this.empresaService = empresaService
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo()

        if (pathInfo == null || pathInfo == "/") {
            List<Empresa> empresas = empresaService.listarEmpresas()

            enviarRespostaSucesso(resp, empresas ?: [])
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (!validarContentTypeJson(req, resp)) {
            return
        }

        try {
            EmpresaCadastroDTO dto = lerCorpoRequisicao(req, EmpresaCadastroDTO)
            Empresa empresaCriada = empresaService.adicionarEmpresa(dto)
            enviarRespostaCriado(resp, empresaCriada)
        } catch (Exception e) {
            tratarExcecao(resp, e)
        }
    }

}
