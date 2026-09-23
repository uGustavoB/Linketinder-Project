package org.uGustavoDev.model

import org.uGustavoDev.model.interfaces.IPessoa

abstract class Pessoa implements IPessoa {
    Integer id
    String nome
    String email
    String pais
    String estado
    String CEP
    String descricao
    String senha
    List<String> competencias = []

    Pessoa(String nome, String email, String estado, String pais, String CEP, String descricao) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.pais = pais
        this.CEP = CEP
        this.descricao = descricao
    }

    void adicionarCompetencias(List<String> novasCompetencias) {
        this.competencias.addAll(novasCompetencias)
    }

    void adicionarCompetencia(String novaCompetencia) {
        this.competencias.add(novaCompetencia)
    }

    void removerCompetencia(String competencia) {
        this.competencias.remove(competencia)
    }
}
