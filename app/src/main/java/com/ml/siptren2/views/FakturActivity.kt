package com.ml.siptren2.views

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.ml.siptren2.R
import com.ml.siptren2.adapters.PdfDocumentAdapter
import com.ml.siptren2.commons.Common
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.Throws

class FakturActivity : AppCompatActivity() {

    val file_name : String  = "faktur.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faktur)

        val btnFaktur = findViewById<Button>(R.id.faktur_tombol)

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object :PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    btnFaktur.setOnClickListener {
                        createPDFFile(Common.getAppPath(this@FakturActivity)+file_name)
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

            })
            .check()
    }

    private fun createPDFFile(path: String) {
        if (File(path).exists())
            File(path).delete()
        try{
            val document = Document()
            //Simpan PDF
            PdfWriter.getInstance(document, FileOutputStream(path))

            //Buka untuk ditulis
            document.open()

            //Setting
            document.pageSize   =  PageSize.A5
            document.addCreationDate()
            document.addAuthor("SipTren")
            document.addCreator("Pondok Pesantren Misbahul Hidayah")

            //Font Setting
            val colorAccent = BaseColor(0, 153, 204, 255)
            val headingFontSize = 20.0f
            val valueFontSize = 26.0f

            //Custom Font
//            val fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED)
            val fontName = BaseFont.createFont(BaseFont.HELVETICA, "UTF-8", BaseFont.EMBEDDED)

            //Menambahkan Judul Faktur
            val titleStyle = Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleStyle)

            val headingStyle = Font(fontName, headingFontSize, Font.NORMAL, colorAccent)
            addNewItem(document, "Order No.", Element.ALIGN_LEFT, headingStyle)
            val valueStyle = Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "#12345", Element.ALIGN_LEFT, valueStyle)

            addLineSparator(document)
            addNewItem(document, "Order Date", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, "03/08/2022", Element.ALIGN_LEFT, valueStyle)

            addLineSparator(document)
            addNewItem(document, "Account Name", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, "Mustofa Lutfi", Element.ALIGN_LEFT, valueStyle)

            addLineSparator(document)

            //Detail Produk
            addLineSpace(document)
            addNewItem(document, "Product Details", Element.ALIGN_CENTER, titleStyle)

            addLineSparator(document)

            //Item1
            addNewItemWithLeftAndRight(document, "Pizza 25", "(0.0%)", titleStyle, valueStyle)
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleStyle, valueStyle)

            addLineSparator(document)

            //Item2
            addNewItemWithLeftAndRight(document, "Pizza 26", "(0.0%)", titleStyle, valueStyle)
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleStyle, valueStyle)

            addLineSparator(document)

            //Total
            addLineSpace(document)
            addLineSpace(document)

            addNewItemWithLeftAndRight(document, "Total", "24000.0", titleStyle, valueStyle)

            //Close
            document.close()

            Toast.makeText(this@FakturActivity, "Sukses", Toast.LENGTH_LONG).show()

            printPDF()
        }catch (e: Exception){
            Log.e("SipTren", ""+e.message)
        }
    }

//    @Throws(DocumentException::class)
    private fun printPDF() {
        val printerManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try{
            val printAdapter = PdfDocumentAdapter(this@FakturActivity, Common.getAppPath(this@FakturActivity) + file_name)
            printerManager.print("Document1", printAdapter, PrintAttributes.Builder().build())
        }catch (e:Exception){
            Log.e("SipTren", ""+e.message)
        }
    }

    @Throws(DocumentException::class)
    private fun addNewItemWithLeftAndRight(
        document: Document,
        textLeft: String,
        textRight: String,
        leftStyle: Font,
        rightStyle: Font
    ) {
        val chunkTextLeft = Chunk(textLeft, leftStyle)
        val chunkTextRight = Chunk(textRight, rightStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun addLineSparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }
}

