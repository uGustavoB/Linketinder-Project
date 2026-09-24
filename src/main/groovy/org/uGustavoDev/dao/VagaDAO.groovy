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

    private static void preencherStatement(PreparedStatement stmt, Vaga vaga) throws SQLException {
        stmt.setInt(1, vaga.empresaId)
        stmt.setString(2, vaga.nome)
        stmt.setString(3, vaga.descricao)
        stmt.setString(4, vaga.estado)
        stmt.setString(5, vaga.cidade)
    }

    private static Vaga extrairVaga(ResultSet rs) throws SQLException {
        Vaga v = new Vaga(
                rs.getInt("empresa_id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getString("estado"),
                rs.getString("cidade")
        )
        v.id = rs.getInt("id")
        
        List<String> competencias = CompetenciaDAO.listarPorVaga(v.id)
        v.adicionarCompetencias(competencias)
        
        return v
    }

    static void inserir(Vaga vaga) {
        String sql = """
            INSERT INTO vagas (empresa_id, nome, descricao, estado, cidade)
            VALUES (?, ?, ?, ?, ?)
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            preencherStatement(stmt, vaga)
            
            stmt.executeUpdate()
            
            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                vaga.id = rs.getInt(1)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir vaga: ${e.message}", e)
        }
    }

    static List<Vaga> listar() {
        List<Vaga> vagas = []
        String sql = "SELECT * FROM vagas"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                vagas.add(extrairVaga(rs))
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vagas: ${e.message}", e)
        }
        
        return vagas
    }

    static Vaga buscarPorId(int id) {
        String sql = "SELECT * FROM vagas WHERE id = ?"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()
            
            if (rs.next()) {
                return extrairVaga(rs)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vaga: ${e.message}", e)
        }
        
        return null
    }

    static List<Vaga> listarPorEmpresa(int empresaId) {
        List<Vaga> vagas = []
        String sql = "SELECT * FROM vagas WHERE empresa_id = ?"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, empresaId)
            ResultSet rs = stmt.executeQuery()
            
            while (rs.next()) {
                vagas.add(extrairVaga(rs))
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vagas por empresa: ${e.message}", e)
        }
        
        return vagas
    }

    static void atualizar(Vaga vaga) {
        String sql = """
            UPDATE vagas 
            SET empresa_id = ?, nome = ?, descricao = ?, estado = ?, cidade = ?
            WHERE id = ?
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            preencherStatement(stmt, vaga)
            stmt.setInt(6, vaga.id)
            
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar vaga: ${e.message}", e)
        }
    }

    static void deletar(int id) {
        String sql = "DELETE FROM vagas WHERE id = ?"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id)
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar vaga: ${e.message}", e)
        }
    }
}
