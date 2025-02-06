FROM eclipse-temurin:17
WORKDIR /app
COPY ./target/microservice-creator-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-XX:InitialRAMPercentage=50.0", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]

