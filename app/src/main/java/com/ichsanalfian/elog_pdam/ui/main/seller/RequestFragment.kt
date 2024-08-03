package com.ichsanalfian.elog_pdam.ui.main.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.adapter.GridAdapter
import com.ichsanalfian.elog_pdam.adapter.LinearAdapter
import com.ichsanalfian.elog_pdam.databinding.FragmentRequestBinding
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

class RequestFragment : Fragment() {
    private lateinit var binding: FragmentRequestBinding
    private lateinit var sellerViewModel: SellerViewModel
    private lateinit var reqAdapter: LinearAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reqAdapter = LinearAdapter(4)

        // Gunakan GridLayoutManager dengan 2 kolom
        val layout = LinearLayoutManager(requireContext())
        binding.rvProdukRequest.apply {
            layoutManager = layout
            adapter = reqAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), layout.orientation))
        }

        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]
        sellerViewModel.getRequestSeller()
        sellerViewModel.getBarang().observe(viewLifecycleOwner) { listBarang ->
            if (listBarang != null) {
                reqAdapter.submitList(listBarang)
            } else {
                println("LIST NULL NGABS!!")
            }
        }

        sellerViewModel.isLoad().observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setupSearchBar() {
        val activity = requireActivity() // Get the activity reference
        val searchView = activity.findViewById<SearchView>(R.id.searchView) // Find the searchView from activity's layout

        // Setel query hint (teks yang muncul saat search view kosong)
        searchView.queryHint = "Cari Produk"

        // Implementasikan listener untuk menghandle perubahan query pada search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan query
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan teks yang berubah
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        // Lakukan pencarian berdasarkan query di view model
        sellerViewModel.searchBarang(query).observe(viewLifecycleOwner) { listBarang ->
            listBarang?.let {
                reqAdapter.submitList(it)
            }
        }
    }

    private fun showLoading(state: Boolean){
//        if (state) View.VISIBLE else View.GONE
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.GONE
        }
    }


}