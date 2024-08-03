package com.ichsanalfian.elog_pdam.ui.main.seller.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailProdukBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.update.UpdateBarangActivity

import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import java.text.NumberFormat
import java.util.*

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProdukBinding

    private lateinit var sellerViewModel: SellerViewModel

    private var namaProduk: String? = null
    private var merkProduk: String? = null
    private var kodeProduk: String? = null
    private var hargaProduk: Int? = null
    private var satuanProduk: String? = null
    private var stokProduk: String? = null
    private var kategoriProduk: String? = null
    private var ukuranProduk: String? = null
    private var deskripsiProduk: String? = null
    private var gambarProduk: String? = null
    private var idSeller: String? = null
    private var idProduk: Int = 0
    private var page: Int = 0
    private var jumlahKeranjang: Int = 0
    private var tglCekout: String? = null
    private var status: Int? = 0

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
        const val EXTRA_ID_SELLER = "extra_id_seller"
        const val EXTRA_TGL_CEKOUT = "extra_tgl_cekout"
        const val EXTRA_STATUS = "extra_status"
        const val EXTRA_JUMLAH_KERANJANG = "extra_jumlah_keranjang"
        //page mana si
        const val EXTRA_PAGE = "extra_page"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]

        // Ambil data dari intent
        idProduk = intent.getIntExtra(EXTRA_ID,0)
        namaProduk = intent.getStringExtra(EXTRA_NAMA)
        merkProduk = intent.getStringExtra(EXTRA_MERK)
        kodeProduk = intent.getStringExtra(EXTRA_KODE)
        hargaProduk = intent.getIntExtra(EXTRA_HARGA,0)
        satuanProduk = intent.getStringExtra(EXTRA_SATUAN)
        stokProduk = intent.getStringExtra(EXTRA_STOK)
        kategoriProduk = intent.getStringExtra(EXTRA_KATEGORI)
        ukuranProduk = intent.getStringExtra(EXTRA_UKURAN)
        deskripsiProduk = intent.getStringExtra(EXTRA_DESKRIPSI)
        gambarProduk = intent.getStringExtra(EXTRA_GAMBAR) //TODO Tambahan
        idProduk = intent.getIntExtra(EXTRA_ID, 0)
        idSeller = intent.getStringExtra(EXTRA_ID_SELLER)
        page = intent.getIntExtra(EXTRA_PAGE, 0)
        jumlahKeranjang = intent.getIntExtra(EXTRA_JUMLAH_KERANJANG, 0)
        tglCekout = intent.getStringExtra(EXTRA_TGL_CEKOUT)
        status = intent.getIntExtra(EXTRA_STATUS,0)

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id")) // Format sebagai mata uang dengan tanda titik dan koma sesuai konvensi Indonesia

        // Tampilkan data pada layout menggunakan data binding
        binding.apply {
            textViewNamaProduk.text = namaProduk
            textViewMerkProduk.text = merkProduk
            textViewKodeProduk.text = kodeProduk
            textViewHargaProduk.text = currencyFormat.format(hargaProduk)
            textViewSatuanProduk.text = satuanProduk
            textViewStokProduk.text = stokProduk
            textViewKategoriProduk.text = kategoriProduk
            textViewUkuranProduk.text = ukuranProduk
            textViewDeskripsiProduk.text = deskripsiProduk

            //page, 0-> keranjang, 1-> beranda, 2-> riwayat, 3-> produk, 4-> request
            if (page == 1){
                buttonTambahKeranjang.setOnClickListener {
                    val jumlahStok = binding.editTextJumlahstok.text.toString().toIntOrNull()
                    if (jumlahStok != null && jumlahStok > 0) {
                        val userId = UserPreferences.user.id.toString()
                        sellerViewModel.addToCart(idProduk, jumlahStok, userId)

                    }
                }
                sellerViewModel.addToCartResult.observe(this@DetailProdukActivity) { result ->
                    val (success, message) = result
                    if (success) {
                        Toast.makeText(this@DetailProdukActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DetailProdukActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            else if (page == 0){
                val stokEditable = Editable.Factory.getInstance().newEditable(jumlahKeranjang.toString())
                editTextJumlahstok.text = stokEditable
                buttonTambahKeranjang.text = "Update Jumlah"
                buttonTambahKeranjang.setOnClickListener{
                    binding.editTextJumlahstok.text.toString().toIntOrNull()?.let {
                        sellerViewModel.update_jumlah_barang(idProduk,
                            it, UserPreferences.user.id.toString())
                        //kasih intent

                    }
                }
                sellerViewModel.update.observe(this@DetailProdukActivity) { result ->
                    val (success, message) = result
                    if (success) {
                        Toast.makeText(this@DetailProdukActivity, message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DetailProdukActivity, BuyerActivity::class.java))

                    } else {
                        Toast.makeText(this@DetailProdukActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            } else if (page == 4){
                editTextJumlahstok.visibility = View.GONE
                buttonTambahKeranjang.visibility = View.GONE

                if (status == 0){
                    qtyRequest.visibility = View.VISIBLE
                    cekoutRequest.visibility = View.VISIBLE
                    btnSending.visibility = View.VISIBLE

                    qtyRequest.text = "Qty:\n$jumlahKeranjang $satuanProduk"

                    val tgl = tglCekout?.substring(0,10)
                    val jam = tglCekout?.substring(11,16)
                    cekoutRequest.text = "Checkout :\n$tgl\n$jam"

                    btnSending.setOnClickListener {
                        sellerViewModel.sending(idSeller,idProduk.toString())
                        Toast.makeText(this@DetailProdukActivity, "Status barang berhasil dikirim", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DetailProdukActivity, SellerActivity::class.java))

                    }
                } else if (status == 1){
                    keterangan.text = "Barang telah anda kirim"
                    keterangan.visibility = View.VISIBLE
                } else{
                    keterangan.text = "Barang telah diterima oleh pembeli"
                    keterangan.visibility = View.VISIBLE
                }

            } else if (page == 2){
                editTextJumlahstok.visibility = View.GONE
                buttonTambahKeranjang.visibility = View.GONE

                if (status != 2){
                    btnDiterima.visibility = View.VISIBLE
                    btnDiterima.setOnClickListener {
                        sellerViewModel.diterima(idSeller, idProduk.toString())
                        Toast.makeText(this@DetailProdukActivity, "Status barang berhasil diterima", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DetailProdukActivity, BuyerActivity::class.java))
                    }
                    println("Ini status: $status")
                } else{
                    keterangan.text = "Barang sudah anda terima"
                    keterangan.visibility = View.VISIBLE
                }

            }
            else{
                bottomAppBar.visibility = View.GONE
            }
            //TODO Tambahan
            Glide.with(applicationContext).load("${BuildConfig.BASE_URL}uploads/barang/${gambarProduk}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageViewProduk)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (page == 3){
            menuInflater.inflate(R.menu.menu_detail, menu)
            super.onCreateOptionsMenu(menu)
        } else{
            false
        }
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
                intent.putExtra(UpdateBarangActivity.EXTRA_ID_SELLER, idSeller)

                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
