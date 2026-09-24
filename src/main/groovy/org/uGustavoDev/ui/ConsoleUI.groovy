package org.uGustavoDev.ui

import org.uGustavoDev.model.Vaga

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

    static int pedirOpcaoDeslogado() {
        imprimirCabecalho("Linketinder - Bem-vindo!")
        println "1 - Login (Candidato / Empresa)"
        println "2 - Cadastrar Candidato"
        println "3 - Cadastrar Empresa"
        println "0 - Sair"

        return lerEscolha("Sua escolha: ", 0, 3)
    }

    static int pedirOpcaoCandidatoLogado(Candidato candidato) {
        imprimirCabecalho("Linketinder - Área do Candidato")
        println "Olá, ${candidato.nome}!"
        println "1 - Meu Perfil"
        println "2 - Vagas (Em Breve)"
        println "0 - Sair (Logout)"

        return lerEscolha("Sua escolha: ", 0, 2)
    }

    static int pedirOpcaoEmpresaLogada(Empresa empresa) {
        imprimirCabecalho("Linketinder - Área da Empresa")
        println "Olá, ${empresa.nome}!"
        println "1 - Meu Perfil"
        println "2 - Vagas"
        println "0 - Sair (Logout)"

        return lerEscolha("Sua escolha: ", 0, 2)
    }

    static int pedirOpcaoMenuPerfil() {
        imprimirCabecalho("Menu de Perfil")
        println "1 - Ver meu perfil"
        println "2 - Editar meu perfil"
        println "3 - Deletar minha conta"
        println "0 - Voltar"

        return lerEscolha("Sua escolha: ", 0, 3)
    }

    static int pedirOpcaoMenuVagasEmpresa() {
        imprimirCabecalho("Menu de Vagas")
        println "1 - Criar nova vaga"
        println "2 - Listar minhas vagas"
        println "3 - Editar uma vaga"
        println "4 - Deletar uma vaga"
        println "0 - Voltar"

        return lerEscolha("Sua escolha: ", 0, 4)
    }

    static Map<String, String> pedirCredenciais() {
        imprimirCabecalho("Login")
        String email = lerTexto("Email: ")
        String senha = lerTexto("Senha: ")
        return [email: email, senha: senha]
    }

    static Candidato pedirDadosCandidato() {
        imprimirCabecalho("Cadastro de Candidato")
        String nome = lerTexto("Nome: ")
        String sobrenome = lerTexto("Sobrenome: ")
        String email = lerTexto("Email: ")
        String senha = lerTexto("Senha (mínimo 6 caracteres): ")
        String estado = lerTexto("Estado (ex: SP): ")
        String pais = lerTexto("País: ")
        String cep = lerTexto("CEP: ")
        String descricao = lerTexto("Descrição Pessoal: ")
        String cpf = lerTexto("CPF: ")

        LocalDate dataNasc = lerData("Data de Nascimento (DD/MM/AAAA): ")

        String compsStr = lerTexto("Competências (separadas por vírgula): ", true)
        List<String> competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        def candidato = new Candidato(nome, sobrenome, email, estado, pais, cep, descricao, cpf, dataNasc)
        candidato.senha = senha
        candidato.competencias = competencias
        return candidato
    }

    static Empresa pedirDadosEmpresa() {
        imprimirCabecalho("Cadastro de Empresa")
        String nome = lerTexto("Nome da Empresa: ")
        String email = lerTexto("Email Corporativo: ")
        String senha = lerTexto("Senha (mínimo 6 caracteres): ")
        String pais = lerTexto("País: ")
        String cep = lerTexto("CEP: ")
        String descricao = lerTexto("Descrição da Empresa: ")
        String cnpj = lerTexto("CNPJ: ")

        String compsStr = lerTexto("Competências requeridas (separadas por vírgula): ", true)
        List<String> competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        def empresa = new Empresa(nome, email, pais, cep, descricao, cnpj)
        empresa.senha = senha
        empresa.competencias = competencias
        return empresa
    }

    static Vaga pedirDadosVaga(int empresaId) {
        imprimirCabecalho("Criar Nova Vaga")
        String nome = lerTexto("Título da Vaga: ")
        String descricao = lerTexto("Descrição da Vaga: ")
        String cidade = lerTexto("Cidade: ")
        String estado = lerTexto("Estado (ex: SP): ")
        
        String compsStr = lerTexto("Competências exigidas (separadas por vírgula): ", true)
        List<String> competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        def vaga = new Vaga(empresaId, nome, descricao, estado, cidade)
        vaga.competencias = competencias
        return vaga
    }
}
