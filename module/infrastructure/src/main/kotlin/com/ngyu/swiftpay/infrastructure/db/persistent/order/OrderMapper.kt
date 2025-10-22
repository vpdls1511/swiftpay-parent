package com.ngyu.swiftpay.infrastructure.db.persistent.order.mapper

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.money.Money
import com.ngyu.swiftpay.core.domain.order.Order
import com.ngyu.swiftpay.infrastructure.db.persistent.order.OrderEntity

object OrderMapper {

  fun toDomain(entity: OrderEntity): Order {
    return Order(
      id = entity.id,
      orderId = entity.orderId,
      merchantId = entity.merchantId,
      orderName = entity.orderName,
      totalAmount = Money(entity.totalAmount, Currency.valueOf(entity.currency)),
      balanceAmount = Money(entity.balanceAmount, Currency.valueOf(entity.currency)),
      supplyAmount = Money(entity.supplyAmount, Currency.valueOf(entity.currency)),
      tax = Money(entity.tax, Currency.valueOf(entity.currency)),
      status = entity.status,
      customerName = entity.customerName,
      customerEmail = entity.customerEmail,
      customerPhone = entity.customerPhone,
      createdAt = entity.createdAt,
      updatedAt = entity.updatedAt
    )
  }

  fun toEntity(domain: Order): OrderEntity {
    return OrderEntity(
      id = domain.id,
      orderId = domain.orderId,
      merchantId = domain.merchantId,
      orderName = domain.orderName,
      totalAmount = domain.totalAmount.amount,
      balanceAmount = domain.balanceAmount.amount,
      supplyAmount = domain.supplyAmount.amount,
      tax = domain.tax.amount,
      currency = domain.totalAmount.currency.name,
      status = domain.status,
      customerName = domain.customerName,
      customerEmail = domain.customerEmail,
      customerPhone = domain.customerPhone,
      createdAt = domain.createdAt,
      updatedAt = domain.updatedAt
    )
  }
}
