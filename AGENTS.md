# LinkeTinder — Documentação Técnica do Projeto (AGENTS.md)

Este documento centraliza as diretrizes arquiteturais, regras de domínio, convenções e instruções operacionais do repositório **Linketinder-Project**. Deve ser utilizado como guia de contexto estrito por agentes de inteligência artificial e desenvolvedores humanos.

---

## 1. Visão Geral

O **LinkeTinder** é uma plataforma concebida para revolucionar o recrutamento profissional, unindo a validação técnica de competências (estilo LinkedIn) à dinâmica de curtidas mútuas e correspondência (estilo Tinder).

### Princípio Fundamental
Garantir imparcialidade nos processos seletivos por meio do **Princípio do Anonimato**, permitindo que candidatos e empresas avaliem afinidades técnicas antes de qualquer exposição de dados pessoais ou corporativos.

### Funcionalidades Existentes no Repositório

1. **Backend (Groovy / JVM)**:
   - **Servidor HTTP Embutido**: Instanciado programaticamente via Apache Tomcat Embed Core na porta `8081`.
   - **Endpoints RESTful**:
     - `GET /candidatos`: Listagem de candidatos.
     - `POST /candidatos`: Cadastro de novo candidato com mapeamento de competências.
     - `GET /empresas`: Listagem de empresas.
     - `POST /empresas`: Cadastro de nova empresa com competências de interesse.
     - `GET /vagas`: Listagem de vagas abertas.
     - `POST /vagas`: Cadastro de oportunidade vinculada a uma empresa existente (`empresaId`).
   - **Persistência Relacional**: Camada DAO estruturada sobre PostgreSQL via JDBC.
   - **Console / Terminal Legado** (`Terminal.groovy`): Fluxo interativo de console contendo telas de login, cadastros, edição/deleção de conta e gerenciamento de vagas. *(Nota: desativado em `Main.groovy` em favor do `ServidorAPI`, mas funcional)*.

2. **Frontend (SPA TypeScript / Vite)**:
   - **Roteamento Hash**: Navegação entre telas (`#/login`, `#/cadastro`, `#/home-candidato`, `#/home-empresa`, `#/meu-perfil`, `#/vagas-candidato`, `#/perfil-empresa`).
   - **Persistência em LocalStorage**: Gestão de dados das coleções `candidatos`, `empresas`, `vagas` e sessão ativa `usuarioLogado`.
   - **Visão Anônima de Vagas**: Visualização pelo candidato omitindo dados da empresa.
   - **Visão Anônima de Candidatos**: Tabela exibida para empresas contendo apenas curso, instituição, competências e descrição.
   - **Dashboard com Gráfico de Competências**: Visualização interativa via Chart.js contabilizando a frequência de competências dos candidatos.

---

## 2. Stack Tecnológica e Estrutura de Pastas

### Tecnologias e Ferramentas

| Componente            | Ferramentas / Bibliotecas                   | Versões / Detalhes                                    |
|:----------------------|:--------------------------------------------|:------------------------------------------------------|
| **Backend**           | Apache Groovy, Gradle                       | Groovy `4.0.29`, Gradle Wrapper `9.6.0`               |
| **Servidor Web**      | Tomcat Embed Core, Jakarta Servlet API      | Tomcat `10.1.28`, Servlet API `6.0.0`                 |
| **Serialização JSON** | Jackson Databind, JSR310 Datatype           | Jackson `2.17.2`                                      |
| **Banco de Dados**    | PostgreSQL (oficial), MySQL (alternativo)   | Driver PostgreSQL `42.7.3`                            |
| **Configuração**      | Dotenv Java                                 | `io.github.cdimascio:dotenv-java:3.0.0`               |
| **Testes Backend**    | Spock Framework, Byte Buddy, JUnit Platform | Spock `2.4-M4-groovy-4.0`, Byte Buddy `1.14.9`        |
| **Análise Estática**  | CodeNarc                                    | CodeNarc `1.6.1` com Groovy-All `3.0.3`               |
| **Frontend**          | TypeScript, Vite, Chart.js, HTML5, CSS3     | TypeScript `~6.0.2`, Vite `^8.2.2`, Chart.js `^4.5.1` |
| **Modelagem DB**      | DBML, dbdiagram.io                          | `specs/db/linketinder.dbml`                           |

