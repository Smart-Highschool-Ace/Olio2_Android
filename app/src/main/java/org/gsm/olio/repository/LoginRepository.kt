package org.gsm.olio.repository

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import kotlinx.coroutines.flow.collect
import org.gsm.olio.LoginMutation
import javax.inject.Inject

class LoginRepository @Inject constructor(val api : ApolloClient) {
    suspend fun loginCheck(token : String) : ApolloCall<LoginMutation.Data>{
        return api.mutation(LoginMutation(token))
    }
}