package com.ngyu.swiftpay.payment.application.service

import com.ngyu.swiftpay.core.common.exception.DuplicateMerchantException
import com.ngyu.swiftpay.core.common.exception.InvalidMerchantDataException
import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.merchant.Merchant
import com.ngyu.swiftpay.core.port.MerchantRepository
import com.ngyu.swiftpay.core.port.SequenceGenerator
import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MerchantService(
  private val apiCredentialsService: ApiCredentialsService,
  private val merchantRepository: MerchantRepository,
  private val sequenceGenerator: SequenceGenerator
) {

  private val log = logger()

  /**
   * 가맹점 등록
   *
   * 상태 : PENDING
   */
  @Transactional
  fun register(request: MerchantRegisterReqeust): MerchantRegisterResponseDto {
    try {
      log.info("가맹점 등록 시작")
      val merchantSeq = sequenceGenerator.nextMerchantId()
      val merchantId = Merchant.createMerchantId(merchantSeq)
      val merchant = request.toDomain(merchantSeq, merchantId)
      val savedMerchant = merchantRepository.save(merchant)

      log.info("가맹점 등록 완료 - merchantId = ${savedMerchant.merchantId} STATUS = ${savedMerchant.status}")
      val credentials = this.approve(savedMerchant.merchantId)
      return MerchantRegisterResponseDto(
        merchantId = savedMerchant.merchantId,
        merchantName = savedMerchant.businessName,
        credentials = credentials
      )
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
  fun approve(merchantId: String): PaymentCredentials {
    log.info("가맹점 승인 시작 PENDING → ACTIVE")

    val domain = merchantRepository.findByMerchantId(merchantId)
    log.info("가맹점 승인 중.. STATUS = ${domain.status}")

    val approvedDomain = domain.approved(LocalDate.now().plusDays(1))
    val savedMerchant = merchantRepository.save(approvedDomain)
    log.info("가맹점 승인 완료 - merchantId = $merchantId STATUS = ${savedMerchant.status}")

    return apiCredentialsService.issueKey()
  }

}
