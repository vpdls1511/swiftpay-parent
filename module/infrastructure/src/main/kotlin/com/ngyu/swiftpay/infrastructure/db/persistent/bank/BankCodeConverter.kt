package com.ngyu.swiftpay.infrastructure.db.persistent.bank

import com.ngyu.swiftpay.core.domain.bank.BankCode
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class BankCodeConverter : AttributeConverter<BankCode, String> {

  override fun convertToDatabaseColumn(attribute: BankCode?): String? {
    return attribute?.code
  }

  override fun convertToEntityAttribute(dbData: String?): BankCode? {
    return dbData?.let { code ->
      BankCode.entries.find { it.code == code }
        ?: throw IllegalStateException("은행 코드 없음: $code")
    }
  }
}
