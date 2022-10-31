package uz.pdp.carpet.ui.main.employee

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.DialogCheckBinding
import uz.pdp.carpet.databinding.FragmentUpdateUserBinding
import uz.pdp.carpet.model.User
import uz.pdp.carpet.ui.BaseFragment
import uz.pdp.carpet.utils.Constants
import uz.pdp.carpet.utils.Constants.STR_CANCELED
import uz.pdp.carpet.utils.Constants.STR_USER_ID
import uz.pdp.carpet.utils.Extensions.click
import uz.pdp.carpet.utils.Extensions.gone
import uz.pdp.carpet.utils.Extensions.hide
import uz.pdp.carpet.utils.Extensions.show
import uz.pdp.carpet.utils.Extensions.toast
import uz.pdp.carpet.utils.Resource
import java.util.*

@AndroidEntryPoint
class UpdateUserFragment : BaseFragment() {

    private var _bn: FragmentUpdateUserBinding? = null
    private val bn get() = _bn!!

    private val viewModel: EmployeeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun initViews(user: User) = bn.apply {
        user.apply {

            Glide.with(this@UpdateUserFragment).load(
                if (url != null) Constants.BASE_URL + url.substring(22)
                else R.drawable.img_man
            ).into(ivProfile)

            tvFullName.text = "$name $surname"
            tvPhone.text = phoneNumber

            ivCall.click {
                requireActivity().startActivity(Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                })
            }

            ivSms.click {
                requireActivity().startActivity(Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$phoneNumber")
                })
            }

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

            ivBack.click {
                requireActivity().onBackPressed()
            }

            btnSave.click {
                viewModel.updateUser(
                    id = id!!, user = User(
                        name = name,
                        surname = surname,
                        phoneNumber = phoneNumber,
                        role = tvRole.text.toString().uppercase(),
                        status = tvStatus.text.toString().uppercase()
                    )
                )
            }

            btnVisible.apply {
                setText(if (visible == false) R.string.str_visible_user else R.string.str_invisible_user)
                click {
                    viewModel.deleteProfile(user.id!!)
                }
            }
        }
    }

    private fun observer() = bn.apply {
        viewModel.apply {
            profileById.observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        frameBackground.hide()
                        progressbarMain.gone()
                        initViews(it.data!!)
                    }
                    Resource.Status.ERROR -> {
                        progressbarMain.gone()
                        it.message.let { msg ->
                            toast(msg)
                            logout(msg)
                        }
                    }
                    Resource.Status.LOADING -> {
                        frameBackground.show()
                        progressbarMain.show()
                    }
                }
            }

            updateUser.observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        btnSave.show()
                        progressbarSave.gone()
                        toast(getString(R.string.str_user_updated))
                        changeData()
                    }
                    Resource.Status.ERROR -> {
                        btnSave.show()
                        progressbarSave.gone()
                        it.message.let { msg ->
                            toast(msg)
                            logout(msg)
                        }
                    }
                    Resource.Status.LOADING -> {
                        btnSave.hide()
                        progressbarSave.show()
                    }
                }
            }

            deleteUser.observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        btnVisible.show()
                        progressbarVisible.gone()
                        toast(getString(R.string.str_user_updated))
                        changeData()
                    }
                    Resource.Status.ERROR -> {
                        btnVisible.show()
                        progressbarVisible.gone()
                        it.message.let { msg ->
                            toast(msg)
                            logout(msg)
                        }
                    }
                    Resource.Status.LOADING -> {
                        btnVisible.hide()
                        progressbarVisible.show()
                    }
                }
            }
        }
    }

    private fun changeData() {
        findNavController().navigate(R.id.action_updateUserFragment_to_employeeFragment,
            Bundle().apply {
                putBoolean(STR_CANCELED, true)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }

}