package org.uGustavoDev.service

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.model.Vaga

class VagaService {

    void adicionarVaga(Vaga vaga) {
        try {
            VagaDAO.inserir(vaga)
            vaga.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAVaga(vaga.id, compId)
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar a vaga no banco de dados.", e)
        }
    }

    List<Vaga> listarVagas() {
        try {
            return VagaDAO.listar()
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as vagas do banco de dados.", e)
        }
    }

    List<Vaga> listarVagasDaEmpresa(int empresaId) {
        try {
            return VagaDAO.listarPorEmpresa(empresaId)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as vagas da empresa.", e)
        }
    }

    Vaga buscarVagaPorId(int id) {
        try {
            return VagaDAO.buscarPorId(id)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível buscar a vaga no banco de dados.", e)
        }
    }

    void atualizarVagaDaEmpresa(int empresaId, int vagaId, Vaga vagaEditada) {
        def vagaExistente = buscarVagaPorId(vagaId)
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
        def vagaExistente = buscarVagaPorId(vagaId)
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
            VagaDAO.atualizar(vaga)
            CompetenciaDAO.removerVinculosVaga(vaga.id)
            vaga.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAVaga(vaga.id, compId)
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a vaga no banco de dados.", e)
        }
    }

    void deletarVaga(int id) {
        try {
            VagaDAO.deletar(id)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar a vaga do banco de dados.", e)
        }
    }
}
