package org.uGustavoDev.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Vaga {
    Integer id
    Integer empresaId
    String nome
    String descricao
    String estado
    String cidade
    List<String> competencias = []

    Vaga(Integer empresaId, String nome, String descricao, String estado, String cidade) {
        this.empresaId = empresaId
        this.nome = nome
        this.descricao = descricao
        this.estado = estado
        this.cidade = cidade
    }

    void adicionarCompetencias(List<String> novasCompetencias) {
        this.competencias.addAll(novasCompetencias)
    }

    @Override
    String toString() {
        return """\
VAGA [#${id}]: ${nome}
Local: ${cidade}, ${estado}
Descrição: ${descricao}
Competências exigidas: ${competencias.isEmpty() ? 'Nenhuma' : competencias.join(', ')}
----------------------------------------"""
    }
}
