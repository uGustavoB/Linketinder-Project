package org.uGustavoDev.service

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.ui.ConsoleUI

class VagaService {

    boolean adicionarVaga(Vaga vaga) {
        try {
            VagaDAO.inserir(vaga)
            vaga.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAVaga(vaga.id, compId)
            }
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível salvar a vaga no banco de dados. " + e.message)
            return false
        }
    }

    void listarVagas() {
        try {
            def vagasDoBanco = VagaDAO.listar()
            if (vagasDoBanco.isEmpty()) {
                ConsoleUI.imprimirMensagem("Nenhuma vaga cadastrada.")
                return
            }

            ConsoleUI.imprimirCabecalho("Lista de Vagas")
            vagasDoBanco.each { ConsoleUI.imprimirMensagem(it.toString()) }
        } catch (Exception e) {
            System.err.println("Não foi possível listar as vagas do banco de dados. " + e.message)
        }
    }

    void listarVagasDaEmpresa(int empresaId) {
        try {
            def vagasDaEmpresa = VagaDAO.listarPorEmpresa(empresaId)
            if (vagasDaEmpresa.isEmpty()) {
                ConsoleUI.imprimirMensagem("Você ainda não criou nenhuma vaga.")
                return
            }
            ConsoleUI.imprimirCabecalho("Minhas Vagas")
            vagasDaEmpresa.each { ConsoleUI.imprimirMensagem(it.toString()) }
        } catch (Exception e) {
            System.err.println("Não foi possível listar as vagas da empresa. " + e.message)
        }
    }

    Vaga buscarVagaPorId(int id) {
        try {
            return VagaDAO.buscarPorId(id)
        } catch (Exception e) {
            System.err.println("Não foi possível buscar a vaga no banco de dados. " + e.message)
            return null
        }
    }

    boolean atualizarVagaDaEmpresa(int empresaId, int vagaId, Vaga vagaEditada) {
        try {
            def vagaExistente = VagaDAO.buscarPorId(vagaId)
            if (vagaExistente != null && vagaExistente.empresaId == empresaId) {
                vagaEditada.id = vagaId
                return atualizarVaga(vagaEditada)
            }
            return false
        } catch (Exception e) {
            System.err.println("Erro ao validar atualização da vaga: " + e.message)
            return false
        }
    }

    boolean deletarVagaDaEmpresa(int empresaId, int vagaId) {
        try {
            def vagaExistente = VagaDAO.buscarPorId(vagaId)
            if (vagaExistente != null && vagaExistente.empresaId == empresaId) {
                return deletarVaga(vagaId)
            }
            return false
        } catch (Exception e) {
            System.err.println("Erro ao validar deleção da vaga: " + e.message)
            return false
        }
    }

    boolean atualizarVaga(Vaga vaga) {
        try {
            VagaDAO.atualizar(vaga)
            CompetenciaDAO.removerVinculosVaga(vaga.id)
            vaga.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAVaga(vaga.id, compId)
            }
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível atualizar a vaga no banco de dados. " + e.message)
            return false
        }
    }

    boolean deletarVaga(int id) {
        try {
            VagaDAO.deletar(id)
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível deletar a vaga do banco de dados. " + e.message)
            return false
        }
    }
}
