FROM openjdk:11 as run
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /authorization-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /authorization-service/authorization-service.jar
ENTRYPOINT ["java", "-jar", "/authorization-service/authorization-service.jar"]

FROM openjdk:11 as debug
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /authorization-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /authorization-service/authorization-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008", "-jar", "/authorization-service/authorization-service.jar"]