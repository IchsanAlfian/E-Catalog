package com.ichsanalfian.elog_pdam.ui.main.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ichsanalfian.elog_pdam.databinding.ActivityAdminBinding
import com.ichsanalfian.elog_pdam.databinding.ActivityRegisterBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}