package org.uGustavoDev.mapper.interfaces

import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.model.Empresa

interface IEmpresaMapper {

    Empresa paraEntidade(EmpresaCadastroDTO dto)

}
