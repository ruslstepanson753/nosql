FROM maven:3.8.4-amazoncorretto-21 as build
WORKDIR app/publisher
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app/publisher
COPY --from=build /app/publisher/target/jira-1.0.jar /app/
COPY ./resources /app/publisher/resources
EXPOSE 8080
CMD ["java", "-jar", "jira-1.0.jar"]