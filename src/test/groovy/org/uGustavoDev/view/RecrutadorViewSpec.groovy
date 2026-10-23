package org.uGustavoDev.view

import org.uGustavoDev.builder.EmpresaBuilder
import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Recrutador
import spock.lang.Specification

import java.time.LocalDate

class RecrutadorViewSpec extends Specification {

    GenericView mockGenericView
    RecrutadorView recrutadorView

    void setup() {
        mockGenericView = Mock(GenericView)
        recrutadorView = new RecrutadorView(mockGenericView)
    }

    void "deve obter dados de recrutador sem solicitar empresaId herdando do contexto"() {
        given: "entradas simuladas do usuario no console"
        int empresaIdContexto = 42
        LocalDate dataNascimento = LocalDate.of(1992, 8, 20)

        when: "o formulario de cadastro de recrutador e acionado"
        RecrutadorCadastroDTO dto = recrutadorView.obterDadosRecrutador(empresaIdContexto)

        then: "o cabecalho e exibido e os campos sao lidos sequencialmente"
        1 * mockGenericView.mostrarCabecalho("Cadastro de Recrutador")
        1 * mockGenericView.lerEntradaUsuario("Nome: ") >> "Mariana Silva"
        1 * mockGenericView.lerEntradaUsuario("Email: ") >> "mariana@tech.com"
        1 * mockGenericView.lerEntradaUsuario("Senha (mínimo 6 caracteres): ") >> "senha123"
        1 * mockGenericView.lerEntradaUsuario("Área de Atuação (ex: RH Tech): ") >> "RH Tech"
        1 * mockGenericView.lerEntradaUsuario("Descrição Profissional: ") >> "Especialista em recrutamento tech"
        1 * mockGenericView.lerEntradaUsuario("CPF: ") >> "123.456.789-00"
        1 * mockGenericView.lerData("Data de Nascimento (DD/MM/AAAA): ") >> dataNascimento

        and: "o DTO retornado mantem o empresaId informado sem solicita-lo ao usuario"
        dto.empresaId == 42
        dto.nome == "Mariana Silva"
        dto.email == "mariana@tech.com"
        dto.senha == "senha123"
        dto.area == "RH Tech"
        dto.descricao == "Especialista em recrutamento tech"
        dto.cpf == "123.456.789-00"
        dto.dataNascimento == dataNascimento
    }

    void "deve exibir perfil do recrutador com dados da empresa associada quando informada"() {
        given: "um recrutador e sua empresa associada"
        Recrutador recrutador = new RecrutadorBuilder()
                .id(10)
                .empresaId(1)
                .nome("Carlos Eduardo")
                .email("carlos@empresa.com")
                .area("Tech Recruiter")
                .descricao("Recrutador sênior")
                .cpf("111.222.333-44")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .build()

        Empresa empresa = new EmpresaBuilder()
                .id(1)
                .nome("Tech Corp")
                .cnpj("12.345.678/0001-99")
                .build()

        when: "o perfil e exibido"
        recrutadorView.mostrarPerfilRecrutador(recrutador, empresa)

        then: "todas as mensagens formatadas sao exibidas incluindo dados da empresa"
        1 * mockGenericView.mostrarCabecalho("Meu Perfil (Recrutador)")
        1 * mockGenericView.mostrarMensagem("ID: #10")
        1 * mockGenericView.mostrarMensagem("Nome: Carlos Eduardo")
        1 * mockGenericView.mostrarMensagem("Email: carlos@empresa.com")
        1 * mockGenericView.mostrarMensagem("Área: Tech Recruiter")
        1 * mockGenericView.mostrarMensagem("Descrição: Recrutador sênior")
        1 * mockGenericView.mostrarMensagem("CPF: 111.222.333-44")
        1 * mockGenericView.mostrarMensagem("Data de Nascimento: 15/05/1990")
        1 * mockGenericView.mostrarMensagem("Empresa Associada: Tech Corp (CNPJ: 12.345.678/0001-99)")
    }

    void "deve exibir perfil do recrutador exibindo apenas ID da empresa caso a entidade empresa seja nula"() {
        given: "um recrutador sem objeto empresa carregado"
        Recrutador recrutador = new RecrutadorBuilder()
                .id(5)
                .empresaId(7)
                .nome("Ana")
                .email("ana@empresa.com")
                .cpf("999.888.777-66")
                .build()

        when: "o perfil e exibido sem empresa"
        recrutadorView.mostrarPerfilRecrutador(recrutador, null)

        then: "o ID da empresa e exibido"
        1 * mockGenericView.mostrarCabecalho("Meu Perfil (Recrutador)")
        1 * mockGenericView.mostrarMensagem("Empresa Associada: ID #7")
    }

    void "deve exibir menu de perfil restrito sem opcao de auto-delecao"() {
        when: "o menu de perfil e exibido"
        int escolha = recrutadorView.mostrarMenuPerfil()

        then: "apenas as opcoes 1, 2 e 0 sao apresentadas e lidas com limites 0 e 2"
        1 * mockGenericView.mostrarCabecalho("Menu de Perfil (Recrutador)")
        1 * mockGenericView.mostrarMensagem("1 - Ver meu perfil")
        1 * mockGenericView.mostrarMensagem("2 - Editar meu perfil")
        1 * mockGenericView.mostrarMensagem("0 - Voltar")
        1 * mockGenericView.lerEscolha("Sua escolha: ", 0, 2) >> 1
        escolha == 1
    }

    void "deve exibir menu principal do recrutador autenticado"() {
        given: "um recrutador autenticado"
        Recrutador recrutador = new RecrutadorBuilder().nome("Mariana").build()

        when: "o menu de recrutador e acionado"
        int escolha = recrutadorView.mostrarMenuRecrutador(recrutador)

        then: "as opcoes de perfil, gestao de vagas e candidatos sao exibidas"
        1 * mockGenericView.mostrarCabecalho("Linketinder - Área do Recrutador")
        1 * mockGenericView.mostrarMensagem("Olá, Mariana!")
        1 * mockGenericView.mostrarMensagem("1 - Meu Perfil")
        1 * mockGenericView.mostrarMensagem("2 - Gerenciar Vagas da Empresa")
        1 * mockGenericView.mostrarMensagem("3 - Explorar Candidatos")
        1 * mockGenericView.mostrarMensagem("0 - Sair (Logout)")
        1 * mockGenericView.lerEscolha("Sua escolha: ", 0, 3) >> 2
        escolha == 2
    }

    void "deve listar recrutadores formatados ou mensagem quando lista vazia"() {
        given: "uma lista com recrutador"
        List<Recrutador> recrutadores = [
                new RecrutadorBuilder().id(1).nome("Carlos").email("carlos@empresa.com").area("Tech").build()
        ]

        when: "a listagem com dados e acionada"
        recrutadorView.mostrarRecrutadores(recrutadores)

        then: "o cabecalho e a linha formatada sao exibidos"
        1 * mockGenericView.mostrarCabecalho("Lista de Recrutadores")
        1 * mockGenericView.mostrarMensagem("ID: #1 | Nome: Carlos | Email: carlos@empresa.com | Área: Tech")

        when: "a listagem vazia e acionada"
        recrutadorView.mostrarRecrutadores([])

        then: "mensagem de aviso e exibida"
        1 * mockGenericView.mostrarCabecalho("Lista de Recrutadores")
        1 * mockGenericView.mostrarMensagem("Nenhum recrutador cadastrado no momento.")
    }

}
