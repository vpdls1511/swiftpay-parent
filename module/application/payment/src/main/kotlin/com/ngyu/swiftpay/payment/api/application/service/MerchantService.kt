package com.ngyu.swiftpay.payment.api.application.service

import com.ngyu.swiftpay.core.domain.merchant.MerchantRepository
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.application.usecase.MerchantUseCase
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentApiKeyUseCase
import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MerchantService(
  private val paymentApiKeyUseCase: PaymentApiKeyUseCase,
  private val merchantRepository: MerchantRepository
) : MerchantUseCase {

  private val log = logger()

  /**
   * 가맹점 등록
   *
   * 상태 : PENDING
   */
  @Transactional
  override fun register(request: MerchantRegisterReqeust): PaymentCredentials {

    log.info("가맹점 등록 시작")
    val savedMerchant = merchantRepository.save(request.toDomain())
    log.info("가맹점 등록 완료 - merchantId = ${savedMerchant.id} STATUS = ${savedMerchant.status}")
    return this.approve(savedMerchant.id)
  }

  /**
   * 가맹점 승인 ( 관리자 )
   *
   * 상태 : PENDING → ACTIVE
   */
  override fun approve(merchantId: String): PaymentCredentials {
    log.info("가맹점 승인 시작 PENDING → ACTIVE")
    val domain = merchantRepository.findByMerchantId(merchantId)
    log.info("가맹점 승인 중.. STATUS = ${domain.status}")
    val approvedDomain = domain.approved(LocalDate.now().plusDays(1))
    val savedMerchant = merchantRepository.save(approvedDomain)
    log.info("가맹점 승인 완료 - merchantId = $merchantId STATUS = ${savedMerchant.status}")

    return paymentApiKeyUseCase.issueKey()
  }

}
