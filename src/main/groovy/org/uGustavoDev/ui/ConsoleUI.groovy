package org.uGustavoDev.ui

import org.uGustavoDev.dto.CandidatoCadastroDTO
import org.uGustavoDev.dto.EmpresaCadastroDTO
import org.uGustavoDev.dto.VagaCadastroDTO
import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
            } catch (DateTimeParseException ignored) {
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
        println "2 - Explorar Vagas"
        println "0 - Sair (Logout)"

        return lerEscolha("Sua escolha: ", 0, 2)
    }

    static int pedirOpcaoEmpresaLogada(Empresa empresa) {
        imprimirCabecalho("Linketinder - Área da Empresa")
        println "Olá, ${empresa.nome}!"
        println "1 - Meu Perfil"
        println "2 - Minhas Vagas"
        println "3 - Explorar Candidatos"
        println "0 - Sair (Logout)"

        return lerEscolha("Sua escolha: ", 0, 3)
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
        return [email:email, senha:senha]
    }

    static CandidatoCadastroDTO pedirDadosCandidato() {
        imprimirCabecalho("Cadastro de Candidato")

        CandidatoCadastroDTO dto = new CandidatoCadastroDTO()
        dto.nome = lerTexto("Nome: ")
        dto.sobrenome = lerTexto("Sobrenome: ")
        dto.email = lerTexto("Email: ")
        dto.senha = lerTexto("Senha (mínimo 6 caracteres): ")
        dto.estado = lerTexto("Estado (ex: SP): ")
        dto.pais = lerTexto("País: ")
        dto.cep = lerTexto("CEP: ")
        dto.descricao = lerTexto("Descrição Pessoal: ")
        dto.cpf = lerTexto("CPF: ")
        dto.dataNascimento = lerData("Data de Nascimento (DD/MM/AAAA): ")

        String compsStr = lerTexto("Competências (separadas por vírgula): ", true)
        dto.competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        return dto
    }

    static EmpresaCadastroDTO pedirDadosEmpresa() {
        imprimirCabecalho("Cadastro de Empresa")

        EmpresaCadastroDTO dto = new EmpresaCadastroDTO()
        dto.nome = lerTexto("Nome da Empresa: ")
        dto.email = lerTexto("Email Corporativo: ")
        dto.senha = lerTexto("Senha (mínimo 6 caracteres): ")
        dto.pais = lerTexto("País: ")
        dto.cep = lerTexto("CEP: ")
        dto.descricao = lerTexto("Descrição da Empresa: ")
        dto.cnpj = lerTexto("CNPJ: ")

        String compsStr = lerTexto("Competências requeridas (separadas por vírgula): ", true)
        dto.competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        return dto
    }

    static VagaCadastroDTO pedirDadosVaga(int empresaId) {
        imprimirCabecalho("Criar Nova Vaga")

        VagaCadastroDTO dto = new VagaCadastroDTO()
        dto.nome = lerTexto("Título da Vaga: ")
        dto.descricao = lerTexto("Descrição da Vaga: ")
        dto.cidade = lerTexto("Cidade: ")
        dto.estado = lerTexto("Estado (ex: SP): ")

        String compsStr = lerTexto("Competências exigidas (separadas por vírgula): ", true)
        dto.competencias = compsStr ? compsStr.split(",").collect { it.trim() } : []

        return dto
    }

}
