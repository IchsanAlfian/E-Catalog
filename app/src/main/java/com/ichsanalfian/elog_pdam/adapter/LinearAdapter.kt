package com.ichsanalfian.elog_pdam.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.ui.main.seller.detail.DetailProdukActivity
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ItemKeranjangBinding
import java.text.NumberFormat
import java.util.*

class LinearAdapter(private val instance: Int) :
//instance, 0-> keranjang, 2-> riwayat, 4-> request
    ListAdapter<Barang, LinearAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickCallback {
        fun onItemClicked(data: Barang)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ViewHolder(private val binding: ItemKeranjangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(barang: Barang) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(barang)
            }

            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id")) // Format sebagai mata uang dengan tanda titik dan koma sesuai konvensi Indonesia

            if (instance == 4){
                binding.apply {
                    total.visibility = View.GONE
                    tglCekout.visibility = View.VISIBLE
                    val tgl = barang.tanggal_checkout.toString().substring(0, 10)
                    val jam = barang.tanggal_checkout.toString().substring(11, 16)
                    tglCekout.text = "$tgl\n$jam"
                }

            } else if (instance == 2){
                binding.apply{
                    tglCekout.visibility = View.VISIBLE
                    val tgl = barang.tanggal_checkout.toString().substring(0,10)
                    val jam = barang.tanggal_checkout.toString().substring(11,16)
                    tglCekout.text = "$tgl\n$jam"
                    status.visibility = View.VISIBLE
                    if (barang.status == 0){
                        status.setImageResource(R.drawable.diproses)
                    } else if (barang.status == 1){
                        status.setImageResource(R.drawable.dikirim)
                    } else{
                        status.setImageResource(R.drawable.sukses)
                    }
                }
            }
            binding.apply {
                namaProduk.text = barang.nama
                qty.text = "Qty:  "+ barang.jumlah.toString()+ " "+barang.satuan

                val totalAmount = barang.jumlah?.times(barang.harga) ?: 0.0 // Menghitung total amount atau menggunakan 0 jika jumlah null
                total.text = "Total:\nRp" + currencyFormat.format(totalAmount).substring(1,10)


                //TODO Tambahan
                Glide.with(itemView.context).load("${BuildConfig.BASE_URL}uploads/barang/${barang.gambar}")
                    .apply(RequestOptions().override(263,263))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(userPic)
            }


            itemView.setOnClickListener {
                // Di GridAdapter, saat item di-klik, tambahkan data produk ke dalam intent
                val intent = Intent(itemView.context, DetailProdukActivity::class.java)
                intent.putExtra(DetailProdukActivity.EXTRA_NAMA, barang.nama)
                intent.putExtra(DetailProdukActivity.EXTRA_MERK, barang.merk)
                intent.putExtra(DetailProdukActivity.EXTRA_KODE, barang.kode)
                intent.putExtra(DetailProdukActivity.EXTRA_HARGA, barang.harga)
                intent.putExtra(DetailProdukActivity.EXTRA_SATUAN, barang.satuan)
                intent.putExtra(DetailProdukActivity.EXTRA_STOK, barang.stok.toString())
                intent.putExtra(DetailProdukActivity.EXTRA_KATEGORI, barang.kategori)
                intent.putExtra(DetailProdukActivity.EXTRA_UKURAN, barang.ukuran)
                intent.putExtra(DetailProdukActivity.EXTRA_DESKRIPSI, barang.deskripsi)
                intent.putExtra(DetailProdukActivity.EXTRA_GAMBAR, barang.gambar)
                intent.putExtra(DetailProdukActivity.EXTRA_ID, barang.id)
                if (instance == 0){
                    intent.putExtra(DetailProdukActivity.EXTRA_PAGE, 0)
                } else if (instance == 2){
                    intent.putExtra(DetailProdukActivity.EXTRA_PAGE, 2)
                } else{
                    intent.putExtra(DetailProdukActivity.EXTRA_PAGE, 4)
                }
                intent.putExtra(DetailProdukActivity.EXTRA_TGL_CEKOUT, barang.tanggal_checkout)
                intent.putExtra(DetailProdukActivity.EXTRA_JUMLAH_KERANJANG, barang.jumlah)
                intent.putExtra(DetailProdukActivity.EXTRA_ID_SELLER, barang.idSeller)
                intent.putExtra(DetailProdukActivity.EXTRA_STATUS, barang.status)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemKeranjangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            viewHolder.bind(data)
        }
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Barang>() {
            override fun areItemsTheSame(oldItem: Barang, newItem: Barang): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Barang, newItem: Barang): Boolean {
                return oldItem == newItem
            }
        }
    }
}