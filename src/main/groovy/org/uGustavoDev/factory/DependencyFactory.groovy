package org.uGustavoDev.factory

import org.uGustavoDev.controller.view.CandidatoController as CandidatoViewController
import org.uGustavoDev.controller.view.EmpresaController as EmpresaViewController
import org.uGustavoDev.controller.view.RecrutadorController as RecrutadorViewController
import org.uGustavoDev.controller.api.CandidatoController as CandidatoApiController
import org.uGustavoDev.controller.api.EmpresaController as EmpresaApiController
import org.uGustavoDev.controller.api.RecrutadorController as RecrutadorApiController
import org.uGustavoDev.controller.api.VagaController as VagaApiController
import org.uGustavoDev.dao.BaseDao
import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.dao.RecrutadorDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IDatabaseTemplate
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dao.interfaces.IRecrutadorDAO
import org.uGustavoDev.dao.interfaces.IVagaDAO
import org.uGustavoDev.mapper.CandidatoMapper
import org.uGustavoDev.mapper.EmpresaMapper
import org.uGustavoDev.mapper.RecrutadorMapper
import org.uGustavoDev.mapper.VagaMapper
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.mapper.interfaces.IRecrutadorMapper
import org.uGustavoDev.mapper.interfaces.IVagaMapper
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.service.EmpresaService
import org.uGustavoDev.service.RecrutadorService
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IEmpresaService
import org.uGustavoDev.service.interfaces.IRecrutadorService
import org.uGustavoDev.service.interfaces.IVagaService

class DependencyFactory {

    static DependencyFactory instance

    IDatabaseTemplate db

    ICompetenciaDAO competenciaDAO
    ICandidatoDAO candidatoDAO
    IEmpresaDAO empresaDAO
    IRecrutadorDAO recrutadorDAO
    IVagaDAO vagaDAO

    ICandidatoMapper candidatoMapper
    IEmpresaMapper empresaMapper
    IRecrutadorMapper recrutadorMapper
    IVagaMapper vagaMapper

    ICandidatoService candidatoService
    IEmpresaService empresaService
    IRecrutadorService recrutadorService
    IVagaService vagaService

    CandidatoApiController candidatoApiController
    EmpresaApiController empresaApiController
    RecrutadorApiController recrutadorApiController
    VagaApiController vagaApiController

    CandidatoViewController candidatoViewController
    EmpresaViewController empresaViewController
    RecrutadorViewController recrutadorViewController

    private DependencyFactory() {
        iniciarDependencias()
    }

    static synchronized DependencyFactory getInstance() {
        if (instance == null) {
            instance = new DependencyFactory()
        }
        return instance
    }

    private void iniciarDependencias() {
        inicializarDB()
        inicializarDAOs()
        inicializarMappers()
        inicializarServices()
        inicializarControllers()
    }

    private void inicializarDB() {
        db = new BaseDao()
    }

    private void inicializarDAOs() {
        competenciaDAO = new CompetenciaDAO(db)
        candidatoDAO = new CandidatoDAO(db, competenciaDAO)
        empresaDAO = new EmpresaDAO(db, competenciaDAO)
        recrutadorDAO = new RecrutadorDAO(db)
        vagaDAO = new VagaDAO(db, competenciaDAO)
    }

    private void inicializarMappers() {
        candidatoMapper = new CandidatoMapper()
        empresaMapper = new EmpresaMapper()
        recrutadorMapper = new RecrutadorMapper()
        vagaMapper = new VagaMapper()
    }

    private void inicializarServices() {
        candidatoService = new CandidatoService(candidatoDAO, competenciaDAO, candidatoMapper)
        empresaService = new EmpresaService(empresaDAO, competenciaDAO, empresaMapper)
        recrutadorService = new RecrutadorService(recrutadorDAO, empresaDAO, recrutadorMapper)
        vagaService = new VagaService(vagaDAO, competenciaDAO, vagaMapper)
    }

    private void inicializarControllers() {
        candidatoApiController = new CandidatoApiController(candidatoService)
        empresaApiController = new EmpresaApiController(empresaService)
        recrutadorApiController = new RecrutadorApiController(recrutadorService)
        vagaApiController = new VagaApiController(vagaService)

        candidatoViewController = new CandidatoViewController(candidatoService, vagaService)
        empresaViewController = new EmpresaViewController(empresaService, vagaService, candidatoService, recrutadorService)
        recrutadorViewController = new RecrutadorViewController(
                recrutadorService,
                vagaService,
                candidatoService,
                empresaService
        )
    }

}
