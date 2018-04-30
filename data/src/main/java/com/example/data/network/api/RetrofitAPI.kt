package com.example.data.network.api

import com.example.data.network.models.ApiResponse
import com.example.data.network.models.request.TagRequestApi
import com.example.data.network.models.response.Tag
import com.example.data.network.models.response.UserMe
import io.reactivex.Observable
import retrofit2.http.*

interface RetrofitAPI {

    @GET("login")
    fun login(@Query("access_token") accessToken: String): Observable<ApiResponse<UserMe>>

    @GET("tags")
    fun getTags(): Observable<ApiResponse<List<Tag>>>

    @POST("tags")
    fun postTags(@Body body: TagRequestApi): Observable<Void>

    @DELETE("tags/{id}")
    fun deleteTag(@Path("id") id: Int): Observable<Unit>
}
