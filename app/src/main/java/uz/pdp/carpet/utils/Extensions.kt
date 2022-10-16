package uz.pdp.carpet.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import uz.pdp.carpet.R


object Extensions {

    fun View.click(click: () -> Unit) {
        setOnClickListener {
            click()
        }
    }

    fun Fragment.toast(msg: String) {
        Snackbar.make(view!!, msg, Snackbar.LENGTH_SHORT).apply {
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                typeface =
                    ResourcesCompat.getFont(requireActivity().applicationContext, R.font.poppins)
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_secondary))
            }
            view.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.color_primary
                )
            )
        }.show()
    }

    fun Fragment.showLoading(progressBar: ProgressBar, button: Button) {
        progressBar.visibility = View.VISIBLE
        button.visibility = View.INVISIBLE
    }

    fun Fragment.hideLoading(progressBar: ProgressBar, button: Button) {
        progressBar.visibility = View.INVISIBLE
        button.visibility = View.VISIBLE
    }

    fun TextInputLayout.text(): String {
        return editText!!.text.toString().trim()
    }

    fun TextInputLayout.checkError() {
        editText!!.doOnTextChanged { text, _, _, _ ->
            if (text!!.isEmpty()) {
                isErrorEnabled = true
                error = context.getString(R.string.str_need_fill)
            } else {
                isErrorEnabled = false
                error = null
            }
        }
    }
}