package org.uGustavoDev.controller

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IVagaService
import org.uGustavoDev.ui.ConsoleUI

class CandidatoController {

    private final ICandidatoService service
    private final IVagaService vagaService

    CandidatoController(ICandidatoService service, IVagaService vagaService) {
        this.service = service
        this.vagaService = vagaService
    }

    Candidato login(String email, String senha) {
        return service.loginCandidato(email, senha)
    }

    void cadastrarCandidato() {
        CandidatoCadastroDTO novoCandidato = ConsoleUI.pedirDadosCandidato()
        try {
            service.adicionarCandidato(novoCandidato)
            ConsoleUI.imprimirMensagem("\nCandidato cadastrado com sucesso!")
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            ConsoleUI.imprimirMensagem("\nFalha ao cadastrar: " + e.message)
        }
        ConsoleUI.aguardarContinuacao()
    }

    Candidato menuPerfil(Candidato candidatoLogado) {
        int opcao = ConsoleUI.pedirOpcaoMenuPerfil()
        switch (opcao) {
            case 1:
                visualizarPerfil(candidatoLogado)
                break
            case 2:
                return editarPerfil(candidatoLogado)
            case 3:
                return deletarConta(candidatoLogado)
        }
        return candidatoLogado
    }

    private void visualizarPerfil(Candidato candidato) {
        ConsoleUI.imprimirCabecalho("Perfil do Candidato")
        ConsoleUI.imprimirMensagem(candidato.toString())
        ConsoleUI.aguardarContinuacao()
    }

    private Candidato editarPerfil(Candidato candidatoLogado) {
        ConsoleUI.imprimirMensagem("Informe os novos dados do candidato:")

        CandidatoCadastroDTO dto = ConsoleUI.pedirDadosCandidato()

        try {
            Candidato candidatoAtualizado = service.atualizarCandidato(candidatoLogado.id, dto)

            ConsoleUI.imprimirMensagem("\nCandidato atualizado com sucesso!")
            return candidatoAtualizado
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            ConsoleUI.imprimirMensagem("\nFalha ao atualizar: " + e.message)
            return candidatoLogado
        } finally {
            ConsoleUI.aguardarContinuacao()
        }
    }

    private Candidato deletarConta(Candidato candidato) {
        try {
            service.deletarCandidato(candidato.id)
            ConsoleUI.imprimirMensagem("\nConta deletada com sucesso.")
            return null
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            ConsoleUI.imprimirMensagem("\nFalha ao deletar: " + e.message)
            return candidato
        } finally {
            ConsoleUI.aguardarContinuacao()
        }
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
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            ConsoleUI.imprimirMensagem("Falha ao listar vagas: " + e.message)
        }
        ConsoleUI.aguardarContinuacao()
    }

}

