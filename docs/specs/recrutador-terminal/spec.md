# Especificação — Versão Terminal de Recrutadores (CLI)

## 1. Contexto e Objetivo
- **Contexto:** A feature de **Recrutador** possui persistência JDBC, modelos, serviços, controller REST e testes unitários. No entanto, o ecossistema do LinkeTinder conta com uma interface de console interativa (`Terminal.groovy`, `ViewFacade`, views de terminal) utilizada para Candidatos e Empresas.
- **Objetivo:** 
  1. Disponibilizar endpoint REST `PUT /recrutadores/{id}` para atualização cadastral via API.
  2. Desenvolver a versão via terminal (CLI) completa para o perfil de **Recrutador**, com autenticação/login de 3 vias (Candidato, Empresa, Recrutador), cadastro e gestão de recrutadores exclusivos pela Empresa associada, manutenção de perfil próprio (sem auto-deleção), manutenção de vagas da empresa (CRUD completo) e exploração de candidatos sob estrita observância ao **Princípio do Anonimato**.

---

## 2. Regras de Negócio e Requisitos Técnicos
1. **Endpoint REST de Edição:**
   - Expor `PUT /recrutadores/{id}` na camada `RecrutadorController` (REST) recebendo `RecrutadorCadastroDTO` em JSON e retornando `200 OK` com dados atualizados ou erros adequados (`400`, `404`, `409`, `500`).
2. **Autenticação de 3 Vias no Terminal:**
   - O menu inicial público (deslogado) oferece login para 3 perfis: 1 - Candidato, 2 - Empresa, 3 - Recrutador.
   - O cadastro de novos recrutadores é **exclusivo da Empresa** e não deve estar disponível no menu público deslogado.
3. **Gestão de Recrutadores pela Empresa:**
   - Empresa autenticada possui menu dedicado de recrutadores:
     - Cadastrar Recrutador (vinculando automaticamente ao `empresaId` da empresa logada).
     - Listar Recrutadores vinculados à empresa.
     - Deletar Recrutador da empresa.
4. **Área do Recrutador Autenticado:**
   - **Meu Perfil:** Recrutador pode visualizar e editar seus próprios dados. Deleção de conta é proibida para o recrutador (exclusiva do painel da empresa).
   - **Vagas da Empresa Associada:** CRUD completo das vagas da empresa (Criar, Listar, Editar, Deletar), idêntico ao painel da empresa.
   - **Explorar Candidatos:** Listagem com anonimato rigoroso (sem nomes, sem dados de contato, sem curtidas/matches nesta etapa).
5. **Princípio do Anonimato:**
   - Em todas as exibições de candidatos para recrutadores no terminal, dados pessoais identificáveis (nome completo, CPF, e-mail, telefone, data de nascimento) permanecem ocultos/mascarados.

---

## 3. Cenários de Sucesso, Falha e Casos de Borda
- **Cenários de Sucesso:**
  - Atualização cadastral de recrutador via `PUT /recrutadores/{id}`.
  - Empresa logada cadastra novo recrutador vinculado ao seu ID.
  - Empresa lista e remove seus recrutadores cadastrados.
  - Recrutador efetua login informando e-mail e senha cadastrados.
  - Recrutador visualiza e atualiza seu próprio perfil.
  - Recrutador gerencia vagas da empresa (criação, listagem, edição, deleção).
  - Recrutador explora lista de candidatos de forma anônima.
- **Casos de Falha e Borda:**
  - Tentativa de login com credenciais incorretas (mensagem de erro sem expor detalhes).
  - Tentativa de cadastro com e-mail ou CPF duplicados (aviso de violação de unicidade).
  - Empresa tentando deletar recrutador inexistente ou pertencente a outra empresa.
  - Recrutador tentando acessar opção de deletar própria conta (opção inexistente no menu do perfil).

---

## 4. Critérios de Aceite (Given / When / Then)

### Feature: Endpoint REST e Terminal de Recrutadores
```gherkin
Cenário: Atualização de recrutador via REST
  Given que existe um recrutador cadastrado com ID 1
  When é enviada uma requisição "PUT /recrutadores/1" com novos dados válidos
  Then o sistema atualiza o registro no banco e retorna status 200 OK com o JSON atualizado

Cenário: Empresa cadastra recrutador associado
  Given que uma empresa está autenticada no terminal
  When seleciona a opção de gerenciar recrutadores e cadastra um novo recrutador com dados válidos
  Then o novo recrutador é persistido no banco vinculado ao ID da empresa

Cenário: Empresa deleta recrutador associado
  Given que a empresa possui um recrutador cadastrado
  When seleciona a opção de deletar e informa o ID do recrutador
  Then o recrutador é removido da base de dados

Cenário: Login bem-sucedido de recrutador no terminal
  Given que existe um recrutador cadastrado no sistema
  When o usuário seleciona a opção de login de recrutador e insere as credenciais corretas
  Then o sistema autentica a sessão e exibe o menu exclusivo do recrutador

Cenário: Recrutador gerencia vagas da empresa
  Given que o recrutador está autenticado
  When acessa o menu de vagas e cria, edita ou deleta uma vaga
  Then as alterações refletem diretamente nas vagas vinculadas à sua empresa associada

Cenário: Proteção do Princípio do Anonimato para recrutador
  Given que o recrutador está autenticado
  When seleciona a opção de explorar candidatos
  Then os candidatos são exibidos apenas com ID anônimo e competências, sem dados pessoais
```

---

## 5. Impactos em Funcionalidades Existentes
- Adição do método `doPut` em `api/RecrutadorController.groovy`.
- Expansão de `IRecrutadorService` / `RecrutadorService` para autenticação (`loginRecrutador`).
- Expansão de `ViewFacade.groovy`, `EmpresaView.groovy` e criação de `RecrutadorView.groovy`.
- Ajustes em `Terminal.groovy` para novos fluxos de menu de Empresa e Recrutador.

---

## 6. O que Ficará Fora do Escopo
1. Edição de dados cadastrais de recrutadores pelo painel da empresa (edição restrita ao próprio recrutador em seu perfil).
2. Deleção da conta de recrutador pelo próprio recrutador.
3. Curtidas, matches e interações de contato direto.
4. Alterações na SPA TypeScript/Vite.
