FROM openjdk:11-jdk

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} training-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar","/training-0.0.1-SNAPSHOT.jar"]