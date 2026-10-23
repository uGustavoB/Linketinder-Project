package org.uGustavoDev

import org.apache.catalina.Context
import org.apache.catalina.startup.Tomcat
import org.uGustavoDev.controller.api.CandidatoController
import org.uGustavoDev.controller.api.EmpresaController
import org.uGustavoDev.controller.api.RecrutadorController
import org.uGustavoDev.controller.api.VagaController

class ServidorAPI implements Runnable {

    private String PORT = "8081"
    private CandidatoController candidatoController
    private EmpresaController empresaController
    private RecrutadorController recrutadorController
    private VagaController vagaController

    ServidorAPI(
            CandidatoController candidatoController,
            EmpresaController empresaController,
            RecrutadorController recrutadorController,
            VagaController vagaController) {
        this.candidatoController = candidatoController
        this.empresaController = empresaController
        this.recrutadorController = recrutadorController
        this.vagaController = vagaController
    }

    @Override
    void run() {
        Tomcat tomcat = new Tomcat()
        tomcat.setPort(PORT as int)
        tomcat.getConnector()
        tomcat.setBaseDir("build/tomcat")

        Context ctx = tomcat.addContext("", new File(".").absolutePath)

        tomcat.addServlet(ctx, "CandidatoController", candidatoController)
        ctx.addServletMappingDecoded("/candidatos/*", "CandidatoController")

        tomcat.addServlet(ctx, "EmpresaController", empresaController)
        ctx.addServletMappingDecoded("/empresas/*", "EmpresaController")

        tomcat.addServlet(ctx, "RecrutadorController", recrutadorController)
        ctx.addServletMappingDecoded("/recrutadores/*", "RecrutadorController")

        tomcat.addServlet(ctx, "VagaController", vagaController)
        ctx.addServletMappingDecoded("/vagas/*", "VagaController")

        tomcat.start()
        println "Servidor iniciado na porta: http://localhost:" + PORT

        tomcat.getServer().await()
    }

}
