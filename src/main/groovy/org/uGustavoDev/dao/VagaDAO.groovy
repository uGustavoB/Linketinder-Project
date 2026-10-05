package org.uGustavoDev.dao

import org.uGustavoDev.model.Vaga

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class VagaDAO {

    private final CompetenciaDAO competenciaDAO

    VagaDAO(CompetenciaDAO competenciaDAO) {
        this.competenciaDAO = competenciaDAO
    }

    private void preencherStatement(PreparedStatement stmt, Vaga vaga) throws SQLException {
        stmt.setInt(1, vaga.empresaId)
        stmt.setString(2, vaga.nome)
        stmt.setString(3, vaga.descricao)
        stmt.setString(4, vaga.estado)
        stmt.setString(5, vaga.cidade)
    }

    private Vaga extrairVaga(ResultSet rs) throws SQLException {
        Vaga v = new Vaga(
                rs.getInt("empresa_id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getString("estado"),
                rs.getString("cidade")
        )
        v.id = rs.getInt("id")

        List<String> competencias = competenciaDAO.listarPorVaga(v.id)
        v.adicionarCompetencias(competencias)

        return v
    }

    void inserir(Vaga vaga) {
        String sql = """
            INSERT INTO vagas (empresa_id, nome, descricao, estado, cidade)
            VALUES (?, ?, ?, ?, ?)
        """

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            preencherStatement(stmt, vaga)
            stmt.executeUpdate()
        }
    }

    List<Vaga> listar() {
        List<Vaga> vagas = []
        String sql = "SELECT * FROM vagas"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                vagas.add(extrairVaga(rs))
            }

            return vagas
        }
    }

    Vaga buscarPorId(int id) {
        String sql = "SELECT * FROM vagas WHERE id = ?"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairVaga(rs)
            }

            return null
        }
    }

    List<Vaga> listarPorEmpresa(int empresaId) {
        List<Vaga> vagas = []
        String sql = "SELECT * FROM vagas WHERE empresa_id = ?"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, empresaId)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                vagas.add(extrairVaga(rs))
            }
            return vagas
        }
    }

    void atualizar(Vaga vaga) {
        String sql = """
            UPDATE vagas
            SET empresa_id = ?, nome = ?, descricao = ?, estado = ?, cidade = ?
            WHERE id = ?
        """

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            preencherStatement(stmt, vaga)
            stmt.setInt(6, vaga.id)

            stmt.executeUpdate()
        }
    }

    void deletar(int id) {
        String sql = "DELETE FROM vagas WHERE id = ?"

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }

}
