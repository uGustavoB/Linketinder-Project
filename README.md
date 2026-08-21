<h1 align="center">LinkeTinder</h1>

## Descrição

Bem-vindo ao projeto **LinkeTinder**, um sistema inovador idealizado a partir do insight do grande empresário Dr. Antônio Paçoca. O objetivo desta plataforma é revolucionar o processo de recrutamento, unindo a praticidade de "match" do Tinder com o mapeamento e validação de competências técnicas do LinkedIn.

Trata-se de um MVP (Minimum Viable Product) via console escrito em **Groovy**, onde é possível cadastrar e gerenciar perfis de Candidatos e de Empresas, visualizar suas qualificações e armazenar esses dados de forma persistente num único repositório unificado.

---

## Funcionalidades

- **Cadastro de Candidatos e Empresas**: Adicione novos perfis com informações detalhadas, capturando inclusive a lista das competências técnicas de cada um.
- **Listagem Estruturada**: Visualize os candidatos e as empresas de forma bastante amigável no terminal, através de formato de tabela elegante (sem emojis ou caracteres que quebrem terminais nativos).
- **Persistência em CSV**: Os dados são salvos de forma automática e imediata no arquivo `pessoas.csv` após qualquer novo cadastro, e carregados na inicialização da aplicação, evitando a perda de dados.

---

## Estrutura do Projeto

```
src/
└── main/
    └── groovy/
        └── org/
            └── uGustavoDev/
                ├── Main.groovy              # Ponto de entrada da aplicação
                ├── model/
                │   ├── Pessoa.groovy        # Classe base (abstrata)
                │   ├── Candidato.groovy     # Entidade de Candidato
                │   ├── Empresa.groovy       # Entidade de Empresa
                │   └── interfaces/
                │       └── IPessoa.groovy   # Interface de contrato (ex: obterDocumento)
                ├── service/
                │   ├── CsvService.groovy         # Responsável pela persistência em CSV
                │   └── LinketinderService.groovy # Lógica de negócios e gerenciamento das listas
                └── ui/
                    └── ConsoleUI.groovy     # Gerenciamento de interface e leitura de inputs
```

---

## Descrição dos Arquivos Principais

| Arquivo                     | Descrição                                                                                                                                         |
|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| `Main.groovy`               | Classe principal que orquestra o loop de repetição da aplicação, inicia as dependências, invoca o menu e chama as ações corretas.                 |
| `Pessoa.groovy`             | Classe mãe abstrata contendo as propriedades em comum entre perfis: Nome, E-mail, País, Estado, CEP, Descrição e as Competências.                 |
| `Candidato.groovy`          | Entidade que herda de `Pessoa` adicionando particularidades exclusivas como o `cpf` e `idade`.                                                    |
| `Empresa.groovy`            | Entidade que herda de `Pessoa` e adiciona particularidades exclusivas corporativas, como o `cnpj`.                                                |
| `LinketinderService.groovy` | Serviço encarregado pelas regras de negócio, pela inicialização de mock de dados (ex: Big Techs), pelo carregamento inicial e listas na RAM.      |
| `CsvService.groovy`         | Classe com lógica puramente voltada para I/O: ler o `pessoas.csv` unificando entidades via coluna `Tipo`, e persistir novos cadastros via stream. |
| `ConsoleUI.groovy`          | Isola toda a complexidade da CLI: limpeza de terminal, formatação ASCII de cabeçalhos, validação de inputs (evitar nulls) e impressão de dados.   |

---

## Estrutura dos Dados Comuns (Pessoa)

| Propriedade       | Tipo           | Descrição                                                      |
|-------------------|----------------|----------------------------------------------------------------|
| **Nome**          | `String`       | Nome do profissional candidato ou nome corporativo da empresa  |
| **Email**         | `String`       | Email para contato ou recrutamento                             |
| **País / Estado** | `String`       | Localização ou sede administrativa                             |
| **CEP**           | `String`       | Código postal correspondente ao endereço                       |
| **Descrição**     | `String`       | Breve resumo pessoal/profissional ou sobre a missão da empresa |
| **Competências**  | `List<String>` | Habilidades que o candidato possui ou que a empresa exige      |

---

## Tecnologias

- **Groovy** (Linguagem escolhida para produtividade e compatibilidade com o ecossistema Java)
- **Gradle** (Gerenciador de dependências e automação de build)

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

3. Execute o projeto usando o Gradle Wrapper:
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
