package com.ichsanalfian.elog_pdam.ui.main.seller.verif

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.ActivityTambahBarangBinding
import com.ichsanalfian.elog_pdam.databinding.ActivityVerifBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.UserData
import com.ichsanalfian.elog_pdam.ui.auth.LoginActivity
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory

class VerifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifBinding
    private lateinit var preferences: UserPreferences
    private lateinit var sellerViewModel: SellerViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]

        preferences = UserPreferences(this)

        if (preferences.getUser().isVerified == 0){
            binding.btnVerif.setOnClickListener {
                binding.belumVerif.visibility = View.GONE
                binding.btnVerif.visibility = View.GONE
                val fragmentManager = supportFragmentManager
                val verifyFragment = VerifyFragment()
                val fragment = fragmentManager.findFragmentByTag(VerifyFragment::class.java.simpleName)

                if (fragment !is VerifyFragment){

                    Log.d("MyFlexibleFragment", "Fragment Name :" + VerifyFragment::class.java.simpleName)
                    fragmentManager
                        .beginTransaction()
                        .add(R.id.frame_container, verifyFragment, VerifyFragment::class.java.simpleName)
                        .commit()
                }
            }

        } else if (preferences.getUser().isVerified == 2){
            binding.apply {
                pending.visibility = View.VISIBLE
                belumVerif.visibility = View.GONE
                btnVerif.visibility = View.GONE
            }

        } else if (preferences.getUser().isVerified == 3){
            sellerViewModel.getReasonReject()
            binding.apply {
                sellerViewModel.getLiveReason().observe(this@VerifActivity){
                    alasan.text = "Alasan: \n\n"+it.message
                }
                rejected.visibility = View.VISIBLE
                alasan.visibility = View.VISIBLE
                btnVerifikasiLgi.visibility = View.VISIBLE

                belumVerif.visibility = View.GONE
                btnVerif.visibility = View.GONE

                btnVerifikasiLgi.setOnClickListener {
                    rejected.visibility = View.GONE
                    alasan.visibility = View.GONE
                    btnVerifikasiLgi.visibility = View.GONE
                    val fragmentManager = supportFragmentManager
                    val verifyFragment = VerifyFragment()
                    val fragment = fragmentManager.findFragmentByTag(VerifyFragment::class.java.simpleName)

                    if (fragment !is VerifyFragment){

                        Log.d("MyFlexibleFragment", "Fragment Name :" + VerifyFragment::class.java.simpleName)
                        fragmentManager
                            .beginTransaction()
                            .add(R.id.frame_container, verifyFragment, VerifyFragment::class.java.simpleName)
                            .commit()
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                preferences.setUser(UserData())
                startActivity(Intent(this@VerifActivity, LoginActivity::class.java))
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}