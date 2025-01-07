# shopping-category

---
## 구현 범위

---

## 프로젝트 설정
### Version
```
- Spring-Boot 3.3.7 (GA)
- Jdk 21, Kotlin 1.9.2
- Gradle 8.5
```
* 스프링부트 최신인 3.4에 대한 이슈가 있는 듯하여, 3.3의 GA인 3.3.7을 선택
    * Jdk17 ~ Jdk21 선택 가능
    * Jdk17의 안전성이 높으나 Jdk21 선택
* Gradle build version 을 참고하여 8.5 를 선택
    * https://docs.gradle.org/current/userguide/compatibility.html

### 빌드 및 프로젝트 실행 (터미널에서 아래와 같이 실행하면 됩니다.)
```bash 
./gradlew wrapper --gradle-version=8.5 --distribution-type=bin
./gradlew -v 
./gradlew clean --refresh-dependencies
./gradlew clean build
./gradlew bootRun
```

### H2 console
* URL: http://localhost:8080/h2-console
* JDBC-URL: jdbc:h2:mem:musinsa;MODE=MYSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_UPPER=false
* User Name: sa
* Password:

### 스웨거
* http://localhost:8080/swagger-ui.html

---

○ 구현 범위에 대한 설명
○ 코드 빌드, 테스트, 실행 방법
○ 기타 추가 정보