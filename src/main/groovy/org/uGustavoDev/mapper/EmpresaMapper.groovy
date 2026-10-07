package org.uGustavoDev.mapper

import org.uGustavoDev.builder.EmpresaBuilder
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.mapper.interfaces.IEmpresaMapper
import org.uGustavoDev.model.Empresa

class EmpresaMapper implements IEmpresaMapper {

    @Override
    Empresa paraEntidade(EmpresaCadastroDTO dto) {
        Empresa empresa = new EmpresaBuilder()
                .nome(dto.nome)
                .email(dto.email)
                .pais(dto.pais)
                .cep(dto.cep)
                .descricao(dto.descricao)
                .cnpj(dto.cnpj)
                .competencias(dto.competencias)
                .build()
        empresa.senha = dto.senha
        return empresa
    }

}
