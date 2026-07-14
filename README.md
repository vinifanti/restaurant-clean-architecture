# Restaurant Clean Architecture API

Sistema de back-end para gestão de restaurantes, desenvolvido como **Tech Challenge — Fase 02** da Pós-Graduação em Arquitetura e Desenvolvimento Java (FIAP / POSTECH).

O projeto implementa o cadastro de **Tipos de Usuário**, **Restaurantes** e **Itens de Cardápio**, seguindo os princípios de **Clean Architecture** (Arquitetura Hexagonal / Ports & Adapters), com o objetivo de manter o domínio da aplicação isolado de frameworks e detalhes de infraestrutura.

> Repositório: https://github.com/higotaviano-commits/restaurant-clean-architecture

---

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Modelo de domínio](#modelo-de-domínio)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Como executar o projeto](#como-executar-o-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Testes](#testes)
- [Status em relação aos requisitos da Fase 02](#status-em-relação-aos-requisitos-da-fase-02)

---

## Sobre o projeto

Um grupo de restaurantes decidiu construir, em conjunto, um sistema único de gestão para reduzir custos com sistemas individuais. A Fase 02 do Tech Challenge expande o sistema com:

- Gestão de **tipos de usuário** (ex.: Dono de Restaurante, Cliente) e associação a usuários já cadastrados.
- Cadastro completo (CRUD) de **restaurantes**, vinculando cada um a um usuário responsável (dono).
- Cadastro completo (CRUD) de **itens de cardápio**, vinculados a um restaurante.

## Arquitetura

O projeto segue o padrão **Clean Architecture / Arquitetura Hexagonal**, separando o código em camadas com responsabilidades bem definidas e dependências apontando sempre para dentro (em direção ao domínio):

```
┌─────────────────────────────────────────────────────────────┐
│                        adapters (in/out)                    │
│  ┌───────────────┐                     ┌───────────────────┐│
│  │  Controllers   │  --> use cases -->  │  Persistence/JPA  ││
│  │  (REST/in)     │                     │  (out)             ││
│  └───────────────┘                     └───────────────────┘│
│              ▲                                    ▲          │
│              │                                    │          │
│  ┌───────────┴────────────────────────────────────┴────────┐ │
│  │                     application                          │ │
│  │      Services (casos de uso) + DTOs + Mappers            │ │
│  └───────────────────────────┬──────────────────────────────┘ │
│                               │                                │
│  ┌────────────────────────────▼──────────────────────────────┐│
│  │                        domain                              ││
│  │   Model (entidades de negócio) + Ports (input/output)      ││
│  │   Exceptions de negócio                                    ││
│  └──────────────────────────────────────────────────────────┘ │
│                                                                │
│                     infrastructure (config)                  │
└─────────────────────────────────────────────────────────────┘
```

**Camadas:**

| Camada | Responsabilidade |
|---|---|
| `domain` | Núcleo da aplicação. Contém os **models** (`User`, `Restaurant`, `MenuItem`, `UserType`) com suas regras de validação e invariantes de negócio, as **ports de entrada** (`*UseCase`, interfaces implementadas pelos services) e as **ports de saída** (`*RepositoryPort`, `PasswordEncoderPort`, interfaces implementadas pelos adapters). Não depende de nenhuma outra camada nem de frameworks. |
| `application` | Implementa os casos de uso (`*Service`), orquestrando chamadas ao domínio e às ports de saída. Contém os **DTOs** de entrada/saída e os **mappers** entre DTOs e entidades de domínio. |
| `adapters/in` | Adaptadores de entrada. Os **Controllers** REST recebem requisições HTTP e delegam para as ports de entrada (`*UseCase`). O `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza o tratamento de exceções de domínio e de validação, padronizando as respostas de erro da API. |
| `adapters/out` | Adaptadores de saída. Implementam as ports de saída definidas no domínio: **persistência JPA** (entidades JPA, repositórios Spring Data e mappers de persistência) e **segurança** (`BCryptPasswordEncoderAdapter`). |
| `infrastructure` | Configurações técnicas transversais, como `SecurityConfig` (Spring Security). |

Essa separação garante que:
- As regras de negócio (domínio) não conhecem detalhes de Spring, JPA ou HTTP.
- É possível trocar o banco de dados ou o framework web sem alterar o domínio, bastando reimplementar as ports.
- Cada módulo (`user`, `usertype`, `restaurant`, `menuitem`) segue a mesma organização interna, facilitando a manutenção.

## Estrutura de pastas

```
src/main/java/com/fiap/restaurant_api/
├── RestaurantApiApplication.java
├── adapters/
│   ├── in/controller/
│   │   ├── UserController.java
│   │   ├── UserTypeController.java
│   │   ├── RestaurantController.java
│   │   ├── MenuItemController.java
│   │   └── GlobalExceptionHandler.java
│   └── out/
│       ├── persistence/
│       │   ├── user/          (entity, mapper, repository, adapter)
│       │   ├── usertype/      (entity, mapper, repository, adapter)
│       │   ├── restaurant/    (entity, mapper, repository, adapter)
│       │   └── menuitem/      (entity, mapper, repository, adapter)
│       └── security/
│           └── BCryptPasswordEncoderAdapter.java
├── application/
│   ├── user/            (UserService, dto/, mapper/)
│   ├── usertype/         (UserTypeService, dto/, mapper/)
│   ├── restaurant/       (RestaurantService, dto/, mapper/)
│   └── menuitem/         (MenuItemService, dto/, mapper/)
├── domain/
│   ├── model/            (User, UserType, Restaurant, MenuItem)
│   ├── port/
│   │   ├── input/        (UserUseCase, UserTypeUseCase, RestaurantUseCase, MenuItemUseCase)
│   │   └── output/        (UserRepositoryPort, UserTypeRepositoryPort, RestaurantRepositoryPort,
│   │                       MenuItemRepositoryPort, PasswordEncoderPort)
│   └── exception/        (BusinessException, ValidationException)
└── infrastructure/
    └── config/SecurityConfig.java

src/main/resources/
└── application.properties

src/test/java/com/fiap/restaurant_api/
└── RestaurantApiApplicationTests.java
```

## Modelo de domínio

### `UserType`
| Campo | Tipo | Observação |
|---|---|---|
| id | Long | gerado pelo banco |
| name | String | obrigatório |
| description | String | opcional |
| lastUpdate | LocalDateTime | atualizado a cada mudança |

### `User`
| Campo | Tipo | Observação |
|---|---|---|
| id | Long | gerado pelo banco |
| name | String | obrigatório |
| email | String | obrigatório, único |
| login | String | obrigatório, único |
| password | String | obrigatório, mínimo 8 caracteres, armazenado com hash (BCrypt) |
| userType | UserType | associação obrigatória a um tipo de usuário |
| lastUpdate | LocalDateTime | atualizado a cada mudança |

### `Restaurant`
| Campo | Tipo | Observação |
|---|---|---|
| id | Long | gerado pelo banco |
| name | String | obrigatório |
| address | String | obrigatório |
| cuisineType | String | obrigatório |
| openingHour / closingHour | LocalTime | obrigatórios |
| owner | User | usuário responsável pelo restaurante |

### `MenuItem`
| Campo | Tipo | Observação |
|---|---|---|
| id | Long | gerado pelo banco |
| name | String | obrigatório |
| description | String | obrigatório |
| price | BigDecimal | obrigatório, maior que zero |
| dineInOnly | boolean | indica se o item só pode ser consumido no restaurante |
| imagePath | String | caminho onde a foto do prato seria armazenada (upload de arquivo fora do escopo) |
| restaurant | Restaurant | restaurante ao qual o item pertence |

As regras acima (obrigatoriedade de campos, tamanho mínimo de senha, preço maior que zero etc.) são validadas diretamente nas entidades de domínio, garantindo que um objeto inválido nunca seja instanciado — independentemente da camada que o chame.

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3.5.0**
    - Spring Web
    - Spring Data JPA
    - Spring Validation (Bean Validation / Jakarta Validation)
    - Spring Security (configuração base com `BCryptPasswordEncoder`)
- **PostgreSQL 16** (banco de dados relacional)
- **H2** (banco em memória usado nos testes)
- **springdoc-openapi** (documentação OpenAPI/Swagger)
- **JJWT** (io.jsonwebtoken) — biblioteca de suporte a JWT incluída no projeto
- **Lombok**
- **JaCoCo** (relatório de cobertura de testes)
- **Docker / Docker Compose**
- **Maven** (com Maven Wrapper — `mvnw` / `mvnw.cmd`)

## Como executar o projeto

### Pré-requisitos
- Java 21
- Docker e Docker Compose
- (Opcional) Maven instalado — o projeto já inclui o Maven Wrapper

### 1. Clonar o repositório

```bash
git clone https://github.com/higotaviano-commits/restaurant-clean-architecture.git
cd restaurant-clean-architecture
```

### 2. Subir banco de dados e aplicação com Docker Compose

O `docker-compose.yml` sobe o PostgreSQL 16 e a aplicação Java em conjunto:

```bash
docker compose up -d --build
```

Aguarde alguns instantes enquanto a imagem é construída e o banco passa no healthcheck. A API estará disponível em `http://localhost:8080`.

Credenciais do banco:
- Banco: `restaurant_db`
- Usuário: `admin`
- Senha: `admin123`

> **Alternativa local:** para executar a aplicação fora do Docker (necessário o banco no ar via `docker compose up -d postgres`):
>
> ```bash
> ./mvnw spring-boot:run
> ```

A API sobe em `http://localhost:8080`.

> **Observação:** a estratégia `spring.jpa.hibernate.ddl-auto=create` recria o schema do banco a cada inicialização da aplicação — ideal para ambiente de desenvolvimento/avaliação, mas não recomendada para produção.

### 4. Documentação interativa (Swagger)

Como o projeto inclui `springdoc-openapi-starter-webmvc-ui`, a documentação interativa dos endpoints fica disponível, por padrão, em:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints da API

Base URL: `http://localhost:8080`

### Tipos de Usuário — `/user-types`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/user-types` | Cria um tipo de usuário |
| GET | `/user-types` | Lista todos os tipos de usuário |
| GET | `/user-types/{id}` | Busca um tipo de usuário por ID |
| PUT | `/user-types/{id}` | Atualiza um tipo de usuário |
| DELETE | `/user-types/{id}` | Remove um tipo de usuário |

### Usuários — `/users`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/users` | Cria um usuário (associado a um `userTypeId`) |
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca um usuário por ID |
| GET | `/users/search?name=` | Busca usuários pelo nome |
| PUT | `/users/{id}` | Atualiza dados de perfil (nome, email, login) |
| PATCH | `/users/{id}/password` | Altera a senha do usuário |
| DELETE | `/users/{id}` | Remove um usuário |

### Restaurantes — `/restaurants`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/restaurants` | Cria um restaurante (associado a um `ownerId`) |
| GET | `/restaurants` | Lista todos os restaurantes |
| GET | `/restaurants/{id}` | Busca um restaurante por ID |
| GET | `/restaurants/search?name=` | Busca restaurantes pelo nome |
| GET | `/restaurants/cuisine?type=` | Busca restaurantes pelo tipo de cozinha |
| PUT | `/restaurants/{id}` | Atualiza dados do restaurante |
| DELETE | `/restaurants/{id}` | Remove um restaurante |

### Itens de Cardápio — `/menu-items`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/menu-items` | Cria um item de cardápio (associado a um `restaurantId`) |
| GET | `/menu-items` | Lista todos os itens de cardápio |
| GET | `/menu-items/{id}` | Busca um item por ID |
| GET | `/menu-items/restaurant/{restaurantId}` | Lista os itens de um restaurante específico |
| PUT | `/menu-items/{id}` | Atualiza um item de cardápio |
| DELETE | `/menu-items/{id}` | Remove um item de cardápio |

### Exemplo de payload — criação de restaurante

```json
POST /restaurants
{
  "name": "Cantina da Nonna",
  "address": "Rua das Flores, 123",
  "cuisineType": "Italiana",
  "openingHour": "11:00:00",
  "closingHour": "23:00:00",
  "ownerId": 1
}
```

### Exemplo de payload — criação de item de cardápio

```json
POST /menu-items
{
  "name": "Lasanha à Bolonhesa",
  "description": "Lasanha tradicional com molho bolonhesa e queijo gratinado",
  "price": 45.90,
  "dineInOnly": false,
  "imagePath": "/images/lasanha.jpg",
  "restaurantId": 1
}
```

## Testes

O projeto está configurado com:
- **JUnit 5** e **Spring Boot Test** para testes automatizados.
- **H2** como banco em memória para a execução dos testes, isolando-os do PostgreSQL de produção.
- **JaCoCo** configurado no `pom.xml` para gerar relatório de cobertura a cada execução do Maven (`./mvnw test`), disponível em `target/site/jacoco/index.html`.

```bash
./mvnw test
```

### Collection Postman

A collection para testes via Postman está em `postman/restaurant-api.postman_collection.json` e cobre todos os 27 endpoints da API, incluindo cenários de sucesso e de erro de validação.

**Como importar:**
1. Abra o Postman → clique em **Import**
2. Selecione o arquivo `postman/restaurant-api.postman_collection.json`
3. Clique em **Import**

**Como usar:**
- Suba a aplicação (`docker compose up -d --build` ou `./mvnw spring-boot:run`)
- Execute os requests **na ordem das pastas** para que as variáveis de ID sejam populadas automaticamente:
  1. User Types → Create
  2. Users → Create
  3. Restaurants → Create
  4. Menu Items → Create
  5. Cleanup (Deletes) — executada por último, propositalmente
- Os requests intermediários (update, get by ID) já usam os IDs salvos automaticamente pelos scripts de teste da collection.
- As exclusões (`DELETE`) ficam isoladas na pasta **Cleanup (Deletes)**, ao final da collection, na ordem inversa das dependências (Menu Item → Restaurant → User → User Type). Isso evita conflitos de chave estrangeira que ocorriam quando um `DELETE` de uma entidade "pai" (ex.: User Type) rodava antes dos `CREATE`s de suas entidades dependentes (ex.: Users).
- Para rodar tudo em sequência: clique nos `...` da collection → **Run collection**.