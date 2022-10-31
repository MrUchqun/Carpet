package uz.pdp.carpet.data.remote

import retrofit2.Response
import retrofit2.http.*
import uz.pdp.carpet.model.*

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

    @POST("profile/adm/filter")
    suspend fun searchProfile(@Body userFilter: UserFilter): Response<List<User>>

    @GET("profile/adm/{id}")
    suspend fun getProfileById(@Path("id") userId: Int): Response<User>

    @PUT("profile/adm/{id}")
    suspend fun updateProfile(@Path("id") id: Int, @Body user: User): Response<User>

    @DELETE("/profile/adm")
    suspend fun deleteProfileById(@Query("id") id: Int): Response<User>
}