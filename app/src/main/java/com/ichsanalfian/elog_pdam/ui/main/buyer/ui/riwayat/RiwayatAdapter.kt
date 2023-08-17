package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.riwayat

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.databinding.ItemKeranjangBinding
import com.ichsanalfian.elog_pdam.databinding.ItemRiwayatBinding
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.ui.main.buyer.ui.keranjang.KeranjangAdapter
import com.ichsanalfian.elog_pdam.ui.main.seller.detail.DetailProdukActivity

class RiwayatAdapter:
    ListAdapter<Barang,RiwayatAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickCallback {
        fun onItemClicked(data: Barang)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ViewHolder(private val binding: ItemRiwayatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(barang)
            }
            binding.apply {
                tvTanggalProduk.text = barang.tanggal_checkout.toString()
                tvNameProduk.text = barang.nama
                tvJumlah.text = barang.jumlah.toString()
                //TODO Tambahan
                Glide.with(itemView.context).load("${BuildConfig.BASE_URL}${barang.gambar}")
                    .apply(RequestOptions().override(263, 263))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(ivProdukRiwayat)
            }

            itemView.setOnClickListener {
                // Di SellerAdapter, saat item di-klik, tambahkan data produk ke dalam intent
                val intent = Intent(itemView.context, DetailProdukActivity::class.java)
                intent.putExtra(DetailProdukActivity.EXTRA_NAMA, barang.nama)
                intent.putExtra(DetailProdukActivity.EXTRA_MERK, barang.merk)
                intent.putExtra(DetailProdukActivity.EXTRA_KODE, barang.kode)
                intent.putExtra(DetailProdukActivity.EXTRA_HARGA, barang.harga.toString())
                intent.putExtra(DetailProdukActivity.EXTRA_SATUAN, barang.satuan)
                intent.putExtra(DetailProdukActivity.EXTRA_STOK, barang.stok.toString())
                intent.putExtra(DetailProdukActivity.EXTRA_KATEGORI, barang.kategori)
                intent.putExtra(DetailProdukActivity.EXTRA_UKURAN, barang.ukuran)
                intent.putExtra(DetailProdukActivity.EXTRA_DESKRIPSI, barang.deskripsi)
                intent.putExtra(DetailProdukActivity.EXTRA_GAMBAR, barang.gambar)
                intent.putExtra(DetailProdukActivity.EXTRA_ID, barang.id)
                intent.putExtra(DetailProdukActivity.EXTRA_PAGE, 2)
                intent.putExtra(DetailProdukActivity.EXTRA_JUMLAH_KERANJANG, barang.jumlah)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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