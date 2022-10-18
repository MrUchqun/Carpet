package uz.pdp.carpet.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.pdp.carpet.data.remote.ApiService
import uz.pdp.carpet.model.PageResponse
import uz.pdp.carpet.model.User
import uz.pdp.carpet.model.UserLogin
import uz.pdp.carpet.model.UserRegister

interface MyRepository {

    // for Auth
    suspend fun signUp(userRegister: UserRegister): Response<User>
    suspend fun signIn(userLogin: UserLogin): Response<User>
    fun login()
    fun logout()
    fun getState(): String
    fun saveToken(jwt: String)

    // for Employee Page
    suspend fun profileAdmPaginationList(page: Int, size: Int): Response<PageResponse>
}