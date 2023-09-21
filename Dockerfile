FROM openjdk:11-jdk-slim
ADD /Match-Aligo/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/app.jar"]
