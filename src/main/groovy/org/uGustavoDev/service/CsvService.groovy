package org.uGustavoDev.service

import org.uGustavoDev.model.Candidato
import org.uGustavoDev.model.Empresa
import org.uGustavoDev.model.Pessoa

class CsvService {
    private static final String ARQUIVO_CSV = "pessoas.csv"
    private static final String DELIMITADOR = ";"

    static void salvarPessoas(List<Pessoa> pessoas) {
        try {
            def file = new File(ARQUIVO_CSV)
            file.withPrintWriter("UTF-8") { writer ->
                // Cabeçalho
                writer.println("Tipo;Nome;Email;Pais;Estado;CEP;Descricao;Documento;Idade;Competencias")
                
                pessoas.each { pessoa ->
                    String tipo = pessoa instanceof Candidato ? "CANDIDATO" : "EMPRESA"
                    String nome = pessoa.nome?.replace(";", ",") ?: ""
                    String email = pessoa.email?.replace(";", ",") ?: ""
                    String pais = pessoa.pais?.replace(";", ",") ?: ""
                    String estado = pessoa.estado?.replace(";", ",") ?: ""
                    String cep = pessoa.CEP?.replace(";", ",") ?: ""
                    String descricao = pessoa.descricao?.replace(";", ",") ?: ""
                    String documento = pessoa.obterDocumento()?.replace(";", ",") ?: ""
                    String idade = pessoa instanceof Candidato ? pessoa.idade.toString() : ""
                    String competencias = pessoa.competencias ? pessoa.competencias.join(",").replace(";", ",") : ""
                    
                    writer.printf("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s%n",
                            tipo, nome, email, pais, estado, cep, descricao, documento, idade, competencias)
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo CSV: " + e.getMessage())
        }
    }

    static List<Pessoa> carregarPessoas() {
        List<Pessoa> pessoas = []
        File arquivo = new File(ARQUIVO_CSV)
        
        if (!arquivo.exists()) {
            return pessoas
        }

        try {
            def linhas = arquivo.readLines("UTF-8")
            if (linhas.size() > 1) {
                // Pular o cabeçalho
                linhas[1..-1].each { linha ->
                    if (!linha.trim().isEmpty()) {
                        String[] dados = linha.split(DELIMITADOR, -1)
                        if (dados.length >= 10) {
                            String tipo = dados[0]
                            String nome = dados[1]
                            String email = dados[2]
                            String pais = dados[3]
                            String estado = dados[4]
                            String cep = dados[5]
                            String descricao = dados[6]
                            String documento = dados[7]
                            String idadeStr = dados[8]
                            String competenciasStr = dados[9]
                            
                            List<String> competencias = competenciasStr ? competenciasStr.split(",").toList() : []
                            
                            if (tipo == "CANDIDATO") {
                                Integer idade = idadeStr ? idadeStr.toInteger() : 0
                                def candidato = new Candidato(nome, email, estado, pais, cep, descricao, documento, idade)
                                candidato.competencias = competencias
                                pessoas.add(candidato)
                            } else if (tipo == "EMPRESA") {
                                def empresa = new Empresa(nome, email, estado, pais, cep, descricao, documento)
                                empresa.competencias = competencias
                                pessoas.add(empresa)
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo CSV: " + e.getMessage())
        }
        
        return pessoas
    }
}
