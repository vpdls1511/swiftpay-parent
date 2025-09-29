FROM eclipse-temurin:17-jre
WORKDIR /app

# 방법 1: 목적지를 디렉토리로 지정
COPY build/libs/swiftpay-api-*.jar ./

# 그리고 이름 변경
RUN mv swiftpay-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
