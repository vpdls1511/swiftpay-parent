# === Runtime Stage ===
FROM eclipse-temurin:17-jre-slim
WORKDIR /app

COPY build/libs/swiftpay-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
