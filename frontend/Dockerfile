# ─────────────────────────────────────────────
# Stage 1: Build — Node 20 + Angular CLI
# ─────────────────────────────────────────────
FROM node:20-alpine AS builder

WORKDIR /app

# Copia manifests de dependências primeiro (cache layer)
COPY package.json package-lock.json* ./

RUN npm ci --prefer-offline

# Copia o restante do código-fonte
COPY . .

# Build de produção
RUN npm run build -- --configuration production

# ─────────────────────────────────────────────
# Stage 2: Runtime — nginx leve
# ─────────────────────────────────────────────
FROM nginx:1.25-alpine

# Remove a config padrão do nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copia a config customizada (SPA + proxy para backend)
COPY nginx.conf /etc/nginx/conf.d/app.conf

# Copia o build Angular gerado
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

