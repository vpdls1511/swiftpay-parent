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

  private val storage = mutableMapOf<String, PaymentEntity>()

  override fun <S : PaymentEntity?> save(entity: S & Any): S & Any {
    storage[entity.id] = entity
    return entity
  }

  override fun findById(id: String): Optional<PaymentEntity> {
    return Optional.ofNullable(storage[id])
  }

  override fun findAll(): List<PaymentEntity> {
    return storage.values.toList()
  }

  override fun deleteById(id: String) {
    storage.remove(id)
  }

  override fun existsById(id: String): Boolean {
    return storage.containsKey(id)
  }

  fun clear() {
    storage.clear()
  }

  // 나머지 필수 구현 메서드들 (기본 구현)
  override fun <S : PaymentEntity> saveAll(entities: Iterable<S>): List<S> {
    return entities.map { save(it) }
  }

  override fun findAllById(ids: Iterable<String>): List<PaymentEntity> {
    return ids.mapNotNull { storage[it] }
  }

  override fun count(): Long = storage.size.toLong()

  override fun delete(entity: PaymentEntity) {
    storage.remove(entity.id)
  }

  override fun deleteAllById(ids: Iterable<String>) {
    ids.forEach { storage.remove(it) }
  }

  override fun deleteAll(entities: Iterable<PaymentEntity>) {
    entities.forEach { storage.remove(it.id) }
  }

  override fun deleteAll() {
    storage.clear()
  }

  override fun flush() {}
  override fun <S : PaymentEntity> saveAndFlush(entity: S): S = save(entity)
  override fun <S : PaymentEntity> saveAllAndFlush(entities: Iterable<S>): List<S> = saveAll(entities).toList()
  override fun deleteAllInBatch(entities: Iterable<PaymentEntity>) = deleteAll(entities)
  override fun deleteAllByIdInBatch(ids: Iterable<String>) = deleteAllById(ids)
  override fun deleteAllInBatch() = deleteAll()
  override fun getOne(id: String): PaymentEntity = findById(id).orElseThrow()
  override fun getById(id: String): PaymentEntity = findById(id).orElseThrow()
  override fun getReferenceById(id: String): PaymentEntity = findById(id).orElseThrow()

  // Example, Sort, Page 관련 메서드들 (사용 안 하면 NotImplementedError)
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
