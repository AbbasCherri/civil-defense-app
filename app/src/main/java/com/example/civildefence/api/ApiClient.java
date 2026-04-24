package com.example.civildefence.api;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        SharedPreferences prefs = context.getSharedPreferences(
                                "civil_defense_prefs", Context.MODE_PRIVATE);
                        String token = prefs.getString("access_token", "");

                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        if (!token.isEmpty()) {
                            builder.header("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(builder.build());
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkUtils.BASE_URL + "/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}