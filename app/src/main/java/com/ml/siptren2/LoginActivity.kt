package com.ml.siptren2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.ml.siptren2.retrofits.API
import com.ml.siptren2.retrofits.APIRespon
import com.ml.siptren2.retrofits.RetrofitClient
import com.ml.siptren2.utils.MySession
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    //Pemesanan Variabel
    lateinit var myAPI: API
    var mySession: MySession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Inisialisasi Object Layout
        val login_btnLogin      = findViewById<Button>(R.id.login_btnLogin)
        val login_edtNisn       = findViewById<TextInputEditText>(R.id.login_edtNisn)
        val login_edtPassword   = findViewById<TextInputEditText>(R.id.login_edtPassword)

        //Inisialisasi variabel
        val retrofit    = RetrofitClient.instance
        myAPI           = retrofit.create(API::class.java)
        mySession       = MySession(applicationContext)

        //Metode untuk menangani tombol LOGIN
        login_btnLogin.setOnClickListener {
            if (login_edtNisn.text.toString().isEmpty()){
                login_edtNisn.setError("Masukkan NISN anda!")
                login_edtNisn.requestFocus()
                return@setOnClickListener
            }
            if (login_edtPassword.text.toString().isEmpty()){
                login_edtPassword.setError("Masukkan Password anda!")
                login_edtPassword.requestFocus()
                return@setOnClickListener
            }

            //Melakukan Cek Data LOGIN
            cekLogin(login_edtNisn.text.toString(), login_edtPassword.text.toString())
        }
    }

    private fun cekLogin(nisn: String, psw: String) {
        val rbNisn: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), nisn)
        val rbPsw: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), psw)

        val uploadData = myAPI.loginUser(rbNisn, rbPsw)
        uploadData!!.enqueue(object: Callback<APIRespon> {
                override fun onFailure(call: Call<APIRespon>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {

                    //val stringResponse = response.body()!!.message
                    //val stringResponse = response.body()!!.data?.NISN
                    val stringResponse = response.body()
                    println(stringResponse)

                    if (stringResponse == null){
                        Toast.makeText(this@LoginActivity, "Akun anda belum terdaftar", Toast.LENGTH_SHORT).show()
                    }else{

                        mySession!!.simpanSession(
                            response.body()!!.data?.id!!, response.body()!!.data?.NISN.toString(),
                            response.body()!!.data?.Name.toString(), response.body()!!.data?.Jkelamin.toString(),
                            response.body()!!.data?.Telp.toString(), response.body()!!.data?.NamaOrtu.toString(),
                            response.body()!!.data?.Foto.toString(), response.body()!!.data?.Alamat.toString(),
                            response.body()!!.data?.Kamar?.NamaKamar.toString()
                        )

                        //Memanggil Page Menu Utama
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
    }

//    private fun cekLogin(nisn: String, password: String) {
//        myAPI.loginUser(nisn, password)
//            .enqueue(object: Callback<APIRespon> {
//                override fun onFailure(call: Call<APIRespon>, t: Throwable) {
//                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {
//
//                    //val stringResponse = response.body()!!.message
//                    //val stringResponse = response.body()!!.data?.NISN
//                    val stringResponse = response.body()
//                    println(stringResponse)
//
//                    if (stringResponse == null){
//                        Toast.makeText(this@LoginActivity, "Akun anda belum terdaftar", Toast.LENGTH_SHORT).show()
//                    }else{
//
//                        mySession!!.simpanSession(
//                            response.body()!!.data?.id!!, response.body()!!.data?.NISN.toString(),
//                            response.body()!!.data?.Name.toString(), response.body()!!.data?.Jkelamin.toString(),
//                            response.body()!!.data?.Telp.toString(), response.body()!!.data?.NamaOrtu.toString(),
//                            response.body()!!.data?.Foto.toString(), response.body()!!.data?.Alamat.toString(),
//                            response.body()!!.data?.Kamar?.NamaKamar.toString()
//                        )
//
//                        //Memanggil Page Menu Utama
//                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
////                        intent.putExtras(bundle)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//            })
//    }
}