package org.uGustavoDev.service

import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.interfaces.IEmpresaService

class EmpresaService implements IEmpresaService {

    private final IEmpresaDAO empresaDAO
    private final ICompetenciaDAO competenciaDAO
    private final IEmpresaMapper empresaMapper

    EmpresaService(IEmpresaDAO empresaDAO, ICompetenciaDAO competenciaDAO, IEmpresaMapper empresaMapper) {
        this.empresaMapper = empresaMapper
        this.empresaDAO = empresaDAO
        this.competenciaDAO = competenciaDAO
    }

    @Override
    void adicionarEmpresa(EmpresaCadastroDTO dto) {
        if (empresaDAO.buscarPorEmail(dto.email) != null) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com o email '${dto.email}'.")
        }
        if (empresaDAO.buscarPorCnpj(dto.cnpj) != null) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com o CNPJ '${dto.cnpj}'.")
        }

        Empresa empresa = empresaMapper.paraEntidade(dto)
        try {
            empresaDAO.inserir(empresa)
            empresa.competencias.each { compNome ->
                int compId = competenciaDAO.buscarOuInserir(compNome)
                competenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível salvar a empresa no banco de dados.", e)
        }
    }

    @Override
    List<Empresa> listarEmpresas() {
        try {
            return empresaDAO.listar()
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível listar as empresas do banco de dados.", e)
        }
    }

    @Override
    Empresa loginEmpresa(String email, String senha) {
        try {
            Empresa empresa = empresaDAO.buscarPorEmail(email)
            if (empresa != null && empresa.senha == senha) {
                return empresa
            }
            return null
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Erro ao tentar realizar login de empresa no banco.", e)
        }
    }

    @Override
    Empresa atualizarEmpresa(int id, EmpresaCadastroDTO dto) {
        Empresa empresa = empresaMapper.paraEntidade(dto)
        empresa.id = id
        try {
            empresaDAO.atualizar(empresa)
            competenciaDAO.removerVinculosEmpresa(empresa.id)
            empresa.competencias.each { compNome ->
                int compId = competenciaDAO.buscarOuInserir(compNome)
                competenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
            return empresa
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível atualizar a empresa no banco de dados.", e)
        }
    }

    @Override
    void deletarEmpresa(int id) {
        try {
            empresaDAO.deletar(id)
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Não foi possível deletar a empresa do banco de dados.", e)
        }
    }

}
