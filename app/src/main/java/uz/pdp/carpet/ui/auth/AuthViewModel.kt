package uz.pdp.carpet.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import uz.pdp.carpet.R
import uz.pdp.carpet.model.*
import uz.pdp.carpet.repository.MyRepository
import uz.pdp.carpet.utils.Resource
import uz.pdp.carpet.utils.SingleLiveEvent
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val myRepository: MyRepository,
    private val application: Application
) : ViewModel() {

    private var _register = SingleLiveEvent<Resource<User>>()
    val register: LiveData<Resource<User>> get() = _register

    private var _login = SingleLiveEvent<Resource<User>>()
    val login: LiveData<Resource<User>> get() = _login

    fun signUp(userRegister: UserRegister) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _register.postValue(Resource.loading())
            myRepository.signUp(userRegister).let {
                if (it.isSuccessful) {
                    myRepository.login()
                    myRepository.saveToken(it.body()!!.jwt)
                    _register.postValue(Resource.success(it.body()!!))
                } else {
                    _register.postValue(Resource.error(it.errorBody()!!.string()))
                }
            }
        } catch (e: SocketTimeoutException) {
            _register.postValue(Resource.error(application.getString(R.string.str_network_error)))
        } catch (e: Exception) {
            _register.postValue(Resource.error(application.getString(R.string.str_checking_information)))
        }
    }

    fun signIn(userLogin: UserLogin) = viewModelScope.launch(Dispatchers.IO) {
        _login.postValue(Resource.loading())
        try {
            myRepository.signIn(userLogin).let {
                if (it.isSuccessful) {
                    myRepository.login()
                    myRepository.saveToken(it.body()!!.jwt)
                    _login.postValue(Resource.success(it.body()!!))
                } else {
                    _login.postValue(Resource.error(it.errorBody()!!.string()))
                }
            }
        } catch (e: SocketTimeoutException) {
            _login.postValue(Resource.error(application.getString(R.string.str_network_error)))
        } catch (e: Exception) {
            _login.postValue(Resource.error(application.getString(R.string.str_checking_information)))
        }
    }

    fun logout() = myRepository.logout()
    fun getState() = myRepository.getState()
}