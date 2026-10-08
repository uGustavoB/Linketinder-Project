package org.uGustavoDev.factory

import io.github.cdimascio.dotenv.Dotenv

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class MysqlConexaoFactory implements IConexaoFactory {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load()

    private static final String URL = dotenv.get("MYSQL_DB_URL") ?: System.getenv("MYSQL_DB_URL") ?: "jdbc:mysql://localhost:3306/linketinder_db"
    private static final String USER = dotenv.get("MYSQL_DB_USER") ?: System.getenv("MYSQL_DB_USER") ?: "root"
    private static final String PASSWORD = dotenv.get("MYSQL_DB_PASSWORD") ?: System.getenv("MYSQL_DB_PASSWORD") ?: "root"

    @Override
    Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco de dados MySQL: ${e.message}", e)
        }
    }
}
