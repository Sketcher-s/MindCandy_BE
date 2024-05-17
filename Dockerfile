FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY .env .env
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]