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
        String dataCriacao = criadoEm != null ? criadoEm.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Data não informada"
        return """\
VAGA [${id}]: ${nome}
Local: ${cidade}, ${estado}
Criada em: ${dataCriacao}
Descrição: ${descricao}
Competências exigidas: ${competencias.isEmpty() ? 'Nenhuma' : competencias.join(', ')}
----------------------------------------"""
    }
}
