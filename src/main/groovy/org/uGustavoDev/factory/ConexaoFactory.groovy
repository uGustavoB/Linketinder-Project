package org.uGustavoDev.factory

import io.github.cdimascio.dotenv.Dotenv
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConexaoFactory {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load()

    private static final String URL = dotenv.get("DB_URL") ?: System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/linketinder_db"
    private static final String USER = dotenv.get("DB_USER") ?: System.getenv("DB_USER") ?: "postgres"
    private static final String PASSWORD = dotenv.get("DB_PASSWORD") ?: System.getenv("DB_PASSWORD") ?: "postgres"

    static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco de dados: ${e.message}", e)
        }
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
