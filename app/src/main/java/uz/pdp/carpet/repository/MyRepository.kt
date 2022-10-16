package uz.pdp.carpet.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.pdp.carpet.data.remote.ApiService
import uz.pdp.carpet.model.User
import uz.pdp.carpet.model.UserLogin
import uz.pdp.carpet.model.UserRegister

interface MyRepository {
    suspend fun signUp(userRegister: UserRegister): Response<User>
    suspend fun signIn(userLogin: UserLogin): Response<User>
    fun login()
    fun logout()
    fun getState(): String
}