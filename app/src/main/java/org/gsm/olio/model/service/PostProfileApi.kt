package org.gsm.olio.model.service

import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.*

interface PostProfileApi {
    @Headers("Accept-Encoding: identity")
    @Multipart
    @PUT("{uploadURL}")
    suspend fun postProfile(
        @Path("uploadURL", encoded = true) url : String,
        @Part image: MultipartBody.Part
    ):retrofit2.Response<Void>

}