package org.uGustavoDev.service

import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.ui.ConsoleUI

import java.time.LocalDate

class LinketinderService {

    LinketinderService() {
    }

    void adicionarCandidato(Candidato candidato) {
        try {
            CandidatoDAO.inserir(candidato)
            candidato.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAoCandidato(candidato.id, compId)
            }
        } catch (Exception e) {
            System.err.println("Não foi possível salvar o candidato no banco de dados. " + e.message)
        }
    }

    void adicionarEmpresa(Empresa empresa) {
        try {
            EmpresaDAO.inserir(empresa)
            empresa.competencias.each { compNome ->
                int compId = CompetenciaDAO.buscarOuInserir(compNome)
                CompetenciaDAO.vincularAEmpresa(empresa.id, compId)
            }
        } catch (Exception e) {
            System.err.println("Não foi possível salvar a empresa no banco de dados. " + e.message)
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
}
