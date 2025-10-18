package com.ngyu.swiftpay.payment.application.service

import com.ngyu.swiftpay.core.domain.merchant.MerchantRepository
import com.ngyu.swiftpay.core.exception.DuplicateMerchantException
import com.ngyu.swiftpay.core.exception.InvalidMerchantDataException
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.application.usecase.MerchantUseCase
import com.ngyu.swiftpay.payment.application.usecase.PaymentApiKeyUseCase
import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
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
    try {
      log.info("가맹점 등록 시작")
      val savedMerchant = merchantRepository.save(request.toDomain())

      log.info("가맹점 등록 완료 - merchantId = ${savedMerchant.id} STATUS = ${savedMerchant.status}")
      return this.approve(savedMerchant.id)
    } catch (e: DataIntegrityViolationException) {
      if (e.message?.contains("Duplicate entry") == true) {
        log.error("이미 등록된 사업자 번호 - businessNumber = ${request.businessNumber}")
        throw DuplicateMerchantException()
      }
      log.error("가맹점 정보 저장 실패", e)
      throw InvalidMerchantDataException()
    } catch (e: Exception) {
      log.error("가맹점 정보 저장 중 알 수 없는 오류 발생", e)
      throw e
    }
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
