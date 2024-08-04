FROM maven:3.8.2-openjdk-17-slim AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.2-jdk-slim
COPY --from=build /target/*.jar demo.jar
ENV PORT=8080
EXPOSE 80
ENTRYPOINT ["java","-jar","demo.jar"]
