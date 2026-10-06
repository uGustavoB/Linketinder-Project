package org.uGustavoDev.dao.interfaces

interface IDatabaseTemplate {

    <T> T executarSQL(String sql, Closure closure)
    <T> T executarSQLComRetornoDeChave(String sql, Closure closure)

}
