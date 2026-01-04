# Phase 3-1 Handover Document
> JPA 연관 관계 매핑 (User-Post ManyToOne) 완료

## 📋 작업 개요

**작업 기간**: 2026-01-03  
**담당자**: 이환  
**Phase**: 3-1 - JPA Entity 연관 관계 매핑  
**상태**: ✅ 완료

---

## 🎯 작업 목표

1. User 엔티티와 Post 엔티티 간 ManyToOne 연관 관계 구현
2. 기존 `String author` 필드를 `User author`로 변경
3. 연관 관계에 맞춰 DTO, Service, Repository, Controller 전면 수정
4. 모든 테스트 코드 수정 및 통과 (53개)
5. Spring MVC 경로 충돌 문제 해결

---

## ✅ 완료된 작업

### 1. User 엔티티 생성

**파일**: `src/main/java/com/gitfactory/blogapi/entity/User.java`
```java
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
```

**주요 특징**:
- JPA Auditing 사용 (`@CreatedDate`, `@LastModifiedDate`)
- Enum 타입 사용 (`UserRole.USER`, `UserRole.ADMIN`)
- 빌더 패턴으로 생성

---

### 2. Post 엔티티 수정

**변경 사항**:
```java
// BEFORE
@Column(nullable = false)
private String author;

// AFTER
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User author;
```

**주요 포인트**:
- `@ManyToOne`: Post(다) → User(일) 관계
- `FetchType.LAZY`: 지연 로딩 (N+1 문제 방지 준비)
- `@JoinColumn(name = "user_id")`: FK 컬럼명 명시

---

### 3. DTO 수정

#### PostRequest.java
```java
// BEFORE
public record PostRequest(
    String title,
    String content,
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

// AFTER
public record PostRequest(
    String title,
    String content,
    Long userId  // ✨ author → userId
) {
    public Post toEntity(User author) {  // ✨ User 객체 주입
        return Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();
    }
}
```

#### PostResponse.java
```java
// BEFORE
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

// AFTER
public record PostResponse(
    Long id,
    String title,
    String content,
    String authorName,  // ✨ author → authorName
    Long authorId,      // ✨ authorId 추가
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getAuthor().getUsername(),  // ✨ User 객체에서 추출
            post.getAuthor().getId(),         // ✨ User ID 추가
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
```

---

### 4. Repository 수정

#### PostRepository.java

**추가된 메서드**:
```java
// 제목으로 검색
List<Post> findByTitleContaining(String keyword);

// 작성자명으로 검색 (User의 username 사용)
List<Post> findByAuthor_UsernameContaining(String keyword);

// User 객체로 검색
List<Post> findByAuthor(User author);

// User ID로 검색
List<Post> findByAuthorId(Long authorId);
```

**Query Method 네이밍 규칙**:
- `Author_Username`: User 엔티티의 username 필드 참조
- `AuthorId`: User의 id 필드 직접 참조

#### UserRepository.java (신규 생성)
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByRole(UserRole role);
    List<User> findByUsernameContaining(String keyword);
}
```

---

### 5. Service 수정

#### PostService.java

**주요 변경 사항**:
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;  // ✨ 추가

    @Transactional
    public PostResponse createPost(PostRequest request) {
        // ✨ User 조회 추가
        User author = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User not found with id: " + request.userId()));

        // ✨ User 객체를 toEntity()에 주입
        Post post = request.toEntity(author);
        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    public List<PostResponse> getPostsByAuthor(String authorName) {
        // ✨ User의 username으로 검색
        List<Post> posts = postRepository.findByAuthor_UsernameContaining(authorName);
        return posts.stream()
                .map(PostResponse::from)
                .toList();
    }
}
```

**예외 처리**:
- `ResourceNotFoundException`: User를 찾을 수 없을 때 발생
- 404 응답 자동 변환 (`@RestControllerAdvice`)

---

### 6. Controller 수정

#### PostController.java

**경로 변경 및 재배치**:
```java
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    // ✨ 경로 순서 중요! (구체적 → 일반적)
    
    @GetMapping  // /api/posts
    public ResponseEntity<List<PostResponse>> getAllPosts() { ... }

    @GetMapping("/search")  // /api/posts/search
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String keyword) { ... }

    @GetMapping("/search/author")  // ✨ /api/posts/search/author
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(@RequestParam String keyword) { ... }

    @GetMapping("/{id}")  // ✨ 맨 아래 배치!
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) { ... }
}
```

**경로 충돌 해결**:
- **문제**: `/{id}`가 `/search`를 가로챔 → "search"를 Long으로 변환 시도
- **해결**: 구체적인 경로(`/search`, `/search/author`)를 `/{id}`보다 먼저 배치
- **변경**: `/author/{author}` → `/search/author?keyword=` (쿼리 파라미터 사용)

