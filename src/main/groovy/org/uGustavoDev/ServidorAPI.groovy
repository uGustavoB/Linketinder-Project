package org.uGustavoDev

import org.apache.catalina.Context
import org.apache.catalina.startup.Tomcat
import org.uGustavoDev.controller.api.CandidatoController

class ServidorAPI implements Runnable{

    private String PORT = "8081"
    private CandidatoController candidatoController

    ServidorAPI(CandidatoController candidatoController) {
        this.candidatoController = candidatoController
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

        tomcat.start()
        println "Servidor iniciado na porta: http://localhost:" + PORT

        tomcat.getServer().await()
    }

}
