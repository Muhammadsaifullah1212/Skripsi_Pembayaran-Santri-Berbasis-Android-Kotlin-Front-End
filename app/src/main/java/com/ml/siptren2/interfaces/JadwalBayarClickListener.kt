package com.ml.siptren2.interfaces

import android.view.View
import com.ml.siptren2.models.ModelJadwalBayar

interface JadwalBayarClickListener {
    fun onItemClicked(view: View, dataJadwalBayar: ModelJadwalBayar)
}