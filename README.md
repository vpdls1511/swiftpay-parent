<div align="center">

# SwiftPay

**간편결제 시스템 구현 프로젝트**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![JVM](https://img.shields.io/badge/JVM-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5+-6DB33F?logo=springboot&logoColor=white)](https://spring.io)
[![Gradle](https://img.shields.io/badge/Gradle-8.14+-02303A?logo=gradle&logoColor=white)](https://gradle.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

---

## 📖 개요

결제/정산 도메인 전문성 확보와 금융 시스템 안정성 보장 경험을 위한 프로젝트입니다.  
헥사고날 아키텍처 기반으로 실무 수준의 간편결제 시스템을 구현합니다.

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **Backend** | Kotlin, Spring Boot, Coroutine |
| **Infrastructure** | K3s, Jenkins, Kaniko |
| **Storage** | MySQL, Redis |
| **Message** | Kafka (예정) |

**서버 환경**
```
On-Premise K3s (N200 / 16GB)
Pod 리소스 제한: 1vCPU / 1GB
```

## 💡 핵심 기능

### 인증/회원
회원 가입/로그인, JWT 인증, API Key 관리

### 결제
카드 등록 및 토큰화, PG/VAN 연동, 결제 승인/취소

### 에스크로
```mermaid
graph LR
    A[결제 예치] --> B[구매 확정]
    B --> C[판매자 정산]
    C --> D[환불 처리]
```

### 안정성
멱등성 키 | 트랜잭션 원자성 | 분산 락 | 이벤트 소싱

## 🗺 개발 로드맵
```
Phase 1 - 인증/회원 (진행중)
  ✅ 멀티모듈 + 헥사고날 아키텍처
  ✅ Exception 처리 인프라
  ⬜ 회원 가입/로그인, JWT 인증

Phase 2 - 결제
  ⬜ 결제 요청 API
  ⬜ PG/VAN Mock 서버
  ⬜ 결제 승인 처리

Phase 3 - 에스크로
  ⬜ 결제 예치/정산 배치
  ⬜ 환불 처리

Phase 4 - 안정성
  ⬜ 멱등성/분산 트랜잭션
  ⬜ 이벤트 소싱
```

