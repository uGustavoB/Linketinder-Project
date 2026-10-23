package org.uGustavoDev

import org.uGustavoDev.factory.DependencyFactory
import org.uGustavoDev.view.ViewFacade

class Main {

    static void main(String[] args) {
        DependencyFactory factory = DependencyFactory.getInstance()
        ViewFacade view = new ViewFacade()

        Terminal terminal = new Terminal(
                factory.candidatoViewController,
                factory.empresaViewController,
                factory.recrutadorViewController,
                view
        )

        terminal.iniciar()

//        ServidorAPI tomcatServer = new ServidorAPI(
//                factory.candidatoApiController,
//                factory.empresaApiController,
//                factory.recrutadorApiController,
//                factory.vagaApiController
//        )
//
//        tomcatServer.run()
    }

}
