# Gradle 빌드 이미지 사용
FROM gradle:8.11.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 프로젝트 파일 복사
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Gradle 실행 권한 부여
RUN chmod +x gradlew

# 프로젝트 빌드
RUN ./gradlew build --no-daemon -x test

# 애플리케이션 실행용 이미지 - aws corretto
FROM amazoncorretto:17

# 빌드 결과 복사
COPY --from=build /app/build/libs/RedisLikeTest-0.0.1-SNAPSHOT.jar /app/app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]