# 멀티스테이지 빌드: gradle 빌드 → 슬림 런타임
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew installDist --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/install/mcp-system-monitor-server /app
ENTRYPOINT ["/app/bin/mcp-system-monitor-server"]
