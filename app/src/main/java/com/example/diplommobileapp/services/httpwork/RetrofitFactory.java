package com.example.diplommobileapp.services.httpwork;

import android.content.Context;

import com.example.diplommobileapp.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static IApi apiService;
    public static void CreateIApiInstance(Context context){
        synchronized (RetrofitFactory.class) {
            if (apiService == null) {
                OkHttpClient client = MyHttpClient.getClient(context);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(context.getString(R.string.api_ip))
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                apiService = retrofit.create(IApi.class);
            }
        }
    }
    public static IApi getApiService() {
        return apiService;
    }
}
