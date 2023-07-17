import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ichsanalfian.elog_pdam.databinding.FragmentProdukBinding
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerAdapter
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerFactory
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerViewModel

class ProdukFragment : Fragment() {
    private lateinit var binding: FragmentProdukBinding
    private lateinit var sellerViewModel: SellerViewModel
    private lateinit var sellerAdapter: SellerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerAdapter = SellerAdapter()

        // Gunakan GridLayoutManager dengan 2 kolom
        binding.rvProduk.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = sellerAdapter
        }

        sellerViewModel = ViewModelProvider(this, SellerFactory())[SellerViewModel::class.java]
        sellerViewModel.setBarang()
        sellerViewModel.getBarang().observe(viewLifecycleOwner) { listBarang ->
            if (listBarang!=null){
                sellerAdapter.submitList(listBarang)
            } else{
                println("LIST NULL NGABS!!")
            }
        }
    }
}