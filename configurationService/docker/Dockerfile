FROM openjdk:12 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8088
ENV APP_HOME /configuration-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /configuration-service/configuration-service.jar
ENTRYPOINT ["java", "-jar", "/configuration-service/configuration-service.jar"]

FROM openjdk:12 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8088
ENV APP_HOME /configuration-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /configuration-service/configuration-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/configuration-service/configuration-service.jar"]