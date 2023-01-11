package com.ml.siptren2.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.ml.siptren2.MainActivity
import com.ml.siptren2.R
import com.ml.siptren2.retrofits.API
import com.ml.siptren2.retrofits.APIRespon
import com.ml.siptren2.retrofits.RetrofitClient
import com.ml.siptren2.utils.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasscekActivity : AppCompatActivity() {

    //Pemesanan Variabel
    lateinit var myAPI: API
    var mySession: MySession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcek)

        //Inisialisasi Object Layout
        val tombolBack          = findViewById<ImageButton>(R.id.passcek_back)
        val tombolLanjutkan     = findViewById<Button>(R.id.passcek_btnLogin)
        val cekPassword         = findViewById<TextInputEditText>(R.id.passcek_edtPassword)

        //Inisialisasi variabel
        val retrofit    = RetrofitClient.instance
        myAPI           = retrofit.create(API::class.java)
        mySession       = MySession(applicationContext)

        //Code tombol
        tombolBack.setOnClickListener{finish()}
        tombolLanjutkan.setOnClickListener{
            if (cekPassword.text.toString().isEmpty()){
                cekPassword.setError("Masukkan Password lama anda!")
                cekPassword.requestFocus()
                return@setOnClickListener
            }
            cekPasswordLama(mySession!!.getValueInt("id")!!, cekPassword.text.toString())
        }
    }

    private fun cekPasswordLama(id: Int, password: String) {
        myAPI.cekPasswordLama(id, password)
            .enqueue(object: Callback<APIRespon> {
                override fun onFailure(call: Call<APIRespon>, t: Throwable) {
                    Toast.makeText(this@PasscekActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {
                    //val stringResponse = response.body()!!.message
                    //val stringResponse = response.body()!!.data?.NISN
                    val stringResponse = response.body()
                    println(stringResponse)

                    if (stringResponse == null){
                        Toast.makeText(this@PasscekActivity, "Password anda salah", Toast.LENGTH_SHORT).show()
                    }else{
                        //Memanggil Page Menu Utama
                        startActivity(Intent(this@PasscekActivity, UbahpassActivity::class.java))
                        finish()
                    }
                }


            })
    }

}