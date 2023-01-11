package com.ml.siptren2.adapterviewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ml.siptren2.R

class DetailBayarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {

    var detailBayar_nama: TextView
    var detailBayar_nominal: TextView

    init {
        detailBayar_nama        = itemView.findViewById(R.id.detailbayar_biaya)
        detailBayar_nominal      = itemView.findViewById(R.id.detailbayar_biayanominal)
    }
}