package com.ml.siptren2.retrofits

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {
    private var retrofit: Retrofit? = null
    private var gson: Gson? = null

    val instance: Retrofit
        get(){
            if (retrofit == null){
                retrofit = Retrofit.Builder().baseUrl("https://api.spp-santri.ptpati.com/api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val instance1: Gson
    get(){
        if (gson == null){
            gson = GsonBuilder()
                .setLenient()
                .create()
        }
        return gson!!
    }
}