### Organização de Diretórios

```
Linketinder-Project/
├── build.gradle                        # Configuração do build Gradle e dependências
├── settings.gradle                     # Configurações do projeto Gradle
├── gradlew / gradlew.bat               # Gradle Wrapper (Linux/Mac e Windows)
├── config/codenarc/codenarc.xml        # Conjunto de regras de qualidade do CodeNarc
├── docs/API.md                         # Documentação dos endpoints HTTP e exemplos JSON
├── specs/
│   ├── README.md                       # Documentação da modelagem relacional
│   └── db/
│       ├── linketinder_db.sql          # DDL de criação de tabelas, constraints e índices
│       ├── linketinder_populate.sql    # DML com dados fictícios para testes locais
│       ├── linketinder.dbml            # Esquema na notação DBML
│       ├── Linketinder.pdf             # Diagrama Entidade-Relacionamento (DER)
│       └── Linketinder.png             # Imagem do DER
├── src/
│   ├── main/groovy/org/uGustavoDev/
│   │   ├── Main.groovy                 # Ponto de entrada (executa ServidorAPI)
│   │   ├── ServidorAPI.groovy          # Configuração programática do Tomcat embutido
│   │   ├── Terminal.groovy             # CLI interativa de console (legada)
│   │   ├── builder/                    # Builders fluentes (Candidato, Empresa, Vaga)
│   │   ├── controller/
│   │   │   ├── api/                    # Servlets HTTP REST (BaseController, Candidato, Empresa, Vaga)
│   │   │   └── view/                   # Controladores da interface de console
│   │   ├── dao/                        # DAOs JDBC (BaseDao, Candidato, Empresa, Vaga, Competencia)
│   │   │   └── interfaces/             # Contratos de acesso a dados (ICandidatoDAO, etc.)
│   │   ├── dto/                        # DTOs de entrada da API (Cadastro de Candidato, Empresa, Vaga)
│   │   ├── exceptions/                 # DatabaseOperationException, ValidationException
│   │   ├── factory/                    # DependencyFactory, ConexaoSingleton, ConexaoFactory
│   │   ├── mapper/                     # Conversores DTO <-> Entidade
│   │   │   └── interfaces/             # Contratos de mapeamento
│   │   ├── model/                      # Entidades de domínio (Pessoa, Candidato, Empresa, Vaga)
│   │   │   └── interfaces/             # IPessoa
│   │   ├── service/                    # Camada de serviços e regras de negócio
│   │   │   └── interfaces/             # Contratos de serviço (ICandidatoService, etc.)
│   │   └── view/                       # Views e Facade da CLI de terminal
│   └── test/groovy/org/uGustavoDev/
│       ├── model/                      # Testes unitários de entidades (CandidatoSpec, EmpresaSpec)
│       └── services/                   # Testes unitários com Mocks Spock (Candidato, Empresa, Vaga)
├── frontend/
│   ├── index.html                      # Ponto de montagem HTML (div#app)
│   ├── package.json                    # Scripts e dependências do frontend
│   ├── tsconfig.json                   # Configuração do compilador TypeScript
│   ├── public/                         # Favicon e SVG spritesheets
│   └── src/
│       ├── main.ts                     # Ponto de entrada SPA e router hashchange
│       ├── style.css                   # Folha de estilos padronizada
│       ├── models/types.ts             # Tipos e interfaces TypeScript
│       ├── services/armazenamento.ts   # Persistência e consultas no LocalStorage
│       └── views/                      # Views modulares (Login, Cadastro, Perfis, Vagas)
└── pessoas.csv                         # Conjunto de dados inicial/legado (CSV)
```

---

## 3. Como Executar e Testar

### Backend (Groovy / Gradle)

1. **Compilar e Iniciar a API REST**:
   - Linux / macOS:
     ```bash
     ./gradlew run
     ```
   - Windows:
     ```cmd
     gradlew.bat run
     ```
   O servidor inicializa por padrão em `http://localhost:8081`.

