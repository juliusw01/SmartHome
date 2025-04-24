FROM openjdk:21
COPY /target/*.jar app.jar
COPY secrets.properties secrets.properties
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]