package mobiledevcourse.nataliabarabanshchikova.graduationwork

import java.util.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//data class Board(val id: Long, val name: String, val desc: String, val created: String)

@Parcelize
data class Board(
    val id: String,
    val name: String,
    val desc: String,
    val dateLastActivity: Date,
    val closed: Boolean
) : Parcelable