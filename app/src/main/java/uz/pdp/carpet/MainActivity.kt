package uz.pdp.carpet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.databinding.ActivityMainBinding
import uz.pdp.carpet.utils.Extensions.hideSoftKeyboard


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bn: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Carpet)
        bn = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bn.root)
    }

}