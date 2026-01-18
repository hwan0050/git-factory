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
### ✅ Phase 2: 중급 개발 (완료!)

### 🚀 Phase 3: 고급 JPA & 성능 최적화 (진행 중)

**시작일**: 2026-01-03  
**현재 진도**: Phase 3-3 완료 ✅

---

#### ✅ Phase 3-1: JPA 연관 관계 매핑 (완료!)

**학습 기간**: 2026-01-03 (1일)  
**완료 커밋**: 17개

**주요 성과**:
- User-Post ManyToOne 관계 구현
- 53개 테스트 100% 통과 ✅
- FetchType.LAZY 적용

---

#### ✅ Phase 3-2: N+1 문제 해결 & Fetch Join (완료!)

**학습 기간**: 2026-01-04 (1일)  
**완료 커밋**: 3개

**성능 개선 결과**:
```
Before: 51 queries (N+1 문제)
After:  1 query (Fetch Join)
개선율: 98% 🚀
```

**문서**:
- [PHASE3-2_HANDOVER.md](./docs/PHASE3-2_HANDOVER.md)

---

#### ✅ Phase 3-3: @EntityGraph & Batch Size (완료!)

**학습 기간**: 2026-01-18 (1일)  
**학습 시간**: 약 3시간  
**완성 코드**: ~265줄  
**완료 커밋**: 2개

**학습 내용**:
- @EntityGraph를 사용한 N+1 해결
- Batch Size 글로벌 설정
- 3가지 방법 성능 비교
- 실전 사용 가이드 작성

**완성 프로젝트**: `blog-api` (성능 최적화)
- @EntityGraph 메서드 2개 추가
- Batch Size 100 설정
- PerformanceComparisonTest 6개 테스트

**성능 비교 결과**:
```
📊 성능 순위:
   1위: @EntityGraph     - 10ms  ⭐ (가장 빠름!)
   2위: Fetch Join       - 13ms
   3위: Batch Size       - 15ms (2 queries)
```

**3가지 방법 비교**:
| 방법 | 쿼리 횟수 | 성능 | 추천도 |
|------|-----------|------|--------|
| @EntityGraph | 1번 | 10ms ⭐ | ⭐⭐⭐⭐⭐ |
| Fetch Join | 1번 | 13ms | ⭐⭐⭐⭐ |
| Batch Size | 2번 | 15ms | ⭐⭐⭐ |

**주요 성과**:
- 3가지 N+1 해결 방법 완성
- @EntityGraph가 가장 효율적 확인
- 상황별 사용 가이드 작성
- 6개 테스트 100% 통과

**실전 가이드**:
1. **일반적인 경우** → @EntityGraph (간결, 빠름)
2. **복잡한 쿼리** → Fetch Join (명시적 제어)
3. **레거시 개선** → Batch Size (설정만)
4. **페이징 필요** → @EntityGraph (제약 없음)

**문서**:
- [PHASE3-3_HANDOVER.md](./docs/PHASE3-3_HANDOVER.md) (~1,500 lines)

---

#### 📋 Phase 3 다음 단계

- [x] Phase 3-1: JPA 연관 관계 매핑
- [x] Phase 3-2: N+1 문제 해결 & Fetch Join
- [x] Phase 3-3: @EntityGraph & Batch Size
- [ ] Phase 3-4: Comment 엔티티 (OneToMany)
- [ ] Phase 3-5: 페이징 & 정렬 (Pageable)
- [ ] Phase 3-6: Spring Security & JWT

---

## 📁 프로젝트 구조

```
git-factory/
├── docs/
│   ├── JPA_LEARNING.md
│   ├── PHASE3-1_HANDOVER.md
│   ├── PHASE3-2_HANDOVER.md
│   └── PHASE3-3_HANDOVER.md              ✨ (Phase 3-3)
│
└── practices/java/spring-boot/blog-api/
    ├── src/
    │   ├── main/
    │   │   ├── java/.../repository/
    │   │   │   └── PostRepository.java   ✨ (@EntityGraph 추가)
    │   │   └── resources/
    │   │       └── application.properties ✨ (Batch Size)
    │   │
    │   └── test/
    │       └── java/.../performance/
    │           ├── N1ProblemTest.java           (4개)
    │           └── PerformanceComparisonTest.java ✨ (6개)
```

