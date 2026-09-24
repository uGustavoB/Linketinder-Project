package org.uGustavoDev.model

import java.time.LocalDateTime

class Vaga {
    Integer id
    Integer empresaId
    String nome
    String descricao
    String estado
    String cidade
    LocalDateTime criadoEm
    List<String> competencias = []

    Vaga(Integer empresaId, String nome, String descricao, String estado, String cidade) {
        this.empresaId = empresaId
        this.nome = nome
        this.descricao = descricao
        this.estado = estado
        this.cidade = cidade
        this.criadoEm = LocalDateTime.now()
    }

    void adicionarCompetencias(List<String> novasCompetencias) {
        this.competencias.addAll(novasCompetencias)
    }

    @Override
    String toString() {
        return "Vaga[id=${id}, empresaId=${empresaId}, nome='${nome}', descricao='${descricao}', estado='${estado}', cidade='${cidade}', criadoEm=${criadoEm}, competencias=${competencias}]"
    }
}
