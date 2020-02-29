FROM openjdk:11 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8890
ENV APP_HOME /personal-accounts-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /personal-accounts-service/personal-accounts-service.jar
ENTRYPOINT ["java", "-jar", "/personal-accounts-service/personal-accounts-service.jar"]

FROM openjdk:11 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8890
ENV APP_HOME /personal-accounts-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /personal-accounts-service/personal-accounts-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5010", "-jar", "/personal-accounts-service/personal-accounts-service.jar"]