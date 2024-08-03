package com.ichsanalfian.elog_pdam.ui.main.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.textfield.TextInputLayout
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityDetailWaitingBinding
import com.ichsanalfian.elog_pdam.viewModel.AdminViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

class DetailWaitingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailWaitingBinding
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWaitingBinding.inflate(layoutInflater)

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

            btnApprove.setOnClickListener {
                showApproveDialog(0)
            }

            btnReject.setOnClickListener {
                showApproveDialog(1)
            }
        }
    }

    private fun showApproveDialog(instance : Int) {
        // instance : 0 -> btnApprove, 1 -> btnReject
        val dialogBuilder = AlertDialog.Builder(this)

        if (instance == 0){
            dialogBuilder.setTitle("Konfirmasi Approve")
            dialogBuilder.setMessage("Apakah Anda yakin ingin approve data ini?")
        } else{
            dialogBuilder.setTitle("Konfirmasi Reject")
            dialogBuilder.setMessage("Apakah Anda yakin ingin menolak data ini?")
        }

        // Tombol "Ya" untuk melakukan tindakan approve
        dialogBuilder.setPositiveButton("Ya") { dialog, _ ->
            // Lakukan tindakan approve di sini
            // Misalnya: approveData()
            if (instance == 0){
                adminViewModel.approving(intent.getStringExtra(EXTRA_ID_SELLER))
                startActivity(Intent(this, AdminActivity::class.java))
            } else{
                showReasonDialog()
            }
            // Tutup dialog
            dialog.dismiss()

        }

        // Tombol "Tidak" untuk menutup dialog
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            // Tutup dialog
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun showReasonDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Alasan Reject")

        val inputEditText = EditText(this)
        inputEditText.hint = "Masukkan alasan approve disini"

// Mengatur tampilan EditText menjadi kotak dengan latar belakang dan padding
        inputEditText.setBackgroundResource(android.R.drawable.editbox_background)
        val padding = resources.getDimensionPixelSize(R.dimen.edit_text_padding) // Definisikan dimensi padding yang sesuai
        inputEditText.setPadding(padding, padding, padding, padding)
        inputEditText.setPaddingRelative(padding + 16, padding, padding + 16, padding) // Menambahkan padding di bagian kiri dan kanan

        dialogBuilder.setView(inputEditText)

        // Tombol "OK" untuk menyimpan alasan dan menutup dialog
        dialogBuilder.setPositiveButton("Kirim") { dialog, _ ->
            val reason = inputEditText.text.toString()

            // Lakukan tindakan approve dengan alasan yang diberikan
            // Misalnya: approveDataWithReason(reason)
            adminViewModel.rejecting(intent.getStringExtra(EXTRA_ID_SELLER), reason)
            // Tutup dialog alasan
            dialog.dismiss()
            startActivity(Intent(this, AdminActivity::class.java))
        }

        // Tombol "Batal" untuk menutup dialog alasan
        dialogBuilder.setNegativeButton("Batal") { dialog, _ ->
            // Tutup dialog alasan
            dialog.dismiss()
        }

        val reasonDialog: AlertDialog = dialogBuilder.create()
        reasonDialog.show()
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
    }
}