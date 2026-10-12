<h1 align="center">LinkeTinder - Documentação da API RESTful</h1>

## Descrição

Este documento detalha a arquitetura, os recursos aplicados, as configurações, os endpoints e os procedimentos de execução da **API RESTful** do projeto **LinkeTinder**.

A API foi desenvolvida em **Groovy** utilizando **Servlets HTTP** gerenciados por um servidor embutido **Apache Tomcat Embed Core**, conectando-se a um banco de dados relacional **PostgreSQL** por meio do padrão **DAO** e seguindo os princípios de arquitetura limpa em camadas.

---

## Recursos Aplicados na Implementação

### 1. Servidor HTTP Embutido (Tomcat Embed)
- **Tomcat Embed Core (`10.1.28`)**: O servidor é instanciado e gerenciado programaticamente pela classe `ServidorAPI`, permitindo executar a API diretamente pelo Gradle sem containers externos.
- **Mapeamento Programático**: Mapeamento de contextos e servlets decodificados (`addServlet` e `addServletMappingDecoded`) para as rotas `/candidatos/*`, `/empresas/*` e `/vagas/*`.

### 2. Classe Base para Controllers (`BaseController`)
Todos os controllers da API estendem a classe abstrata `BaseController` (que estende `HttpServlet`), garantindo padronização, reutilização de código e conformidade com o protocolo HTTP:

- **Serialização e Desserialização JSON**
- **Suporte a CORS (Cross-Origin Resource Sharing)**
- **Validação de Cabeçalhos HTTP**
- **Leitura do Corpo da Requisição**
- **Tratamento Centralizado de Exceções**
- **Helpers de Resposta HTTP**
### 3. Padrões de Projeto na Camada Web
- **Injeção de Dependências (Factory)**: A classe `DependencyFactory` (Singleton) centraliza a criação dos DAOs, Mappers, Services e Controllers, injetando as instâncias nos servlets correspondentes.
- **Data Transfer Object (DTO)**: Uso de DTOs (`CandidatoCadastroDTO`, `EmpresaCadastroDTO`, `VagaCadastroDTO`) para tipar e desacoplar a entrada da API das entidades do banco.
- **Mapper Pattern**: Conversores dedicados (`CandidatoMapper`, `EmpresaMapper`, `VagaMapper`) transformando DTOs em modelos de domínio.
- **Builder Pattern**: Instanciação fluente das entidades através de `CandidatoBuilder`, `EmpresaBuilder` e `VagaBuilder`.

---

## Configurações do Projeto

- **Porta Padrão**: `8081` (configurada em `ServidorAPI.groovy`).
- **URL Base**: `http://localhost:8081`
- **Diretório Base Temporário**: `build/tomcat`
- **Cabeçalhos Exigidos para `POST`**:
  ```http
  Content-Type: application/json
  ```

---

## Endpoints da API

### Resumo das Rotas

| Recurso        | Método | Rota          | Descrição                                      | Status Sucesso |
|----------------|--------|---------------|------------------------------------------------|----------------|
| **Candidatos** | `GET`  | `/candidatos` | Lista todos os candidatos cadastrados          | `200 OK`       |
| **Candidatos** | `POST` | `/candidatos` | Cadastra um novo candidato                     | `201 Created`  |
| **Empresas**   | `GET`  | `/empresas`   | Lista todas as empresas cadastradas            | `200 OK`       |
| **Empresas**   | `POST` | `/empresas`   | Cadastra uma nova empresa                      | `201 Created`  |
| **Vagas**      | `GET`  | `/vagas`      | Lista todas as vagas cadastradas               | `200 OK`       |
| **Vagas**      | `POST` | `/vagas`      | Cadastra uma nova vaga associada a uma empresa | `201 Created`  |

---

### Detalhamento dos Endpoints

#### 1. Candidatos

##### `GET /candidatos`
Retorna a lista completa de candidatos cadastrados.

- **Status de Sucesso**: `200 OK`
- **Exemplo de Resposta**:
  ```json
  [
    {
      "id": 1,
      "nome": "Gustavo",
      "sobrenome": "Gabriel",
      "email": "gustavo@email.com",
      "pais": "Brasil",
      "estado": "PB",
      "cep": "58000-000",
      "descricao": "Desenvolvedor Backend Groovy & Java",
      "cpf": "12345678901",
      "dataNascimento": [2000, 5, 15],
      "competencias": ["Groovy", "Java", "PostgreSQL", "Git"]
    }
  ]
  ```

