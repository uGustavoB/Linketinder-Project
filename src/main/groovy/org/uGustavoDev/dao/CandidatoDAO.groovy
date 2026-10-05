package org.uGustavoDev.dao

import org.uGustavoDev.dao.interfaces.ICandidatoDAO
import org.uGustavoDev.model.Candidato

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class CandidatoDAO implements ICandidatoDAO {

    private final CompetenciaDAO competenciaDAO

    CandidatoDAO(CompetenciaDAO competenciaDAO) {
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

        List<String> competencias = competenciaDAO.listarPorCandidato(c.id)
        c.adicionarCompetencias(competencias)

        return c
    }

    @Override
    void inserir(Candidato candidato) {
        String sql = """
            INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, estado, cep, descricao, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

        BaseDao.executarSQL(sql) { stmt ->
            preencherStatement(stmt, candidato)
            stmt.executeUpdate()
        }
    }

    @Override
    List<Candidato> listar() {
        List<Candidato> candidatos = []
        String sql = "SELECT * FROM candidatos"

        return BaseDao.executarSQL(sql) { stmt ->
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

        return BaseDao.executarSQL(sql) { stmt ->
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

        return BaseDao.executarSQL(sql) { stmt ->
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

        return BaseDao.executarSQL(sql) { stmt ->
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

        BaseDao.executarSQL(sql) { stmt ->
            preencherStatement(stmt, candidato)
            stmt.setInt(11, candidato.id)
            stmt.executeUpdate()
        }
    }

    @Override
    void deletar(int id) {
        String sql = "DELETE FROM candidatos WHERE id = ?"
        BaseDao.executarSQL(sql) { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }

}
