package com.ml.siptren2.retrofits

import okhttp3.RequestBody
import com.ml.siptren2.retrofits.APIRespon
import retrofit2.Call
import retrofit2.http.*

interface ApiConfig {
    @Multipart
    @POST("pembayaran/bayar")
    fun uploadBayar(
        @Field("id") id: Int?,
        @Field("TglBayar") tglBayar: String?,
        @Field("id_spp") idSpp: Int?,
        @Field("Nominal") nominal: String?,
        @PartMap map: Map<String?, RequestBody?>?
    ): Call<APIRespon?>?
}