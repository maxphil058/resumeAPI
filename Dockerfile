# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy just pom first to cache dependencies
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Now copy sources and build
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Optional memory tuning
ENV JAVA_OPTS=""

# Respect Render's PORT env; Spring also reads server.port from application.properties
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
