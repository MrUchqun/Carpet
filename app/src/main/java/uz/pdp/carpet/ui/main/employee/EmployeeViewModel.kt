package uz.pdp.carpet.ui.main.employee

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.pdp.carpet.R
import uz.pdp.carpet.model.User
import uz.pdp.carpet.repository.MyRepository
import uz.pdp.carpet.repository.MyRepositoryImpl
import uz.pdp.carpet.utils.Resource
import uz.pdp.carpet.utils.SingleLiveEvent
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val myRepository: MyRepository,
    private val application: Application
) : ViewModel() {

    private var _userList = SingleLiveEvent<Resource<List<User>>>()
    val userList: LiveData<Resource<List<User>>> get() = _userList

    private var currentPage = 0
    private var totalPage = 1

    fun loadData() {
        if (currentPage == 0) {
            profileAdmPaginationList()
        }
    }

    fun profileAdmPaginationList() = viewModelScope.launch {
        if (currentPage < totalPage) {
            try {
                _userList.value = Resource.loading()
                myRepository.profileAdmPaginationList(currentPage++, 10).let {
                    if (it.isSuccessful) {
                        totalPage = it.body()!!.totalPages
                        _userList.value = Resource.success(it.body()!!.content)
                    } else {
                        _userList.value = Resource.error(it.errorBody()!!.string())
                    }
                }
            } catch (e: SocketTimeoutException) {
                _userList.value = Resource.error(application.getString(R.string.str_network_error))
            } catch (e: Exception) {
                _userList.value =
                    Resource.error(application.getString(R.string.str_checking_information))
            }
        } else {
            _userList.value = Resource.error(application.getString(R.string.str_thats_all))
        }
    }

}