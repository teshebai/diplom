package kz.sdk.shina.fragments

import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override var showBottomNavigation: Boolean = false

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onBindView() {
        super.onBindView()
        coroutineScope.launch {
            delay(3000)
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}