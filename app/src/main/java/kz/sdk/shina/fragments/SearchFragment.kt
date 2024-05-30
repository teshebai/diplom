package kz.sdk.shina.fragments

import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.shina.adapters.ProductAdapter
import kz.sdk.shina.base.BaseFragment
import kz.sdk.shina.databinding.FragmentSearchBinding
import kz.sdk.shina.models.Product

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private lateinit var adapter: ProductAdapter
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Cars")

    override fun onBindView() {
        super.onBindView()
        adapter = ProductAdapter()
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        loadProducts()

        adapter.itemClick = { product ->
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(product)
            )
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = binding.editText.text.toString()
                if (searchText.isNotEmpty()) {
                    searchProduct(searchText)
                } else {
                    Toast.makeText(requireContext(), "Введите название товара", Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
    }

    private fun loadProducts() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                snapshot.children.forEach { dataSnapshot ->
                    dataSnapshot.getValue(Product::class.java)?.let { product ->
                        products.add(product)
                    }
                }
                adapter.submitList(products)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load products: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchProduct(input: String) {
        databaseReference.orderByChild("title").startAt(input).endAt(input + "\uf8ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val searchResults = mutableListOf<Product>()
                    snapshot.children.forEach { dataSnapshot ->
                        dataSnapshot.getValue(Product::class.java)?.let { product ->
                            searchResults.add(product)
                        }
                    }
                    adapter.submitList(searchResults)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Search failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
