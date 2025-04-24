FROM maven:3.8.7-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:21
COPY --from=build /app/target/*.jar app.jar
COPY secrets.properties secrets.properties
EXPOSE 8008
ENTRYPOINT ["java", "-jar", "/app.jar"]