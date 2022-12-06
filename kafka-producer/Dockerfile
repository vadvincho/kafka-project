FROM openjdk:11-jdk

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} kafka-producer-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar","/kafka-producer-0.0.1-SNAPSHOT.jar"]

EXPOSE 8083