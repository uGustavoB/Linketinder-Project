package org.uGustavoDev.view

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.model.Candidato

class CandidatoView {
    private final GenericView genericoView

    CandidatoView(GenericView genericoView) {
        this.genericoView = genericoView
    }

    CandidatoCadastroDTO obterDadosCandidato() {
        genericoView.mostrarCabecalho("Cadastro de Candidato")

        CandidatoCadastroDTO dto = new CandidatoCadastroDTO()
        dto.nome = genericoView.lerEntradaUsuario("Nome: ")
        dto.sobrenome = genericoView.lerEntradaUsuario("Sobrenome: ")
        dto.email = genericoView.lerEntradaUsuario("Email: ")
        dto.senha = genericoView.lerEntradaUsuario("Senha (mínimo 6 caracteres): ")
        dto.estado = genericoView.lerEntradaUsuario("Estado (ex: SP): ")
        dto.pais = genericoView.lerEntradaUsuario("País: ")
        dto.cep = genericoView.lerEntradaUsuario("CEP: ")
        dto.descricao = genericoView.lerEntradaUsuario("Descrição Pessoal: ")
        dto.cpf = genericoView.lerEntradaUsuario("CPF: ")
        dto.dataNascimento = genericoView.lerData("Data de Nascimento (DD/MM/AAAA): ")

        String compsStr = genericoView.lerEntradaUsuario("Competências (separadas por vírgula): ", true)
        dto.competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        return dto
    }

    void mostrarPerfilCandidato(Candidato candidato) {
        genericoView.mostrarCabecalho("Perfil do Candidato")
        genericoView.mostrarMensagem(candidato.toString())
    }

    void mostrarCandidatos(List<Candidato> candidatos) {
        genericoView.mostrarCabecalho("Lista de Candidatos Disponíveis")
        if (candidatos.isEmpty()) {
            genericoView.mostrarMensagem("Nenhum candidato cadastrado no momento.")
        } else {
            candidatos.each { genericoView.mostrarMensagem(it.toAnonymousString()) }
        }
    }
}
