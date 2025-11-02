#Build jar
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

#Copy pom.xml trước để cache dependency
COPY pom.xml .

#Tải sẵn dependency (offline)
RUN mvn dependency:go-offline -B

#Copy code sau để không làm mất cache dependency
COPY src ./src

#Build project
RUN mvn clean package -DskipTests

#Run app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy file jar từ stage build sang
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8080 cho Spring Boot
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
