package com.ichsanalfian.elog_pdam.ui.main.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailApprovedBinding
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailWaitingBinding
import com.ichsanalfian.elog_pdam.viewModel.AdminViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

class DetailApprovedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailApprovedBinding
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailApprovedBinding.inflate(layoutInflater)

        setContentView(binding.root)
        adminViewModel = ViewModelProvider(this, ViewModelFactory())[AdminViewModel::class.java]

        binding.apply {
            tvNama.text = intent.getStringExtra(EXTRA_NAMA_PERUSAHAAN)
            tvAlamat.text = intent.getStringExtra(EXTRA_ALAMAT)
            tvNamaDir.text = intent.getStringExtra(EXTRA_NAMA_DIREKTUR)
            tvNoTep.text = intent.getStringExtra(EXTRA_NO_TELP)
            tvNoWA.text = intent.getStringExtra(EXTRA_NO_WA)
            tvEmail.text = intent.getStringExtra(EXTRA_EMAIL)
            tvNpwp.text = intent.getStringExtra(EXTRA_NPWP)
            tvBidUsh.text = intent.getStringExtra(EXTRA_BID_USAHA)
            tvNib.text = intent.getStringExtra(EXTRA_NIB)
            tvKbli4.text = intent.getStringExtra(EXTRA_KBLI)
            tvTgl.text = intent.getStringExtra(EXTRA_TGL)
            tvApproved.text = intent.getStringExtra(EXTRA_APV)
            Glide.with(applicationContext)
                .load("${BuildConfig.BASE_URL}uploads/logo/${intent.getStringExtra(EXTRA_LOGO)}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(ivLogo)
            Glide.with(applicationContext)
                .load("${BuildConfig.BASE_URL}uploads/ktp/${intent.getStringExtra(EXTRA_KTP)}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(ivKtp)
        }
    }

    companion object {
        const val EXTRA_ID_SELLER = "extra_id_seller"
        const val EXTRA_NAMA_PERUSAHAAN = "extra_nama_perusahaan"
        const val EXTRA_ALAMAT = "extra_alamat"
        const val EXTRA_LOGO = "extra_logo"
        const val EXTRA_NAMA_DIREKTUR = "extra_nama_perusahaan"
        const val EXTRA_KTP = "extra_ktp"
        const val EXTRA_NO_TELP = "extra_no_telp"
        const val EXTRA_NO_WA = "extra_no_wa"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_BID_USAHA = "extra_bid_usaha"
        const val EXTRA_NIB = "extra_nib"
        const val EXTRA_KBLI = "extra_kbli"
        const val EXTRA_NPWP = "extra_npwp"
        const val EXTRA_TGL = "extra_tgl"
        const val EXTRA_APV = "extra_apv"

    }
}