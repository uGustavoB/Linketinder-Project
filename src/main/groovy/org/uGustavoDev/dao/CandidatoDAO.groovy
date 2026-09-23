package org.uGustavoDev.dao

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.factory.ConexaoFactory

import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Types

class CandidatoDAO {

    private static void preencherStatement(PreparedStatement stmt, Candidato candidato) throws SQLException {
        stmt.setString(1, candidato.nome)
        stmt.setString(2, candidato.sobrenome)
        if (candidato.dataNascimento != null) {
            stmt.setDate(3, Date.valueOf(candidato.dataNascimento))
        } else {
            stmt.setNull(3, Types.DATE)
        }

        stmt.setString(4, candidato.email)
        stmt.setString(5, candidato.cpf)
        stmt.setString(6, candidato.pais)
        stmt.setString(7, candidato.estado)
        stmt.setString(8, candidato.CEP)
        stmt.setString(9, candidato.descricao)
        stmt.setString(10, candidato.senha ?: "123456")
    }

    private static Candidato extrairCandidato(ResultSet rs) throws SQLException {
        Candidato c = new Candidato(
                rs.getString("nome"),
                rs.getString("sobrenome"),
                rs.getString("email"),
                rs.getString("estado"),
                rs.getString("pais"),
                rs.getString("cep"),
                rs.getString("descricao"),
                rs.getString("cpf"),
                null
        )
        c.id = rs.getInt("id")
        c.senha = rs.getString("senha")

        Date dataNascimentoSql = rs.getDate("data_nascimento")
        if (dataNascimentoSql != null) {
            c.dataNascimento = dataNascimentoSql.toLocalDate()
        }
        
        List<String> competencias = CompetenciaDAO.listarPorCandidato(c.id)
        c.adicionarCompetencias(competencias)
        
        return c
    }

    static void inserir(Candidato candidato) {
        String sql = """
            INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, estado, cep, descricao, senha) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherStatement(stmt, candidato)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                candidato.id = rs.getInt(1)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir candidato: ${e.message}", e)
        }
    }

    static List<Candidato> listar() {
        List<Candidato> candidatos = []
        String sql = "SELECT * FROM candidatos"

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                candidatos.add(extrairCandidato(rs))
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar candidatos: ${e.message}", e)
        }

        return candidatos
    }

    static Candidato buscarPorId(int id) {
        String sql = "SELECT * FROM candidatos WHERE id = ?"

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairCandidato(rs)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidato: ${e.message}", e)
        }

        return null
    }

    static Candidato buscarPorEmail(String email) {
        String sql = "SELECT * FROM candidatos WHERE email = ?"

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairCandidato(rs)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidato por email: ${e.message}", e)
        }

        return null
    }

    static Candidato buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM candidatos WHERE cpf = ?"

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairCandidato(rs)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidato por CPF: ${e.message}", e)
        }

        return null
    }

    static void atualizar(Candidato candidato) {
        String sql = """
            UPDATE candidatos 
            SET nome = ?, sobrenome = ?, data_nascimento = ?, email = ?, cpf = ?, pais = ?, estado = ?, cep = ?, descricao = ?, senha = ?
            WHERE id = ?
        """

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, candidato)
            stmt.setInt(11, candidato.id)

            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar candidato: ${e.message}", e)
        }
    }

    static void deletar(int id) {
        String sql = "DELETE FROM candidatos WHERE id = ?"

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id)
            stmt.executeUpdate()
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar candidato: ${e.message}", e)
        }
    }
}
