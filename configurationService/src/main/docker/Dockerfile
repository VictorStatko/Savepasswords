FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/configurationService-0.0.1.jar
ADD ${JAR_FILE} configuration-service.jar
ENTRYPOINT ["java","-jar","/configuration-service.jar"]