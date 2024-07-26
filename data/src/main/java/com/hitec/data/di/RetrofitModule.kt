package com.hitec.data.di

import com.hitec.data.db.retrofit.HitecService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


const val HITEC_DEFAULT_URL = "http://1.233.95.240:8080/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 로그 레벨 설정 (BODY는 요청과 응답의 모든 정보를 로그로 출력)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃을 30초로 설정
            .writeTimeout(30, TimeUnit.SECONDS) // 쓰기 타임아웃을 30초로 설정
            .readTimeout(30, TimeUnit.SECONDS) // 읽기 타임아웃을 30초로 설정
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val tikXmlConverterFactory = TikXmlConverterFactory.create(
            TikXml.Builder()
                //해당 옵션 때문에 TikXml builder 추가, 데이터 가져올 때 매핑이 안된 xml데이터에 대한 예외 무시
                .exceptionOnUnreadXml(false)
                .build()
        )

        val scalarsConverterFactory = ScalarsConverterFactory.create()

        return Retrofit.Builder()
            .baseUrl(HITEC_DEFAULT_URL)
            .addConverterFactory(tikXmlConverterFactory)
            .addConverterFactory(scalarsConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    fun provideHitecService(retrofit: Retrofit): HitecService {
        return retrofit.create(HitecService::class.java)
    }

}

