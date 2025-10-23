# SwiftPay - 결제시스템

> Claude를 이용한 코드리뷰 결과..
---

## 📊 코드 리뷰 결과 (2025.10.24)

### 아키텍처 평가
- **구조**: Hexagonal Architecture 시도 (core → application → infrastructure)
- **현황**: 방향은 맞으나 포트/어댑터 경계가 일부 흐릿함
- **강점**: VO 설계 우수 (Money), 도메인 로직 엔티티 내 위치
- **약점**: 트랜잭션 경계, SRP 위반, data class 남용

### SOLID 원칙 준수도
| 원칙 | 평가 | 개선 필요 사항 |
|------|------|----------------|
| **SRP** | ⭐⭐ | PaymentService 책임 과다 |
| **OCP** | ⭐⭐ | 전략 패턴 미사용 |
| **LSP** | ⭐⭐⭐⭐ | 양호 |
| **ISP** | ⭐⭐⭐⭐ | UseCase 분리 우수 |
| **DIP** | ⭐⭐⭐ | 포트/어댑터 구조 양호 |

### DDD 평가 (4/10)
**✅ 잘한 것**
- Money, Currency VO 설계 우수
- 엔티티 내 비즈니스 로직 배치
- Bounded Context 분리 시도

**❌ 개선 필요**
- **data class 남용**: 엔티티에 data class 사용 → ID 기반 동일성 구현 필요
- **Aggregate 경계 불명확**: Payment-Escrow-Order 관계 재설계
- **도메인 이벤트 부재**: 결제 완료 → 정산 시작 이벤트 필요
- **인프라 관심사 침투**: ID 생성, 시간 의존성이 도메인에 포함

### 페이먼츠 도메인 이해도 (5/10)
**✅ 이해하고 있음**
- 에스크로 개념 (Hold → Settle/Refund)
- 결제 상태 관리
- 멱등성 키 필요성

**❌ 추가 학습 필요**
- 정산 주기 전략 (일별/실시간)
- 부분 취소 처리
- PG사 타임아웃 대응
- 분산 트랜잭션 (Saga 패턴)
- 정산 실패 시 재시도 전략

---

## 🔧 리팩토링 로드맵

### ⚠️ P0 - Critical
**기능 추가 전 반드시 해결해야 할 치명적 이슈**

#### 1. DDD Entity 재설계 ⭐⭐⭐
```kotlin
// AS-IS (문제)
data class Payment(val id: Long?, ...) // 모든 필드 비교

// TO-BE (해결)
class Payment(val id: Long?, ...) {
    override fun equals(other: Any?) = 
        other is Payment && id == other.id  // ID만 비교
    override fun hashCode() = id.hashCode()
}
```
**대상**: Payment, Order, Escrow, Merchant, Settlement  
**이유**: 엔티티 동일성 보장 (DDD 핵심)

#### 2. PaymentService 책임 분리 ⭐⭐⭐
```kotlin
// AS-IS (문제)
class PaymentService(
    orderRepo, paymentRepo, escrowService, strategyFactory
) {
    fun readyOrder() { ... }      // 주문 생성
    fun processing() { ... }      // 결제 처리
}

// TO-BE (해결)
class CreateOrderUseCase(orderRepo)
class ProcessPaymentUseCase(paymentRepo, escrowService)
class ConfirmEscrowUseCase(escrowRepo)
```
**이유**: SRP 위반, 테스트 어려움, 의존성 과다

#### 3. 트랜잭션 경계 재설계 ⭐⭐⭐
```kotlin
// AS-IS (문제)
@Transactional
fun processing() {
    val payment = savePayment()      // 커밋됨
    val result = processPayment()    // 여기서 실패 시?
    return result
}

// TO-BE (해결)
@Transactional
fun processing() {
    val payment = payment.inProgress()
    try {
        escrowService.hold(payment)
        paymentRepo.save(payment.success())
    } catch (e: PaymentException) {
        paymentRepo.save(payment.failed())
        throw e
    }
}
```
**이유**: 데이터 정합성 (결제는 IN_PROGRESS인데 에스크로 없는 상황 발생)

#### 4. 도메인 예외 계층 구조 ⭐⭐
```kotlin
sealed class PaymentException(message: String) : RuntimeException(message)
class PaymentNotFoundException : PaymentException("결제를 찾을 수 없음")
class PaymentProcessingException : PaymentException("결제 처리 실패")
class EscrowHoldException : PaymentException("에스크로 예치 실패")
```
**이유**: Exception 대신 구체적 예외, 도메인 언어 사용

---

### 🔥 P1 - High
**아키텍처 개선 및 확장성 확보**

#### 5. ID 생성 외부화 ⭐⭐
```kotlin
// core/domain/port
interface SequenceGenerator {
    fun generatePaymentId(): String
    fun generateEscrowId(): String
}

// infrastructure
@Component
class SnowflakeSequenceGenerator : SequenceGenerator {
    override fun generatePaymentId() = 
        "swift_pay_${snowflake.nextId()}"
}
```
**이유**: 테스트 가능성, 인프라 관심사 분리

