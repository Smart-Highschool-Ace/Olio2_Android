package org.gsm.olio.model.service

import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PostProfileApi {
    @Multipart
    @PUT("{uploadURL}")
    suspend fun postProfile(
        @Path("uploadURL") url : String,
        @Part image: MultipartBody.Part?
    ):retrofit2.Response<String>

}