2. **Executar a Suíte de Testes Automatizados (Spock)**:
   - Linux / macOS:
     ```bash
     ./gradlew test
     ```
   - Windows:
     ```cmd
     gradlew.bat test
     ```
   *(Valida regras de negócio em memória usando Spock Mocks sem tocar no banco de dados).*

3. **Executar Teste Específico**:
   ```bash
   ./gradlew test --tests "org.uGustavoDev.services.CandidatoServiceSpec"
   ```

4. **Executar Análise de Qualidade de Código (CodeNarc)**:
   ```bash
   ./gradlew codenarcMain codenarcTest
   ```
   *(Relatórios gerados em `build/reports/codenarc/`).*

### Frontend (TypeScript / Vite)

1. **Instalar Dependências**:
   ```bash
   cd frontend
   npm install
   ```

2. **Executar em Modo de Desenvolvimento**:
   ```bash
   npm run dev
   ```
   Disponível por padrão em `http://localhost:5173`.

3. **Build de Produção e Preview**:
   ```bash
   npm run build
   npm run preview
   ```

### Banco de Dados (PostgreSQL)

1. **Criar a Base de Dados**:
   ```bash
   createdb -U postgres linketinder_db
   ```
2. **Aplicar Estrutura de Tabelas e Índices (DDL)**:
   ```bash
   psql -U postgres -d linketinder_db -f specs/db/linketinder_db.sql
   ```
3. **Aplicar Dados Iniciais de Teste (DML)**:
   ```bash
   psql -U postgres -d linketinder_db -f specs/db/linketinder_populate.sql
   ```
4. **Configuração das Variáveis de Ambiente**:
   Criar um arquivo `.env` na raiz do projeto (ou exportar variáveis de ambiente):
   ```env
   DB_TYPE=POSTGRES
   DB_URL=jdbc:postgresql://localhost:5432/linketinder_db
   DB_USER=postgres
   DB_PASSWORD=postgres
   ```

---

## 4. Convenções e Padrões de Projeto

### Convenções de Nomenclatura e Código
- **Classes, Interfaces e Traits**: `PascalCase` (ex: `CandidatoService`, `ICompetenciaDAO`, `BaseController`).
- **Interfaces**: Prefixo `I` para contratos de DAOs, Services e Mappers (ex: `ICandidatoDAO`, `IVagaService`).
- **Métodos e Atributos**: `camelCase` (ex: `buscarPorEmail()`, `dataNascimento`).
- **Constantes**: `UPPER_SNAKE_CASE` (ex: `PORT`, `URL`, `DEFAULT_MODEL`).
- **Tabelas e Colunas de Banco**: `snake_case` (ex: `candidato_competencia`, `candidato_curtiu_em`).
- **Arquivos de Teste**: Sufixo `Spec` (ex: `VagaServiceSpec.groovy`), especificações escritas com strings descritivas em formato BDD (*given / when / then*).

### Padrões Arquiteturais e de Projeto
- **Arquitetura em Camadas (Layered MVC)**:
  - `Controller`: Recebe requisições HTTP, valida Content-Type, delega ao serviço e formata respostas.
  - `Service`: Centraliza validações de domínio, unicidade e orquestra múltiplos DAOs.
  - `Mapper`: Converte DTOs em modelos de domínio e vice-versa, desacoplando payloads externos do modelo interno.
  - `DAO`: Manipulação exclusiva de comandos SQL e mapeamento de `ResultSet`.
  - `Model`: Representação do estado das entidades com encapsulamento de regras locais.
- **Padrões de Criação e Estruturais**:
  - **Builder**: `CandidatoBuilder`, `EmpresaBuilder` e `VagaBuilder` para construção fluente de objetos.
  - **Factory**: `DependencyFactory` atuando como container manual de Injeção de Dependência (IoC); `ConexaoFactory` para alternância de provedor de banco de dados.
  - **Singleton**: `ConexaoSingleton` mantendo conexão única aberta durante a sessão; `DependencyFactory.getInstance()`.
  - **Facade**: `ViewFacade` agrupando operações de interação no modo console.
  - **Template Method / Callback**: `BaseDao.executarSQL()` e `executarSQLComRetornoDeChave()` encapsulando try-with-resources, preparação de statements e fechamento de conexões via closures Groovy.

