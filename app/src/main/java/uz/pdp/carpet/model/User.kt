package uz.pdp.carpet.model

data class User(
    val id: Int,
    val jwt: String,
    val name: String,
    val phoneNumber: String,
    val role: String,
    val status: String,
    val surname: String,
    val url: String? = null,
    val visible: Boolean
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

data class UserFilter(
    val createdDateFrom: String? = null,
    val createdDateTo: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val status: String? = null,
    val surname: String? = null
)