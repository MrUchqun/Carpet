package uz.pdp.carpet.model

data class User(
    val id: Any,
    val jwt: String,
    val name: String,
    val phoneNumber: String,
    val role: String,
    val status: Any,
    val surname: String,
    val url: Any,
    val visible: Any
)

data class UserRegister(
    val configPassword: String,
    val name: String,
    val password: String,
    val phoneNumber: String,
    val surname: String
)

data class UserLogin(
    val password: String,
    val phoneNumber: String
)

data class AuthErrorResponse(
    val error: String,
    val path: String,
    val status: Int,
    val timestamp: String
)