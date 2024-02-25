FROM openjdk:17

LABEL version=0.1

ARG JAR_PATH=./build/libs/MyFirstWeb-1.0-SNAPSHOT.jar

RUN mkdir -p /app

WORKDIR /app

COPY ${JAR_PATH} /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]