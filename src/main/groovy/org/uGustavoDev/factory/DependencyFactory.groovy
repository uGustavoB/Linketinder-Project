package org.uGustavoDev.factory

import org.uGustavoDev.controller.CandidatoController
import org.uGustavoDev.controller.EmpresaController
import org.uGustavoDev.dao.BaseDao
import org.uGustavoDev.dao.CandidatoDAO
import org.uGustavoDev.dao.CompetenciaDAO
import org.uGustavoDev.dao.EmpresaDAO
import org.uGustavoDev.dao.VagaDAO
import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IDatabaseTemplate
import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.dao.interfaces.IVagaDAO
import org.uGustavoDev.mapper.CandidatoMapper
import org.uGustavoDev.mapper.EmpresaMapper
import org.uGustavoDev.mapper.VagaMapper
import org.uGustavoDev.mapper.interfaces.ICandidatoMapper
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.mapper.interfaces.IVagaMapper
import org.uGustavoDev.service.CandidatoService
import org.uGustavoDev.service.EmpresaService
import org.uGustavoDev.service.VagaService
import org.uGustavoDev.service.interfaces.ICandidatoService
import org.uGustavoDev.service.interfaces.IEmpresaService
import org.uGustavoDev.service.interfaces.IVagaService

class DependencyFactory {

    static DependencyFactory instance

    IDatabaseTemplate db

    ICompetenciaDAO competenciaDAO
    ICandidatoDAO candidatoDAO
    IEmpresaDAO empresaDAO
    IVagaDAO vagaDAO

    ICandidatoMapper candidatoMapper
    IEmpresaMapper empresaMapper
    IVagaMapper vagaMapper

    ICandidatoService candidatoService
    IEmpresaService empresaService
    IVagaService vagaService

    CandidatoController candidatoController
    EmpresaController empresaController

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
        vagaDAO = new VagaDAO(db, competenciaDAO)
    }

    private void inicializarMappers() {
        candidatoMapper = new CandidatoMapper()
        empresaMapper = new EmpresaMapper()
        vagaMapper = new VagaMapper()
    }

    private void inicializarServices() {
        candidatoService = new CandidatoService(candidatoDAO, competenciaDAO, candidatoMapper)
        empresaService = new EmpresaService(empresaDAO, competenciaDAO, empresaMapper)
        vagaService = new VagaService(vagaDAO, competenciaDAO, vagaMapper)
    }

    private void inicializarControllers() {
        candidatoController = new CandidatoController(candidatoService, vagaService)
        empresaController = new EmpresaController(empresaService, vagaService, candidatoService)
    }

}
