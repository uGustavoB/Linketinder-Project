package org.uGustavoDev.view

import groovy.transform.CompileStatic
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Recrutador

import java.time.format.DateTimeFormatter

@CompileStatic
class RecrutadorView {

    private final GenericView genericoView
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    RecrutadorView(GenericView genericoView) {
        this.genericoView = genericoView
    }

    RecrutadorCadastroDTO obterDadosRecrutador(Integer empresaId = null) {
        genericoView.mostrarCabecalho("Cadastro de Recrutador")

        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO()
        dto.empresaId = empresaId
        dto.nome = genericoView.lerEntradaUsuario("Nome: ")
        dto.email = genericoView.lerEntradaUsuario("Email: ")
        dto.senha = genericoView.lerEntradaUsuario("Senha (mínimo 6 caracteres): ")
        dto.area = genericoView.lerEntradaUsuario("Área de Atuação (ex: RH Tech): ")
        dto.descricao = genericoView.lerEntradaUsuario("Descrição Profissional: ")
        dto.cpf = genericoView.lerEntradaUsuario("CPF: ")
        dto.dataNascimento = genericoView.lerData("Data de Nascimento (DD/MM/AAAA): ")

        return dto
    }

    void mostrarPerfilRecrutador(Recrutador recrutador, Empresa empresa = null) {
        genericoView.mostrarCabecalho("Meu Perfil (Recrutador)")
        genericoView.mostrarMensagem("ID: #${recrutador.id}")
        genericoView.mostrarMensagem("Nome: ${recrutador.nome}")
        genericoView.mostrarMensagem("Email: ${recrutador.email}")
        genericoView.mostrarMensagem("Área: ${recrutador.area ?: 'N/A'}")
        genericoView.mostrarMensagem("Descrição: ${recrutador.descricao ?: 'N/A'}")
        genericoView.mostrarMensagem("CPF: ${recrutador.cpf}")
        String dataStr = recrutador.dataNascimento ? recrutador.dataNascimento.format(DATE_FORMATTER) : "N/A"
        genericoView.mostrarMensagem("Data de Nascimento: ${dataStr}")

        if (empresa != null) {
            genericoView.mostrarMensagem("Empresa Associada: ${empresa.nome} (CNPJ: ${empresa.cnpj})")
        } else if (recrutador.empresaId != null) {
            genericoView.mostrarMensagem("Empresa Associada: ID #${recrutador.empresaId}")
        }
    }

    int mostrarMenuPerfil() {
        genericoView.mostrarCabecalho("Menu de Perfil (Recrutador)")
        genericoView.mostrarMensagem("1 - Ver meu perfil")
        genericoView.mostrarMensagem("2 - Editar meu perfil")
        genericoView.mostrarMensagem("0 - Voltar")
        return genericoView.lerEscolha("Sua escolha: ", 0, 2)
    }

    int mostrarMenuRecrutador(Recrutador recrutador) {
        genericoView.mostrarCabecalho("Linketinder - Área do Recrutador")
        genericoView.mostrarMensagem("Olá, ${recrutador.nome}!")
        genericoView.mostrarMensagem("1 - Meu Perfil")
        genericoView.mostrarMensagem("2 - Gerenciar Vagas da Empresa")
        genericoView.mostrarMensagem("3 - Explorar Candidatos")
        genericoView.mostrarMensagem("0 - Sair (Logout)")
        return genericoView.lerEscolha("Sua escolha: ", 0, 3)
    }

    void mostrarRecrutadores(List<Recrutador> recrutadores, String titulo = "Lista de Recrutadores") {
        genericoView.mostrarCabecalho(titulo)
        if (recrutadores == null || recrutadores.isEmpty()) {
            genericoView.mostrarMensagem("Nenhum recrutador cadastrado no momento.")
        } else {
            recrutadores.each { Recrutador r ->
                genericoView.mostrarMensagem("ID: #${r.id} | Nome: ${r.nome} | Email: ${r.email} | Área: ${r.area ?: 'N/A'}")
            }
        }
    }

}
