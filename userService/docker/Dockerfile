FROM openjdk:11 as run
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8889
ENV APP_HOME /user-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /user-service/user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service/user-service.jar"]

FROM openjdk:11 as debug
LABEL maintainer="statkovit@gmail.com"
EXPOSE 8889
ENV APP_HOME /user-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
WORKDIR $APP_HOME
COPY /target/*.jar /user-service/user-service.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5009", "-jar", "/user-service/user-service.jar"]