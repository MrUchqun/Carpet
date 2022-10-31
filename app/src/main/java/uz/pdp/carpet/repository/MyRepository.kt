package uz.pdp.carpet.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.pdp.carpet.data.remote.ApiService
import uz.pdp.carpet.model.*

interface MyRepository {

    // for Auth
    suspend fun signUp(userRegister: UserRegister): Response<User>
    suspend fun signIn(userLogin: UserLogin): Response<User>
    fun login()
    fun logout()
    fun getState(): String
    fun saveToken(jwt: String?)

    // for Employee Page
    suspend fun profileAdmPaginationList(page: Int, size: Int): Response<PageResponse>
    suspend fun searchProfile(userFilter: UserFilter): Response<List<User>>

    // for Update user page
    suspend fun getProfileById(userId: Int): Response<User>
    suspend fun updateProfile(id: Int, user: User): Response<User>
    suspend fun deleteProfile(id: Int): Response<User>
}