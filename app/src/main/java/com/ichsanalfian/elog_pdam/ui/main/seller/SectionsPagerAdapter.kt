package com.ichsanalfian.elog_pdam.ui.main.seller

import ProdukFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titles = arrayOf("Produk", "Transaksi") // Ganti dengan judul tab sesuai kebutuhan Anda
    private val fragments = arrayOf(ProdukFragment(), RequestFragment()) // Tambahkan fragment sesuai dengan judul tab

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return titles.size
    }
}
