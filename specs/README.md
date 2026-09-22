<h1 align="center">LinkeTinder - Modelagem de Dados</h1>

## Descrição

Este diretório contém a modelagem e os scripts SQL referentes ao banco de dados do projeto **LinkeTinder**, visando a futura migração do sistema atual (baseado em armazenamento em memória) para um modelo persistente e relacional.

A estrutura do banco de dados foi desenhada para suportar as principais entidades e os relacionamentos necessários para o processo de "match" entre Candidatos e Vagas (Empresas), replicando a ideia de unir a validação técnica de uma rede profissional com a dinâmica interativa do Tinder.

---

## Funcionalidades do Banco de Dados

- **Persistência de Candidatos e Empresas**: Armazenamento seguro e estruturado dos perfis, garantindo integridade de dados com unicidade (como CPF, CNPJ e e-mail únicos).
- **Relacionamento de Competências**: Sistema de habilidades vinculado em relações N:M, tanto para Candidatos (competências que dominam) quanto para Vagas (competências exigidas).
- **Gerenciamento de Vagas**: Estrutura dedicada para empresas publicarem suas oportunidades, especificando detalhes e localidade.
- **Sistema de Match (Curtidas)**: Tabela especializada para armazenar os eventos de "curtidas" cruzadas. Registra o momento exato (timestamp) em que um candidato curte uma vaga e quando a empresa curte o candidato, possibilitando validar o "match".

---

## Estrutura do Diretório

```
specs/
└── db/
    ├── linketinder.dbml           # Estrutura do banco de dados na linguagem DBML (Database Markup Language)
    ├── linketinder_db.sql         # Script SQL para criação das tabelas e restrições (DDL)
    ├── linketinder_populate.sql   # Script SQL com massa de dados fictícios para testes (DML)
    └── Linketinder.pdf            # Diagrama Entidade-Relacionamento (DER) gerado
```

---

## Descrição das Entidades Principais

| Entidade                | Descrição                                                                                                                                         |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| `candidatos`            | Armazena os dados pessoais e credenciais de acesso dos profissionais (nome, CPF, data de nascimento, e-mail, senha, etc.).                        |
| `empresas`              | Armazena os dados corporativos e credenciais de acesso das companhias (nome, CNPJ, e-mail, senha, etc.).                                          |
| `vagas`                 | Oportunidades abertas pelas empresas, contendo detalhes como descrição, estado e cidade. Possui chave estrangeira vinculada à empresa criadora.   |
| `competencias`          | Tabela de domínio para armazenar o catálogo de habilidades técnicas padronizadas (ex: Java, Python, Groovy, React).                               |
| `candidato_competencia` | Tabela associativa que vincula os candidatos às múltiplas competências que eles possuem (relação N:M).                                            |
| `vaga_competencia`      | Tabela associativa que vincula as vagas às múltiplas competências exigidas para o preenchimento (relação N:M).                                    |
| `curtidas`              | Tabela para registro do sistema de Match. Relaciona um Candidato a uma Vaga, com registro de data/hora da curtida de ambas as partes.             |

---

## Tecnologias e Ferramentas

- **dbdiagram.io**: Ferramenta online utilizada para realizar a modelagem visual do banco de dados e exportar os scripts e diagramas.
- **DBML (Database Markup Language)**: Linguagem utilizada nativamente pelo dbdiagram.io para desenhar e documentar de maneira ágil a estrutura relacional do banco.
- **SQL**: Scripts baseados na linguagem padrão de banco de dados relacional, preparados para serem executados em SGBDs como PostgreSQL ou MySQL.
- **Diagrama ER**: Representação visual do banco de dados para facilitar o entendimento da arquitetura pelos desenvolvedores.

---

## Voltar ao Início

[⬅ Voltar para a documentação principal](../README.md)
