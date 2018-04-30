package com.example.data.network.api

import com.example.data.network.models.ApiResponse
import com.example.data.network.models.request.TagRequestApi
import com.example.data.network.models.response.Tag
import com.example.data.network.models.response.UserMe
import io.reactivex.Observable

interface APIService {

    fun login(accessToken: String): Observable<ApiResponse<UserMe>>

    fun getTags(): Observable<ApiResponse<List<Tag>>>

    fun postTags(body: TagRequestApi): Observable<Void>

    fun deleteTag(id: Int): Observable<Unit>
}
