# 📚 Git Factory Learning Path

> **학습 경로 가이드** - 체계적인 풀스택 개발 여정

이 문서는 Git Factory 프로젝트의 전체 학습 경로를 상세히 안내합니다.

---

## 🎯 학습 목표

1. **실전 중심 학습**: 이론보다 실습, 튜토리얼보다 프로젝트
2. **체계적 문서화**: 모든 학습 과정을 상세히 기록
3. **점진적 발전**: 각 Phase가 다음 Phase의 기반
4. **실무 역량 강화**: Git Workflow부터 MSA까지

---

## 📅 Phase 별 학습 계획

### ✅ Phase 1: 기초 다지기 (완료)

**기간**: 2024-11-18 ~ 2024-11-22 (5일)  
**목표**: Git, TypeScript, React 기초 완성

#### 학습 모듈
1. **Git Workflow** (~2일)
    - 브랜치 전략 (feature, develop, main)
    - Conflict 해결 실습
    - Conventional Commits

2. **TypeScript** (~1일)
    - Type Guards 20개
    - Utility Types 11개
    - 실전 함수 45개

3. **React Fundamentals** (~1일)
    - Hooks (useState, useEffect)
    - Custom Hooks
    - Component 설계

4. **통합 프로젝트** (~1일)
    - 북마크 관리 앱 완성
    - CRUD 구현
    - LocalStorage 활용

---

### ✅ Phase 2: 중급 개발 (완료)

**기간**: 2025-11-23 ~ 2025-12-20 (28일)  
**목표**: Backend 기초 + 테스트 + 문서화

#### 학습 모듈

**2-1. Next.js 14** (1일)
- App Router 구조
- Server/Client Components
- Dynamic Routes

**2-2. Spring Boot + JPA** (1일)
- REST API 설계
- JPA Entity & Repository
- 계층형 아키텍처

**2-3. Docker** (1일)
- Containerization 개념
- Dockerfile 작성
- Docker Compose

**2-4. TDD & Testing** (2일)
- Repository/Service/Controller 테스트
- Mockito & MockMvc
- 25개 테스트 작성

**2-5. API 문서화 & 통합 테스트** (3일)
- REST Docs
- Swagger/OpenAPI
- JaCoCo 커버리지 97%

---

### 🔄 Phase 3: 고급 JPA & 성능 최적화 (진행 중)

**시작일**: 2026-01-03  
**현재 진도**: 3-2 완료 ✅  
**목표**: JPA 심화, 성능 최적화, 보안

---

#### ✅ 3-1. JPA 연관 관계 매핑 (완료)

**학습 기간**: 2026-01-03 (1일)  
**목표**: Entity 간 관계 설정 및 LAZY Loading

**학습 내용**:
- @ManyToOne 관계 매핑
- User 엔티티 설계
- FetchType.LAZY 전략
- Query Methods 네이밍

**실습 과제**:
- [x] User 엔티티 생성
- [x] Post-User 관계 설정
- [x] 53개 테스트 수정
- [x] Spring MVC 경로 충돌 해결

**성과**:
- ✅ User-Post ManyToOne 관계 구현
- ✅ 53개 테스트 100% 통과
- ✅ ERD 다이어그램 작성

**학습 자료**:
- 📘 [JPA_LEARNING.md](../docs/JPA_LEARNING.md)
- 📗 [PHASE3-1_HANDOVER.md](../docs/PHASE3-1_HANDOVER.md)

---

#### ✅ 3-2. N+1 문제 해결 & Fetch Join (완료)

**학습 기간**: 2026-01-04 (1일)  
**목표**: N+1 문제 이해 및 성능 최적화

**학습 내용**:
- N+1 문제 발생 원인
- Fetch Join 개념 및 구현
- @Query with JOIN FETCH
- Query Method 자동 JOIN
- SQL 로깅 및 분석
- 성능 측정

**실습 과제**:
- [x] N+1 문제 재현 테스트
- [x] Fetch Join 메서드 구현
- [x] 성능 비교 테스트
- [x] SQL 로깅 설정

**성과**:
- ✅ 4개 성능 테스트 작성
- ✅ 쿼리 98% 감소 (51 → 1)
- ✅ Query Method JOIN 발견
- ✅ SQL 분석 완료

**성능 개선 결과**:
```
Before: 51 queries (N+1 문제)
After:  1 query (Fetch Join)
개선율: 98%
```

