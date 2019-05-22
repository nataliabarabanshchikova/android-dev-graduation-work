package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class List(
    val id: String,
    val name: String
) : Parcelable