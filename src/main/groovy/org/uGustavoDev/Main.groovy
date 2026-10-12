package org.uGustavoDev

import org.uGustavoDev.factory.DependencyFactory
import org.uGustavoDev.view.ViewFacade

static void main(String[] args) {
    DependencyFactory factory = DependencyFactory.getInstance()
    ViewFacade view = new ViewFacade()

//    Terminal terminal = new Terminal(
//            factory.candidatoController,
//            factory.empresaController,
//            view
//    )

//    terminal.iniciar()

    ServidorAPI tomcatServer = new ServidorAPI(
            factory.candidatoController,
            factory.empresaApiController,
            factory.vagaController
    )

    tomcatServer.run()
}
