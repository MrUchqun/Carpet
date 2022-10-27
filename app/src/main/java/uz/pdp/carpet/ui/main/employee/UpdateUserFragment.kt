package uz.pdp.carpet.ui.main.employee

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.FragmentUpdateUserBinding
import uz.pdp.carpet.model.User
import uz.pdp.carpet.ui.BaseFragment
import uz.pdp.carpet.utils.Constants
import uz.pdp.carpet.utils.Constants.STR_USER_ID
import uz.pdp.carpet.utils.Extensions.hide
import uz.pdp.carpet.utils.Extensions.show
import uz.pdp.carpet.utils.Extensions.toast
import uz.pdp.carpet.utils.Resource

@AndroidEntryPoint
class UpdateUserFragment : BaseFragment() {

    private var _bn: FragmentUpdateUserBinding? = null
    private val bn get() = _bn!!

    private val viewModel: EmployeeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bn = FragmentUpdateUserBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        loadData()
    }

    private fun loadData() {
        if (arguments != null) {
            val userId = arguments?.get(STR_USER_ID) as Int
            viewModel.getUserById(userId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(user: User) = bn.apply {
        user.apply {
            tvFullName.text = "$name $surname"
            tvPhone.text = phoneNumber
            tvStatus.apply {
                setText(status)
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        resources.getStringArray(R.array.status_list)
                    )
                )
            }
            tvRole.apply {
                setText(role)
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        resources.getStringArray(R.array.role_list)
                    )
                )
            }
            Glide.with(this@UpdateUserFragment)
                .load(
                    if (url != null) Constants.BASE_URL + url.substring(22)
                    else R.drawable.img_man
                )
                .into(ivProfile)
        }
    }

    private fun observer() = bn.apply {
        viewModel.profileById.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    frameBackground.visibility = View.INVISIBLE
                    progressbar.visibility = View.GONE

                    initViews(it.data!!)
                }
                Resource.Status.ERROR -> {
                    progressbar.visibility = View.GONE
                    it.message.let { msg ->
                        toast(msg)
                        logout(msg)
                    }
                }
                Resource.Status.LOADING -> {
                    frameBackground.visibility = View.VISIBLE
                    progressbar.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }

}