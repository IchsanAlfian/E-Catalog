package com.ichsanalfian.elog_pdam.ui.main.buyer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityBuyerBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.UserData
import com.ichsanalfian.elog_pdam.ui.auth.LoginActivity

class BuyerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerBinding
    private lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = UserPreferences(this)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_buyer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_beranda, R.id.navigation_keranjang, R.id.navigation_riwayat
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
            startActivity(Intent(this@BuyerActivity, LoginActivity::class.java))
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