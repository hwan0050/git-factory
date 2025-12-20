# Phase 2-5 Module 3: 통합 테스트 및 JaCoCo 커버리지

> 작성일: 2025-12-20  
> 작성자: 이환  
> 프로젝트: Git Factory - Blog API

---

## 📚 학습 목표

### 1. 통합 테스트 이해 및 작성
- `@SpringBootTest`를 활용한 전체 컨텍스트 통합 테스트
- 실제 HTTP 요청/응답 시뮬레이션
- End-to-End 테스트 시나리오 작성

### 2. JaCoCo 테스트 커버리지 측정
- JaCoCo 플러그인 설정
- 커버리지 리포트 생성
- 코드 품질 측정 및 분석

### 3. 목표 달성 결과
- ✅ 통합 테스트 5개 작성 완료
- ✅ 전체 테스트 38개 100% 통과
- ✅ 테스트 커버리지 97% 달성 (목표 80% 초과)

---

## 🔍 통합 테스트란?

### 단위 테스트 vs 통합 테스트

| 구분 | 단위 테스트 | 통합 테스트 |
|------|------------|------------|
| **범위** | 개별 메서드/클래스 | 전체 시스템 |
| **의존성** | Mock 객체 사용 | 실제 Bean 사용 |
| **속도** | 빠름 | 느림 |
| **목적** | 로직 검증 | 통합 동작 검증 |
| **어노테이션** | `@WebMvcTest`, `@DataJpaTest` | `@SpringBootTest` |

### 통합 테스트의 장점
- 실제 환경과 유사한 테스트
- 컴포넌트 간 상호작용 검증
- 전체 플로우 검증 가능

### 통합 테스트의 단점
- 실행 시간이 오래 걸림
- 설정이 복잡함
- 실패 원인 파악이 어려울 수 있음

---

## 🛠️ 통합 테스트 작성

### 1. 테스트 클래스 구조

```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("BlogApi 통합 테스트")
class BlogApiIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
}
```

**주요 어노테이션:**
- `@SpringBootTest`: 전체 Spring 컨텍스트 로드
- `@AutoConfigureMockMvc`: MockMvc 자동 설정
- `@Transactional`: 각 테스트 후 자동 롤백

### 2. 전체 CRUD 플로우 테스트

```java
@Test
@DisplayName("통합 테스트: 게시글 전체 CRUD 플로우")
void 게시글_전체_CRUD_플로우_테스트() throws Exception {
    // 1. 게시글 생성 (POST)
    PostRequest createRequest = new PostRequest(
        "통합 테스트 제목",
        "통합 테스트 내용입니다.",
        "테스터"
    );

    String response = mockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("통합 테스트 제목"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long postId = objectMapper.readTree(response).get("id").asLong();

    // 2. 생성된 게시글 조회 (GET)
    mockMvc.perform(get("/api/posts/{id}", postId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("통합 테스트 제목"));

    // 3. 게시글 수정 (PUT)
    PostRequest updateRequest = new PostRequest(
        "수정된 제목",
        "수정된 내용입니다.",
        "수정자"
    );

    mockMvc.perform(put("/api/posts/{id}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("수정된 제목"));

    // 4. 게시글 삭제 (DELETE)
    mockMvc.perform(delete("/api/posts/{id}", postId))
        .andExpect(status().isNoContent());

    // 5. 삭제 확인 (GET - 404 예상)
    mockMvc.perform(get("/api/posts/{id}", postId))
        .andExpect(status().isNotFound());
}
```

**테스트 시나리오:**
1. POST로 게시글 생성
2. 생성된 ID로 조회
3. PUT으로 내용 수정
4. DELETE로 삭제
5. 삭제 후 404 확인

### 3. 검색 기능 테스트

```java
@Test
@DisplayName("통합 테스트: 제목으로 게시글 검색")
void 제목으로_게시글_검색() throws Exception {
    // Given - 여러 게시글 생성
    PostRequest post1 = new PostRequest("Spring Boot 학습", "내용1", "작성자1");
    PostRequest post2 = new PostRequest("JPA 학습", "내용2", "작성자2");
    PostRequest post3 = new PostRequest("Spring Security", "내용3", "작성자3");

    mockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(post1)))
        .andExpect(status().isCreated());

    // ... post2, post3도 생성

    // When & Then - "Spring" 키워드로 검색
    mockMvc.perform(get("/api/posts/search")
            .param("keyword", "Spring"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title").value("Spring Boot 학습"))
        .andExpect(jsonPath("$[1].title").value("Spring Security"));
}
```

