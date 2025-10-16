package com.ngyu.swiftpay.payment.api.application.service

import com.ngyu.swiftpay.core.domain.merchant.MerchantRepository
import com.ngyu.swiftpay.payment.api.application.usecase.MerchantUseCase
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentApiKeyUseCase
import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MerchantService(
  private val paymentApiKeyUseCase: PaymentApiKeyUseCase,
  private val merchantRepository: MerchantRepository
): MerchantUseCase {
  /**
   * 가맹점 등록
   *
   * 상태 : PENDING
   */
  override fun register(request: MerchantRegisterReqeust): PaymentCredentials {
    val savedMerchant = merchantRepository.save(request.toDomain())

    return approve(savedMerchant.id)
  }

  /**
   * 가맹점 승인 ( 관리자 )
   *
   * 상태 : PENDING → ACTIVE
   */
  override fun approve(merchantId: String): PaymentCredentials {
    val domain = merchantRepository.findByMerchantId(merchantId)
    domain.approved(LocalDate.now().plusDays(1))
    merchantRepository.save(domain)

    return paymentApiKeyUseCase.issueKey()
  }

}
