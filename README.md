# SwiftPay - 결제 시스템

> Kotlin 기반 에스크로 결제 시스템 학습 프로젝트

---

## 📌 프로젝트 개요

**SwiftPay**는 에스크로(Escrow) 기반의 결제 시스템으로, 구매자와 판매자 사이의 안전한 거래를 중개합니다.  
헥사고날 아키텍처와 멀티 모듈 구조를 적용하여 도메인 중심 설계를 학습하기 위한 프로젝트입니다.

---

## 🛠️ 기술 스택

- **Language**: Kotlin 1.9+
- **Framework**: Spring Boot 3.x
- **Database**: MySQL (JPA + QueryDSL)
- **Cache**: Redis
- **Security**: Spring Security, HMAC
- **Build**: Gradle (Kotlin DSL)

---

## 🎯 학습 목표

이 프로젝트를 통해 다음 기술과 개념을 학습했습니다:

- ✅ **Kotlin + Spring Boot** - 코틀린 기반 백엔드 개발
- ✅ **Hexagonal Architecture (포트-어댑터)** - 도메인 중심 설계
- ✅ **멀티 모듈 아키텍처** - 계층별 모듈 분리
- ✅ **도메인 주도 설계(DDD)** - 불변 도메인 객체, 상태 전이 관리
- ✅ **전략 패턴** - 결제 수단별 처리 로직 분리
- ✅ **Redis 캐싱** - API Key 인증 성능 최적화
- ✅ **JPA + QueryDSL** - 데이터베이스 영속성 관리

---

## 🏗️ 시스템 아키텍처

### 모듈 구조
```
swiftpay-parent/
├── module/
│   ├── application/     # 애플리케이션 계층
│   │   ├── payment/     # 결제 서비스
│   │   └── auth/        # 인증 서비스
│   ├── core/            # 도메인 모델 + 포트 인터페이스
│   ├── infrastructure/  # 어댑터 구현 (DB, Redis, 외부 API)
│   ├── security/        # 보안 및 인증
│   └── common/          # 공통 유틸리티
```

### 헥사고날 아키텍처 적용
- **Core**: 도메인 모델과 포트(인터페이스) 정의
- **Infrastructure**: 포트의 실제 구현체 (JPA, Redis, External API)
- **Application**: 유스케이스 구현 및 비즈니스 로직 조율

---

## 💡 에스크로(Escrow) 시스템

### 개념
중개자(플랫폼)가 구매자와 판매자 사이에서 **결제금을 일시적으로 보관**하다가,
거래 완료 조건이 충족되면 판매자에게 정산하는 시스템입니다.

### 플로우
```
1. 구매자 결제 → 2. 에스크로 예치 → 3. 상품/서비스 제공 → 4. 구매 확정 → 5. 판매자 정산
```

### 핵심 기능
- **예치(Hold)**: 결제 승인 후 자금을 에스크로 계좌에 보관
- **정산(Settlement)**: 구매 확정 후 판매자에게 자금 송금 (수수료 차감)
- **환불(Refund)**: 취소/반품 시 구매자에게 환급

---

## 🔧 주요 구현 기능

### ✅ 완료된 기능

#### 1. 가맹점 관리
- 상점 등록 및 관리
- API Key 발급 및 인증 (HMAC 기반)
- Redis를 통한 API Key 캐싱

#### 2. 결제 처리
- 전략 패턴 기반 결제 수단 분리 (카드/계좌이체)
- Mock 외부 결제 API 클라이언트
- 결제 도메인 상태 전이 관리 (PENDING → IN_PROGRESS → SUCCEEDED/FAILED)

#### 3. 에스크로 시스템
- 결제 성공 시 자동 예치(Hold)
- 구매 확정 API를 통한 정산 준비

#### 4. 정산 시스템
- Settlement 도메인 설계
- 정산 스케줄러 (매일 오전 9시 실행)
- 수수료 계산 로직 (10% 고정)

#### 5. 도메인 주도 설계
- 불변 도메인 객체 (Payment, Escrow, Settlement)
- 도메인 이벤트 기반 상태 변경
- Value Object (Money, BankCode 등)

---

## 📊 개발 현황

### Phase 1 - 기본 인프라 ✅
- [x] 멀티 모듈 아키텍처 구성
- [x] 헥사고날 아키텍처 적용
- [x] 가맹점 등록 및 API Key 발급
- [x] API Key 기반 인증 필터
- [x] Redis 캐싱 구현

### Phase 2 - 결제 처리 ✅
- [x] 결제 요청 API
- [x] 전략 패턴 기반 결제 수단 처리
- [x] Mock 외부 결제 API 클라이언트
- [x] 결제 도메인 상태 관리

### Phase 3 - 에스크로 시스템 🔄 (부분 완료)
- [x] 결제 예치 (Hold)
- [x] 구매 확정 API
- [x] 정산 도메인 설계
- [x] 정산 스케줄러 기초 구현
- []정산 배치 실행 로직
- [] 환불 처리


---


## 📝 프로젝트 회고

### 배운 점
- 헥사고날 아키텍처를 통한 도메인 중심 설계의 이해
- 멀티 모듈 구조에서 의존성 관리의 중요성
- 도메인 객체의 불변성과 상태 전이 패턴
- 전략 패턴을 활용한 확장 가능한 설계

### 아쉬운 점
- 정산 배치 실행 로직 미완성
- 환불 처리 기능 미구현
- 이벤트 소싱 및 Kafka 미적용
- 실제 외부 결제 API 연동 부재

---

## 📂 주요 파일 구조

```
module/core/src/main/kotlin/com/ngyu/swiftpay/core/
├── domain/              # 도메인 모델
│   ├── payment/         # 결제 도메인
│   ├── escrow/          # 에스크로 도메인
│   ├── settlement/      # 정산 도메인
│   └── merchant/        # 가맹점 도메인
└── port/                # 포트 인터페이스
    ├── repository/      # 리포지토리 인터페이스
    └── client/          # 외부 API 클라이언트 인터페이스
```
