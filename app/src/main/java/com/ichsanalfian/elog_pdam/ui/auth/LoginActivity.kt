package com.ichsanalfian.elog_pdam.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ichsanalfian.elog_pdam.databinding.ActivityLoginBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.LoginResponse
import com.ichsanalfian.elog_pdam.ui.main.admin.AdminActivity
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity
import com.ichsanalfian.elog_pdam.viewModel.AuthViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

//TODO Kelas tambahan, jangan lupa copas xml sama drawablenya juga
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        authViewModel = ViewModelProvider(this@LoginActivity, ViewModelFactory.getInstance())[AuthViewModel::class.java]

        binding.apply {
            btnLogin.setOnClickListener{
                val username = ETusername.text.toString()
                val password = ETpassword.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty()){
                    authViewModel.login(username, password)
                    authViewModel.getDatabyLogin().observe(this@LoginActivity){
                        if (!it.error){
                            saveUserData(it)
                            Toast.makeText(this@LoginActivity, "Berhasil Login ${UserPreferences.user.id}", Toast.LENGTH_SHORT).show()

                            when(it.userData?.role){
                                "seller" -> startActivity(Intent(this@LoginActivity, SellerActivity::class.java))
                                "buyer" -> startActivity(Intent(this@LoginActivity, BuyerActivity::class.java))
                                "admin" -> startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                            }
                        }else {
                            Toast.makeText(this@LoginActivity, "Ada kesalahan dari server", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else{
                    Toast.makeText(this@LoginActivity, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
                }
            }

            signUpLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun saveUserData(response: LoginResponse) {
        val pref = UserPreferences(this)
        if(response.userData != null) {
            pref.setUser(response.userData)
        }
    }
}