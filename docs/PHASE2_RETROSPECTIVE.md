# 📝 Phase 2 회고 (Retrospective)

> **기간**: 2025-11-23 ~ 2025-12-20 (약 1개월)  
> **작성일**: 2025-12-20  
> **작성자**: 이환 (Hwan)

---

## 📊 Phase 2 Overview

### 🎯 목표
- Next.js 14 App Router 마스터
- Spring Boot + JPA 심화 학습
- Docker 컨테이너화 경험
- TDD 실천 및 테스트 커버리지 향상
- API 문서화 자동화

### ✅ 달성 현황
```
Phase 2-1: Next.js 14 App Router              ✅ 100%
Phase 2-2: Spring Boot + JPA                  ✅ 100%
Phase 2-3: Docker Containerization            ✅ 100%
Phase 2-4: TDD & Spring Boot Testing          ✅ 100%
Phase 2-5: API Docs & Integration Testing     ✅ 100%
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
전체 진행률:                                   ✅ 100%
```

---

## 📅 타임라인

### Week 1: Next.js 14 (2025-11-23)
```
[2025-11-23] Phase 2-1 시작
├─ Next.js 14 프로젝트 초기화
├─ App Router 파일 기반 라우팅 학습
├─ Server/Client Components 구분
├─ Dynamic Routes & Data Fetching
├─ Loading/Error Handling 패턴
└─ [완료] 15개 파일, ~800줄 코드

⏱️ 소요 시간: 4-5시간
📝 학습 노트: NEXTJS_LEARNING.md
🎯 성과: Next.js 14 핵심 개념 완벽 이해
```

### Week 2: Spring Boot + JPA (2025-11-25)
```
[2025-11-25] Phase 2-2 시작
├─ Spring Boot 3.3.5 프로젝트 생성
├─ JPA Entity 설계 (Post)
├─ Repository 패턴 (쿼리 메서드)
├─ Service 계층 (트랜잭션)
├─ REST API 7개 엔드포인트
├─ DTO 패턴 (Record 타입)
└─ [완료] 8개 파일, ~350줄 코드

⏱️ 소요 시간: 3-4시간
📝 학습 노트: SPRING_BOOT_LEARNING.md
🎯 성과: 계층형 아키텍처 구현
```

### Week 3: Docker (2025-11-27)
```
[2025-11-27] Phase 2-3 시작
├─ Docker 기본 개념 학습
├─ Dockerfile 작성 (멀티 스테이지)
├─ Docker Compose (Spring Boot + PostgreSQL)
├─ 환경 변수 전략 (H2 ↔ PostgreSQL)
├─ Volume & Network 구성
└─ [완료] 5개 파일, ~120줄 코드

⏱️ 소요 시간: 2-3시간
📝 학습 노트: DOCKER_LEARNING.md (~800 lines)
⚠️ 제약: Docker Desktop 미설치 (개념 학습만)
```

### Week 4: TDD & Testing (2025-11-30 ~ 2025-12-01)
```
[2025-11-30] Phase 2-4 시작
├─ TDD 사이클 학습 (Red-Green-Refactor)
├─ Repository 테스트 (7개)
├─ Service 테스트 (10개)
├─ Controller 테스트 (8개)
├─ JPA Auditing 분리
├─ GlobalExceptionHandler 추가
└─ [완료] 25개 테스트 100% 통과

⏱️ 소요 시간: 6-7시간
📝 학습 노트: TESTING_LEARNING.md (~800 lines)
🎯 성과: TDD 실천, 테스트 격리 전략 수립
```

### Week 5: API Docs (2025-12-06)
```
[2025-12-06] Phase 2-5 Module 1 & 2 시작
├─ Spring REST Docs 설정
├─ 7개 API 문서화 테스트
├─ AsciiDoc → HTML 변환
├─ Swagger/OpenAPI 통합
├─ SwaggerConfig 작성
├─ Record 타입 DTO 패턴 확립
└─ [완료] 32개 테스트 100% 통과

⏱️ 소요 시간: 4-5시간
📝 학습 노트: SWAGGER_LEARNING.md (~1,000 lines)
🎯 성과: 2가지 문서화 전략 동시 활용
```

