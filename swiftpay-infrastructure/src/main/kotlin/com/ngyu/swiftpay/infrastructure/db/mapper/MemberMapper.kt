package com.ngyu.swiftpay.infrastructure.db.mapper

import com.ngyu.swiftpay.core.domain.Member
import com.ngyu.swiftpay.core.domain.vo.Password
import com.ngyu.swiftpay.infrastructure.db.persistent.MemberEntity

class MemberMapper {

  fun toEntity(domain: Member): MemberEntity {
    return MemberEntity(
      id = domain.id,
      username = domain.username,
      password = domain.password.value,
      name = domain.name,
      email = domain.email,
      status = domain.status
    )
  }

  fun toDomain(entity: MemberEntity): Member {
    return Member(
      id = entity.id,
      username = entity.username,
      password = Password.ofEncrypt(entity.password),
      name = entity.name,
      email = entity.email,
      status = entity.status,
      createdAt = entity.createdAt
    )
  }

}
