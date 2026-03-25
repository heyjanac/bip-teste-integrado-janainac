# Arquitetura do Projeto

## Visão geral

A solução segue arquitetura em camadas com separação clara de responsabilidades:

1. **Controller (HTTP)**: recebe requests e responde JSON
2. **Service (aplicação)**: orquestra casos de uso
3. **Repository (persistência)**: acesso a dados com Spring Data JPA
4. **EJB (domínio transacional)**: regra crítica de transferência com locking
5. **Banco (H2 em memória)**: tabela `BENEFICIO`

## Módulos

### `backend-module`

Responsável pela API REST:

- Controllers:
  - `BeneficioController`
  - `TransferenciaController`
- Services:
  - `BeneficioService`
  - `TransferenciaService` (adapter para EJB)
- Repository:
  - `BeneficioRepository`
- Infra:
  - `GlobalExceptionHandler`
  - `WebConfig` (CORS)
  - `OpenApiConfig` (Swagger/OpenAPI)

### `ejb-module`

Responsável pela regra transacional de transferência:

- `BeneficioEjbService`
- Entity `com.example.ejb.entity.Beneficio`
- Exceptions de negócio:
  - `BeneficioNotFoundException`
  - `SaldoInsuficienteException`
  - `TransferenciaInvalidaException`

## Fluxo de CRUD (benefícios)

1. Cliente chama endpoint `/api/v1/beneficios`
2. `BeneficioController` valida payload e delega para `BeneficioService`
3. `BeneficioService` aplica regra de negócio e chama `BeneficioRepository`
4. `BeneficioRepository` persiste em `BENEFICIO`
5. Response retorna DTO para o cliente

## Fluxo de transferência

1. Cliente chama `POST /api/v1/transferencias`
2. `TransferenciaController` valida payload e chama `TransferenciaService`
3. `TransferenciaService` delega para `BeneficioEjbService.transfer`
4. EJB executa:
   - validação de parâmetros
   - locking pessimista (`PESSIMISTIC_WRITE`) em ordem de ID
   - validação de saldo
   - atualização atômica origem/destino
5. Exceções de negócio são mapeadas no `GlobalExceptionHandler`

## Tratamento de erros

Padrão: `ProblemDetail` (RFC 7807)

- `400`: payload inválido / transferência inválida
- `404`: recurso não encontrado
- `409`: saldo insuficiente
- `500`: erro interno inesperado

## Concorrência e consistência

A operação de transferência usa estratégia dupla para integridade:

- **Pessimistic locking** no `EntityManager.find(..., PESSIMISTIC_WRITE)`
- **Optimistic locking** com coluna `VERSION` (`@Version`)

Isso reduz risco de *lost update* e saldo inconsistente sob concorrência.

## Diagrama textual

```text
HTTP Client
   |
   v
Controller (Spring MVC)
   |
   v
Service (Backend)
   |                 \
   |                  --> Service EJB (transfer)
   v                          |
Repository (Spring Data)      v
   |                      EntityManager/JPA
   \___________  _____________/
               \/
             H2 (BENEFICIO)
```

