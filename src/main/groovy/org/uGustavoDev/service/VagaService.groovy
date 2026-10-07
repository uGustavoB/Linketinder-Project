package org.uGustavoDev.service

import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IVagaDAO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.IVagaMapper
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.interfaces.IVagaService

class VagaService implements IVagaService {

    private final IVagaDAO vagaDAO
    private final ICompetenciaDAO competenciaDAO
    private final IVagaMapper vagaMapper

    VagaService(IVagaDAO vagaDAO, ICompetenciaDAO competenciaDAO, IVagaMapper vagaMapper) {
        this.vagaMapper = vagaMapper
        this.vagaDAO = vagaDAO
        this.competenciaDAO = competenciaDAO
    }

    void adicionarVaga(int empresaId, VagaCadastroDTO dto) {
        Vaga vaga = vagaMapper.paraEntidade(dto, empresaId)
        try {
            vagaDAO.inserir(vaga)
            vincularCompetenciasAoUsuario(vaga.id, vaga.competencias)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível salvar a vaga no banco de dados.", e)
        }
    }

    List<Vaga> listarVagas() {
        try {
            return vagaDAO.listar()
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar as vagas do banco de dados.", e)
        }
    }

    List<Vaga> listarVagasDaEmpresa(int empresaId) {
        try {
            return vagaDAO.listarPorEmpresa(empresaId)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar as vagas da empresa.", e)
        }
    }

    Vaga buscarVagaPorId(int id) {
        try {
            return vagaDAO.buscarPorId(id)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível buscar a vaga no banco de dados.", e)
        }
    }

    void atualizarVagaDaEmpresa(int empresaId, int vagaId, VagaCadastroDTO dto) {
        Vaga vagaExistente = buscarVagaPorId(vagaId)
        if (vagaExistente == null) {
            throw new IllegalArgumentException("Vaga não encontrada.")
        }
        if (vagaExistente.empresaId != empresaId) {
            throw new IllegalArgumentException("Esta vaga não pertence à sua empresa.")
        }

        atualizarVaga(vagaId, empresaId, dto)
    }

    void deletarVagaDaEmpresa(int empresaId, int vagaId) {
        Vaga vagaExistente = buscarVagaPorId(vagaId)
        if (vagaExistente == null) {
            throw new IllegalArgumentException("Vaga não encontrada.")
        }
        if (vagaExistente.empresaId != empresaId) {
            throw new IllegalArgumentException("Esta vaga não pertence à sua empresa.")
        }

        deletarVaga(vagaId)
    }

    Vaga atualizarVaga(int vagaId, int empresaId, VagaCadastroDTO dto) {
        Vaga vaga = vagaMapper.paraEntidade(dto, empresaId)
        vaga.id = vagaId
        try {
            vagaDAO.atualizar(vaga)
            competenciaDAO.removerVinculosVaga(vaga.id)
            vincularCompetenciasAoUsuario(vaga.id, vaga.competencias)
            return vaga
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível atualizar a vaga no banco de dados.", e)
        }
    }

    void deletarVaga(int id) {
        try {
            vagaDAO.deletar(id)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível deletar a vaga do banco de dados.", e)
        }
    }

    // Gustavo - mudar nome do método
    private void vincularCompetenciasAoUsuario(int vagaId, List<String> competencias) {
        competencias.each { compNome ->
            int compId = competenciaDAO.buscarOuInserir(compNome)
            competenciaDAO.vincularAVaga(vagaId, compId)
        }
    }

}
