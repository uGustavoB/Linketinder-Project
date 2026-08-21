package org.uGustavoDev

import org.uGustavoDev.service.LinketinderService
import org.uGustavoDev.ui.ConsoleUI

static void main(String[] args) {
  LinketinderService service = new LinketinderService()
  boolean executando = true

  ConsoleUI.limparTela()

  while (executando) {
    int opcao = ConsoleUI.pedirOpcaoPrincipal()

    switch (opcao) {
      case 1:
        service.listarCandidatos()
        ConsoleUI.aguardarContinuacao()
        break
      case 2:
        service.listarEmpresas()
        ConsoleUI.aguardarContinuacao()
        break
      case 3:
        def novoCandidato = ConsoleUI.pedirDadosCandidato()
        service.adicionarCandidato(novoCandidato)
        ConsoleUI.imprimirMensagem("\nCandidato cadastrado com sucesso!")
        ConsoleUI.aguardarContinuacao()
        break
      case 4:
        def novaEmpresa = ConsoleUI.pedirDadosEmpresa()
        service.adicionarEmpresa(novaEmpresa)
        ConsoleUI.imprimirMensagem("\nEmpresa cadastrada com sucesso!")
        ConsoleUI.aguardarContinuacao()
        break
      case 0:
        ConsoleUI.imprimirMensagem("Encerrando o Linketinder. Até logo!")
        executando = false
        break
    }
  }
}