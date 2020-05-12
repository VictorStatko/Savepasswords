FROM openjdk:11
LABEL maintainer="statkovit@gmail.com"
ENV APP_HOME /gateway-service
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
COPY gatewayService/target/*.jar /gateway-service/gateway-service.jar
COPY gatewayService/docker/savepasswords.p12 /gatewayService/savepasswords.p12
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:+UseStringDeduplication", "-Xmx550m", "-jar", "/gateway-service/gateway-service.jar"]