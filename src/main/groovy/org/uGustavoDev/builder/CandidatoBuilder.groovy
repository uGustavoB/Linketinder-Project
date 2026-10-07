package org.uGustavoDev.builder

import org.uGustavoDev.model.Candidato

import java.time.LocalDate

class CandidatoBuilder {

    private String nome
    private String sobrenome
    private String email
    private String senha
    private String estado
    private String pais
    private String CEP
    private String descricao
    private String cpf
    private LocalDate dataNascimento
    private List<String> competencias = []
    private Integer id

    CandidatoBuilder nome(String nome) {
        this.nome = nome
        return this
    }

    CandidatoBuilder sobrenome(String sobrenome) {
        this.sobrenome = sobrenome
        return this
    }

    CandidatoBuilder email(String email) {
        this.email = email
        return this
    }

    CandidatoBuilder senha(String senha) {
        this.senha = senha
        return this
    }

    CandidatoBuilder estado(String estado) {
        this.estado = estado
        return this
    }

    CandidatoBuilder pais(String pais) {
        this.pais = pais
        return this
    }

    CandidatoBuilder CEP(String CEP) {
        this.CEP = CEP
        return this
    }

    CandidatoBuilder descricao(String descricao) {
        this.descricao = descricao
        return this
    }

    CandidatoBuilder cpf(String cpf) {
        this.cpf = cpf
        return this
    }

    CandidatoBuilder dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento
        return this
    }

    CandidatoBuilder competencias(List<String> competencias) {
        this.competencias = competencias
        return this
    }

    CandidatoBuilder id(Integer id) {
        this.id = id
        return this
    }

    Candidato build() {
        Candidato candidato = new Candidato(nome, sobrenome, email, estado, pais, CEP, descricao, cpf, dataNascimento, senha)
        if (id != null) {
            candidato.id = id
        }
        if (competencias && !competencias.isEmpty()) {
            candidato.adicionarCompetencias(competencias)
        }
        return candidato
    }

}
