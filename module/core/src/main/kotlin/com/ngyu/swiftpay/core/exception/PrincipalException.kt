package com.ngyu.swiftpay.core.exception

class PrincipalException (
  message: String,
  cause: Throwable? = null,
) : RuntimeException(message, cause)
