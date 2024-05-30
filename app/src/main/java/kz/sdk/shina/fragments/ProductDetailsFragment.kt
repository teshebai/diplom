package kz.sdk.shina.fragments

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentProductDetailsBinding
import kz.sdk.shina.firebase.UserDao
import javax.inject.Inject


@AndroidEntryPoint
class ProductDetailsFragment:BaseFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate) {


    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao

    private val args:ProductDetailsFragmentArgs by navArgs()
    override fun onBindView() {
        super.onBindView()
        val car =args.product
        with(binding){
            Glide.with(requireContext())
                .load(car.img)
                .placeholder(R.drawable.placeholder)
                .into(img)
            name.text = "${car.title}, ${car.year} года"
            price.text = "${car.price} ₸"
            creditPriceEt.text = "${car.price?.div(72)} ₸"
            perv.text = "${(car.price?.times(0.2))?.toInt()!!} ₸"
            calculateCredit.setOnClickListener{
                Toast.makeText(requireContext(),  "${car.price?.div(72)} ₸/мес", Toast.LENGTH_SHORT).show()
            }
            type.text = car.type
            volume.text = car.volume.toString()
            millage.text = "${car.millage} км"
            transmission.text = car.transmission
            color.text = car.color
            phoneBtn.setOnClickListener {
                startDialer("87072224556")
            }
            favButton.setOnClickListener {
                userDao.saveProductToList(car)
                showCustomDialog("Успешно" , "Добавлено в избранное")
            }
        }
    }
    private fun startDialer(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        binding.credit.isVisible = args.product.isCredit
    }
}