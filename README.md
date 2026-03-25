# BIP Teste Integrado

Projeto de avaliação técnica com arquitetura em camadas: banco SQL, módulo EJB, backend Spring Boot e frontend.

## Estrutura

- `backend-module/`: API REST Spring Boot
- `ejb-module/`: regras transacionais EJB (transferência)
- `db/`: scripts de schema e seed
- `frontend/`: placeholder do frontend
- `docs/`: análises, etapas e documentação técnica

## Requisitos

- Java 17
- Maven Wrapper (`./mvnw` já incluso)

## Build e testes

```bash
cd "/media/janaina/Dados/MEUS/GIT/bip-teste-integrado"
./mvnw -q test
```

## Executar backend

```bash
cd "/media/janaina/Dados/MEUS/GIT/bip-teste-integrado"
./mvnw -q -pl backend-module -am spring-boot:run
```

Backend disponível em:

- API: `http://localhost:8080/api/v1`
- H2 Console: `http://localhost:8080/h2-console`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints principais

### Benefícios

- `GET /api/v1/beneficios`
- `GET /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios`
- `PUT /api/v1/beneficios/{id}`
- `DELETE /api/v1/beneficios/{id}`

### Transferências

- `POST /api/v1/transferencias`

Payload exemplo:

```json
{
  "fromId": 1,
  "toId": 2,
  "amount": 50.00
}
```

## Documentação complementar

- `docs/ANALISE_PROJETO.md`
- `docs/ARCHITECTURE.md`
- `docs/ETAPA1_ESTRUTURA_MAVEN.md`
- `docs/ETAPA2_CORRECAO_EJB.md`
- `docs/ETAPA3_CRUD_BACKEND_TRANSFERENCIA.md`
- `docs/ETAPA4_TESTES.md`

