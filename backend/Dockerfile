FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace/app
COPY backend/pom.xml .
COPY backend/src ./src
COPY frontend ../frontend
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
