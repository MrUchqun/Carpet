package uz.pdp.carpet.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.FragmentRegisterBinding
import uz.pdp.carpet.model.UserRegister
import uz.pdp.carpet.utils.Constants.PHONE_PREFIX
import uz.pdp.carpet.utils.Extensions.checkError
import uz.pdp.carpet.utils.Extensions.click
import uz.pdp.carpet.utils.Extensions.hideLoading
import uz.pdp.carpet.utils.Extensions.showLoading
import uz.pdp.carpet.utils.Extensions.text
import uz.pdp.carpet.utils.Extensions.toast
import uz.pdp.carpet.utils.Resource

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val TAG: String? = RegisterFragment::class.simpleName

    private var _bn: FragmentRegisterBinding? = null
    private val bn get() = _bn!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bn = FragmentRegisterBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        initViews()
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    toast(getString(R.string.str_sign_up_success))
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                Resource.Status.ERROR -> {
                    toast(it.message!!)
                    hideLoading(bn.progressbar, bn.btnSignUp)
                }
                Resource.Status.LOADING -> {
                    showLoading(bn.progressbar, bn.btnSignUp)
                }
            }
        }
    }

    private fun initViews() = bn.apply {
        checkEditTexts()

        btnSignUp.click {
            if (isValid()) {
                viewModel.signUp(
                    UserRegister(
                        configPassword = etConfigPassword.text(),
                        name = etName.text(),
                        password = etPassword.text(),
                        phoneNumber = PHONE_PREFIX + etPhone.text(),
                        surname = etSurname.text()
                    )
                )
            } else if (configPass()) {
                toast(getString(R.string.str_fill_all_feads))
            } else {
                toast(getString(R.string.str_pass_did_not_match))
            }
        }
    }

    private fun checkEditTexts() {
        bn.etPassword.checkError()
        bn.etPhone.checkError()
        bn.etName.checkError()
        bn.etSurname.checkError()
        bn.etConfigPassword.checkError()
    }

    private fun configPass(): Boolean {
        return bn.etPassword.text() == bn.etConfigPassword.text()
    }

    private fun isValid(): Boolean {
        return configPass() && bn.etName.text().isNotEmpty() && bn.etPhone.text()
            .isNotEmpty() && bn.etSurname.text().isNotEmpty() && bn.etPassword.text().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }

}