<h1 align="center">LinkeTinder</h1>

## Descrição

Bem-vindo ao projeto **LinkeTinder**, um sistema inovador idealizado a partir do insight do grande empresário Dr. Antônio Paçoca. O objetivo desta plataforma é revolucionar o processo de recrutamento, unindo a praticidade de "match" do Tinder com o mapeamento e validação de competências técnicas do LinkedIn.

Trata-se de um MVP (Minimum Viable Product) via console escrito em **Groovy**, onde é possível cadastrar e gerenciar perfis de Candidatos e de Empresas, visualizar vagas de forma anônima e conectar talentos às oportunidades ideais. O projeto evoluiu para uma arquitetura limpa em **MVC (Model-View-Controller)** e integra-se a um banco de dados relacional (PostgreSQL) utilizando o padrão **DAO**.

> **Frontend Web**: Para a versão web interativa em Single Page Application (desenvolvida com TypeScript, Vite e Chart.js), consulte a [Documentação do Frontend](frontend/README.md).

> **Modelagem de Dados**: O banco de dados foi construído com o auxílio da ferramenta **dbdiagram.io**, contando com tabelas de Candidatos, Empresas, Vagas e Competências. O diagrama DER (.pdf) e scripts de inserção podem ser conferidos na [Documentação de Modelagem](specs/README.md).

---

## Funcionalidades

- **Cadastro e Autenticação**: Sistema de Login e Cadastro separado para Candidatos e Empresas.
- **Exploração Anônima**: 
  - Candidatos podem visualizar as vagas sem saber imediatamente qual empresa a publicou.
  - Empresas podem buscar talentos de forma anônima, garantindo imparcialidade e focando puramente nas competências.
- **Gerenciamento de Vagas**: Empresas podem cadastrar, listar, atualizar e deletar suas vagas abertas, vinculando as competências exigidas.
- **Arquitetura MVC**: Clara separação de responsabilidades entre Interface de Usuário (`ConsoleUI`), Lógica de Fluxo (`Controllers`), Regras de Negócio (`Services`) e Acesso a Dados (`DAOs`).

---

## Estrutura do Projeto

```
src/
├── main/
│   └── groovy/
│       └── org/
│           └── uGustavoDev/
│               ├── Main.groovy              # Ponto de entrada (Roteador principal)
│               ├── controller/              # Camada de controle de fluxo (MVC)
│               │   ├── CandidatoController.groovy
│               │   └── EmpresaController.groovy
│               ├── service/                 # Camada de regras de negócio (Validações)
│               │   ├── CandidatoService.groovy
│               │   ├── EmpresaService.groovy
│               │   └── VagaService.groovy
│               ├── dao/                     # Data Access Objects (Comunicação com PostgreSQL)
│               ├── model/                   # Classes de Entidade
│               │   ├── Pessoa.groovy        # Classe base (abstrata)
│               │   ├── Candidato.groovy     
│               │   ├── Empresa.groovy       
│               │   └── Vaga.groovy
│               └── ui/
│                   └── ConsoleUI.groovy     # Único local que interage com o terminal (Inputs/Prints)
└── test/
    └── groovy/
        └── org/
            └── uGustavoDev/
                ├── model/                   # Testes unitários puros das entidades
                │   ├── CandidatoSpec.groovy 
                │   └── EmpresaSpec.groovy   
                └── services/                # Testes automatizados usando Spock e GroovyMock(DAO)
                    ├── CandidatoServiceSpec.groovy 
                    ├── EmpresaServiceSpec.groovy
                    └── VagaServiceSpec.groovy
```

---

## Estrutura dos Dados

| Entidade        | Propriedades Principais                                                                                         |
|-----------------|-----------------------------------------------------------------------------------------------------------------|
| **Pessoa**      | Classe abstrata com atributos base: Nome, Email, País, CEP, Descrição, Senha e Lista de Competências.           |
| **Candidato**   | Herda de Pessoa. Adiciona `sobrenome`, `cpf`, `estado` e `dataNascimento` (LocalDate).                          |
| **Empresa**     | Herda de Pessoa. Adiciona `cnpj`.                                                                               |
| **Vaga**        | Pertence a uma Empresa. Contém `nome`, `descricao`, `cidade`, `estado` e Lista de Competências.                 |

---

## Tecnologias

- **Groovy** (Linguagem escolhida para produtividade e compatibilidade com o ecossistema Java)
- **Gradle** (Gerenciador de dependências e automação de build)
- **PostgreSQL / JDBC** (Banco de dados relacional oficial do projeto)
- **Spock Framework** (Framework de testes unitários e BDD, utilizando recursos avançados como `GroovyMock` para simular o banco de dados)

---

## Como Executar

1. Clone o repositório:
    ```bash
    git clone https://github.com/uGustavoB/Linketinder-Project.git
    ```

2. Navegue até o diretório do projeto:
    ```bash
    cd Linketinder-Project
    ```

3. Execute a Suite de Testes Automatizados (Valida Regras de Negócio sem tocar no Banco de Dados):
    - **No Windows:**
      ```bash
      gradlew.bat test
      ```
    - **No Linux/Mac:**
      ```bash
      ./gradlew test
      ```

4. Execute a Aplicação usando o Gradle Wrapper:
    - **No Windows:**
      ```bash
      ./gradlew.bat -q --console plain run
      ```
    - **No Linux/Mac:**
      ```bash
      ./gradlew -q --console plain run
      ```

---

## Autores

Este projeto foi desenvolvido por:

- **Gustavo Gabriel** - [GitHub](https://github.com/uGustavoB)
