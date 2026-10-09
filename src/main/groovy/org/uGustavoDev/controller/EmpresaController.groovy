package org.uGustavoDev.controller

import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IEmpresaService
import org.uGustavoDev.service.interfaces.IVagaService

class EmpresaController {

    private final IEmpresaService empresaService
    private final IVagaService vagaService
    private final ICandidatoService candidatoService

    EmpresaController(IEmpresaService empresaService, IVagaService vagaService, ICandidatoService candidatoService) {
        this.empresaService = empresaService
        this.vagaService = vagaService
        this.candidatoService = candidatoService
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

}
