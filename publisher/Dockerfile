# Build stage
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app

# Копируем только нужные файлы для сборки
COPY publisher/pom.xml .
COPY publisher/src/ ./src/

# Собираем приложение
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21-alpine

WORKDIR /app

# Копируем собранный JAR
COPY --from=build /app/target/publisher-*.jar /app/app.jar

# Копируем ресурсы если нужно
COPY --from=build /app/src/main/resources/ /app/resources/

EXPOSE 24110
ENTRYPOINT ["java", "-jar", "app.jar"]