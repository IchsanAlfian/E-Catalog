package com.ichsanalfian.elog_pdam.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ichsanalfian.elog_pdam.MainActivity
import com.ichsanalfian.elog_pdam.databinding.ActivityRegisterBinding
import com.ichsanalfian.elog_pdam.viewModel.AuthViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

//TODO Kelas Tambahan, jangan lupa copas xml sama drawablenya juga
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        authViewModel = ViewModelProvider(this@RegisterActivity, ViewModelFactory.getInstance())[AuthViewModel::class.java]

        binding.apply {
            btnRegister.setOnClickListener {
                val username = ETusernameReg.text.toString()
                val password = ETpasswordReg.text.toString()
                val perusahaan = ETperusahaanReg.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty() && perusahaan.isNotEmpty()){
                    authViewModel.register(username, password, perusahaan)
                    Toast.makeText(this@RegisterActivity, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                } else{
                    Toast.makeText(this@RegisterActivity, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
                }
            }

            signIn.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }

        authViewModel.isLoad().observe(this@RegisterActivity){
            showLoading(it)
        }
    }

    private fun showLoading(state: Boolean){
//        if (state) View.VISIBLE else View.GONE
        if (state){
            binding.progressBar5.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
        } else{
            binding.progressBar5.visibility = View.GONE
            binding.btnRegister.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        // Tidak melakukan apa-apa atau melakukan tindakan lain sesuai kebutuhan

        // Contoh: Menampilkan pesan atau memunculkan dialog konfirmasi keluar
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Konfirmasi Keluar")
        dialogBuilder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            finish()
        }
        dialogBuilder.setNegativeButton("Tidak", null)
        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}