package com.ngyu.swiftpay.security.fake

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyEntity
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyMapper
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.FluentQuery
import java.util.*
import java.util.function.Function

class FakeApiKeyJpaRepository : JpaRepository<ApiKeyEntity, String> {

  private val storage: MutableMap<String, ApiKeyEntity> = mutableMapOf()

  fun save(domain: ApiKey): ApiKeyEntity {
    val entity = ApiKeyMapper.toEntity(domain)
    return save(entity)
  }

  fun findByLookUpKey(lookUpKey: String): ApiKeyEntity? {
    return storage[lookUpKey]
  }

  override fun <S : ApiKeyEntity> save(entity: S): S {
    storage[entity.lookupKey] = entity
    return entity
  }

  override fun <S : ApiKeyEntity> saveAll(entities: MutableIterable<S>): MutableList<S> {
    return entities.map { save(it) }.toMutableList()
  }

  override fun findById(id: String): Optional<ApiKeyEntity> {
    return Optional.ofNullable(storage.values.find { it.apiKey == id })
  }

  override fun existsById(id: String): Boolean {
    return storage.values.any { it.apiKey == id }
  }

  override fun findAll(): MutableList<ApiKeyEntity> {
    return storage.values.toMutableList()
  }

  override fun findAll(sort: Sort): MutableList<ApiKeyEntity> {
    return findAll()
  }

  override fun findAll(pageable: Pageable): Page<ApiKeyEntity> {
    val all = findAll()
    val start = pageable.offset.toInt()
    val end = minOf(start + pageable.pageSize, all.size)
    return PageImpl(all.subList(start, end), pageable, all.size.toLong())
  }

  override fun findAllById(ids: MutableIterable<String>): MutableList<ApiKeyEntity> {
    return storage.values.filter { it.apiKey in ids }.toMutableList()
  }

  override fun count(): Long {
    return storage.size.toLong()
  }

  override fun deleteById(id: String) {
    storage.values.removeIf { it.apiKey == id }
  }

  override fun delete(entity: ApiKeyEntity) {
    storage.remove(entity.lookupKey)
  }

  override fun deleteAllById(ids: MutableIterable<String>) {
    ids.forEach { deleteById(it) }
  }

  override fun deleteAll(entities: MutableIterable<ApiKeyEntity>) {
    entities.forEach { delete(it) }
  }

  override fun deleteAll() {
    storage.clear()
  }

  override fun flush() {}

  override fun <S : ApiKeyEntity> saveAndFlush(entity: S): S {
    return save(entity)
  }

  override fun <S : ApiKeyEntity> saveAllAndFlush(entities: MutableIterable<S>): MutableList<S> {
    return saveAll(entities)
  }

  override fun deleteAllInBatch() {
    deleteAll()
  }

  override fun deleteAllByIdInBatch(ids: MutableIterable<String>) {
    deleteAllById(ids)
  }

  override fun deleteAllInBatch(entities: MutableIterable<ApiKeyEntity>) {
    deleteAll(entities)
  }

  override fun getOne(id: String): ApiKeyEntity {
    return findById(id).orElseThrow()
  }

  override fun getById(id: String): ApiKeyEntity {
    return findById(id).orElseThrow()
  }

  override fun getReferenceById(id: String): ApiKeyEntity {
    return findById(id).orElseThrow()
  }

  override fun <S : ApiKeyEntity> findAll(example: Example<S>): MutableList<S> {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }

  override fun <S : ApiKeyEntity> findAll(example: Example<S>, sort: Sort): MutableList<S> {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }

  override fun <S : ApiKeyEntity> findAll(example: Example<S>, pageable: Pageable): Page<S> {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }

  override fun <S : ApiKeyEntity> findOne(example: Example<S>): Optional<S> {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }

  override fun <S : ApiKeyEntity> count(example: Example<S>): Long {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }

  override fun <S : ApiKeyEntity> exists(example: Example<S>): Boolean {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }

  override fun <S : ApiKeyEntity, R : Any?> findBy(
    example: Example<S>,
    queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
  ): R & Any {
    throw UnsupportedOperationException("Not implemented for fake repository")
  }
}
