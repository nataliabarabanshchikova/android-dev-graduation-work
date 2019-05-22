package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardDetail(
    val id: String,
    val name: String,
    val desc: String,
    val closed: Boolean,
    val attachments: ArrayList<CardAttachment>,
    val url: String
) : Parcelable