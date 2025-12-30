package com.ngyu.swiftpay.payment.application.service.order

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.order.Order
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.OrderRepository
import com.ngyu.swiftpay.payment.api.controller.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.controller.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.application.service.merchant.MerchantService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
  private val merchantService: MerchantService,
  private val orderRepository: OrderRepository,
  private val sequenceGenerator: SequenceGenerator
) {
  private val log = logger()

  @Transactional
  fun createOrder(request: OrderCreateRequestDto): OrderCreateResponseDto {
    val merchant = merchantService.getMerchant(request.merchantId)

    log.info("주문서 생성 시작 | merchantId=${request.merchantId}, orderName=${request.orderName}, amount=${request.totalAmount}")
    //TODO - 추후에 merchantId 검증 로직 필요
    val orderSeq = sequenceGenerator.nextOrderId()
    val orderId = Order.createOrderId(orderSeq)
    val order = request.toDomain(orderSeq, orderId, merchant.id)
    val savedDomain = orderRepository.save(order)

    log.info("주문서 생성 완료 | orderId=${savedDomain.orderId}, merchantId=${savedDomain.merchantId}")
    return OrderCreateResponseDto.fromDomain(savedDomain)
  }

  @Transactional(readOnly = true)
  fun findOrder(orderId: String): Order {
    val order = orderRepository.findByOrder(orderId)

    return order
  }
}
