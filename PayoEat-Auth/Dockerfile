FROM openjdk:17-jdk

WORKDIR /app

COPY target/*.jar application.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "application.jar"]