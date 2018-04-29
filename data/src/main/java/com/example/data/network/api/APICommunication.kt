package com.example.data.network.api

import android.util.Log
import com.example.data.network.models.ApiResponse
import com.example.data.network.models.request.TagRequestApi
import com.example.data.network.models.response.Tag
import com.example.data.network.models.response.UserMe
import com.example.data.network.util.RequestExecutor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber

class APICommunication(private val retrofitAPI: RetrofitAPI, private val requestExecutor: RequestExecutor) : APIService {

    companion object {
        fun <T> execute(observable: Observable<T>) {
            val subscription = observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({result -> Log.e("Succes", "" + result) },
                            {error -> Log.e("Error", error.message)})
        }

    }

    override fun getTags(): Observable<ApiResponse<List<Tag>>> {
        return requestExecutor.execute(retrofitAPI.getTags())
    }

    override fun postTags(body: TagRequestApi): Observable<Void> {
        return requestExecutor.execute(retrofitAPI.postTags(body))
    }

    override fun deleteTag(id: Int): Observable<Unit> {
        return requestExecutor.execute(retrofitAPI.deleteTag(id))
    }

    override fun login(accessToken: String): Observable<ApiResponse<UserMe>> {
        return requestExecutor.execute(retrofitAPI.login(accessToken))
    }
}
