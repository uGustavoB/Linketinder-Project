package org.uGustavoDev.view

import org.uGustavoDev.builder.RecrutadorBuilder
import org.uGustavoDev.model.Recrutador
import spock.lang.Specification

class EmpresaViewSpec extends Specification {

    GenericView mockGenericView
    EmpresaView empresaView

    void setup() {
        mockGenericView = Mock(GenericView)
        empresaView = new EmpresaView(mockGenericView)
    }

    void "deve exibir submenu de gestao de recrutadores na empresa"() {
        when: "o menu de gestao de recrutadores e exibido"
        int opcao = empresaView.mostrarMenuGestaoRecrutadores()

        then: "as opcoes de cadastrar, listar, deletar e voltar sao exibidas"
        1 * mockGenericView.mostrarCabecalho("Gestão de Recrutadores")
        1 * mockGenericView.mostrarMensagem("1 - Cadastrar Recrutador")
        1 * mockGenericView.mostrarMensagem("2 - Listar Recrutadores")
        1 * mockGenericView.mostrarMensagem("3 - Deletar Recrutador")
        1 * mockGenericView.mostrarMensagem("0 - Voltar")
        1 * mockGenericView.lerEscolha("Sua escolha: ", 0, 3) >> 1
        opcao == 1
    }

    void "deve exibir recrutadores da empresa ou mensagem informativa quando nao houver cadastros"() {
        given: "um recrutador vinculado"
        List<Recrutador> recrutadores = [
                new RecrutadorBuilder().id(2).nome("Paula").email("paula@empresa.com").area("RH").build()
        ]

        when: "a empresa lista seus recrutadores"
        empresaView.mostrarRecrutadores(recrutadores)

        then: "o formato de exibicao e exibido"
        1 * mockGenericView.mostrarCabecalho("Lista de Recrutadores da Empresa")
        1 * mockGenericView.mostrarMensagem("ID: #2 | Nome: Paula | Email: paula@empresa.com | Área: RH")

        when: "a empresa nao possui recrutadores"
        empresaView.mostrarRecrutadores([])

        then: "mensagem de lista vazia e exibida"
        1 * mockGenericView.mostrarCabecalho("Lista de Recrutadores da Empresa")
        1 * mockGenericView.mostrarMensagem("Nenhum recrutador cadastrado para esta empresa.")
    }

}
