FROM eclipse-temurin:21-jdk

# docker CLI 설치
RUN apt-get update && \
    apt-get install -y docker.io && \
    apt-get clean

VOLUME /tmp

# JAR 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Spring Boot 실행
ENTRYPOINT ["sh", "-c", "java -jar /app.jar --spring.profiles.active=live"]