# shopping-category

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

### 빌드 및 프로젝트 실행 (문제 발생 시 터미널에서 실행)
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

## 구현 범위에 대한 설명
### 기능
```
구현 1) - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
구현 2) - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
구현 4) - 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API
```
