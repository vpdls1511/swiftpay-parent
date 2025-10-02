package com.ngyu.swiftpay.infrastructure.db.persistent.member

import com.ngyu.swiftpay.core.domain.member.MemberStatus
import com.ngyu.swiftpay.infrastructure.db.persistent.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "member")
class MemberEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(unique = true, nullable = false, length = 50)
  val username: String = "",

  @Column(nullable = false)
  val password: String = "",

  @Column(nullable = false, length = 100)
  val name: String = "",

  @Column(unique = true, nullable = false)
  val email: String = "",

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val status: MemberStatus = MemberStatus.PENDING

) : BaseTimeEntity()  // 괄호 위치 수정
