package com.ichsanalfian.elog_pdam.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                } else{
                    Toast.makeText(this@RegisterActivity, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
                }
            }

            signIn.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }


    }
}