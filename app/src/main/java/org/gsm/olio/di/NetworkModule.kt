package org.gsm.olio.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.gsm.olio.BuildConfig
import org.gsm.olio.model.service.PostProfileApi
import org.gsm.olio.model.service.UploadImageApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class type1

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class type2


    @Provides
    @Singleton
    fun apolloClient(): ApolloClient {

        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BASE_URL)
            .okHttpClient(okHttpClient())
            .build()

    }

    @Provides
    @Singleton
    @type1
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.IMAGE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
    }

    @Provides
    @Singleton
    @type1
    fun provideImgUpload(@type1 retrofit: Retrofit): UploadImageApi =
        retrofit.create(UploadImageApi::class.java)

    @Provides
    @Singleton
    @type2
    fun retrofit2(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.POST_PROFILE_URL)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @type2
    fun postProfile(@type2 retrofit: Retrofit): PostProfileApi =
        retrofit.create(PostProfileApi::class.java)

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val logger = httpLogger(builder)
        return builder
            //서버로부터의 응답까지의 시간이 읽기 시간 초과보다 크면 요청 실패로 판단한다.
            .readTimeout(10, TimeUnit.SECONDS)
            //서버로 요청을 시작한 후 15초가 지날 때 까지 데이터가 안오면 에러로 판단한다.
            .connectTimeout(10, TimeUnit.SECONDS)
            // 얼마나 빨리 서버로 데이터를 받을 수 있는지 판단한다.
            .writeTimeout(15, TimeUnit.SECONDS)
            //Header 추가
            .addInterceptor(MyInterceptor())
            //서버로 부터 받아온 데이터 log 찍기
            .addInterceptor(logger)
            .build()
    }

    private fun httpLogger(builder: OkHttpClient.Builder) : HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        //Multipart response Log 메세지 제외
        builder.addInterceptor {
            val original : Request = it.request()
            val request : Request = original.newBuilder().build()
            val hasMultipart = request.headers.names().contains("multipart")
            //None으로 할 시 Request요청도 안보이므로 HEADERS로 바꿔 Request, Response 메세지만 본다.
            logger.level = (if(hasMultipart) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.BODY)
            return@addInterceptor it.proceed(request)
        }.build()

        return logger
    }

}

class MyInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {

        val request: Request = chain.request()
        var stringurl = request.url.toString()
        stringurl = stringurl.replace("%3F", "?")

        val newRequest = request().newBuilder()
            .url(stringurl)
            .build()

        return proceed(newRequest)
    }

}