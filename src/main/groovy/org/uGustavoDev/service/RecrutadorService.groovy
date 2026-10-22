package org.uGustavoDev.service

import groovy.transform.CompileStatic
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dao.interfaces.IRecrutadorDAO
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.IRecrutadorMapper
import org.uGustavoDev.model.Recrutador
import org.uGustavoDev.service.interfaces.IRecrutadorService

@CompileStatic
class RecrutadorService implements IRecrutadorService {

    private final IRecrutadorDAO recrutadorDAO
    private final IEmpresaDAO empresaDAO
    private final IRecrutadorMapper recrutadorMapper

    RecrutadorService(IRecrutadorDAO recrutadorDAO, IEmpresaDAO empresaDAO, IRecrutadorMapper recrutadorMapper) {
        this.recrutadorDAO = recrutadorDAO
        this.empresaDAO = empresaDAO
        this.recrutadorMapper = recrutadorMapper
    }

    @Override
    Recrutador adicionarRecrutador(RecrutadorCadastroDTO dto) {
        if (dto.empresaId == null || empresaDAO.buscarPorId(dto.empresaId) == null) {
            throw new IllegalArgumentException("Empresa informada não existe ou não foi encontrada.")
        }
        if (recrutadorDAO.buscarPorEmail(dto.email) != null) {
            throw new IllegalArgumentException("Já existe um recrutador cadastrado com o email '${dto.email}'.")
        }
        if (recrutadorDAO.buscarPorCpf(dto.cpf) != null) {
            throw new IllegalArgumentException("Já existe um recrutador cadastrado com o CPF '${dto.cpf}'.")
        }
        if (dto.senha == null || dto.senha.length() < 6) {
            throw new IllegalArgumentException("A senha do recrutador deve conter no mínimo 6 caracteres.")
        }

        Recrutador recrutador = recrutadorMapper.paraEntidade(dto)

        try {
            recrutadorDAO.inserir(recrutador)
            return recrutador
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível salvar o recrutador no banco de dados.", e)
        }
    }

    @Override
    List<Recrutador> listarRecrutadores() {
        try {
            return recrutadorDAO.listar()
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar os recrutadores do banco de dados.", e)
        }
    }

    @Override
    List<Recrutador> listarRecrutadoresPorEmpresa(int empresaId) {
        try {
            return recrutadorDAO.listarPorEmpresa(empresaId)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar os recrutadores da empresa no banco de dados.", e)
        }
    }

    @Override
    Recrutador buscarRecrutadorPorId(int id) {
        try {
            Recrutador recrutador = recrutadorDAO.buscarPorId(id)
            if (recrutador == null) {
                throw new IllegalArgumentException("Recrutador não encontrado com o ID '${id}'.")
            }
            return recrutador
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Erro ao buscar recrutador por ID.", e)
        }
    }

    @Override
    Recrutador atualizarRecrutador(int id, RecrutadorCadastroDTO dto) {
        Recrutador existente = recrutadorDAO.buscarPorId(id)
        if (existente == null) {
            throw new IllegalArgumentException("Recrutador não encontrado para atualização.")
        }
        if (dto.empresaId == null || empresaDAO.buscarPorId(dto.empresaId) == null) {
            throw new IllegalArgumentException("Empresa informada não existe ou não foi encontrada.")
        }
        Recrutador outroEmail = recrutadorDAO.buscarPorEmail(dto.email)
        if (outroEmail != null && outroEmail.id != id) {
            throw new IllegalArgumentException("Já existe outro recrutador cadastrado com o email '${dto.email}'.")
        }
        Recrutador outroCpf = recrutadorDAO.buscarPorCpf(dto.cpf)
        if (outroCpf != null && outroCpf.id != id) {
            throw new IllegalArgumentException("Já existe outro recrutador cadastrado com o CPF '${dto.cpf}'.")
        }
        if (dto.senha != null && dto.senha.length() < 6) {
            throw new IllegalArgumentException("A senha do recrutador deve conter no mínimo 6 caracteres.")
        }

        Recrutador atualizado = recrutadorMapper.paraEntidade(dto)
        atualizado.id = id

        try {
            recrutadorDAO.atualizar(atualizado)
            return atualizado
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível atualizar o recrutador no banco de dados.", e)
        }
    }

    @Override
    void deletarRecrutador(int id) {
        try {
            Recrutador existente = recrutadorDAO.buscarPorId(id)
            if (existente == null) {
                throw new IllegalArgumentException("Recrutador não encontrado para exclusão.")
            }
            recrutadorDAO.deletar(id)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível deletar o recrutador do banco de dados.", e)
        }
    }

}
