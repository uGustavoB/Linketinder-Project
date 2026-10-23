package org.uGustavoDev.view

import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.dto.RecrutadorCadastroDTO
import org.uGustavoDev.model.Recrutador
import spock.lang.Specification

class ViewFacadeSpec extends Specification {

    GenericView mockGenericView
    CandidatoView mockCandidatoView
    EmpresaView mockEmpresaView
    VagaView mockVagaView
    RecrutadorView mockRecrutadorView
    ViewFacade viewFacade

    void setup() {
        mockGenericView = Mock(GenericView)
        mockCandidatoView = Mock(CandidatoView)
        mockEmpresaView = Mock(EmpresaView)
        mockVagaView = Mock(VagaView)
        mockRecrutadorView = Mock(RecrutadorView)

        viewFacade = new ViewFacade(
                mockGenericView,
                mockCandidatoView,
                mockEmpresaView,
                mockVagaView,
                mockRecrutadorView
        )
    }

    void "deve instanciar ViewFacade com construtor padrao sem erros"() {
        when: "o construtor padrao e invocado"
        ViewFacade facade = new ViewFacade()

        then: "todas as views internas sao inicializadas"
        facade.genericoView != null
        facade.candidatoView != null
        facade.empresaView != null
        facade.vagaView != null
        facade.recrutadorView != null
    }

    void "deve delegar operacoes de recrutador para RecrutadorView"() {
        given: "um DTO e um recrutador"
        RecrutadorCadastroDTO dto = new RecrutadorCadastroDTO(nome: "Mariana")
        Recrutador recrutador = new RecrutadorBuilder().id(1).nome("Mariana").build()

        when: "obterDadosRecrutador e acionado na facade"
        viewFacade.obterDadosRecrutador(10)

        then: "delega para a recrutadorView passando o id da empresa"
        1 * mockRecrutadorView.obterDadosRecrutador(10) >> dto

        when: "mostrarPerfilRecrutador e acionado"
        viewFacade.mostrarPerfilRecrutador(recrutador, null)

        then: "delega para a recrutadorView"
        1 * mockRecrutadorView.mostrarPerfilRecrutador(recrutador, null)

        when: "mostrarMenuPerfilRecrutador e acionado"
        viewFacade.mostrarMenuPerfilRecrutador()

        then: "delega para a recrutadorView"
        1 * mockRecrutadorView.mostrarMenuPerfil() >> 1

        when: "mostrarMenuRecrutador e acionado"
        viewFacade.mostrarMenuRecrutador(recrutador)

        then: "delega para a recrutadorView"
        1 * mockRecrutadorView.mostrarMenuRecrutador(recrutador) >> 2

        when: "mostrarRecrutadores e acionado"
        viewFacade.mostrarRecrutadores([recrutador])

        then: "delega para a recrutadorView"
        1 * mockRecrutadorView.mostrarRecrutadores([recrutador], "Lista de Recrutadores")
    }

    void "deve delegar gestao de recrutadores para EmpresaView"() {
        given: "uma lista de recrutadores"
        List<Recrutador> recrutadores = [new RecrutadorBuilder().id(1).build()]

        when: "mostrarMenuGestaoRecrutadores e acionado na facade"
        viewFacade.mostrarMenuGestaoRecrutadores()

        then: "delega para a empresaView"
        1 * mockEmpresaView.mostrarMenuGestaoRecrutadores() >> 1

        when: "mostrarRecrutadoresEmpresa e acionado"
        viewFacade.mostrarRecrutadoresEmpresa(recrutadores)

        then: "delega para a empresaView"
        1 * mockEmpresaView.mostrarRecrutadores(recrutadores)
    }

    void "deve exibir menu de selecao de tipo de login com 3 perfis"() {
        when: "o menu de escolha de perfil de login e acionado"
        int escolha = viewFacade.mostrarMenuTipoLogin()

        then: "as opcoes de Candidato, Empresa, Recrutador e Voltar sao apresentadas"
        1 * mockGenericView.mostrarCabecalho("Login - Selecione seu Perfil")
        1 * mockGenericView.mostrarMensagem("1 - Candidato")
        1 * mockGenericView.mostrarMensagem("2 - Empresa")
        1 * mockGenericView.mostrarMensagem("3 - Recrutador")
        1 * mockGenericView.mostrarMensagem("0 - Voltar")
        1 * mockGenericView.lerEscolha("Sua escolha: ", 0, 3) >> 3
        escolha == 3
    }

}
