# -------- Chạy ứng dụng Spring Boot --------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy file jar đã build sẵn
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8080
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
