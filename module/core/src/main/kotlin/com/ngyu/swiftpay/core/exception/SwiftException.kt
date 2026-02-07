package com.ngyu.swiftpay.core.exception

open class SwiftException(
  val swiftError: SwiftError,
  override val message: String = swiftError.message,
  override val cause: Throwable? = null
) : RuntimeException(message, cause) {

  constructor(swiftError: SwiftError) : this(swiftError, swiftError.message, null)

  constructor(swiftError: SwiftError, message: String) : this(swiftError, message, null)

  constructor(swiftError: SwiftError, cause: Throwable) : this(swiftError, swiftError.message, cause)
}
