package org.uGustavoDev.service

import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.model.Candidato

class CandidatoService {

    private final CandidatoDAO candidatoDAO
    private final CompetenciaDAO competenciaDAO

    CandidatoService(CandidatoDAO candidatoDAO, CompetenciaDAO competenciaDAO) {
        this.candidatoDAO = candidatoDAO
        this.competenciaDAO = competenciaDAO
    }

    void adicionarCandidato(Candidato candidato) {
        if (candidatoDAO.buscarPorEmail(candidato.email) != null) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o email '${candidato.email}'.")
        }
        if (candidatoDAO.buscarPorCpf(candidato.cpf) != null) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o CPF '${candidato.cpf}'.")
        }

        try {
            candidatoDAO.inserir(candidato)
            vincularCompetenciasAoUsuario(candidato.id, candidato.competencias)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível salvar o candidato no banco de dados.", e)
        }
    }

    List<Candidato> listarCandidatos() {
        try {
            return candidatoDAO.listar()
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar os candidatos do banco de dados.", e)
        }
    }

    Candidato loginCandidato(String email, String senha) {
        try {
            Candidato candidato = candidatoDAO.buscarPorEmail(email)
            if (candidato != null && candidato.senha == senha) {
                return candidato
            }
            return null
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Erro ao tentar realizar login de candidato no banco.", e)
        }
    }

    void atualizarCandidato(Candidato candidato) {
        try {
            candidatoDAO.atualizar(candidato)
            competenciaDAO.removerVinculosCandidato(candidato.id)
            vincularCompetenciasAoUsuario(candidato.id, candidato.competencias)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível atualizar o candidato no banco de dados.", e)
        }
    }

    void deletarCandidato(int id) {
        try {
            candidatoDAO.deletar(id)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível deletar o candidato do banco de dados.", e)
        }
    }

    private void vincularCompetenciasAoUsuario(int usuarioId, List<String> competencias) {
        competencias.each { compNome ->
            int compId = competenciaDAO.buscarOuInserir(compNome)
            competenciaDAO.vincularAoCandidato(usuarioId, compId)
        }
    }

}
