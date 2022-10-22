package uz.pdp.carpet.ui.main.employee

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.DialogUserFilterBinding
import uz.pdp.carpet.databinding.FragmentEmployeeBinding
import uz.pdp.carpet.model.UserFilter
import uz.pdp.carpet.ui.BaseFragment
import uz.pdp.carpet.utils.Constants
import uz.pdp.carpet.utils.Extensions.click
import uz.pdp.carpet.utils.Extensions.getSearchText
import uz.pdp.carpet.utils.Extensions.hide
import uz.pdp.carpet.utils.Extensions.hideSoftKeyboard
import uz.pdp.carpet.utils.Extensions.show
import uz.pdp.carpet.utils.Extensions.text
import uz.pdp.carpet.utils.Extensions.toast
import uz.pdp.carpet.utils.Resource
import java.util.*


@AndroidEntryPoint
class EmployeeFragment : BaseFragment() {

    private var _bn: FragmentEmployeeBinding? = null
    private val bn get() = _bn!!

    private val viewModel: EmployeeViewModel by viewModels()
    private val userAdapter = UserAdapter()

    private var isFilter = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bn = FragmentEmployeeBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onResume() {
        super.onResume()
        setupUI(bn.parentView, bn.etSearch.editText!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        initViews()
        loadData()
    }

    private fun initViews() = bn.apply {

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && etSearch.editText!!.text.isEmpty() && viewModel.currentPage > 0 && !isFilter) {
                        viewModel.profileAdmPaginationList()
                    }
                }
            })
        }

        swipeRefreshLayout.apply {
            setColorSchemeResources(
                R.color.color_primary,
                R.color.color_secondary,
                R.color.color_success,
                R.color.color_error
            )

            setOnRefreshListener {
                if (etSearch.editText!!.text.isEmpty()) {
                    isFilter = false
                    loadData()
                } else {
                    hide()
                }
            }
        }

        etSearch.apply {
            editText?.apply {
                addTextChangedListener {
                    isFilter = false
                    if (text().isNotEmpty()) {
                        getSearchText().apply {
                            filterUser(
                                when (size) {
                                    1 -> UserFilter(name = get(0))
                                    2 -> UserFilter(name = get(0), surname = get(1))
                                    else -> UserFilter(name = "${get(0)} ${get(1)} ${get(2)}")
                                }
                            )
                        }
                    } else {
                        loadData()
                    }
                }


                setOnEditorActionListener { _, i, keyEvent ->
                    if (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_DONE) {
                        hideSoftKeyboard(this)
                    }
                    false
                }

            }

            setEndIconOnClickListener {
                openFilterDialog()
            }
        }

    }

    private fun openFilterDialog() {

        val fDialogBn = DialogUserFilterBinding.inflate(layoutInflater)
        val fDialog = Dialog(requireContext(), R.style.Theme_Carpet)

        var name: String?
        var surname: String?
        var phoneNumber: String?
        var role: String? = null
        var status: String? = null

        fDialog.apply {
            setContentView(fDialogBn.root)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.attributes = LayoutParams().apply {
                copyFrom(window?.attributes)
                width = LayoutParams.MATCH_PARENT
                height = LayoutParams.MATCH_PARENT
            }
            show()

            fDialogBn.apply {
                toggleButtonRole.addOnButtonCheckedListener { group, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.role_admin -> role = Constants.STR_ADMIN
                            R.id.role_employee -> role = Constants.STR_EMPLOYEE
                            R.id.role_customer -> role = Constants.STR_CUSTOMER
                        }
                    } else if (group.checkedButtonId == View.NO_ID) {
                        role = null
                    }
                }

                toggleButtonStatus.addOnButtonCheckedListener { group, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.status_active -> status = Constants.STR_ACTIVE
                            R.id.status_not_active -> status = Constants.STR_NOT_ACTIVE
                            R.id.status_blocked -> status = Constants.STR_BLOCKED
                        }
                    } else if (group.checkedButtonId == View.NO_ID) {
                        status = null
                    }
                }

                btnApply.click {
                    isFilter = true
                    name = if (etName.text().isNotEmpty()) etName.text().lowercase() else null
                    surname =
                        if (etSurname.text().isNotEmpty()) etSurname.text().lowercase() else null
                    phoneNumber = if (etPhone.text().isNotEmpty()) "+998" + etPhone.text() else null
                    filterUser(
                        UserFilter(
                            name = name,
                            surname = surname,
                            phoneNumber = phoneNumber,
                            role = role,
                            status = status
                        )
                    )
                    dismiss()
                }

                btnCancel.click {
                    isFilter = false
                    dismiss()
                }
            }
        }
    }

    private fun observer() {
        viewModel.paginationUserList.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bn.swipeRefreshLayout.hide()
                    userAdapter.updateList(it.data!!.toMutableList())
                }
                Resource.Status.ERROR -> {
                    bn.swipeRefreshLayout.hide()
                    it.message.let { msg ->
                        toast(msg)
                        logout(msg)
                    }
                }
                Resource.Status.LOADING -> {
                    bn.swipeRefreshLayout.show()
                }
            }
        }
    }

    private fun filterUser(userFilter: UserFilter) {
        userAdapter.submitList(null)
        viewModel.searchProfile(userFilter)
    }

    private fun loadData() {
        userAdapter.submitList(null)
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }
}