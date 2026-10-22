# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/v1.1.0/spec.html).

## [Não lançado]

### Adicionado
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
