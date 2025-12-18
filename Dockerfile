# SonarShowcase Monolith Dockerfile
# Multi-stage build for Spring Boot + React application

# =============================================================================
# Stage 1: Build the frontend
# =============================================================================
FROM node:24-alpine AS frontend-build
WORKDIR /app/frontend

# Copy frontend package files
COPY frontend/package.json frontend/package-lock.json* ./

# Install dependencies
RUN npm ci --silent

# Copy frontend source
COPY frontend/ ./

# Build the frontend
RUN npm run build

# =============================================================================
# Stage 2: Build the backend with frontend included
# =============================================================================
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app

# Copy the parent pom
COPY pom.xml ./

# Copy backend source
COPY backend/pom.xml backend/
COPY backend/src backend/src

# Copy frontend pom (needed for multi-module resolution)
COPY frontend/pom.xml frontend/

# Copy built frontend assets from previous stage
COPY --from=frontend-build /app/frontend/dist frontend/dist

# Build the application (skip frontend build since we already built it)
RUN mvn clean package -DskipTests -pl backend -am \
    -Dfrontend-maven-plugin.skip=true

# =============================================================================
# Stage 3: Runtime
# =============================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Copy the built JAR
COPY --from=backend-build /app/backend/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

