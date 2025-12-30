package com.ngyu.swiftpay.payment.application.service.merchant

import com.ngyu.swiftpay.core.common.exception.DuplicateMerchantException
import com.ngyu.swiftpay.core.common.exception.InvalidMerchantDataException
import com.ngyu.swiftpay.core.common.exception.MerchantNotFoundException
import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.merchant.Merchant
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.MerchantRepository
import com.ngyu.swiftpay.payment.api.controller.dto.MerchantInfoRequest
import com.ngyu.swiftpay.payment.api.controller.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.controller.dto.MerchantRegisterResponseDto
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentCredentials
import com.ngyu.swiftpay.payment.application.auth.ApiCredentialsService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class MerchantService(
  private val apiCredentialsService: ApiCredentialsService,
  private val merchantRepository: MerchantRepository,
  private val sequenceGenerator: SequenceGenerator
) {

  private val log = logger()

  @Transactional
  fun getMerchantInfo(
    principal: PaymentCredentials,
    request: MerchantInfoRequest
  ): MerchantRegisterResponseDto {
    log.info("가맹점 정보 찾기 시작 merchantId: ${request.merchantId}")
    if (principal.apiKey.isEmpty() || principal.apiPairKey.isEmpty()) {
      log.error("api key가 존재하지 않습니다.")
      throw MerchantNotFoundException()
    }

    val domain = merchantRepository.findByMerchantId(request.merchantId)
    val credentials = apiCredentialsService.reissueToken(domain.id, principal.apiPairKey)

    return MerchantRegisterResponseDto(
      merchantId = domain.merchantId,
      merchantName = domain.businessName,
      credentials = credentials,
    )
  }

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

    return apiCredentialsService.issueKey(domain.id)
  }

}