### 4. 작성한 통합 테스트 목록

| 테스트명 | 검증 내용 |
|---------|----------|
| 게시글_전체_CRUD_플로우_테스트 | POST→GET→PUT→DELETE 전체 플로우 |
| 여러_게시글_생성_후_전체_조회 | 여러 게시글 생성 후 목록 조회 |
| 제목으로_게시글_검색 | 키워드 검색 기능 |
| 작성자로_게시글_검색 | 작성자별 검색 기능 |
| 존재하지_않는_게시글_조회_404 | 예외 처리 검증 |

---

## 📊 JaCoCo 설정 및 사용

### 1. build.gradle 설정

```gradle
plugins {
    id 'jacoco'  // JaCoCo 플러그인 추가
}

jacoco {
    toolVersion = "0.8.11"  // 최신 버전
}

jacocoTestReport {
    dependsOn test
    
    reports {
        xml.required = true   // CI/CD용
        html.required = true  // 사람이 보기 좋음
        csv.required = false
    }
    
    // 제외할 클래스 설정 (선택사항)
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: [
                '**/BlogApiApplication.class',
                '**/config/**',
                '**/dto/**',
            ])
        }))
    }
}

tasks.named('test') {
    finalizedBy jacocoTestReport  // 테스트 후 자동 리포트 생성
}
```

### 2. JaCoCo 실행 방법

#### IntelliJ에서:
```
1. 우측 Gradle 탭 클릭
2. blog-api → Tasks → verification → test 더블클릭

또는

1. 우측 Gradle 탭
2. blog-api → Tasks → verification → jacocoTestReport 더블클릭
```

#### Terminal에서:
```bash
./gradlew clean test
# 또는
./gradlew clean test jacocoTestReport
```

### 3. 리포트 확인

**리포트 위치:**
```
build/reports/jacoco/test/html/index.html
```

**IntelliJ에서 열기:**
```
1. 좌측 프로젝트 창
2. build/reports/jacoco/test/html/index.html 우클릭
3. Open in → Browser
```

---

## 📈 커버리지 분석 결과

### 전체 통계

```
총 커버리지:      97% (204/210 instructions)
브랜치 커버리지:  50% (2/4 branches)
테스트 개수:      38개
테스트 성공률:    100%
```

### 패키지별 커버리지

| 패키지 | 라인 커버리지 | 평가 |
|--------|--------------|------|
| exception | 73% | 🟢 양호 |
| service | 100% | 🟢 완벽 |
| controller | 100% | 🟢 완벽 |
| entity | 100% | 🟢 완벽 |
| **전체** | **97%** | **🟢 우수** |

### 목표 달성도

```
✅ 목표: 80% 이상
✅ 실제: 97%
✅ 초과 달성: +17%p
```

### 평가

**우수한 점:**
1. 핵심 비즈니스 로직 100% 커버
2. Service, Controller, Entity 완벽 테스트
3. 업계 표준(70-80%)을 크게 상회

**개선 가능 영역:**
1. Exception 패키지 커버리지 향상 (73% → 90%+)
2. 브랜치 커버리지 개선 (50% → 80%+)
3. Edge case 테스트 추가

---

## 🐛 트러블슈팅

### 1. Record 타입 DTO builder() 에러

**문제:**
```java
// ❌ Record에는 builder() 없음
PostRequest.builder()
    .title("제목")
    .build();
```

**해결:**
```java
// ✅ Record 생성자 사용
new PostRequest("제목", "내용", "작성자");
```

### 2. Post Entity getter 메서드 에러

**문제:**
```java
// ❌ Post는 Record가 아님
post.title()
```

**해결:**
```java
// ✅ 일반 클래스 getter
post.getTitle()
```

### 3. PostService update() save() 누락

**문제:**
```java
// ❌ update 후 save() 없음
post.update(title, content, author);
return PostResponse.from(post);
```

**해결:**
```java
// ✅ save() 명시적 호출
post.update(title, content, author);
Post updatedPost = postRepository.save(post);
return PostResponse.from(updatedPost);
```

### 4. 검색 API 파라미터 이름 불일치

**문제:**
```java
// ❌ 테스트: title
.param("title", "Spring")

// API: keyword 기대
public List<PostResponse> searchPosts(@RequestParam String keyword)
```

**해결:**
```java
// ✅ 파라미터 이름 일치
.param("keyword", "Spring")
```

### 5. MockBean import 버전 문제

