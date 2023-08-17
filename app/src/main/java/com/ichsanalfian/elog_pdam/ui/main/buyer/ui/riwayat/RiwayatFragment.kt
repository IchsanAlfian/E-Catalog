package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichsanalfian.elog_pdam.databinding.FragmentKeranjangBinding
import com.ichsanalfian.elog_pdam.databinding.FragmentRiwayatBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerActivity
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerViewModel
import com.ichsanalfian.elog_pdam.ui.main.buyer.ui.keranjang.KeranjangAdapter
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory


class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null

    private lateinit var keranjangViewModel: BuyerViewModel
    private lateinit var keranjangAdapter: RiwayatAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keranjangAdapter =RiwayatAdapter()


        binding.rvRiwayat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = keranjangAdapter
        }

        keranjangViewModel = ViewModelProvider(this, ViewModelFactory())[BuyerViewModel::class.java]
        keranjangViewModel.getRiwayat()
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
}