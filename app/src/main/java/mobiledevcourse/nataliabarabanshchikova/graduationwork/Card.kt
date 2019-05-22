package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    val id: String,
    val idList: String,
    val name: String,
    val desc: String,
    val closed: Boolean
) : Parcelable