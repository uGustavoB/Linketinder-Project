package org.uGustavoDev.controller.view

import groovy.transform.CompileStatic
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

@CompileStatic
class RecrutadorController {

    private final IRecrutadorService recrutadorService
    private final IVagaService vagaService
    private final ICandidatoService candidatoService
    private final IEmpresaService empresaService

    RecrutadorController(
            IRecrutadorService recrutadorService,
            IVagaService vagaService,
            ICandidatoService candidatoService,
            IEmpresaService empresaService = null) {
        this.recrutadorService = recrutadorService
        this.vagaService = vagaService
        this.candidatoService = candidatoService
        this.empresaService = empresaService
    }

    Recrutador login(String email, String senha) {
        return recrutadorService.loginRecrutador(email, senha)
    }

    Recrutador atualizarRecrutador(int id, RecrutadorCadastroDTO dto) {
        return recrutadorService.atualizarRecrutador(id, dto)
    }

    Empresa buscarEmpresa(int empresaId) {
        return empresaService?.listarEmpresas()?.find { Empresa emp -> emp.id == empresaId }
    }

    void adicionarVaga(int empresaId, VagaCadastroDTO dto) {
        vagaService.adicionarVaga(empresaId, dto)
    }

    List<Vaga> listarVagasDaEmpresa(int empresaId) {
        return vagaService.listarVagasDaEmpresa(empresaId)
    }

    void atualizarVagaDaEmpresa(int empresaId, int vagaId, VagaCadastroDTO dto) {
        vagaService.atualizarVagaDaEmpresa(empresaId, vagaId, dto)
    }

    void deletarVagaDaEmpresa(int empresaId, int vagaId) {
        vagaService.deletarVagaDaEmpresa(empresaId, vagaId)
    }

    List<Candidato> listarCandidatos() {
        return candidatoService.listarCandidatos()
    }

}
