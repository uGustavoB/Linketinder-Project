package org.uGustavoDev.service

import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.ui.ConsoleUI

class LinketinderService {

    LinketinderService() {
    }

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
