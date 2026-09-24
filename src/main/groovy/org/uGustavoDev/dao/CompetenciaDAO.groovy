package org.uGustavoDev.dao

import org.uGustavoDev.factory.ConexaoFactory

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class CompetenciaDAO {

    static int buscarOuInserir(String nome) {
        String sqlBusca = "SELECT id FROM competencias WHERE nome = ?"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)) {
            
            stmtBusca.setString(1, nome.trim())
            ResultSet rs = stmtBusca.executeQuery()
            
            if (rs.next()) {
                return rs.getInt("id")
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competência: ${e.message}", e)
        }

        String sqlInsert = "INSERT INTO competencias (nome) VALUES (?)"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
             
            stmtInsert.setString(1, nome.trim())
            stmtInsert.executeUpdate()
            
            ResultSet rs = stmtInsert.getGeneratedKeys()
            if (rs.next()) {
                return rs.getInt(1)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir competência: ${e.message}", e)
        }
        
        throw new RuntimeException("Não foi possível obter o ID da competência recém-inserida.")
    }

    static void vincularAoCandidato(int candidatoId, int competenciaId) {
        String sql = "INSERT INTO candidato_competencia (candidato_id, competencia_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, candidatoId)
            stmt.setInt(2, competenciaId)
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular competência ao candidato: ${e.message}", e)
        }
    }
    
    static List<String> listarPorCandidato(int candidatoId) {
        List<String> competencias = []
        String sql = """
            SELECT c.nome 
            FROM competencias c
            JOIN candidato_competencia cc ON c.id = cc.competencia_id
            WHERE cc.candidato_id = ?
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, candidatoId)
            ResultSet rs = stmt.executeQuery()
            
            while (rs.next()) {
                competencias.add(rs.getString("nome"))
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competências do candidato: ${e.message}", e)
        }
        
        return competencias
    }

    static void removerVinculosCandidato(int candidatoId) {
        String sql = "DELETE FROM candidato_competencia WHERE candidato_id = ?"
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, candidatoId)
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover vínculos de competência do candidato: ${e.message}", e)
        }
    }

    static void vincularAEmpresa(int empresaId, int competenciaId) {
        String sql = "INSERT INTO empresa_competencia (empresa_id, competencia_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, empresaId)
            stmt.setInt(2, competenciaId)
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular competência à empresa: ${e.message}", e)
        }
    }

    static List<String> listarPorEmpresa(int empresaId) {
        List<String> competencias = []
        String sql = """
            SELECT c.nome 
            FROM competencias c
            JOIN empresa_competencia ec ON c.id = ec.competencia_id
            WHERE ec.empresa_id = ?
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, empresaId)
            ResultSet rs = stmt.executeQuery()
            
            while (rs.next()) {
                competencias.add(rs.getString("nome"))
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competências da empresa: ${e.message}", e)
        }
        
        return competencias
    }
}
