package com.ml.siptren2.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ml.siptren2.R
import com.ml.siptren2.retrofits.API
import com.ml.siptren2.retrofits.APIRespon
import com.ml.siptren2.retrofits.RetrofitClient
import com.ml.siptren2.utils.MySession
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class KonfirmasibayarActivity : AppCompatActivity() {

    //Pemesanan Variabel
    lateinit var myAPI: API
    var mySession: MySession? = null

    private lateinit var imgFoto: ImageView
    var bmp: Bitmap? = null
    private var photoFile: File? = null
    val CAPTURE_IMAGE_REQUEST = 1
    var mCurrentPhotoPath: String? = null

    var idTagihan: Int? = null
    var nilaiTagihan: Int? = null

    val formatter: NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasibayar)

        //Inisialisasi Object Layout
        val jumlahTagihan        = findViewById<TextView>(R.id.konfirmasi_nominal)
        val btnFoto              = findViewById<Button>(R.id.konfirmasi_btnFoto)
        val btnUpload            = findViewById<Button>(R.id.konfirmasi_btnUpload)
        imgFoto                  = findViewById(R.id.konfirmasi_imgFoto)

        //Inisialisasi variabel
        val retrofit    = RetrofitClient.instance
        myAPI           = retrofit.create(API::class.java)
        mySession       = MySession(applicationContext)

        //Menampilkan nilai dari Bundle halaman sebelumnya ke Objek Layout
        if(intent.extras != null){
            val bundle = intent.extras
            idTagihan = bundle?.getInt("idtagihan")
            jumlahTagihan.setText(formatter.format(bundle?.getInt("nominalbayar")))
            nilaiTagihan = bundle?.getInt("nominalbayar")
        }

        //Action pada Foto
        btnFoto.setOnClickListener{
            captureImage()
        }

        btnUpload.setOnClickListener{
            val builder = AlertDialog.Builder(this@KonfirmasibayarActivity)
            builder.setMessage("Apakah anda akan meng-Konfirmasi Pembayaran ini?")
                .setCancelable(false)
                .setPositiveButton("Iya") { dialog, id ->
                    val simpleDate = SimpleDateFormat("dd-MM-yyyy hh:mm")
                    val currentDate = simpleDate.format(Date())

//                    simpanPembayaran(mySession!!.getValueInt("id")!!, currentDate, idTagihan!!, nilaiTagihan!!, photoFile!!)
//                    simpanPembayaran(mySession!!.getValueInt("id")!!.toString(), currentDate, idTagihan!!.toString(), nilaiTagihan!!.toString(), photoFile!!)
                }
                .setNegativeButton("Batal") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

//    private fun simpanPembayaran(id: String, tanggal: String, idTagihan: String, nilaiNominal: String, file: File) {
//
//        val rbId: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), id)
//        val rbTanggal: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), tanggal)
//        val rbIdTagihan: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), idTagihan)
//        val rbNilaiTagihan: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), nilaiNominal)
//        val rbFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//        val rbBukti: MultipartBody.Part = MultipartBody.Part.createFormData("Bukti", file!!.name, rbFile)
//
//        val uploadData = myAPI.bayarSpp(rbId, rbTanggal, rbIdTagihan, rbNilaiTagihan, rbBukti)
//        uploadData!!.enqueue(object: Callback<APIRespon> {
//            override fun onFailure(call: Call<APIRespon>, t: Throwable) {
//                Toast.makeText(this@KonfirmasibayarActivity, t.message, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(call: Call<APIRespon>, response: Response<APIRespon>) {
//                //val stringResponse = response.body()!!.message
//                //val stringResponse = response.body()!!.data?.NISN
//                val stringResponse = response.body()
//                //println(stringResponse)
//
//                if (stringResponse == null){
//                    Toast.makeText(this@KonfirmasibayarActivity, "Gagal Simpan Pembayaran Anda", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(this@KonfirmasibayarActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        })
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            bmp = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            imgFoto.setImageBitmap(bmp)
        }else{
            imgFoto.setImageURI(data?.data)
        }
    }

    private fun captureImage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(this,"com.ml.siptren2.fileprovider", photoFile!!)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext, ex.message.toString())
                }
            } else {
                displayMessage(baseContext, "Null")
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}