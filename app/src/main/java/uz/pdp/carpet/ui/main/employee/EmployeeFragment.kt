package uz.pdp.carpet.ui.main.employee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.FragmentEmployeeBinding
import uz.pdp.carpet.model.UserFilter
import uz.pdp.carpet.ui.BaseFragment
import uz.pdp.carpet.utils.Extensions.hide
import uz.pdp.carpet.utils.Extensions.show
import uz.pdp.carpet.utils.Extensions.toast
import uz.pdp.carpet.utils.Resource


@AndroidEntryPoint
class EmployeeFragment : BaseFragment() {

    private var _bn: FragmentEmployeeBinding? = null
    private val bn get() = _bn!!

    private val viewModel: EmployeeViewModel by viewModels()
    private val userAdapter = UserAdapter()
    private var isLoadData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bn = FragmentEmployeeBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        initViews()
        viewModel.loadData()
        isLoadData = true
    }

    private fun initViews() = bn.apply {
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && etSearch.editText!!.text.isEmpty()) {
                        if (isLoadData) viewModel.profileAdmPaginationList()
                        else isLoadData = true
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
                    viewModel.profileAdmPaginationList()
                } else {
                    hide()
                }
            }
        }

        etSearch.apply {
            editText?.apply {
                addTextChangedListener {
                    userAdapter.submitList(null)
                    if (text.isNotEmpty()) {
                        viewModel.searchProfile(UserFilter(name = text.toString()))
                    } else {
                        viewModel.loadData()
                        isLoadData = false
                    }
                }
            }
        }

    }

    private fun observer() {
        viewModel.paginationUserList.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bn.swipeRefreshLayout.hide()
                    userAdapter.updateList(it.data!!)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }
}