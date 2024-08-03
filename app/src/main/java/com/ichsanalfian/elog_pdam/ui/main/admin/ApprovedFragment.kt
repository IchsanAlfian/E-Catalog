package com.ichsanalfian.elog_pdam.ui.main.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichsanalfian.elog_pdam.adapter.AdminAdapter
import com.ichsanalfian.elog_pdam.databinding.FragmentWaitingListBinding
import com.ichsanalfian.elog_pdam.viewModel.AdminViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

class ApprovedFragment : Fragment() {
    private lateinit var binding: FragmentWaitingListBinding
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var adminAdapter: AdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaitingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminAdapter = AdminAdapter(1)
        val layout = LinearLayoutManager(requireContext())
        // Gunakan GridLayoutManager dengan 2 kolom
        binding.rvVerifList.apply {

            layoutManager = layout
            adapter = adminAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), layout.orientation))
        }

        adminViewModel = ViewModelProvider(this, ViewModelFactory())[AdminViewModel::class.java]
        adminViewModel.setApprovedList()
        adminViewModel.getApprovedList().observe(viewLifecycleOwner) { approvedList ->
            if (approvedList != null) {
                adminAdapter.submitList(approvedList)
            } else {
                binding.apply {
                    rvVerifList.visibility = View.GONE
                    emptyData.visibility = View.VISIBLE
                }
            }
        }
        adminViewModel.isLoad().observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.GONE
        }
    }
}
