package org.uGustavoDev.view

import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.model.Empresa

class EmpresaView {

    private final GenericView genericoView

    EmpresaView(GenericView genericoView) {
        this.genericoView = genericoView
    }

    EmpresaCadastroDTO obterDadosEmpresa() {
        genericoView.mostrarCabecalho("Cadastro de Empresa")

        EmpresaCadastroDTO dto = new EmpresaCadastroDTO()
        dto.nome = genericoView.lerEntradaUsuario("Nome da Empresa: ")
        dto.email = genericoView.lerEntradaUsuario("Email Corporativo: ")
        dto.senha = genericoView.lerEntradaUsuario("Senha (mínimo 6 caracteres): ")
        dto.pais = genericoView.lerEntradaUsuario("País: ")
        dto.cep = genericoView.lerEntradaUsuario("CEP: ")
        dto.descricao = genericoView.lerEntradaUsuario("Descrição da Empresa: ")
        dto.cnpj = genericoView.lerEntradaUsuario("CNPJ: ")

        String compsStr = genericoView.lerEntradaUsuario("Competências requeridas (separadas por vírgula): ", true)
        dto.competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        return dto
    }

    void mostrarPerfilEmpresa(Empresa empresa) {
        genericoView.mostrarCabecalho("Meu Perfil (Empresa)")
        genericoView.mostrarMensagem(empresa.toString())
    }

}
