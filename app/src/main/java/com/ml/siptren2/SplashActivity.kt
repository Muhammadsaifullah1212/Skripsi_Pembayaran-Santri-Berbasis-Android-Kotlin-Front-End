package com.ml.siptren2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ml.siptren2.utils.MySession

class SplashActivity : AppCompatActivity() {

    //Pemesanan Variabel
    private var mDelayHandler: Handler? = null
    private val SPLASH_DURASI: Long = 4000
    var mySession: MySession? = null

    //Methode untuk event/kejadian setelah Splash Berakhir
    internal val mRunnable: Runnable = Runnable {
        //val intent = Intent(applicationContext, LoginActivity::class.java)
        //startActivity(intent)

        mySession!!.cekLogin()
        //Terjadi splash Screen
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Menyembunyikan Status Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_splash)

        //Inisialisasi Handler
        mDelayHandler = Handler()

        mySession   = MySession(applicationContext)
        //Navigasi dengan durasi
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DURASI)
    }
}