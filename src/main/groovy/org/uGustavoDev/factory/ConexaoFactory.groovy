package org.uGustavoDev.factory

import io.github.cdimascio.dotenv.Dotenv

import java.sql.Connection

class ConexaoFactory {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load()
    
    static IConexaoFactory getFactory() {
        String dbType = dotenv.get("DB_TYPE") ?: System.getenv("DB_TYPE") ?: "POSTGRES"
        
        switch (dbType.toUpperCase()) {
            case "MYSQL":
                return new MysqlConexaoFactory()
            case "POSTGRES":
            default:
                return new PostgresConexaoFactory()
        }
    }

    static Connection getConnection() {
        return getFactory().getConnection()
    }

    static void main(String[] args) {
        try {
            Connection conn = getConnection()
            if (conn != null) {
                println "Sucesso!"
                conn.close()
            }
        } catch (Exception e) {
            println "Falha ao conectar: ${e.message}"
        }
    }

}
