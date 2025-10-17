package com.ngyu.swiftpay.payment.api.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.order.Order
import com.ngyu.swiftpay.core.domain.payment.PayMethod
import com.ngyu.swiftpay.core.domain.payment.PaymentCardType
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "주문서 생성 요청 Dto")
data class OrderCreateRequestDto(
  val merchantId: String,
  val orderName: String,
  val totalAmount: Long,
  val currency: Currency,
  val customerName: String? = null,
  val customerEmail: String? = null,
  val customerPhone: String? = null
) {
  fun toDomain(): Order {
    return Order.create(
      merchantId = this.merchantId,
      orderName = this.orderName,
      totalAmount = this.totalAmount,
      currency = this.currency,
      customerName = this.customerName,
      customerEmail = this.customerEmail,
      customerPhone = this.customerPhone
    )
  }
}

@Schema(description = "주문서 생성 응답")
data class OrderCreateResponseDto(
  val orderId: String,
  val orderName: String,
  val amount: Long,
  val customerName: String? = null,
  val customerEmail: String? = null
) {
  companion object {
    fun fromDomain(domain: Order): OrderCreateResponseDto {
      return OrderCreateResponseDto(
        orderId = domain.orderId,
        orderName= domain.orderName,
        amount = domain.totalAmount.toLong(),
        customerName = domain.customerName,
        customerEmail = domain.customerEmail
      )
    }
  }
}

@Schema(description = "결제 요청 Dto")
data class PaymentRequestDto(
  // 기본정보
  val id: String,                     // 고유 ID
  val orderId: String,                // 가맹점의 주문번호
  val orderName: String,              // 상품 이름
  val amount: BigDecimal,             // 상품 가격
  val currency: String = "KRW",       // 통화

  // 결제 수단 정보
  val method: PayMethod,              // 결제 수단
  @Schema(
    description = "결제 수단별 상세 정보",
    oneOf = [
      PaymentDtoMethodDetails.Card::class,
      PaymentDtoMethodDetails.BankTransfer::class,
    ],
    discriminatorProperty = "type"
  )
  val methodDetail: PaymentDtoMethodDetails, // 결제 상세정보 ( CARD일 경우, 옵션 )

  // 옵션 - 콜백 URL
  val callBack: PaymentCallback? = null,
)

@Schema(description = "결제 후 콜백 URL")
data class PaymentCallback(
  @field:Schema(description = "결제 성공 URL", example = "https://example.com/success", required = false)
  val successUrl: String? = null,

  @field:Schema(description = "결제 취소 URL", example = "https://example.com/cancel", required = false)
  val cancelUrl: String? = null,

  @field:Schema(description = "결제 실패 URL", example = "https://example.com/failure", required = false)
  val failureUrl: String? = null
)

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes(
  JsonSubTypes.Type(value = PaymentDtoMethodDetails.Card::class, name = "CARD"),
  JsonSubTypes.Type(value = PaymentDtoMethodDetails.BankTransfer::class, name = "BANK_TRANSFER")
)
sealed class PaymentDtoMethodDetails {

  @Schema(description = "카드 결제 옵션")
  data class Card(
    val cardNumber: String?,
    val cardExpiry: String?,
    val cardCvc: String?,
    val installmentPlan: Int? = 0,
    val cardType: PaymentCardType?,
    val useCardPoint: Boolean = false
  ) : PaymentDtoMethodDetails()

  @Schema(description = "계좌 이체 옵션")
  data class BankTransfer(
    val bankCode: String?,
    val accountNumber: String?
  ) : PaymentDtoMethodDetails()
}
