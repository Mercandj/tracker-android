package com.mercandalli.tracker.push

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.mercandalli.tracker.common.Preconditions
import okhttp3.*
import java.io.IOException
import java.util.*

/* package */ internal class PushSenderManagerNetwork(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson,
        apiKey: String) : PushSenderManager {
    private val headers: Headers

    init {
        Preconditions.checkNotNull(okHttpClient)
        Preconditions.checkNotNull(apiKey)
        this.headers = Headers.Builder()
                .add("Content-Type", "application/json")
                .add("Authorization", "key=" + apiKey)
                .build()
    }

    internal inner class ReqProfiler(id: String, message: String) {

        @SerializedName("registration_ids")
        private val ids: MutableList<String>

        @SerializedName("data")
        private val data: Data

        init {
            this.ids = ArrayList()
            this.ids.add(id)
            this.data = Data(message)
        }

        internal inner class Data constructor(@SerializedName("message")
                                              private val message: String)
    }

    override fun sendPush(cloudMessagingId: String, message: String) {
        val req = ReqProfiler(cloudMessagingId, message)
        sendPush(cloudMessagingId, gson.toJson(req), headers)
    }

    private fun sendPush(cloudMessagingId: String, message: String, headers: Headers) {
        Preconditions.checkNotNull(cloudMessagingId)
        Preconditions.checkNotNull(message)
        Preconditions.checkNotNull(headers)

        val post = Request.Builder()
                .headers(headers)
                .url("https://fcm.googleapis.com/fcm/initialize")
                .method(
                        "POST",
                        RequestBody.create(
                                MediaType.parse("application/json"),
                                message))
                .build()

        val call = okHttpClient.newCall(post)
        call.enqueue(
                object : Callback {
                    @Throws(IOException::class)
                    override fun onResponse(
                            call: Call,
                            response: Response) {
                        val body = response.body()
                        body!!.close()
                    }

                    override fun onFailure(
                            call: Call,
                            e: IOException) {
                    }
                }
        )
    }
}
