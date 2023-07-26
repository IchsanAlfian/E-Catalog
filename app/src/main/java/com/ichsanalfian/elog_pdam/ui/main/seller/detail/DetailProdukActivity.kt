package com.ichsanalfian.elog_pdam.ui.main.seller.detail

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailProdukBinding
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.viewModel.SellerFactory
import com.ichsanalfian.elog_pdam.ui.main.seller.viewModel.SellerViewModel

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProdukBinding
    private var idProduk: Int = 0
    private lateinit var sellerViewModel: SellerViewModel
    companion object {
        const val EXTRA_ID = "extra_id"
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
        sellerViewModel = ViewModelProvider(this, SellerFactory())[SellerViewModel::class.java]

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
        idProduk = intent.getIntExtra(EXTRA_ID, 0)

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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Hapus Produk")
                    .setMessage("Apakah Anda yakin ingin menghapus produk ini?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        // Panggil fungsi deleteBarang dari sellerViewModel
                        sellerViewModel.deleteBarang(idProduk)
                        Toast.makeText(this, "Produk berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this, SellerActivity::class.java))
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}
