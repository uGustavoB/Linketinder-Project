package org.uGustavoDev.dao

import org.uGustavoDev.exceptions.DatabaseOperationException
import org.uGustavoDev.factory.ConexaoFactory

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class BaseDao {

    static <T> T executarSQL(String sql, Closure closure) {
        try (Connection conexao = ConexaoFactory.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            closure.delegate = stmt
            return closure(stmt)
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao executar SQL: " + e.message, e)
        }
    }

}
