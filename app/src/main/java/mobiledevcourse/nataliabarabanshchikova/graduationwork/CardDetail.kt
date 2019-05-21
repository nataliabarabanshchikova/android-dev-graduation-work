package mobiledevcourse.nataliabarabanshchikova.graduationwork

data class CardDetail(
    val id: String,
    val name: String,
    val desc: String,
    val closed: Boolean,
    val attachments: ArrayList<CardAttachment>,
    val url: String
)