package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ichsanalfian.elog_pdam.adapter.GridAdapter
import com.ichsanalfian.elog_pdam.databinding.FragmentBerandaBinding
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory


class BerandaFragment : Fragment() {

    private lateinit var binding: FragmentBerandaBinding
    private lateinit var sellerViewModel: SellerViewModel
    private lateinit var sellerAdapter: GridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerAdapter = GridAdapter(1)

        // Gunakan GridLayoutManager dengan 2 kolom
        binding?.rvProdukBuyer?.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = sellerAdapter
        }

        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]
        sellerViewModel.setBarangBuyer()
        sellerViewModel.getBarang().observe(viewLifecycleOwner) { listBarang ->
            if (listBarang != null) {
                sellerAdapter.submitList(listBarang)
            } else {
                println("LIST NULL NGABS!!")
                binding.emptyData2.visibility = View.VISIBLE
            }
        }
        sellerViewModel.isLoad().observe(viewLifecycleOwner){
            showLoading(it)
        }

        // Panggil fungsi untuk mengatur search bar di Toolbar
        setupSearchBar()
    }
    private fun setupSearchBar() {

        val searchView = binding!!.searchView2

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
        sellerViewModel.searchBarang(query).observe(viewLifecycleOwner, Observer { listBarang ->
            listBarang?.let {
                sellerAdapter.submitList(it)
            }
        })
    }

    private fun showLoading(state: Boolean){
//        if (state) View.VISIBLE else View.GONE
        if (state){
            binding?.progressBar?.visibility = View.VISIBLE
        } else{
            binding?.progressBar?.visibility = View.GONE
        }
    }
}