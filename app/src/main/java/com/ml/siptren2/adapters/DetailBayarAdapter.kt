package com.ml.siptren2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ml.siptren2.R
import com.ml.siptren2.adapterviewholders.DetailBayarViewHolder
import com.ml.siptren2.adapterviewholders.JadwalBayarViewHolder
import com.ml.siptren2.interfaces.JadwalBayarClickListener
import com.ml.siptren2.models.ModelDetailBayar
import com.ml.siptren2.models.ModelJadwalBayar
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailBayarAdapter(
    var context: Context,
    var listDetailBayar: List<ModelDetailBayar>
):
    RecyclerView.Adapter<DetailBayarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailBayarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_detailbayar, parent, false)
        return DetailBayarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDetailBayar.size
    }

    override fun onBindViewHolder(holder: DetailBayarViewHolder, position: Int) {
        holder.detailBayar_nama.text    = listDetailBayar[position].NamaBiaya
        holder.detailBayar_nominal.text  = listDetailBayar[position].Nominal
    }
}