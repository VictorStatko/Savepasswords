FROM dependencies AS builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
COPY src /build/src
RUN mvn clean package -DskipTests

FROM openjdk:12 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8889
ENV APP_HOME /user-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service/user-service.jar"]

FROM openjdk:12 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8889
ENV APP_HOME /user-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar user-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5009", "-jar", "/user-service/user-service.jar"]