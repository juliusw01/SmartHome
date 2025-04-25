FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:21
COPY --from=build /app/target/*.jar app.jar
COPY secrets.properties secrets.properties
COPY ../../wait-for-it.sh wait-for-it.sh

RUN chmod +x wait-for-it.sh
EXPOSE 8008
ENTRYPOINT ["./wait-for-it.sh", "api_gateway:8080", "--", "java", "-jar", "app.jar"]