package org.uGustavoDev.dao

import org.uGustavoDev.model.Empresa
import org.uGustavoDev.factory.ConexaoFactory

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class EmpresaDAO {

    private static void preencherStatement(PreparedStatement stmt, Empresa empresa) throws SQLException {
        stmt.setString(1, empresa.nome)
        stmt.setString(2, empresa.cnpj)
        stmt.setString(3, empresa.email)
        stmt.setString(4, empresa.descricao)
        stmt.setString(5, empresa.pais)
        stmt.setString(6, empresa.CEP)
        stmt.setString(7, empresa.senha ?: "123456")
    }

    private static Empresa extrairEmpresa(ResultSet rs) throws SQLException {
        Empresa e = new Empresa(
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("pais"),
                rs.getString("cep"),
                rs.getString("descricao"),
                rs.getString("cnpj")
        )
        e.id = rs.getInt("id")
        e.senha = rs.getString("senha")
        return e
    }

    static void inserir(Empresa empresa) {
        String sql = """
            INSERT INTO empresas (nome, cnpj, email, descricao, pais, cep, senha) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            preencherStatement(stmt, empresa)
            stmt.executeUpdate()
            
            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                empresa.id = rs.getInt(1)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir empresa: ${e.message}", e)
        }
    }

    static List<Empresa> listar() {
        List<Empresa> empresas = []
        String sql = "SELECT * FROM empresas"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                empresas.add(extrairEmpresa(rs))
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empresas: ${e.message}", e)
        }
        
        return empresas
    }

    static Empresa buscarPorId(int id) {
        String sql = "SELECT * FROM empresas WHERE id = ?"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()
            
            if (rs.next()) {
                return extrairEmpresa(rs)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empresa: ${e.message}", e)
        }
        
        return null
    }

    static void atualizar(Empresa empresa) {
        String sql = """
            UPDATE empresas 
            SET nome = ?, cnpj = ?, email = ?, descricao = ?, pais = ?, cep = ?, senha = ?
            WHERE id = ?
        """
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            preencherStatement(stmt, empresa)
            stmt.setInt(8, empresa.id)
            
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar empresa: ${e.message}", e)
        }
    }

    static void deletar(int id) {
        String sql = "DELETE FROM empresas WHERE id = ?"
        
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id)
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar empresa: ${e.message}", e)
        }
    }
}
