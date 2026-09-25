package org.uGustavoDev

import org.uGustavoDev.controller.CandidatoController
import org.uGustavoDev.controller.EmpresaController
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.service.EmpresaService
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.ui.ConsoleUI

static void main(String[] args) {
    CandidatoService candidatoService = new CandidatoService()
    EmpresaService empresaService = new EmpresaService()
    VagaService vagaService = new VagaService()
    
    CandidatoController candidatoController = new CandidatoController(candidatoService, vagaService)
    EmpresaController empresaController = new EmpresaController(empresaService, vagaService)

    boolean executando = true
    Candidato candidatoLogado = null
    Empresa empresaLogada = null

    ConsoleUI.limparTela()

    while (executando) {
        if (candidatoLogado != null) {
            int opcao = ConsoleUI.pedirOpcaoCandidatoLogado(candidatoLogado)
            switch (opcao) {
                case 1:
                    candidatoLogado = candidatoController.menuPerfil(candidatoLogado)
                    break
                case 2:
                    candidatoController.menuVagas()
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
                    empresaLogada = empresaController.menuPerfil(empresaLogada)
                    break
                case 2:
                    empresaController.menuVagas(empresaLogada)
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
                        Map<String, String> creds = ConsoleUI.pedirCredenciais()
                        candidatoLogado = candidatoController.login(creds.email, creds.senha)
                        if (candidatoLogado == null) {
                            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
                            ConsoleUI.aguardarContinuacao()
                        }
                    } else if (tipoLogin == 2) {
                        Map<String, String> creds = ConsoleUI.pedirCredenciais()
                        empresaLogada = empresaController.login(creds.email, creds.senha)
                        if (empresaLogada == null) {
                            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
                            ConsoleUI.aguardarContinuacao()
                        }
                    }
                    break
                case 2:
                    candidatoController.cadastrarCandidato()
                    break
                case 3:
                    empresaController.cadastrarEmpresa()
                    break
                case 0:
                    ConsoleUI.imprimirMensagem("Encerrando o Linketinder. Até logo!")
                    executando = false
                    break
            }
        }
    }
}