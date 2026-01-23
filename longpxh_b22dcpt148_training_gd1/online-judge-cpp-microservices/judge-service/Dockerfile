FROM eclipse-temurin:21-jre
LABEL authors="long"
WORKDIR /app
COPY judge-presentation/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
