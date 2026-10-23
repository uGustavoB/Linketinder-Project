# Plano de Implementação — Versão Terminal de Recrutadores (CLI)

Este plano organiza as tarefas atômicas e sequenciais para disponibilizar o endpoint REST de edição e implementar toda a experiência interativa via terminal (CLI) para a feature de Recrutadores.

---

## Tarefa 1: Implementação do Endpoint REST de Edição (`PUT /recrutadores/{id}`)
- **Objetivo:** Adicionar o método `doPut` em `RecrutadorController` (REST) para permitir a atualização cadastral de recrutadores via API JSON, consumindo o serviço existente.
- **Arquivos afetados:**
  - `src/main/groovy/org/uGustavoDev/controller/api/RecrutadorController.groovy`
- **Cenários cobertos:** Atualização com dados válidos (`200 OK`), ID inexistente (`404`), dados inválidos/conflito de e-mail/CPF (`400`/`409`).
- **Validação:** `./gradlew test`

---

## Tarefa 2: Método de Autenticação no Serviço de Recrutadores
- **Objetivo:** Garantir a presença do contrato e implementação do método `loginRecrutador(String email, String senha)` em `IRecrutadorService` e `RecrutadorService`, permitindo autenticação por e-mail e senha no terminal.
- **Arquivos afetados:**
  - `src/main/groovy/org/uGustavoDev/service/interfaces/IRecrutadorService.groovy`
  - `src/main/groovy/org/uGustavoDev/service/RecrutadorService.groovy`
  - `src/test/groovy/org/uGustavoDev/services/RecrutadorServiceSpec.groovy`
- **Cenários cobertos:** Login com sucesso (retorna entidade), senha incorreta ou e-mail inexistente (retorna `null`).
- **Validação:** `./gradlew test --tests "org.uGustavoDev.services.RecrutadorServiceSpec"`

---

## Tarefa 3: Criação da `RecrutadorView`, Submenus de Gestão e Expansão do `ViewFacade`
- **Objetivo:** 
  1. Criar `RecrutadorView` para formulários de dados de recrutador (sem solicitar `empresaId`, pois é herdado da empresa logada), exibição formatada do perfil (com dados da empresa associada) e menu de perfil restrito (sem opção de auto-deleção).
  2. Implementar menu de gestão de recrutadores para a Empresa (`1 - Cadastrar Recrutador`, `2 - Listar Recrutadores`, `3 - Deletar Recrutador`, `0 - Voltar`) e listagem formatada de recrutadores.
  3. Expor as operações correspondentes no `ViewFacade`.
- **Arquivos afetados:**
  - `src/main/groovy/org/uGustavoDev/view/RecrutadorView.groovy` (Novo)
  - `src/main/groovy/org/uGustavoDev/view/EmpresaView.groovy`
  - `src/main/groovy/org/uGustavoDev/view/ViewFacade.groovy`
- **Cenários cobertos:** Menus e formulários isolados, sem expor opções inválidas ao perfil logado.
- **Validação:** `./gradlew classes`

---

## Tarefa 4: Integração de Controladores de View e Navegação no `Terminal.groovy`
- **Objetivo:**
  1. Atualizar menu deslogado: Login de 3 opções (Candidato, Empresa, Recrutador). Remover qualquer opção de cadastro de recrutador da tela pública.
  2. Atualizar menu da Empresa: Inclusão da opção `4 - Gerenciar Recrutadores`, permitindo cadastrar (associando a `empresaLogada.id`), listar e deletar recrutadores vinculados.
  3. Implementar menu do Recrutador autenticado:
     - `1 - Meu Perfil`: Ver perfil e editar dados (deleção proibida).
     - `2 - Gerenciar Vagas da Empresa`: CRUD completo de vagas (Criar, Listar, Editar, Deletar), reaproveitando as rotinas já testadas de vagas com `recrutadorLogado.empresaId`.
     - `3 - Explorar Candidatos`: Listagem de candidatos sob sigilo e anonimato rigorosos.
     - `0 - Sair (Logout)`.
- **Arquivos afetados:**
  - `src/main/groovy/org/uGustavoDev/controller/view/RecrutadorController.groovy` (Novo)
  - `src/main/groovy/org/uGustavoDev/controller/view/EmpresaController.groovy`
  - `src/main/groovy/org/uGustavoDev/Terminal.groovy`
- **Cenários cobertos:** Navegação de ponta a ponta para todos os perfis conforme regras de negócio.
- **Validação:** `./gradlew classes`

---

## Tarefa 5: Suíte de Testes Automatizados Unitários (Spock)
- **Objetivo:** Implementar/completar testes unitários Spock cobrindo login de recrutadores, deleção e listagem de recrutadores por empresa, e manutenção de vagas via recrutador.
- **Arquivos afetados:**
  - `src/test/groovy/org/uGustavoDev/services/RecrutadorServiceSpec.groovy`
- **Cenários cobertos:** Sucesso e exceções de persistência e validação.
- **Validação:** `./gradlew test`

---

## Tarefa 6: Validação Integrada, CodeNarc e Auditoria de Anonimato
- **Objetivo:** Executar suíte completa de testes, análise estática CodeNarc e certificar que candidatos permanecem 100% anônimos ao serem listados por recrutadores. Atualizar documentação de release.
- **Arquivos afetados:**
  - `CHANGELOG.md`
- **Validação:**
  ```bash
  ./gradlew test
  ./gradlew codenarcMain codenarcTest
  ```
