package com.wavez.trackerwater.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "history_table")
data class HistoryModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("amountHistory")
    var amountHistory: Int = 0,
    @ColumnInfo("dateHistory")
    var dateHistory: Long = System.currentTimeMillis()
) : Parcelable