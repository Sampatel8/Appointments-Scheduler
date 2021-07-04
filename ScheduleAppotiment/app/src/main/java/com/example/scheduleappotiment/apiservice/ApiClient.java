package com.example.scheduleappotiment.apiservice;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL="https://3vision2-dev-ed.lightning.force.com";
    public static final String USERNAME="siddharth9365@gmail.com.appointment";
    public static final String PASSWORD="appointment@1235zf8kTbOISywc0dSpmmrsfb8d";
    public static final String CLIENT_SECRET ="4E09E6E0B26783BCF53AD0DC93B6F2C05ED9B159AC7D20A948191DC20B770B94";
    public static final String CLIENT_ID="3MVG9cHH2bfKACZY3qTfeMnKcfPOqHVgbOnMLjARLVdGwC4IGLvB3RDc7sUay5KzoFZ547DdWJA7ollnJSgUR";


    private static Retrofit retrofit = null;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiPoint getService(){
        return ApiClient.getClient().create(ApiPoint.class);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        Retrofit localRetrofit=null;
        if (!TextUtils.isEmpty(authToken)) {
            AuthIntreceptor interceptor =
                    new AuthIntreceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                localRetrofit=new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }else localRetrofit=retrofit;
        return localRetrofit.create(serviceClass);
    }
}
