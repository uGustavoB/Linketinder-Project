package org.uGustavoDev.service

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.model.Empresa

class EmpresaService {

    private final EmpresaDAO empresaDAO
    private final CompetenciaDAO competenciaDAO

    EmpresaService(EmpresaDAO empresaDAO, CompetenciaDAO competenciaDAO) {
        this.empresaDAO = empresaDAO
        this.competenciaDAO = competenciaDAO
    }

    void adicionarEmpresa(Empresa empresa) {
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
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar a empresa no banco de dados.", e)
        }
    }

    List<Empresa> listarEmpresas() {
        try {
            return empresaDAO.listar()
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as empresas do banco de dados.", e)
        }
    }

    Empresa loginEmpresa(String email, String senha) {
        try {
            Empresa empresa = empresaDAO.buscarPorEmail(email)
            if (empresa != null && empresa.senha == senha) {
                return empresa
            }
            return null
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar realizar login de empresa no banco.", e)
        }
    }

    void atualizarEmpresa(Empresa empresa) {
        try {
            empresaDAO.atualizar(empresa)
            competenciaDAO.removerVinculosEmpresa(empresa.id)
            empresa.competencias.each { compNome ->
                int compId = competenciaDAO.buscarOuInserir(compNome)
                competenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível atualizar a empresa no banco de dados.", e)
        }
    }

    void deletarEmpresa(int id) {
        try {
            empresaDAO.deletar(id)
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar a empresa do banco de dados.", e)
        }
    }

}
