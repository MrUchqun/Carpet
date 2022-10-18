package uz.pdp.carpet.utils

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import uz.pdp.carpet.utils.Constants.AUTHORIZATION
import uz.pdp.carpet.utils.Constants.AUTH_TYPE
import uz.pdp.carpet.utils.Constants.JWT_TOKEN
import uz.pdp.carpet.utils.Constants.NULL
import javax.inject.Inject

data class AuthInterceptor(private val sharedPreferences: SharedPreferences) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = sharedPreferences.getString(JWT_TOKEN, NULL)
        if (token != NULL)
            requestBuilder.addHeader(AUTHORIZATION, "$AUTH_TYPE $token")
        return chain.proceed(requestBuilder.build())
    }
}