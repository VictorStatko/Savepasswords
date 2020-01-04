FROM openjdk:12 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8890
ENV APP_HOME /permissions-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /permissions-service/permissions-service.jar
ENTRYPOINT ["java", "-jar", "/permissions-service/permissions-service.jar"]

FROM openjdk:12 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8890
ENV APP_HOME /permissions-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /permissions-service/permissions-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5010", "-jar", "/permissions-service/permissions-service.jar"]