package com.ml.siptren2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ml.siptren2.R
import com.ml.siptren2.adapters.JadwalBayarAdapter
import com.ml.siptren2.interfaces.JadwalBayarClickListener
import com.ml.siptren2.models.ModelJadwalBayar
import com.ml.siptren2.retrofits.API
import com.ml.siptren2.retrofits.RetrofitClient
import com.ml.siptren2.utils.MySession
import com.ml.siptren2.views.DetailbayarActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class BerandaFragment : Fragment(), JadwalBayarClickListener {

    //Variabel - variabel
    val bundle = Bundle()
    private lateinit var berandaRv: RecyclerView
    private lateinit var berandaSwiper: SwipeRefreshLayout
    private lateinit var api: API
    var compositeDisposable = CompositeDisposable()
    var loadDataJadwalBayar: List<ModelJadwalBayar> = ArrayList()
    var jadwalByarAdapter: JadwalBayarAdapter? = null
    var mySession: MySession? = null

    private lateinit var formatted: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_beranda, container, false)

        //Tanggal hari ini
//        val current = LocalDate.now()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        formatted = current.format(formatter)
//        println("Current Date and Time is: $formatted")

        //Inisialisasi Object Layout
        val berandaNama         = view.findViewById<TextView>(R.id.beranda_nama)
        val berandaNisn         = view.findViewById<TextView>(R.id.beranda_nisn)
        val berandaKamar        = view.findViewById<TextView>(R.id.beranda_kamar)
        val berandaOrtu         = view.findViewById<TextView>(R.id.beranda_ortu)
        val berandaFoto         = view.findViewById<ImageView>(R.id.beranda_foto)

        berandaRv           = view.findViewById(R.id.beranda_recycler)
        berandaSwiper       = view.findViewById(R.id.beranda_swiper)

        //Inisialisasi Session
        mySession           = MySession(requireActivity()!!.applicationContext)

        //Implementasi Class/Interface
        val retrofit        = RetrofitClient.instance
        api                 = retrofit.create(API::class.java)

        //Tampilkan data ke Object dari SESSION
        berandaNama.setText(mySession!!.getValueString("nama")!!)
        berandaNisn.setText(mySession!!.getValueString("nisn")!!)
        berandaKamar.setText(mySession!!.getValueString("kamar")!!)
        berandaOrtu.setText(mySession!!.getValueString("ortu")!!)
        Picasso.with(context)
            .load(mySession!!.getValueString("foto")!!)
            .into(berandaFoto)

        //SetRecyclerView
        berandaRv.layoutManager  = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        berandaRv.setHasFixedSize(true)
        berandaRv.post { getJadwalBayar() }
        berandaSwiper.setOnRefreshListener {
            berandaSwiper.isRefreshing = true
            getJadwalBayar()
        }

        return view
    }

    //Metode untuk tampil data Jadwal Bayar
    private fun getJadwalBayar(){
        val simpleDate = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = simpleDate.format(Date())

        compositeDisposable!!.add(api!!.jadwalPembayaran(mySession!!.getValueInt("id"), currentDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data1 ->
                getData(data1)
            })
    }

    //Metode ambil data
    private fun getData(listData: List<ModelJadwalBayar>){
        loadDataJadwalBayar = listData
        jadwalByarAdapter = JadwalBayarAdapter(requireContext()!!.applicationContext, listData, this)
        berandaRv.adapter = jadwalByarAdapter
        berandaSwiper.isRefreshing = false
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onResume() {
        getJadwalBayar()
        super.onResume()
    }

    override fun onItemClicked(view: View, dataJadwalBayar: ModelJadwalBayar) {
        //Mengirimkan data dari Login ke Halaman Detail Pembayaran
        bundle.putInt("id", dataJadwalBayar.id)
        bundle.putString("bulan", dataJadwalBayar.Bulan + ' ' + dataJadwalBayar.TahunAjaran)
        bundle.putString("jatuhtempo", dataJadwalBayar.TglJatuhTempo)
        bundle.putInt("nominal", dataJadwalBayar.Tagihan)
        bundle.putString("tglbayar", dataJadwalBayar.TglBayar)
        bundle.putString("status", if (dataJadwalBayar.Status == "KOSONG") "" else dataJadwalBayar.Status)

        val intent = Intent(requireActivity(), DetailbayarActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }


}