FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /authorization-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
COPY authorizationService/target/*.jar /authorization-service/authorization-service.jar
COPY authorizationService/docker/kafka.keystore.jks /authorization-service/kafka.keystore.jks
COPY authorizationService/docker/kafka.truststore.jks /authorization-service/kafka.truststore.jks
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:+UseStringDeduplication", "-Xmx550m", "-jar", "/authorization-service/authorization-service.jar"]