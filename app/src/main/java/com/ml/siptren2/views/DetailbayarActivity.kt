package com.ml.siptren2.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.ml.siptren2.R
import com.ml.siptren2.adapters.DetailBayarAdapter
import com.ml.siptren2.models.ModelDetailBayar
import com.ml.siptren2.retrofits.API
import com.ml.siptren2.retrofits.APIRespon
import com.ml.siptren2.retrofits.RetrofitClient
import com.ml.siptren2.utils.MySession
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailbayarActivity : AppCompatActivity() {

    //Variabel - variabel
    val bundle = Bundle()
    var keteranganTagihan: String? = null
    var idTagihan: Int? = null
    var jumlahTagihan: Int? = null
    private lateinit var detailbayarMain: LinearLayout
    private lateinit var detailBayarRv: RecyclerView
    private lateinit var detailBayarSwiper: SwipeRefreshLayout
    private lateinit var api: API
    var compositeDisposable = CompositeDisposable()
    var loadDataDetailBayar: List<ModelDetailBayar> = ArrayList()
    var detailBayarAdapter: DetailBayarAdapter? = null
    var mySession: MySession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val formatter: NumberFormat = DecimalFormat("#,###")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailbayar)

        //MidTrans
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-G58uxcreVgezrXoR")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                    result ->
            })
            .setMerchantBaseUrl("https://ptpati.com/api_android/ppresponse.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()

        //Inisialisasi Session
        mySession           = MySession(applicationContext)

        //Inisialisasi Object Layout
        val bulanTagihan        = findViewById<TextView>(R.id.detailbayar_bulan)
        val jatuhTempo          = findViewById<TextView>(R.id.detailbayar_jatuhtempo)
        val nominal             = findViewById<TextView>(R.id.detailbayar_nominal)
        val tglBayar            = findViewById<TextView>(R.id.detailbayar_tglbayar)
        val status              = findViewById<TextView>(R.id.detailbayar_status)
        val btnBayar            = findViewById<Button>(R.id.detailbayar_btnbayar)
        val btnFaktur           = findViewById<Button>(R.id.detailbayar_btnfaktur)
        detailbayarMain         = findViewById(R.id.detailbayar_main)

        detailBayarRv           = findViewById(R.id.detailbayar_recycler)
        detailBayarSwiper       = findViewById(R.id.detailbayar_swiper)

        //Menampilkan nilai dari Bundle halaman sebelumnya ke Objek Layout
        if(intent.extras != null){
            val bundle = intent.extras
            idTagihan = bundle?.getInt("id")
            bulanTagihan.setText(bundle?.getString("bulan"))
            keteranganTagihan = bundle?.getString("bulan")
            jatuhTempo.setText(bundle?.getString("jatuhtempo"))
            nominal.setText(formatter.format(bundle?.getInt("nominal")))
            jumlahTagihan = bundle?.getInt("nominal");
            tglBayar.setText(bundle?.getString("tglbayar"))
            status.setText(bundle?.getString("status"))
        }

        if (status.text == "LUNAS"){
            btnBayar.isEnabled = false
            btnFaktur.isEnabled = true
        }else if(status.text == "MENUNGGU KONFIRMASI") {
            btnBayar.isEnabled = false
            btnFaktur.isEnabled = false
        }else{
            btnBayar.isEnabled = true
            btnFaktur.isEnabled = false
        }

        //Implementasi Class/Interface
        val retrofit    = RetrofitClient.instance
        api             = retrofit.create(API::class.java)

        //SetRecyclerView
        detailBayarRv.layoutManager  = LinearLayoutManager(this@DetailbayarActivity, LinearLayoutManager.VERTICAL, false)
        detailBayarRv.setHasFixedSize(true)
        detailBayarRv.post { getDetailBayar() }
        detailBayarSwiper.setOnRefreshListener {
            detailBayarSwiper.isRefreshing = true
            getDetailBayar()
        }

        //Funtion Tombol
        btnBayar.setOnClickListener{
            /*
            bundle.putInt("nominalbayar", jumlahTagihan!!)
            bundle.putInt("idtagihan", idTagihan!!)

            val intent = Intent(this@DetailbayarActivity, KonfirmasibayarActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
             */

            val transactionRequest = TransactionRequest("SIP-" + System.currentTimeMillis().toShort() + "", jumlahTagihan!!.toDouble())
            val detail = ItemDetails("Pembayaran", jumlahTagihan!!.toDouble(), 1, keteranganTagihan)
            val itemDetails = ArrayList<ItemDetails>()
            itemDetails.add(detail)
            uiKitDetails(transactionRequest, mySession!!.getValueString("nama")!!, mySession!!.getValueString("telp")!!)
            transactionRequest.itemDetails = itemDetails
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)

            //Simpan ke database
            val simpleDate = SimpleDateFormat("dd-MM-yyyy hh:mm")
            val currentDate = simpleDate.format(Date())
            simpanPembayaran(mySession!!.getValueInt("id")!!.toString(), currentDate, idTagihan!!.toString(), jumlahTagihan!!.toString())
        }
        btnFaktur.setOnClickListener{
            captureScreenshot()
        }
    }

    private fun simpanPembayaran(id: String, tanggal: String, idTagihan: String, nilaiNominal: String) {
        val rbId: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), id)
        val rbTanggal: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), tanggal)
        val rbIdTagihan: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), idTagihan)
        val rbNilaiTagihan: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), nilaiNominal)

        val uploadData = api.bayarSpp(rbId, rbTanggal, rbIdTagihan, rbNilaiTagihan)
        uploadData.enqueue(object: Callback<APIRespon> {
            override fun onFailure(call: Call<APIRespon>, t: Throwable) {
                Toast.makeText(this@DetailbayarActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {
                //val stringResponse = response.body()!!.message
                //val stringResponse = response.body()!!.data?.NISN
                val stringResponse = response.body()
                //println(stringResponse)

                if (stringResponse == null){
                    Toast.makeText(this@DetailbayarActivity, "Gagal Simpan Pembayaran Anda", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@DetailbayarActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })
    }

    private fun uiKitDetails(transactionRequest: TransactionRequest, nama: String, telp: String) {
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = nama
        customerDetails.phone = telp
        customerDetails.firstName = "-"
        customerDetails.lastName = "-"
        customerDetails.email = ""
        transactionRequest.customerDetails = customerDetails

//        val billingAddress = BillingAddress()
//        billingAddress.address = "Banda Aceh"
//        billingAddress.city = "Banda Aceh"
//        billingAddress.postalCode = "23233"
//        customerDetails.billingAddress = billingAddress
    }

    //Metode untuk tampil data Jadwal Bayar
    private fun getDetailBayar(){
        compositeDisposable!!.add(api!!.detailPembayaran(idTagihan!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data1 ->
                getData(data1)
            })
    }

    //Metode ambil data
    private fun getData(listData: List<ModelDetailBayar>){
        loadDataDetailBayar = listData
        detailBayarAdapter = DetailBayarAdapter(applicationContext, listData)
        detailBayarRv.adapter = detailBayarAdapter
        detailBayarSwiper.isRefreshing = false
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
        getDetailBayar()
        super.onResume()
    }

    //Function untuk captureScreenShoot
    private fun captureScreenshot(){
        val sekarang = Date()
        DateFormat.format("dd-mm-yyyy_hh:mm:ss", sekarang)

        val path = getExternalFilesDir(null)!!.absolutePath + "/" + sekarang + ".jpg"
        var bitmap = Bitmap.createBitmap(detailbayarMain.width, detailbayarMain.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        detailbayarMain.draw(canvas)
        val imageFile = File(path)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val URI = FileProvider.getUriForFile(applicationContext, "com.ml.siptren2.fileprovider", imageFile)

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "Bukti Pembayaran" + "\n" + keteranganTagihan)
        intent.putExtra(Intent.EXTRA_STREAM, URI)
        intent.type = "text/plain"
        startActivity(intent)
    }
}