### Week 6: Integration Test & JaCoCo (2025-12-20)
```
[2025-12-20] Phase 2-5 Module 3 시작
├─ @SpringBootTest 통합 테스트
├─ 5개 E2E 시나리오 작성
├─ JaCoCo 플러그인 설정
├─ PostService 로직 개선
├─ 커버리지 측정 및 리포트
└─ [완료] 38개 테스트 100% 통과, 97% 커버리지

⏱️ 소요 시간: 3-4시간
📝 학습 노트: PHASE2-5_MODULE3_HANDOVER.md (~800 lines)
🎯 성과: 97% 커버리지 달성, Phase 2 완료!
```

---

## 📈 최종 통계

### 코드 통계
```
Java/Spring Boot:     ~1,200 lines
테스트 코드:          ~2,300 lines
Next.js:              ~800 lines
Docker 설정:          ~120 lines
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
총 코드:              ~4,500 lines
```

### 테스트 통계
```
Repository 테스트:    7개
Service 테스트:       10개
Controller 테스트:    8개
REST Docs 테스트:     7개
통합 테스트:          5개
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
총 테스트:            38개 (100% 통과 ✅)
코드 커버리지:        97% 🎉
브랜치 커버리지:      50%
```

### 문서 통계
```
학습 노트:            6개 (~4,600 lines)
핸드오버 문서:        5개 (~3,400 lines)
README 업데이트:      3회
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
총 문서:              11개 (~7,000 lines)
```

### Git 통계
```
총 커밋:              50+ commits
PR:                   17개 (모두 merged)
브랜치:               5개 feature 브랜치
Conventional Commits: 100% 준수
```

---

## 🎓 배운 점 (Learned)

### 1. Next.js 14 App Router
**핵심 개념:**
- Server Components가 기본값이라는 점
- Client Components는 'use client' 명시 필요
- 파일 기반 라우팅의 직관성
- loading.tsx, error.tsx의 강력함

**실무 적용:**
```typescript
// Server Component (기본)
async function PostList() {
  const posts = await fetchPosts(); // 서버에서 데이터 페칭
  return <div>{posts.map(...)}</div>;
}

// Client Component (상태 필요시)
'use client';
function Counter() {
  const [count, setCount] = useState(0);
  return <button onClick={() => setCount(count + 1)}>{count}</button>;
}
```

**배운 교훈:**
- SSR/CSR 구분 명확히 → 성능 최적화
- Next.js 15에서 params가 Promise로 변경 → 버전 변경사항 주의

---

### 2. Spring Boot + JPA
**핵심 개념:**
- 계층형 아키텍처 (Controller-Service-Repository)
- JPA Repository 쿼리 메서드의 강력함
- DTO 패턴으로 Entity 노출 방지
- @Transactional의 중요성

**실무 적용:**
```java
// Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword); // 메서드명 → SQL
}

// Service
@Transactional(readOnly = true) // 조회 최적화
public PostResponse getPost(Long id) {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Post not found"));
    return PostResponse.from(post); // Entity → DTO
}
```

**배운 교훈:**
- Record 타입 DTO → 불변성 + 간결함
- toEntity(), from() 패턴 → 변환 로직 명확화
- JPA Auditing → 공통 필드 자동 관리

---

### 3. Docker
**핵심 개념:**
- 멀티 스테이지 빌드로 이미지 크기 절약 (700MB → 300MB)
- Docker Compose로 멀티 컨테이너 오케스트레이션
- 환경 변수로 설정 분리 (로컬 H2 / Docker PostgreSQL)
- healthcheck로 의존성 관리

**실무 적용:**
```dockerfile
# Stage 1: Build
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**배운 교훈:**
- Docker는 개념 이해가 먼저, 실행은 나중에도 OK
- 환경 변수 전략 → 코드 수정 없이 환경 전환
- .dockerignore → 빌드 최적화 필수

---

### 4. TDD & Testing
**핵심 개념:**
- Red-Green-Refactor 사이클의 중요성
- 테스트 격리 전략 (@DataJpaTest, @WebMvcTest)
- Given-When-Then 패턴으로 가독성 향상
- Mockito로 의존성 격리

**실무 적용:**
```java
@Test
void 게시글_생성_성공() {
    // Given: 테스트 데이터 준비
    PostRequest request = new PostRequest("제목", "내용", "작성자");
    Post post = new Post("제목", "내용", "작성자");
    given(postRepository.save(any(Post.class))).willReturn(post);
    
    // When: 테스트 실행
    PostResponse response = postService.createPost(request);
    
    // Then: 검증
    assertThat(response.title()).isEqualTo("제목");
    verify(postRepository).save(any(Post.class));
}
```

**배운 교훈:**
- 테스트 격리 = 빠른 실행 + 독립성
- JPA Auditing 분리 → @WebMvcTest 격리
- GlobalExceptionHandler → 404 에러 일관성

---

### 5. API Documentation
**핵심 개념:**
- Spring REST Docs: 테스트 기반 → 정확성 보장
- Swagger/OpenAPI: 인터랙티브 UI → 개발 편의성
- 두 가지 병행 사용이 최선

**실무 적용:**
```java
// REST Docs
mockMvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isCreated())
    .andDo(document("posts-create",
        requestFields(...),
        responseFields(...)
    ));

