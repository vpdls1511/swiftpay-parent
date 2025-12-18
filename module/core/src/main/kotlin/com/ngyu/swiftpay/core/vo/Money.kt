package com.ngyu.swiftpay.core.vo

import com.ngyu.swiftpay.core.common.logger.logger
import java.math.BigDecimal
import java.math.RoundingMode

data class Money(
  val amount: BigDecimal,
  val currency: Currency
) {
  private val log = logger()

  companion object {

    /**
     * 원화(KRW) Money 생성
     */
    fun won(amount: Long): Money = Money(BigDecimal.valueOf(amount), Currency.KRW)
    fun won(amount: Int): Money = won(amount.toLong())

    /**
     * Money.won(0) 이 아닌 Money.ZERO 를 이용하여 더 직관적인 표현을 할 수 있게 한다.
     */
    val ZERO: Money = won(0)
  }

  /**
   * '+' operator 를 이용한 연산이 가능
   */
  operator fun plus(other: Money): Money {
    require(other.currency == this.currency) { "통화가 다릅니다." }
    return Money(this.amount + other.amount, this.currency)
  }
  /**
   * '-' operator 를 이용한 연산이 가능
   */
  operator fun minus(other: Money): Money {
    require(other.currency == this.currency) { "통화가 다릅니다." }
    return Money(this.amount - other.amount, this.currency)
  }

  operator fun div(divider: Long): Money {
    require(divider != 0L) { "0으로 나눌 수 없습니다." }
    return Money(
      this.amount.divide(BigDecimal.valueOf(divider), this.currency.decimalFormat, RoundingMode.HALF_UP),
      this.currency
    )
  }

  operator fun div(other: Money): BigDecimal {
    require(other.currency == this.currency) { "통화가 다릅니다." }
    return this.amount.divide(other.amount, other.currency.decimalFormat, RoundingMode.HALF_UP)
  }

  /**
   * '==, <= , >=' 같은 비교 operator를 사용 가능
   * - a < b : -1
   * - a == b : 0
   * - a > b : 1
   * @return Int
   */
  operator fun compareTo(other: Money): Int {
    require(other.currency == this.currency) { "통화가 다릅니다." }
    return this.amount.compareTo(other.amount)
  }

  /**
   * 비율을 곱한 금액 계산
   *
   * 사용 예시:
   * - 수수료 계산: amount.multiply(0.03)
   * - 할인 계산: price.multiply(0.20)
   *
   * @param rate 곱할 비율 (0.03 = 3%)
   * @return 계산된 금액
   */
  fun multiply(rate: BigDecimal): Money {
    return Money(
      this.amount
        .multiply(rate)
        .setScale(this.currency.decimalFormat, RoundingMode.HALF_UP),
      this.currency
    )
  }

  fun toLong(): Long = this.amount.toLong()
  fun toBigDecimal(): BigDecimal = this.amount
}

enum class Currency(
  val code: String,
  val decimalFormat: Int,
  val symbol: String,
) {
  KRW("KRW", 0, "₩")
}
