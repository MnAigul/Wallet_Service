FROM openjdk:17

ENV LIQUIBASE_VERSION 3.5.3

RUN apk add --no-cache curl tar \
    && curl -L https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.tar.gz | tar -xz \
    && chmod +x /liquibase/liquibase \
    && ln -s /liquibase/liquibase /usr/local/bin/

WORKDIR /liquibase
#WORKDIR /app
#
#COPY pom.xml .
#
#
#COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/target/Wallet-Service-1.0-SNAPSHOT.jar"]