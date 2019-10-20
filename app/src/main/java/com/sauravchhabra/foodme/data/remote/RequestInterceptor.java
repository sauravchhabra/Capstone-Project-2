package com.sauravchhabra.foodme.data.remote;

import android.support.annotation.NonNull;

import com.sauravchhabra.foodme.util.ConstantsUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .addHeader(ConstantsUtils.USER_KEY_HEADER, ConstantsUtils.ZOMATO_API_KEY)
                .build();
        return chain.proceed(newRequest);
    }
}
