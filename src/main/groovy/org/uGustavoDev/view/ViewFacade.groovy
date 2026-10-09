package org.uGustavoDev.view

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Vaga

class ViewFacade {

    CandidatoView candidatoView
    EmpresaView empresaView
    VagaView vagaView
    GenericView genericoView

    ViewFacade() {
        this.genericoView = new GenericView()
        this.candidatoView = new CandidatoView(this.genericoView)
        this.empresaView = new EmpresaView(this.genericoView)
        this.vagaView = new VagaView(this.genericoView)
    }

    void mostrarCabecalho(String titulo) {
        genericoView.mostrarCabecalho(titulo)
    }

    void mostrarMensagem(String msg) {
        genericoView.mostrarMensagem(msg)
    }

    void aguardarContinuacao() {
        genericoView.aguardarContinuacao()
    }

    void limparTela() {
        genericoView.limparTela()
    }

    String lerEntradaUsuario(String mensagem, boolean permiteVazio = false) {
        return genericoView.lerEntradaUsuario(mensagem, permiteVazio)
    }

    Integer lerEscolha(String mensagem, int min, int max, boolean permiteVazio = false) {
        return genericoView.lerEscolha(mensagem, min, max, permiteVazio)
    }

    CandidatoCadastroDTO obterDadosCandidato() {
        return candidatoView.obterDadosCandidato()
    }

    void mostrarPerfilCandidato(Candidato candidato) {
        candidatoView.mostrarPerfilCandidato(candidato)
    }

    void mostrarCandidatos(List<Candidato> candidatos) {
        candidatoView.mostrarCandidatos(candidatos)
    }

    EmpresaCadastroDTO obterDadosEmpresa() {
        return empresaView.obterDadosEmpresa()
    }

    void mostrarPerfilEmpresa(Empresa empresa) {
        empresaView.mostrarPerfilEmpresa(empresa)
    }

    VagaCadastroDTO obterDadosVaga() {
        return vagaView.obterDadosVaga()
    }

    void mostrarVagas(List<Vaga> vagas, String titulo = "Lista de Vagas") {
        vagaView.mostrarVagas(vagas, titulo)
    }

    Map<String, String> obterCredenciais() {
        genericoView.mostrarCabecalho("Login")
        String email = genericoView.lerEntradaUsuario("Email: ")
        String senha = genericoView.lerEntradaUsuario("Senha: ")
        return [email: email, senha: senha]
    }
    
    int mostrarMenuDeslogado() {
        genericoView.mostrarCabecalho("Linketinder - Bem-vindo!")
        genericoView.mostrarMensagem("1 - Login (Candidato / Empresa)")
        genericoView.mostrarMensagem("2 - Cadastrar Candidato")
        genericoView.mostrarMensagem("3 - Cadastrar Empresa")
        genericoView.mostrarMensagem("0 - Sair")
        return genericoView.lerEscolha("Sua escolha: ", 0, 3)
    }

    int mostrarMenuCandidato(Candidato candidato) {
        genericoView.mostrarCabecalho("Linketinder - Área do Candidato")
        genericoView.mostrarMensagem("Olá, ${candidato.nome}!")
        genericoView.mostrarMensagem("1 - Meu Perfil")
        genericoView.mostrarMensagem("2 - Explorar Vagas")
        genericoView.mostrarMensagem("0 - Sair (Logout)")
        return genericoView.lerEscolha("Sua escolha: ", 0, 2)
    }

    int mostrarMenuEmpresa(Empresa empresa) {
        genericoView.mostrarCabecalho("Linketinder - Área da Empresa")
        genericoView.mostrarMensagem("Olá, ${empresa.nome}!")
        genericoView.mostrarMensagem("1 - Meu Perfil")
        genericoView.mostrarMensagem("2 - Minhas Vagas")
        genericoView.mostrarMensagem("3 - Explorar Candidatos")
        genericoView.mostrarMensagem("0 - Sair (Logout)")
        return genericoView.lerEscolha("Sua escolha: ", 0, 3)
    }

    int mostrarMenuPerfil() {
        genericoView.mostrarCabecalho("Menu de Perfil")
        genericoView.mostrarMensagem("1 - Ver meu perfil")
        genericoView.mostrarMensagem("2 - Editar meu perfil")
        genericoView.mostrarMensagem("3 - Deletar minha conta")
        genericoView.mostrarMensagem("0 - Voltar")
        return genericoView.lerEscolha("Sua escolha: ", 0, 3)
    }

    int mostrarMenuVagas() {
        genericoView.mostrarCabecalho("Menu de Vagas")
        genericoView.mostrarMensagem("1 - Criar nova vaga")
        genericoView.mostrarMensagem("2 - Listar minhas vagas")
        genericoView.mostrarMensagem("3 - Editar uma vaga")
        genericoView.mostrarMensagem("4 - Deletar uma vaga")
        genericoView.mostrarMensagem("0 - Voltar")
        return genericoView.lerEscolha("Sua escolha: ", 0, 4)
    }

}
