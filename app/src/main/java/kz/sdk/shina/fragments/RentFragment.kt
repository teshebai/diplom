package kz.sdk.shina.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.shina.adapters.FilterAdapter
import kz.sdk.shina.adapters.ProductAdapter
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentRentBinding
import kz.sdk.shina.models.Filter
import kz.sdk.shina.models.Product



@AndroidEntryPoint
class RentFragment:BaseFragment<FragmentRentBinding>(FragmentRentBinding::inflate) {
    private lateinit var adapter: ProductAdapter

    private lateinit var filterAdapter: FilterAdapter

    private var selectedFilterTitle: String? = null

    override fun onBindView() {
        super.onBindView()
        adapter = ProductAdapter()
        filterAdapter = FilterAdapter()
        with(binding) {
            rentRecycler.adapter = adapter
            rentRecycler.layoutManager = LinearLayoutManager(requireContext())
            filterRecycler.adapter = filterAdapter
            filterRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        loadEvents()

        adapter.itemClick =  {
            findNavController().navigate(
                RentFragmentDirections.actionRentFragmentToProductRentFragment(it)
            )
        }
        filterAdapter.itemClick = {
            selectedFilterTitle = it.title
            loadEvents()
        }
        filterAdapter.submitList(getFilters())

    }
    private fun getFilters():List<Filter>{
        return listOf(
            Filter(1, "Пробег"),
            Filter(2, "По алфавиту"),
            Filter(3, "Цена"),

            )
    }
    private fun loadEvents() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("rentCars")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                snapshot.children.mapNotNullTo(products) {
                    it.getValue(Product::class.java)
                }
                products.sortWith(compareBy {
                    when (selectedFilterTitle) {
                        "Пробег" -> it.millage
                        "По алфавиту" -> it.title
                        "Цена" -> it.price
                        else -> it.title
                    }
                })
                adapter.submitList(products)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load cars: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}