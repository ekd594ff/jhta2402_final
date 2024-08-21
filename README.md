# IntArea

## Requirement

### .env 생성
```Bash
VITE_REACT_DEV_PORT=8083
VITE_API_HOST=http://localhost:8080

SPRING_BOOT_PORT=8080
FRONTEND_DEV_HOST=http://localhost:8083
```
### application.yml
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

  mvc:
    # WebMvcConfig 에 있는 ** 정규식 패턴 사용하기 위함
    pathmatch:
      matching-strategy: ant_path_matcher

server:
  port: ${SPRING_BOOT_PORT:8080} # .env 파일 없으면 8080포트에서 실행

jwt:
  secret: "Secret"
  token-validity-in-seconds: 1209600 # 14일
```
### React modules install

```Bash
# frontend/
npm install --save
```
