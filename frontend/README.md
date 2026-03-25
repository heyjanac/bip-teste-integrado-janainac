# Frontend Angular - BIP

Frontend Angular standalone para consumo da API do backend (`/api/v1`).

## Requisitos

- Node.js 20+
- npm 10+

## Instalar dependencias

```bash
cd "/media/janaina/Dados/MEUS/GIT/bip-teste-integrado/frontend"
npm install
```

## Rodar em desenvolvimento

```bash
cd "/media/janaina/Dados/MEUS/GIT/bip-teste-integrado/frontend"
npm start
```

Aplicacao local:

- `http://localhost:4200`

## Build

```bash
cd "/media/janaina/Dados/MEUS/GIT/bip-teste-integrado/frontend"
npm run build
```

Output:

- `frontend/dist/frontend`

## Rotas implementadas

- `/beneficios` (listagem)
- `/beneficios/novo` (criacao)
- `/beneficios/:id/editar` (edicao)
- `/transferencias` (transferencia)

## Integracao com backend

Arquivo de ambiente:

- `src/environments/environment.ts`

URL padrao:

- `http://localhost:8080/api/v1`
