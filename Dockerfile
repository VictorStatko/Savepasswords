FROM maven:3.6.2-jdk-12 AS dependencies

COPY ./authorizationService/pom.xml /pom.xml
RUN mvn dependency:go-offline -B --fail-never
RUN rm /pom.xml

COPY ./configurationService/pom.xml /pom.xml
RUN mvn dependency:go-offline -B --fail-never
RUN rm /pom.xml

COPY ./discoveryService/pom.xml /pom.xml
RUN mvn dependency:go-offline -B --fail-never
RUN rm /pom.xml

COPY ./gatewayService/pom.xml /pom.xml
RUN mvn dependency:go-offline -B --fail-never
RUN rm /pom.xml

COPY ./userService/pom.xml /pom.xml
RUN mvn dependency:go-offline -B --fail-never
RUN rm /pom.xml