##### `POST /candidatos`
Cadastra um novo perfil de candidato.

- **Headers**: `Content-Type: application/json`
- **Exemplo de Payload (`Request Body`)**:
  ```json
  {
    "nome": "Ana",
    "sobrenome": "Silva",
    "email": "ana.silva@email.com",
    "senha": "senhaSegura123",
    "pais": "Brasil",
    "estado": "SP",
    "cep": "01000-000",
    "descricao": "Desenvolvedora Full Stack",
    "cpf": "11122233344",
    "dataNascimento": "1998-08-20",
    "competencias": ["Java", "Spring Boot", "TypeScript"]
  }
  ```
- **Status de Sucesso**: `201 Created` (retorna o objeto criado com `id` gerado).

---

#### 2. Empresas

##### `GET /empresas`
Retorna a lista de todas as empresas cadastradas.

- **Status de Sucesso**: `200 OK`
- **Exemplo de Resposta**:
  ```json
  [
    {
      "id": 1,
      "nome": "Tech Corp",
      "email": "contato@techcorp.com",
      "pais": "Brasil",
      "cep": "01000-000",
      "descricao": "Empresa de tecnologia e inovação",
      "cnpj": "12.345.678/0001-90",
      "competencias": ["Java", "Cloud", "PostgreSQL"]
    }
  ]
  ```

##### `POST /empresas`
Cadastra uma nova empresa no sistema.

- **Headers**: `Content-Type: application/json`
- **Exemplo de Payload (`Request Body`)**:
  ```json
  {
    "nome": "Inova Labs",
    "email": "contato@inovalabs.com",
    "senha": "empresaSenha123",
    "pais": "Brasil",
    "cep": "58000-000",
    "descricao": "Laboratório de soluções em inteligência de dados",
    "cnpj": "98.765.432/0001-10",
    "competencias": ["Python", "Groovy", "Docker"]
  }
  ```
- **Status de Sucesso**: `201 Created`.

---

#### 3. Vagas

##### `GET /vagas`
Retorna todas as vagas disponíveis no sistema.

- **Status de Sucesso**: `200 OK`
- **Exemplo de Resposta**:
  ```json
  [
    {
      "id": 1,
      "empresaId": 1,
      "nome": "Desenvolvedor Backend Pleno",
      "descricao": "Desenvolvimento e manutenção de APIs RESTful",
      "estado": "PB",
      "cidade": "João Pessoa",
      "competencias": ["Groovy", "Java", "PostgreSQL"]
    }
  ]
  ```

##### `POST /vagas`
Cadastra uma nova oportunidade vinculada a uma empresa existente.

- **Headers**: `Content-Type: application/json`
- **Exemplo de Payload (`Request Body`)**:
  ```json
  {
    "empresaId": 1,
    "nome": "Desenvolvedor Backend Pleno",
    "descricao": "Atuar na criação de serviços em Groovy e Micronaut",
    "cidade": "João Pessoa",
    "estado": "PB",
    "competencias": ["Groovy", "Micronaut", "PostgreSQL", "Docker"]
  }
  ```
- **Status de Sucesso**: `201 Created`.
- **Regras de Negócio**: O campo `empresaId` é obrigatório; se omitido, retorna `400 Bad Request`.

---

## Como Executar

### 1. Iniciar o Servidor da API
Com o PostgreSQL em execução, inicie o servidor:

- **Linux / macOS**:
  ```bash
  ./gradlew run
  ```
- **Windows**:
  ```cmd
  gradlew.bat run
  ```

Aguarde a mensagem de confirmação no console:
```
Servidor iniciado na porta: http://localhost:8081
```

### 2. Executar os Testes Automatizados da API
A suite de testes utiliza o **Spock Framework** com mocks para validar controladores e regras de negócio:

- **Linux / macOS**:
  ```bash
  ./gradlew test
  ```
- **Windows**:
  ```cmd
  gradlew.bat test
  ```

Para executar exclusivamente os testes dos controllers da API:
```bash
./gradlew test --tests "org.uGustavoDev.controller.api.*"
```

---

## Voltar ao Início

[⬅ Voltar para a documentação principal](../README.md)
