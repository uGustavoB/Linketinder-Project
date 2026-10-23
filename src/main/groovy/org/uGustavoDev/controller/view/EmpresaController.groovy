package org.uGustavoDev.controller.view

import org.uGustavoDev.dto.EmpresaCadastroDTO
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

class EmpresaController {

    private final IEmpresaService empresaService
    private final IVagaService vagaService
    private final ICandidatoService candidatoService
    private final IRecrutadorService recrutadorService

    EmpresaController(
            IEmpresaService empresaService,
            IVagaService vagaService,
            ICandidatoService candidatoService,
            IRecrutadorService recrutadorService = null) {
        this.empresaService = empresaService
        this.vagaService = vagaService
        this.candidatoService = candidatoService
        this.recrutadorService = recrutadorService
    }

    Empresa login(String email, String senha) {
        return empresaService.loginEmpresa(email, senha)
    }

    void cadastrarEmpresa(EmpresaCadastroDTO dto) {
        empresaService.adicionarEmpresa(dto)
    }

    Empresa atualizarEmpresa(int id, EmpresaCadastroDTO dto) {
        return empresaService.atualizarEmpresa(id, dto)
    }

    void deletarEmpresa(int id) {
        empresaService.deletarEmpresa(id)
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

    void cadastrarRecrutador(RecrutadorCadastroDTO dto) {
        recrutadorService.adicionarRecrutador(dto)
    }

    List<Recrutador> listarRecrutadoresDaEmpresa(int empresaId) {
        return recrutadorService.listarRecrutadoresPorEmpresa(empresaId)
    }

    void deletarRecrutadorDaEmpresa(int empresaId, int recrutadorId) {
        Recrutador recrutador = recrutadorService.buscarRecrutadorPorId(recrutadorId)
        if (recrutador == null || recrutador.empresaId != empresaId) {
            throw new IllegalArgumentException("Recrutador não pertence a esta empresa ou não foi encontrado.")
        }
        recrutadorService.deletarRecrutador(recrutadorId)
    }

}
