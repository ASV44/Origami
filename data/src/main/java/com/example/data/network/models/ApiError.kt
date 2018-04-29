package com.example.data.network.models

data class ApiError(val code: Int, val message: String, val subError: List<String>)