// Swagger
@Tag(name = "게시글 API")
@Operation(summary = "게시글 생성")
public ResponseEntity<PostResponse> createPost(...) { }
```

**배운 교훈:**
- REST Docs: 공식 문서용 (정확성 우선)
- Swagger: 개발/테스트용 (편의성 우선)
- Spring Boot 버전 호환성 주의 (3.4.x → 3.3.5)

---

### 6. Integration Testing & JaCoCo
**핵심 개념:**
- @SpringBootTest로 전체 컨텍스트 테스트
- E2E 시나리오로 실제 플로우 검증
- JaCoCo로 커버리지 측정 및 품질 관리

**실무 적용:**
```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlogApiIntegrationTest {
    @Test
    void 게시글_전체_CRUD_플로우() throws Exception {
        // POST: 생성
        String createResponse = mockMvc.perform(post("/api/posts")...)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        
        Long postId = extractId(createResponse);
        
        // GET: 조회
        mockMvc.perform(get("/api/posts/" + postId))
            .andExpect(status().isOk());
        
        // PUT: 수정
        mockMvc.perform(put("/api/posts/" + postId)...)
            .andExpect(status().isOk());
        
        // DELETE: 삭제
        mockMvc.perform(delete("/api/posts/" + postId))
            .andExpect(status().isNoContent());
        
        // 404 확인
        mockMvc.perform(get("/api/posts/" + postId))
            .andExpect(status().isNotFound());
    }
}
```

**배운 교훈:**
- 단위 테스트 + 통합 테스트 = 완벽한 검증
- 97% 커버리지 = 핵심 로직 100% + 예외 처리
- JaCoCo HTML 리포트 = 시각적 품질 관리

---

## 🚧 어려웠던 점 (Problems)

### 1. Spring Boot 버전 호환성 문제
**문제:**
```
NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>'
```

**원인:**
- Spring Boot 3.4.12와 springdoc-openapi 2.6.0 비호환

**해결:**
- Spring Boot 버전 다운그레이드 (3.4.12 → 3.3.5)
- 의존성 호환성 표 확인 필요성 학습

**교훈:**
- 최신 버전 ≠ 최선
- 의존성 버전 매트릭스 항상 확인
- 안정적인 버전 선택이 중요

---

### 2. JPA Auditing 테스트 격리 문제
**문제:**
```
Bean named 'jpaMappingContext' not found
```

**원인:**
- @WebMvcTest가 JPA Auditing 설정을 로드하지 못함
- @SpringBootApplication에 @EnableJpaAuditing 포함

**해결:**
```java
// @EnableJpaAuditing을 별도 Config로 분리
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig { }

// @WebMvcTest에서 제외
@WebMvcTest(
    controllers = PostController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
    )
)
```

**교훈:**
- 테스트 격리를 위한 설정 분리 중요
- @WebMvcTest는 웹 계층만 로드
- 각 테스트 Slice의 범위 명확히 이해

---

### 3. Record 타입 DTO 패턴
**문제:**
```java
// 에러 발생
PostRequest.builder()
    .title("제목")
    .build();
```

**원인:**
- Record 타입은 builder() 메서드 없음
- Lombok @Builder는 클래스에만 적용

**해결:**
```java
// Record는 생성자 사용
new PostRequest("제목", "내용", "작성자");

