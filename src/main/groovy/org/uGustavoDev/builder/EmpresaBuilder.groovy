package org.uGustavoDev.builder

import org.uGustavoDev.model.Empresa

class EmpresaBuilder {

    private String nome
    private String email
    private String senha
    private String pais
    private String CEP
    private String descricao
    private String cnpj
    private List<String> competencias = []
    private Integer id

    EmpresaBuilder nome(String nome) {
        this.nome = nome
        return this
    }

    EmpresaBuilder email(String email) {
        this.email = email
        return this
    }

    EmpresaBuilder senha(String senha) {
        this.senha = senha
        return this
    }

    EmpresaBuilder pais(String pais) {
        this.pais = pais
        return this
    }

    EmpresaBuilder cep(String CEP) {
        this.CEP = CEP
        return this
    }

    EmpresaBuilder descricao(String descricao) {
        this.descricao = descricao
        return this
    }

    EmpresaBuilder cnpj(String cnpj) {
        this.cnpj = cnpj
        return this
    }

    EmpresaBuilder competencias(List<String> competencias) {
        this.competencias = competencias
        return this
    }

    EmpresaBuilder id(Integer id) {
        this.id = id
        return this
    }

    Empresa build() {
        Empresa empresa = new Empresa(nome, email, pais, CEP, descricao, cnpj, senha)
        if (id != null) {
            empresa.id = id
        }
        if (competencias && !competencias.isEmpty()) {
            empresa.adicionarCompetencias(competencias)
        }
        return empresa
    }

}
