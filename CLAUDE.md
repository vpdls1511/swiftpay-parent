# SwiftPay 프로젝트 가이드

## 프로젝트 개요

SwiftPay는 **헥사고날 아키텍처** 기반의 간편결제 시스템 구현 프로젝트입니다.
결제/정산 도메인 전문성 확보와 금융 시스템 안정성 보장 경험을 목표로 합니다.

## 기술 스택

- **Language**: Kotlin 1.9.25
- **Runtime**: JVM 17
- **Framework**: Spring Boot 3.5.6
- **Build Tool**: Gradle 8.14+ (Kotlin DSL)
- **Async**: Coroutine
- **Database**: MySQL, Redis
- **Infrastructure**: K3s, Jenkins, Kaniko
- **Message Queue**: Kafka (예정)

## 아키텍처

### 헥사고날 아키텍처 (Ports & Adapters)

- **Domain Layer**: 비즈니스 로직 및 엔티티
- **Application Layer**: 유스케이스 구현
- **Adapter Layer**:
  - Inbound: REST API, Controller
  - Outbound: DB, 외부 API 연동

### 멀티모듈 구조

```
swiftpay-parent/
├── module/
│   ├── application/
│   │   ├── auth/         # 인증/회원 서비스
│   │   └── payment/      # 결제 서비스
│   ├── core/             # 핵심 도메인 로직
│   ├── security/         # 보안 관련
│   └── common/           # 공통 설정 및 유틸리티
```

## 모듈 설명

### `module:core`
- 도메인 예외 처리 (SwiftException, DomainException 등)
- Value Object (Money 등)
- 로깅 인프라 (RequestIdFilter, Logger Extension)
- 전역 예외 핸들러 (GlobalExceptionHandler)

### `module:common`
- 공통 설정 (BaseConfig, DbConfig, QueryDslConfig)
- 보안 설정 (SecurityConfig, BaseAuthenticationFilter)
- Redis 설정 (RedisConfig)
- Swagger 설정 (BaseSwaggerConfig)

### `module:application:auth`
- 회원 가입/로그인
- JWT 인증
- API Key 관리

### `module:application:payment`
- 결제 요청/승인/취소
- 카드 등록 및 토큰화
- PG/VAN 연동

## 코딩 컨벤션

### Kotlin 스타일
- 패키지명: lowercase (예: `com.ngyu.swiftpay`)
- 클래스명: PascalCase
- 함수/변수명: camelCase
- 상수: UPPER_SNAKE_CASE

### 예외 처리
- 도메인별 커스텀 예외 사용 (DomainException, PaymentException, PrincipalException 등)
- SwiftError 인터페이스를 통한 에러 코드 관리
- GlobalExceptionHandler를 통한 중앙화된 예외 처리

### 로깅
- SLF4J + Logback 사용
- RequestIdFilter를 통한 요청 추적
- Logger Extension 활용

## 개발 로드맵

### Phase 1 - 인증/회원 (진행중)
- ✅ 멀티모듈 + 헥사고날 아키텍처
- ✅ Exception 처리 인프라
- ⬜ 회원 가입/로그인, JWT 인증

### Phase 2 - 결제
- ⬜ 결제 요청 API
- ⬜ PG/VAN Mock 서버
- ⬜ 결제 승인 처리

### Phase 3 - 에스크로
- ⬜ 결제 예치/정산 배치
- ⬜ 환불 처리

### Phase 4 - 안정성
- ⬜ 멱등성/분산 트랜잭션
- ⬜ 이벤트 소싱

## 핵심 기능

### 인증/회원
- 회원 가입/로그인
- JWT 인증
- API Key 관리

### 결제
- 카드 등록 및 토큰화
- PG/VAN 연동
- 결제 승인/취소

### 에스크로
- 결제 예치
- 구매 확정
- 판매자 정산
- 환불 처리

### 안정성
- 멱등성 키
- 트랜잭션 원자성
- 분산 락
- 이벤트 소싱

## 개발 환경

### 서버 환경
- On-Premise K3s (N200 / 16GB)
- Pod 리소스 제한: 1vCPU / 1GB

### 빌드 & 실행
```bash
# 빌드
./gradlew build

# 테스트
./gradlew test

# 특정 모듈 실행
./gradlew :module:application:auth:bootRun
```

## 주요 디렉토리

```
module/
├── core/src/main/kotlin/com/ngyu/swiftpay/core/
│   ├── exception/          # 예외 클래스
│   ├── logger/             # 로깅 유틸리티
│   └── vo/                 # Value Object
│
├── common/src/main/kotlin/me/ngyu/swiftpay/common/
│   ├── config/             # 공통 설정
│   ├── security/           # 보안 설정
│   └── redis/              # Redis 설정
│
└── application/
    ├── auth/               # 인증 서비스
    └── payment/            # 결제 서비스
```

## 주의사항

### 금융 시스템 특성
- 트랜잭션의 원자성 보장 필수
- 멱등성 키를 통한 중복 요청 방지
- 모든 금액 처리는 Money VO 사용
- 민감 정보(카드번호 등)는 암호화/토큰화

### 코드 작성 시
- 비즈니스 로직은 Domain Layer에 위치
- Controller는 얇게, Service에 로직 집중
- 외부 의존성은 인터페이스로 추상화
- 테스트 코드 필수 작성

## 참고 문서
- README.md: 프로젝트 개요 및 로드맵
- HELP.md: Spring Boot 관련 도움말
