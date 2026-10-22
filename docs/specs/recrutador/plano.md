# Plano de Implementação — Gestão de Recrutadores (Back-End)

Este plano organiza as tarefas atômicas e sequenciais para implementação da feature de Recrutadores no Back-End do LinkeTinder, abrangendo banco de dados e camadas de back-end com testes e atualização obrigatória do `CHANGELOG.md` a cada tarefa.

---

- [x] **Tarefa 1: Adição da Tabela `recrutadores` no Script SQL (Banco de Dados)**
  - *Objetivo:* Criar a tabela `recrutadores` no script DDL `specs/db/linketinder_db.sql`.
  - *Status:* Concluído. DDL adicionado com constraints, índices e changelog atualizado.

---

- [x] **Tarefa 2: Implementação do Modelo de Domínio e Builders de Recrutador (Back-End)**
  - *Objetivo:* Criar a entidade `Recrutador` e o `RecrutadorBuilder` com tipagem estática.
  - *Status:* Concluído. Entidade, Builder e testes Spock (`RecrutadorSpec`) implementados e validados.

---

- [x] **Tarefa 3: Implementação dos DTOs e Mappers de Recrutador (Back-End)**
  - *Objetivo:* Criar o DTO de cadastro `RecrutadorCadastroDTO` e o mapeador `RecrutadorMapper`.
  - *Status:* Concluído. DTO, interface mapper, implementação e testes unitários Spock (`RecrutadorMapperSpec`) implementados e validados.

---

- [x] **Tarefa 4: Implementação da Camada DAO de Recrutadores (Back-End / Banco de Dados)**
  - *Objetivo:* Criar a interface `IRecrutadorDAO` e a classe `RecrutadorDAO` com operações JDBC para persistência e consulta.
  - *Status:* Concluído. Interface DAO, implementação JDBC estaticamente tipada e atualização do changelog realizadas com sucesso. Validadas por compilação e suíte de testes.

---

- [x] **Tarefa 5: Implementação da Camada de Serviço e Testes Spock (Back-End)**
  - *Objetivo:* Criar `IRecrutadorService` e `RecrutadorService` com validações de regra de negócio, acompanhados da suíte de testes unitários com Spock Mocks (`RecrutadorServiceSpec`).
  - *Status:* Concluído. Serviços, interface e testes unitários implementados e validados com 100% de sucesso.

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
