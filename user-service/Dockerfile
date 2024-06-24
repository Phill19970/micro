FROM openjdk:17-alpine

ARG JAR_APP=target/*.jar

WORKDIR /app

COPY ${JAR_APP} /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]