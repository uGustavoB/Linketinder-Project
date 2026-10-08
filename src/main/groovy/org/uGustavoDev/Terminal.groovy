package org.uGustavoDev

import org.uGustavoDev.controller.CandidatoController
import org.uGustavoDev.controller.EmpresaController
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.view.ViewFacade

class Terminal {
    private final CandidatoController candidatoController
    private final EmpresaController empresaController
    private final ViewFacade view

    Terminal(CandidatoController candidatoController, EmpresaController empresaController, ViewFacade view) {
        this.candidatoController = candidatoController
        this.empresaController = empresaController
        this.view = view
    }

    void iniciar() {
        boolean executando = true
        Candidato candidatoLogado = null
        Empresa empresaLogada = null

        view.limparTela()

        while (executando) {
            if (candidatoLogado != null) {
                int opcao = view.mostrarMenuCandidato(candidatoLogado)
                switch (opcao) {
                    case 1:
                        candidatoLogado = menuPerfilCandidato(candidatoLogado)
                        break
                    case 2:
                        menuVagasCandidato()
                        break
                    case 0:
                        candidatoLogado = null
                        view.mostrarMensagem("Logout efetuado.")
                        break
                }
            } else if (empresaLogada != null) {
                int opcao = view.mostrarMenuEmpresa(empresaLogada)
                switch (opcao) {
                    case 1:
                        empresaLogada = menuPerfilEmpresa(empresaLogada)
                        break
                    case 2:
                        menuVagasEmpresa(empresaLogada)
                        break
                    case 3:
                        menuExplorarCandidatos()
                        break
                    case 0:
                        empresaLogada = null
                        view.mostrarMensagem("Logout efetuado.")
                        break
                }
            } else {
                int opcao = view.mostrarMenuDeslogado()
                switch (opcao) {
                    case 1:
                        int tipoLogin = view.lerEscolha("Fazer login como:\n1 - Candidato\n2 - Empresa\n0 - Voltar\nSua escolha: ", 0, 2)
                        if (tipoLogin == 1) {
                            candidatoLogado = tentarLoginCandidato()
                        } else if (tipoLogin == 2) {
                            empresaLogada = tentarLoginEmpresa()
                        }
                        break
                    case 2:
                        cadastrarCandidato()
                        break
                    case 3:
                        cadastrarEmpresa()
                        break
                    case 0:
                        view.mostrarMensagem("Encerrando o Linketinder. Até logo!")
                        executando = false
                        break
                }
            }
        }
    }

    private Candidato tentarLoginCandidato() {
        def creds = view.obterCredenciais()
        try {
            def usuario = candidatoController.login(creds.email, creds.senha)
            if (usuario == null) {
                view.mostrarMensagem("Email ou senha incorretos.")
                view.aguardarContinuacao()
            }
            return usuario
        } catch (DatabaseOperationException e) {
            view.mostrarMensagem("Sistema temporariamente indisponível: " + e.message)
            view.aguardarContinuacao()
            return null
        }
    }

    private Empresa tentarLoginEmpresa() {
        def creds = view.obterCredenciais()
        try {
            def usuario = empresaController.login(creds.email, creds.senha)
            if (usuario == null) {
                view.mostrarMensagem("Email ou senha incorretos.")
                view.aguardarContinuacao()
            }
            return usuario
        } catch (DatabaseOperationException e) {
            view.mostrarMensagem("Sistema temporariamente indisponível: " + e.message)
            view.aguardarContinuacao()
            return null
        }
    }

