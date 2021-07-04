package com.example.scheduleappotiment.apiservice;

import android.util.Log;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthIntreceptor implements Interceptor {

    private String credentials;

    public AuthIntreceptor(String token) {
        this.credentials="Bearer "+token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //Log.d("onResponse", "intercept: "+credentials);
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .addHeader("Authorization", credentials)
                .header("Content-type","application/json").build();
        Log.d("onResponse", "intercept: header:="+request.header("Content-type")+"\t request:="+request.toString());
        return chain.proceed(authenticatedRequest);
    }

}