---

### 7. 전체 테스트 수정

#### 수정된 테스트 파일 (6개)

1. **PostServiceTest.java**
    - UserRepository Mock 추가
    - User 객체 생성 및 주입
    - `createPost_UserNotFound()` 테스트 추가

2. **PostRepositoryTest.java**
    - UserRepository 의존성 추가
    - 모든 테스트에서 User 먼저 생성
    - `findByAuthor_UsernameContaining()` 테스트 추가

3. **PostControllerTest.java**
    - PostRequest: `userId` 사용
    - PostResponse: `authorId` 추가
    - 경로 변경: `/author/{author}` → `/search/author?keyword=`

4. **PostControllerRestDocsTest.java**
    - REST Docs 필드 설명 업데이트
    - `pathParameters` → `queryParameters`
    - Request/Response 필드 변경

5. **BlogApiIntegrationTest.java**
    - 실제 DB에 User 생성
    - 모든 Post 생성 시 userId 사용
    - 작성자 검색 테스트 개선

6. **UserRepositoryTest.java** (신규 생성)
    - User CRUD 테스트
    - Query Method 테스트

**테스트 결과**: ✅ 53/53 통과

---

## 📊 DB 스키마 변경

### ERD
```
┌─────────────────┐         ┌─────────────────┐
│     users       │         │     posts       │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────│ id (PK)         │
│ username (UQ)   │    1    │ user_id (FK)    │ N
│ email (UQ)      │         │ title           │
│ password        │         │ content         │
│ role (ENUM)     │         │ created_at      │
│ created_at      │         │ updated_at      │
│ updated_at      │         └─────────────────┘
└─────────────────┘
```

### DDL
```sql
-- users 테이블
CREATE TABLE users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,  -- 'USER' or 'ADMIN'
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

-- posts 테이블
CREATE TABLE posts (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,  -- ✨ author → user_id
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 🔍 주요 학습 포인트

### 1. JPA 연관 관계 매핑

**@ManyToOne vs @OneToMany**:
```java
// Post 입장 (Many)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User author;

// User 입장 (One) - 양방향 시 추가 가능
@OneToMany(mappedBy = "author")
private List<Post> posts = new ArrayList<>();
```

**단방향 vs 양방향**:
- 현재: **단방향** (Post → User만 참조)
- 장점: 단순함, User가 Post를 몰라도 됨
- 양방향 필요 시: User에서 작성한 글 목록 조회할 때

### 2. FetchType.LAZY의 중요성
```java
@ManyToOne(fetch = FetchType.LAZY)  // ✨ 지연 로딩
private User author;
```

**LAZY (지연 로딩)**:
- Post 조회 시 User는 가져오지 않음
- `post.getAuthor().getUsername()` 호출 시 User 조회
- N+1 문제 대비 (나중에 Fetch Join으로 해결)

**EAGER (즉시 로딩)**:
- Post 조회 시 User도 무조건 조회
- N+1 문제 발생 가능
- **사용 지양!**

### 3. Query Method 네이밍 규칙
```java
// User의 username 필드로 검색
List<Post> findByAuthor_UsernameContaining(String keyword);

// 생성되는 JPQL
// SELECT p FROM Post p WHERE p.author.username LIKE %?1%

// 생성되는 SQL (LEFT JOIN 자동)
// SELECT p.* FROM posts p 
// LEFT JOIN users u ON p.user_id = u.id 
// WHERE u.username LIKE %?%
```

**네이밍 규칙**:
- `_`: 연관 엔티티 필드 참조
- `Containing`: LIKE %keyword%
- `IgnoreCase`: 대소문자 무시

### 4. DTO 변환 패턴

**Entity → DTO (from 메서드)**:
```java
public static PostResponse from(Post post) {
    return new PostResponse(
        post.getId(),
        post.getTitle(),
        post.getContent(),
        post.getAuthor().getUsername(),  // ✨ 연관 엔티티 접근
        post.getAuthor().getId(),
        post.getCreatedAt(),
        post.getUpdatedAt()
    );
}
```

**DTO → Entity (toEntity 메서드)**:
```java
public Post toEntity(User author) {  // ✨ 연관 엔티티 주입
    return Post.builder()
        .title(title)
        .content(content)
        .author(author)
        .build();
}
```

### 5. Spring MVC 경로 매칭 우선순위
```java
// ❌ 잘못된 순서
@GetMapping("/{id}")           // 먼저 정의 → 모든 경로 가로챔
@GetMapping("/search")         // 도달 불가!

