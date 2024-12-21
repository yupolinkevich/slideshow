FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /slideshow
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /slideshow/target/slideshow.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "slideshow.jar"]
