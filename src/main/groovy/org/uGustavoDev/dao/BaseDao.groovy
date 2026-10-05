package org.uGustavoDev.dao

import org.uGustavoDev.factory.ConexaoFactory

import java.sql.Connection
import java.sql.PreparedStatement

class BaseDao {
    static <T> T executarSQL(String sql, Closure closure) {
        try (
                Connection conexao = ConexaoFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ){
            closure.delegate = stmt
            return closure(stmt)
        } catch (Exception e) {
            throw new RuntimeException("Erro ao executar SQL: " + e.message, e)
        }
    }
}
