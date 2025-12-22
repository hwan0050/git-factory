# Phase 2-5 Module 1-2 Handover Document

## 📋 작업 개요

**Phase**: 2-5 API Documentation & Integration Testing  
**Module**: 1 (Spring REST Docs) & 2 (Swagger/OpenAPI)  
**작업 기간**: 2025-12-06  
**담당자**: 이환  
**상태**: ✅ 완료

---

## 🎯 달성 목표

### Module 1: Spring REST Docs
- [x] REST Docs 의존성 및 설정 추가
- [x] 7개 API 엔드포인트 테스트 작성
- [x] AsciiDoc 문서 작성
- [x] HTML 문서 자동 생성

### Module 2: Swagger/OpenAPI
- [x] Swagger 의존성 추가
- [x] OpenAPI 설정 (SwaggerConfig)
- [x] Controller 어노테이션 추가
- [x] DTO 스키마 정의
- [x] Swagger UI 실행 확인

---

## 📦 생성/수정된 파일

### 설정 파일

#### 1. build.gradle
**위치**: `blog-api/build.gradle`  
**변경 사항**:
```gradle
plugins {
    id 'org.springframework.boot' version '3.3.5'  // 3.4.12 → 3.3.5
    id 'org.asciidoctor.jvm.convert' version '3.3.2'  // 추가
}

configurations {
    asciidoctorExt  // 추가
}

ext {
    snippetsDir = file('build/generated-snippets')  // 추가
}

dependencies {
    // Swagger 추가
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0'
    
    // REST Docs 추가
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
}

// REST Docs 관련 태스크 추가
tasks.named('test') {
    outputs.dir snippetsDir
}

asciidoctor {
    inputs.dir snippetsDir
    configurations 'asciidoctorExt'
    dependsOn test
    sourceDir = file('src/docs/asciidoc')
    outputDir = file('build/docs/asciidoc')
}

bootJar {
    dependsOn asciidoctor
    from ("${asciidoctor.outputDir}") {
        into 'static/docs'
    }
}
```

**중요 변경**:
- Spring Boot 버전: 3.4.12 → 3.3.5 (호환성 문제 해결)
- springdoc-openapi: 2.6.0 추가
- AsciiDoctor 플러그인 및 태스크 설정

---

### 신규 파일

#### 2. SwaggerConfig.java
**위치**: `src/main/java/com/gitfactory/blogapi/config/SwaggerConfig.java`  
**목적**: Swagger/OpenAPI 설정  
**주요 내용**:
- API 기본 정보 (제목, 설명, 버전)
- 연락처 정보 (Git Factory)
- 라이선스 정보 (MIT)
- 서버 정보 (로컬, 프로덕션)

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Blog API Documentation")
                .description("게시글 관리를 위한 RESTful API")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Git Factory")
                    .url("https://github.com/hwan0050/git-factory"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("로컬 개발 서버"),
                new Server().url("https://api.production.com").description("프로덕션 서버")
            ));
    }
}
```

---

#### 3. PostControllerRestDocsTest.java
**위치**: `src/test/java/com/gitfactory/blogapi/controller/PostControllerRestDocsTest.java`  
**목적**: REST Docs 테스트 및 snippets 생성  
**테스트 메서드**:
1. `전체_게시글_조회_API_문서화()`
2. `게시글_ID로_조회_API_문서화()`
3. `게시글_생성_API_문서화()`
4. `게시글_수정_API_문서화()`
5. `게시글_삭제_API_문서화()`
6. `제목으로_검색_API_문서화()`
7. `작성자로_검색_API_문서화()`

**주요 기술**:
- `@WebMvcTest`: Controller 레이어 테스트
- `@AutoConfigureRestDocs`: REST Docs 자동 설정
- `@ExtendWith(RestDocumentationExtension.class)`: REST Docs 확장
- `@MockBean`: Service 계층 모킹 (Spring Boot 3.3.5)
- `document()`: snippets 생성
- `preprocessRequest/Response(prettyPrint())`: JSON 포맷팅

---

#### 4. index.adoc
**위치**: `src/docs/asciidoc/index.adoc`  
**목적**: REST Docs 메인 문서  
**구조**:
```asciidoc
= Blog API Documentation
:doctype: book
:toc: left
:toclevels: 2

== 개요
=== HTTP 동사
=== HTTP 상태 코드

== 리소스
=== 게시글 API
==== 전체 게시글 조회
==== 게시글 ID로 조회
==== 게시글 생성
==== 게시글 수정
==== 게시글 삭제
==== 제목으로 검색
==== 작성자로 검색

