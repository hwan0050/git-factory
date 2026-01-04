# JPA 학습 노트
> Spring Data JPA & Hibernate 실습 기록

## 📚 목차

1. [JPA 기본 개념](#1-jpa-기본-개념)
2. [Entity 설계](#2-entity-설계)
3. [연관 관계 매핑](#3-연관-관계-매핑)
4. [Repository 패턴](#4-repository-패턴)
5. [Query Methods](#5-query-methods)
6. [예외 처리](#6-예외-처리)
7. [테스트 전략](#7-테스트-전략)
8. [트러블슈팅](#8-트러블슈팅)

---

## 1. JPA 기본 개념

### JPA란?

**Java Persistence API**
- 자바 ORM 기술 표준
- 객체와 관계형 DB를 매핑
- SQL을 직접 작성하지 않고 객체 중심 개발

### 주요 구성 요소
```
Application
    ↓
Spring Data JPA (Repository 추상화)
    ↓
JPA (표준 인터페이스)
    ↓
Hibernate (구현체)
    ↓
JDBC
    ↓
Database
```

### 영속성 컨텍스트 (Persistence Context)

**1차 캐시**:
```java
// 같은 트랜잭션 내에서
Post post1 = postRepository.findById(1L).get();  // DB 조회
Post post2 = postRepository.findById(1L).get();  // 1차 캐시에서 조회 (DB X)

post1 == post2  // true (같은 객체)
```

**변경 감지 (Dirty Checking)**:
```java
@Transactional
public void updatePost(Long id) {
    Post post = postRepository.findById(id).get();
    post.updateTitle("새 제목");  // setter 호출
    // postRepository.save(post); 불필요!
    // 트랜잭션 종료 시 자동 UPDATE
}
```

---

## 2. Entity 설계

### @Entity 어노테이션
```java
@Entity  // JPA가 관리하는 엔티티임을 선언
@Table(name = "posts")  // 테이블명 지정 (생략 시 클래스명)
public class Post {
    // ...
}
```

### 기본 키 전략

**IDENTITY 전략** (MySQL, PostgreSQL):
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

- DB가 자동으로 ID 생성 (AUTO_INCREMENT)
- INSERT 즉시 실행 (ID를 알아야 영속성 컨텍스트 관리 가능)

**SEQUENCE 전략** (Oracle, PostgreSQL):
```java
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
@SequenceGenerator(name = "post_seq", sequenceName = "post_sequence")
private Long id;
```

### 컬럼 매핑
```java
@Column(
    name = "title",           // 컬럼명 (생략 시 필드명)
    nullable = false,         // NOT NULL 제약
    unique = true,            // UNIQUE 제약
    length = 200,             // VARCHAR 길이
    columnDefinition = "TEXT" // DDL 직접 지정
)
private String title;
```

### Enum 타입 매핑
```java
@Enumerated(EnumType.STRING)  // ✅ 권장! DB에 "USER", "ADMIN" 저장
private UserRole role;

@Enumerated(EnumType.ORDINAL) // ❌ 비권장! DB에 0, 1 저장 (순서 변경 시 위험)
private UserRole role;
```

### JPA Auditing (생성/수정 시간 자동 관리)

**설정 클래스**:
```java
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
```

**Entity에 적용**:
```java
@EntityListeners(AuditingEntityListener.class)
public class Post {
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

---

## 3. 연관 관계 매핑

### 3-1. ManyToOne (다대일)

**Post(다) → User(일)**:
```java
@Entity
public class Post {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
}
```

**주요 옵션**:
- `fetch = FetchType.LAZY`: 지연 로딩 (필수!)
- `@JoinColumn(name = "user_id")`: FK 컬럼명 지정
- `nullable = false`: NOT NULL 제약

### 3-2. FetchType (로딩 전략)

**LAZY (지연 로딩)** ✅ 권장:
```java
@ManyToOne(fetch = FetchType.LAZY)
private User author;

// Post 조회
Post post = postRepository.findById(1L).get();
// SQL: SELECT * FROM posts WHERE id = 1

// User 사용 시점에 조회
String username = post.getAuthor().getUsername();
// SQL: SELECT * FROM users WHERE id = ?
```

**EAGER (즉시 로딩)** ❌ 비권장:
```java
@ManyToOne(fetch = FetchType.EAGER)
private User author;

// Post 조회만 해도 User도 함께 조회
Post post = postRepository.findById(1L).get();
// SQL: SELECT * FROM posts p LEFT JOIN users u ON p.user_id = u.id WHERE p.id = 1
```

**왜 LAZY를 사용해야 하나?**
1. N+1 문제 방지 (Fetch Join으로 해결 가능)
2. 필요할 때만 조회 (성능 최적화)
3. EAGER는 예측 불가능한 쿼리 발생

### 3-3. N+1 문제

**문제 상황**:
```java
List<Post> posts = postRepository.findAll();  // 1번 쿼리

for (Post post : posts) {
    String author = post.getAuthor().getUsername();  // N번 쿼리
}
// 총 N+1번의 쿼리 발생!
```

**해결 방법 1: Fetch Join**:
```java
@Query("SELECT p FROM Post p JOIN FETCH p.author")
List<Post> findAllWithAuthor();

// SQL: SELECT * FROM posts p INNER JOIN users u ON p.user_id = u.id
// 1번의 쿼리로 해결!
```

**해결 방법 2: @EntityGraph**:
```java
@EntityGraph(attributePaths = {"author"})
@Query("SELECT p FROM Post p")
List<Post> findAllWithAuthor();
```

**해결 방법 3: Batch Size**:
```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 100
```

### 3-4. 단방향 vs 양방향

**단방향** (현재 구현):
```java
// Post → User (O)
public class Post {
    @ManyToOne
    private User author;
}

// User → Post (X)
public class User {
    // Post 참조 없음
}
```

**양방향**:
```java
// Post → User
public class Post {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
}

// User → Post
public class User {
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();
}
```

**양방향 주의사항**:
```java
// 연관관계 편의 메서드
public void setAuthor(User author) {
    this.author = author;
    author.getPosts().add(this);  // 양쪽 모두 설정
}
```

**단방향 vs 양방향 선택 기준**:
- 기본은 **단방향**
- User에서 Post 목록을 자주 조회한다면 → 양방향
- 복잡도 증가 고려

---

## 4. Repository 패턴

### JpaRepository 인터페이스
```java
public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 제공 메서드
    // - save(entity)
    // - findById(id)
    // - findAll()
    // - delete(entity)
    // - count()
}
```

**제공되는 메서드**:
```java
// 저장 및 수정
Post save(Post post);

// 조회
Optional<Post> findById(Long id);
List<Post> findAll();
List<Post> findAllById(Iterable<Long> ids);

// 삭제
void delete(Post post);
void deleteById(Long id);
void deleteAll();

// 존재 여부
boolean existsById(Long id);

// 개수
long count();
```

---

## 5. Query Methods

### 5-1. 메서드 이름으로 쿼리 생성

**기본 규칙**:
```java
List<Post> findByTitle(String title);
// SELECT p FROM Post p WHERE p.title = ?1

List<Post> findByTitleContaining(String keyword);
// SELECT p FROM Post p WHERE p.title LIKE %?1%

List<Post> findByTitleStartingWith(String prefix);
// SELECT p FROM Post p WHERE p.title LIKE ?1%
```

### 5-2. 연관 엔티티 필드 참조

**언더스코어(_) 사용**:
```java
List<Post> findByAuthor_Username(String username);
// SELECT p FROM Post p WHERE p.author.username = ?1

List<Post> findByAuthor_UsernameContaining(String keyword);
// SELECT p FROM Post p WHERE p.author.username LIKE %?1%
```

**생성되는 SQL**:
```sql
SELECT p.* 
FROM posts p 
LEFT JOIN users u ON p.user_id = u.id 
WHERE u.username LIKE '%keyword%'
```

### 5-3. 키워드 조합
```java
// AND
List<Post> findByTitleAndContent(String title, String content);
// WHERE title = ? AND content = ?

// OR
List<Post> findByTitleOrContent(String title, String content);
// WHERE title = ? OR content = ?

// Between
List<Post> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
// WHERE created_at BETWEEN ? AND ?

// LessThan, GreaterThan
List<Post> findByIdLessThan(Long id);
// WHERE id < ?

// In
List<Post> findByIdIn(List<Long> ids);
// WHERE id IN (?, ?, ?)

// OrderBy
List<Post> findByTitleContainingOrderByCreatedAtDesc(String keyword);
// WHERE title LIKE %?% ORDER BY created_at DESC
```

### 5-4. @Query 어노테이션

**JPQL 사용**:
```java
@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
List<Post> searchByTitle(@Param("keyword") String keyword);
```

**Native Query**:
```java
@Query(value = "SELECT * FROM posts WHERE title LIKE %:keyword%", nativeQuery = true)
List<Post> searchByTitleNative(@Param("keyword") String keyword);
```

**동적 정렬**:
```java
@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
List<Post> searchByTitle(@Param("keyword") String keyword, Sort sort);

// 사용
List<Post> posts = postRepository.searchByTitle("제목", Sort.by("createdAt").descending());
```

---

## 6. 예외 처리

### ResourceNotFoundException

**커스텀 예외 클래스**:
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**GlobalExceptionHandler**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다.");
    }
}
```

**사용 예시**:
```java
@Service
public class PostService {
    
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Post not found with id: " + id));
        return PostResponse.from(post);
    }
}
```

---

## 7. 테스트 전략

### 7-1. Repository 테스트

**@DataJpaTest**:
```java
@DataJpaTest  // JPA 관련 빈만 로드
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    void 게시글_저장_테스트() {
        // Given
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .author(user)
                .build();

        // When
        Post savedPost = postRepository.save(post);

        // Then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getAuthor().getUsername()).isEqualTo("testuser");
    }
}
```

### 7-2. Service 테스트

**@ExtendWith(MockitoExtension.class)**:
```java
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void 게시글_생성_성공() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(UserRole.USER)
                .build();
        
        PostRequest request = new PostRequest("제목", "내용", 1L);
        
        Post post = request.toEntity(user);
        post.setId(1L);  // Reflection으로 ID 설정

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // When
        PostResponse response = postService.createPost(request);

        // Then
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.authorName()).isEqualTo("testuser");
    }
}
```

### 7-3. Controller 테스트

**@WebMvcTest**:
```java
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void 게시글_조회_성공() throws Exception {
        // Given
        PostResponse response = new PostResponse(
                1L, "제목", "내용", "작성자", 1L,
                LocalDateTime.now(), LocalDateTime.now()
        );
        given(postService.getPostById(1L)).willReturn(response);

        // When & Then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"));
    }
}
```

### 7-4. Integration 테스트

**@SpringBootTest + @Transactional**:
```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional  // 각 테스트 후 롤백
class BlogApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 전체_플로우_테스트() throws Exception {
        // 1. User 생성
        User user = userRepository.save(
                User.builder()
                        .username("testuser")
                        .email("test@example.com")
                        .password("password")
                        .role(UserRole.USER)
                        .build()
        );

        // 2. Post 생성
        PostRequest request = new PostRequest("제목", "내용", user.getId());
        
        String response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long postId = objectMapper.readTree(response).get("id").asLong();

        // 3. Post 조회
        mockMvc.perform(get("/api/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"));
    }
}
```

---

## 8. 트러블슈팅

### 8-1. LazyInitializationException

**문제**:
```java
@Transactional(readOnly = true)
public PostResponse getPostById(Long id) {
    Post post = postRepository.findById(id).get();
    return PostResponse.from(post);  // ✅ 트랜잭션 내에서 변환
}

// ❌ 트랜잭션 밖에서 접근
public String getAuthorName(Long postId) {
    Post post = postRepository.findById(postId).get();
    // 트랜잭션 종료
    return post.getAuthor().getUsername();  // LazyInitializationException!
}
```

**해결책**:
1. 트랜잭션 내에서 필요한 데이터 모두 로드
2. Fetch Join 사용
3. DTO 변환을 서비스 레이어에서 수행

### 8-2. N+1 문제

**문제 확인**:
```yaml
spring:
  jpa:
    show-sql: true  # SQL 로그 출력
    properties:
      hibernate:
        format_sql: true
```

**로그 확인**:
```
Hibernate: select * from posts
Hibernate: select * from users where id=?
Hibernate: select * from users where id=?
Hibernate: select * from users where id=?
...
```

**해결**:
```java
@Query("SELECT p FROM Post p JOIN FETCH p.author")
List<Post> findAllWithAuthor();
```

### 8-3. Detached Entity

**문제**:
```java
Post post = new Post();
post.setId(1L);  // 영속성 컨텍스트에 없는 엔티티
postRepository.save(post);  // 새로운 엔티티로 인식 → INSERT 시도
```

**해결**:
```java
Post post = postRepository.findById(1L).get();  // 영속 상태
post.updateTitle("새 제목");
// save() 불필요 (더티 체킹)
```

### 8-4. MultipleBagFetchException

**문제**:
```java
@Query("SELECT u FROM User u " +
       "JOIN FETCH u.posts " +
       "JOIN FETCH u.comments")
List<User> findAllWithPostsAndComments();
// MultipleBagFetchException!
```

**해결**:
```java
// 방법 1: 하나씩 Fetch
@Query("SELECT DISTINCT u FROM User u JOIN FETCH u.posts")
List<User> findAllWithPosts();

// 방법 2: @EntityGraph
@EntityGraph(attributePaths = {"posts", "comments"})
List<User> findAll();

// 방법 3: Set 사용
@OneToMany
private Set<Post> posts = new HashSet<>();  // List → Set
```

---

## 📚 참고 자료

### 공식 문서
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html)
- [JPA 2.2 Specification](https://download.oracle.com/otn-pub/jcp/persistence-2_2-mrel-spec/JavaPersistence.pdf)

### 추천 도서
- 자바 ORM 표준 JPA 프로그래밍 (김영한)
- 스프링 부트와 AWS로 혼자 구현하는 웹 서비스

### 유용한 링크
- [Baeldung JPA Tutorials](https://www.baeldung.com/jpa-hibernate-tutorials)
- [Vlad Mihalcea's Blog](https://vladmihalcea.com/)

---

## 🎯 학습 체크리스트

- [x] JPA 기본 개념 이해
- [x] Entity 설계 및 매핑
- [x] @ManyToOne 연관 관계
- [x] Repository 인터페이스 활용
- [x] Query Methods 작성
- [x] 예외 처리 전략
- [x] 테스트 코드 작성
- [ ] @OneToMany 양방향 매핑
- [ ] @ManyToMany 관계
- [ ] N+1 문제 해결 (Fetch Join)
- [ ] QueryDSL 활용
- [ ] Specification 패턴
- [ ] 페이징 & 정렬

---

**최초 작성**: 2026-01-03  
**최종 수정**: 2026-01-03  
**작성자**: 이환