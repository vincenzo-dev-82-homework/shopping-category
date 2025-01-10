# 쇼핑 카테고리 API
쇼핑 카테고리 및 상품 관련 기능을 제공하는 REST API입니다.  
DDD(Domain-Driven Design) 구조를 기반으로 설계되었으며, 카테고리와 상품, 브랜드 데이터를 효율적으로 관리하고 최저/최고 가격 조회 등의 기능을 지원합니다.

---

## ⚙️ 프로젝트 설정
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

### 소스 코드 클론
```bash 
git clone https://github.com/vincenzo-dev-82-homework/shopping-category.git
cd shopping-category
```

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
### 고민거리
~~~
문제가 카테고리 기준의 데이터가 우선이었지만 브랜드와 상품에 대한 메타를 기반하고 있어야 할 것 같았습니다.
브랜드와 상품도 각각의 도메인으로 분할 될 수도 있지만, 당장 구현해야할 정보의 메타와 양을 기준으로 판단했을 때 굳이 분리하지 않아도 될 것 같았습니다. 
이렇게 브랜드와 상품을 product 라는 도메인으로 묶고 category의 기준에서 product와 연관이 있는 데이터 조회를 고민해 보았습니다.

그리고 예제와 같은 프로젝트는 어찌 보면 전통적인 layered-architechure로 구현해도 되겠지만,
이후의 확장성을 고려한 DDD 구조를 선택하기로 했고, 헥사고날 형태의 구조는 바로 구현하기에 오버엔지니어링이 아닐까 생각했습니다.  
~~~

### 프로젝트 구조
~~~
src/
├── main/
│   └── kotlin/
│       └── com.musinsa/
│           ├── category/              # 카테고리 관련 도메인 및 로직
│           │   ├── api/               # - REST API Controller 및 DTO
│           │   ├── application/       # - 비즈니스 로직 (Service Layer)
│           │   ├── domain/            # - 도메인 엔터티 및 Repository 인터페이스
│           │   └── infrastructure/    # - JPA Repository 구현체
│           ├── product/               # 상품 관련 도메인 및 로직
│           │   ├── api/               # - REST API Controller 및 DTO
│           │   ├── application/       # - 비즈니스 로직 (Service Layer)
│           │   ├── domain/            # - 도메인 엔터티 및 Repository 인터페이스
│           │   └── infrastructure/    # - JPA Repository 구현체
│           └── common/                # 공통 기능 (예외 처리, Auditing 등)
└── resources/
└── application.yml                # 설정 파일
~~~

### 구조에 대한 개괄적 설명
1. 계층적 분리
~~~
api, application, domain, infrastructure로 계층을 분리하여 명확한 역할을 부여.
REST API, 비즈니스 로직, 도메인 모델, 데이터 액세스르 독립적으로 구현했습니다.
~~~
2. 도메인 중심 설계
~~~
도메인 엔터티(Category, Product, Brand 등)가 각 도메인의 핵심 역할을 담당.
비즈니스 규칙 및 데이터 조작이 Service와 Repository로 나뉘어져 있어 도메인 중심의 설계를 따르도록 구현했습니다.
~~~
3. 유연성
~~~
CategoryType과 같은 Enum, CategoryResources의 DTO 클래스는 변경에 유연하게 대처할 수 있도록 설계.
인터페이스(CategoryRepository, CategoryProductRepository)를 통해 구현체와의 결합도를 낮췄습니다.
~~~
4. 확장성
~~~
새로운 기능 추가 시 계층별로 명확히 책임을 나누어 확장성 향상
새로운 도메인이나 서비스 추가 시에도 기존 구조와 충돌 없이 작업 가능하도록 구현했습니다.
~~~


---
## 📘 구현 API 목록
### 1. 카테고리 별 최저가격 브랜드와 상품 가격, 총액 조회 API
- **설명**  
  카테고리 별로 최저가 상품과 해당 브랜드를 조회하며, 모든 카테고리의 상품 총액도 반환합니다.
- **요청**  
  HTTP Method: `GET`  
  URL: `/v1/musinsa/categories/lowest-price`  
  Request Body: 없음
- **응답**
  ```json
  [
    {
      "categories": [
        {
          "categoryName": "상의",
          "brandName": "C",
          "price": 10000
        },
        {
          "categoryName": "아우터",
          "brandName": "E",
          "price": 5000
        }
      ],
      "totalPrice": 15000
    }
  ]

---

### 2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저 가격 조회 API
- **설명**  
  단일 브랜드로 모든 카테고리 상품을 구매할 경우, 최저가격을 제공하는 브랜드와 각 카테고리의 상품 가격, 총액을 조회합니다.
- **요청**  
  HTTP Method: `GET`  
  URL: `/v1/musinsa/brands/lowest-price`  
  Request Body: 없음

- **응답**
  ```json
  {
    "최저가": {
      "브랜드": "D",
      "카테고리": [
        {
          "카테고리": "상의",
          "가격": 10100
        },
        {
          "카테고리": "아우터",
          "가격": 5100
        },
        {
          "카테고리": "바지",
          "가격": 3000
        },
        {
          "카테고리": "스니커즈",
          "가격": 9500
        },
        {
          "카테고리": "가방",
          "가격": 2500
        },
        {
          "카테고리": "모자",
          "가격": 1500
        },
        {
          "카테고리": "양말",
          "가격": 2400
        },
        {
          "카테고리": "액세서리",
          "가격": 2000
        }
      ],
      "총액": 36100
    }
  }

---

### 3. 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회 API

- **설명**  
  특정 카테고리의 최저가와 최고가 브랜드 및 상품 가격을 조회합니다.

- **요청**  
  HTTP Method: `GET`  
  URL: `/v1/musinsa/categories/{categoryName}/prices`  
  Path Parameter:
  - `categoryName` (필수): 조회할 카테고리 이름 (예: "상의")

- **응답**
  ```json
  {
    "카테고리": "상의",
    "최저가": [
      {
        "브랜드": "C",
        "가격": 10000
      }
    ],
    "최고가": [
      {
        "브랜드": "I",
        "가격": 11400
      }
    ]
  }

--- 

### 4. 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API
* 4-1. 브랜드 생성 API
  * 요청
    * HTTP Method: `POST`  
    * URL: `/v1/musinsa/brands`
* 4-2. 브랜드 수정 API
  * **요청**
    * HTTP Method: `PUT`
    * URL: `/v1/musinsa/brands/{brandId}`
* 4-3. 브랜드 삭제 API
  * 요청
    * HTTP Method: `DELETE`
    * URL: `/v1/musinsa/brands/{brandId}`
* 4-4. 단일 브랜드 조회 API
  * 요청
    * HTTP Method: `GET`
    * URL: `/v1/musinsa/brands/{brandId}`
* 4-5. 전체 브랜드 조회 API
  * 요청
    * HTTP Method: `GET`
    * URL: `/v1/musinsa/brands/`

* 4-6. 상품 생성 API
  * 요청
    * HTTP Method: `POST`
    * URL: `/v1/musinsa/brands`
* 4-7. 상품 수정 API
  * **요청**
    * HTTP Method: `PUT`
    * URL: `/v1/musinsa/brands/{brandId}`
* 4-8. 상품 삭제 API
  * 요청
    * HTTP Method: `DELETE`
    * URL: `/v1/musinsa/brands/{brandId}`
* 4-9. 단일 상품 조회 API
  * 요청
    * HTTP Method: `GET`
    * URL: `/v1/musinsa/brands/{brandId}`
* 4-10. 전체 상품 조회 API
  * 요청
    * HTTP Method: `GET`
    * URL: `/v1/musinsa/brands/`
