package org.gsm.olio.repository

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import okhttp3.MultipartBody
import org.gsm.olio.UpdateUserMutation
import org.gsm.olio.di.NetworkModule
import org.gsm.olio.model.data.request.RequestUrl
import org.gsm.olio.model.service.PostProfileApi
import org.gsm.olio.model.service.UploadImageApi
import org.gsm.olio.type.UserUpdateInput
import javax.inject.Inject

class FirstLoginRepository @Inject constructor(
    @NetworkModule.type1 val api: UploadImageApi,
    @NetworkModule.type2 val api2 : PostProfileApi,
    private val api3 : ApolloClient
    ) {

    suspend fun uploadImage(name: String) = api.uploadImage(RequestUrl(name=name,"jpg"))

    suspend fun postProfile(url : String, img : MultipartBody.Part) = api2.postProfile(url = url, image = img)

    suspend fun createUser(name:String ,img : String) : ApolloCall<UpdateUserMutation.Data>{
        return api3.mutation(UpdateUserMutation(UserUpdateInput(name = name as Optional.Absent, profile_image = img as Optional.Absent)))
    }

}

