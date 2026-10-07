package org.uGustavoDev.dao

import org.uGustavoDev.builder.CandidatoBuilder
import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.dao.interfaces.ICompetenciaDAO
import org.uGustavoDev.dao.interfaces.IDatabaseTemplate
import org.uGustavoDev.model.Candidato

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.sql.Date

class CandidatoDAO implements ICandidatoDAO {

    private final IDatabaseTemplate db
    private final ICompetenciaDAO competenciaDAO

    CandidatoDAO(IDatabaseTemplate db, ICompetenciaDAO competenciaDAO) {
        this.db = db
        this.competenciaDAO = competenciaDAO
    }

    private void preencherStatement(PreparedStatement stmt, Candidato candidato) throws SQLException {
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

    private Candidato extrairCandidato(ResultSet rs) throws SQLException {
        Date dataNascimentoSql = rs.getDate("data_nascimento")
        
        Candidato candidato = new CandidatoBuilder()
                .id(rs.getInt("id"))
                .nome(rs.getString("nome"))
                .sobrenome(rs.getString("sobrenome"))
                .email(rs.getString("email"))
                .estado(rs.getString("estado"))
                .pais(rs.getString("pais"))
                .CEP(rs.getString("cep"))
                .descricao(rs.getString("descricao"))
                .cpf(rs.getString("cpf"))
                .dataNascimento(dataNascimentoSql != null ? dataNascimentoSql.toLocalDate() : null)
                .build()
                
        candidato.senha = rs.getString("senha")

        List<String> competencias = competenciaDAO.listarPorCandidato(candidato.id)
        candidato.adicionarCompetencias(competencias)

        return candidato
    }

    @Override
    void inserir(Candidato candidato) {
        String sql = """
            INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, estado, cep, descricao, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

        db.executarSQLComRetornoDeChave(sql) { stmt ->
            preencherStatement(stmt, candidato)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                candidato.id = rs.getInt(1)
            }
        }
    }

    @Override
    List<Candidato> listar() {
        List<Candidato> candidatos = []
        String sql = "SELECT * FROM candidatos"

        return db.executarSQL(sql) { stmt ->
            ResultSet rs = stmt.executeQuery()
            while (rs.next()) {
                candidatos.add(extrairCandidato(rs))
            }
            return candidatos
        }
    }

    @Override
    Candidato buscarPorId(int id) {
        String sql = "SELECT * FROM candidatos WHERE id = ?"

        return db.executarSQL(sql) { stmt ->
            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairCandidato(rs)
            }

            return null
        }
    }

    @Override
    Candidato buscarPorEmail(String email) {
        String sql = "SELECT * FROM candidatos WHERE email = ?"

        return db.executarSQL(sql) { stmt ->
            stmt.setString(1, email)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairCandidato(rs)
            }

            return null
        }
    }

    @Override
    Candidato buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM candidatos WHERE cpf = ?"

        return db.executarSQL(sql) { stmt ->
            stmt.setString(1, cpf)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairCandidato(rs)
            }
            return null
        }
    }

    @Override
    void atualizar(Candidato candidato) {
        String sql = """
            UPDATE candidatos
            SET nome = ?, sobrenome = ?, data_nascimento = ?, email = ?, cpf = ?, pais = ?, estado = ?, cep = ?, descricao = ?, senha = ?
            WHERE id = ?
        """

        db.executarSQL(sql) { stmt ->
            preencherStatement(stmt, candidato)
            stmt.setInt(11, candidato.id)
            stmt.executeUpdate()
        }
    }

    @Override
    void deletar(int id) {
        String sql = "DELETE FROM candidatos WHERE id = ?"
        db.executarSQL(sql) { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }

}
