# Phase 2-5: API Documentation & Integration Testing 학습 노트

## 📚 학습 개요

**기간**: 2025-12-06  
**목표**: Spring REST Docs와 Swagger/OpenAPI를 통한 API 문서 자동화  
**완료**: Module 1 (Spring REST Docs), Module 2 (Swagger/OpenAPI)  
**상태**: ✅ 완료

---

## 🎯 Module 1: Spring REST Docs

### 핵심 개념

**Spring REST Docs**는 테스트 기반으로 API 문서를 자동 생성하는 도구입니다.

#### 장점
- ✅ **정확성**: 테스트가 통과해야만 문서 생성 → 문서와 코드 불일치 방지
- ✅ **품질**: 테스트 작성을 강제하여 코드 품질 향상
- ✅ **전문성**: 정적 HTML 문서로 깔끔한 프레젠테이션

#### 단점
- ⚠️ **테스트 필요**: 모든 API에 대한 테스트 작성 필수
- ⚠️ **정적 문서**: 실시간 API 테스트 불가
- ⚠️ **학습 곡선**: AsciiDoc 문법 학습 필요

---

### 구현 과정

#### 1단계: 의존성 추가

```gradle
plugins {
    id 'org.asciidoctor.jvm.convert' version '3.3.2'
}

configurations {
    asciidoctorExt
}

ext {
    snippetsDir = file('build/generated-snippets')
}

dependencies {
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
}
```

**핵심 포인트**:
- `asciidoctor` 플러그인으로 AsciiDoc → HTML 변환
- `snippetsDir`에 테스트 결과 저장
- `asciidoctorExt` 설정으로 snippets 참조

---

#### 2단계: 테스트 작성

```java
@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class PostControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void 게시글_생성_API_문서화() throws Exception {
        PostRequest request = new PostRequest("제목", "내용", "작성자");
        given(postService.createPost(any())).willReturn(response);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(document("posts-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용"),
                    fieldWithPath("author").description("작성자")
                ),
                responseFields(
                    fieldWithPath("id").description("게시글 ID"),
                    // ...
                )
            ));
    }
}
```

**핵심 포인트**:
- `@AutoConfigureRestDocs`: REST Docs 자동 설정
- `document()`: snippets 생성
- `preprocessRequest/Response(prettyPrint())`: JSON 포맷팅
- `requestFields`, `responseFields`: 필드 문서화

---

#### 3단계: AsciiDoc 작성

`src/docs/asciidoc/index.adoc`:

```asciidoc
= Blog API Documentation
:doctype: book
:toc: left
:toclevels: 2
:source-highlighter: highlightjs

== 개요

이 API는 블로그 게시글을 관리하기 위한 RESTful API입니다.

=== HTTP 동사

|===
| 동사 | 용도

| `GET`
| 리소스 조회

| `POST`
| 새 리소스 생성

| `PUT`
| 기존 리소스 수정

| `DELETE`
| 리소스 삭제
|===

== 게시글 API

=== 게시글 생성

include::{snippets}/posts-create/http-request.adoc[]
include::{snippets}/posts-create/request-fields.adoc[]
include::{snippets}/posts-create/http-response.adoc[]
include::{snippets}/posts-create/response-fields.adoc[]
```

**핵심 포인트**:
- `{snippets}`: Gradle에서 설정한 snippetsDir 참조
- `include::`: 생성된 snippets 포함
- 목차, 코드 하이라이팅 등 설정

---

#### 4단계: 문서 생성

```bash
# 1. 테스트 실행 (snippets 생성)
./gradlew test

# 2. AsciiDoc → HTML 변환
./gradlew asciidoctor

# 3. 결과 확인
start build/docs/asciidoc/index.html
```

**생성 결과**:
- `build/generated-snippets/`: 7개 API의 snippets
- `build/docs/asciidoc/index.html`: 최종 문서

---

### 트러블슈팅

#### 이슈 1: @MockBean Deprecation

**문제**:
```
@MockBean is deprecated in Spring Boot 3.4.x
```

**해결**:
```java
// Spring Boot 3.3.5에서는 @MockBean 사용 가능
@MockBean
private PostService postService;

// Spring Boot 3.4.x에서는 @MockitoBean 사용
@MockitoBean
private PostService postService;
```

---

## 🎯 Module 2: Swagger/OpenAPI

### 핵심 개념

**Swagger/OpenAPI**는 실시간으로 API를 테스트할 수 있는 인터랙티브 문서 도구입니다.

