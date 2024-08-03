import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.FragmentProdukBinding
import com.ichsanalfian.elog_pdam.adapter.GridAdapter
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel


class ProdukFragment : Fragment() {
    private lateinit var binding: FragmentProdukBinding
    private lateinit var sellerViewModel: SellerViewModel
    private lateinit var gridAdapter: GridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridAdapter = GridAdapter(3)

        // Gunakan GridLayoutManager dengan 2 kolom
        binding.rvProduk.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gridAdapter
        }

        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]
        sellerViewModel.setBarang()
        sellerViewModel.getBarang().observe(viewLifecycleOwner) { listBarang ->
            if (listBarang != null) {
                gridAdapter.submitList(listBarang)
            } else {
                binding.apply {
                    rvProduk.visibility = View.GONE
                    emptyData.visibility = View.VISIBLE
                }
                println("LIST NULL NGABS!!")
            }
        }
        sellerViewModel.isLoad().observe(viewLifecycleOwner){
            showLoading(it)
        }

        // Panggil fungsi untuk mengatur search bar di Toolbar
        setupSearchBar()
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
                gridAdapter.submitList(it)
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