---

## 🧪 테스트 실행 방법

```bash
cd practices/java/spring-boot/blog-api

# 전체 테스트 실행
./gradlew clean test

# 성능 테스트만 실행
./gradlew test --tests "com.gitfactory.blogapi.performance.*"
```

**예상 결과**:
```
✅ N1ProblemTest:                   4/4   (100%)
✅ PerformanceComparisonTest:       6/6   (100%)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Performance 패키지:              10/10 (100%)
```

---

## 📖 학습 문서

### Phase 3: 고급 JPA & 성능 최적화
- [JPA_LEARNING.md](./docs/JPA_LEARNING.md) - 1,000 lines
- [PHASE3-1_HANDOVER.md](./docs/PHASE3-1_HANDOVER.md) - 800 lines
- [PHASE3-2_HANDOVER.md](./docs/PHASE3-2_HANDOVER.md) - 1,200 lines
- [PHASE3-3_HANDOVER.md](./docs/PHASE3-3_HANDOVER.md) - 1,500 lines ✨

---

## 📊 학습 통계

### ✅ Phase 1 (완료)
- **기간**: 2024-11-18 ~ 2024-11-22 (5일)
- **작성 코드**: ~4,000 lines

### ✅ Phase 2 (완료!)
- **기간**: 2025-11-23 ~ 2025-12-20
- **테스트**: 38개 (100% 통과)
- **코드 커버리지**: 97%

### 🔄 Phase 3 (진행 중)
- **시작일**: 2026-01-03
- **현재 진도**: Phase 3-3 완료 ✅
- **완료 모듈**: 3-1, 3-2, 3-3
- **작성 코드**:
    - Entity/DTO: ~600 lines
    - 테스트: ~845 lines (63개)
- **테스트 케이스**: 63개 (100% 통과 ✅)
- **성능 개선**:
    - N+1 쿼리: 98% 감소 (51 → 1)
    - 3가지 해결 방법 완성
- **작성 문서**: ~4,500 lines (4개 문서)
- **PR**: 3개 (모두 Merged)

---

## 🚀 다음 단계

### Phase 3 진행 중
- [x] Phase 3-1: JPA 연관 관계 매핑
- [x] Phase 3-2: N+1 문제 해결 & Fetch Join
- [x] Phase 3-3: @EntityGraph & Batch Size
- [ ] Phase 3-4: Comment 엔티티 (OneToMany)
- [ ] Phase 3-5: 페이징 & 정렬
- [ ] Phase 3-6: Spring Security & JWT

**진행률**: 50% (3/6 완료) 🎉

---

## 📝 업데이트 로그

### 2026-01-18 - Phase 3-3 완료! 🎉
- ✅ @EntityGraph 구현 (2개 메서드)
- ✅ Batch Size 설정 (100)
- ✅ 3가지 방법 성능 비교 완료
- ✅ 성능 테스트 6개 작성 (모두 통과)
- ✅ @EntityGraph 10ms로 가장 빠름!
- ✅ 상황별 사용 가이드 작성
- 📊 총 265줄 코드, 1,500줄 문서
- **Phase 3 절반 완료!** (3/6) 🎊

### 2026-01-04 - Phase 3-2 완료! 🎉
- ✅ N+1 문제 해결 (Fetch Join)
- ✅ 성능 98% 개선 (51 → 1 query)
- 📊 총 180줄 코드, 1,200줄 문서

### 2026-01-03 - Phase 3-1 완료! 🎉
- ✅ JPA 연관 관계 매핑
- ✅ 53개 테스트 리팩토링
- 📊 총 600줄 코드, 1,800줄 문서

---

## 📞 Contact

- **GitHub**: [@hwan0050](https://github.com/hwan0050)
- **Email**: akma0050@naver.com

---

<p align="center">
  Made with ❤️ for Learning
</p>