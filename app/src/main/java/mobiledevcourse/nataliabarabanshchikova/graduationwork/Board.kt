package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "board_entity")
data class Board(
    @PrimaryKey val id: String,
    val name: String,
    val desc: String,
    val closed: Boolean
) : Parcelable