package org.uGustavoDev.view

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class GenericView {

    private final Scanner scanner = new Scanner(System.in)
    private static final String DATE_PATTERN = "dd/MM/yyyy"
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN)

    void mostrarCabecalho(String titulo) {
        println "\n========================================"
        println "  ${titulo.toUpperCase()}"
        println "========================================"
    }

    void limparTela() {
        print "\033[H\033[2J"
        System.out.flush()
    }

    void aguardarContinuacao() {
        println "\nPressione Enter para continuar..."
        scanner.nextLine()
        limparTela()
    }

    void mostrarMensagem(String msg) {
        println msg
    }

    String lerEntradaUsuario(String mensagem, boolean permiteVazio = false) {
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

    Integer lerEscolha(String mensagem, int min, int max, boolean permiteVazio = false) {
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
            } catch (NumberFormatException ignored) {
                println "Entrada inválida. Digite um número."
            }
        }
        limparTela()
        return escolha
    }

    LocalDate lerData(String mensagem) {
        while (true) {
            print mensagem
            String entrada = scanner.nextLine()
            try {
                return LocalDate.parse(entrada, DATE_FORMATTER)
            } catch (DateTimeParseException ignored) {
                println "Data inválida. Use o formato DD/MM/AAAA."
            }
        }
    }
}
