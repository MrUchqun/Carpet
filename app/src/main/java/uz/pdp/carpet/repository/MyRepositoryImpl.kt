package uz.pdp.carpet.repository

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import retrofit2.Response
import uz.pdp.carpet.data.remote.ApiService
import uz.pdp.carpet.model.User
import uz.pdp.carpet.model.UserLogin
import uz.pdp.carpet.model.UserRegister
import uz.pdp.carpet.utils.Constants.STATE
import uz.pdp.carpet.utils.Constants.USER_LOGIN
import uz.pdp.carpet.utils.Constants.USER_LOGOUT
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
) : MyRepository {

    override suspend fun signUp(userRegister: UserRegister): Response<User> {
        return api.signUp(userRegister)
    }

    override suspend fun signIn(userLogin: UserLogin): Response<User> {
        return api.signIn(userLogin)
    }

    override fun login() {
        sharedPreferences.edit().putString(STATE, USER_LOGIN).apply()
    }

    override fun logout() {
        sharedPreferences.edit().putString(STATE, USER_LOGOUT).apply()
    }

    override fun getState(): String {
        return sharedPreferences.getString(STATE, USER_LOGOUT)!!
    }
}