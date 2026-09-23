package org.uGustavoDev

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.LinketinderService
import org.uGustavoDev.ui.ConsoleUI

static void main(String[] args) {
    LinketinderService service = new LinketinderService()
    boolean executando = true

    Candidato candidatoLogado = null
    Empresa empresaLogada = null

    ConsoleUI.limparTela()

    while (executando) {
        if (candidatoLogado != null) {
            int opcao = ConsoleUI.pedirOpcaoCandidatoLogado(candidatoLogado)
            switch (opcao) {
                case 1:
                    ConsoleUI.imprimirCabecalho("Meu Perfil (Candidato)")
                    ConsoleUI.imprimirMensagem(candidatoLogado.toString())
                    ConsoleUI.aguardarContinuacao()
                    break
                case 0:
                    candidatoLogado = null
                    ConsoleUI.imprimirMensagem("Logout efetuado.")
                    break
            }
        } else if (empresaLogada != null) {
            int opcao = ConsoleUI.pedirOpcaoEmpresaLogada(empresaLogada)
            switch (opcao) {
                case 1:
                    ConsoleUI.imprimirCabecalho("Meu Perfil (Empresa)")
                    ConsoleUI.imprimirMensagem(empresaLogada.toString())
                    ConsoleUI.aguardarContinuacao()
                    break
                case 0:
                    empresaLogada = null
                    ConsoleUI.imprimirMensagem("Logout efetuado.")
                    break
            }
        } else {
            int opcao = ConsoleUI.pedirOpcaoDeslogado()

            switch (opcao) {
                case 1:
                    int tipoLogin = ConsoleUI.lerEscolha("Fazer login como:\n1 - Candidato\n2 - Empresa\n0 - Voltar\nSua escolha: ", 0, 2)
                    if (tipoLogin == 1) {
                        def creds = ConsoleUI.pedirCredenciais()
                        candidatoLogado = service.loginCandidato(creds.email, creds.senha)
                        if (candidatoLogado == null) {
                            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
                            ConsoleUI.aguardarContinuacao()
                        }
                    } else if (tipoLogin == 2) {
                        def creds = ConsoleUI.pedirCredenciais()
                        empresaLogada = service.loginEmpresa(creds.email, creds.senha)
                        if (empresaLogada == null) {
                            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
                            ConsoleUI.aguardarContinuacao()
                        }
                    }
                    break
                case 2:
                    def novoCandidato = ConsoleUI.pedirDadosCandidato()
                    if (service.adicionarCandidato(novoCandidato)) {
                        ConsoleUI.imprimirMensagem("\nCandidato cadastrado com sucesso!")
                    }
                    ConsoleUI.aguardarContinuacao()
                    break
                case 3:
                    def novaEmpresa = ConsoleUI.pedirDadosEmpresa()
                    if (service.adicionarEmpresa(novaEmpresa)) {
                        ConsoleUI.imprimirMensagem("\nEmpresa cadastrada com sucesso!")
                    }
                    ConsoleUI.aguardarContinuacao()
                    break
                case 0:
                    ConsoleUI.imprimirMensagem("Encerrando o Linketinder. Até logo!")
                    executando = false
                    break
            }
        }
    }
}