# Plano de Implementação — Gestão de Recrutadores (Back-End)

Este plano organiza as tarefas atômicas e sequenciais para implementação da feature de Recrutadores no Back-End do LinkeTinder, abrangendo banco de dados e camadas de back-end com testes e atualização obrigatória do `CHANGELOG.md` a cada tarefa.

---

- [x] **Tarefa 1: Adição da Tabela `recrutadores` no Script SQL (Banco de Dados)**
  - *Objetivo:* Criar a tabela `recrutadores` no script DDL `specs/db/linketinder_db.sql`.
  - *Status:* Concluído. DDL adicionado com constraints, índices e changelog atualizado.

---

## Tarefa 2: Implementação do Modelo de Domínio e Builders de Recrutador (Back-End)
- **Objetivo:** Criar a entidade `Recrutador` estipulando os atributos de domínio (`id`, `empresaId`, `nome`, `area`, `descricao`, `email`, `cpf`, `dataNascimento`, `senha`) com tipagem estática estrita (`groovy.transform.CompileStatic`) e o `RecrutadorBuilder` para construção fluente em testes.
- **Camada afetada:** Back-End (Domínio / Builders).
- **Arquivos a criar ou alterar:**
  - `src/main/groovy/org/uGustavoDev/model/Recrutador.groovy` (Criar)
  - `src/main/groovy/org/uGustavoDev/builder/RecrutadorBuilder.groovy` (Criar)
  - `CHANGELOG.md` (Alterar)
- **Testes necessários:** Testes unitários de instanciação do modelo e builder (`src/test/groovy/org/uGustavoDev/model/RecrutadorSpec.groovy`).
- **Comandos ou verificações para validar a conclusão:** `./gradlew test --tests "org.uGustavoDev.model.RecrutadorSpec"`
- **Critérios de aceite relacionados:** Estrutura correta da entidade conforme especificação.
- **Verificações de preservação do Princípio do Anonimato:** Assegurar que nenhuma propriedade sensível de recrutador seja exposta em métodos de serialização pública.

---

## Tarefa 3: Implementação dos DTOs e Mappers de Recrutador (Back-End)
- **Objetivo:** Criar o DTO de cadastro `RecrutadorCadastroDTO` e o mapeador `RecrutadorMapper` (implementando `IRecrutadorMapper`) para converter DTOs em instâncias de `Recrutador` com tipagem estática.
- **Camada afetada:** Back-End (DTO / Mappers).
- **Arquivos a criar ou alterar:**
  - `src/main/groovy/org/uGustavoDev/dto/RecrutadorCadastroDTO.groovy` (Criar)
  - `src/main/groovy/org/uGustavoDev/mapper/interfaces/IRecrutadorMapper.groovy` (Criar)
  - `src/main/groovy/org/uGustavoDev/mapper/RecrutadorMapper.groovy` (Criar)
  - `CHANGELOG.md` (Alterar)
- **Testes necessários:** Testes unitários do mapper.
- **Comandos ou verificações para validar a conclusão:** `./gradlew test`
- **Critérios de aceite relacionados:** Conversão correta entre payload de entrada e entidade de domínio.
- **Verificações de preservação do Princípio do Anonimato:** Isolamento estrito de DTOs internos de recrutador.

---

## Tarefa 4: Implementação da Camada DAO de Recrutadores (Back-End / Banco de Dados)
- **Objetivo:** Criar a interface `IRecrutadorDAO` e a classe de acesso a dados `RecrutadorDAO` estendendo `BaseDao`, provendo operações JDBC para inserir, buscar por ID, e-mail ou CPF, listar por empresa e deletar recrutadores.
- **Camada afetada:** Back-End (DAO) e Banco de Dados.
- **Arquivos a criar ou alterar:**
  - `src/main/groovy/org/uGustavoDev/dao/interfaces/IRecrutadorDAO.groovy` (Criar)
  - `src/main/groovy/org/uGustavoDev/dao/RecrutadorDAO.groovy` (Criar)
  - `CHANGELOG.md` (Alterar)
