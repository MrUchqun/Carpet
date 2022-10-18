package uz.pdp.carpet.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.pdp.carpet.BuildConfig
import uz.pdp.carpet.data.remote.ApiService
import uz.pdp.carpet.repository.MyRepository
import uz.pdp.carpet.repository.MyRepositoryImpl
import uz.pdp.carpet.utils.AuthInterceptor
import uz.pdp.carpet.utils.Constants
import uz.pdp.carpet.utils.Constants.LOCAL_SHARED_PREF
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(LOCAL_SHARED_PREF, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences): AuthInterceptor {
        return AuthInterceptor(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().addInterceptor(authInterceptor)
        return if (BuildConfig.DEBUG) builder.addInterceptor(logging).build()
        else builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyRepository(
        apiService: ApiService,
        sharedPreferences: SharedPreferences
    ): MyRepository {
        return MyRepositoryImpl(apiService, sharedPreferences)
    }

}