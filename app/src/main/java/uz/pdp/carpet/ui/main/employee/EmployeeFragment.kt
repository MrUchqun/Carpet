package uz.pdp.carpet.ui.main.employee

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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

@AndroidEntryPoint
class EmployeeFragment : BaseFragment() {

    private var _bn: FragmentEmployeeBinding? = null
    private val bn get() = _bn!!

    private val viewModel: EmployeeViewModel by viewModels()
    private val userAdapter = UserAdapter()

    private var _fDialogBinding: DialogUserFilterBinding? = null
    private val fDialogBinding get() = _fDialogBinding!!
    private lateinit var fDialog: Dialog
    private var isFilter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("EMPLOYEE_FRAGMENT", "onCreate")

        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        Log.d("EMPLOYEE_FRAGMENT", "onCreateView")


        _bn = FragmentEmployeeBinding.inflate(inflater, container, false)
        _fDialogBinding = DialogUserFilterBinding.inflate(inflater)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("EMPLOYEE_FRAGMENT", "onViewCreated")

        observer()
        initViews()
        setupUI(view, bn.etSearch.editText!!)
    }

    private fun initViews() = bn.apply {
        fDialog = Dialog(requireContext(), R.style.Theme_Carpet).apply {
            setContentView(fDialogBinding.root)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.attributes = LayoutParams().apply {
                copyFrom(window?.attributes)
                width = LayoutParams.MATCH_PARENT
                height = LayoutParams.MATCH_PARENT
            }
        }

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
                    if (isFilter) closeFilterDialog()
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
                    if (text.isNotEmpty()) {
                        setEndIconDrawable(R.drawable.ic_close)
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
                        }
                    } else {
                        setEndIconDrawable(R.drawable.ic_filter)
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
                editText?.apply {
                    if (text!!.isEmpty()) openFilterDialog()
                    else text.clear()
                }
            }
        }

    }

    private fun openFilterDialog() {
        fDialog.show()
        clearFocusDialog()

        fDialogBinding.apply {

            btnApply.click {
                isFilter = true

                filterUser(
                    UserFilter(
                        name = etName.text().lowercase().ifEmpty { null },
                        surname = etSurname.text().lowercase().ifEmpty { null },
                        phoneNumber = etPhone.text().ifEmpty { null },

                        role = when (toggleButtonRole.checkedButtonId) {
                            R.id.role_admin -> Constants.STR_ADMIN
                            R.id.role_employee -> Constants.STR_EMPLOYEE
                            R.id.role_customer -> Constants.STR_CUSTOMER
                            else -> null
                        },

                        status = when (toggleButtonStatus.checkedButtonId) {
                            R.id.status_active -> Constants.STR_ACTIVE
                            R.id.status_not_active -> Constants.STR_NOT_ACTIVE
                            R.id.status_blocked -> Constants.STR_BLOCKED
                            else -> null
                        }
                    )
                )

                fDialog.dismiss()
            }

            btnCancel.click {
                if (isFilter) loadData()
                closeFilterDialog()
            }

            ivClose.click {
                fDialog.dismiss()
            }

            toggleButtonRole.addOnButtonCheckedListener { _, _, _ ->
                clearFocusDialog()
            }

            toggleButtonStatus.addOnButtonCheckedListener { _, _, _ ->
                clearFocusDialog()
            }
        }
    }

    private fun clearFocusDialog() = fDialogBinding.apply {
        hideSoftKeyboard(etSurname.editText!!)
        hideSoftKeyboard(etPhone.editText!!)
        hideSoftKeyboard(etName.editText!!)
    }

    private fun closeFilterDialog() {
        isFilter = false
        fDialogBinding.apply {
            etName.editText?.text?.clear()
            etSurname.editText?.text?.clear()
            etPhone.editText?.text?.clear()
            toggleButtonRole.clearChecked()
            toggleButtonStatus.clearChecked()
        }
        fDialog.dismiss()
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

        Log.d("EMPLOYEE_FRAGMENT", "onDestroyView")

        _bn = null
    }
}