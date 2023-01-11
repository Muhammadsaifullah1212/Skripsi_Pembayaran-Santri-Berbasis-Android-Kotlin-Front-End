package com.ml.siptren2.payments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.ml.siptren2.R

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        SdkUIFlowBuilder.init()
            .setClientKey("Mid-client-BhtsdxMudYbwvM15")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                result ->
            })
            .setMerchantBaseUrl("https://ptpati.com/api_android/ppresponse.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()

        //Inisialisasi Object Layout
        val btnBayar        = findViewById<TextView>(R.id.payment_btnbayar)

        btnBayar.setOnClickListener{
            val transactionRequest = TransactionRequest("SIP-" + System.currentTimeMillis().toShort() + "", 600000.0)
            val detail = ItemDetails("NamaItemId", 600000.0, 1, "Kursus Kotlin (Nama)")
            val itemDetails = ArrayList<ItemDetails>()
            itemDetails.add(detail)
        }

    }
}