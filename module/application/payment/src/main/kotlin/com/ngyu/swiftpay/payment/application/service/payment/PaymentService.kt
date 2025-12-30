package com.ngyu.swiftpay.payment.application.service.payment

import com.ngyu.swiftpay.core.common.exception.PaymentProcessException
import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentStatus
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.PaymentRepository
import com.ngyu.swiftpay.payment.api.controller.dto.ConfirmPaymentResponse
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentResponseDto
import com.ngyu.swiftpay.payment.application.service.escrow.EscrowService
import com.ngyu.swiftpay.payment.application.strategy.PaymentStrategyFactory
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(
  // TODO - 아직 각 전략의 내부 서비스를 완성하지 않은 단계. 우선, 도메인 생성 후 DB 저장까지만.
  private val paymentStrategyFactory: PaymentStrategyFactory,
  private val paymentRepository: PaymentRepository,
  private val escrowService: EscrowService,
  private val sequenceGenerator: SequenceGenerator
) {

  private val log = logger()

  @Transactional
  fun processing(request: PaymentRequestDto): PaymentResponseDto {
    log.info("결제 처리 시작 :: orderId=${request.orderId}, merchantId=${request.merchantId}, method=${request.method}, amount=${request.amount}")
    validatePaymentRequest(request)

    val payment = createPayment(request)
    val processed = processPayment(payment)
    val saved = paymentRepository.save(processed)

    log.info("결제 정보 저장 완료 :: orderId = ${request.orderId} , paymentId = ${saved.paymentId}")

    return PaymentResponseDto.fromDomain(saved)
  }

  fun confirmPayment(paymentId: String): ConfirmPaymentResponse {
    val settlement = escrowService.settle(paymentId)

    return ConfirmPaymentResponse.create(settlement)
  }

  /**
   * 1. 중복된 orderId 인지 검증
   * 2. 결제수단 검증
   * 3. 금액의 유효성 ( 0원보다 작은지 )
   * 4. 결제를 하고자 하는 가맹점이 활성화 상태인지
   * 5. 해당 결제수단을 지원하는지
   *
   * 위 조건을 모두 만족하지 못한다면, Exception 발생.
   */
  private fun validatePaymentRequest(request: PaymentRequestDto) {
    // TODO - pending -> progress 결제 요청 자체의 유효성을 검사해야한다..
  }

  /**
   * 결제를 위한 데이터를 만들고, 이를 DB에 Pending 상태로 저장한다.
   */
  private fun createPayment(request: PaymentRequestDto): Payment {
    val paymentSeq = sequenceGenerator.nextPaymentId()
    val paymentId = Payment.createPaymentId(paymentSeq)
    val payment = request.toDomain(paymentSeq, paymentId)

    return paymentRepository.save(payment)
  }

  private fun processPayment(payment: Payment): Payment {
    return try {
      val strategy = paymentStrategyFactory.getStrategy(payment)
      log.info("결제 전략 선택 완료 | strategy=${strategy.getStrategyName()}")

      val processed = strategy.process(payment)
      log.info("결제 요청 처리 성공 | paymentId=${processed.paymentId}, status=${processed.status}")

      if (processed.status == PaymentStatus.SUCCEEDED) {
        escrowService.hold(payment)
        log.info("에스크로 예치 성공 | paymentId=${payment.paymentId}")
      }

      processed
    } catch (e: Exception) {
      log.error("결제 실패 | paymentId=${payment.paymentId}", e)
      val cancel = payment.cancel(e.message ?: "결제 처리 중 오류")
      paymentRepository.save(cancel)
      throw PaymentProcessException()
    }
  }

}
