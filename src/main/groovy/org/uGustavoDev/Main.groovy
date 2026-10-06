package org.uGustavoDev

import org.uGustavoDev.dao.interfaces.IDatabaseTemplate
import org.uGustavoDev.controller.CandidatoController
import org.uGustavoDev.controller.EmpresaController
import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dao.interfaces.IVagaDAO
import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.mapper.CandidatoMapper
import org.uGustavoDev.mapper.EmpresaMapper
import org.uGustavoDev.mapper.VagaMapper
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.mapper.interfaces.IVagaMapper
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.service.EmpresaService
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IEmpresaService
import org.uGustavoDev.service.interfaces.IVagaService
import org.uGustavoDev.ui.ConsoleUI

static void main(String[] args) {
    IDatabaseTemplate db = new org.uGustavoDev.dao.BaseDao()
    ICompetenciaDAO competenciaDAO = new CompetenciaDAO(db)
    ICandidatoDAO candidatoDAO = new CandidatoDAO(db, competenciaDAO)
    IEmpresaDAO empresaDAO = new EmpresaDAO(db, competenciaDAO)
    IVagaDAO vagaDAO = new VagaDAO(db, competenciaDAO)

    ICandidatoMapper candidatoMapper = new CandidatoMapper()
    IEmpresaMapper empresaMapper = new EmpresaMapper()
    IVagaMapper vagaMapper = new VagaMapper()

    ICandidatoService candidatoService = new CandidatoService(candidatoDAO, competenciaDAO, candidatoMapper)
    IEmpresaService empresaService = new EmpresaService(empresaDAO, competenciaDAO, empresaMapper)
    IVagaService vagaService = new VagaService(vagaDAO, competenciaDAO, vagaMapper)

    CandidatoController candidatoController = new CandidatoController(candidatoService, vagaService)
    EmpresaController empresaController = new EmpresaController(empresaService, vagaService, candidatoService)

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
                case 3:
                    empresaController.menuExplorarCandidatos()
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

                    switch (tipoLogin) {
                        case 1:
                            candidatoLogado = tentarLogin(candidatoController) as Candidato
                            break
                        case 2:
                            empresaLogada = tentarLogin(empresaController) as Empresa
                            break
                        case 0:
                            break
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

private static def tentarLogin(def controller) {
    Map<String, String> creds = ConsoleUI.pedirCredenciais()
    try {
        def usuario = controller.login(creds.email, creds.senha)
        if (usuario == null) {
            ConsoleUI.imprimirMensagem("Email ou senha incorretos.")
            ConsoleUI.aguardarContinuacao()
        }
        return usuario
    } catch (DatabaseOperationException e) {
        ConsoleUI.imprimirMensagem("Sistema temporariamente indisponível: " + e.message)
        ConsoleUI.aguardarContinuacao()
        return null
    }
}
