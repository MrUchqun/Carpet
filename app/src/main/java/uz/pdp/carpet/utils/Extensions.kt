package uz.pdp.carpet.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import uz.pdp.carpet.R
import java.util.*
import kotlin.collections.ArrayList


object Extensions {

    fun Activity.toast(msg: String?) {
        Snackbar.make(findViewById(android.R.id.content), msg!!, Snackbar.LENGTH_SHORT).apply {
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                typeface =
                    ResourcesCompat.getFont(this@toast, R.font.poppins)
                setTextColor(ContextCompat.getColor(this@toast, R.color.color_secondary))
            }
            view.setBackgroundResource(R.drawable.rounded_background)
        }.show()
    }


    fun Fragment.hideSoftKeyboard(
        view: View? = requireActivity().currentFocus
    ) {
        view?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        view?.clearFocus()
    }

    fun Fragment.toast(msg: String?) {
        Snackbar.make(requireParentFragment().requireView(), msg!!, Snackbar.LENGTH_SHORT).apply {
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                typeface =
                    ResourcesCompat.getFont(requireActivity().applicationContext, R.font.poppins)
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_secondary))
            }
            view.setBackgroundResource(R.drawable.rounded_background)
        }.show()
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

    fun EditText.getSearchText(): List<String> =
        text.toString().trim().lowercase(Locale.getDefault()).split(" ")


    fun SwipeRefreshLayout.show() {
        isRefreshing = true
    }

    fun SwipeRefreshLayout.hide() {
        isRefreshing = false
    }

    fun View.click(click: () -> Unit) {
        setOnClickListener {
            click()
        }
    }

    fun View.hide() {
        visibility = View.INVISIBLE
    }

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }
}