package com.ngyu.swiftpay.infrastructure.db.persistent.merchant

import com.ngyu.swiftpay.core.domain.merchant.Merchant

object MerchantMapper {
  fun toEntity(domain: Merchant): MerchantEntity {
    return MerchantEntity(
      id = domain.id,
      merchantId = domain.merchantId,
      userId = domain.userId,
      businessNumber = domain.businessNumber,
      businessName = domain.businessName,
      representativeName = domain.representativeName,
      businessType = domain.businessType,
      email = domain.email,
      phoneNumber = domain.phoneNumber,
      address = domain.address,
      bankAccountNumber = domain.bankAccountNumber,
      feeRate = domain.feeRate,
      settlementCycle = domain.settlementCycle,
      contractStartDate = domain.contractStartDate,
      contractEndDate = domain.contractEndDate,
      status = domain.status,
      settleWebhookUrl = domain.settleWebhookUrl,
      approvedAt = domain.approvedAt
    )
  }

  fun toDomain(entity: MerchantEntity): Merchant {
    return Merchant(
      id = entity.id,
      merchantId = entity.merchantId,
      userId = entity.userId,
      businessNumber = entity.businessNumber,
      businessName = entity.businessName,
      representativeName = entity.representativeName,
      businessType = entity.businessType,
      email = entity.email,
      phoneNumber = entity.phoneNumber,
      address = entity.address,
      bankAccountNumber = entity.bankAccountNumber,
      feeRate = entity.feeRate,
      settlementCycle = entity.settlementCycle,
      status = entity.status,
      settleWebhookUrl = entity.settleWebhookUrl,
      contractStartDate = entity.contractStartDate,
      contractEndDate = entity.contractEndDate,
      createdAt = entity.createdAt,
      updatedAt = entity.updatedAt,
      approvedAt = entity.approvedAt
    )
  }
}
