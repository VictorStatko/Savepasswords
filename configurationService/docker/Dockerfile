FROM maven:3.6.2-jdk-12 AS builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src /build/src
RUN mvn clean package -DskipTests

FROM openjdk:12 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8088
ENV APP_HOME /configuration-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar configuration-service.jar
ENTRYPOINT ["java", "-jar", "/configuration-service/configuration-service.jar"]

FROM openjdk:12 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8088
ENV APP_HOME /configuration-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar configuration-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/configuration-service/configuration-service.jar"]