---

## 5. Domínio e Modelo de Dados

### Diagrama Conceitual de Relacionamentos

```
  [Candidato] 1 --- N [candidato_competencia] N --- 1 [Competência]
                                                            |
                                                           N|
                                                   [empresa_competencia]
                                                           N|
                                                            |
  [Empresa]   1 ------------------------------------------- 1
      | 1
      |
      | N
   [Vaga]     1 --- N [vaga_competencia]      N --- 1 [Competência]
      | 1
      |
      | N
  [Curtida]   N --------------------------------- 1 [Candidato]
  (candidato_id, vaga_id, candidato_curtiu_em, empresa_curtiu_em)
```

### Detalhamento das Entidades

1. **Pessoa** (`abstract class Pessoa implements IPessoa`):
   - Atributos base: `id` (Integer), `nome` (String), `email` (String, único), `pais` (String), `CEP` (String), `descricao` (String), `senha` (String), `competencias` (List<String>).

2. **Candidato** (`Candidato extends Pessoa`):
   - Backend: adiciona `sobrenome` (String), `estado` (String), `cpf` (String, único), `dataNascimento` (LocalDate).
   - Frontend: tipos TypeScript adicionam `idade` (number) e lista de `formacoes` (`Formacao[]`: curso, instituição, nível).

3. **Empresa** (`Empresa extends Pessoa`):
   - Adiciona `cnpj` (String, único).

4. **Vaga** (`Vaga`):
   - Atributos: `id` (Integer), `empresaId` (Integer, FK obrigatória), `nome` (String), `descricao` (String), `estado` (String), `cidade` (String) [ou `local` no frontend], `competencias` (List<String>).

5. **Competência** (`competencias`):
   - Catálogo padronizado de habilidades (`id`, `nome` UNIQUE).
   - Relacionamento N:M através de tabelas associativas:
     - `candidato_competencia` (`candidato_id`, `competencia_id`)
     - `empresa_competencia` (`empresa_id`, `competencia_id`)
     - `vaga_competencia` (`vaga_id`, `competencia_id`)

6. **Curtida** (`curtidas`):
   - Tabela no banco de dados (`specs/db/linketinder_db.sql`):
     - `candidato_id` (FK `candidatos.id`)
     - `vaga_id` (FK `vagas.id`)
     - `candidato_curtiu_em` (Timestamp)
     - `empresa_curtiu_em` (Timestamp)
     - Chave primária composta: `(candidato_id, vaga_id)`.

7. **Match**:
   - Ocorre logicamente quando para uma mesma tupla `(candidato_id, vaga_id)`, tanto `candidato_curtiu_em` quanto `empresa_curtiu_em` não são nulos.

---

## 6. Regras de Negócio

### Regras Confirmadas no Código e Banco de Dados

1. **Unicidade de Identificadores e Contatos**:
   - Um candidato não pode ser cadastrado com email ou CPF já existente (`CandidatoService.adicionarCandidato`).
   - Uma empresa não pode ser cadastrada com email ou CNPJ já existente (`EmpresaService.adicionarEmpresa`).
   - A unicidade é garantida no banco de dados por constraints `UNIQUE` em `candidatos(email, cpf)` e `empresas(email, cnpj)`.

2. **Segurança de Credenciais**:
   - Senha mínima de 6 caracteres garantida via CHECK constraint SQL (`char_length(senha) >= 6`).

3. **Propriedade e Isolamento de Vagas**:
   - Toda vaga pertence obrigatoriamente a uma empresa existente (`empresaId` validado no Service e FK com `ON DELETE CASCADE`).
   - Uma empresa só pode atualizar ou excluir vagas que pertençam legitimamente ao seu próprio `empresaId` (`VagaService.atualizarVagaDaEmpresa`, `VagaService.deletarVagaDaEmpresa`).

4. **Gestão Dinâmica de Competências**:
   - Competências são inseridas sob demanda (`buscarOuInserir` em `CompetenciaDAO`). Se a competência textual já existir, seu ID é reaproveitado; caso contrário, é criada no catálogo.
   - Atualizações cadastrais removem os vínculos anteriores do usuário/vaga e recriam os novos relacionamentos em lote.