#### 장점
- ✅ **실시간 테스트**: 브라우저에서 바로 API 호출 가능
- ✅ **직관적 UI**: 개발자 친화적 인터페이스
- ✅ **빠른 구현**: 어노테이션만 추가하면 자동 생성
- ✅ **표준 스펙**: OpenAPI 3.x 표준 준수

#### 단점
- ⚠️ **정확성**: 코드와 문서 불일치 가능
- ⚠️ **어노테이션 관리**: 많은 어노테이션 필요
- ⚠️ **프로덕션 노출**: 보안 설정 필요

---

### 구현 과정

#### 1단계: 의존성 추가

```gradle
dependencies {
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0'
}
```

**버전 호환성**:
- Spring Boot 3.3.x ✅ springdoc-openapi 2.6.0
- Spring Boot 3.4.x ❌ 호환 이슈 발생

---

#### 2단계: Swagger 설정

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
                new Server()
                    .url("http://localhost:8080")
                    .description("로컬 개발 서버"),
                new Server()
                    .url("https://api.production.com")
                    .description("프로덕션 서버")
            ));
    }
}
```

**핵심 포인트**:
- API 기본 정보 설정
- 연락처 및 라이선스
- 서버 정보 (로컬/프로덕션)

---

#### 3단계: Controller 어노테이션

```java
@Tag(name = "게시글 API", description = "게시글 CRUD 및 검색 API")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "생성할 게시글 정보"
        )
        @RequestBody PostRequest request
    ) {
        // ...
    }
}
```

**핵심 포인트**:
- `@Tag`: API 그룹화
- `@Operation`: 메서드 설명
- `@ApiResponses`: 응답 코드별 설명
- `@Parameter`: 파라미터 설명

---

#### 4단계: DTO 스키마 정의

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

**핵심 포인트**:
- `@Schema`: 필드 설명 및 예제
- `requiredMode`: 필수 여부
- `example`: Swagger UI에 표시될 예제 값

---

#### 5단계: 접속 및 테스트

**Swagger UI**:
```
http://localhost:8080/swagger-ui/index.html
```

**OpenAPI JSON**:
```
http://localhost:8080/v3/api-docs
```

**테스트 방법**:
1. API 엔드포인트 선택
2. "Try it out" 클릭
3. Request Body 수정
4. "Execute" 클릭
5. Response 확인

---

### 트러블슈팅

#### 이슈 1: NoSuchMethodError

**문제**:
```
NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>'
```

**원인**:
- Spring Boot 3.4.12 와 springdoc-openapi 2.6.0 버전 호환 문제

**해결**:
```gradle
// Before
id 'org.springframework.boot' version '3.4.12'

