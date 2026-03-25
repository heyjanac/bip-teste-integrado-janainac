# ─────────────────────────────────────────────
# Stage 1: Build — Maven + JDK 17
# ─────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /workspace

# Copia os POMs primeiro para aproveitar o cache de dependências
COPY pom.xml ./
COPY ejb-module/pom.xml ejb-module/
COPY backend-module/pom.xml backend-module/

# Baixa dependências (cache layer separado)
RUN mvn -B dependency:go-offline -q

# Copia o código-fonte
COPY ejb-module/src ejb-module/src
COPY backend-module/src backend-module/src

# Builda todos os módulos (ejb primeiro, depois backend)
RUN mvn -B clean package -DskipTests

# ─────────────────────────────────────────────
# Stage 2: Runtime — JRE leve
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Cria usuário não-root (segurança)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia apenas o JAR gerado
COPY --from=builder /workspace/backend-module/target/backend-module-0.0.1-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

