package org.uGustavoDev

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
    
    boolean executando = true

    Candidato candidatoLogado = null
    Empresa empresaLogada = null

    ConsoleUI.limparTela()

    while (executando) {
        if (candidatoLogado != null) {
            int opcao = ConsoleUI.pedirOpcaoCandidatoLogado(candidatoLogado)
            switch (opcao) {
                case 1:
                    int opcaoPerfil = ConsoleUI.pedirOpcaoMenuPerfil()
                    if (opcaoPerfil == 1) {
                        ConsoleUI.imprimirCabecalho("Meu Perfil (Candidato)")
                        ConsoleUI.imprimirMensagem(candidatoLogado.toString())
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoPerfil == 2) {
                        ConsoleUI.imprimirMensagem("Por favor, informe seus novos dados:")
                        def candidatoEditado = ConsoleUI.pedirDadosCandidato()
                        candidatoEditado.id = candidatoLogado.id
                        if (candidatoService.atualizarCandidato(candidatoEditado)) {
                            candidatoLogado = candidatoEditado
                            ConsoleUI.imprimirMensagem("\nCandidato atualizado com sucesso!")
                        }
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoPerfil == 3) {
                        if (candidatoService.deletarCandidato(candidatoLogado.id)) {
                            candidatoLogado = null
                            ConsoleUI.imprimirMensagem("\nConta deletada com sucesso.")
                        }
                        ConsoleUI.aguardarContinuacao()
                    }
                    break
                case 2:
                    ConsoleUI.imprimirMensagem("Módulo de Vagas para Candidatos (Em Breve)")
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
                    int opcaoPerfil = ConsoleUI.pedirOpcaoMenuPerfil()
                    if (opcaoPerfil == 1) {
                        ConsoleUI.imprimirCabecalho("Meu Perfil (Empresa)")
                        ConsoleUI.imprimirMensagem(empresaLogada.toString())
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoPerfil == 2) {
                        ConsoleUI.imprimirMensagem("Por favor, informe seus novos dados:")
                        def empresaEditada = ConsoleUI.pedirDadosEmpresa()
                        empresaEditada.id = empresaLogada.id
                        if (empresaService.atualizarEmpresa(empresaEditada)) {
                            empresaLogada = empresaEditada
                            ConsoleUI.imprimirMensagem("\nEmpresa atualizada com sucesso!")
                        }
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoPerfil == 3) {
                        if (empresaService.deletarEmpresa(empresaLogada.id)) {
                            empresaLogada = null
                            ConsoleUI.imprimirMensagem("\nConta deletada com sucesso.")
                        }
                        ConsoleUI.aguardarContinuacao()
                    }
                    break
                case 2:
                    int opcaoVagas = ConsoleUI.pedirOpcaoMenuVagasEmpresa()
                    if (opcaoVagas == 1) {
                        def novaVaga = ConsoleUI.pedirDadosVaga(empresaLogada.id)
                        if (vagaService.adicionarVaga(novaVaga)) {
                            ConsoleUI.imprimirMensagem("\nVaga criada com sucesso!")
                        }
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoVagas == 2) {
                        vagaService.listarVagasDaEmpresa(empresaLogada.id)
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoVagas == 3) {
                        int vagaId = ConsoleUI.lerEscolha("ID da vaga para editar: ", 1, Integer.MAX_VALUE)
                        ConsoleUI.imprimirMensagem("Por favor, informe os novos dados para a vaga:")
                        def vagaEditada = ConsoleUI.pedirDadosVaga(empresaLogada.id)
                        if (vagaService.atualizarVagaDaEmpresa(empresaLogada.id, vagaId, vagaEditada)) {
                            ConsoleUI.imprimirMensagem("\nVaga atualizada com sucesso!")
                        } else {
                            ConsoleUI.imprimirMensagem("Vaga não encontrada, não pertence à sua empresa, ou erro na atualização.")
                        }
                        ConsoleUI.aguardarContinuacao()
                    } else if (opcaoVagas == 4) {
                        int vagaId = ConsoleUI.lerEscolha("ID da vaga para deletar: ", 1, Integer.MAX_VALUE)
                        if (vagaService.deletarVagaDaEmpresa(empresaLogada.id, vagaId)) {
                            ConsoleUI.imprimirMensagem("\nVaga deletada com sucesso.")
                        } else {
                            ConsoleUI.imprimirMensagem("Vaga não encontrada, não pertence à sua empresa, ou erro na deleção.")
                        }
                        ConsoleUI.aguardarContinuacao()
                    }
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
                        candidatoLogado = candidatoService.loginCandidato(creds.email, creds.senha)
                        if (candidatoLogado == null) {
                            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
                            ConsoleUI.aguardarContinuacao()
                        }
                    } else if (tipoLogin == 2) {
                        def creds = ConsoleUI.pedirCredenciais()
                        empresaLogada = empresaService.loginEmpresa(creds.email, creds.senha)
                        if (empresaLogada == null) {
                            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
                            ConsoleUI.aguardarContinuacao()
                        }
                    }
                    break
                case 2:
                    def novoCandidato = ConsoleUI.pedirDadosCandidato()
                    if (candidatoService.adicionarCandidato(novoCandidato)) {
                        ConsoleUI.imprimirMensagem("\nCandidato cadastrado com sucesso!")
                    }
                    ConsoleUI.aguardarContinuacao()
                    break
                case 3:
                    def novaEmpresa = ConsoleUI.pedirDadosEmpresa()
                    if (empresaService.adicionarEmpresa(novaEmpresa)) {
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