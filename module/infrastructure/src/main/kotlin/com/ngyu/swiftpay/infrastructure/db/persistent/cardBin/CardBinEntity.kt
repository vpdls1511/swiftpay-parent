package com.ngyu.swiftpay.infrastructure.db.persistent.cardBin

import com.ngyu.swiftpay.core.domain.payment.PaymentCardType
import jakarta.persistence.*

@Entity
@Table(name = "card_bin")
class CardBinEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(nullable = false, length = 8, unique = true)
  val bin: String,

  @Column(nullable = false, length = 50)
  val issuer: String,

  @Column(length = 50)
  val slipParameterName: String? = null,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val ownerType: CardOwnerType,

  @Column(length = 30)
  val brand: String? = null,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val cardType: PaymentCardType,

  @Column(columnDefinition = "TEXT")
  val changeHistory: String? = null,

  @Column(columnDefinition = "TEXT")
  val remark: String? = null,
)

enum class CardOwnerType(
  val description: String,
  val code: String
) {
  PERSONAL("개인", "PERSONAL"),
  CORPORATE("법인", "CORPORATE"),
  ;

  companion object {
    fun fromCode(code: String): CardOwnerType? {
      return entries.find { it.code == code }
    }

    fun fromDescription(description: String): CardOwnerType? {
      return entries.find { it.description == description }
    }
  }
}
