package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CardAttachment (
    val id: String,
    val bytes: Int,
    val name: String,
    val date: Date,
    val url: String
) : Parcelable