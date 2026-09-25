package org.uGustavoDev.controller

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.ui.ConsoleUI

class CandidatoController {
    private final CandidatoService service
    private final VagaService vagaService

    CandidatoController(CandidatoService service, VagaService vagaService) {
        this.service = service
        this.vagaService = vagaService
    }

    Candidato login(String email, String senha) {
        try {
            return service.loginCandidato(email, senha)
        } catch (Exception e) {
            ConsoleUI.imprimirMensagem("Erro ao logar: " + e.message)
            return null
        }
    }

    void cadastrarCandidato() {
        Candidato novoCandidato = ConsoleUI.pedirDadosCandidato()
        try {
            service.adicionarCandidato(novoCandidato)
            ConsoleUI.imprimirMensagem("\nCandidato cadastrado com sucesso!")
        } catch (Exception e) {
            ConsoleUI.imprimirMensagem("\nFalha ao cadastrar: " + e.message)
        }
        ConsoleUI.aguardarContinuacao()
    }

    Candidato menuPerfil(Candidato candidatoLogado) {
        int opcaoPerfil = ConsoleUI.pedirOpcaoMenuPerfil()
        if (opcaoPerfil == 1) {
            ConsoleUI.imprimirCabecalho("Meu Perfil (Candidato)")
            ConsoleUI.imprimirMensagem(candidatoLogado.toString())
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoPerfil == 2) {
            ConsoleUI.imprimirMensagem("Por favor, informe seus novos dados:")
            Candidato candidatoEditado = ConsoleUI.pedirDadosCandidato()
            candidatoEditado.id = candidatoLogado.id
            try {
                service.atualizarCandidato(candidatoEditado)
                candidatoLogado = candidatoEditado
                ConsoleUI.imprimirMensagem("\nCandidato atualizado com sucesso!")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("\nFalha ao atualizar: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        } else if (opcaoPerfil == 3) {
            try {
                service.deletarCandidato(candidatoLogado.id)
                candidatoLogado = null
                ConsoleUI.imprimirMensagem("\nConta deletada com sucesso.")
            } catch (Exception e) {
                ConsoleUI.imprimirMensagem("\nFalha ao deletar: " + e.message)
            }
            ConsoleUI.aguardarContinuacao()
        }
        return candidatoLogado
    }

    void menuVagas() {
        try {
            List<Vaga> vagas = vagaService.listarVagas()
            if (vagas.isEmpty()) {
                ConsoleUI.imprimirMensagem("Nenhuma vaga cadastrada no momento.")
            } else {
                ConsoleUI.imprimirCabecalho("Lista de Vagas Disponíveis")
                vagas.each { ConsoleUI.imprimirMensagem(it.toString()) }
            }
        } catch (Exception e) {
            ConsoleUI.imprimirMensagem("Falha ao listar vagas: " + e.message)
        }
        ConsoleUI.aguardarContinuacao()
    }
}

