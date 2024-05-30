package kz.sdk.shina.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentProductRentBinding
import kz.sdk.shina.firebase.UserDao
import javax.inject.Inject


@AndroidEntryPoint
class ProductRentFragment:BaseFragment<FragmentProductRentBinding>(FragmentProductRentBinding::inflate) {
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
            type.text = car.type
            volume.text = car.volume.toString()
            millage.text = "${car.millage} км"
            transmission.text = car.transmission
            color.text = car.color
            favButton.setOnClickListener {
                userDao.saveProductRentToList(car)
                showCustomDialog("Успешно" , "Добавлено в избранное")
            }
            nextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_productRentFragment_to_agreementFragment)
            }
        }
    }

}