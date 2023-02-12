FROM openjdk:17-alpine

COPY build/libs/ranker-0.0.1.jar ranker.jar
EXPOSE 8080:8080
ENTRYPOINT ["java", "-jar", "/ranker.jar"]