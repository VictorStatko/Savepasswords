FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /email-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
COPY emailService/target/*.jar /email-service/email-service.jar
COPY emailService/docker/kafka.keystore.jks /email-service/kafka.keystore.jks
COPY emailService/docker/kafka.truststore.jks /email-service/kafka.truststore.jks
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:+UseStringDeduplication", "-Xmx550m", "-jar", "/email-service/email-service.jar"]