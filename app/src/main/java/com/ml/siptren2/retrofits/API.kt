package com.ml.siptren2.retrofits

import android.graphics.Bitmap
import com.ml.siptren2.models.ModelDetailBayar
import com.ml.siptren2.models.ModelJadwalBayar
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface API {

    //API Login
//    @POST("login")
//    @FormUrlEncoded
//    fun loginUser (@Field("username") username: String,
//                   @Field("password") password: String): Call<APIRespon>

    @POST("login")
    @Multipart
    fun loginUser (@Part("username") username: RequestBody,
                   @Part("password") password: RequestBody): Call<APIRespon>


    //API Cek Password
    @POST("profile/cek-password")
    @FormUrlEncoded
    fun cekPasswordLama (@Field("id") id: Int,
                         @Field("password_lama") password: String): Call<APIRespon>

    //API Ubah Password
    @POST("profile/update-password")
    @FormUrlEncoded
    fun ubahPassword (@Field("id") id: Int,
                      @Field("password_baru") password1: String,
                      @Field("password_baru_confirmation") password2: String): Call<APIRespon>

    //API Jadwal Pembayaran
    @POST("pembayaran/jadwal")
    @FormUrlEncoded
    fun jadwalPembayaran (@Field("id") id: Int,
                          @Field("tanggal") tanggal: String) : Observable<List<ModelJadwalBayar>>

    //API Detail Pembayaran
    @POST("pembayaran/jadwal/detail")
    @FormUrlEncoded
    fun detailPembayaran (@Field("id_spp") id: Int) : Observable<List<ModelDetailBayar>>

    //API Pembayaran -- Bayar SPP
//    @Headers("Accept: application/json")
//    @POST("pembayaran/bayar")
//    @FormUrlEncoded
//    fun bayarSpp (@Field("id") id: Int,
//                  @Field("TglBayar") tglbayar: String,
//                  @Field("id_spp") idspp: Int,
//                  @Field("Nominal") nominal: Int,
//                  @Field("Bukti") bukti: File
//    ): Call<APIRespon>

//    @Headers("Accept: application/json")
//    @POST("pembayaran/bayar")
//    @Multipart
//    fun bayarSpp (@Part("id") id: RequestBody?,
//                  @Part("TglBayar") tglbayar: RequestBody?,
//                  @Part("id_spp") idspp: RequestBody?,
//                  @Part("Nominal") nominal: RequestBody?,
//                  @Part bukti: MultipartBody.Part?): Call<APIRespon>?

//    @POST("pembayaran/bayar")
//    @Multipart
//    fun bayarSpp (@Part("id") id: RequestBody,
//                  @Part("TglBayar") tglbayar: RequestBody,
//                  @Part("id_spp") idspp: RequestBody,
//                  @Part("Nominal") nominal: RequestBody,
//                  @Part bukti: MultipartBody.Part): Call<APIRespon>

    @POST("pembayaran/bayar")
    @Multipart
    fun bayarSpp (@Part("id") id: RequestBody,
                  @Part("TglBayar") tglbayar: RequestBody,
                  @Part("id_spp") idspp: RequestBody,
                  @Part("Nominal") nominal: RequestBody): Call<APIRespon>

}