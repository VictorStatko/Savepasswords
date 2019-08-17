FROM maven:3.6.1-jdk-11-slim AS builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src /build/src
RUN mvn clean package -DskipTests

FROM openjdk:11 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8887
ENV APP_HOME /gateway-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar gateway-service.jar
ENTRYPOINT ["java", "-jar", "/gateway-service/gateway-service.jar"]

FROM openjdk:11 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8887
ENV APP_HOME /gateway-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar gateway-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5007", "-jar", "/gateway-service/gateway-service.jar"]