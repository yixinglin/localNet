FROM openjdk:8-jdk-alpine
COPY . /app
WORKDIR /app
CMD ["--server.port=8088", "--spring.profiles.active=preprod"]
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "localNetServer-1.0-SNAPSHOT.jar"]