FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache tzdata
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENV TZ=America/New_York
CMD ["java", "-Duser.timezone=America/New_York", "-jar", "app.jar"]