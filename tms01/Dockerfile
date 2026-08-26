# Stage 1: Build file JAR từ mã nguồn Java (Maven + JDK 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy file pom.xml và mã nguồn src
COPY pom.xml .
COPY src ./src

# Biên dịch dự án và bỏ qua chạy Unit Test để rút ngắn thời gian build
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng Java nhẹ trên JRE 21 Alpine Linux
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy file .jar đã build ở Stage 1 sang Stage 2
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
