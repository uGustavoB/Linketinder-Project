<h1 align="center">LinkeTinder - Frontend SPA</h1>

## Descrição

Bem-vindo ao frontend da plataforma **LinkeTinder**, uma Single Page Application (SPA) construída em **TypeScript** utilizando **Vite** e **Chart.js**.

Esta aplicação gerencia dados e sessões via `localStorage`, mantendo regras estritas de anonimato mútuo entre candidatos e empresas antes do match, e fornecendo elementos de visualização de dados.

---

## Funcionalidades

- **Autenticação e Roteamento por Perfil**:
  - Tela de Login com validação de credenciais em texto claro e identificação automática do perfil de acesso (**Candidato** ou **Empresa**).
  - Controle de sessão e redirecionamento dinâmico via hash routing (`#/login`, `#/home-candidato`, `#/home-empresa`, etc.).

- **Cadastro Dinâmico de Usuários**:
  - Formulário único e flexível com alternância imediata entre Candidato e Empresa.
  - Inserção dinâmica de **Competências** (botão "Nova competência" com adição gradual e limpeza de campo).
  - Inserção dinâmica de **Formações Acadêmicas** para candidatos (informando curso e instituição de ensino).

- **Visão do Candidato**:
  - **Lista de Vagas com Anonimato**: Visualização de todas as oportunidades disponíveis com cargo, local, descrição e competências requeridas, preservando em sigilo o nome e CNPJ da empresa contratante.
  - **Meu Perfil**: Tela exclusiva em modo de leitura para consulta dos próprios dados pessoais, biografia, competências e histórico educacional.

- **Visão da Empresa**:
  - **Gestão de Vagas (Create & Delete)**: Formulário para criação de novas vagas e listagem das vagas da própria empresa com botão de exclusão imediata.
  - **Lista de Candidatos com Anonimato**: Tabela com todos os candidatos do sistema, exibindo apenas curso, instituição, competências e descrição (sem expor dados pessoais como nome, CPF ou e-mail).
  - **Gráfico de Barras por Competência (Chart.js)**: Gráfico interativo e responsivo que consolida e quantifica a distribuição das competências técnicas entre os candidatos cadastrados.
  - **Meu Perfil**: Visualização detalhada dos dados corporativos e competências exigidas pela organização.

- **Armazenamento e Persistência**:
  - Persistência contínua de usuários, vagas e sessão ativa no `localStorage`.

---

## Estrutura do Projeto

```
frontend/
├── public/                 # Recursos estáticos e favicons
├── src/
│   ├── assets/             # Ícones e assets gráficos
│   ├── models/
│   │   └── types.ts        # Interfaces e tipagens TypeScript (Pessoa, Candidato, Empresa, Vaga, etc.)
│   ├── services/
│   │   └── armazenamento.ts # Camada de persistência e gerenciamento no localStorage
│   ├── views/
│   │   ├── CadastroPessoa.ts # View unificada de cadastro de candidatos e empresas
│   │   ├── HomeCandidato.ts  # Painel principal do candidato autenticado
│   │   ├── HomeEmpresa.ts    # Painel principal da empresa autenticada
│   │   ├── Login.ts          # View de login e autenticação
│   │   ├── MeuPerfil.ts      # Visualização dos dados do próprio usuário (read-only)
│   │   ├── PerfilEmpresa.ts  # Painel da empresa: gestão de vagas, lista anônima e gráfico Chart.js
│   │   └── VagasCandidato.ts # Listagem anônima de vagas para candidatos
│   ├── main.ts             # Ponto de entrada da SPA e gerenciador de rotas (hashchange)
│   └── style.css           # Estilização global padronizada
├── index.html              # HTML base da aplicação
├── package.json            # Metadados, scripts e dependências do projeto
└── tsconfig.json           # Configurações do compilador TypeScript
```

---

## Descrição dos Arquivos Principais

| Arquivo | Descrição |
| :--- | :--- |
| `main.ts` | Ponto de entrada da SPA; implementa o roteador baseado em hash (`#/rota`) e monta os componentes na `div#app`. |
| `types.ts` | Contratos e tipos TypeScript para `Pessoa`, `Candidato`, `Empresa`, `Vaga`, `Formacao`, `Competencia` e `UsuarioLogado`. |
| `armazenamento.ts` | Serviço responsável pela leitura, escrita e sincronização de dados no `localStorage`. |
| `Login.ts` | View de autenticação que valida as credenciais informadas e inicializa a sessão do usuário. |
| `CadastroPessoa.ts` | View de registro de usuários com seletores dinâmicos para inclusão de competências e formações acadêmicas. |
| `HomeCandidato.ts` | Menu inicial com as opções de acesso para o candidato logado. |
| `HomeEmpresa.ts` | Menu inicial com as opções de acesso para a empresa logada. |
| `VagasCandidato.ts` | Tabela anônima de oportunidades abertas para o candidato. |
| `PerfilEmpresa.ts` | Painel completo da empresa: criação e exclusão de vagas, listagem anônima de candidatos e plotagem de gráfico Chart.js. |
| `MeuPerfil.ts` | View somente leitura dos dados cadastrais do usuário logado (candidato ou empresa). |
| `style.css` | Folha de estilos padronizada baseada em layout limpo com paleta profissional. |

---

## Modelagem de Dados

### Candidato (`Candidato extends Pessoa`)
| Propriedade | Tipo | Descrição |
| :--- | :--- | :--- |
| **id** | `string` | Identificador único |
| **nome** | `string` | Nome completo do candidato |
| **email** | `string` | E-mail para acesso e contato |
| **senha** | `string` | Senha em texto claro |
| **cpf** | `string` | Cadastro de Pessoa Física |
| **idade** | `number` | Idade do candidato |
| **pais / estado / cep** | `string` | Localização e endereço |
| **descricao** | `string` | Resumo sobre o profissional |
| **competencias** | `Competencia[]` | Lista de habilidades técnicas |
| **formacoes** | `Formacao[]` | Lista de formações acadêmicas (curso e instituição) |

### Empresa (`Empresa extends Pessoa`)
| Propriedade | Tipo | Descrição |
| :--- | :--- | :--- |
| **id** | `string` | Identificador único |
| **nome** | `string` | Razão Social / Nome da empresa |
| **email** | `string` | E-mail corporativo |
| **senha** | `string` | Senha em texto claro |
| **cnpj** | `string` | Cadastro Nacional da Pessoa Jurídica |
| **pais / estado / cep** | `string` | Sede e localização da empresa |
| **descricao** | `string` | Missão e descrição da empresa |
| **competencias** | `Competencia[]` | Tecnologias e competências de interesse |

---

## Tecnologias

- **TypeScript** (Tipagem estática e segurança na arquitetura de código)
- **Vite** (Bundler ultrarrápido para desenvolvimento frontend moderno)
- **Chart.js** (Biblioteca para plotagem e renderização do gráfico de barras de competências)
- **HTML5 & CSS3** (Estruturação semântica e estilização responsiva)

---

## Como Executar

1. Navegue até a pasta do frontend:
   ```bash
   cd frontend
   ```

2. Instale as dependências:
   ```bash
   npm install
   ```

3. Inicie o servidor de desenvolvimento:
   ```bash
   npm run dev
   ```

4. Acesse no navegador o endereço indicado no terminal (por padrão: `http://localhost:5173`).

5. Para gerar o build de produção:
   ```bash
   npm run build
   ```

6. Para visualizar o build de produção localmente:
   ```bash
   npm run preview
   ```

---

## Autores

Este projeto foi desenvolvido por:

- **Gustavo Gabriel** - [GitHub](https://github.com/uGustavoB)
