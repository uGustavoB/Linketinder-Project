package org.uGustavoDev.mapper

import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.mapper.interfaces.IRecrutadorMapper
import org.uGustavoDev.model.Recrutador
import spock.lang.Specification
import java.time.LocalDate

class RecrutadorMapperSpec extends Specification {

    void "deve mapear um RecrutadorCadastroDTO para a entidade Recrutador corretamente"() {
        given: "um DTO preenchido com dados de recrutador"
        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(
                empresaId: 1,
                nome: "Ana Beatriz",
                area: "TI",
                descricao: "Recrutadora Tech",
                email: "ana.beatriz@empresa.com",
                cpf: "123.456.789-10",
                dataNascimento: LocalDate.of(1992, 4, 12),
                senha: "senha123"
        )

        IRecrutadorMapper mapper = new RecrutadorMapper()

        when: "o mapper converte o DTO para entidade"
        Recrutador recrutador = mapper.paraEntidade(dto)

        then: "todos os campos da entidade devem refletir exatamente os dados do DTO"
        recrutador.empresaId == dto.empresaId
        recrutador.nome == dto.nome
        recrutador.area == dto.area
        recrutador.descricao == dto.descricao
        recrutador.email == dto.email
        recrutador.cpf == dto.cpf
        recrutador.dataNascimento == dto.dataNascimento
        recrutador.senha == dto.senha
    }

}