**해결 방법 비교**:
| 방법 | 구현 난이도 | 효과 | 상태 |
|------|------------|------|------|
| Fetch Join | 중 | ⭐⭐⭐⭐⭐ | ✅ 완료 |
| @EntityGraph | 하 | ⭐⭐⭐⭐ | 📅 예정 |
| Batch Size | 하 | ⭐⭐⭐ | 📅 예정 |
| Query Method | - | ⭐⭐⭐⭐ | ✅ 확인 |

**학습 자료**:
- 📙 [PHASE3-2_HANDOVER.md](../docs/PHASE3-2_HANDOVER.md)

---

#### 📋 3-3. @EntityGraph & Batch Size (예정)

**예정일**: 2026-01-05  
**목표**: 추가 성능 최적화 방법 학습

**학습 내용**:
- @EntityGraph 활용
- Batch Size 설정
- 페이징과의 조합
- 성능 비교

**실습 과제**:
- [ ] @EntityGraph 구현
- [ ] Batch Size 설정
- [ ] 성능 측정 및 비교
- [ ] 문서 작성

---

#### 📋 3-4. Comment 엔티티 (OneToMany) (예정)

**예정일**: 2026-01-06  
**목표**: OneToMany 관계 및 양방향 매핑

**학습 내용**:
- @OneToMany 관계
- 양방향 매핑
- orphanRemoval
- CascadeType

**실습 과제**:
- [ ] Comment 엔티티 생성
- [ ] Post-Comment 관계 설정
- [ ] CRUD API 구현
- [ ] 테스트 작성

---

#### 📋 3-5. 페이징 & 정렬 (Pageable) (예정)

**예정일**: 2026-01-07  
**목표**: 대용량 데이터 처리

**학습 내용**:
- Pageable 인터페이스
- Page vs Slice
- 정렬 전략
- 커서 기반 페이징

**실습 과제**:
- [ ] Pageable 구현
- [ ] 정렬 기능 추가
- [ ] 성능 테스트
- [ ] API 문서화

---

#### 📋 3-6. Spring Security & JWT (예정)

**예정일**: 2026-01-08 ~  
**목표**: 인증/인가 구현

**학습 내용**:
- Spring Security 구조
- JWT 인증
- Role 기반 권한
- 보안 필터 체인

**실습 과제**:
- [ ] Security 설정
- [ ] JWT 발급/검증
- [ ] 권한 체크
- [ ] 보안 테스트

---

## 📊 Phase 3 학습 현황

**전체 진행률**: 33% (2/6 완료)

```
✅ 3-1. JPA 연관 관계 매핑       [████████████] 100%
✅ 3-2. N+1 문제 해결            [████████████] 100%
⬜ 3-3. @EntityGraph & Batch     [            ]   0%
⬜ 3-4. Comment 엔티티           [            ]   0%
⬜ 3-5. 페이징 & 정렬            [            ]   0%
⬜ 3-6. Spring Security          [            ]   0%
```

**완료된 학습**:
- ✅ JPA 연관 관계 (@ManyToOne)
- ✅ FetchType.LAZY 전략
- ✅ N+1 문제 이해 및 해결
- ✅ Fetch Join 구현
- ✅ Query Method 자동 JOIN
- ✅ SQL 로깅 및 분석

**학습 중**:
- 🔄 성능 최적화 전략

**다음 학습**:
- 📅 @EntityGraph
- 📅 Batch Size
- 📅 OneToMany 관계

---

## 📈 학습 통계

### Phase 3 누적 현황

**코드**:
- Entity/DTO: ~600 lines
- 테스트: ~580 lines
- 총 코드: ~1,180 lines

**테스트**:
- 총 테스트: 57개
- 통과율: 100% ✅
- 커버리지: 유지

**성능 개선**:
- N+1 쿼리: 98% 감소
- 51 queries → 1 query

**문서**:
- JPA_LEARNING.md: ~1,000 lines
- PHASE3-1_HANDOVER.md: ~800 lines
- PHASE3-2_HANDOVER.md: ~1,200 lines
- 총 문서: ~3,000 lines

**Pull Requests**:
- Phase 3-1: #1 (Merged ✅)
- Phase 3-2: #2 (Merged ✅)

---

## 🎯 학습 원칙

### 1. 실습 우선
- 이론 30%, 실습 70%
- 모든 개념은 코드로 구현
- 동작하는 프로젝트 완성

