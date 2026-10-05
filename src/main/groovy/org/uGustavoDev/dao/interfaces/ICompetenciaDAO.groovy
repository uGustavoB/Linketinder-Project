package org.uGustavoDev.dao.interfaces

interface ICompetenciaDAO {

    int buscarOuInserir(String nome)

    void vincularAoCandidato(int candidatoId, int competenciaId)

    List<String> listarPorCandidato(int candidatoId)

    void removerVinculosCandidato(int candidatoId)

    void vincularAEmpresa(int empresaId, int competenciaId)

    List<String> listarPorEmpresa(int empresaId)

    void removerVinculosEmpresa(int empresaId)

    void vincularAVaga(int vagaId, int competenciaId)

    List<String> listarPorVaga(int vagaId)

    void removerVinculosVaga(int vagaId)

}
