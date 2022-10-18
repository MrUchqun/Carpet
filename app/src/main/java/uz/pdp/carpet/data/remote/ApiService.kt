package uz.pdp.carpet.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import uz.pdp.carpet.model.PageResponse
import uz.pdp.carpet.model.User
import uz.pdp.carpet.model.UserLogin
import uz.pdp.carpet.model.UserRegister

interface ApiService {

    @POST("auth/registration")
    suspend fun signUp(@Body userRegister: UserRegister): Response<User>

    @POST("auth/login")
    suspend fun signIn(@Body userLogin: UserLogin): Response<User>

    @GET("profile/adm/pagination/list")
    suspend fun profileAdmPaginationList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PageResponse>
}