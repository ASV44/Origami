package com.example.data.network.models.response

class UserMe(val id: Int,
             val tags: List<Int>,
             val fb_access_token: String,
             val email: String)