**문제:**
```java
// ❌ Spring Boot 3.4.x 전용
import org.springframework.test.context.bean.override.mockito.MockitoBean;
```

**해결:**
```java
// ✅ Spring Boot 3.3.5 호환
import org.springframework.boot.test.mock.mockito.MockBean;
```

---

## 📚 학습 정리

### 1. 통합 테스트 핵심 개념

**@SpringBootTest 특징:**
- 전체 Spring 컨텍스트 로드
- 실제 Bean 사용 (Mock 아님)
- 실제 환경과 가장 유사
- 실행 시간이 오래 걸림

**MockMvc 활용:**
- HTTP 요청/응답 시뮬레이션
- 실제 서버 구동 없이 테스트
- 컨트롤러부터 전체 레이어 테스트

**@Transactional:**
- 각 테스트 메서드 후 자동 롤백
- 테스트 간 데이터 격리
- 데이터베이스 상태 초기화

### 2. JaCoCo 커버리지 측정

**커버리지 종류:**
- **Instruction Coverage**: 바이트코드 명령어 실행 비율
- **Branch Coverage**: 조건문 분기 실행 비율
- **Line Coverage**: 코드 라인 실행 비율
- **Method Coverage**: 메서드 실행 비율
- **Class Coverage**: 클래스 실행 비율

**좋은 커버리지 기준:**
- 70-80%: 일반적인 목표
- 80-90%: 우수
- 90%+: 매우 우수
- 100%: 불필요 (비용 대비 효과 낮음)

### 3. 테스트 전략

**테스트 피라미드:**
```
        /\
       /  \  E2E Tests (적음)
      /────\
     /      \ Integration Tests (중간)
    /────────\
   /          \ Unit Tests (많음)
  /────────────\
```

**우리 프로젝트:**
- 단위 테스트: 30개 (79%)
- 통합 테스트: 5개 (13%)
- REST Docs: 7개 (18%)
- 기타: 1개

### 4. 실무 적용 포인트

**테스트 작성 시:**
1. 핵심 비즈니스 로직에 집중
2. Happy Path + Edge Case 모두 테스트
3. 테스트 이름을 명확하게 작성
4. Given-When-Then 구조 활용

**커버리지 관리:**
1. 80% 이상 유지
2. 핵심 로직은 100% 목표
3. DTO, Config는 제외 가능
4. 브랜치 커버리지도 고려

**CI/CD 연동:**
1. 커밋 전 테스트 필수
2. PR 시 커버리지 체크
3. 커버리지 감소 방지
4. 자동화된 리포트 생성

---

## 🎯 성과 및 다음 단계

### 달성 성과

```
✅ 통합 테스트 5개 작성
✅ 전체 테스트 38개 100% 통과
✅ 테스트 커버리지 97% 달성
✅ JaCoCo 설정 및 리포트 생성
✅ 트러블슈팅 경험 축적
```

### Phase 2-5 완료!

```
Module 1: REST Docs      ✅
Module 2: Swagger UI     ✅
Module 3: 통합 테스트    ✅
```

### 다음 학습 계획

**Phase 3: 고급 기능 구현**
- Security 인증/인가
- 페이징 및 정렬
- 예외 처리 고도화
- 로깅 및 모니터링

---

## 📎 참고 자료

### 공식 문서
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [AssertJ Documentation](https://assertj.github.io/doc/)

### 학습 자료
- Spring Boot 공식 가이드
- JUnit 5 User Guide
- Mockito 프레임워크 가이드

### 프로젝트 파일
- `BlogApiIntegrationTest.java`: 통합 테스트
- `build.gradle`: JaCoCo 설정
- `build/reports/jacoco/test/html/index.html`: 커버리지 리포트

---

## 📝 회고

### 잘한 점
1. **체계적인 테스트 작성**: 단위 → 통합 → 문서화 순서로 진행
2. **높은 커버리지 달성**: 97%로 목표 초과 달성
3. **실전 경험 축적**: 다양한 트러블슈팅 해결

### 배운 점
1. **통합 테스트의 중요성**: 전체 플로우 검증의 필요성 체감
2. **Record 타입 특성**: DTO 패턴의 장단점 이해
3. **테스트 자동화**: JaCoCo를 통한 품질 관리 방법

### 개선할 점
1. **브랜치 커버리지**: 조건문 테스트 강화 필요
2. **예외 처리 테스트**: Exception 패키지 커버리지 향상
3. **성능 테스트**: 대용량 데이터 처리 테스트 추가

---

**작성 완료: 2025-12-20**  
**다음: Phase 3 - 고급 기능 구현**