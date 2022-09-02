package org.gsm.olio.model.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import org.gsm.olio.model.data.request.RequestUrl
import org.gsm.olio.model.data.response.ResponseUrl
import retrofit2.http.*

interface UploadImageApi {

    @POST("dev/image")
    suspend fun uploadImage(
        @Body img: RequestUrl,
    ):retrofit2.Response<ResponseUrl>


}