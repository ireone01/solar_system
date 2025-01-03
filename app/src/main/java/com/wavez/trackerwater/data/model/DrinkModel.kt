package com.wavez.trackerwater.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*
@kotlinx.parcelize.Parcelize
@Entity(tableName = "drink_table")
data class DrinkModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0,
    @ColumnInfo("amountDrink")
    val amountDrink: Float = 0f,
    @ColumnInfo("countDrink")
    var countDrink: Int = 0,
    @ColumnInfo("dateDrink")
    val dateDrink: Long = System.currentTimeMillis()
) : Parcelable