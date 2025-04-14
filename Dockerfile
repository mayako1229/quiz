# Stage 1: Build the application using Maven
FROM maven:3.8.6-jdk-17 AS build
WORKDIR /app
# pom.xml とソースをコピー
COPY pom.xml .
COPY src ./src
# テストはスキップしてビルド
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app
# Stage 1 でビルドした JAR ファイルをコピー
COPY --from=build /app/target/quiz-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
