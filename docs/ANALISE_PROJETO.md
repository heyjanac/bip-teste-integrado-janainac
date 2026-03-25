# Análise do Projeto: BIP - Teste Integrado

**Data**: 2026-03-24
**Repositório**: https://github.com/rafa-cipri-puti/bip-teste-integrado.git
**Branch**: main
**Commit base**: `16f08de` (Initial commit, 19/12/2025)

---

## 1. Visão Geral

Este é um **teste técnico de avaliação de candidato**. O objetivo é implementar uma solução completa em camadas (DB → EJB → Backend → Frontend) corrigindo um bug crítico em um serviço EJB e entregando aplicação funcional com testes e documentação.

---

## 2. Stack Técnica Identificada

| Camada | Tecnologia | Versão | Status |
|--------|-----------|--------|--------|
| **Backend** | Spring Boot | 3.2.5 | Template mínimo |
| **JDK** | Java | 17 | Definido |
| **Persistência** | Spring Data JPA + H2 | latest | Dependência adicionada |
| **EJB** | Jakarta EJB 4.0 (@Stateless) | 4.0+ | Com bug documentado |
| **Frontend** | Angular | (a definir) | Vazio (placeholder) |
| **Banco** | H2 (testes) + schema SQL | - | Schema definido |
| **Build** | Maven | 3.8+ | Configurado |
| **CI/CD** | GitHub Actions | - | Parcialmente configurado |

---

## 3. Estrutura Atual do Projeto

```
bip-teste-integrado/
├── CLAUDE.md                           # Instruções do Claude Code
├── .github/workflows/ci.yml           # Pipeline CI (build Maven)
├── backend-module/
│   └── src/main/java/com/example/backend/
│       ├── pom.xml                     # ⚠️ Local não convencional
│       ├── BackendApplication.java     # Entry point Spring Boot
│       └── BeneficioController.java    # Controller mock (1 endpoint)
├── ejb-module/
│   └── src/main/java/com/example/ejb/
│       └── BeneficioEjbService.java    # ⚠️ Com 5 bugs críticos
├── frontend/
│   └── README.md                       # Placeholder (vazio)
├── db/
│   ├── schema.sql                      # Schema tabela BENEFICIO
│   └── seed.sql                        # 2 registros de exemplo
└── docs/
    └── README.md                       # Objetivo e critérios
```

---

## 4. Arquivos Existentes — Análise Detalhada

### 4.1 `db/schema.sql`

```sql
CREATE TABLE BENEFICIO (
  ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  NOME VARCHAR(100) NOT NULL,
  DESCRICAO VARCHAR(255),
  VALOR DECIMAL(15,2) NOT NULL,
  ATIVO BOOLEAN DEFAULT TRUE,
  VERSION BIGINT DEFAULT 0
);
```

**Análise**:
- Coluna `VERSION` presente — preparada para optimistic locking
- ID com auto-increment
- `VALOR` como DECIMAL(15,2) — correto para valores monetários
- Sem constraints de CHECK (ex: `VALOR >= 0`)
- Sem índices adicionais

### 4.2 `db/seed.sql`

```sql
INSERT INTO BENEFICIO (NOME, DESCRICAO, VALOR, ATIVO) VALUES
('Beneficio A', 'Descrição A', 1000.00, TRUE),
('Beneficio B', 'Descrição B', 500.00, TRUE);
```

### 4.3 `BackendApplication.java`

Entry point Spring Boot padrão — sem customização.

### 4.4 `BeneficioController.java`

Apenas 1 endpoint GET retornando lista hardcoded (`"Beneficio A"`, `"Beneficio B"`). Falta CRUD completo.

### 4.5 `BeneficioEjbService.java` — COM BUG

```java
@Stateless
public class BeneficioEjbService {
    @PersistenceContext
    private EntityManager em;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Beneficio from = em.find(Beneficio.class, fromId);
        Beneficio to   = em.find(Beneficio.class, toId);

        // BUG: sem validações, sem locking, pode gerar saldo negativo e lost update
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
```

**5 bugs identificados**:

| # | Bug | Impacto | Correção esperada |
|---|-----|---------|-------------------|
| 1 | Sem validação de saldo | Permite saldo negativo | `if (from.getValor().compareTo(amount) < 0) throw` |
| 2 | Sem locking | Race condition / lost update sob concorrência | `LockModeType.PESSIMISTIC_WRITE` |
| 3 | Sem validação de nulls | NullPointerException se IDs não existem | `if (from == null) throw BeneficioNotFoundException` |
| 4 | Sem transação explícita | Estado inconsistente possível | `@TransactionAttribute(REQUIRED)` |
| 5 | Sem rollback em caso de erro | Falha parcial sem recuperação | Exceptions + container managed rollback |

**Nota**: A classe `Beneficio` (entity) referenciada **não existe** — precisa ser criada.

### 4.6 `.github/workflows/ci.yml`

Pipeline CI básico:
- Java 17 (Temurin)
- `mvn -B -f backend-module clean package`
- **Falta**: build do EJB, testes explícitos, build do frontend

### 4.7 `pom.xml` do Backend

- Spring Boot 3.2.5 parent
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `h2`, `spring-boot-starter-test`
- **Problema**: está em `backend-module/src/main/java/com/example/backend/pom.xml` (local não convencional)

---

## 5. Completude do Projeto

### O que existe (40%)

- Estrutura de pastas definida
- Schema BD com coluna VERSION para optimistic locking
- Seed data (2 registros)
- Entry point Spring Boot
- Controller mock (1 endpoint)
- EJB Service com bug documentado
- Pipeline CI básico
- Documentação de objetivo e critérios

### O que falta (60%)

**Backend:**
- Entity JPA `Beneficio`
- Repository `BeneficioRepository`
- Service layer com lógica de negócio
- CRUD completo no Controller (POST, PUT, DELETE, GET/:id)
- Endpoint de transferência
- DTOs e validações
- Exception handler global
- Config CORS
- `application.properties`

**EJB:**
- `pom.xml` do módulo
- Entity `Beneficio` (JPA)
- Correção dos 5 bugs
- Exceptions customizadas

**Frontend:**
- Projeto Angular inteiro

**Testes:**
- Unitários (Service, Controller)
- Integração (Repository + H2)
- Concorrência (transferência simultânea)

**Documentação:**
- Swagger/OpenAPI
- README da raiz
- Documentação de arquitetura

---

## 6. Critérios de Avaliação

| Critério | Peso | Status Atual |
|----------|------|-------------|
| Arquitetura em camadas | 20% | Parcial (só estrutura de pastas) |
| Correção do bug EJB | 20% | Não feito |
| CRUD + Transferência | 15% | Mock apenas |
| Testes | 15% | 0% cobertura |
| Qualidade de código | 10% | Mínimo |
| Documentação | 10% | Parcial (README.md) |
| Frontend | 10% | Vazio |

---
