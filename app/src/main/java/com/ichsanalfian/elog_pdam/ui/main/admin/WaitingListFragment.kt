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

class WaitingListFragment : Fragment() {
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
        adminAdapter = AdminAdapter(0)
        val layout = LinearLayoutManager(requireContext())
        // Gunakan GridLayoutManager dengan 2 kolom
        binding.rvVerifList.apply {

            layoutManager = layout
            adapter = adminAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), layout.orientation))
        }

        adminViewModel = ViewModelProvider(this, ViewModelFactory())[AdminViewModel::class.java]
        adminViewModel.setVerifList()
        adminViewModel.getVerifList().observe(viewLifecycleOwner) { verifList ->
            if (verifList != null) {
                adminAdapter.submitList(verifList)
            } else {
                binding.apply {
                    rvVerifList.visibility = View.GONE
                    emptyData.visibility = View.VISIBLE
                }
                println("LIST NULL NGABS!!")
            }
        }
        adminViewModel.isLoad().observe(viewLifecycleOwner){
            showLoading(it)
        }

        // Panggil fungsi untuk mengatur search bar di Toolbar
//        setupSearchBar()
    }

//    private fun setupSearchBar() {
//        val activity = requireActivity() // Get the activity reference
//        val searchView = activity.findViewById<SearchView>(R.id.searchView) // Find the searchView from activity's layout
//
//        // Setel query hint (teks yang muncul saat search view kosong)
//        searchView.queryHint = "Cari Produk"
//
//        // Implementasikan listener untuk menghandle perubahan query pada search view
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // Panggil fungsi untuk melakukan pencarian berdasarkan query
//                performSearch(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                // Panggil fungsi untuk melakukan pencarian berdasarkan teks yang berubah
//                performSearch(newText)
//                return true
//            }
//        })
//    }
//
//    private fun performSearch(query: String) {
//        // Lakukan pencarian berdasarkan query di view model
//        adminViewModel.searchBarang(query).observe(viewLifecycleOwner) { listBarang ->
//            listBarang?.let {
//                sellerAdapter.submitList(it)
//            }
//        }
//    }

    private fun showLoading(state: Boolean){
//        if (state) View.VISIBLE else View.GONE
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.GONE
        }
    }
}