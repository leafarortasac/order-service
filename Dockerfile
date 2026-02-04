FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

WORKDIR /app/shared-contracts
COPY shared-contracts/pom.xml .
COPY shared-contracts/src ./src
RUN mvn clean install -DskipTests

WORKDIR /app/order-service
COPY order-service/pom.xml .
RUN mvn dependency:go-offline

COPY order-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/order-service/target/*.jar app.jar
RUN apk add --no-cache tzdata
ENV TZ=America/Manaus
EXPOSE 8082
ENTRYPOINT ["java", "-Xmx512m", "-Duser.timezone=America/Manaus", "-jar", "app.jar"]