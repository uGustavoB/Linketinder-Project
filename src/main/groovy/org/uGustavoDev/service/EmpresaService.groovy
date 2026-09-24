package org.uGustavoDev.service

import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.ui.ConsoleUI

class EmpresaService {

    boolean adicionarEmpresa(Empresa empresa) {
        if (EmpresaDAO.buscarPorEmail(empresa.email) != null) {
            System.err.println("Erro: Já existe uma empresa cadastrada com o email '${empresa.email}'.")
            return false
        }
        if (EmpresaDAO.buscarPorCnpj(empresa.cnpj) != null) {
            System.err.println("Erro: Já existe uma empresa cadastrada com o CNPJ '${empresa.cnpj}'.")
            return false
        }

        try {
            EmpresaDAO.inserir(empresa)
            empresa.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível salvar a empresa no banco de dados. " + e.message)
            return false
        }
    }

    void listarEmpresas() {
        try {
            def empresasDoBanco = EmpresaDAO.listar()
            if (empresasDoBanco.isEmpty()) {
                ConsoleUI.imprimirMensagem("Nenhuma empresa cadastrada.")
                return
            }

            ConsoleUI.imprimirCabecalho("Lista de Empresas")
            empresasDoBanco.each { ConsoleUI.imprimirMensagem(it.toString()) }
        } catch (Exception e) {
            System.err.println("Não foi possível listar as empresas do banco de dados. " + e.message)
        }
    }

    Empresa loginEmpresa(String email, String senha) {
        try {
            Empresa empresa = EmpresaDAO.buscarPorEmail(email)
            if (empresa != null && empresa.senha == senha) {
                return empresa
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar realizar login de empresa no banco: " + e.message)
        }
        return null
    }

    boolean atualizarEmpresa(Empresa empresa) {
        try {
            EmpresaDAO.atualizar(empresa)
            CompetenciaDAO.removerVinculosEmpresa(empresa.id)
            empresa.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível atualizar a empresa no banco de dados. " + e.message)
            return false
        }
    }

    boolean deletarEmpresa(int id) {
        try {
            EmpresaDAO.deletar(id)
            return true
        } catch (Exception e) {
            System.err.println("Não foi possível deletar a empresa do banco de dados. " + e.message)
            return false
        }
    }
}
