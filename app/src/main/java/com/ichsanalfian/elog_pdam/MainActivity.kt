package com.ichsanalfian.elog_pdam

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.SectionsPagerAdapter
import com.ichsanalfian.elog_pdam.databinding.ActivityMainBinding
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSeller.setOnClickListener {
            startActivity(Intent(this,SellerActivity::class.java))
        }
    }
}