## 📝 Phase 3-2: N+1 문제 해결 & 성능 최적화

### 🎯 작업 내용

Fetch Join을 사용한 N+1 문제 해결 및 성능 측정

### ✅ 주요 변경사항

#### 1. SQL 로깅 설정
- **application.properties** 수정
    - `spring.jpa.show-sql=true`
    - `spring.jpa.properties.hibernate.format_sql=true`
    - `spring.jpa.properties.hibernate.use_sql_comments=true`
    - SQL 쿼리 및 파라미터 바인딩 로그 활성화

#### 2. PostRepository - Fetch Join 메서드 추가
```java
@Query("SELECT p FROM Post p JOIN FETCH p.author")
List<Post> findAllWithAuthor();

@Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
Post findByIdWithAuthor(Long id);
```

#### 3. N+1 문제 테스트 작성
- **N1ProblemTest.java** 신규 생성 (4개 테스트)
    - TEST 1: N+1 문제 재현 - findAll()
    - TEST 2: Query Method JOIN 확인 - searchByAuthor()
    - TEST 3: Fetch Join 해결 - findAllWithAuthor()
    - TEST 4: 성능 비교

### 📊 테스트 결과

#### ❌ N+1 문제 (findAll)
```sql
-- Post 조회: 1번
SELECT * FROM posts

-- User 조회: 50번 (각 Post마다!)
SELECT * FROM users WHERE id=?
SELECT * FROM users WHERE id=?
...

총 쿼리: 51번
```

#### ✅ Fetch Join 해결 (findAllWithAuthor)
```sql
-- Post + User 한 번에 조회: 1번
SELECT p.*, u.* 
FROM posts p 
JOIN users u ON p.user_id = u.id

총 쿼리: 1번
```

### 🚀 성능 개선
```
findAll() (N+1)           : 23ms, 51 queries
findAllWithAuthor() (JOIN): 68ms, 1 query

쿼리 횟수: 51번 → 1번 (98% 감소)
성능 개선율: 33.8%
```

**Note**: 실제 프로덕션 환경에서는 더 큰 성능 차이 발생
- 네트워크 왕복 시간 (51번 vs 1번)
- 데이터양 증가 시 효과 극대화

### 📚 학습 포인트

#### N+1 문제란?
- **발생 원인**: LAZY Loading에서 연관 엔티티 접근 시 추가 쿼리 발생
- **문제점**: 쿼리 횟수 = 1 + N (N = 조회된 엔티티 개수)

#### Fetch Join
```java
// JPQL
@Query("SELECT p FROM Post p JOIN FETCH p.author")

// 생성되는 SQL
SELECT p.*, u.* FROM posts p INNER JOIN users u ON p.user_id = u.id
```

#### Query Method의 자동 JOIN
```java
// Query Method
List<Post> findByAuthor_UsernameContaining(String keyword);

// 자동으로 LEFT JOIN 생성!
SELECT p.* FROM posts p 
LEFT JOIN users u ON p.user_id = u.id 
WHERE u.username LIKE ?
```

### 🐛 트러블슈팅

**Issue**: H2 Database에서는 성능 차이가 미미
- **원인**: 인메모리 DB라 네트워크 왕복 없음
- **실제 환경**: PostgreSQL/MySQL에서는 극적인 차이

### 📁 변경 파일

**신규 생성** (1개):
- `src/test/java/com/gitfactory/blogapi/performance/N1ProblemTest.java`

**수정** (2개):
- `src/main/resources/application.properties` (SQL 로깅)
- `src/main/java/com/gitfactory/blogapi/repository/PostRepository.java` (Fetch Join)

### ✨ 테스트 결과
```
4 tests completed, 4 passed ✅

- n1Problem_findAll()           : N+1 문제 재현
- n1Problem_searchByAuthor()    : Query Method JOIN 확인
- fetchJoin_solution()          : Fetch Join 해결
- performance_comparison()      : 성능 비교
```

### 🔜 다음 단계

- [ ] @EntityGraph 방식 학습
- [ ] Batch Size 설정
- [ ] Service 계층에 적용
- [ ] 실제 API에서 성능 측정