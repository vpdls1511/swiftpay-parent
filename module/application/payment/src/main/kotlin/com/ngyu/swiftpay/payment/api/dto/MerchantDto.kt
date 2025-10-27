package com.ngyu.swiftpay.payment.api.dto

import com.ngyu.swiftpay.core.domain.merchant.Merchant

data class MerchantRegisterReqeust(
  val businessNumber: String,
  val businessName: String,
  val representativeName: String,
  val businessType: String,
  val email: String,
  val phoneNumber: String,
  val address: String,
  val bankAccountNumber: String
) {
  fun toDomain(seq: Long, merchantId: String): Merchant {
    return Merchant(
      id = seq,
      merchantId = merchantId,
      businessNumber = this.businessNumber,
      businessName = this.businessName,
      representativeName = this.representativeName,
      businessType = this.businessType,
      bankAccountNumber = this.bankAccountNumber,
      email = this.email,
      phoneNumber = this.phoneNumber,
      address = this.address,
    )
  }
}


data class MerchantRegisterResponseDto(
  val merchantId: String,
  val merchantName: String,
  val credentials: PaymentCredentials
)