// After
id 'org.springframework.boot' version '3.3.5'
```

---

#### 이슈 2: Record 타입 접근자

**문제**:
```java
// ❌ 에러
post.update(
    request.getTitle(),
    request.getContent(),
    request.getAuthor()
);
```

**해결**:
```java
// ✅ Record는 getter 대신 필드명으로 접근
post.update(
    request.title(),
    request.content(),
    request.author()
);
```

---

#### 이슈 3: PostResponse.from() 메서드 누락

**문제**:
```java
// Controller에서 사용
return ResponseEntity.ok(PostResponse.from(post));
// 하지만 from() 메서드가 없음
```

**해결**:
```java
public record PostResponse(
    Long id,
    String title,
    String content,
    String author,
    LocalDateTime createdAt,
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

#### 이슈 4: Post.update() 파라미터 개수

**문제**:
```java
// Service에서 3개 파라미터 전달
post.update(title, content, author);

// 하지만 Entity는 2개만 받음
public void update(String title, String content) {
    this.title = title;
    this.content = content;
}
```

**해결**:
```java
public void update(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;  // author도 수정 가능하도록
}
```

---

#### 이슈 5: Repository 메서드명

**문제**:
```java
// Service에서 호출
postRepository.findByTitleContaining(keyword);

// 하지만 Repository에 메서드 없음
```

**해결**:
```java
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingIgnoreCase(String title);
    List<Post> findByTitleContaining(String title);
    List<Post> findByAuthor(String author);
}
```

---

## 📊 REST Docs vs Swagger 비교

| 특징 | Spring REST Docs | Swagger/OpenAPI |
|------|------------------|-----------------|
| **문서 생성 방식** | 테스트 기반 | 어노테이션 기반 |
| **정확성** | ⭐⭐⭐⭐⭐ (매우 높음) | ⭐⭐⭐ (중간) |
| **UI** | 정적 HTML | 인터랙티브 UI |
| **API 테스트** | ❌ 불가능 | ✅ 실시간 테스트 |
| **구현 난이도** | ⭐⭐⭐⭐ (높음) | ⭐⭐ (낮음) |
| **유지보수** | 테스트와 동기화 | 어노테이션 관리 |
| **프로덕션 노출** | ✅ 안전 | ⚠️ 보안 설정 필요 |
| **문서 품질** | ⭐⭐⭐⭐⭐ (전문적) | ⭐⭐⭐⭐ (실용적) |
| **학습 곡선** | 가파름 | 완만함 |
| **사용 시나리오** | 공식 문서, 외부 공개 | 내부 개발, 테스트 |

---

## 🎯 실무 활용 전략

### 추천: 두 가지 모두 사용!

#### Spring REST Docs
- **목적**: 공식 API 문서
- **대상**: 외부 개발자, 파트너사
- **장점**: 높은 신뢰성, 전문적 외관

#### Swagger/OpenAPI
- **목적**: 개발 중 빠른 테스트
- **대상**: 내부 개발팀
- **장점**: 실시간 테스트, 빠른 피드백

---

## 💡 핵심 학습 내용

### 1. 문서화의 중요성
- API 문서는 코드만큼 중요
- 자동화된 문서는 항상 최신 상태 유지
- 테스트 기반 문서는 신뢰성 보장

### 2. 도구의 특성 이해
- REST Docs: 정확성과 전문성
- Swagger: 편의성과 실용성
- 상황에 맞는 도구 선택

### 3. 버전 호환성
- Spring Boot 버전 확인 필수
- 의존성 버전 조합 테스트
- 안정적인 버전 선택 중요

### 4. Record 타입 활용
- 불변 DTO로 안전성 향상
- 접근자 문법 차이 주의
- toEntity(), from() 패턴 활용

---

## 📁 프로젝트 구조

```
blog-api/
├── build.gradle (REST Docs + Swagger 설정)
├── src/
│   ├── main/
│   │   └── java/com/gitfactory/blogapi/
│   │       ├── config/
│   │       │   ├── JpaAuditingConfig.java
│   │       │   └── SwaggerConfig.java ✨
│   │       ├── controller/
│   │       │   └── PostController.java (Swagger 어노테이션) ✨
│   │       ├── dto/
│   │       │   ├── PostRequest.java (@Schema, toEntity()) ✨
│   │       │   └── PostResponse.java (@Schema, from()) ✨
│   │       ├── entity/
│   │       │   └── Post.java (update 메서드) ✨
│   │       ├── repository/
│   │       │   └── PostRepository.java (검색 메서드) ✨
│   │       └── service/
│   │           └── PostService.java (Record 접근자) ✨
│   ├── docs/
│   │   └── asciidoc/
│   │       └── index.adoc ✨
│   └── test/
│       └── java/com/gitfactory/blogapi/controller/
│           ├── PostControllerTest.java
│           └── PostControllerRestDocsTest.java ✨
└── build/
    ├── generated-snippets/ (7개 API snippets) ✨
    └── docs/asciidoc/index.html ✨
```

---

## 🎓 다음 학습 계획

### Module 3: Integration Testing
- @SpringBootTest를 활용한 통합 테스트
- 실제 DB 연동 테스트
- JaCoCo를 통한 테스트 커버리지 측정
- Testcontainers (선택적)

---

## 📚 참고 자료

### Spring REST Docs
- [Spring REST Docs 공식 문서](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)
- [AsciiDoc 문법](https://docs.asciidoctor.org/asciidoc/latest/)

### Swagger/OpenAPI
- [springdoc-openapi 공식 문서](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)

---

## ✅ 체크리스트

- [x] Spring REST Docs 의존성 추가
- [x] REST Docs 테스트 작성 (7개 API)
- [x] AsciiDoc 문서 작성
- [x] HTML 문서 생성 확인
- [x] Swagger 의존성 추가
- [x] SwaggerConfig 작성
- [x] Controller 어노테이션 추가
- [x] DTO 스키마 정의
- [x] Swagger UI 접속 확인
- [x] 버전 호환성 이슈 해결
- [x] Record 타입 관련 수정
- [x] Repository 메서드 추가
- [x] 모든 컴파일 에러 해결
- [x] 애플리케이션 실행 확인

---

**작성일**: 2025-12-06  
**작성자**: 이환  
**프로젝트**: Git Factory - Spring Boot 학습