package org.uGustavoDev.service.interfaces

import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.model.Empresa

interface IEmpresaService {

    void adicionarEmpresa(EmpresaCadastroDTO empresa)
    List<Empresa> listarEmpresas()
    Empresa loginEmpresa(String email, String senha)
    void atualizarEmpresa(EmpresaCadastroDTO empresa)
    void deletarEmpresa(int id)

}