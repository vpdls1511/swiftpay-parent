package com.ngyu.swiftpay.core.domain.money

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(
  val amount: BigDecimal,
  val currency: Currency
) {
  companion object {

    /**
     * 원화(KRW) Money 생성
     */
    fun won(amount: Long):Money = Money(BigDecimal.valueOf(amount), Currency.KRW)
    fun won(amount: Int):Money = this.won(amount.toLong())

    /**
     * Money.won(0) 이 아닌 Money.ZERO 를 이용하여 더 직관적인 표현을 할 수 있게 한다.
     */
    val ZERO: Money = this.won(0)
  }

  /**
   * '+' operator 를 이용한 연산이 가능
   */
  operator fun plus(other: Money): Money {
    require(other.currency == this.currency) { "통화가 다릅니다." }
    return Money(other.amount + this.amount, this.currency)
  }

  /**
   * '-' operator 를 이용한 연산이 가능
   */
  operator fun minus(other: Money): Money {
    require(other.currency == this.currency) { "통화가 다릅니다." }
    return Money(this.amount - other.amount, this.currency)
  }

  /**
   * 수수료 계산을 위한 기능
   */
  fun multiply(rate: BigDecimal): Money {
    return Money(
      this.amount
        .multiply(rate)
        .setScale(this.currency.decimalFormat, RoundingMode.HALF_UP),
      this.currency
    )
  }
}

enum class Currency(
  val code: String,
  val decimalFormat: Int,
  val symbol: String,
) {
  KRW("KRW", 0, "₩")
}
