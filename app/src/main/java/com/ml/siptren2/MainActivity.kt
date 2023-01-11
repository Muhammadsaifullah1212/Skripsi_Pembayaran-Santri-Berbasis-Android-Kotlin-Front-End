package com.ml.siptren2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ml.siptren2.databinding.ActivityMainBinding
import com.ml.siptren2.fragments.BerandaFragment
import com.ml.siptren2.fragments.ProfilFragment
import com.ml.siptren2.fragments.RiwayatFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Default Fragment Beranda
        val fragment = BerandaFragment()
        defaultFragment(fragment)

        //Metode pilihan menu bottom
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.beranda -> {
                    val fragment = BerandaFragment()
                    defaultFragment(fragment)
                    //return@setOnItemSelectedListener
                }
//                R.id.riwayat -> {
//                    val fragment = RiwayatFragment()
//                    defaultFragment(fragment)
//                    //return@setOnItemSelectedListener
//                }
                R.id.profil -> {
                    val fragment = ProfilFragment()
                    defaultFragment(fragment)
                    //return@setOnItemSelectedListener
                }
            }
            true
        }
    }

    //Metode Default Fragment
    private fun defaultFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}