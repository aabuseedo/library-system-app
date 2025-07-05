package com.example.librarysystemapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/api/"; // استخدام 10.0.2.2 مع emulator
    //http://192.168.X.X:8000/api/ >> لو كنت استخدم جوالي على نفس شبكة الحاسوب بدل emulator
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
