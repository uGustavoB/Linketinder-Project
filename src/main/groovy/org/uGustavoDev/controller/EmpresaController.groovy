package org.uGustavoDev.controller

import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.EmpresaService
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.ui.ConsoleUI

class EmpresaController {
    private final EmpresaService empresaService
    private final VagaService vagaService
    private final CandidatoService candidatoService

    EmpresaController(EmpresaService empresaService, VagaService vagaService, CandidatoService candidatoService) {
        this.empresaService = empresaService
        this.vagaService = vagaService
        this.candidatoService = candidatoService
    }

    Empresa login(String email, String senha) {
        try {
            return empresaService.loginEmpresa(email, senha)
        } catch (Exception e) {
            ConsoleUI.imprimirMensagem("Erro ao logar: " + e.message)
            return null
        }
    }

    void cadastrarEmpresa() {
        Empresa novaEmpresa = ConsoleUI.pedirDadosEmpresa()
        try {
            empresaService.adicionarEmpresa(novaEmpresa)
            ConsoleUI.imprimirMensagem("\nEmpresa cadastrada com sucesso!")
        } catch (Exception e) {
            ConsoleUI.imprimirMensagem("\nFalha ao cadastrar: " + e.message)
        }
        ConsoleUI.aguardarContinuacao()
    }

    Empresa menuPerfil(Empresa empresaLogada) {
        int opcaoPerfil = ConsoleUI.pedirOpcaoMenuPerfil()
        if (opcaoPerfil == 1) {
            ConsoleUI.imprimirCabecalho("Meu Perfil (Empresa)")
            ConsoleUI.imprimirMensagem(empresaLogada.toString())
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoPerfil == 2) {
            ConsoleUI.imprimirMensagem("Por favor, informe seus novos dados:")
            Empresa empresaEditada = ConsoleUI.pedirDadosEmpresa()
            empresaEditada.id = empresaLogada.id
            try {
                empresaService.atualizarEmpresa(empresaEditada)
                empresaLogada = empresaEditada
                ConsoleUI.imprimirMensagem("\nEmpresa atualizada com sucesso!")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("\nFalha ao atualizar: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoPerfil == 3) {
            try {
                empresaService.deletarEmpresa(empresaLogada.id)
                empresaLogada = null
                ConsoleUI.imprimirMensagem("\nConta deletada com sucesso.")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("\nFalha ao deletar: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        }
        return empresaLogada
    }

    void menuVagas(Empresa empresaLogada) {
        int opcaoVagas = ConsoleUI.pedirOpcaoMenuVagasEmpresa()
        if (opcaoVagas == 1) {
            Vaga novaVaga = ConsoleUI.pedirDadosVaga(empresaLogada.id)
            try {
                vagaService.adicionarVaga(novaVaga)
                ConsoleUI.imprimirMensagem("\nVaga criada com sucesso!")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("\nFalha ao criar vaga: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoVagas == 2) {
            try {
                List<Vaga> vagas = vagaService.listarVagasDaEmpresa(empresaLogada.id)
                if (vagas.isEmpty()) {
                    ConsoleUI.imprimirMensagem("Você ainda não criou nenhuma vaga.")
                } else {
                    ConsoleUI.imprimirCabecalho("Minhas Vagas")
                    vagas.each { ConsoleUI.imprimirMensagem(it.toString()) }
                }
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("Falha ao listar vagas: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoVagas == 3) {
            int vagaId = ConsoleUI.lerEscolha("ID da vaga para editar: ", 1, Integer.MAX_VALUE)
            ConsoleUI.imprimirMensagem("Por favor, informe os novos dados para a vaga:")
            Vaga vagaEditada = ConsoleUI.pedirDadosVaga(empresaLogada.id)
            try {
                vagaService.atualizarVagaDaEmpresa(empresaLogada.id, vagaId, vagaEditada)
                ConsoleUI.imprimirMensagem("\nVaga atualizada com sucesso!")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("Erro: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoVagas == 4) {
            int vagaId = ConsoleUI.lerEscolha("ID da vaga para deletar: ", 1, Integer.MAX_VALUE)
            try {
                vagaService.deletarVagaDaEmpresa(empresaLogada.id, vagaId)
                ConsoleUI.imprimirMensagem("\nVaga deletada com sucesso.")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("Erro: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        }
    }

    void menuExplorarCandidatos() {
        try {
            List<Candidato> candidatos = candidatoService.listarCandidatos()
            if (candidatos.isEmpty()) {
                ConsoleUI.imprimirMensagem("Nenhum candidato cadastrado no momento.")
            } else {
                ConsoleUI.imprimirCabecalho("Lista de Candidatos Disponíveis")
                candidatos.each { ConsoleUI.imprimirMensagem(it.toAnonymousString()) }
            }
        } catch (Exception e) {
            ConsoleUI.imprimirMensagem("Falha ao listar candidatos: " + e.message)
        }
        ConsoleUI.aguardarContinuacao()
    }
}
