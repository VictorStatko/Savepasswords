FROM openjdk:11 as run
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /email-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /email-service/email-service.jar
ENTRYPOINT ["java", "-jar", "/email-service/email-service.jar"]

FROM openjdk:11 as debug
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /email-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /email-service/email-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5011", "-jar", "/email-service/email-service.jar"]