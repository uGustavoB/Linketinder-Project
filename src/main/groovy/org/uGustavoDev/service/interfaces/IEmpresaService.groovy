package org.uGustavoDev.service.interfaces

import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.model.Empresa

interface IEmpresaService {

    void adicionarEmpresa(EmpresaCadastroDTO dto)
    List<Empresa> listarEmpresas()
    Empresa loginEmpresa(String email, String senha)
    Empresa atualizarEmpresa(int id, EmpresaCadastroDTO dto)
    void deletarEmpresa(int id)

}
