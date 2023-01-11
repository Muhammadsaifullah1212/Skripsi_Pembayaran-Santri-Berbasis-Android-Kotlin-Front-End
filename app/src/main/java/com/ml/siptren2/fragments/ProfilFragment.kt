package com.ml.siptren2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.ml.siptren2.LoginActivity
import com.ml.siptren2.R
import com.ml.siptren2.utils.MySession
import com.ml.siptren2.views.FakturActivity
import com.ml.siptren2.views.PasscekActivity
import com.squareup.picasso.Picasso

class ProfilFragment : Fragment() {

    //Variabel - variabel
    var mySession: MySession? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_profil, container, false)

        //Inisialisasi Object Layout
        val btnubah         = view.findViewById<LinearLayout>(R.id.profil_ubahpassword)
        val btnlogout       = view.findViewById<LinearLayout>(R.id.profil_logout)
        val profilNama      = view.findViewById<TextView>(R.id.profil_nama)
        val profilNisn      = view.findViewById<TextView>(R.id.profil_nisn)
        val profilJenisKel  = view.findViewById<TextView>(R.id.profil_jeniskelamin)
        val profilTelp      = view.findViewById<TextView>(R.id.profil_kontak)
        val profilKamar     = view.findViewById<TextView>(R.id.profil_kamar)
        val profilOrtu      = view.findViewById<TextView>(R.id.profil_ortu)
        val profilAlamat    = view.findViewById<TextView>(R.id.profil_alamat)
        val profilFoto      = view.findViewById<ImageView>(R.id.profil_gambar)

        //Inisialisasi Session
        mySession           = MySession(requireActivity()!!.applicationContext)

        //Menampilkan data dari SESSION
        profilNama.setText(mySession!!.getValueString("nama")!!)
        profilNisn.setText(mySession!!.getValueString("nisn")!!)
        profilJenisKel.setText(mySession!!.getValueString("jeniskel")!!)
        profilTelp.setText(mySession!!.getValueString("telp")!!)
        profilKamar.setText(mySession!!.getValueString("kamar")!!)
        profilOrtu.setText(mySession!!.getValueString("ortu")!!)
        profilAlamat.setText(mySession!!.getValueString("alamat")!!)
        Picasso.with(context)
            .load(mySession!!.getValueString("foto")!!)
            .into(profilFoto)

        // Handle Click Tombol Logout
        btnlogout.setOnClickListener{ funLogout() }
        btnubah.setOnClickListener{ funUbahPassword() }

        return view
    }

    // Function untuk Logout
    private fun funLogout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah kamu akan Logout dari SipTren?")
            .setCancelable(false)
            .setPositiveButton("Iya") { _, _ ->
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                mySession!!.logoutPengguna()
                requireActivity().finish()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    // Funtion untuk Ubah Password
    private fun funUbahPassword(){
        startActivity(Intent(requireActivity(), PasscekActivity::class.java))
    }
}