package uz.pdp.carpet.repository

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import retrofit2.Response
import uz.pdp.carpet.data.remote.ApiService
import uz.pdp.carpet.model.*
import uz.pdp.carpet.utils.Constants.JWT_TOKEN
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
        sharedPreferences.edit().apply {
            putString(STATE, USER_LOGOUT)
            putString(JWT_TOKEN, null)
        }.apply()
    }

    override fun getState(): String {
        return sharedPreferences.getString(STATE, USER_LOGOUT)!!
    }

    override fun saveToken(jwt: String) {
        sharedPreferences.edit().putString(JWT_TOKEN, jwt).apply()
    }

    override suspend fun profileAdmPaginationList(page: Int, size: Int): Response<PageResponse> {
        return api.profileAdmPaginationList(page, size)
    }

    override suspend fun searchProfile(userFilter: UserFilter): Response<List<User>> {
        return api.searchProfile(userFilter)
    }

    override suspend fun getProfileById(userId: Int): Response<User> {
        return api.getProfileById(userId)
    }
}