package org.uGustavoDev.service

import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.service.interfaces.ICandidatoService

class CandidatoService implements ICandidatoService {

    private final ICandidatoDAO candidatoDAO
    private final ICompetenciaDAO competenciaDAO
    private final ICandidatoMapper candidatoMapper

    CandidatoService(ICandidatoDAO candidatoDAO, ICompetenciaDAO competenciaDAO, ICandidatoMapper candidatoMapper) {
        this.candidatoDAO = candidatoDAO
        this.competenciaDAO = competenciaDAO
        this.candidatoMapper = candidatoMapper
    }

    @Override
    void adicionarCandidato(CandidatoCadastroDTO dto) {
        if (candidatoDAO.buscarPorEmail(dto.email) != null) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o email '${dto.email}'.")
        }
        if (candidatoDAO.buscarPorCpf(dto.cpf) != null) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o CPF '${dto.cpf}'.")
        }

        Candidato novoCandidato = candidatoMapper.paraEntidade(dto)

        try {
            candidatoDAO.inserir(novoCandidato)
            vincularCompetenciasAoUsuario(novoCandidato.id, novoCandidato.competencias)
        } catch (DatabaseOperationException e) {
            e.printStackTrace(); throw new DatabaseOperationException("Não foi possível salvar o candidato no banco de dados. Causa: " + e.cause?.message, e)
        }
    }

    @Override
    List<Candidato> listarCandidatos() {
        try {
            return candidatoDAO.listar()
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar os candidatos do banco de dados.", e)
        }
    }

    @Override
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

    @Override
    Candidato atualizarCandidato(int id, CandidatoCadastroDTO dto) {
        Candidato candidatoExistente = candidatoDAO.buscarPorId(id)

        if (candidatoExistente == null) {
            throw new IllegalArgumentException("Candidato não encontrado para edição.")
        }
        if (candidatoDAO.buscarPorEmail(dto.email) != null && candidatoDAO.buscarPorEmail(dto.email).id != id) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o email '${dto.email}'.")
        }
        if (candidatoDAO.buscarPorCpf(dto.cpf) != null && candidatoDAO.buscarPorCpf(dto.cpf).id != id) {
            throw new IllegalArgumentException("Já existe um candidato cadastrado com o CPF '${dto.cpf}'.")
        }

        candidatoExistente = candidatoMapper.paraEntidade(dto)
        candidatoExistente.id = id
        try {
            candidatoDAO.atualizar(candidatoExistente)
            competenciaDAO.removerVinculosCandidato(id)
            vincularCompetenciasAoUsuario(id, dto.competencias)

            return candidatoExistente
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível atualizar o candidato no banco de dados.", e)
        }
    }

    @Override
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
