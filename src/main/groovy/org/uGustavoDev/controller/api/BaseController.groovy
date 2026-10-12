package org.uGustavoDev.controller.api

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.exceptions.ValidationException

abstract class BaseController extends HttpServlet {

    protected ObjectMapper objectMapper

    BaseController() {
        this.objectMapper = new ObjectMapper()
        this.objectMapper.registerModule(new JavaTimeModule())
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*")
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")
        resp.setStatus(HttpServletResponse.SC_OK)
    }

    protected boolean validarContentTypeJson(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.contentType
        if (contentType == null || !contentType.toLowerCase().contains("application/json")) {
            enviarRespostaErro(
                    response,
                    HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "O cabeçalho Content-Type deve ser application/json"
            )
            return false
        }
        return true
    }

    protected String lerCorpoComoTexto(HttpServletRequest request) {
        try {
            String texto = request.reader?.text?.trim()
            if (!texto) {
                throw new ValidationException("O corpo da requisição não pode estar vazio.")
            }
            return texto
        } catch (IOException e) {
            throw new ValidationException("Falha ao ler o corpo da requisição: " + e.message)
        }
    }

    protected <T> T lerCorpoRequisicao(HttpServletRequest request, Class<T> clazz) {
        String corpo = lerCorpoComoTexto(request)
        try {
            return objectMapper.readValue(corpo, clazz)
        } catch (JsonProcessingException e) {
            throw new ValidationException("Erro ao processar JSON: formato inválido ou dados incompatíveis.")
        }
    }

    protected void enviarRespostaJson(HttpServletResponse response, int status, Object corpo) {
        response.setStatus(status)
        response.setContentType("application/json;charset=UTF-8")

        if (corpo != null) {
            try {
                String json = objectMapper.writeValueAsString(corpo)
                response.writer.write(json)
                response.writer.flush()
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao serializar resposta JSON")
            }
        }
    }

    protected void enviarRespostaSucesso(HttpServletResponse response, Object dados) {
        enviarRespostaJson(response, HttpServletResponse.SC_OK, dados)
    }

    protected void enviarRespostaSemConteudo(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT)
        response.setHeader("Access-Control-Allow-Origin", "*")
    }

    protected void enviarRespostaErro(HttpServletResponse response, int status, String mensagem) {
        Map<String, Object> payloadErro = [
                status: status,
                mensagem: mensagem
        ]
        enviarRespostaJson(response, status, payloadErro)
    }
    
}
