package uz.pdp.carpet.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import uz.pdp.carpet.R
import uz.pdp.carpet.ui.auth.AuthViewModel
import uz.pdp.carpet.utils.Extensions.hideSoftKeyboard


open class BaseFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()

    fun logout(msg: String?) {
        if (msg == requireContext().getString(R.string.str_relogin)) {
            authViewModel.logout()
            Navigation.findNavController(
                requireActivity().findViewById<FragmentContainerView>(
                    R.id.nav_host
                )
            ).navigate(R.id.action_mainFragment_to_loginFragment)
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

    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(view: View, editText: EditText) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard(editText)
                false
            }
        }

        // If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView, editText)
            }
        }
    }
}