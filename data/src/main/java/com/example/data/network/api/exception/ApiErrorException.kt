package com.example.data.network.api.exception

class ApiErrorException(val code: Int, errorMessage: String) : RuntimeException(errorMessage)