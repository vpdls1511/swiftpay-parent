package com.ngyu.swiftpay.core.domain

import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.core.vo.Money
import com.ngyu.swiftpay.core.domain.order.Order
import com.ngyu.swiftpay.core.domain.order.OrderStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@DisplayName("Order 도메인 테스트")
class OrderTest {

  lateinit var order: Order

  @BeforeEach
  fun setUp() {
    order = Order.create(
      merchantId = UUID.randomUUID().toString(),
      orderName = "테스트 상품",
      totalAmount = 110000L,
      currency = Currency.KRW
    )
  }

  @Test
  @DisplayName("주문 생성 시 초기 상태는 READY이다")
  fun createOrderTest() {
    assertThat(order.status).isEqualTo(OrderStatus.READY)
    assertThat(order.balanceAmount).isEqualTo(Money.won(110000))
  }

  @Test
  @DisplayName("세금은 총액의 1/11이다")
  fun taxCalculationTest() {
    assertThat(order.tax).isEqualTo(Money.won(10000))
    assertThat(order.supplyAmount).isEqualTo(Money.won(100000))
  }

  @Test
  @DisplayName("READY 상태에서 processing 호출 시 PROCESSING 상태로 변경된다")
  fun processingTest() {
    val processed = order.processing()
    assertThat(processed.status).isEqualTo(OrderStatus.PROCESSING)
  }

  @Test
  @DisplayName("PROCESSING 상태에서 done 호출 시 DONE 상태로 변경된다")
  fun doneTest() {
    val done = order.processing().done()
    assertThat(done.status).isEqualTo(OrderStatus.DONE)
  }

  @Test
  @DisplayName("PROCESSING 상태에서 cancel 호출 시 CANCELLED 상태로 변경된다")
  fun cancelTest() {
    val cancelled = order.processing().cancel()
    assertThat(cancelled.status).isEqualTo(OrderStatus.CANCELLED)
  }

  @Test
  @DisplayName("DONE 상태에서 부분 환불 시 PARTIAL_REFUNDED 상태로 변경된다")
  fun partialRefundTest() {
    val done = order.processing().done()
    val refunded = done.refund(Money.won(55000))

    assertThat(refunded.status).isEqualTo(OrderStatus.PARTIAL_REFUNDED)
    assertThat(refunded.balanceAmount).isEqualTo(Money.won(55000))
  }

  @Test
  @DisplayName("DONE 상태에서 전액 환불 시 REFUNDED 상태로 변경된다")
  fun fullRefundTest() {
    val done = order.processing().done()
    val refunded = done.refund(Money.won(110000))

    assertThat(refunded.status).isEqualTo(OrderStatus.REFUNDED)
    assertThat(refunded.balanceAmount).isEqualTo(Money.ZERO)
  }

  @Test
  @DisplayName("환불 가능 금액을 초과하여 환불할 수 없다")
  fun exceedRefundAmountTest() {
    val done = order.processing().done()

    assertThrows<IllegalArgumentException> {
      done.refund(Money.won(120000))
    }
  }

  @Test
  @DisplayName("READY 상태가 아닌데 processing 호출 시 예외가 발생한다")
  fun invalidStateTransitionTest() {
    val processed = order.processing()

    assertThrows<IllegalArgumentException> {
      processed.processing()
    }
  }
}
