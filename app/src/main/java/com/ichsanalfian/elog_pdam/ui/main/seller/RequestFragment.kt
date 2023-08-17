package com.ichsanalfian.elog_pdam.ui.main.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.FragmentProdukBinding
import com.ichsanalfian.elog_pdam.databinding.FragmentRequestBinding
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

class RequestFragment : Fragment() {
    private lateinit var binding: FragmentRequestBinding
    private lateinit var sellerViewModel: SellerViewModel
    private lateinit var sellerAdapter: SellerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerAdapter = SellerAdapter()

        // Gunakan GridLayoutManager dengan 2 kolom
        binding.rvProdukRequest.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = sellerAdapter
        }

        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]
        sellerViewModel.getRequestSeller()
        sellerViewModel.getBarang().observe(viewLifecycleOwner) { listBarang ->
            if (listBarang != null) {
                sellerAdapter.submitList(listBarang)
            } else {
                println("LIST NULL NGABS!!")
            }
        }


    }


}