package org.gsm.olio.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.gsm.olio.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @JvmStatic
    fun apolloClient(okHttpClient: OkHttpClient) : ApolloClient {
        return ApolloClient.Builder()
            .okHttpClient(okHttpClient)
            .serverUrl(BuildConfig.BASE_URL)
            .build()
    }

}