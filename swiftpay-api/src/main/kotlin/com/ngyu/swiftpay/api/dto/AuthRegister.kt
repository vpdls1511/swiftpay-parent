package com.ngyu.swiftpay.api.dto

data class AuthRegisterRequest(val username: String, val password: String)
data class AuthRegisterResponse(val apiToken: String)
