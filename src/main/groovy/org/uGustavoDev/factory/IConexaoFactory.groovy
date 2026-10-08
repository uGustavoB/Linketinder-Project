package org.uGustavoDev.factory

import java.sql.Connection

interface IConexaoFactory {

    Connection getConnection()

}
