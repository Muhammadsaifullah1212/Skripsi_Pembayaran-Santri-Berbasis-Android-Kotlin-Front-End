package com.ml.siptren2.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.ml.siptren2.R
import com.ml.siptren2.retrofits.API
import com.ml.siptren2.retrofits.APIRespon
import com.ml.siptren2.retrofits.RetrofitClient
import com.ml.siptren2.utils.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UbahpassActivity : AppCompatActivity() {

    //Pemesanan Variabel
    lateinit var myAPI: API
    var mySession: MySession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubahpass)

        //Inisialisasi Object Layout
        val tombolBack          = findViewById<ImageButton>(R.id.ubahpass_back)
        val tombolSimpan        = findViewById<Button>(R.id.ubahpass_btnSimpan)
        val pass1               = findViewById<TextInputEditText>(R.id.ubahpass_edtPass1)
        val pass2               = findViewById<TextInputEditText>(R.id.ubahpass_edtPass2)

        //Inisialisasi variabel
        val retrofit    = RetrofitClient.instance
        myAPI           = retrofit.create(API::class.java)
        mySession       = MySession(applicationContext)

        //Code tombol
        tombolBack.setOnClickListener{finish()}
        tombolSimpan.setOnClickListener{
            if (pass1.text.toString().isEmpty()){
                pass1.setError("Masukkan Password baru anda!")
                pass1.requestFocus()
                return@setOnClickListener
            }
            if (pass2.text.toString().isEmpty()){
                pass2.setError("Ulangi password anda!")
                pass2.requestFocus()
                return@setOnClickListener
            }
            if (pass1.text.toString().equals(pass2.text.toString())){
                ubahPasswordAnda(mySession!!.getValueInt("id")!!, pass1.text.toString(), pass2.text.toString())
            }else{
                Toast.makeText(this@UbahpassActivity, "Konfirmasi Password anda salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ubahPasswordAnda(id: Int, password1: String, password2: String) {
        myAPI.ubahPassword(id, password1, password2)
            .enqueue(object: Callback<APIRespon> {
                override fun onFailure(call: Call<APIRespon>, t: Throwable) {
                    Toast.makeText(this@UbahpassActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {
                    //val stringResponse = response.body()!!.message
                    //val stringResponse = response.body()!!.data?.NISN
                    val stringResponse = response.body()
                    println(stringResponse)

                    if (stringResponse == null){
                        Toast.makeText(this@UbahpassActivity, "Proses perubahan password gagal!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@UbahpassActivity, "Password anda SUKSES diubah..!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }


            })
    }
}