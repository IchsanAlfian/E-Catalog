package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichsanalfian.elog_pdam.adapter.LinearAdapter
import com.ichsanalfian.elog_pdam.databinding.FragmentRiwayatBinding
import com.ichsanalfian.elog_pdam.viewModel.BuyerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory


class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null

    private lateinit var buyerViewModel: BuyerViewModel
    private lateinit var linearAdapter: LinearAdapter

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
        linearAdapter = LinearAdapter(2)

        val layout = LinearLayoutManager(requireContext())
        binding.rvRiwayat.apply {
            layoutManager = layout
            adapter = linearAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), layout.orientation))
        }

        buyerViewModel = ViewModelProvider(this, ViewModelFactory())[BuyerViewModel::class.java]
        buyerViewModel.getRiwayat()
        buyerViewModel.getBarang().observe(viewLifecycleOwner) { keranjangList ->
            if (keranjangList != null) {
                linearAdapter.submitList(keranjangList)
            } else {
                println("KOSONG")
                binding.emptyData4.visibility = View.VISIBLE
            }
        }

        buyerViewModel.isLoad().observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean){
//        if (state) View.VISIBLE else View.GONE
        if (state){
            binding.progressBar3.visibility = View.VISIBLE
        } else{
            binding.progressBar3.visibility = View.GONE
        }
    }
}