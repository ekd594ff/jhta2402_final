FROM openjdk:17-jdk-bullseye

# Install curl, Node.js, and Supervisor
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs supervisor && \
    apt-get clean

# 작업 디렉토리 설정
WORKDIR /apps

ARG GIT_BRANCH_NAME
ARG APP_DIR_NAME
ARG GIT_URI

# Clone the Git repository
RUN git clone -b ${GIT_BRANCH_NAME} ${GIT_URI} ${APP_DIR_NAME}

# Change the working directory to /apps/${APP_DIR_NAME}
WORKDIR /apps/${APP_DIR_NAME}
RUN chmod +x gradlew

# Build the Spring Boot application
RUN ./gradlew build

# Install frontend dependencies
WORKDIR /apps/${APP_DIR_NAME}/frontend
RUN npm install

# Supervisor 설정 파일 복사
COPY /apps/${APP_DIR_NAME}/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Supervisor를 사용하여 Spring Boot와 Vite 서버를 동시에 실행
CMD ["/usr/bin/supervisord"]
