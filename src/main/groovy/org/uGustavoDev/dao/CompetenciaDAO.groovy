package org.uGustavoDev.dao


import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CompetenciaDAO {
    private static void preencherStatement(PreparedStatement stmt, String nome) throws SQLException {
        stmt.setString(1, nome.trim())
    }

    int buscarOuInserir(String nome) {
        String sqlBusca = "SELECT id FROM competencias WHERE nome = ?"
        
        BaseDao.executarSQL(sqlBusca) { PreparedStatement stmtBusca ->
            preencherStatement(stmtBusca, nome)
            ResultSet rs = stmtBusca.executeQuery()

            if (rs.next()) {
                return rs.getInt("id")
            }
        }

        String sqlInsert = "INSERT INTO competencias (nome) VALUES (?)"
        
        BaseDao.executarSQL(sqlInsert) { PreparedStatement stmtInsert ->
            preencherStatement(stmtInsert, nome)
            stmtInsert.executeUpdate()
            ResultSet rs = stmtInsert.getGeneratedKeys()

            if (rs.next()) {
                return rs.getInt(1)
            }
        }
        
        throw new RuntimeException("Não foi possível obter o ID da competência recém-inserida.")
    }

    void vincularAoCandidato(int candidatoId, int competenciaId) {
        String sql = "INSERT INTO candidato_competencia (candidato_id, competencia_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
        
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, candidatoId)
            stmt.setInt(2, competenciaId)
            stmt.executeUpdate()
        }
    }
    
    List<String> listarPorCandidato(int candidatoId) {
        List<String> competencias = []

        String sql = """
            SELECT c.nome 
            FROM competencias c
            JOIN candidato_competencia cc ON c.id = cc.competencia_id
            WHERE cc.candidato_id = ?
        """
        
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, candidatoId)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                competencias.add(rs.getString("nome"))
            }
        }
        
        return competencias
    }

    void removerVinculosCandidato(int candidatoId) {
        String sql = "DELETE FROM candidato_competencia WHERE candidato_id = ?"
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, candidatoId)
            stmt.executeUpdate()
        }
    }

    void vincularAEmpresa(int empresaId, int competenciaId) {
        String sql = "INSERT INTO empresa_competencia (empresa_id, competencia_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
        
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, empresaId)
            stmt.setInt(2, competenciaId)
            stmt.executeUpdate()
        }
    }

    List<String> listarPorEmpresa(int empresaId) {
        List<String> competencias = []
        String sql = """
            SELECT c.nome 
            FROM competencias c
            JOIN empresa_competencia ec ON c.id = ec.competencia_id
            WHERE ec.empresa_id = ?
        """
        
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, empresaId)
            ResultSet rs = stmt.executeQuery()
            
            while (rs.next()) {
                competencias.add(rs.getString("nome"))
            }
        }
        
        return competencias
    }

    void removerVinculosEmpresa(int empresaId) {
        String sql = "DELETE FROM empresa_competencia WHERE empresa_id = ?"

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, empresaId)
            stmt.executeUpdate()
        }
    }

    void vincularAVaga(int vagaId, int competenciaId) {
        String sql = "INSERT INTO vaga_competencia (vaga_id, competencia_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
        
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, vagaId)
            stmt.setInt(2, competenciaId)
            stmt.executeUpdate()
        }
    }

    List<String> listarPorVaga(int vagaId) {
        List<String> competencias = []
        String sql = """
            SELECT c.nome 
            FROM competencias c
            JOIN vaga_competencia vc ON c.id = vc.competencia_id
            WHERE vc.vaga_id = ?
        """
        
        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, vagaId)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                competencias.add(rs.getString("nome"))
            }
        }
        
        return competencias
    }

    void removerVinculosVaga(int vagaId) {
        String sql = "DELETE FROM vaga_competencia WHERE vaga_id = ?"

        BaseDao.executarSQL(sql) { PreparedStatement stmt ->
            stmt.setInt(1, vagaId)
            stmt.executeUpdate()
        }
    }
}
