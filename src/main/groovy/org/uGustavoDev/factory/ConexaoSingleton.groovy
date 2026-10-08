package org.uGustavoDev.factory

import java.sql.Connection

class ConexaoSingleton {

    private static Connection instance

    private ConexaoSingleton() {}

    static synchronized Connection getInstance() {
        if (instance == null || instance.isClosed()) {
            instance = ConexaoFactory.getConnection()
        }
        return instance
    }

}
