package uz.pdp.carpet.ui.main.employee

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.pdp.carpet.R
import uz.pdp.carpet.model.User
import uz.pdp.carpet.model.UserFilter
import uz.pdp.carpet.repository.MyRepository
import uz.pdp.carpet.utils.Resource
import uz.pdp.carpet.utils.SingleLiveEvent
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val myRepository: MyRepository,
    private val application: Application
) : ViewModel() {

    private var _paginationUserList = SingleLiveEvent<Resource<List<User>>>()
    val paginationUserList: LiveData<Resource<List<User>>> get() = _paginationUserList

    var currentPage = 0
    private var totalPage = 1

    fun loadData() {
        currentPage = 0
        totalPage = 1
        profileAdmPaginationList()
    }


    fun profileAdmPaginationList() = viewModelScope.launch {
        if (currentPage < totalPage) {
            try {
                _paginationUserList.value = Resource.loading()

                myRepository.profileAdmPaginationList(currentPage, 10).let {
                    if (it.isSuccessful) {
                        _paginationUserList.value = Resource.success(it.body()!!.content)

                        totalPage = it.body()!!.totalPages
                        currentPage++

                    } else if (it.code() == 401) {
                        _paginationUserList.value =
                            Resource.error(application.getString(R.string.str_relogin))
                    } else {
                        _paginationUserList.value = Resource.error(it.errorBody()!!.string())
                    }
                }

            } catch (e: SocketTimeoutException) {
                _paginationUserList.value =
                    Resource.error(application.getString(R.string.str_network_error))
            } catch (e: Exception) {
                _paginationUserList.value =
                    Resource.error(application.getString(R.string.str_checking_information))
            }
        } else {
            _paginationUserList.value =
                Resource.error(application.getString(R.string.str_thats_all))
        }
    }

    fun searchProfile(userFilter: UserFilter) = viewModelScope.launch {
        try {
            _paginationUserList.value = Resource.loading()

            myRepository.searchProfile(userFilter).let {
                if (it.isSuccessful) {
                    _paginationUserList.value = Resource.success(it.body()!!)
                } else if (it.code() == 401) {
                    _paginationUserList.value =
                        Resource.error(application.getString(R.string.str_relogin))
                } else {
                    _paginationUserList.value = Resource.error(it.errorBody()!!.string())
                }
            }

        } catch (e: SocketTimeoutException) {
            _paginationUserList.value =
                Resource.error(application.getString(R.string.str_network_error))
        } catch (e: Exception) {
            _paginationUserList.value =
                Resource.error(application.getString(R.string.str_checking_information))
        }
    }

}