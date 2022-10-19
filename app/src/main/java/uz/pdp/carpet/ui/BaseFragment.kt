package uz.pdp.carpet.ui

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import uz.pdp.carpet.R
import uz.pdp.carpet.ui.auth.AuthViewModel
import uz.pdp.carpet.utils.Extensions.toast

open class BaseFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()

    fun logout(msg: String?) {
        if (msg == requireContext().getString(R.string.str_relogin)) {
            authViewModel.logout()
            Navigation.findNavController(
                requireActivity().findViewById<FragmentContainerView>(
                    R.id.nav_host
                )
            )
                .navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    fun showLoading(progressBar: ProgressBar, button: Button) {
        progressBar.visibility = View.VISIBLE
        button.visibility = View.INVISIBLE
    }

    fun hideLoading(progressBar: ProgressBar, button: Button) {
        progressBar.visibility = View.INVISIBLE
        button.visibility = View.VISIBLE
    }

}