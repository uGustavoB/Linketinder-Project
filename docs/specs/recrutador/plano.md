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

- [x] **Tarefa 6: Implementação do Controller REST e Registro de Dependências (Back-End)**
  - *Objetivo:* Criar `RecrutadorController` estendendo `BaseController` para expor os endpoints REST de recrutadores, atualizando o `DependencyFactory`, o `ServidorAPI` e o `Main`.
  - *Status:* Concluído. Controller REST implementado e registrado no container de dependências e no Tomcat embutido. Valorr validado por compilação e testes automatizados.

---

- [x] **Tarefa 7: Validação Integrada, Análise CodeNarc e Regressão (Back-End / Qualidade)**
  - *Objetivo:* Executar a suíte completa de testes (`./gradlew test`), executar a análise estática CodeNarc e garantir que não há regressões.
  - *Status:* Concluído. Testes executados com sucesso total (100% aprovados), sem regressões e com atualização do `CHANGELOG.md` e conformidade com o Princípio do Anonimato.

---

- [ ] **Tarefa 8: Implementação do Endpoint REST de Atualização (`PUT /recrutadores/{id}`)**
  - *Objetivo:* Expor a operação de atualização de recrutadores via API REST no `RecrutadorController`.
  - *Status:* Pendente. Aguardando execução conforme novo escopo aprovado.
