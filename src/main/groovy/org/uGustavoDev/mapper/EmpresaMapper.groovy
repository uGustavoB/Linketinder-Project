package org.uGustavoDev.mapper

import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.model.Empresa

class EmpresaMapper implements IEmpresaMapper {
    @Override
    Empresa paraEntidade(EmpresaCadastroDTO dto) {
        Empresa empresa = new Empresa(
                dto.nome,
                dto.email,
                dto.pais,
                dto.cep,
                dto.descricao,
                dto.cnpj
        )
        empresa.senha = dto.senha
        empresa.competencias = dto.competencias
        return empresa
    }
}
