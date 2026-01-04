# 🏭 Git Factory

> 🎓 **체계적인 풀스택 개발 학습 저장소** - Git 워크플로우부터 MSA까지

[![GitHub stars](https://img.shields.io/github/stars/hwan0050/git-factory?style=social)](https://github.com/hwan0050/git-factory/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/hwan0050/git-factory?style=social)](https://github.com/hwan0050/git-factory/network/members)
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Git Factory는 **현대적인 웹 개발 기술과 MSA(Microservices Architecture)**를 학습하기 위한 체계적인 저장소입니다.

## ✨ 특징

- ✅ **Git 워크플로우** - 브랜치 전략, 협업, 코드 리뷰
- ✅ **풀스택 개발** - Frontend부터 Backend까지
- ✅ **MSA 아키텍처** - 마이크로서비스 설계 및 구현
- ✅ **최신 개발 방법론** - TDD, Clean Code, Agile
- ✅ **실전 프로젝트** - 학습한 내용을 실제 프로젝트에 적용

---

## 🎯 프로젝트 개요

Git Factory는 단순한 코드 저장소가 아닌, **체계적인 학습 여정**을 기록하는 공간입니다.  
각 Phase는 실전 프로젝트를 통해 기술을 익히고, 상세한 문서화를 통해 지식을 내재화합니다.

### 🎯 핵심 학습 원칙

1. **실전 중심**: 이론보다 실습, 튜토리얼보다 프로젝트
2. **체계적 문서화**: 모든 학습 과정을 상세히 기록
3. **점진적 발전**: 각 Phase가 다음 Phase의 기반이 됨
4. **Git Workflow**: 실무와 동일한 브랜치 전략 및 PR 프로세스

---

## 🗺️ 학습 로드맵

### ✅ Phase 1: 기초 다지기 (완료!)

**학습 기간**: 2024-11-18 ~ 2024-11-22 (5일)  
**총 학습 시간**: 약 15시간  
**완성 코드**: ~4,000줄  
**완료 PR**: 4개

#### 완료된 모듈
- ✅ **Git 워크플로우 마스터**
- ✅ **TypeScript 완벽 정복**
- ✅ **React 핵심 개념**
- ✅ **통합 실전 프로젝트** (북마크 관리 앱)

---

### ✅ Phase 2: 중급 개발 (완료!)

**시작일**: 2025-11-23  
**완료일**: 2025-12-20

#### 완료된 모듈
- ✅ Phase 2-1: Next.js 14 App Router
- ✅ Phase 2-2: Spring Boot 3.x + JPA
- ✅ Phase 2-3: Docker & Containerization
- ✅ Phase 2-4: TDD & Spring Boot Testing
- ✅ Phase 2-5: API Documentation & Integration Testing

**최종 성과**:
- 테스트: 38개 (100% 통과)
- 코드 커버리지: 97%
- 작성 문서: ~7,000 lines

---

### 🚀 Phase 3: 고급 JPA & 성능 최적화 (진행 중)

**시작일**: 2026-01-03  
**현재 진도**: Phase 3-2 완료 ✅

---

#### ✅ Phase 3-1: JPA 연관 관계 매핑 (완료!)

**학습 기간**: 2026-01-03 (1일)  
**학습 시간**: 약 4-5시간  
**완료 커밋**: 17개

**학습 내용**:
- JPA 연관 관계 매핑 (@ManyToOne)
- User 엔티티 설계 (UserRole Enum)
- Post-User 연관 관계 구현
- FetchType.LAZY 지연 로딩 전략
- Query Methods 네이밍 규칙

**주요 성과**:
- User-Post ManyToOne 관계 구현
- 53개 테스트 100% 통과 ✅
- Spring MVC 경로 충돌 해결
- FetchType.LAZY 적용 (N+1 문제 대비)

**ERD**:
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

**문서**:
- [PHASE3-1_HANDOVER.md](./docs/PHASE3-1_HANDOVER.md)
- [JPA_LEARNING.md](./docs/JPA_LEARNING.md)

---

#### ✅ Phase 3-2: N+1 문제 해결 & Fetch Join (완료!)

**학습 기간**: 2026-01-04 (1일)  
**학습 시간**: 약 3-4시간  
**완성 코드**: ~180줄 (테스트 코드)  
**완료 커밋**: 3개

**학습 내용**:
- N+1 문제 개념 및 발생 원인
- Fetch Join을 통한 N+1 문제 해결
- @Query with JOIN FETCH
- Query Method 자동 JOIN 발견
- SQL 로깅 설정 및 쿼리 분석
- 성능 비교 및 측정

**완성 프로젝트**: `blog-api` (성능 최적화)
- N1ProblemTest 4개 테스트 작성
- findAllWithAuthor() Fetch Join 메서드
- findByIdWithAuthor() Fetch Join 메서드
- SQL 로깅 설정 (hibernate.use_sql_comments)
- 성능 비교 테스트 (51 queries → 1 query)

**테스트 구조**:
```
src/test/java/com/gitfactory/blogapi/
└── performance/
    └── N1ProblemTest.java       (4개 테스트) ✅

테스트 시나리오:
1. N+1 문제 재현 - findAll() (51 queries)
2. Query Method 자동 JOIN 확인
3. Fetch Join 해결 - findAllWithAuthor() (1 query)
4. 성능 비교 (findAll vs findAllWithAuthor)
```

**성능 개선 결과**:
```
Before (N+1):     51 queries (1 + 50)
After (Fetch):    1 query
쿼리 감소율:      98% (51 → 1)
```

**주요 성과**:
- N+1 문제 재현 및 확인
- Fetch Join 구현 (JPQL @Query)
- Query Method 자동 JOIN 발견
- SQL 로깅으로 쿼리 분석
- 성능 측정 및 비교

**해결 방법 비교**:
1. **Fetch Join** (구현 완료) - 가장 효과적
2. **@EntityGraph** (학습 예정) - 간결한 코드
3. **Batch Size** (학습 예정) - 글로벌 설정
4. **Query Method** (확인 완료) - 자동 JOIN

**문서**:
- [PHASE3-2_HANDOVER.md](./docs/PHASE3-2_HANDOVER.md) (~1,200 lines)

---

#### 📋 Phase 3 다음 단계

- [x] Phase 3-1: JPA 연관 관계 매핑
- [x] Phase 3-2: N+1 문제 해결 & Fetch Join
- [ ] Phase 3-3: @EntityGraph & Batch Size
- [ ] Phase 3-4: Comment 엔티티 (OneToMany)
- [ ] Phase 3-5: 페이징 & 정렬 (Pageable)
- [ ] Phase 3-6: Spring Security & JWT

---

## 📁 프로젝트 구조

```
git-factory/
├── docs/                                    # 📚 모든 학습 문서 통합
│   ├── SPRING_BOOT_LEARNING.md
│   ├── DOCKER_LEARNING.md
│   ├── TESTING_LEARNING.md
│   ├── SWAGGER_LEARNING.md
│   ├── JPA_LEARNING.md                     (Phase 3-1)
│   ├── PHASE3-1_HANDOVER.md                (Phase 3-1)
│   └── PHASE3-2_HANDOVER.md                (Phase 3-2) ✨
│
├── practices/
│   └── java/spring-boot/blog-api/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/gitfactory/blogapi/
│       │   │   │   ├── repository/
│       │   │   │   │   ├── PostRepository.java        ✨ (Fetch Join 추가)
│       │   │   │   │   └── UserRepository.java
│       │   │   │   └── ...
│       │   │   └── resources/
│       │   │       └── application.properties         ✨ (SQL 로깅)
│       │   │
│       │   └── test/
│       │       └── java/com/gitfactory/blogapi/
│       │           ├── repository/         (7개)
│       │           ├── service/            (10개)
│       │           ├── controller/         (8개)
│       │           ├── integration/        (5개)
│       │           └── performance/
│       │               └── N1ProblemTest.java         ✨ (4개)
│       │
│       └── build.gradle
│
└── README.md
```

---

## 🧪 테스트 실행 방법

### blog-api 테스트

```bash
# 전체 테스트 실행
./gradlew clean test

# 성능 테스트만 실행
./gradlew test --tests "com.gitfactory.blogapi.performance.N1ProblemTest"
```

**예상 결과**:
```
BUILD SUCCESSFUL in 20s
✅ N1ProblemTest:              4/4   (100%) ✨
✅ BlogApiIntegrationTest:     5/5   (100%)
✅ PostRepositoryTest:         7/7   (100%)
✅ PostServiceTest:            10/10 (100%)
✅ PostControllerTest:         8/8   (100%)
✅ PostControllerRestDocsTest: 7/7   (100%)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ 총합:                       41/41 (100%) 🎉
```

---

## 📖 학습 문서

### Phase 3: 고급 JPA & 성능 최적화
- [JPA_LEARNING.md](./docs/JPA_LEARNING.md) - 1,000 lines
    - JPA 연관 관계 매핑
    - FetchType.LAZY vs EAGER
    - Query Methods 네이밍 규칙
- [PHASE3-1_HANDOVER.md](./docs/PHASE3-1_HANDOVER.md) - 800 lines
    - User-Post 관계 구현
    - 53개 테스트 리팩토링
- [PHASE3-2_HANDOVER.md](./docs/PHASE3-2_HANDOVER.md) - 1,200 lines
    - N+1 문제 개념 및 해결
    - Fetch Join 구현
    - 성능 측정 및 비교

---

## 🎯 Git Workflow

### 커밋 메시지 컨벤션

```bash
# Conventional Commits 형식
<type>(<scope>): <subject>

# 예시
perf(repository): Implement Fetch Join to solve N+1 problem
test(performance): Add N1ProblemTest with 4 test cases
docs(phase3-2): Add N+1 problem solution handover document
```

**Type**:
- `feat`: 새로운 기능
- `fix`: 버그 수정
- `docs`: 문서 변경
- `test`: 테스트 추가/수정
- `perf`: 성능 개선
- `refactor`: 리팩토링

---

## 📊 학습 통계

### ✅ Phase 1 (완료)
- **기간**: 2024-11-18 ~ 2024-11-22 (5일)
- **작성 코드**: ~4,000 lines
- **PR**: 4개

### ✅ Phase 2 (완료!)
- **기간**: 2025-11-23 ~ 2025-12-20
- **테스트 케이스**: 38개 (100% 통과)
- **코드 커버리지**: 97%
- **PR**: 17개

### 🔄 Phase 3 (진행 중)
- **시작일**: 2026-01-03
- **현재 진도**: Phase 3-2 완료 ✅
- **완료 모듈**: 3-1, 3-2
- **작성 코드**:
    - Entity/DTO 수정: ~600 lines
    - 테스트 코드: ~580 lines (57개)
- **테스트 케이스**: 57개 (100% 통과 ✅)
- **성능 개선**: N+1 쿼리 98% 감소 (51 → 1)
- **작성 문서**: ~3,000 lines (3개 문서)
- **PR**: 2개

---

## 🚀 다음 단계

### Phase 3 진행 중
- [x] Phase 3-1: JPA 연관 관계 매핑
- [x] Phase 3-2: N+1 문제 해결 & Fetch Join
- [ ] Phase 3-3: @EntityGraph & Batch Size
- [ ] Phase 3-4: Comment 엔티티 (OneToMany)
- [ ] Phase 3-5: 페이징 & 정렬
- [ ] Phase 3-6: Spring Security & JWT

---

## 📝 업데이트 로그

### 2026-01-04 - Phase 3-2 완료! 🎉
- ✅ N+1 문제 개념 학습 및 재현
- ✅ Fetch Join 구현 (JOIN FETCH)
- ✅ 성능 테스트 4개 작성 (모두 통과)
- ✅ 쿼리 최적화 (51 queries → 1 query, 98% 감소)
- ✅ SQL 로깅 설정 및 분석
- ✅ Query Method 자동 JOIN 발견
- 📊 총 180줄 테스트 코드, 1,200줄 문서
- **N+1 문제 완벽 해결!** 🚀

### 2026-01-03 - Phase 3-1 완료! 🎉
- ✅ JPA 연관 관계 매핑 (@ManyToOne) 학습 완료
- ✅ User 엔티티 신규 생성
- ✅ 전체 테스트 코드 리팩토링 (53개 100% 통과)
- 📊 총 600줄 코드, 1,800줄 문서

### 2025-12-20 - Phase 2 완료! 🎊
- ✅ 통합 테스트 & JaCoCo 커버리지 97% 달성
- **Phase 2 전체 완료!**

---

## 📞 Contact

- **GitHub**: [@hwan0050](https://github.com/hwan0050)
- **Email**: akma0050@naver.com

---

## 📝 License

이 프로젝트는 개인 학습 목적으로 작성되었습니다.

---

<p align="center">
  Made with ❤️ for Learning
</p>