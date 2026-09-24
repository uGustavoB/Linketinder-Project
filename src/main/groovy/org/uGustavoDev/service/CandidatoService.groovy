package org.uGustavoDev.service

import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.ui.ConsoleUI

class CandidatoService {

    boolean adicionarCandidato(Candidato candidato) {
        if (CandidatoDAO.buscarPorEmail(candidato.email) != null) {
            System.err.println("Erro: Já existe um candidato cadastrado com o email '${candidato.email}'.")
            return false
        }
        if (CandidatoDAO.buscarPorCpf(candidato.cpf) != null) {
            System.err.println("Erro: Já existe um candidato cadastrado com o CPF '${candidato.cpf}'.")
            return false
        }

        try {
            CandidatoDAO.inserir(candidato)
            candidato.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAoCandidato(candidato.id, compId)
            }
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível salvar o candidato no banco de dados. " + e.message)
            return false
        }
    }

    void listarCandidatos() {
        try {
            def candidatosDoBanco = CandidatoDAO.listar()
            if (candidatosDoBanco.isEmpty()) {
                ConsoleUI.imprimirMensagem("Nenhum candidato cadastrado.")
                return
            }

            ConsoleUI.imprimirCabecalho("Lista de Candidatos")
            candidatosDoBanco.each { ConsoleUI.imprimirMensagem(it.toString()) }
        } catch (Exception e) {
            System.err.println("Não foi possível listar os candidatos do banco de dados. " + e.message)
        }
    }

    Candidato loginCandidato(String email, String senha) {
        try {
            Candidato candidato = CandidatoDAO.buscarPorEmail(email)
            if (candidato != null && candidato.senha == senha) {
                return candidato
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar realizar login de candidato no banco: " + e.message)
        }
        return null
    }

    boolean atualizarCandidato(Candidato candidato) {
        try {
            CandidatoDAO.atualizar(candidato)
            CompetenciaDAO.removerVinculosCandidato(candidato.id)
            candidato.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAoCandidato(candidato.id, compId)
            }
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível atualizar o candidato no banco de dados. " + e.message)
            return false
        }
    }

    boolean deletarCandidato(int id) {
        try {
            CandidatoDAO.deletar(id)
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível deletar o candidato do banco de dados. " + e.message)
            return false
        }
    }
}
