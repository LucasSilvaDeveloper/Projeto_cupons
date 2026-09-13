# Coupon API

API REST para gerenciamento de cupons de desconto, desenvolvida com Spring Boot como parte de um desafio técnico. Implementa criação e remoção lógica (soft delete) de cupons, com as regras de negócio encapsuladas em objetos de domínio.

## Sumário

- [Stack utilizada](#stack-utilizada)
- [Regras de negócio](#regras-de-negócio)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como executar](#como-executar)
- [Endpoints](#endpoints)
- [Testes e cobertura](#testes-e-cobertura)
- [Documentação da API (Swagger)](#documentação-da-api-swagger)

## Stack utilizada

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 Database (em memória)
- Bean Validation
- Springdoc OpenAPI (Swagger)
- JUnit 5 + Mockito
- JaCoCo (cobertura de testes)
- Docker e Docker Compose

## Regras de negócio

### Criação de cupom

- Campos obrigatórios: `code`, `description`, `discountValue`, `expirationDate`.
- O `code` é sanitizado automaticamente: caracteres especiais são removidos antes de salvar, e o resultado precisa ter exatamente 6 caracteres alfanuméricos.
- O `discountValue` precisa ser maior ou igual a `0.5`, sem valor máximo.
- O `expirationDate` não pode estar no passado.
- O cupom pode ser criado já publicado (`published: true`); se omitido, assume `false`.

### Remoção de cupom

- É feito soft delete: o cupom não é removido do banco, apenas seu status muda para `DELETED`.
- Não é possível deletar um cupom que já está com status `DELETED`.

Todas essas regras estão encapsuladas na classe `Coupon` (pacote `domain`), que é independente de qualquer mecanismo de persistência — não existe forma de instanciar um `Coupon` em estado inválido.

## Estrutura do projeto

```
br.com.cupons
├── domain              # regras de negocio (Coupon, CouponStatus, excecoes de dominio)
├── entity              # entidade JPA (CouponEntity)
├── mapper              # traducao entre Coupon (dominio) e CouponEntity (persistencia)
├── repository          # interface Spring Data JPA
├── service             # orquestracao entre dominio e persistencia
├── controller          # endpoints REST, contrato de API (Swagger) e DTOs
├── exceptionhandler     # tratamento global de excecoes -> respostas HTTP padronizadas
└── config              # configuracao do OpenAPI/Swagger
```

## Como executar

### Opção 1 — Docker (recomendado)

Na raiz do projeto:

```bash
docker compose up --build
```

A aplicação sobe em `http://localhost:8080`.

### Opção 2 — Maven

Pré-requisito: Java 21 instalado.

```bash
mvn spring-boot:run
```

### Opção 3 — IDE

Executa a classe `CuponsApplication` diretamente.

Em qualquer uma das opções, o banco H2 é criado em memória automaticamente — não é necessário nenhum setup de banco de dados.

## Endpoints

| Método | Rota            | Descrição                          |
|--------|-----------------|-------------------------------------|
| POST   | `/coupon`       | Cria um novo cupom                  |
| GET    | `/coupon/{id}`  | Busca um cupom pelo identificador   |
| DELETE | `/coupon/{id}`  | Remove logicamente um cupom (soft delete) |

### Exemplo de criação

```bash
curl --location 'http://localhost:8080/coupon' \
--header 'Content-Type: application/json' \
--data '{
    "code": "ABC-123",
    "description": "Cupom de boas-vindas",
    "discountValue": 10.0,
    "expirationDate": "2027-12-31T23:59:59.000Z",
    "published": false
}'
```

### Respostas de erro

| Status | Cenário                                              |
|--------|-------------------------------------------------------|
| 400    | Dados de criação inválidos (código, desconto, data ou campo obrigatório ausente) |
| 404    | Cupom não encontrado                                  |
| 409    | Tentativa de deletar um cupom já deletado             |

## Testes e cobertura

O projeto conta com testes em três camadas:

- **Domínio** (`CouponTest`) — testes unitários puros, sem Spring, cobrindo todas as regras de negócio de criação e remoção.
- **Service** (`CouponServiceTest`) — testes com Mockito, validando a orquestração entre domínio, mapper e repository.
- **Controller** (`CouponControllerTest`) — testes de integração com `@SpringBootTest` e `MockMvc`, cobrindo o fluxo HTTP completo (sucesso e cenários de erro).

Para rodar os testes e gerar o relatório de cobertura:

```bash
mvn clean test
```

O relatório fica disponível em `target/site/jacoco/index.html`.

## Documentação da API (Swagger)

Com a aplicação em execução, a documentação interativa está disponível em:

```
http://localhost:8080/swagger-ui.html
```
