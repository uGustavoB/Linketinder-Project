# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/v1.1.0/spec.html).

## [Não lançado]

### Adicionado
- Integração do fluxo de navegação do recrutador no terminal (login tripartite, menu autenticado, gestão de vagas da empresa, exploração anônima de candidatos e submenu corporativo de recrutadores na empresa) com controladores de view `RecrutadorController` e `EmpresaController`, injeção no `DependencyFactory` e testes unitários em `RecrutadorControllerSpec` e `EmpresaControllerSpec`.
- Classe `RecrutadorView` com formulário de dados (herança de `empresaId`), exibição formatada de perfil, menu de perfil restrito (sem auto-deleção) e menu de recrutador autenticado.
- Submenu de gestão de recrutadores (`mostrarMenuGestaoRecrutadores`) e listagem formatada de recrutadores vinculados na `EmpresaView`.
- Delegações correspondentes e menu de seleção de perfil de autenticação (`mostrarMenuTipoLogin`) adicionados ao `ViewFacade`.
- Testes unitários com Spock cobrindo os fluxos de visualização e menus em `RecrutadorViewSpec`, `EmpresaViewSpec` e `ViewFacadeSpec`.
- Método de autenticação `loginRecrutador(String email, String senha)` em `IRecrutadorService` e `RecrutadorService`, com testes unitários em `RecrutadorServiceSpec` cobrindo cenários de sucesso, senha incorreta, e-mail inexistente e propagação de `DatabaseOperationException`.
- Suíte de testes unitários Spock para `RecrutadorController` (`RecrutadorControllerSpec`) cobrindo o endpoint REST `PUT /recrutadores/{id}` com cenários de 200 OK, 404 Not Found, 409 Conflict, 400 Bad Request e 415 Unsupported Media Type.
- Endpoint REST `PUT /recrutadores/{id}` em `RecrutadorController` para suportar a atualização de recrutadores via API REST.
- Validação integrada da feature de recrutadores, execução bem-sucedida de testes automatizados e preservação integral do Princípio do Anonimato.
- `RecrutadorController` expondo os endpoints REST para gerenciamento de recrutadores (`/recrutadores/*`), registrado no `DependencyFactory` e no servidor embutido `ServidorAPI`.
- Interface `IRecrutadorService`, implementação `RecrutadorService` com validações de unicidade, vínculo corporativo e senha, acompanhadas da suíte de testes unitários com Spock Mocks (`RecrutadorServiceSpec`).
- Interface `IRecrutadorDAO` e implementação `RecrutadorDAO` com tipagem estática e suporte JDBC para operações de persistência e consulta de recrutadores.
- DTO de cadastro `RecrutadorCadastroDTO`, contrato `IRecrutadorMapper` e implementação `RecrutadorMapper` com tipagem estática, acompanhados de testes Spock.
- Entidade de domínio `Recrutador` e `RecrutadorBuilder` com tipagem estática estrita (`groovy.transform.CompileStatic`) e testes Spock correspondentes.
- Tabela `recrutadores` no script DDL do banco de dados (`specs/db/linketinder_db.sql`) com restrições de chave estrangeira, unicidade de e-mail/CPF e validação de senha.
- Documentação técnica e de diretrizes para agentes e desenvolvedores em `AGENTS.md`.
- Definição estruturada da skill de desenvolvimento orientado por especificação (`feature-com-spec`).

---

## [0.1.0] — 2026-10-21 (Estimado com base no histórico)

### Adicionado
- Endpoints de cadastro e listagem de vagas (`POST /vagas`, `GET /vagas`).
- Endpoints de cadastro e listagem de empresas (`POST /empresas`, `GET /empresas`).
- Endpoint de cadastro e listagem de candidatos (`POST /candidatos`, `GET /candidatos`).
- Servidor HTTP embutido baseado em Apache Tomcat Embed Core (`ServidorAPI.groovy`).
- Camada de persistência relacional via JDBC (DAOs, Conexão via Singleton e Factory).
- Suíte de testes automatizados com Spock Framework e validação de qualidade estática com CodeNarc.
- Interface Frontend SPA em TypeScript e Vite com persistência em `LocalStorage` e gráficos de competências.
- Fluxo interativo de console legado (`Terminal.groovy`) estruturado com padrão Facade.
- Padrões de projeto implementados: Builder, Factory, Singleton, DAO e Mappers.
