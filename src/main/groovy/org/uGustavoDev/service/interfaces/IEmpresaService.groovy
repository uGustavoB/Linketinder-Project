package org.uGustavoDev.service.interfaces

import org.uGustavoDev.model.Empresa

interface IEmpresaService {

    void adicionarEmpresa(Empresa empresa)
    List<Empresa> listarEmpresas()
    Empresa loginEmpresa(String email, String senha)
    void atualizarEmpresa(Empresa empresa)
    void deletarEmpresa(int id)

}