package uz.pdp.carpet.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.pdp.carpet.model.User
import uz.pdp.carpet.model.UserLogin
import uz.pdp.carpet.model.UserRegister

interface ApiService {

    @POST("auth/registration")
    suspend fun signUp(@Body userRegister: UserRegister): Response<User>

    @POST("auth/login")
    suspend fun signIn(@Body userLogin: UserLogin): Response<User>
}