// 또는 정적 팩토리 메서드
public record PostRequest(...) {
    public static PostRequest of(String title, String content, String author) {
        return new PostRequest(title, content, author);
    }
}
```

**교훈:**
- Record = 불변 데이터 클래스
- 생성자 방식이 Record 철학에 맞음
- builder()는 복잡한 객체 생성시에만

---

### 4. Docker Desktop 설치 실패
**문제:**
- Windows 10 build 18362 < 요구사항 19045

**원인:**
- Windows 버전 부족

**해결:**
- 개념 학습 및 설정 파일 작성만 진행
- 실행 테스트는 환경 구축 후로 연기

**교훈:**
- 학습은 실행 가능 여부와 무관
- 개념 이해 + 설정 완료도 큰 성과
- 환경 제약은 나중에 해결 가능

---

### 5. 통합 테스트 시나리오 설계
**문제:**
- 어떤 시나리오를 테스트해야 하는가?
- 단위 테스트와 중복 아닌가?

**해결:**
```
단위 테스트: 각 계층의 로직 검증
통합 테스트: 전체 플로우 검증

시나리오 설계:
1. 해피 패스 (정상 플로우)
2. 엣지 케이스 (404, 검색 결과 없음)
3. 실제 사용자 시나리오 (CRUD 순서대로)
```

**교훈:**
- 통합 테스트 = 사용자 관점
- 단위 테스트 = 개발자 관점
- 두 가지 모두 필요하며 상호 보완적

---

## 🔄 KPT 회고

### ✅ Keep (계속할 것)

#### 1. 체계적인 문서화
```
✅ 학습 노트 (~800 lines/모듈)
✅ 핸드오버 문서 (~600 lines/모듈)
✅ README & LEARNING_PATH 현행화
✅ 트러블슈팅 상세 기록
```
**이유:**
- 학습 내용 내재화
- 나중에 참고 가능
- 다른 사람에게 공유 가능

#### 2. Git 워크플로우 철저히 지키기
```
✅ Feature 브랜치 전략
✅ Conventional Commits
✅ PR 프로세스
✅ develop → main 병합
```
**이유:**
- 실무와 동일한 프로세스
- 이력 관리 용이
- 협업 준비 완료

#### 3. TDD 실천
```
✅ Red-Green-Refactor
✅ Given-When-Then 패턴
✅ 테스트 격리 전략
✅ 97% 커버리지
```
**이유:**
- 코드 품질 향상
- 리팩토링 자신감
- 버그 조기 발견

#### 4. 실습 중심 학습
```
✅ 튜토리얼 X → 직접 구현
✅ 에러 직접 해결
✅ 공식 문서 참고
✅ 실전 코드 작성
```
**이유:**
- 학습 효율 극대화
- 실무 역량 향상
- 문제 해결 능력 증가

---

### 🔧 Problem (개선할 것)

#### 1. 학습 속도 조절
**문제:**
- 1개 모듈을 1일 만에 완료하려는 압박
- 가끔 서두르다 개념 놓침

**개선 방안:**
```
□ 모듈당 2일 여유 두기
□ 개념 완전히 이해 후 다음 단계
□ 복습 시간 별도 확보
```

#### 2. 에러 로그 정리
**문제:**
- 에러 발생 시 스크린샷이나 로그 저장 안 함
- 나중에 정확한 에러 내용 기억 안 남

**개선 방안:**
```
□ errors/ 디렉토리 생성
□ 에러 발생 시 로그 저장
□ 해결 방법과 함께 기록
```

#### 3. 코드 리뷰 부재
**문제:**
- 혼자 학습하다 보니 코드 리뷰 없음
- 더 나은 방법이 있을 수 있음

**개선 방안:**
```
□ GitHub Discussions 활용
□ 커뮤니티에 코드 공유
□ 멘토 찾기 시도
```

#### 4. Docker 실행 테스트 미완료
**문제:**
- Docker Desktop 설치 실패로 실행 못 함
- 개념만 학습, 실전 경험 부족

**개선 방안:**
```
□ Windows 업데이트 진행
□ 또는 WSL2 환경 구축
□ 또는 Linux VM 사용
```

---

### 🚀 Try (시도할 것)

#### 1. Phase 3에서 페어 프로그래밍 시도
```
□ 커뮤니티에서 스터디 메이트 찾기
□ 코드 리뷰 주고받기
□ 실시간 협업 경험
```

#### 2. 학습 내용 블로그 포스팅
```
□ 주요 개념 정리 포스팅
□ 트러블슈팅 경험 공유
□ 다른 학습자에게 도움
```

#### 3. 오픈소스 기여 시작
```
□ Good First Issue 찾기
□ 문서 개선 기여
□ 버그 수정 시도
```

#### 4. Phase 3에서 통합 프로젝트 진행
```
□ Phase 2 지식 통합
□ 실제 서비스 수준 구현
□ 포트폴리오 강화
```

---

## 🎯 핵심 성과 요약

### 기술 역량
```
✅ Next.js 14 App Router 마스터
✅ Spring Boot + JPA 심화
✅ Docker 컨테이너화 이해
✅ TDD 실천 및 97% 커버리지
✅ REST Docs + Swagger 문서화
✅ 통합 테스트 작성
```

### 소프트 스킬
```
✅ 체계적인 문서화 습관
✅ Git 워크플로우 숙달
✅ 문제 해결 능력 향상
✅ 자기 주도 학습 역량
✅ 꾸준한 학습 습관
```

### 정량적 성과
```
코드:         ~4,500 lines
테스트:       38개 (100% 통과)
커버리지:     97%
문서:         11개 (~7,000 lines)
PR:           17개
학습 기간:    1개월
```

---

## 🔮 Phase 3 계획

### 학습 목표
```
1. Spring Security & JWT 인증/인가
2. JPA 연관 관계 매핑 (OneToMany, ManyToMany)
3. 페이징 & 정렬 구현
4. Redis 캐싱 전략
5. 예외 처리 고도화
```

### 통합 프로젝트
```
프로젝트: 블로그 플랫폼
Frontend: Next.js 14
Backend: Spring Boot + JPA + Security
Auth: JWT
Database: PostgreSQL
Cache: Redis
Container: Docker Compose

