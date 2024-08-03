package com.ichsanalfian.elog_pdam.ui.main.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivitySellerBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.UserData
import com.ichsanalfian.elog_pdam.ui.auth.LoginActivity
import com.ichsanalfian.elog_pdam.ui.main.admin.AdminActivity
import com.ichsanalfian.elog_pdam.ui.main.admin.DetailWaitingActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.add.TambahBarangActivity

class SellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerBinding
    private lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.hide()

        preferences = UserPreferences(this)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, "Produk", "Request")
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener {
            startActivity(Intent(this, TambahBarangActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                showApproveDialog()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showApproveDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Log Out")
        dialogBuilder.setMessage("Apakah Anda yakin?")

        // Tombol "Ya" untuk melakukan tindakan approve
        dialogBuilder.setPositiveButton("Ya") { dialog, _ ->
            preferences.setUser(UserData())
            startActivity(Intent(this@SellerActivity, LoginActivity::class.java))
            // Tutup dialog
            dialog.dismiss()
            finish()
        }
        // Tombol "Tidak" untuk menutup dialog
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            // Tutup dialog
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}