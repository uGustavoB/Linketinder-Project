package org.uGustavoDev

import org.uGustavoDev.controller.view.CandidatoController
import org.uGustavoDev.controller.view.EmpresaController
import org.uGustavoDev.controller.view.RecrutadorController
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.factory.DependencyFactory
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Recrutador
import org.uGustavoDev.model.Vaga
import org.uGustavoDev.view.ViewFacade

class Terminal {

    private final CandidatoController candidatoController
    private final EmpresaController empresaController
    private final RecrutadorController recrutadorController
    private final ViewFacade view

    Terminal(CandidatoController candidatoController, EmpresaController empresaController, ViewFacade view) {
        this(
                candidatoController,
                empresaController,
                DependencyFactory.getInstance().recrutadorViewController,
                view
        )
    }

    Terminal(
            CandidatoController candidatoController,
            EmpresaController empresaController,
            RecrutadorController recrutadorController,
            ViewFacade view) {
        this.candidatoController = candidatoController
        this.empresaController = empresaController
        this.recrutadorController = recrutadorController ?: DependencyFactory.getInstance().recrutadorViewController
        this.view = view
    }

    void iniciar() {
        boolean executando = true
        Candidato candidatoLogado = null
        Empresa empresaLogada = null
        Recrutador recrutadorLogado = null

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
                        menuExplorarCandidatos(false)
                        break
                    case 4:
                        menuGestaoRecrutadores(empresaLogada)
                        break
                    case 0:
                        empresaLogada = null
                        view.mostrarMensagem("Logout efetuado.")
                        break
                }
            } else if (recrutadorLogado != null) {
                int opcao = view.mostrarMenuRecrutador(recrutadorLogado)
                switch (opcao) {
                    case 1:
                        recrutadorLogado = menuPerfilRecrutador(recrutadorLogado)
                        break
                    case 2:
                        menuVagasRecrutador(recrutadorLogado)
                        break
                    case 3:
                        menuExplorarCandidatos(true)
                        break
                    case 0:
                        recrutadorLogado = null
                        view.mostrarMensagem("Logout efetuado.")
                        break
                }
            } else {
                int opcao = view.mostrarMenuDeslogado()
                switch (opcao) {
                    case 1:
                        int tipoLogin = view.mostrarMenuTipoLogin()
                        if (tipoLogin == 1) {
                            candidatoLogado = tentarLoginCandidato()
                        } else if (tipoLogin == 2) {
                            empresaLogada = tentarLoginEmpresa()
                        } else if (tipoLogin == 3) {
                            recrutadorLogado = tentarLoginRecrutador()
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
        Map<String, String> creds = view.obterCredenciais()
        if (creds == null) {
            return null
        }
        Candidato usuario = null
        try {
            usuario = candidatoController?.login(creds.email, creds.senha)
            if (usuario == null) {
                view.mostrarMensagem("Email ou senha incorretos.")
                view.aguardarContinuacao()
            }
        } catch (DatabaseOperationException e) {
            view.mostrarMensagem("Sistema temporariamente indisponível: " + e.message)
            view.aguardarContinuacao()
        }
        return usuario
    }

    private Empresa tentarLoginEmpresa() {
        Map<String, String> creds = view.obterCredenciais()
        if (creds == null) {
            return null
        }
        Empresa usuario = null
        try {
            usuario = empresaController?.login(creds.email, creds.senha)
            if (usuario == null) {
                view.mostrarMensagem("Email ou senha incorretos.")
                view.aguardarContinuacao()
            }
        } catch (DatabaseOperationException e) {
            view.mostrarMensagem("Sistema temporariamente indisponível: " + e.message)
            view.aguardarContinuacao()
        }
        return usuario
    }

    private Recrutador tentarLoginRecrutador() {
        if (recrutadorController == null) {
            view.mostrarMensagem("Serviço de recrutador não disponível.")
            view.aguardarContinuacao()
            return null
        }
        Map<String, String> creds = view.obterCredenciais()
        if (creds == null) {
            return null
        }
        Recrutador usuario = null
        try {
            usuario = recrutadorController.login(creds.email, creds.senha)
            if (usuario == null) {
                view.mostrarMensagem("Email ou senha incorretos.")
                view.aguardarContinuacao()
            }
        } catch (DatabaseOperationException e) {
            view.mostrarMensagem("Sistema temporariamente indisponível: " + e.message)
            view.aguardarContinuacao()
        }
        return usuario
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

    private void menuExplorarCandidatos(boolean isRecrutador = false) {
        try {
            List<Candidato> candidatos = isRecrutador ?
                    recrutadorController.listarCandidatos() :
                    empresaController.listarCandidatos()
            view.mostrarCandidatos(candidatos)
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("Falha ao listar candidatos: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private void menuGestaoRecrutadores(Empresa empresaLogada) {
        boolean voltar = false
        while (!voltar) {
            int opcao = view.mostrarMenuGestaoRecrutadores()
            switch (opcao) {
                case 1:
                    cadastrarRecrutadorParaEmpresa(empresaLogada)
                    break
                case 2:
                    listarRecrutadoresDaEmpresa(empresaLogada)
                    break
                case 3:
                    deletarRecrutadorDaEmpresa(empresaLogada)
                    break
                case 0:
                    voltar = true
                    break
            }
        }
    }

    private void cadastrarRecrutadorParaEmpresa(Empresa empresaLogada) {
        RecrutadorCadastroDTO dto = view.obterDadosRecrutador(empresaLogada.id)
        try {
            empresaController.cadastrarRecrutador(dto)
            view.mostrarMensagem("\nRecrutador cadastrado com sucesso!")
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("\nFalha ao cadastrar: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private void listarRecrutadoresDaEmpresa(Empresa empresaLogada) {
        try {
            List<Recrutador> recrutadores = empresaController.listarRecrutadoresDaEmpresa(empresaLogada.id)
            view.mostrarRecrutadoresEmpresa(recrutadores)
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("Falha ao listar recrutadores: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private void deletarRecrutadorDaEmpresa(Empresa empresaLogada) {
        try {
            List<Recrutador> recrutadores = empresaController.listarRecrutadoresDaEmpresa(empresaLogada.id)
            view.mostrarRecrutadoresEmpresa(recrutadores)
            if (recrutadores.isEmpty()) {
                view.aguardarContinuacao()
                return
            }
            Integer id = view.lerEscolha(
                    "Informe o ID do recrutador a ser deletado (ou vazio para cancelar): ",
                    1,
                    Integer.MAX_VALUE,
                    true
            )
            if (id != null) {
                empresaController.deletarRecrutadorDaEmpresa(empresaLogada.id, id)
                view.mostrarMensagem("\nRecrutador deletado com sucesso.")
            }
        } catch (DatabaseOperationException | IllegalArgumentException e) {
            view.mostrarMensagem("\nFalha ao deletar: " + e.message)
        }
        view.aguardarContinuacao()
    }

    private Recrutador menuPerfilRecrutador(Recrutador recrutadorLogado) {
        int opcao = view.mostrarMenuPerfilRecrutador()
        switch (opcao) {
            case 1:
                Empresa empresa = recrutadorController.buscarEmpresa(recrutadorLogado.empresaId)
                view.mostrarPerfilRecrutador(recrutadorLogado, empresa)
                view.aguardarContinuacao()
                break
            case 2:
                view.mostrarMensagem("Informe os novos dados do recrutador:")
                RecrutadorCadastroDTO dto = view.obterDadosRecrutador(recrutadorLogado.empresaId)
                try {
                    Recrutador atualizado = recrutadorController.atualizarRecrutador(recrutadorLogado.id, dto)
                    view.mostrarMensagem("\nRecrutador atualizado com sucesso!")
                    view.aguardarContinuacao()
                    return atualizado
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao atualizar: " + e.message)
                    view.aguardarContinuacao()
                }
                break
        }
        return recrutadorLogado
    }

    private void menuVagasRecrutador(Recrutador recrutadorLogado) {
        int opcao = view.mostrarMenuVagas()
        switch (opcao) {
            case 1:
                VagaCadastroDTO dto = view.obterDadosVaga()
                try {
                    recrutadorController.adicionarVaga(recrutadorLogado.empresaId, dto)
                    view.mostrarMensagem("\nVaga criada com sucesso!")
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("\nFalha ao criar vaga: " + e.message)
                }
                view.aguardarContinuacao()
                break
            case 2:
                try {
                    List<Vaga> vagas = recrutadorController.listarVagasDaEmpresa(recrutadorLogado.empresaId)
                    if (vagas.isEmpty()) {
                        view.mostrarMensagem("A empresa ainda não possui nenhuma vaga cadastrada.")
                    } else {
                        view.mostrarVagas(vagas, "Vagas da Empresa")
                    }
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("Falha ao listar vagas: " + e.message)
                }
                view.aguardarContinuacao()
                break
            case 3:
                int vagaId = view.lerEscolha("ID da vaga para editar: ", 1, Integer.MAX_VALUE)
                view.mostrarMensagem("Por favor, informe os novos dados para a vaga:")
                VagaCadastroDTO dtoEditada = view.obterDadosVaga()
                try {
                    recrutadorController.atualizarVagaDaEmpresa(recrutadorLogado.empresaId, vagaId, dtoEditada)
                    view.mostrarMensagem("\nVaga atualizada com sucesso!")
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("Erro: " + e.message)
                }
                view.aguardarContinuacao()
                break
            case 4:
                int vagaIdDel = view.lerEscolha("ID da vaga para deletar: ", 1, Integer.MAX_VALUE)
                try {
                    recrutadorController.deletarVagaDaEmpresa(recrutadorLogado.empresaId, vagaIdDel)
                    view.mostrarMensagem("\nVaga deletada com sucesso.")
                } catch (DatabaseOperationException | IllegalArgumentException e) {
                    view.mostrarMensagem("Erro: " + e.message)
                }
                view.aguardarContinuacao()
                break
        }
    }

}