    private void cadastrarCandidato() {
        def dto = view.obterDadosCandidato()
        try {
            candidatoController.cadastrarCandidato(dto)
            view.mostrarMensagem("\nCandidato cadastrado com sucesso!")
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("\nFalha ao cadastrar: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private void cadastrarEmpresa() {
        def dto = view.obterDadosEmpresa()
        try {
            empresaController.cadastrarEmpresa(dto)
            view.mostrarMensagem("\nEmpresa cadastrada com sucesso!")
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("\nFalha ao cadastrar: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private Candidato menuPerfilCandidato(Candidato candidatoLogado) {
        int opcao = view.mostrarMenuPerfil()
        switch (opcao) {
            case 1:
                view.mostrarPerfilCandidato(candidatoLogado)
                view.aguardarContinuacao()
                break
            case 2:
                view.mostrarMensagem("Informe os novos dados do candidato:")
                def dto = view.obterDadosCandidato()
                try {
                    Candidato atualizado = candidatoController.atualizarCandidato(candidatoLogado.id, dto)
                    view.mostrarMensagem("\nCandidato atualizado com sucesso!")
                    view.aguardarContinuacao()
                    return atualizado
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao atualizar: " + e.message)
                    view.aguardarContinuacao()
                }
                break
            case 3:
                try {
                    candidatoController.deletarCandidato(candidatoLogado.id)
                    view.mostrarMensagem("\nConta deletada com sucesso.")
                    view.aguardarContinuacao()
                    return null
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao deletar: " + e.message)
                    view.aguardarContinuacao()
                }
                break
        }
        return candidatoLogado
    }

    private void menuVagasCandidato() {
        try {
            List<Vaga> vagas = candidatoController.listarVagas()
            view.mostrarVagas(vagas, "Lista de Vagas Disponíveis")
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("Falha ao listar vagas: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private Empresa menuPerfilEmpresa(Empresa empresaLogada) {
        int opcao = view.mostrarMenuPerfil()
        switch (opcao) {
            case 1:
                view.mostrarPerfilEmpresa(empresaLogada)
                view.aguardarContinuacao()
                break
            case 2:
                view.mostrarMensagem("Por favor, informe seus novos dados:")
                def dto = view.obterDadosEmpresa()
                try {
                    Empresa atualizada = empresaController.atualizarEmpresa(empresaLogada.id, dto)
                    view.mostrarMensagem("\nEmpresa atualizada com sucesso!")
                    view.aguardarContinuacao()
                    return atualizada
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao atualizar: " + e.message)
                    view.aguardarContinuacao()
                }
                break
            case 3:
                try {
                    empresaController.deletarEmpresa(empresaLogada.id)
                    view.mostrarMensagem("\nConta deletada com sucesso.")
                    view.aguardarContinuacao()
                    return null
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao deletar: " + e.message)
                    view.aguardarContinuacao()
                }
                break
        }
        return empresaLogada
    }

    private void menuVagasEmpresa(Empresa empresaLogada) {
        int opcao = view.mostrarMenuVagas()
        switch (opcao) {
            case 1:
                def dto = view.obterDadosVaga()
                try {
                    empresaController.adicionarVaga(empresaLogada.id, dto)
                    view.mostrarMensagem("\nVaga criada com sucesso!")
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao criar vaga: " + e.message)
                }
                view.aguardarContinuacao()
                break
            case 2:
                try {
                    List<Vaga> vagas = empresaController.listarVagasDaEmpresa(empresaLogada.id)
                    if (vagas.isEmpty()) {
                        view.mostrarMensagem("Você ainda não criou nenhuma vaga.")
                    } else {
                        view.mostrarVagas(vagas, "Minhas Vagas")
                    }
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("Falha ao listar vagas: " + e.message)
                }
                view.aguardarContinuacao()
                break
            case 3:
                int vagaId = view.lerEscolha("ID da vaga para editar: ", 1, Integer.MAX_VALUE)
                view.mostrarMensagem("Por favor, informe os novos dados para a vaga:")
                def dtoEditada = view.obterDadosVaga()
                try {
                    empresaController.atualizarVagaDaEmpresa(empresaLogada.id, vagaId, dtoEditada)
                    view.mostrarMensagem("\nVaga atualizada com sucesso!")
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("Erro: " + e.message)
                }
                view.aguardarContinuacao()
                break
            case 4:
                int vagaIdDel = view.lerEscolha("ID da vaga para deletar: ", 1, Integer.MAX_VALUE)
                try {
                    empresaController.deletarVagaDaEmpresa(empresaLogada.id, vagaIdDel)
                    view.mostrarMensagem("\nVaga deletada com sucesso.")
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("Erro: " + e.message)
                }
                view.aguardarContinuacao()
                break
        }
    }

    private void menuExplorarCandidatos() {
        try {
            List<Candidato> candidatos = empresaController.listarCandidatos()
            view.mostrarCandidatos(candidatos)
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("Falha ao listar candidatos: " + e.message)
        }
        view.aguardarContinuacao()
    }
}
