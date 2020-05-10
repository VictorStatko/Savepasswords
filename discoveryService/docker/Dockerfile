FROM openjdk:11 as run
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /discovery-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /discovery-service/discovery-service.jar
ENTRYPOINT ["java", "-jar", "/discovery-service/discovery-service.jar"]

FROM openjdk:11 as debug
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /discovery-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /discovery-service/discovery-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006", "-jar", "/discovery-service/discovery-service.jar"]