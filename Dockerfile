FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /slideshow
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /slideshow

COPY --from=build /slideshow/target/slideshow-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
