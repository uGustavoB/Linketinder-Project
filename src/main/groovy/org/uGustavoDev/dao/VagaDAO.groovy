package org.uGustavoDev.dao

import org.uGustavoDev.factory.ConexaoFactory
import org.uGustavoDev.model.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp

class VagaDAO {

    static void inserir(Vaga vaga) {
        String sql = """
            INSERT INTO vagas (empresa_id, nome, descricao, estado, cidade, criado_em)
            VALUES (?, ?, ?, ?, ?, ?)
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            stmt.setInt(1, vaga.empresaId)
            stmt.setString(2, vaga.nome)
            stmt.setString(3, vaga.descricao)
            stmt.setString(4, vaga.estado)
            stmt.setString(5, vaga.cidade)
            stmt.setTimestamp(6, Timestamp.valueOf(vaga.criadoEm))
            
            stmt.executeUpdate()
            
            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                vaga.id = rs.getInt(1)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir vaga: ${e.message}", e)
        }
    }
}
