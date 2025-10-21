FROM openjdk:17-jdk-slim

WORKDIR /app

COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn
RUN chmod +x ./mvnw

COPY src ./src
RUN ./mvnw clean package -DskipTests

EXPOSE 8081

CMD ["java", "-jar", "target/appointment-scheduler-0.0.1-SNAPSHOT.jar"]
