# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=0.0.1-SNAPSHOT
ENV EMAIL_HOST_NAME: missing_host_name
ENV EMAIL_USER_NAME: missing_user_name
ENV EMAIL_PASSWORD: missing_password

WORKDIR /app
COPY --from=build /build/target/psk-full-stack-*.jar /app/

EXPOSE 8088

ENV DB_URL=jdbc:postgresql://postgres-sql-psk:5432/psk-full-stack
ENV MAILDEV_URL=localhost

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOST_NAME=${EMAIL_HOST_NAME}
ENV EMAIL_USER_NAME=${EMAIL_USER_NAME}
ENV EMAIL_PASSWORD=${EMAIL_PASSWORD}

CMD java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL}  psk-full-stack-${JAR_VERSION}.jar