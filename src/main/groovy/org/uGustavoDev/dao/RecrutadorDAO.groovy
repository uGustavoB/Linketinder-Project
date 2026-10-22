package org.uGustavoDev.dao

import groovy.transform.CompileStatic
import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.dao.interfaces.IRecrutadorDAO
import org.uGustavoDev.dao.interfaces.IDatabaseTemplate
import org.uGustavoDev.model.Recrutador

import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

@CompileStatic
class RecrutadorDAO implements IRecrutadorDAO {

    private final IDatabaseTemplate db

    RecrutadorDAO(IDatabaseTemplate db) {
        this.db = db
    }

    private void preencherStatement(PreparedStatement stmt, Recrutador recrutador) throws SQLException {
        stmt.setInt(1, recrutador.empresaId)
        stmt.setString(2, recrutador.nome)
        stmt.setString(3, recrutador.area)
        stmt.setString(4, recrutador.descricao)
        stmt.setString(5, recrutador.email)
        stmt.setString(6, recrutador.cpf)
        if (recrutador.dataNascimento != null) {
            stmt.setDate(7, Date.valueOf(recrutador.dataNascimento))
        } else {
            stmt.setNull(7, Types.DATE)
        }
        stmt.setString(8, recrutador.senha ?: "123456")
    }

    private Recrutador extrairRecrutador(ResultSet rs) throws SQLException {
        Date dataNascSql = rs.getDate("data_nascimento")

        return new RecrutadorBuilder()
                .id(rs.getInt("id"))
                .empresaId(rs.getInt("empresa_id"))
                .nome(rs.getString("nome"))
                .area(rs.getString("area"))
                .descricao(rs.getString("descricao"))
                .email(rs.getString("email"))
                .cpf(rs.getString("cpf"))
                .dataNascimento(dataNascSql != null ? dataNascSql.toLocalDate() : null)
                .senha(rs.getString("senha"))
                .build()
    }

    @Override
    void inserir(Recrutador recrutador) {
        String sql = """
            INSERT INTO recrutadores (empresa_id, nome, area, descricao, email, cpf, data_nascimento, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """

        db.executarSQLComRetornoDeChave(sql) { PreparedStatement stmt ->
            preencherStatement(stmt, recrutador)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                recrutador.id = rs.getInt(1)
            }
        }
    }

    @Override
    List<Recrutador> listar() {
        List<Recrutador> recrutadores = []
        String sql = "SELECT * FROM recrutadores"

        return db.executarSQL(sql) { PreparedStatement stmt ->
            ResultSet rs = stmt.executeQuery()
            while (rs.next()) {
                recrutadores.add(extrairRecrutador(rs))
            }
            return recrutadores
        }
    }

    @Override
    List<Recrutador> listarPorEmpresa(int empresaId) {
        List<Recrutador> recrutadores = []
        String sql = "SELECT * FROM recrutadores WHERE empresa_id = ?"

        return db.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, empresaId)
            ResultSet rs = stmt.executeQuery()
            while (rs.next()) {
                recrutadores.add(extrairRecrutador(rs))
            }
            return recrutadores
        }
    }

    @Override
    Recrutador buscarPorId(int id) {
        String sql = "SELECT * FROM recrutadores WHERE id = ?"

        return db.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairRecrutador(rs)
            }
            return null
        }
    }

    @Override
    Recrutador buscarPorEmail(String email) {
        String sql = "SELECT * FROM recrutadores WHERE email = ?"

        return db.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setString(1, email)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairRecrutador(rs)
            }
            return null
        }
    }

    @Override
    Recrutador buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM recrutadores WHERE cpf = ?"

        return db.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setString(1, cpf)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairRecrutador(rs)
            }
            return null
        }
    }

    @Override
    void atualizar(Recrutador recrutador) {
        String sql = """
            UPDATE recrutadores
            SET empresa_id = ?, nome = ?, area = ?, descricao = ?, email = ?, cpf = ?, data_nascimento = ?, senha = ?
            WHERE id = ?
        """

        db.executarSQL(sql) { PreparedStatement stmt ->
            preencherStatement(stmt, recrutador)
            stmt.setInt(9, recrutador.id)
            stmt.executeUpdate()
        }
    }

    @Override
    void deletar(int id) {
        String sql = "DELETE FROM recrutadores WHERE id = ?"
        db.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }
}
