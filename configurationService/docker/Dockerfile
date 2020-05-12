FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /configuration-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
COPY configurationService/target/*.jar /configuration-service/configuration-service.jar
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:+UseStringDeduplication", "-Xmx550m", "-jar", "/configuration-service/configuration-service.jar"]