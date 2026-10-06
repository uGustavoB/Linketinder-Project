package org.uGustavoDev.dao

import org.uGustavoDev.dao.interfaces.IEmpresaDAO
import org.uGustavoDev.model.Empresa

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class EmpresaDAO implements IEmpresaDAO {

    private final CompetenciaDAO competenciaDAO

    EmpresaDAO(CompetenciaDAO competenciaDAO) {
        this.competenciaDAO = competenciaDAO
    }

    private void preencherStatement(PreparedStatement stmt, Empresa empresa) throws SQLException {
        stmt.setString(1, empresa.nome)
        stmt.setString(2, empresa.cnpj)
        stmt.setString(3, empresa.email)
        stmt.setString(4, empresa.descricao)
        stmt.setString(5, empresa.pais)
        stmt.setString(6, empresa.CEP)
        stmt.setString(7, empresa.senha ?: "123456")
    }

    private Empresa extrairEmpresa(ResultSet rs) throws SQLException {
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

        List<String> competencias = competenciaDAO.listarPorEmpresa(e.id)
        e.adicionarCompetencias(competencias)

        return e
    }

    @Override
    void inserir(Empresa empresa) {
        String sql = """
            INSERT INTO empresas (nome, cnpj, email, descricao, pais, cep, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """

        BaseDao.executarSQLComRetornoDeChave(sql) { PreparedStatement stmt ->
            preencherStatement(stmt, empresa)
            stmt.executeUpdate()
            
            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                empresa.id = rs.getInt(1)
            }
        }
    }

    @Override
    List<Empresa> listar() {
        List<Empresa> empresas = []
        String sql = "SELECT * FROM empresas"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                empresas.add(extrairEmpresa(rs))
            }

            return empresas
        }
    }

    @Override
    Empresa buscarPorId(int id) {
        String sql = "SELECT * FROM empresas WHERE id = ?"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairEmpresa(rs)
            }

            return null
        }
    }

    @Override
    Empresa buscarPorEmail(String email) {
        String sql = "SELECT * FROM empresas WHERE email = ?"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setString(1, email)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairEmpresa(rs)
            }

            return null
        }
    }

    @Override
    Empresa buscarPorCnpj(String cnpj) {
        String sql = "SELECT * FROM empresas WHERE cnpj = ?"

        return BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setString(1, cnpj)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                return extrairEmpresa(rs)
            }

            return null
        }
    }

    @Override
    void atualizar(Empresa empresa) {
        String sql = """
            UPDATE empresas
            SET nome = ?, cnpj = ?, email = ?, descricao = ?, pais = ?, cep = ?, senha = ?
            WHERE id = ?
        """

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            preencherStatement(stmt, empresa)
            stmt.setInt(8, empresa.id)

            stmt.executeUpdate()
        }
    }

    @Override
    void deletar(int id) {
        String sql = "DELETE FROM empresas WHERE id = ?"

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate()
        }
    }

}
