package org.uGustavoDev.builder

import org.uGustavoDev.model.Vaga

class VagaBuilder {

    Integer id
    Integer empresaId
    String nome
    String descricao
    String estado
    String cidade
    List<String> competencias = []

    VagaBuilder id(Integer id) {
        this.id = id
        return this
    }

    VagaBuilder empresaId(Integer empresaId) {
        this.empresaId = empresaId
        return this
    }

    VagaBuilder nome(String nome) {
        this.nome = nome
        return this
    }

    VagaBuilder descricao(String descricao) {
        this.descricao = descricao
        return this
    }

    VagaBuilder estado(String estado) {
        this.estado = estado
        return this
    }

    VagaBuilder cidade(String cidade) {
        this.cidade = cidade
        return this
    }

    VagaBuilder competencias(List<String> competencias) {
        this.competencias = competencias
        return this
    }

    Vaga build() {
        Vaga vaga = new Vaga(empresaId, nome, descricao, estado, cidade)
        if (this.id != null) {
            vaga.id = this.id
        }
        if (this.competencias != null) {
            vaga.competencias = this.competencias
        }
        return vaga
    }

}
