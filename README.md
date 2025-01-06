# shopping-category

---
## Project
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

### 빌드 및 프로젝트 실행
```bash
./gradlew wrapper --gradle-version=8.5 --distribution-type=bin
./gradlew -v
./gradlew clean --refresh-dependencies
./gradlew build
./gradlew bootRun
```

### H2 console
* http://localhost:8080/h2-console
* url: jdbc:h2:mem:musinsa;MODE=MYSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
* username: sa
* password:

### 스웨거
* http://localhost:8080/swagger-ui.html

---