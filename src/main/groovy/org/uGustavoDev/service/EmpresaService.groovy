package org.uGustavoDev.service

import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.interfaces.IEmpresaService

class EmpresaService implements IEmpresaService {

    private final IEmpresaDAO empresaDAO
    private final ICompetenciaDAO competenciaDAO

    EmpresaService(IEmpresaDAO empresaDAO, ICompetenciaDAO competenciaDAO) {
        this.empresaDAO = empresaDAO
        this.competenciaDAO = competenciaDAO
    }

    @Override
    void adicionarEmpresa(EmpresaCadastroDTO empresa) {
        if (empresaDAO.buscarPorEmail(empresa.email) != null) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com o email '${empresa.email}'.")
        }
        if (empresaDAO.buscarPorCnpj(empresa.cnpj) != null) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com o CNPJ '${empresa.cnpj}'.")
        }

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
    void atualizarEmpresa(EmpresaCadastroDTO empresa) {
        try {
            empresaDAO.atualizar(empresa)
            competenciaDAO.removerVinculosEmpresa(empresa.id)
            empresa.competencias.each { compNome ->
                int compId = competenciaDAO.buscarOuInserir(compNome)
                competenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
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
