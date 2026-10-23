package org.uGustavoDev.service.interfaces

import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Recrutador

interface IRecrutadorService {

    Recrutador adicionarRecrutador(RecrutadorCadastroDTO dto)

    List<Recrutador> listarRecrutadores()

    List<Recrutador> listarRecrutadoresPorEmpresa(int empresaId)

    Recrutador buscarRecrutadorPorId(int id)

    Recrutador loginRecrutador(String email, String senha)

    Recrutador atualizarRecrutador(int id, RecrutadorCadastroDTO dto)

    void deletarRecrutador(int id)

}