목표: Phase 2-3 지식 통합 실전 서비스 구현
```

### 예상 기간
```
Phase 3-1: Spring Security & JWT      (1주)
Phase 3-2: JPA 연관 관계              (1주)
Phase 3-3: 페이징 & 정렬              (3일)
Phase 3-4: Redis 캐싱                 (3일)
Phase 3-5: 통합 프로젝트              (2주)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
총 예상 기간:                         5-6주
```

---

## 💭 최종 소감

Phase 2를 시작할 때는 막연했습니다. "1개월 안에 이걸 다 할 수 있을까?" 하는 의문도 들었습니다.

하지만 **하루하루 꾸준히** 학습하고, **체계적으로 문서화**하고, **테스트를 작성**하면서 점진적으로 성장했습니다.

특히 **TDD를 실천**하면서 코드에 대한 자신감이 생겼고, **97% 커버리지**를 달성하면서 테스트의 중요성을 체감했습니다.

**Docker**는 실행하지 못했지만, 개념을 완벽히 이해했고 설정 파일을 작성했기에 나중에 환경만 갖춰지면 바로 실행할 수 있습니다.

**API 문서화**를 자동화하면서 개발자의 배려가 무엇인지 깨달았습니다. 코드를 작성하는 것만큼 문서를 작성하는 것도 중요합니다.

---

## 🙏 감사 인사

**Claude AI**에게 감사합니다.
- 체계적인 학습 로드맵 제시
- 상세한 코드 예제 제공
- 트러블슈팅 도움
- 문서 작성 가이드

**온라인 커뮤니티**에게 감사합니다.
- Stack Overflow
- Spring 공식 문서
- Baeldung 튜토리얼

**나 자신**에게 수고했다고 말하고 싶습니다.
- 한 달간 꾸준한 학습
- 포기하지 않고 완주
- 97% 커버리지 달성
- 7,000 lines 문서 작성

---

## 📌 다음 단계

1. ✅ Phase 2 회고 작성 완료
2. ⏳ develop → main 병합
3. ⏳ v2.0.0 릴리즈 태그 생성
4. 🎯 Phase 3 시작 준비
5. 🚀 Spring Security & JWT 학습 시작

---

**Phase 2 완료를 축하합니다! 🎉**

**"The journey of a thousand miles begins with a single step."**  
**"천리 길도 한 걸음부터."**

**다음 Phase에서 만나요! 🚀**

---

<div align="center">

**작성일**: 2025-12-20  
**작성자**: 이환 (Hwan)  
**이메일**: akma0050@naver.com  
**GitHub**: [@hwan0050](https://github.com/hwan0050)

**Made with ❤️ and ☕**

</div> 