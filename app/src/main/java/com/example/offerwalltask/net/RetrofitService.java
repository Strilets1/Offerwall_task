package com.example.offerwalltask.net;

import com.example.offerwalltask.api.MockableAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static RetrofitService mInstance;
    private static final String BASE_URL = "https://demo0040494.mockable.io/api/v1/";
    private Retrofit mRetrofit;

    private RetrofitService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitService getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitService();
        }
        return mInstance;
    }

    public MockableAPI getJSONApi() {
        return mRetrofit.create(MockableAPI.class);
    }
}
