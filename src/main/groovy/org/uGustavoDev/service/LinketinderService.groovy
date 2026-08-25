package org.uGustavoDev.service

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.ui.ConsoleUI

class LinketinderService {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []

    LinketinderService() {
        inicializarDados()
    }

    void inicializarDados() {
        inicializarCandidatos()
        inicializarEmpresas()
    }

    void inicializarCandidatos() {
        def candidato1 = new Candidato("Gustavo", "gustavo@email.com", "PB", "Brasil", "58000-000", "Desenvolvedor buscando aplicar skills em projetos reais.", "111.111.111-11", 25)
        candidato1.competencias = ["Java", "Spring Framework", "Python"]

        def candidato2 = new Candidato("Ana Silva", "ana@email.com", "SP", "Brasil", "01000-000", "Especialista em frontend e design de interfaces.", "222.222.222-22", 30)
        candidato2.competencias = ["Angular", "TypeScript"]

        def candidato3 = new Candidato("Carlos Eduardo", "carlos@email.com", "RJ", "Brasil", "20000-000", "Engenheiro de dados com foco em pipelines.", "333.333.333-33", 28)
        candidato3.competencias = ["Python", "SQL"]

        def candidato4 = new Candidato("Mariana Souza", "mariana@email.com", "MG", "Brasil", "30000-000", "Desenvolvedora focada em ecossistemas mobile.", "444.444.444-44", 23)
        candidato4.competencias = ["Flutter", "Dart"]

        def candidato5 = new Candidato("João Pedro", "joao@email.com", "SC", "Brasil", "88000-000", "Arquiteto de software e entusiasta open-source.", "555.555.555-55", 35)
        candidato5.competencias = ["Java", "Docker", "AWS"]

        candidatos.addAll([candidato1, candidato2, candidato3, candidato4, candidato5])
    }

    void inicializarEmpresas() {
        def empresa1 = new Empresa("Google", "careers@google.com", "SP", "Brasil", "04538-133", "Empresa multinacional de servicos online e software.", "06.990.590/0001-23")
        empresa1.competencias = ["Python", "Java", "Go", "Cloud"]

        def empresa2 = new Empresa("Meta", "jobs@meta.com", "SP", "Brasil", "04538-133", "Conglomerado de tecnologia e midias sociais.", "13.654.497/0001-32")
        empresa2.competencias = ["React", "JavaScript", "Python", "C++"]

        def empresa3 = new Empresa("Nubank", "vagas@nubank.com.br", "SP", "Brasil", "05425-070", "Pioneira no segmento de servicos financeiros atuando como banco digital.", "18.236.120/0001-58")
        empresa3.competencias = ["Clojure", "Flutter", "Datomic"]

        def empresa4 = new Empresa("Itau Unibanco", "carreiras@itau.com.br", "SP", "Brasil", "04344-902", "Maior banco privado do Brasil e da America Latina.", "60.872.504/0001-23")
        empresa4.competencias = ["Java", "Spring Boot", "AWS", "Angular"]

        def empresa5 = new Empresa("Mercado Livre", "talentos@mercadolivre.com", "SP", "Brasil", "06233-903", "Empresa de tecnologia que oferece solucoes de comercio eletronico.", "03.007.331/0001-41")
        empresa5.competencias = ["Java", "Golang", "Python", "React"]

        empresas.addAll([empresa1, empresa2, empresa3, empresa4, empresa5])
    }

    void adicionarCandidato(Candidato candidato) {
        candidatos.add(candidato)
    }

    void adicionarEmpresa(Empresa empresa) {
        empresas.add(empresa)
    }

    void listarCandidatos() {
        if (candidatos.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhum candidato cadastrado.")
            return
        }

        ConsoleUI.imprimirCabecalho("Lista de Candidatos")
        candidatos.each { ConsoleUI.imprimirMensagem(it.toString()) }
    }

    void listarEmpresas() {
        if (empresas.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhuma empresa cadastrada.")
            return
        }

        ConsoleUI.imprimirCabecalho("Lista de Empresas")
        empresas.each { ConsoleUI.imprimirMensagem(it.toString()) }
    }
}