- **Testes necessários:** Testes de persistência e consulta DAO.
- **Comandos ou verificações para validar a conclusão:** `./gradlew test`
- **Critérios de aceite relacionados:** Garantia de unicidade via consultas DAO e persistência correta com `empresa_id`.
- **Verificações de preservação do Princípio do Anonimato:** Operações de banco restritas ao escopo corporativo.

---

## Tarefa 5: Implementação da Camada de Serviço e Testes Spock (Back-End)
- **Objetivo:** Criar `IRecrutadorService` e `RecrutadorService` validando regras de negócio (unicidade de e-mail e CPF, verificação de existência da empresa, tratamento de senha e filtragem por área/acesso), acompanhados da suíte de testes unitários com Spock Mocks (`RecrutadorServiceSpec`).
- **Camada afetada:** Back-End (Serviços / Regras de Negócio).
- **Arquivos a criar ou alterar:**
  - `src/main/groovy/org/uGustavoDev/service/interfaces/IRecrutadorService.groovy` (Criar)
  - `src/main/groovy/org/uGustavoDev/service/RecrutadorService.groovy` (Criar)
  - `src/test/groovy/org/uGustavoDev/services/RecrutadorServiceSpec.groovy` (Criar)
  - `CHANGELOG.md` (Alterar)
- **Testes necessários:** Testes unitários cobrindo cenários de sucesso, duplicidade de e-mail/CPF, senha inválida e vínculo com empresa.
- **Comandos ou verificações para validar a conclusão:** `./gradlew test --tests "org.uGustavoDev.services.RecrutadorServiceSpec"`
- **Critérios de aceite relacionados:** Cobertura de todos os cenários Gherkin definidos na `spec.md`.
- **Verificações de preservação do Princípio do Anonimato:** Validação de que serviços de recrutadores não vazam dados para candidatos.

---

## Tarefa 6: Implementação do Controller REST e Registro de Dependências (Back-End)
- **Objetivo:** Criar `RecrutadorController` estendendo `BaseController` para expor os endpoints REST de cadastro (`POST /recrutadores`) e listagem/exclusão, atualizando o `DependencyFactory` e o `ServidorAPI` para registrar o servlet.
- **Camada afetada:** Back-End (Controladores / API REST / IoC).
- **Arquivos a criar ou alterar:**
  - `src/main/groovy/org/uGustavoDev/controller/api/RecrutadorController.groovy` (Criar)
  - `src/main/groovy/org/uGustavoDev/factory/DependencyFactory.groovy` (Alterar)
  - `src/main/groovy/org/uGustavoDev/ServidorAPI.groovy` (Alterar)
  - `CHANGELOG.md` (Alterar)
- **Testes necessários:** Testes de integração/unitários de controller.
- **Comandos ou verificações para validar a conclusão:** Inicialização do servidor embutido via `./gradlew run` e requisições HTTP validando os endpoints.
- **Critérios de aceite relacionados:** Exposição correta dos endpoints REST e tratamento de erros via `BaseController`.
- **Verificações de preservação do Princípio do Anonimato:** Endpoints protegidos e desvinculados de rotas de candidatos.

---

## Tarefa 7: Validação Integrada, Análise CodeNarc e Regressão (Back-End / Qualidade)
- **Objetivo:** Executar a suíte completa de testes (`./gradlew test`), executar a análise estática CodeNarc (`./gradlew codenarcMain codenarcTest`) e garantir que não há regressões em nenhuma funcionalidade pré-existente (Candidatos, Empresas, Vagas).
- **Camada afetada:** Back-End / Qualidade e Testes.
- **Arquivos a criar ou alterar:**
  - `CHANGELOG.md` (Alterar)
- **Testes necessários:** Suíte completa de testes automatizados e CodeNarc.
- **Comandos ou verificações para validar a conclusão:** 
  ```bash
  ./gradlew test
  ./gradlew codenarcMain codenarcTest
  ```
- **Critérios de aceite relacionados:** 100% de sucesso nos testes, sem violações estáticas indevidas de CodeNarc e preservação de todas as regras anteriores.
- **Verificações de preservação do Princípio do Anonimato:** Auditoria final confirmando que o anonimato de candidatos permanece intacto e recrutadores permanecem em sigilo.
