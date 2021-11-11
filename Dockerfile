FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} onboardservices.jar
ENTRYPOINT ["java","-jar","/onboardservices.jar"]