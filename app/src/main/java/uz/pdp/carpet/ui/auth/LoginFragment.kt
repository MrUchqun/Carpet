package uz.pdp.carpet.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.FragmentLoginBinding
import uz.pdp.carpet.model.UserLogin
import uz.pdp.carpet.utils.Constants.PHONE_PREFIX
import uz.pdp.carpet.utils.Constants.USER_LOGIN
import uz.pdp.carpet.utils.Extensions.checkError
import uz.pdp.carpet.utils.Extensions.click
import uz.pdp.carpet.utils.Extensions.hideLoading
import uz.pdp.carpet.utils.Extensions.showLoading
import uz.pdp.carpet.utils.Extensions.text
import uz.pdp.carpet.utils.Extensions.toast
import uz.pdp.carpet.utils.Resource

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _bn: FragmentLoginBinding? = null
    private val bn get() = _bn!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bn = FragmentLoginBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        initViews()
    }

    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    toast(getString(R.string.str_login_success))
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                Resource.Status.ERROR -> {
                    toast(it.message!!)
                    hideLoading(bn.progressbar, bn.btnSignIn)
                }
                Resource.Status.LOADING -> {
                    showLoading(bn.progressbar, bn.btnSignIn)
                }
            }
        }
    }

    private fun initViews() = bn.apply {
        checkEditTexts()

        tvSignUp.click {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnSignIn.click {
            if (etPhone.text().isNotEmpty() && etPassword.text().isNotEmpty()) {
                viewModel.signIn(
                    UserLogin(
                        password = etPassword.text(),
                        phoneNumber = PHONE_PREFIX + etPhone.text()
                    )
                )
            } else {
                toast(getString(R.string.str_fill_all_feads))
            }
        }
    }

    private fun checkEditTexts() {
        bn.etPassword.checkError()
        bn.etPhone.checkError()
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.getState() == USER_LOGIN) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        bn.etPhone.isErrorEnabled = false
        bn.etPassword.isErrorEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }
}