package com.ml.siptren2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ml.siptren2.R
import com.ml.siptren2.adapterviewholders.JadwalBayarViewHolder
import com.ml.siptren2.interfaces.JadwalBayarClickListener
import com.ml.siptren2.models.ModelJadwalBayar
import java.text.DecimalFormat
import java.text.NumberFormat

class JadwalBayarAdapter(
    var context: Context,
    var listJadwalBayar: List<ModelJadwalBayar>,
    var listener: JadwalBayarClickListener
):
    RecyclerView.Adapter<JadwalBayarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalBayarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_jadwalbayar, parent, false)
        return JadwalBayarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listJadwalBayar.size
    }

    override fun onBindViewHolder(holder: JadwalBayarViewHolder, position: Int) {
        val formatter: NumberFormat = DecimalFormat("#,###")

        holder.itemJadwal_tempo.text    = listJadwalBayar[position].TglJatuhTempo
        holder.itemJadwal_nominal.text  = formatter.format(listJadwalBayar[position].Tagihan)
        holder.itemJadwal_status.text   = if (listJadwalBayar[position].TglBayar == "") "" else if (listJadwalBayar[position].Status == "KOSONG") "" else listJadwalBayar[position].Status

        holder.itemView.setOnClickListener {
            listener?.onItemClicked(it, listJadwalBayar[position])
        }
    }
}