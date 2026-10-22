# Especificação — Gestão de Recrutadores (Back-End)

## 1. Contexto e Objetivo
- **Contexto:** O LinkeTinder possui suporte a empresas e vagas, mas atualmente carece de uma entidade de **Recrutador** associada às empresas para gerenciar as oportunidades.
- **Objetivo:** Implementar no Back-End (Groovy/JDBC/Tomcat) o domínio, persistência, serviços e endpoints REST para cadastro, listagem, atualização, filtragem por área e exclusão de recrutadores vinculados a uma empresa, com tipagem estática estrita e foco exclusivo no Back-End.

---

## 2. Regras de Negócio e Requisitos Técnicos
1. **Atributos Obrigatórios da Entidade Recrutador:**
   - `id` (Integer, autoincrementado)
   - `empresaId` (Integer, FK obrigatória para a tabela `empresas` com `ON DELETE CASCADE`)
   - `nome` (String, obrigatório)
   - `area` (String, obrigatório — ex: TI, RH, Marketing, Segurança da Informação, etc.)
   - `descricao` (String, opcional)
   - `email` (String, único, obrigatório)
   - `cpf` (String, único, obrigatório, formato `XXX.XXX.XXX-XX`)
   - `dataNascimento` (LocalDate, opcional/obrigatório conforme regra de cadastro)
   - `senha` (String, obrigatória, mínimo de 6 caracteres)
2. **Unicidade de Dados:**
   - O e-mail e o CPF do recrutador devem ser estritamente únicos no banco de dados. Tentativas de duplicidade devem lançar exceção de validação (`IllegalArgumentException` ou `ValidationException`).
3. **Vínculo com Empresa:**
   - Cada recrutador pertence obrigatoriamente a uma única empresa (`empresaId`).
   - A empresa pode possuir múltiplos recrutadores.
4. **Permissões e Operações:**
   - O cadastro e a exclusão do recrutador são realizados por um administrador da empresa (ou via fluxo administrativo da empresa).
   - Tanto administradores de empresas quanto recrutadores podem gerenciar vagas.
   - O sistema deve permitir configurar um perfil de acesso (ou atributo de restrição) para que um recrutador visualize apenas as vagas da sua respectiva área ou todas as vagas da empresa.
5. **Sigilo Absoluto:**
   - Dados pessoais e credenciais do recrutador nunca são expostos a candidatos.
6. **Diretrizes de Código:**
   - Tipagem estática estrita em todas as classes Groovy (`groovy.transform.CompileStatic` ou tipagem explícita sem `def`).
   - Arquitetura em camadas: `Model`, `DTO`, `Mapper`, `DAO`, `Service`, `Controller` (Servlet).

---

## 3. Cenários de Sucesso, Falha e Casos de Borda
- **Cenários de Sucesso:**
  - Cadastro de recrutador válido vinculado a uma empresa existente.
  - Listagem de recrutadores de uma empresa.
  - Listagem de vagas filtradas pela área do recrutador ou acesso total.
  - Exclusão bem-sucedida de recrutador por ID.
- **Casos de Falha e Borda:**
  - Cadastro com e-mail ou CPF já existente (lança exceção).
  - Senha com menos de 6 caracteres (violação de constraint ou validação de serviço).
  - Tentativa de vincular recrutador a uma empresa inexistente.

---

## 4. Critérios de Aceite (Given / When / Then)

### Feature: Gestão de Recrutadores

```gherkin
Cenário: Cadastro com sucesso de um recrutador vinculado à empresa
  Given que existe uma empresa cadastrada com ID 1
  And um DTO de cadastro de recrutador com e-mail único e CPF válido
  When o serviço de recrutador é acionado para adicionar o recrutador
  Then o recrutador é salvo no banco de dados com ID gerado
  And o vínculo com a empresa 1 é estabelecido

Cenário: Falha ao cadastrar recrutador com CPF ou e-mail duplicado
  Given que já existe um recrutador cadastrado com o CPF "123.456.789-00"
  When um novo recrutador com o mesmo CPF é enviado para cadastro
  Then uma exceção de validação é lançada impedindo a inserção

Cenário: Listagem de vagas respeitando o perfil de acesso por área
  Given um recrutador configurado com restrição para a área "TI"
  When o recrutador solicita a listagem de vagas da empresa
  Then apenas as vagas pertencentes à área "TI" são retornadas
```

---

## 5. Impactos em Funcionalidades Existentes
- Adição da tabela `recrutadores` no script SQL de banco de dados (`specs/db/linketinder_db.sql`).
- Registro do novo `RecrutadorController` no servidor embutido (`ServidorAPI.groovy`) e no `DependencyFactory`.
- Nenhuma alteração em arquivos de Front-End.

---

## 6. O que Ficará Fora do Escopo
1. Interface de Front-End (SPA TypeScript/Vite).
2. Funcionalidades de Curtidas e Match para recrutadores.
3. Fluxo interativo via terminal (`Terminal.groovy`).
