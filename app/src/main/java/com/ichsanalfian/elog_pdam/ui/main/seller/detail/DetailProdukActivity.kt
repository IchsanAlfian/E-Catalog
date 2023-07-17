package com.ichsanalfian.elog_pdam.ui.main.seller.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailProdukBinding
import com.ichsanalfian.elog_pdam.databinding.ActivitySellerBinding

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProdukBinding
    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_MERK = "extra_merk"
        const val EXTRA_KODE = "extra_kode"
        const val EXTRA_HARGA = "extra_harga"
        const val EXTRA_SATUAN = "extra_satuan"
        const val EXTRA_STOK = "extra_stok"
        const val EXTRA_KATEGORI = "extra_kategori"
        const val EXTRA_UKURAN = "extra_ukuran"
        const val EXTRA_DESKRIPSI = "extra_deskripsi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val namaProduk = intent.getStringExtra(EXTRA_NAMA)
        val merkProduk = intent.getStringExtra(EXTRA_MERK)
        val kodeProduk = intent.getStringExtra(EXTRA_KODE)
        val hargaProduk = intent.getStringExtra(EXTRA_HARGA)
        val satuanProduk = intent.getStringExtra(EXTRA_SATUAN)
        val stokProduk = intent.getStringExtra(EXTRA_STOK)
        val kategoriProduk = intent.getStringExtra(EXTRA_KATEGORI)
        val ukuranProduk = intent.getStringExtra(EXTRA_UKURAN)
        val deskripsiProduk = intent.getStringExtra(EXTRA_DESKRIPSI)

        // Tampilkan data pada layout menggunakan data binding
        binding.textViewNamaProduk.text = namaProduk
        binding.textViewMerkProduk.text = merkProduk
        binding.textViewKodeProduk.text = kodeProduk
        binding.textViewHargaProduk.text = hargaProduk
        binding.textViewSatuanProduk.text = satuanProduk
        binding.textViewStokProduk.text = stokProduk
        binding.textViewKategoriProduk.text = kategoriProduk
        binding.textViewUkuranProduk.text = ukuranProduk
        binding.textViewDeskripsiProduk.text = deskripsiProduk
    }

}
