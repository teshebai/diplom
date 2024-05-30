package kz.sdk.shina.fragments

import android.net.Uri
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentAdminBinding
import kz.sdk.shina.models.Product
import javax.inject.Inject


@AndroidEntryPoint
class AdminFragment:BaseFragment<FragmentAdminBinding>(FragmentAdminBinding::inflate) {
    override var showBottomNavigation = false

    private var imageUri: Uri? = null
    private var isCredit:Boolean = false
    @Inject
    lateinit var storageReference: StorageReference

    private val imageResultLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.img.setImageURI(it)
            imageUri = it
            binding.textImg.isVisible = false
        }
    }


    override fun onBindView() {
        super.onBindView()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.creditCheckbox.setOnCheckedChangeListener { _, isChecked ->
            isCredit = isChecked
        }
        binding.uploadImg.setOnClickListener {
            selectEventImage()
        }
        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty() || binding.yearInput.text.isNullOrEmpty() ||
                binding.millageInput.text.isNullOrEmpty() || binding.colorInput.text.isNullOrEmpty() ||
                binding.typeInput.text.isNullOrEmpty() || binding.volumeInput.text.isNullOrEmpty() ||
                binding.transmissionInput.text.isNullOrEmpty() || binding.priceInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val price = binding.priceInput.text.toString().toInt()
                    val year = binding.yearInput.text.toString().toInt()
                    val volume = binding.volumeInput.text.toString().toInt()
                    val millage = binding.millageInput.text.toString().toInt()
                    uploadImage { imageUrl ->
                        saveEventToDatabase(binding.nameInput.text.toString(), imageUrl, price, year, binding.typeInput.text.toString(), binding.transmissionInput.text.toString(), volume, millage, binding.colorInput.text.toString(), isCredit)
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Пожалуйста, введите действительные числовые значения", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    fun selectEventImage() {
        imageResultLauncher.launch("image/*")
    }

    private fun uploadImage(callback: (String) -> Unit) {
        imageUri?.let { uri ->
            binding.img.setImageURI(uri)
            val ref = storageReference.child(uri.lastPathSegment ?: "temp")
            ref.putFile(uri).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    callback(downloadUri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveEventToDatabase(name: String, img:String, price:Int,
                                    year:Int, type:String, transmission:String,
                                    volume:Int, millage:Int, color:String,
                                    isCredit:Boolean) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Cars")
        val eventId = databaseReference.push().key

        val event = Product(title =  name, img = img, price = price, year = year, type = type, transmission = transmission, volume = volume, millage = millage, color = color, isCredit = isCredit)
        eventId?.let {
            databaseReference.child(it).setValue(event).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Объявление добавлено успешно", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_adminFragment_to_homeFragment)
                } else {
                    Toast.makeText(context, "Failed to create event: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}