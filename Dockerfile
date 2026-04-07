FROM amazoncorretto:17-alpine

WORKDIR /app

# 환경 변수 설정 (프로젝트 정보 및 JVM 옵션)
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

# 프로젝트 파일 복사
# Gradle Wrapper 실행 파일과 소스 코드를 모두 복사
COPY gradlew ./
COPY gradle/ gradle/
COPY settings.gradle ./
COPY build.gradle ./
COPY src/ src/

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle Wrapper를 사용하여 애플리케이션 빌드
RUN ./gradlew build -x test

EXPOSE 80

# 빌드된 JAR 파일을 실행하며, 환경변수를 활용하여 파일명을 추론하고 JVM 옵션을 적용
CMD java ${JVM_OPTS} -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar