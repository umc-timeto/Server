# Dockerfile
# - docker build 시 gradle bootJar 수행
# - 실행은 경량 JRE 이미지에서 수행

FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Gradle 캐시 최적화
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# 소스 복사
COPY . .

# 빌드 (배포 시 테스트 스킵)
RUN ./gradlew clean bootJar -x test

# ================= Runtime =================
FROM eclipse-temurin:17-jre
WORKDIR /app

# non-root 권장
RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
