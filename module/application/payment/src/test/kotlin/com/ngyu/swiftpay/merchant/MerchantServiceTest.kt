package com.ngyu.swiftpay.payment.application.service

import com.ngyu.swiftpay.core.common.exception.DuplicateMerchantException
import com.ngyu.swiftpay.core.common.exception.InvalidMerchantDataException
import com.ngyu.swiftpay.core.domain.merchant.Merchant
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.MerchantRepository
import com.ngyu.swiftpay.payment.api.controller.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentCredentials
import com.ngyu.swiftpay.payment.application.auth.ApiCredentialsService
import com.ngyu.swiftpay.payment.application.service.merchant.MerchantService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import java.time.LocalDate

class MerchantServiceTest {

  private lateinit var apiCredentialsService: ApiCredentialsService
  private lateinit var merchantRepository: MerchantRepository
  private lateinit var sequenceGenerator: SequenceGenerator
  private lateinit var merchantService: MerchantService

  @BeforeEach
  fun setUp() {
    apiCredentialsService = mockk()
    merchantRepository = mockk()
    sequenceGenerator = mockk()
    merchantService = MerchantService(apiCredentialsService, merchantRepository, sequenceGenerator)
  }

  @Test
  @DisplayName("가맹점 정상 등록에 성공한다")
  fun registerMerchant_Success() {
    // given
    val request = MerchantRegisterReqeust(
      businessNumber = "123-45-67890",
      businessName = "테스트 가맹점",
      representativeName = "홍길동",
      businessType = "소매업",
      email = "test@test.com",
      phoneNumber = "010-1234-5678",
      address = "서울시 강남구",
      bankAccountNumber = "1234567890"
    )

    val merchantSeq = 1L
    val merchantId = Merchant.createMerchantId(merchantSeq)
    val merchant = request.toDomain(merchantSeq, merchantId)  // 실제 객체
    val approvedMerchant = merchant.approved(LocalDate.now().plusDays(1))  // 실제 도메인 로직
    val credentials = PaymentCredentials("test-client-id", "test-secret-key")

    every { sequenceGenerator.nextMerchantId() } returns merchantSeq
    every { merchantRepository.save(merchant) } returns merchant  // 첫 번째 save
    every { merchantRepository.findByMerchantId(merchantId) } returns merchant
    every { merchantRepository.save(approvedMerchant) } returns approvedMerchant  // 두 번째 save
    every { apiCredentialsService.issueKey(merchantSeq) } returns credentials

    // when
    val result = merchantService.register(request)

    // then
    assertThat(result.merchantId).isEqualTo(merchantId)
    assertThat(result.merchantName).isEqualTo("테스트 가맹점")
    assertThat(result.credentials).isEqualTo(credentials)

    verify(exactly = 1) { sequenceGenerator.nextMerchantId() }
    verify(exactly = 2) { merchantRepository.save(any()) }
    verify(exactly = 1) { apiCredentialsService.issueKey(merchantSeq) }
  }

  @Test
  @DisplayName("이미 등록된 사업자 번호일 경우 DuplicateMerchantException 발생")
  fun registerMerchant_DuplicateBusinessNumber() {
    // given
    val request = MerchantRegisterReqeust(
      businessNumber = "123-45-67890",
      businessName = "테스트 가맹점",
      representativeName = "홍길동",
      businessType = "소매업",
      email = "test@test.com",
      phoneNumber = "010-1234-5678",
      address = "서울시 강남구",
      bankAccountNumber = "1234567890"
    )

    every { sequenceGenerator.nextMerchantId() } returns 1L
    every { merchantRepository.save(any()) } throws DataIntegrityViolationException(
      "Duplicate entry '123-45-67890'"
    )

    // when & then
    assertThrows(DuplicateMerchantException::class.java) {
      merchantService.register(request)
    }
  }

  @Test
  @DisplayName("데이터 무결성 위반 시 InvalidMerchantDataException 발생")
  fun registerMerchant_InvalidData() {
    // given
    val request = MerchantRegisterReqeust(
      businessNumber = "123-45-67890",
      businessName = "테스트 가맹점",
      representativeName = "홍길동",
      businessType = "소매업",
      email = "test@test.com",
      phoneNumber = "010-1234-5678",
      address = "서울시 강남구",
      bankAccountNumber = "1234567890"
    )

    every { sequenceGenerator.nextMerchantId() } returns 1L
    every { merchantRepository.save(any()) } throws DataIntegrityViolationException(
      "Invalid data"
    )

    // when & then
    assertThrows(InvalidMerchantDataException::class.java) {
      merchantService.register(request)
    }
  }
}
