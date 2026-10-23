package org.uGustavoDev.controller.view

import org.uGustavoDev.builder.EmpresaBuilder
import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.builder.VagaBuilder
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Recrutador
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IEmpresaService
import org.uGustavoDev.service.interfaces.IRecrutadorService
import org.uGustavoDev.service.interfaces.IVagaService
import spock.lang.Specification

class RecrutadorControllerSpec extends Specification {

    IRecrutadorService mockRecrutadorService = Mock(IRecrutadorService)
    IVagaService mockVagaService = Mock(IVagaService)
    ICandidatoService mockCandidatoService = Mock(ICandidatoService)
    IEmpresaService mockEmpresaService = Mock(IEmpresaService)

    RecrutadorController controller = new RecrutadorController(
            mockRecrutadorService,
            mockVagaService,
            mockCandidatoService,
            mockEmpresaService
    )

    void "deve autenticar recrutador delegando para o servico"() {
        given: "credenciais de login"
        String email = "recrutador@empresa.com"
        String senha = "senha"
        Recrutador recrutador = new RecrutadorBuilder().email(email).build()

        when: "o login e executado no controller"
        Recrutador resultado = controller.login(email, senha)

        then: "o servico correspondente e invocado e o resultado retornado"
        1 * mockRecrutadorService.loginRecrutador(email, senha) >> recrutador
        resultado == recrutador
    }

    void "deve atualizar recrutador delegando para o servico"() {
        given: "um DTO com dados atualizados"
        int id = 5
        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(nome: "Novo Nome")
        Recrutador atualizado = new RecrutadorBuilder().id(id).nome("Novo Nome").build()

        when: "a atualizacao e solicitada"
        Recrutador resultado = controller.atualizarRecrutador(id, dto)

        then: "o servico de atualizacao e acionado"
        1 * mockRecrutadorService.atualizarRecrutador(id, dto) >> atualizado
        resultado == atualizado
    }

    void "deve buscar empresa por id filtrando a listagem de empresas"() {
        given: "empresas cadastradas no servico"
        Empresa emp1 = new EmpresaBuilder().id(1).nome("Empresa Um").build()
        Empresa emp2 = new EmpresaBuilder().id(2).nome("Empresa Dois").build()

        when: "buscamos a empresa pelo ID 2"
        Empresa resultado = controller.buscarEmpresa(2)

        then: "a empresa correta e localizada"
        1 * mockEmpresaService.listarEmpresas() >> [emp1, emp2]
        resultado == emp2
    }

    void "deve delegar operacoes de gestao de vagas para o servico de vagas"() {
        given: "dados de vaga e IDs de contexto"
        int empresaId = 1
        int vagaId = 10
        VagaCadastroDTO dto = new VagaCadastroDTO(nome: "Dev Groovy", descricao: "Vaga Pleno")
        List<Vaga> vagas = [new VagaBuilder().id(vagaId).empresaId(empresaId).build()]

        when: "as acoes de vaga sao executadas pelo recrutador"
        controller.adicionarVaga(empresaId, dto)
        List<Vaga> listadas = controller.listarVagasDaEmpresa(empresaId)
        controller.atualizarVagaDaEmpresa(empresaId, vagaId, dto)
        controller.deletarVagaDaEmpresa(empresaId, vagaId)

        then: "as chamadas sao repassadas com sucesso ao vagaService"
        1 * mockVagaService.adicionarVaga(empresaId, dto)
        1 * mockVagaService.listarVagasDaEmpresa(empresaId) >> vagas
        1 * mockVagaService.atualizarVagaDaEmpresa(empresaId, vagaId, dto)
        1 * mockVagaService.deletarVagaDaEmpresa(empresaId, vagaId)
        listadas == vagas
    }

    void "deve delegar listagem de candidatos para o servico de candidatos"() {
        given: "candidatos no sistema"
        List<Candidato> candidatos = []

        when: "o recrutador lista os candidatos"
        List<Candidato> resultado = controller.listarCandidatos()

        then: "o servico de candidatos e acionado retornando a lista"
        1 * mockCandidatoService.listarCandidatos() >> candidatos
        resultado == candidatos
    }

}
