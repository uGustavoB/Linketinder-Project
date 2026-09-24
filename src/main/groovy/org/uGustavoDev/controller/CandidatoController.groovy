package org.uGustavoDev.controller

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.ui.ConsoleUI

class CandidatoController {
    private final CandidatoService service

    CandidatoController(CandidatoService service) {
        this.service = service
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
}
