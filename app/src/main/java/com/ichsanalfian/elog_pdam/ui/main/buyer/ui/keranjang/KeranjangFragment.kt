package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.keranjang

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichsanalfian.elog_pdam.databinding.FragmentKeranjangBinding
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerViewModel
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerAdapter
import com.ichsanalfian.elog_pdam.ui.main.seller.detail.DetailProdukActivity
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory


class KeranjangFragment : Fragment() {

    private var _binding: FragmentKeranjangBinding? = null
    private val binding get() = _binding!!

    private lateinit var keranjangViewModel: BuyerViewModel
    private lateinit var keranjangAdapter: KeranjangAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keranjangAdapter =KeranjangAdapter()


        binding.rvKeranjang.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = keranjangAdapter
        }

        keranjangViewModel = ViewModelProvider(this, ViewModelFactory())[BuyerViewModel::class.java]
        keranjangViewModel.getKeranjang()
        keranjangViewModel.getBarang().observe(viewLifecycleOwner) { keranjangList ->
            if (keranjangList != null) {
                keranjangAdapter.submitList(keranjangList)
            } else {
                println("KOSONG")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onItemClicked(data: Barang) {
//        // Implementasikan tindakan saat barang di-klik di keranjang
//        // Misalnya, buka tampilan detail barang dalam keranjang
//        val intent = Intent(requireContext(), DetailProdukActivity::class.java)
//        intent.putExtra(DetailProdukActivity.EXTRA_BARANG, data)
//        startActivity(intent)
//    }
}
