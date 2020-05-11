FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /personal-accounts-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
COPY /target/*.jar /personal-accounts-service/personal-accounts-service.jar
COPY /docker/kafka.keystore.jks /personal-accounts-service/kafka.keystore.jks
COPY /docker/kafka.truststore.jks /personal-accounts-service/kafka.truststore.jks
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:+UseStringDeduplication", "-Xmx550m", "-jar", "/personal-accounts-service/personal-accounts-service.jar"]