package com.ichsanalfian.elog_pdam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.SectionsPagerAdapter
import com.ichsanalfian.elog_pdam.databinding.ActivityMainBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.ui.auth.LoginActivity
import com.ichsanalfian.elog_pdam.ui.auth.RegisterActivity
import com.ichsanalfian.elog_pdam.ui.main.admin.AdminActivity
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.verif.VerifActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences(this)
        val id = pref.getUser().id
        val role = pref.getUser().role
        val verif = pref.getUser().isVerified

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            if(id != ""){
                if (verif == 1){
                    when (role) {
                        "seller" -> {
                            startActivity(Intent(this, SellerActivity::class.java))
                        }
                        "buyer" -> {
                            startActivity(Intent(this, BuyerActivity::class.java))
                        }
                        "admin" -> {
                            startActivity(Intent(this, AdminActivity::class.java))
                        }
                    }
                }else{
                    startActivity(Intent(this, VerifActivity::class.java))
                }
            }
            else startActivity(Intent(this, LoginActivity::class.java))
            finish()
        },2000)

    }
}