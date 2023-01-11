package com.ml.siptren2.adapterviewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ml.siptren2.R

class JadwalBayarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var itemJadwal_tempo: TextView
    var itemJadwal_nominal: TextView
    var itemJadwal_status: TextView

    init {
        itemJadwal_tempo        = itemView.findViewById(R.id.itemJadwal_tempo)
        itemJadwal_nominal      = itemView.findViewById(R.id.itemJadwal_nominal)
        itemJadwal_status       = itemView.findViewById(R.id.itemJadwal_status)
    }
}