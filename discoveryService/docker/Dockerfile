FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /discovery-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
COPY /target/*.jar /discovery-service/discovery-service.jar
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:+UseStringDeduplication", "-Xmx550m", "-jar", "/discovery-service/discovery-service.jar"]