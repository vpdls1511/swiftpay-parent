package com.ngyu.swiftpay.core.domain

import java.io.Serializable

/**
 * 도메인 엔티티의 기본 추상 클래스
 *
 * ID 기반의 동등성(equality) 비교를 제공한다.
 * 모든 도메인 엔티티는 이 클래스를 상속받아 ID를 기준으로 동일성을 판단한다.
 *
 * @param ID 엔티티의 식별자 타입 (Serializable이어야 함)
 */
abstract class BaseDomain<ID : Serializable> {

  abstract val id: ID?

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as BaseDomain<*>
    if (id == null || other.id == null) return false


    return id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }

}
