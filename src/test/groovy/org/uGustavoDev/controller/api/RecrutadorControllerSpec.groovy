package org.uGustavoDev.controller.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Recrutador
import org.uGustavoDev.service.interfaces.IRecrutadorService
import spock.lang.Specification

import java.time.LocalDate

class RecrutadorControllerSpec extends Specification {

    IRecrutadorService mockRecrutadorService
    RecrutadorController controller
    HttpServletRequest mockRequest
    HttpServletResponse mockResponse
    StringWriter responseWriter

    void setup() {
        mockRecrutadorService = Mock(IRecrutadorService)
        controller = new RecrutadorController(mockRecrutadorService)
        mockRequest = Mock(HttpServletRequest)
        mockResponse = Mock(HttpServletResponse)
        responseWriter = new StringWriter()
        mockResponse.getWriter() >> new PrintWriter(responseWriter)
    }

    void "deve atualizar recrutador com sucesso retornando status 200 OK"() {
        given: "um ID existente e um payload JSON válido"
        String jsonCorpo = '''{
            "empresaId": 1,
            "nome": "Carlos Atualizado",
            "area": "RH Tech",
            "descricao": "Senior Tech Recruiter",
            "email": "carlos.atualizado@empresa.com",
            "cpf": "123.456.789-00",
            "dataNascimento": "1990-05-15",
            "senha": "novaSenha123"
        }'''
        mockRequest.getContentType() >> "application/json"
        mockRequest.getPathInfo() >> "/1"
        mockRequest.getReader() >> new BufferedReader(new StringReader(jsonCorpo))

        Recrutador recrutadorEsperado = new RecrutadorBuilder()
                .id(1)
                .nome("Carlos Atualizado")
                .email("carlos.atualizado@empresa.com")
                .cpf("123.456.789-00")
                .area("RH Tech")
                .descricao("Senior Tech Recruiter")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .empresaId(1)
                .senha("novaSenha123")
                .build()

        when: "o método doPut é acionado"
        controller.doPut(mockRequest, mockResponse)

        then: "o serviço de atualização é chamado com o ID e o DTO correspondentes"
        1 * mockRecrutadorService.atualizarRecrutador(1, _ as RecrutadorCadastroDTO) >> recrutadorEsperado

        and: "o status 200 OK é retornado com o JSON do recrutador atualizado"
        1 * mockResponse.setStatus(HttpServletResponse.SC_OK)
        responseWriter.toString().contains("Carlos Atualizado")
        responseWriter.toString().contains("carlos.atualizado@empresa.com")
    }

    void "deve retornar status 404 quando o recrutador com o ID informado nao for encontrado"() {
        given: "um ID inexistente e payload JSON"
        String jsonCorpo = '''{
            "empresaId": 1,
            "nome": "Carlos Inexistente",
            "area": "RH",
            "descricao": "Recrutador",
            "email": "carlos.inexistente@empresa.com",
            "cpf": "123.456.789-00",
            "dataNascimento": "1990-05-15",
            "senha": "senhaValida123"
        }'''
        mockRequest.getContentType() >> "application/json"
        mockRequest.getPathInfo() >> "/999"
        mockRequest.getReader() >> new BufferedReader(new StringReader(jsonCorpo))

        mockRecrutadorService.atualizarRecrutador(999, _ as RecrutadorCadastroDTO) >> {
            throw new IllegalArgumentException("Recrutador não encontrado para atualização.")
        }

        when: "o método doPut é chamado"
        controller.doPut(mockRequest, mockResponse)

        then: "o status 404 NOT FOUND é retornado"
        1 * mockResponse.setStatus(HttpServletResponse.SC_NOT_FOUND)
        responseWriter.toString().contains("Recrutador não encontrado para atualização.")
    }

    void "deve retornar status 409 quando houver conflito de email ou CPF em uso por outro recrutador"() {
        given: "um payload com e-mail duplicado"
        String jsonCorpo = '''{
            "empresaId": 1,
            "nome": "Carlos",
            "area": "RH",
            "descricao": "Recrutador",
            "email": "ja.existe@empresa.com",
            "cpf": "123.456.789-00",
            "dataNascimento": "1990-05-15",
            "senha": "senhaValida123"
        }'''
        mockRequest.getContentType() >> "application/json"
        mockRequest.getPathInfo() >> "/1"
        mockRequest.getReader() >> new BufferedReader(new StringReader(jsonCorpo))

        mockRecrutadorService.atualizarRecrutador(1, _ as RecrutadorCadastroDTO) >> {
            throw new IllegalArgumentException("Já existe outro recrutador cadastrado com o email 'ja.existe@empresa.com'.")
        }

        when: "o método doPut é acionado"
        controller.doPut(mockRequest, mockResponse)

        then: "o status 409 CONFLICT é retornado"
        1 * mockResponse.setStatus(HttpServletResponse.SC_CONFLICT)
        responseWriter.toString().contains("Já existe outro recrutador cadastrado com o email")
    }

    void "deve retornar status 400 quando o ID no path for invalido ou ausente"() {
        given: "um path sem ID numérico válido"
        mockRequest.getContentType() >> "application/json"
        mockRequest.getPathInfo() >> "/invalido"

        when: "o método doPut é acionado"
        controller.doPut(mockRequest, mockResponse)

        then: "o status 400 BAD REQUEST é retornado"
        1 * mockResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        responseWriter.toString().contains("ID de recrutador inválido para atualização.")
    }

    void "deve retornar status 415 quando o Content-Type nao for application/json"() {
        given: "uma requisição com cabeçalho text/plain"
        mockRequest.getContentType() >> "text/plain"

        when: "o método doPut é acionado"
        controller.doPut(mockRequest, mockResponse)

        then: "o status 415 UNSUPPORTED MEDIA TYPE é retornado"
        1 * mockResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE)
        0 * mockRecrutadorService.atualizarRecrutador(_, _)
    }

    void "deve retornar status 400 quando houver erro de validacao cadastral no corpo da requisicao"() {
        given: "um payload com senha menor que 6 caracteres"
        String jsonCorpo = '''{
            "empresaId": 1,
            "nome": "Carlos",
            "area": "RH",
            "descricao": "Recrutador",
            "email": "carlos@empresa.com",
            "cpf": "123.456.789-00",
            "dataNascimento": "1990-05-15",
            "senha": "123"
        }'''
        mockRequest.getContentType() >> "application/json"
        mockRequest.getPathInfo() >> "/1"
        mockRequest.getReader() >> new BufferedReader(new StringReader(jsonCorpo))

        mockRecrutadorService.atualizarRecrutador(1, _ as RecrutadorCadastroDTO) >> {
            throw new IllegalArgumentException("A senha do recrutador deve conter no mínimo 6 caracteres.")
        }

        when: "o método doPut é acionado"
        controller.doPut(mockRequest, mockResponse)

        then: "o status 400 BAD REQUEST é retornado"
        1 * mockResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        responseWriter.toString().contains("A senha do recrutador deve conter no mínimo 6 caracteres.")
    }

}
