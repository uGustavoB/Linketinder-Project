package org.uGustavoDev.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa

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
            } catch (NumberFormatException ignored) {
                println "Entrada inválida. Digite um número."
            }
        }
        limparTela()
        return escolha
    }

    static LocalDate lerData(String mensagem) {
        while (true) {
            print mensagem
            String entrada = scanner.nextLine()
            try {
                return LocalDate.parse(entrada, DATE_FORMATTER)
            } catch (Exception ignored) {
                println "Data inválida. Use o formato DD/MM/AAAA."
            }
        }
    }

    static int pedirOpcaoPrincipal() {
        imprimirCabecalho("Linketinder - Menu Principal")
        println "Escolha uma ação:"
        println "1 - Listar Candidatos"
        println "2 - Listar Empresas"
        println "3 - Cadastrar Candidato"
        println "4 - Cadastrar Empresa"
        println "0 - Sair"

        return lerEscolha("Sua escolha: ", 0, 4)
    }

    static Candidato pedirDadosCandidato() {
        imprimirCabecalho("Cadastro de Candidato")
        String nome = lerTexto("Nome: ")
        String sobrenome = lerTexto("Sobrenome: ")
        String email = lerTexto("Email: ")
        String estado = lerTexto("Estado (ex: SP): ")
        String pais = lerTexto("País: ")
        String cep = lerTexto("CEP: ")
        String descricao = lerTexto("Descrição Pessoal: ")
        String cpf = lerTexto("CPF: ")

        LocalDate dataNasc = lerData("Data de Nascimento (DD/MM/AAAA): ")

        String compsStr = lerTexto("Competências (separadas por vírgula): ", true)
        List<String> competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        def candidato = new Candidato(nome, sobrenome, email, estado, pais, cep, descricao, cpf, dataNasc)
        candidato.competencias = competencias
        return candidato
    }

    static Empresa pedirDadosEmpresa() {
        imprimirCabecalho("Cadastro de Empresa")
        String nome = lerTexto("Nome da Empresa: ")
        String email = lerTexto("Email Corporativo: ")
        String pais = lerTexto("País: ")
        String cep = lerTexto("CEP: ")
        String descricao = lerTexto("Descrição da Empresa: ")
        String cnpj = lerTexto("CNPJ: ")

        String compsStr = lerTexto("Competências requeridas (separadas por vírgula): ", true)
        List<String> competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        def empresa = new Empresa(nome, email, pais, cep, descricao, cnpj)
        empresa.competencias = competencias
        return empresa
    }
}
