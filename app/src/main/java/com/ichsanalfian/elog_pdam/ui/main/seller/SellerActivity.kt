package com.ichsanalfian.elog_pdam.ui.main.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ichsanalfian.elog_pdam.databinding.ActivitySellerBinding
import com.ichsanalfian.elog_pdam.ui.main.seller.add.TambahBarangActivity

class SellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener {
            startActivity(Intent(this, TambahBarangActivity::class.java))
        }
    }
}