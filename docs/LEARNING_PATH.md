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
### ✅ Phase 2: 중급 개발 (완료)

### 🔄 Phase 3: 고급 JPA & 성능 최적화 (진행 중)

**시작일**: 2026-01-03  
**현재 진도**: 3-3 완료 ✅  
**목표**: JPA 심화, 성능 최적화, 보안

---

#### ✅ 3-1. JPA 연관 관계 매핑 (완료)

**학습 기간**: 2026-01-03 (1일)  
**목표**: Entity 간 관계 설정 및 LAZY Loading

**실습 과제**:
- [x] User 엔티티 생성
- [x] Post-User 관계 설정
- [x] 53개 테스트 수정
- [x] Spring MVC 경로 충돌 해결

**성과**:
- ✅ User-Post ManyToOne 관계 구현
- ✅ 53개 테스트 100% 통과

---

#### ✅ 3-2. N+1 문제 해결 & Fetch Join (완료)

**학습 기간**: 2026-01-04 (1일)  
**목표**: N+1 문제 이해 및 성능 최적화

**실습 과제**:
- [x] N+1 문제 재현 테스트
- [x] Fetch Join 메서드 구현
- [x] 성능 비교 테스트
- [x] SQL 로깅 설정

**성과**:
- ✅ 쿼리 98% 감소 (51 → 1)
- ✅ 4개 성능 테스트 작성

---

#### ✅ 3-3. @EntityGraph & Batch Size (완료)

**학습 기간**: 2026-01-18 (1일)  
**학습 시간**: 약 3시간  
**목표**: 추가 성능 최적화 방법 학습

**학습 내용**:
- @EntityGraph를 사용한 N+1 해결
- Batch Size 글로벌 설정
- 3가지 방법 성능 비교
- 실전 사용 가이드 작성

**실습 과제**:
- [x] @EntityGraph 구현 (2개 메서드)
- [x] Batch Size 설정 (100)
- [x] 성능 측정 및 비교
- [x] 문서 작성 (1,500 lines)

**성과**:
- ✅ 6개 성능 테스트 작성
- ✅ @EntityGraph 가장 빠름 (10ms)
- ✅ 3가지 방법 비교 완료
- ✅ 상황별 가이드 작성

**성능 비교 결과**:
```
📊 성능 순위:
   1위: @EntityGraph     - 10ms  ⭐
   2위: Fetch Join       - 13ms
   3위: Batch Size       - 15ms
```

**3가지 방법 비교**:
| 방법 | 쿼리 횟수 | 성능 | 코드 복잡도 | 추천도 |
|------|-----------|------|-------------|--------|
| @EntityGraph | 1번 | 10ms | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Fetch Join | 1번 | 13ms | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Batch Size | 2번 | 15ms | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

**실전 가이드**:
1. **일반적인 경우** → @EntityGraph (간결, 빠름, 페이징 가능)
2. **복잡한 쿼리** → Fetch Join (WHERE 조건, 명시적 제어)
3. **레거시 개선** → Batch Size (설정만으로 적용)

**학습 자료**:
- 📘 [PHASE3-3_HANDOVER.md](../docs/PHASE3-3_HANDOVER.md)

---

#### 📋 3-4. Comment 엔티티 (OneToMany) (예정)

**예정일**: 다음 세션  
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
#### 📋 3-6. Spring Security & JWT (예정)

---

## 📊 Phase 3 학습 현황

**전체 진행률**: 50% (3/6 완료) 🎉

```
✅ 3-1. JPA 연관 관계 매핑       [████████████] 100%
✅ 3-2. N+1 문제 해결            [████████████] 100%
✅ 3-3. @EntityGraph & Batch     [████████████] 100%
⬜ 3-4. Comment 엔티티           [            ]   0%
⬜ 3-5. 페이징 & 정렬            [            ]   0%
⬜ 3-6. Spring Security          [            ]   0%
```

**완료된 학습**:
- ✅ JPA 연관 관계 (@ManyToOne)
- ✅ FetchType.LAZY 전략
- ✅ N+1 문제 이해 및 해결
- ✅ Fetch Join 구현
- ✅ @EntityGraph 구현 ⭐
- ✅ Batch Size 설정
- ✅ 3가지 방법 성능 비교
- ✅ Query Method 자동 JOIN
- ✅ SQL 로깅 및 분석

**학습 중**:
- 🔄 OneToMany 관계 준비

**다음 학습**:
- 📅 Comment 엔티티
- 📅 양방향 매핑
- 📅 Collection Fetch

---

## 📈 학습 통계

### Phase 3 누적 현황

**코드**:
- Entity/DTO: ~600 lines
- 테스트: ~845 lines
- 총 코드: ~1,445 lines

**테스트**:
- 총 테스트: 63개
- 통과율: 100% ✅
- 커버리지: 유지

**성능 개선**:
- N+1 쿼리: 98% 감소 (51 → 1)
- 3가지 해결 방법 완성
- @EntityGraph 최고 성능 (10ms)

**문서**:
- JPA_LEARNING.md: ~1,000 lines
- PHASE3-1_HANDOVER.md: ~800 lines
- PHASE3-2_HANDOVER.md: ~1,200 lines
- PHASE3-3_HANDOVER.md: ~1,500 lines
- 총 문서: ~4,500 lines

**Pull Requests**:
- Phase 3-1: #1 (Merged ✅)
- Phase 3-2: #2 (Merged ✅)
- Phase 3-3: #3 (Merged ✅)

---

## 🎓 배운 점 정리

### Phase 3-3 핵심 교훈

1. **@EntityGraph가 최고!** ⭐
    - 코드가 가장 간결
    - 성능도 가장 빠름 (10ms)
    - 페이징과 함께 사용 가능
    - **일반적으로 가장 추천!**

2. **상황별로 선택하자**
    - 일반: @EntityGraph
    - 복잡한 쿼리: Fetch Join
    - 레거시: Batch Size

3. **성능 측정의 중요성**
    - H2는 차이 작음
    - 실제 DB는 네트워크 비용 큼
    - 항상 측정으로 검증

4. **JOIN 최적화**
    - @ManyToOne은 INNER JOIN 최적화
    - LEFT JOIN이 필요하면 명시
    - Hibernate가 똑똑함

---

## 📅 학습 일정 관리

### 주간 목표 (2026-01-19 ~ 2026-01-25)
- [ ] Phase 3-4 완료
- [ ] Comment 엔티티 구현
- [ ] OneToMany 양방향 매핑

### 월간 목표 (2026-01)
- [ ] Phase 3 전체 완료
- [ ] 80개 이상 테스트
- [ ] Security 기초 구현

---

<p align="center">
  <strong>꾸준함이 답이다 🔥</strong>
</p>