== 예제
=== cURL 명령어 예제
```

**snippets 포함**:
- http-request.adoc
- request-fields.adoc
- http-response.adoc
- response-fields.adoc
- path-parameters.adoc
- query-parameters.adoc

---

### 수정된 파일

#### 5. PostController.java
**변경 사항**: Swagger 어노테이션 추가 (간소화 버전)

```java
@Tag(name = "게시글 API", description = "게시글 CRUD 및 검색 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    // 기존 코드 유지
    // @Tag 어노테이션만 추가
}
```

**최초 계획**:
- @Operation, @ApiResponses, @Parameter 등 상세 어노테이션 추가 계획
- 실제 구현: @Tag만 추가하여 간소화 (복잡한 어노테이션은 버전 호환 이슈로 제외)

---

#### 6. PostRequest.java
**변경 사항**:
- `@Schema` 어노테이션 추가
- `toEntity()` 메서드 추가

```java
public record PostRequest(
    @Schema(description = "게시글 제목", example = "첫 번째 게시글", requiredMode = REQUIRED)
    String title,
    
    @Schema(description = "게시글 내용", example = "게시글 내용입니다.", requiredMode = REQUIRED)
    String content,
    
    @Schema(description = "작성자", example = "홍길동", requiredMode = REQUIRED)
    String author
) {
    public Post toEntity() {
        return Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();
    }
}
```

---

#### 7. PostResponse.java
**변경 사항**:
- `@Schema` 어노테이션 추가
- `from(Post)` 정적 메서드 추가

```java
public record PostResponse(
    @Schema(description = "게시글 ID", example = "1")
    Long id,
    
    @Schema(description = "게시글 제목", example = "첫 번째 게시글")
    String title,
    
    @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
    String content,
    
    @Schema(description = "작성자", example = "홍길동")
    String author,
    
    @Schema(description = "생성일시", example = "2025-12-06T23:00:00")
    LocalDateTime createdAt,
    
    @Schema(description = "수정일시", example = "2025-12-06T23:00:00")
    LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getAuthor(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
```

---

#### 8. PostService.java
**변경 사항**: Record 접근자 사용 & toEntity() 메서드 활용

```java
// 변경 전 (존재하지 않는 메서드 호출)
Post post = Post.builder()
    .title(request.getTitle())     // ❌
    .content(request.getContent()) // ❌
    .author(request.getAuthor())   // ❌
    .build();

// 변경 후 (Record 접근자 & toEntity 사용)
Post post = request.toEntity();    // ✅

post.update(
    request.title(),    // ✅ Record 접근자
    request.content(),  // ✅
    request.author()    // ✅
);
```

---

#### 9. Post.java (Entity)
**변경 사항**: update 메서드 파라미터 추가

```java
// 변경 전
public void update(String title, String content) {
    this.title = title;
    this.content = content;
}

// 변경 후
public void update(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;  // author도 수정 가능
}
```

---

#### 10. PostRepository.java
**변경 사항**: 검색 메서드 추가

```java
public interface PostRepository extends JpaRepository<Post, Long> {
    // 추가된 메서드
    List<Post> findByTitleContainingIgnoreCase(String title);
    List<Post> findByTitleContaining(String title);
    List<Post> findByAuthor(String author);
}
```

---

## 🔧 기술적 해결 과제

### 1. Spring Boot 버전 호환성

**문제**:
```
NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>'
```

**원인**:
- Spring Boot 3.4.12와 springdoc-openapi 2.6.0 호환 문제
- ControllerAdviceBean 생성자 시그니처 변경

**해결**:
- Spring Boot 버전 다운그레이드: 3.4.12 → 3.3.5
- springdoc-openapi 2.6.0과 완벽 호환

**교훈**:
- 최신 버전이 항상 좋은 것은 아님
- 안정적인 버전 조합 선택 중요
- 의존성 호환성 사전 확인 필요

---

### 2. Record 타입 접근자

**문제**:
```java
// ❌ 컴파일 에러
request.getTitle()
post.getCreatedAt()
```

**원인**:
- Record 타입은 getter 메서드 대신 필드명으로 직접 접근
- 기존 Java Bean 패턴과 차이

**해결**:
```java
// ✅ Record 접근자
request.title()
response.createdAt()
```

**교훈**:
- Record 타입의 특성 이해 필요
- DTO로 Record 사용 시 접근 방식 주의

---

### 3. DTO 변환 패턴

**문제**:
- Controller에서 Entity ↔ DTO 변환 로직 반복
- 코드 중복 및 가독성 저하

**해결**:
```java
// PostRequest → Entity
public Post toEntity() {
    return Post.builder()
        .title(title)
        .content(content)
        .author(author)
        .build();
}

// Entity → PostResponse
public static PostResponse from(Post post) {
    return new PostResponse(
        post.getId(),
        post.getTitle(),
        // ...
    );
}
```

**교훈**:
- DTO 내부에 변환 로직 캡슐화
- 정적 팩토리 메서드 활용 (from)
- 인스턴스 메서드 활용 (toEntity)

---

### 4. @MockBean Deprecation

**문제** (Spring Boot 3.4.x):
```java
@MockBean is deprecated
```

**해결**:
- Spring Boot 3.3.5에서는 `@MockBean` 사용 가능
- Spring Boot 3.4.x에서는 `@MockitoBean` 사용

```java
// Spring Boot 3.3.5
@MockBean
private PostService postService;

// Spring Boot 3.4.x (향후)
@MockitoBean
private PostService postService;
```

---

## 📊 테스트 결과

### REST Docs 테스트

```
PostControllerRestDocsTest
├─ 전체_게시글_조회_API_문서화() ✅
├─ 게시글_ID로_조회_API_문서화() ✅
├─ 게시글_생성_API_문서화() ✅
├─ 게시글_수정_API_문서화() ✅
├─ 게시글_삭제_API_문서화() ✅
├─ 제목으로_검색_API_문서화() ✅
└─ 작성자로_검색_API_문서화() ✅

7 tests passed ✅
```

**생성된 Snippets**:
```
build/generated-snippets/
├── posts-get-all/
│   ├── http-request.adoc
│   ├── http-response.adoc
│   └── response-fields.adoc
├── posts-get-by-id/
├── posts-create/
├── posts-update/
├── posts-delete/
├── posts-search/
└── posts-search-by-author/
```

**생성된 문서**:
```
build/docs/asciidoc/index.html ✅
```

---

### Swagger UI 테스트

**접속 확인**:
- ✅ http://localhost:8080/swagger-ui/index.html
- ✅ http://localhost:8080/v3/api-docs

**표시 내용**:
- ✅ API 제목 및 설명
- ✅ 버전 정보 (1.0.0)
- ✅ 연락처 및 라이선스
- ✅ 서버 정보
- ✅ 7개 API 엔드포인트
- ✅ HTTP 메서드별 색상 구분
- ✅ Try it out 기능

---

### 애플리케이션 실행

```
Started BlogApiApplication in 4.492 seconds
Tomcat started on port 8080 (http)
H2 console available at '/h2-console'
```

**확인 항목**:
- ✅ Spring Boot 3.3.5 실행
- ✅ Hibernate 테이블 생성
- ✅ H2 Database 연결
- ✅ JPA Auditing 활성화
- ✅ Swagger UI 로딩
- ✅ REST Docs 문서 접근 가능

---

## 🌐 접속 정보

### 애플리케이션
- **Base URL**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:mem:blogdb`
    - Username: `sa`
    - Password: (empty)

### API 문서
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **REST Docs**: build/docs/asciidoc/index.html (정적 파일)

### API 엔드포인트
- `GET /api/posts` - 전체 게시글 조회
- `GET /api/posts/{id}` - 특정 게시글 조회
- `POST /api/posts` - 게시글 생성
- `PUT /api/posts/{id}` - 게시글 수정
- `DELETE /api/posts/{id}` - 게시글 삭제
- `GET /api/posts/search?keyword=` - 제목으로 검색
- `GET /api/posts/author/{author}` - 작성자로 검색

---

## 📁 프로젝트 구조

```
blog-api/
├── build.gradle ✨
├── src/
│   ├── main/
│   │   ├── java/com/gitfactory/blogapi/
│   │   │   ├── config/
│   │   │   │   ├── JpaAuditingConfig.java
│   │   │   │   └── SwaggerConfig.java ✨
│   │   │   ├── controller/
│   │   │   │   └── PostController.java ✨ (수정)
│   │   │   ├── dto/
│   │   │   │   ├── PostRequest.java ✨ (수정)
│   │   │   │   └── PostResponse.java ✨ (수정)
│   │   │   ├── entity/
│   │   │   │   └── Post.java ✨ (수정)
│   │   │   ├── repository/
│   │   │   │   └── PostRepository.java ✨ (수정)
│   │   │   ├── service/
│   │   │   │   └── PostService.java ✨ (수정)
│   │   │   └── BlogApiApplication.java
│   │   └── resources/
│   │       └── application.yml
│   ├── docs/
│   │   └── asciidoc/
│   │       └── index.adoc ✨ (신규)
│   └── test/
│       └── java/com/gitfactory/blogapi/controller/
│           ├── PostControllerTest.java
│           └── PostControllerRestDocsTest.java ✨ (신규)
└── build/
    ├── generated-snippets/ ✨ (생성)
    │   ├── posts-get-all/
    │   ├── posts-get-by-id/
    │   ├── posts-create/
    │   ├── posts-update/
    │   ├── posts-delete/
    │   ├── posts-search/
    │   └── posts-search-by-author/
    └── docs/
        └── asciidoc/
            └── index.html ✨ (생성)
```

---

## 🚀 빌드 및 실행 방법

### REST Docs 문서 생성

```bash
# 1. 테스트 실행 (snippets 생성)
./gradlew clean test

# 2. AsciiDoc → HTML 변환
./gradlew asciidoctor

# 3. 문서 확인
start build/docs/asciidoc/index.html
```

### 애플리케이션 실행

```bash
# 방법 1: Gradle
./gradlew bootRun

# 방법 2: IntelliJ
BlogApiApplication.java → Run (Ctrl + Shift + F10)

# 방법 3: Gradle 탭
Tasks → application → bootRun
```

### Swagger UI 접속

```bash
# 애플리케이션 실행 후
start http://localhost:8080/swagger-ui/index.html
```

---

## 📚 사용된 기술 스택

### 프레임워크 & 라이브러리
- Spring Boot 3.3.5
- Spring Data JPA
- Spring Web MVC
- Spring REST Docs
- springdoc-openapi 2.6.0

### 테스트
- JUnit 5
- Mockito
- MockMvc
- AssertJ
- Hamcrest

### 문서화
- AsciiDoctor
- Swagger/OpenAPI 3.1

### 데이터베이스
- H2 Database (개발)
- PostgreSQL (프로덕션 준비)

### 빌드 도구
- Gradle 8.x

---

## 🎯 다음 단계 (Module 3)

### 통합 테스트
- [ ] @SpringBootTest 활용
- [ ] 실제 DB 연동 테스트
- [ ] 트랜잭션 테스트
- [ ] 전체 플로우 테스트

### 테스트 커버리지
- [ ] JaCoCo 플러그인 설정
- [ ] 커버리지 리포트 생성
- [ ] 커버리지 목표 설정 (80% 이상)

### Testcontainers (선택)
- [ ] Docker 컨테이너 기반 테스트
- [ ] PostgreSQL 컨테이너 설정
- [ ] 실제 DB 환경과 동일한 테스트

---

## 💡 개선 제안

### 단기 (Module 3)
1. 통합 테스트로 전체 플로우 검증
2. 테스트 커버리지 80% 이상 달성
3. 예외 처리 테스트 추가

### 중기 (Phase 3)
1. 보안 설정 (Spring Security)
2. 인증/인가 기능
3. Swagger 프로덕션 보안 설정

### 장기
1. API 버저닝 전략
2. 성능 테스트
3. 모니터링 및 로깅

---

## 📝 참고 사항

### 버전 호환성 매트릭스

| Spring Boot | springdoc-openapi | 호환 여부 |
|-------------|-------------------|-----------|
| 3.4.x | 2.6.0 | ❌ NoSuchMethodError |
| 3.3.5 | 2.6.0 | ✅ 완벽 호환 |
| 3.2.x | 2.3.0 | ✅ 호환 |

### Record 타입 사용 시 주의사항
- getter 메서드 없음 → 필드명으로 직접 접근
- `request.title()` (O) vs `request.getTitle()` (X)
- Jackson 직렬화/역직렬화 지원

### REST Docs vs Swagger 선택 가이드
- **공식 문서**: REST Docs (정확성 중요)
- **개발/테스트**: Swagger (편의성 중요)
- **추천**: 두 가지 모두 사용

---

## ✅ 완료 체크리스트

### Module 1: Spring REST Docs
- [x] 의존성 및 플러그인 설정
- [x] 테스트 코드 작성 (7개)
- [x] Snippets 생성 확인
- [x] AsciiDoc 문서 작성
- [x] HTML 문서 생성 확인

### Module 2: Swagger/OpenAPI
- [x] 의존성 추가
- [x] SwaggerConfig 작성
- [x] Controller 어노테이션
- [x] DTO 스키마 정의
- [x] Swagger UI 접속 확인

### 버그 수정
- [x] Spring Boot 버전 호환성
- [x] Record 타입 접근자
- [x] PostResponse.from() 추가
- [x] Post.update() 파라미터
- [x] Repository 메서드 추가
- [x] PostService 수정

### 문서화
- [x] 학습 노트 작성
- [x] 핸드오버 문서 작성
- [ ] README.md 업데이트 (다음 단계)

---

**인수인계 완료일**: 2025-12-06  
**작성자**: 이환  
**검토자**: -  
**다음 작업자**: 이환 (Module 3)