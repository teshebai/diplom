package kz.sdk.shina.fragments

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentAgreementBinding

class AgreementFragment:BaseFragment<FragmentAgreementBinding>(FragmentAgreementBinding::inflate) {
    override var showBottomNavigation = false

    private var selectedFilePath: String? = null
    override fun onBindView() {
        super.onBindView()

        binding.uploadRsa.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 100)
        }
        binding.confirmButton.setOnClickListener {

            showCustomDialog("Успех", "Договор успешно подписан!")
            findNavController().navigate(R.id.action_agreementFragment_to_paymentFragment)

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedFilePath = data?.data?.path
            Toast.makeText(context, "File Selected: $selectedFilePath", Toast.LENGTH_LONG).show()
        }
    }
}