#### 6. 전략 패턴 활성화 ⭐⭐
```kotlin
// AS-IS
// TODO - 전략패턴으로 나누자.. (주석만 있음)

// TO-BE
@Transactional
fun processing(request: PaymentRequestDto): PaymentResponseDto {
    val payment = savePayment(request)
    val strategy = strategyFactory.getStrategy(payment)
    return strategy.process(payment)
}
```
**대상**: PaymentCardStrategy, PaymentBankStrategy 실제 구현

#### 7. Aggregate Root 명확화 ⭐⭐
```
Payment (Root)
  └── PaymentMethodDetails (VO)
  
Order (Root)
  └── Money (VO)
  
Escrow (Root)
  └── Money (VO)
```
**규칙**:
- Repository는 Aggregate Root에만
- Root 외 접근은 Root를 통해서만

#### 8. Repository 메서드 재설계 ⭐
```kotlin
// AS-IS
fun findByPayment(domain: Payment): Payment

// TO-BE
fun findById(id: Long): Payment?
fun findByPaymentId(paymentId: String): Payment?
fun findByMerchantIdAndOrderId(merchantId: String, orderId: String): Payment?
```

---

### 🎯 P2 - Medium
**도메인 완성도 향상**

#### 9. 도메인 이벤트 도입 ⭐⭐
```kotlin
sealed interface PaymentEvent {
    val paymentId: String
    val occurredAt: LocalDateTime
}

data class PaymentSucceededEvent(
    override val paymentId: String,
    val amount: Money,
    override val occurredAt: LocalDateTime
) : PaymentEvent

// Payment 엔티티
class Payment {
    private val events = mutableListOf<PaymentEvent>()
    
    fun success(): Payment {
        val updated = copy(status = SUCCEEDED)
        events.add(PaymentSucceededEvent(paymentId, amount, now()))
        return updated
    }
}
```

#### 10. TaxPolicy 추상화 ⭐
```kotlin
interface TaxPolicy {
    fun calculateTax(amount: Money): Money
}

class KoreaTaxPolicy : TaxPolicy {
    override fun calculateTax(amount: Money) = amount / 11
}
```

#### 11. 부분 취소 로직 ⭐
```kotlin
// Order
fun partialRefund(amount: Money): Order {
    require(balanceAmount >= amount)
    return copy(
        balanceAmount = balanceAmount - amount,
        status = if (balanceAmount == amount) REFUNDED else PARTIAL_REFUNDED
    )
}
```

---

### 🔮 P3 - Low
**고급 기능 및 안정성**

#### 12. 멱등성 검증
- Redis 기반 idempotencyKey 검증
- 동일 요청 중복 처리 방지

#### 13. 정산 배치 설계
- Spring Batch 또는 Quartz
- 실패 시 재시도 로직

#### 14. Saga 패턴
- 결제 성공 → 재고 차감 → 포인트 적립
- 보상 트랜잭션 구현

#### 15. 이벤트 소싱
- 모든 상태 변경을 이벤트로 저장
- CQRS 도입 검토

---

## 개발 진척도

### Phase 1 - 기본 인프라 ✅
- [x] 상점 등록 및 API Key 발급
- [x] 결제 요청 API

### Phase 2 - 에스크로 🔄
- [x] 결제 예치 (Hold)
- [ ] 구매 확정 처리
- [ ] 판매자 정산 배치
- [ ] 환불 처리

### Phase 2.5 - 리팩토링 (NEW) 🚀
**1주차 (P0)**
- [ ] Entity data class → class 변환
- [ ] PaymentService 분리
- [ ] 트랜잭션 경계 수정
- [ ] 도메인 예외 추가

**2주차 (P1)**
- [ ] SequenceGenerator 포트
- [ ] 전략 패턴 구현
- [ ] Aggregate Root 재설계
- [ ] Repository 메서드 정리

**3-4주차 (P2)**
- [ ] 도메인 이벤트
- [ ] TaxPolicy 추상화
- [ ] 부분 취소 로직

### Phase 3 - 안정성
- [ ] 멱등성 키 기반 중복 방지
- [ ] 분산 트랜잭션 처리 (Saga)
- [ ] 정산 실패 재시도 로직
- [ ] 이벤트 소싱 도입

---

## 📚 학습 자료
- [ ] Eric Evans - Domain-Driven Design (특히 Aggregate 장)
- [ ] Vaughn Vernon - Implementing Domain-Driven Design
- [ ] Martin Fowler - Patterns of Enterprise Application Architecture
- [ ] "토스 SLASH 21 - 실시간 이체 개발기" 발표 자료

---

## 💡 개발 원칙
1. **Entity는 class, VO는 data class**
2. **하나의 UseCase는 하나의 책임**
3. **트랜잭션 경계 = UseCase 경계**
4. **도메인 예외만 catch, 시스템 예외는 전파**
5. **인프라 관심사는 포트로 추상화**
