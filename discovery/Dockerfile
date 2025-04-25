FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:21
COPY --from=build /app/target/*.jar app.jar
COPY ../wait-for-it.sh wait-for-it.sh

RUN chmod +x wait-for-it.sh
EXPOSE 8082
ENTRYPOINT ["./wait-for-it.sh", "config_server:8081", "--", "java", "-jar", "app.jar"]