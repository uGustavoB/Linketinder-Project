package org.uGustavoDev.view

import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Vaga

class VagaView {
    private final GenericView genericoView

    VagaView(GenericView genericoView) {
        this.genericoView = genericoView
    }

    VagaCadastroDTO obterDadosVaga() {
        genericoView.mostrarCabecalho("Dados da Vaga")

        VagaCadastroDTO dto = new VagaCadastroDTO()
        dto.nome = genericoView.lerEntradaUsuario("Título da Vaga: ")
        dto.descricao = genericoView.lerEntradaUsuario("Descrição da Vaga: ")
        dto.cidade = genericoView.lerEntradaUsuario("Cidade: ")
        dto.estado = genericoView.lerEntradaUsuario("Estado (ex: SP): ")

        String compsStr = genericoView.lerEntradaUsuario("Competências exigidas (separadas por vírgula): ", true)
        dto.competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        return dto
    }

    void mostrarVagas(List<Vaga> vagas, String titulo = "Lista de Vagas") {
        if (vagas.isEmpty()) {
            genericoView.mostrarMensagem("Nenhuma vaga cadastrada no momento.")
        } else {
            genericoView.mostrarCabecalho(titulo)
            vagas.each { genericoView.mostrarMensagem(it.toString()) }
        }
    }
}
