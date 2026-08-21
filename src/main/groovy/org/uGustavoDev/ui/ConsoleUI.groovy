package org.uGustavoDev.ui

import java.time.format.DateTimeFormatter

class ConsoleUI {
  private static final Scanner scanner = new Scanner(System.in)
  private static final String DATE_PATTERN = "dd/MM/yyyy"
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN)

  static void imprimirCabecalho(String titulo) {
    println "\n========================================"
    println "  ${titulo.toUpperCase()}"
    println "========================================"
  }

  static void limparTela() {
    print "\033[H\033[2J"
    System.out.flush()
  }

  static void aguardarContinuacao() {
    println "\nPressione Enter para continuar..."
    scanner.nextLine()
    limparTela()
  }

  static void imprimirMensagem(String msg) {
    println msg
  }

  static String lerTexto(String mensagem, boolean permiteVazio = false) {
    while (true) {
      print mensagem
      String entrada = scanner.nextLine()

      if (!permiteVazio && entrada.trim().isEmpty()) {
        println "Este campo não pode ser vazio. Tente novamente."
        continue
      }
      return entrada
    }
  }

  static Integer lerEscolha(String mensagem, int min, int max, boolean permiteVazio = false) {
    Integer escolha = null
    while (true) {
      print "\n$mensagem"
      String entrada = scanner.nextLine()

      if (permiteVazio && entrada.trim().isEmpty()) {
        limparTela()
        return null
      }

      try {
        escolha = Integer.parseInt(entrada)
        if (escolha >= min && escolha <= max) {
          break
        } else {
          println "Opção inválida. Escolha entre $min e $max."
        }
      } catch (NumberFormatException e) {
        println "Entrada inválida. Digite um número."
      }
    }
    limparTela()
    return escolha
  }

  static int pedirOpcaoPrincipal() {
    imprimirCabecalho("Linketinder - Menu Principal")
    println "Escolha uma ação:"
    println "1 - Listar Candidatos"
    println "2 - Listar Empresas"
    println "0 - Sair"

    return lerEscolha("Sua escolha: ", 0, 2)
  }
}
