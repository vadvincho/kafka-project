FROM openjdk:11-jdk

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} kafka-consumer-db-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar","/kafka-consumer-db-0.0.1-SNAPSHOT.jar"]