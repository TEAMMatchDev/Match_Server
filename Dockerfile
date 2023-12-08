FROM openjdk:11-jdk-slim
RUN apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone
ADD /Match-Batch/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
