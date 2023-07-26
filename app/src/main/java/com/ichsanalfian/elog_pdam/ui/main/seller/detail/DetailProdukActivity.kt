package com.ichsanalfian.elog_pdam.ui.main.seller.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailProdukBinding
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity

import com.ichsanalfian.elog_pdam.ui.main.seller.viewModel.SellerFactory
import com.ichsanalfian.elog_pdam.ui.main.seller.viewModel.SellerViewModel

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProdukBinding

    private lateinit var sellerViewModel: SellerViewModel

    private var namaProduk: String? = null
    private var merkProduk: String? = null
    private var kodeProduk: String? = null
    private var hargaProduk: String? = null
    private var satuanProduk: String? = null
    private var stokProduk: String? = null
    private var kategoriProduk: String? = null
    private var ukuranProduk: String? = null
    private var deskripsiProduk: String? = null
    private var gambarProduk: String? = null
    private var idProduk: Int = 0
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
        namaProduk = intent.getStringExtra(EXTRA_NAMA)
        merkProduk = intent.getStringExtra(EXTRA_MERK)
        kodeProduk = intent.getStringExtra(EXTRA_KODE)
        hargaProduk = intent.getStringExtra(EXTRA_HARGA)
        satuanProduk = intent.getStringExtra(EXTRA_SATUAN)
        stokProduk = intent.getStringExtra(EXTRA_STOK)
        kategoriProduk = intent.getStringExtra(EXTRA_KATEGORI)
        ukuranProduk = intent.getStringExtra(EXTRA_UKURAN)
        deskripsiProduk = intent.getStringExtra(EXTRA_DESKRIPSI)
        gambarProduk = intent.getStringExtra(EXTRA_GAMBAR) //TODO Tambahan
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
            R.id.action_update ->{
                val intent = Intent(this, UpdateBarangActivity::class.java)
                intent.putExtra(UpdateBarangActivity.EXTRA_NAMA, namaProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_MERK, merkProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_HARGA, hargaProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_KODE, kodeProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_SATUAN, satuanProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_STOK, stokProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_KATEGORI, kategoriProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_UKURAN, ukuranProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_DESKRIPSI, deskripsiProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_GAMBAR, gambarProduk)
                intent.putExtra(UpdateBarangActivity.EXTRA_ID, idProduk)

                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}