### 2. 테스트 주도
- 모든 기능에 테스트 작성
- TDD 사이클 준수
- 커버리지 90% 이상 유지

### 3. 문서화 필수
- 학습 노트 작성
- 핸드오버 문서 작성
- README 업데이트

### 4. Git Workflow
- Feature 브랜치 사용
- Conventional Commits
- Pull Request & Code Review

---

## 📚 추천 학습 자료

### Phase 3 관련

**JPA & Hibernate**:
- 📘 자바 ORM 표준 JPA 프로그래밍 (김영한)
- 🌐 [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- 🎥 인프런 - JPA 활용 시리즈

**성능 최적화**:
- 📗 [Vlad Mihalcea - N+1 Query Problem](https://vladmihalcea.com/n-plus-1-query-problem/)
- 📙 High-Performance Java Persistence
- 🌐 [Baeldung - JPA Performance](https://www.baeldung.com/jpa-performance)

**Spring Security**:
- 📕 스프링 시큐리티 인 액션
- 🌐 [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- 🎥 JWT 인증/인가 실습

---

## 🔜 다음 Phase 미리보기

### Phase 4: 프론트엔드 심화
- Next.js App Router 심화
- Server Actions
- React Query
- Zustand State Management

### Phase 5: MSA 아키텍처
- Spring Cloud
- Service Discovery
- API Gateway
- Message Queue (Kafka)

### Phase 6: DevOps & 배포
- CI/CD (GitHub Actions)
- AWS 배포
- Monitoring (Prometheus, Grafana)
- 로깅 전략

---

## 📝 학습 체크리스트

### Phase 3-2 완료 항목 ✅

- [x] N+1 문제 개념 이해
- [x] Fetch Join 구현
- [x] 성능 테스트 작성
- [x] SQL 로깅 설정
- [x] 성능 비교 및 분석
- [x] 문서 작성 완료
- [x] Pull Request 생성
- [x] README 업데이트

### Phase 3-3 준비 사항

- [ ] @EntityGraph 개념 학습
- [ ] Batch Size 문서 읽기
- [ ] 예제 코드 분석
- [ ] 학습 계획 수립

---

## 💡 학습 팁

### 효과적인 학습 방법

1. **개념 → 구현 → 테스트 → 문서**
    - 개념을 먼저 이해하고
    - 직접 코드로 구현하고
    - 테스트로 검증하고
    - 문서로 정리

2. **문제 해결 중심**
    - "왜 이 문제가 발생했나?"
    - "어떻게 해결할 수 있나?"
    - "다른 방법은 없나?"

3. **성능 측정 습관**
    - 최적화 전/후 비교
    - 수치로 증명
    - 트레이드오프 고려

4. **지속적인 리팩토링**
    - 코드 품질 개선
    - 테스트 유지보수
    - 문서 업데이트

---

## 🎓 배운 점 정리

### Phase 3-1 핵심 교훈
1. **연관 관계는 신중하게**
    - LAZY Loading 기본
    - 양방향은 필요시만
    - 순환 참조 주의

2. **Query Methods 네이밍**
    - `_` 로 엔티티 탐색
    - Spring Data JPA 자동 생성
    - 명확한 메서드명

3. **테스트 리팩토링의 중요성**
    - 엔티티 변경 시 전체 영향
    - Mock 데이터 일관성
    - 테스트 격리

### Phase 3-2 핵심 교훈
1. **N+1 문제는 치명적**
    - LAZY Loading의 함정
    - 네트워크 왕복 비용
    - 실제 환경에서 더 심각

2. **Fetch Join이 최선**
    - 한 번의 쿼리로 해결
    - JPQL로 명시적 제어
    - 가장 효과적인 방법

3. **성능 측정이 핵심**
    - 추측보다 측정
    - SQL 로그 분석
    - 쿼리 횟수 확인

4. **Query Method도 강력**
    - 자동 JOIN 생성
    - 네이밍 규칙만 따르면
    - 추가 코드 불필요

---

## 📅 학습 일정 관리

### 주간 목표 (2026-01-06 ~ 2026-01-10)
- [ ] Phase 3-3 완료
- [ ] Phase 3-4 시작
- [ ] Comment API 구현

### 월간 목표 (2026-01)
- [ ] Phase 3 전체 완료
- [ ] 60개 이상 테스트
- [ ] Security 기초 구현

---

<p align="center">
  <strong>꾸준함이 답이다 🔥</strong>
</p>