### Regras Conceituais de Curtida e Match (Modeladas no Banco, Não Implementadas na Aplicação)

- **Fluxo de Curtida da Vaga**: O candidato avalia as vagas de forma anônima e registra interesse. No banco, preenche `candidato_curtiu_em`.
- **Fluxo de Curtida do Candidato**: A empresa avalia candidatos com base em competências. No banco, preenche `empresa_curtiu_em` vinculado à vaga que está buscando preencher.
- **Condição de Match**: Validada quando:
  ```sql
  candidato_curtiu_em IS NOT NULL AND empresa_curtiu_em IS NOT NULL
  ```

---

## 7. Princípio do Anonimato

O anonimato é a premissa central de integridade do LinkeTinder para eliminar vieses na triagem.

### Regras Confirmadas: Exibição ANTES do Match

| Visualizador  | Alvo Visualizado | O Que DEVE Ser Exibido                                                                                                                        | O Que DEVE Ser Ocultado (Sigilo)                                                                                                             |
|:--------------|:-----------------|:----------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------|
| **Empresa**   | **Candidato**    | - ID anônimo (`#id`)<br>- Estado e País<br>- Descrição profissional<br>- Competências técnicas<br>- Formações acadêmicas (curso, instituição) | - **Nome completo e sobrenome**<br>- **CPF**<br>- **E-mail pessoal**<br>- **Data de nascimento / Idade exata**<br>- **Endereço exato / CEP** |
| **Candidato** | **Vaga**         | - Título do cargo / Nome da vaga<br>- Localidade (Cidade, Estado)<br>- Descrição da oportunidade<br>- Competências técnicas exigidas          | - **Nome / Razão Social da Empresa**<br>- **CNPJ**<br>- **E-mail de contato da empresa**                                                     |

*Implementações de referência no repositório:*
- Terminal: `Candidato.toAnonymousString()` e `Vaga.toString()`.
- Frontend: Tabela anônima em `PerfilEmpresa.ts` e listagem de vagas em `VagasCandidato.ts`.

---

## 8. Procedimentos e Versionamento de Banco de Dados

- **Localização dos Scripts**:
  - DDL: `specs/db/linketinder_db.sql`
  - DML (População de testes): `specs/db/linketinder_populate.sql`
- **Tabelas Versionadas**:
  - `candidatos`
  - `empresas`
  - `competencias`
  - `candidato_competencia`
  - `empresa_competencia`
  - `vagas`
  - `vaga_competencia`
  - `curtidas`
- **Regras de Aplicação**:
  - Modificações na modelagem devem ser registradas tanto no arquivo DBML (`specs/db/linketinder.dbml`) quanto no script SQL (`specs/db/linketinder_db.sql`).
  - Não há ferramenta de migração automatizada instalada (ex: Flyway/Liquibase). A aplicação é feita manualmente via cliente SQL (`psql`).

---

## 9. Restrições e Diretrizes para Agentes de IA

1. **Não alterar funcionalidades fora do escopo sem autorização explícita**:
   - Não refatorar ou reescrever camadas existentes (ex: substituir Servlets por Spring Boot/Micronaut) sem solicitação direta do usuário.
2. **Não inventar regras de negócio**:
   - Não deduzir regras não documentadas sobre curtidas, pesos de competências, match ou algoritmos de ranking sem validação prévia.
3. **Preservar rigidamente o Princípio do Anonimato**:
   - Qualquer nova tela, endpoint ou comando que liste candidatos para empresas antes do match **nunca** deve conter dados pessoais identificáveis (nome, CPF, email, nascimento, CEP).
4. **Garantir a integridade do código**:
   - Ao alterar código Groovy, certifique-se de executar `./gradlew test` e `./gradlew codenarcMain` para manter a suíte de testes íntegra e sem novas violações de estilo.
5. **Utilizar tipagem estática em todos os escopos**: 
   - Todo código Groovy novo ou alterado deve utilizar tipagem estática, evitando declarações com def e outros recursos que dependam de tipagem dinâmica.
