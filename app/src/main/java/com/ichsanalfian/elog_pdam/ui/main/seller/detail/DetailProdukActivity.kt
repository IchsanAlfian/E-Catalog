package com.ichsanalfian.elog_pdam.ui.main.seller.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailProdukBinding

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
        const val EXTRA_GAMBAR = "extra_gambar" //TODO Tambahan
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
        val gambarProduk = intent.getStringExtra(EXTRA_GAMBAR) //TODO Tambahan

        // Tampilkan data pada layout menggunakan data binding
        binding.apply {
            textViewNamaProduk.text = namaProduk
            textViewMerkProduk.text = merkProduk
            textViewKodeProduk.text = kodeProduk
            textViewHargaProduk.text = hargaProduk
            textViewSatuanProduk.text = satuanProduk
            textViewStokProduk.text = stokProduk
            textViewKategoriProduk.text = kategoriProduk
            textViewUkuranProduk.text = ukuranProduk
            textViewDeskripsiProduk.text = deskripsiProduk
            //TODO Tambahan
            Glide.with(applicationContext).load("${BuildConfig.BASE_URL}${gambarProduk}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageViewProduk)
        }
    }

}
