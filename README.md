# IntArea
###  배포 주소: https://intarea.store
![페이지 메인화면 캡처](intarea_main_capture.png)

## 프로젝트 개요

### ■ 주제
```
인테리어 서비스 제공업자와 고객을 연결하는 사이트
```

### ■ 일반 기능
```
- 스프링 시큐리티
- 헤더(회원가입, 로그인, 로그아웃, 검색, 회원 권한별 리스트 출력)
   * 검색 기능: 포트폴리오 제목 및 내용, 회사명, 솔루션 이름 및 내용
- 메인화면(추천 업체, 추천 상품 포트폴리오, 추천 솔루션 출력)
   * 소형 디스플레이에서도 깨지지 않도록 반응형 화면 적용
- 회사 조회(모든 공개된 회사 정보 열람 가능)
- 포트폴리오 조회(모든 공개된 상품 포트폴리오 정보 및 리뷰 열람 가능)
- 리뷰 신고 기능(각각의 리뷰에 신고버튼 부여)
```
### ■ 사용자 권한별 기능

#### 1. 관리자 권한
- 관리자 페이지
  ``` 
  - 서비스에 대한 거의 모든 데이터 열람 가능(소팅 기능 구현)
  - 일부 정보 수정 가능
  - 신고 목록 출력
  ```
#### 2. 판매자 권한
- 회사 관리 페이지
  ```
  - 회사 정보
  - 상품 포트폴리오 목록(새 작성, 수정, 공개여부 변경)
  - 받은 리뷰 목록
  ```
- 거래 관리 페이지
  ```
  - 받은 견적 요청 목록
  - 작성한 견적서 목록
  - 신규 견적서 작성
  - 거래 취소처리
  ```
#### 3. 일반 사용자 권한
- MyPage(사용자 정보 열람 및 수정 가능)
  ```
  - 사용자가 작성한 신고목록 및 처리상태 출력
  ```
- 견적 요청서 및 견적서
  ```
  - 신규 견적 요청서 작성
  - 받은 견적서 목록
  - 승인된 견적(완료된 거래)에 대한 리뷰 작성
  ```

### ■ 사용 기술 및 서비스

- 언어: Java 17, JavaScript
- 개발도구: IntelliJ, VSCode
- 서버: Node.js, AWS
- UI: React


### ■ 서버 구성: Amazon Web Service
- DB 구성: Amazon EC2 / PostgreSQL
- 이미지 저장: Amazon S3

### ■ 기타 활용 도구
- 진행상황 컨트롤: Notion
- 소통: 슬랙
- ERD 작성: ERDCloud(https://www.erdcloud.com/d/ea6GQFJNKK7qDGauk)
- 화면 다이어그램: https://app.diagrams.net/#G1SYYZpUSokPOUO2KBgryEKKi1QlnlNamW


## 설정

### ■ .env
```Bash
VITE_REACT_DEV_PORT=8083
VITE_API_HOST=http://localhost:8080

SPRING_BOOT_PORT=8080
FRONTEND_DEV_HOST=http://localhost:8083
```
### ■ application.yml
- src/main/resources/application.yml
```Yaml 
spring:
  config:
    import: optional:file:.env[.properties]
  devtools:
    livereload:
      enabled: true
  application:
    name: home
  datasource:
    driver-class-name: org.postgresql.Driver
    url: "Database URL"
    username: "Database username"
    password: "Database password"
  jpa:
    hibernate:
      ddl-auto: update
      naming:
        # jpa camelCase
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    # script 파일이 hibernate 초기화 이후 동작하게 하기 위한 옵션
    defer-datasource-initialization: true

  mvc:
    # WebMvcConfig 에 있는 ** 정규식 패턴 사용하기 위함
    pathmatch:
      matching-strategy: ant_path_matcher
  security:
    oauth2:
      client:
        registration:
          google:
            client-name: google
            client-id: "client-id"
            client-secret: "client-secret"
            redirect-uri: http://localhost:8080/api/login/oauth2/code/google
            authorization-grant-type: authorization_code
            scope:
              - profile
              - email
          naver:
            client-name: naver
            client-id: "client-id"
            client-secret: "client-secret"
            redirect-uri: http://localhost:8080/api/login/oauth2/code/naver
            authorization-grant-type: authorization_code
            scope:
              - name
              - email
          kakao:
            client-name: kakao
            client-id: "client-id"
            client-secret: "client-secret"
            client-authentication-method: client_secret_post
            redirect-uri: http://localhost:8080/api/login/oauth2/code/kakao
            authorization-grant-type: authorization_code
            scope:
              - profile_nickname
              - account_email
        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id

server:
  port: ${SPRING_BOOT_PORT:8080} # .env 파일 없으면 8080포트에서 실행

jwt:
  secret: "Secret"
  token-validity-in-seconds: 1209600 # 14일
  domain: localhost # 쿠키 저장 domain

cloud:
  aws:
    s3:
      bucket: "Bucket name"
    stack.auto: false
    region.static: "Region"
    credentials:
      accessKey: "AccessKey"
      secretKey: "SecretKey"
```
### ■ React modules install

```Bash
# frontend/
npm install --save
```
