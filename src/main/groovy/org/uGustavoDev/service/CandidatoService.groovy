package org.uGustavoDev.service

import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.model.Candidato

class CandidatoService {

    void adicionarCandidato(Candidato candidato) {
        if (CandidatoDAO.buscarPorEmail(candidato.email) != null) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o email '${candidato.email}'.")
        }
        if (CandidatoDAO.buscarPorCpf(candidato.cpf) != null) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o CPF '${candidato.cpf}'.")
        }

        try {
            CandidatoDAO.inserir(candidato)
            candidato.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAoCandidato(candidato.id, compId)
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar o candidato no banco de dados.", e)
        }
    }

    List<Candidato> listarCandidatos() {
        try {
            return CandidatoDAO.listar()
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar os candidatos do banco de dados.", e)
        }
    }

    Candidato loginCandidato(String email, String senha) {
        try {
            Candidato candidato = CandidatoDAO.buscarPorEmail(email)
            if (candidato != null && candidato.senha == senha) {
                return candidato
            }
            return null
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar realizar login de candidato no banco.", e)
        }
    }

    void atualizarCandidato(Candidato candidato) {
        try {
            CandidatoDAO.atualizar(candidato)
            CompetenciaDAO.removerVinculosCandidato(candidato.id)
            candidato.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAoCandidato(candidato.id, compId)
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar o candidato no banco de dados.", e)
        }
    }

    void deletarCandidato(int id) {
        try {
            CandidatoDAO.deletar(id)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar o candidato do banco de dados.", e)
        }
    }
}
