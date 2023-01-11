package com.ml.siptren2.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.ml.siptren2.LoginActivity
import com.ml.siptren2.MainActivity

class MySession (var context: Context) {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var PRIVATE_MODE    = 0

    companion object{
        private const val PREF_NAME = "siptren"
        private const val IS_LOGIN  = "false"

        //Const variabel lain
        const val KEY_ID          = "id"
        const val KEY_NISN        = "nisn"
        const val KEY_NAMA        = "nama"
        var KEY_JENISKEL          = "jeniskel"
        var KEY_TELP              = "telp"
        var KEY_ORTU              = "ortu"
        var KEY_FOTO              = "foto"
        var KEY_ALAMAT            = "alamat"
        var KEY_KAMAR             = "kamar"
    }

    init {
        sharedPreferences   = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor              = sharedPreferences.edit()
    }

    //Metode simpan data di File
    fun simpanSession(id: Int, nisn: String, nama: String, jeniskel: String, telp: String, ortu: String, foto: String, alamat: String, kamar: String){
        editor.putBoolean(IS_LOGIN, true)
        editor.putInt(KEY_ID, id)
        editor.putString(KEY_NISN, nisn)
        editor.putString(KEY_NAMA, nama)
        editor.putString(KEY_JENISKEL, jeniskel)
        editor.putString(KEY_TELP, telp)
        editor.putString(KEY_ORTU, ortu)
        editor.putString(KEY_FOTO, foto)
        editor.putString(KEY_ALAMAT, alamat)
        editor.putString(KEY_KAMAR, kamar)
        editor.commit()
    }

    val isLogin: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGIN, false)


    fun cekLogin(){
        if (!isLogin){
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags    = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }else{
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags    = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun logoutPengguna(){
        editor.clear()
        editor.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags    = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun getValueString(KEY_NAME: String): String?{
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int{
        return sharedPreferences.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean{
        return sharedPreferences.getBoolean(KEY_NAME, defaultValue)
    }
}