// ✅ 올바른 순서
@GetMapping("/search")         // 구체적 경로 먼저
@GetMapping("/search/author")
@GetMapping("/{id}")           // PathVariable은 마지막
```

**원칙**: 구체적 → 일반적 순서로 배치

---

## 🐛 트러블슈팅

### 문제 1: 테스트에서 User를 찾을 수 없음

**증상**:
```
ResourceNotFoundException: User not found with id: 1
```

**원인**:
- Service에서 User 조회 로직 추가했으나 테스트에 UserRepository Mock이 없음

**해결**:
```java
@MockBean
private UserRepository userRepository;

@BeforeEach
void setUp() {
    User mockUser = User.builder()
        .id(1L)
        .username("testuser")
        .email("test@example.com")
        .password("password")
        .role(UserRole.USER)
        .build();
    
    given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));
}
```

### 문제 2: Spring MVC 경로 충돌

**증상**:
```
GET /api/posts/search → 500 Error
Failed to convert 'search' to Long
```

**원인**:
- `@GetMapping("/{id}")`가 `/search`를 먼저 매칭
- "search"를 Long id로 변환 시도

**해결**:
1. 경로 순서 재배치 (`/{id}`를 맨 아래)
2. `/author/{author}` → `/search/author?keyword=` 변경

### 문제 3: PostResponse 파라미터 불일치

**증상**:
```java
// 6개 파라미터로 생성
PostResponse response = new PostResponse(...);  // 컴파일 에러
```

**원인**:
- `authorId` 필드 추가로 7개 파라미터 필요

**해결**:
```java
// ✅ 7개 파라미터
PostResponse response = new PostResponse(
    1L,              // id
    "제목",          // title
    "내용",          // content
    "작성자",        // authorName
    1L,              // authorId ✨ 추가
    LocalDateTime.now(),
    LocalDateTime.now()
);
```

---

## 📁 파일 구조
```
blog-api/
├── src/
│   ├── main/
│   │   ├── java/com/gitfactory/blogapi/
│   │   │   ├── controller/
│   │   │   │   └── PostController.java           ✨ 수정
│   │   │   ├── dto/
│   │   │   │   ├── PostRequest.java              ✨ 수정
│   │   │   │   └── PostResponse.java             ✨ 수정
│   │   │   ├── entity/
│   │   │   │   ├── Post.java                     ✨ 수정
│   │   │   │   ├── User.java                     ✨ 신규
│   │   │   │   └── UserRole.java                 ✨ 신규
│   │   │   ├── exception/
│   │   │   │   └── ResourceNotFoundException.java ✨ 신규
│   │   │   ├── repository/
│   │   │   │   ├── PostRepository.java           ✨ 수정
│   │   │   │   └── UserRepository.java           ✨ 신규
│   │   │   └── service/
│   │   │       └── PostService.java              ✨ 수정
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/gitfactory/blogapi/
│           ├── controller/
│           │   ├── PostControllerTest.java       ✨ 수정
│           │   └── PostControllerRestDocsTest.java ✨ 수정
│           ├── integration/
│           │   └── BlogApiIntegrationTest.java   ✨ 수정
│           ├── repository/
│           │   ├── PostRepositoryTest.java       ✨ 수정
│           │   └── UserRepositoryTest.java       ✨ 신규
│           └── service/
│               └── PostServiceTest.java          ✨ 수정
└── docs/
    ├── PHASE3-1_HANDOVER.md                      ✨ 이 문서
    └── JPA_LEARNING.md                           ✨ 작성 예정
```

---

## 🎓 다음 단계 (Phase 3-2)

### 1. Comment 엔티티 추가
- Post와 ManyToOne 관계
- User와 ManyToOne 관계
- 대댓글 (Self-referencing)

### 2. Category 엔티티 추가
- Post와 ManyToMany 관계
- `@ManyToMany` vs 중간 테이블 직접 생성

### 3. N+1 문제 해결
- Fetch Join 사용
- `@EntityGraph` 사용
- Batch Size 설정

### 4. 페이징 & 정렬
- `Pageable` 사용
- `Page<T>` vs `Slice<T>`
- 정렬 조건 동적 처리

---

## 📚 참고 자료

- [JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- [Association Mapping](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#associations)

---

## ✍️ 작성자 노트

**어려웠던 점**:
1. 테스트 코드 전면 수정 - User 의존성 모든 테스트에 추가
2. Spring MVC 경로 충돌 - 디버깅 시간 소요
3. DTO 변환 패턴 - Entity 참조를 어디까지 허용할지 고민

**배운 점**:
1. 연관 관계 매핑 시 테스트 코드 수정 범위가 넓음
2. 경로 설계 시 우선순위 고려 필수
3. DTO에서 연관 엔티티 접근은 간단하게 유지

**개선 사항**:
1. N+1 문제 대비 필요 (Fetch Join)
2. User 생성 로직 중복 → Fixture 패턴 도입 고려
3. REST API 문서 자동화 강화

---

**작성일**: 2026-01-03  
**최종 수정**: 2026-01-03  
**Phase**: 3-1 완료 ✅