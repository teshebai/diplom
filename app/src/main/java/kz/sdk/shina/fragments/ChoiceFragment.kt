package kz.sdk.shina.fragments

import androidx.navigation.fragment.findNavController
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentChoiceBinding

class ChoiceFragment:BaseFragment<FragmentChoiceBinding>(FragmentChoiceBinding::inflate) {

    override var showBottomNavigation = false
    override fun onBindView() {
        super.onBindView()
        binding.rentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_choiceFragment_to_createRentFragment)
        }
        binding.sellBtn.setOnClickListener {
            findNavController().navigate(R.id.action_choiceFragment_to_adminFragment)
        }
        binding.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }

}