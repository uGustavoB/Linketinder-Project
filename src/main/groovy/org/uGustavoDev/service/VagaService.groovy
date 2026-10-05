package org.uGustavoDev.service

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.model.Vaga

class VagaService {
    private final VagaDAO vagaDAO
    private final CompetenciaDAO competenciaDAO

    VagaService(VagaDAO vagaDAO, CompetenciaDAO competenciaDAO) {
        this.vagaDAO = vagaDAO
        this.competenciaDAO = competenciaDAO
    }

    void adicionarVaga(Vaga vaga) {
        try {
            vagaDAO.inserir(vaga)
            vincularCompetenciasAoUsuario(vaga.id, vaga.competencias)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar a vaga no banco de dados.", e)
        }
    }

    List<Vaga> listarVagas() {
        try {
            return vagaDAO.listar()
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as vagas do banco de dados.", e)
        }
    }

    List<Vaga> listarVagasDaEmpresa(int empresaId) {
        try {
            return vagaDAO.listarPorEmpresa(empresaId)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as vagas da empresa.", e)
        }
    }

    Vaga buscarVagaPorId(int id) {
        try {
            return vagaDAO.buscarPorId(id)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível buscar a vaga no banco de dados.", e)
        }
    }

    void atualizarVagaDaEmpresa(int empresaId, int vagaId, Vaga vagaEditada) {
        Vaga vagaExistente = buscarVagaPorId(vagaId)
        if (vagaExistente == null) {
            throw new IllegalArgumentException("Vaga não encontrada.")
        }
        if (vagaExistente.empresaId != empresaId) {
            throw new IllegalArgumentException("Esta vaga não pertence à sua empresa.")
        }
        
        vagaEditada.id = vagaId
        atualizarVaga(vagaEditada)
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

    void atualizarVaga(Vaga vaga) {
        try {
            vagaDAO.atualizar(vaga)
            competenciaDAO.removerVinculosVaga(vaga.id)
            vincularCompetenciasAoUsuario(vaga.id, vaga.competencias)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a vaga no banco de dados.", e)
        }
    }

    void deletarVaga(int id) {
        try {
            vagaDAO.deletar(id)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar a vaga do banco de dados.", e)
        }
    }

    private void vincularCompetenciasAoUsuario(int vagaId, List<String> competencias) {
        competencias.each { compNome ->
            int compId = competenciaDAO.buscarOuInserir(compNome)
            competenciaDAO.vincularAVaga(vagaId, compId)
        }
    }
}
