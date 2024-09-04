FROM openjdk:17-jdk-bullseye

RUN apt-get update && \ 
    apt-get install -y nodejs npm && \
    apt-get clean

# 작업 디렉토리 설정
WORKDIR /apps

ARG GIT_BRANCH_NAME
ARG APP_DIR_NAME
ARG GIT_URI

# Clone the Git repository. Here we dynamically specify the repository name using the variable defined earlier.
RUN git clone -b ${GIT_BRANCH_NAME} ${GIT_URI} ${APP_DIR_NAME}

# Changes the working directory to /apps/${REPO_NAME}. This uses the variable to dynamically set the directory path.
WORKDIR /apps/${APP_DIR_NAME}
RUN rm -rf .git

WORKDIR /apps/${APP_DIR_NAME}/frontend
RUN npm install