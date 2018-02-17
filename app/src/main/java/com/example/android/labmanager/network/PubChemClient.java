package com.example.android.labmanager.network;

import com.example.android.labmanager.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by katar on 25.06.2017.
 */

public class PubChemClient {

    private static final String BASE_URL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/";

    private static Retrofit retrofit;


    private static Retrofit createRetrofit() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor());



        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        clientBuilder.networkInterceptors().add(httpLoggingInterceptor);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
    }

    public static PubChemService getService() {
        if (retrofit == null) {
            retrofit = createRetrofit();
        }
        return retrofit.create(PubChemService.class);
    }




}
