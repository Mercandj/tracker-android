package com.mercandalli.tracker.push;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mercandalli.tracker.common.Preconditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/* package */ class PushSenderManagerNetwork implements PushSenderManager {

    private final OkHttpClient okHttpClient;
    private final Headers headers;
    private final Gson gson = new Gson();

    /* package */ PushSenderManagerNetwork(
            final OkHttpClient okHttpClient,
            final String apiKey) {
        Preconditions.INSTANCE.checkNotNull(okHttpClient);
        Preconditions.INSTANCE.checkNotNull(apiKey);

        this.okHttpClient = okHttpClient;
        this.headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .add("Authorization", "key=" + apiKey)
                .build();
    }

    class ReqProfiler {

        @SerializedName("registration_ids")
        private final List<String> ids;

        @SerializedName("data")
        private final Data data;

        ReqProfiler(final String id, final String message) {
            this.ids = new ArrayList<>();
            this.ids.add(id);
            this.data = new Data(message);
        }

        class Data {

            @SerializedName("message")
            private String message;

            private Data(final String message) {
                this.message = message;
            }
        }
    }

    @Override
    public void sendPush(final String cloudMessagingId, final String message) {
        ReqProfiler req = new ReqProfiler(cloudMessagingId, message);
        sendPush(cloudMessagingId, gson.toJson(req), headers);
    }

    private void sendPush(String cloudMessagingId, String message, Headers headers) {
        Preconditions.INSTANCE.checkNotNull(cloudMessagingId);
        Preconditions.INSTANCE.checkNotNull(message);
        Preconditions.INSTANCE.checkNotNull(headers);

        Request post = new Request.Builder()
                .headers(headers)
                .url("https://fcm.googleapis.com/fcm/send")
                .method(
                        "POST",
                        RequestBody.create(
                                MediaType.parse("application/json"),
                                message))
                .build();

        Call call = okHttpClient.newCall(post);
        call.enqueue(
                new Callback() {
                    @Override
                    public void onResponse(
                            @NonNull final Call call,
                            @NonNull final Response response) throws IOException {
                        final ResponseBody body = response.body();
                        body.close();
                    }

                    @Override
                    public void onFailure(
                            @NonNull final Call call,
                            @NonNull final IOException e) {
                    }
                }
        );
    }
}
