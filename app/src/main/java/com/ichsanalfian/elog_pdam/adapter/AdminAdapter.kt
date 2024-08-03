package com.ichsanalfian.elog_pdam.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.databinding.ItemWaitingBinding
import com.ichsanalfian.elog_pdam.model.VerifSellerData
import com.ichsanalfian.elog_pdam.ui.main.admin.DetailApprovedActivity
import com.ichsanalfian.elog_pdam.ui.main.admin.DetailWaitingActivity

class AdminAdapter(private val instance: Int): ListAdapter<VerifSellerData, AdminAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
//instance --> 0 = Waiting List, 1 = Approved List

    interface OnItemClickCallback {
        fun onItemClicked(data: VerifSellerData)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ViewHolder(private val binding: ItemWaitingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(verifSellerData: VerifSellerData) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(verifSellerData)
            }
            binding.apply {
                namaPerusahaan.text = verifSellerData.namaPerusahaan
                //TODO Tambahan
                Glide.with(itemView.context)
                    .load("${BuildConfig.BASE_URL}uploads/logo/${verifSellerData.logo}")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter() // Mengganti centerCrop() menjadi fitCenter()
                    .into(userPic)

            }

            if (instance == 0){
                binding.tglSubmit.text = verifSellerData.tglSubmit
                itemView.setOnClickListener {
                    // Di GridAdapter, saat item di-klik, tambahkan data produk ke dalam intent
                    val intent = Intent(itemView.context, DetailWaitingActivity::class.java)
                    intent.putExtra(DetailWaitingActivity.EXTRA_ID_SELLER, verifSellerData.idSeller)
                    intent.putExtra(DetailWaitingActivity.EXTRA_NAMA_PERUSAHAAN, verifSellerData.namaPerusahaan)
                    intent.putExtra(DetailWaitingActivity.EXTRA_ALAMAT, verifSellerData.alamat)
                    intent.putExtra(DetailWaitingActivity.EXTRA_LOGO, verifSellerData.logo)
                    intent.putExtra(DetailWaitingActivity.EXTRA_NAMA_DIREKTUR, verifSellerData.namaDirektur)
                    intent.putExtra(DetailWaitingActivity.EXTRA_KTP, verifSellerData.ktpDirektur)
                    intent.putExtra(DetailWaitingActivity.EXTRA_NO_TELP, verifSellerData.noTelp)
                    intent.putExtra(DetailWaitingActivity.EXTRA_NO_WA, verifSellerData.noWA)
                    intent.putExtra(DetailWaitingActivity.EXTRA_EMAIL, verifSellerData.email)
                    intent.putExtra(DetailWaitingActivity.EXTRA_BID_USAHA, verifSellerData.bidangUsaha)
                    intent.putExtra(DetailWaitingActivity.EXTRA_NIB, verifSellerData.nib)
                    intent.putExtra(DetailWaitingActivity.EXTRA_KBLI, verifSellerData.kbli)
                    intent.putExtra(DetailWaitingActivity.EXTRA_NPWP, verifSellerData.npwp)
                    intent.putExtra(DetailWaitingActivity.EXTRA_TGL, verifSellerData.tglSubmit)

                    itemView.context.startActivity(intent)
                }
            } else{
                binding.tglSubmit.text = verifSellerData.tglApproved
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailApprovedActivity::class.java)
                    intent.putExtra(DetailApprovedActivity.EXTRA_ID_SELLER, verifSellerData.idSeller)
                    intent.putExtra(DetailApprovedActivity.EXTRA_NAMA_PERUSAHAAN, verifSellerData.namaPerusahaan)
                    intent.putExtra(DetailApprovedActivity.EXTRA_ALAMAT, verifSellerData.alamat)
                    intent.putExtra(DetailApprovedActivity.EXTRA_LOGO, verifSellerData.logo)
                    intent.putExtra(DetailApprovedActivity.EXTRA_NAMA_DIREKTUR, verifSellerData.namaDirektur)
                    intent.putExtra(DetailApprovedActivity.EXTRA_KTP, verifSellerData.ktpDirektur)
                    intent.putExtra(DetailApprovedActivity.EXTRA_NO_TELP, verifSellerData.noTelp)
                    intent.putExtra(DetailApprovedActivity.EXTRA_NO_WA, verifSellerData.noWA)
                    intent.putExtra(DetailApprovedActivity.EXTRA_EMAIL, verifSellerData.email)
                    intent.putExtra(DetailApprovedActivity.EXTRA_BID_USAHA, verifSellerData.bidangUsaha)
                    intent.putExtra(DetailApprovedActivity.EXTRA_NIB, verifSellerData.nib)
                    intent.putExtra(DetailApprovedActivity.EXTRA_KBLI, verifSellerData.kbli)
                    intent.putExtra(DetailApprovedActivity.EXTRA_NPWP, verifSellerData.npwp)
                    intent.putExtra(DetailApprovedActivity.EXTRA_TGL, verifSellerData.tglSubmit)
                    intent.putExtra(DetailApprovedActivity.EXTRA_APV, verifSellerData.tglApproved)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWaitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VerifSellerData>() {
            override fun areItemsTheSame(
                oldItem: VerifSellerData,
                newItem: VerifSellerData
            ): Boolean {
                return oldItem.idSeller == newItem.idSeller
            }

            override fun areContentsTheSame(
                oldItem: VerifSellerData,
                newItem: VerifSellerData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}