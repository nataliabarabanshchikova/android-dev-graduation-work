package mobiledevcourse.nataliabarabanshchikova.graduationwork

import java.util.*

//data class Board(val id: Long, val name: String, val desc: String, val created: String)

data class Board(
    val id: String,
    val name: String,
    val desc: String,
    val dateLastActivity: Date,
    val closed: Boolean
)