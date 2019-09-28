FROM dependencies AS builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
COPY src /build/src
RUN mvn clean package -DskipTests

FROM openjdk:12 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8888
ENV APP_HOME /authorization-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar authorization-service.jar
ENTRYPOINT ["java", "-jar", "/authorization-service/authorization-service.jar"]

FROM openjdk:12 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8888
ENV APP_HOME /authorization-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar authorization-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008", "-jar", "/configuration-service/authorization-service.jar"]