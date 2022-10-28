package uz.pdp.carpet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.databinding.ActivityMainBinding
import uz.pdp.carpet.utils.Extensions.toast


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bn: ActivityMainBinding
    private var countBackPressed = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Carpet)
        bn = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bn.root)
    }

    override fun onBackPressed() {
        val navController = Navigation.findNavController(this.findViewById(R.id.fragment_view))
        if (navController.currentDestination?.id == R.id.employeeFragment && countBackPressed > 0) {
            countBackPressed--
            toast(getString(R.string.str_for_exit))
        } else {
            super.onBackPressed()
            countBackPressed = 2
        }
    }

}