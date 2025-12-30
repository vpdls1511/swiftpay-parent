package com.ngyu.swiftpay.infrastructure.fake

import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PaymentEntity
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PaymentJpaRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.FluentQuery
import java.util.*
import java.util.function.Function

class FakePaymentJpaRepository : PaymentJpaRepository {

  private val storage = mutableMapOf<Long, PaymentEntity>()
  private var autoIncrement = 1L

  override fun findByPaymentId(paymentId: String): PaymentEntity? {
    TODO("Not yet implemented")
  }

  override fun <S : PaymentEntity> save(entity: S): S {
    val id = entity.id ?: autoIncrement++
    @Suppress("UNCHECKED_CAST")
    val saved = PaymentEntity(
      id = id,
      paymentId = entity.paymentId,
      merchantId = entity.merchantId,
      orderId = entity.orderId,
      orderName = entity.orderName,
      amount = entity.amount,
      currency = entity.currency,
      method = entity.method,
      methodDetail = entity.methodDetail,
      successUrl = entity.successUrl,
      cancelUrl = entity.cancelUrl,
      failureUrl = entity.failureUrl,
      status = entity.status,
      reason = entity.reason,
      idempotencyKey = entity.idempotencyKey,
    ) as S
    storage[id] = saved
    return saved
  }

  override fun findById(id: Long): Optional<PaymentEntity> {
    return Optional.ofNullable(storage[id])
  }

  override fun findAll(): List<PaymentEntity> {
    return storage.values.toList()
  }

  override fun deleteById(id: Long) {
    storage.remove(id)
  }

  override fun existsById(id: Long): Boolean {
    return storage.containsKey(id)
  }

  fun clear() {
    storage.clear()
    autoIncrement = 1L
  }

  // 나머지 구현
  override fun <S : PaymentEntity> saveAll(entities: Iterable<S>): List<S> {
    return entities.map { save(it) }
  }

  override fun findAllById(ids: Iterable<Long>): List<PaymentEntity> {
    return ids.mapNotNull { storage[it] }
  }

  override fun count(): Long = storage.size.toLong()

  override fun delete(entity: PaymentEntity) {
    entity.id?.let { storage.remove(it) }
  }

  override fun deleteAllById(ids: Iterable<Long>) {
    ids.forEach { storage.remove(it) }
  }

  override fun deleteAll(entities: Iterable<PaymentEntity>) {
    entities.forEach { it.id?.let { id -> storage.remove(id) } }
  }

  override fun deleteAll() = clear()

  override fun flush() {}
  override fun <S : PaymentEntity> saveAndFlush(entity: S): S = save(entity)
  override fun <S : PaymentEntity> saveAllAndFlush(entities: Iterable<S>): List<S> = saveAll(entities)
  override fun deleteAllInBatch(entities: Iterable<PaymentEntity>) = deleteAll(entities)
  override fun deleteAllByIdInBatch(ids: Iterable<Long>) = deleteAllById(ids)
  override fun deleteAllInBatch() = deleteAll()
  override fun getOne(id: Long): PaymentEntity = findById(id).orElseThrow()
  override fun getById(id: Long): PaymentEntity = findById(id).orElseThrow()
  override fun getReferenceById(id: Long): PaymentEntity = findById(id).orElseThrow()

  override fun <S : PaymentEntity> findAll(example: Example<S>): List<S> = throw NotImplementedError()
  override fun <S : PaymentEntity> findAll(example: Example<S>, sort: Sort): List<S> = throw NotImplementedError()
  override fun findAll(sort: Sort): List<PaymentEntity> = throw NotImplementedError()
  override fun findAll(pageable: Pageable): Page<PaymentEntity> = throw NotImplementedError()
  override fun <S : PaymentEntity> findOne(example: Example<S>): Optional<S> = throw NotImplementedError()
  override fun <S : PaymentEntity> findAll(example: Example<S>, pageable: Pageable): Page<S> = throw NotImplementedError()
  override fun <S : PaymentEntity> count(example: Example<S>): Long = throw NotImplementedError()
  override fun <S : PaymentEntity> exists(example: Example<S>): Boolean = throw NotImplementedError()
  override fun <S : PaymentEntity, R : Any> findBy(example: Example<S>, queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>): R = throw NotImplementedError()
}
