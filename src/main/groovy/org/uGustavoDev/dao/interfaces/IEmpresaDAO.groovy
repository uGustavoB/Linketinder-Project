package org.uGustavoDev.dao.interfaces

import org.uGustavoDev.model.Empresa

interface IEmpresaDAO {

    void inserir(Empresa empresa)

    List<Empresa> listar()

    Empresa buscarPorId(int id)

    Empresa buscarPorEmail(String email)

    Empresa buscarPorCnpj(String cnpj)

    void atualizar(Empresa empresa)

    void deletar(int id)

}
