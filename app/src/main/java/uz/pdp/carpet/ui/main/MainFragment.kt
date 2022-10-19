package uz.pdp.carpet.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.carpet.R
import uz.pdp.carpet.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _bn: FragmentMainBinding? = null
    private val bn get() = _bn!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bn = FragmentMainBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBnv()
    }

    private fun setupBnv() {
        NavigationUI.setupWithNavController(
            bn.bottomNavigation,
            Navigation.findNavController(requireActivity(), R.id.fragment_view)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bn = null
    }
}