# Phase 3-3 Handover Document
> @EntityGraph & Batch Size를 활용한 N+1 문제 추가 해결 방법

## 📋 작업 개요

**작업 기간**: 2026-01-18  
**담당자**: 이환  
**Phase**: 3-3 - @EntityGraph & Batch Size 성능 최적화  
**상태**: ✅ 완료

---

## 🎯 작업 목표

1. @EntityGraph를 사용한 N+1 문제 해결
2. Batch Size 설정을 통한 N+1 문제 완화
3. 3가지 방법 성능 비교 (Fetch Join vs @EntityGraph vs Batch Size)
4. 실전 사용 가이드 작성

---

## ✅ 완료된 작업

### 1. PostRepository - @EntityGraph 메서드 추가

**파일**: `src/main/java/com/gitfactory/blogapi/repository/PostRepository.java`

```java
// ✨ @EntityGraph로 N+1 문제 해결 (간결한 코드)
@EntityGraph(attributePaths = {"author"})
@Query("SELECT p FROM Post p")
List<Post> findAllWithEntityGraph();

@EntityGraph(attributePaths = {"author"})
Optional<Post> findWithAuthorById(Long id);
```

**@EntityGraph란?**
- Spring Data JPA가 제공하는 N+1 해결 방법
- `attributePaths`로 함께 로딩할 연관 엔티티 지정
- Fetch Join보다 코드가 간결
- 동적으로 페치 전략 변경 가능

**특징**:
- 일반적으로 LEFT OUTER JOIN 사용
- `@ManyToOne`은 최적화되어 INNER JOIN 사용
- Fetch Join과 동일한 1번의 쿼리

---

### 2. Batch Size 설정

**파일**: `src/main/resources/application.properties`

```properties
# ✨ Batch Fetch Size 설정 (N+1 문제 완화)
spring.jpa.properties.hibernate.default_batch_fetch_size=100
```

**Batch Size란?**
- Hibernate의 글로벌 설정
- 연관 엔티티를 IN 절로 한 번에 조회
- 코드 변경 없이 N+1 완화
- 모든 연관 관계에 자동 적용

**동작 방식**:
```sql
-- N+1 문제 (Before)
SELECT * FROM posts;
SELECT * FROM users WHERE id = ?;  -- 50번 반복

-- Batch Size 적용 (After)
SELECT * FROM posts;
SELECT * FROM users WHERE id IN (?, ?, ?, ...);  -- 1번
```

---

### 3. 성능 비교 테스트 작성

**파일**: `src/test/java/com/gitfactory/blogapi/performance/PerformanceComparisonTest.java`

#### 테스트 실행 결과

```
📊 성능 순위:
   1위: @EntityGraph     - 10ms  ⭐
   2위: Fetch Join       - 13ms
   3위: Batch Size       - 15ms
```

---

## 📊 3가지 방법 비교

### 종합 비교표

| 방법 | 쿼리 횟수 | 성능 | JOIN 타입 | 코드 복잡도 | 추천도 |
|------|-----------|------|-----------|-------------|--------|
| **@EntityGraph** | 1번 | 10ms ⭐ | INNER | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Fetch Join** | 1번 | 13ms | INNER | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Batch Size** | 2번 | 15ms | - | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

---

## 💡 실전 사용 가이드

### 1. 일반적인 경우 → **@EntityGraph** ⭐ (추천!)

```java
@EntityGraph(attributePaths = {"author"})
List<Post> findAll();
```

### 2. 복잡한 쿼리 → **Fetch Join**

```java
@Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.title LIKE %:keyword%")
List<Post> findByTitleWithAuthor(String keyword);
```

### 3. 레거시 개선 → **Batch Size**

```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=100
```

### 4. 페이징 필요 → **@EntityGraph**

```java
@EntityGraph(attributePaths = {"author"})
Page<Post> findAll(Pageable pageable);
```

---

## 🎓 학습 포인트

### 1. @EntityGraph의 JOIN 타입

**예상**: LEFT OUTER JOIN  
**실제**: INNER JOIN (최적화)

**이유**: `@ManyToOne`의 경우 Hibernate가 데이터 정합성을 보고 INNER JOIN으로 자동 최적화

---

### 2. 성능 측정의 중요성

**H2 Database** (인메모리):
- 차이가 작음 (10ms vs 15ms)

**실제 DB** (PostgreSQL/MySQL):
- 네트워크 왕복이 큰 영향
- 2 queries는 2배의 네트워크 비용

---

## 🐛 트러블슈팅

### 문제 1: 페이징 + Fetch Join 경고

**해결**: @EntityGraph 사용
```java
@EntityGraph(attributePaths = {"author"})
Page<Post> findAll(Pageable pageable);
```

### 문제 2: Batch Size가 동작 안 함

**원인**: EAGER Loading은 Batch Size 적용 안 됨  
**해결**: LAZY Loading으로 변경

---

## 📁 파일 구조

```
blog-api/
├── src/
│   ├── main/
│   │   ├── java/.../repository/
│   │   │   └── PostRepository.java            ✨ (@EntityGraph 추가)
│   │   └── resources/
│   │       └── application.properties         ✨ (Batch Size 설정)
│   └── test/
│       └── java/.../performance/
│           └── PerformanceComparisonTest.java ✨ (6개 테스트)
```

---

## 📊 테스트 결과

```
✅ PerformanceComparisonTest:  6/6 (100%)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ 총 Performance 테스트:    10/10 (100%)
```

---

## 🎯 주요 성과

1. ✅ 3가지 N+1 해결 방법 완성
2. ✅ 성능 측정 완료 (10ms vs 13ms vs 15ms)
3. ✅ 실전 가이드 작성
4. ✅ 6개 테스트 100% 통과

---

## 🔜 다음 단계 (Phase 3-4)

- Comment 엔티티 추가 (OneToMany)
- 양방향 매핑
- Collection Fetch 최적화

---

**작성일**: 2026-01-18  
**Phase**: 3-3 완료 ✅