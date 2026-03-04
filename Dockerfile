# Build stage
FROM eclipse-temurin:25.0.1_8-jdk AS build
WORKDIR /app

COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts gradle/libs.versions.toml ./
COPY gradle/ gradle/

COPY api/build.gradle.kts api/
COPY core/build.gradle.kts core/
COPY infra/build.gradle.kts infra/

RUN chmod +x gradlew

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

# Now copy the source code
COPY api/src api/src
COPY core/src core/src
COPY infra/src infra/src

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon bootJar

RUN jdeps --ignore-missing-deps -q \
    --recursive \
    --multi-release 25 \
    --print-module-deps \
    --class-path 'core/build/libs/*:infra/build/libs/*' \
    api/build/libs/hrm-service.jar > deps.txt

RUN jlink \
    --add-modules $(cat deps.txt),java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,java.compiler,jdk.crypto.ec,jdk.unsupported \
    --compress zip-9 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output /custom-jre

# Runtime stage
FROM gcr.io/distroless/base-debian12

WORKDIR /app

COPY --from=build /custom-jre /opt/java/openjdk
COPY --from=build /app/api/build/libs/hrm-service.jar ./app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:+UseZGC -Xms128m -Xmx256m -Duser.timezone=Asia/Ho_Chi_Minh"

ENTRYPOINT ["/opt/java/openjdk/bin/java", "-jar", "app.jar"]
