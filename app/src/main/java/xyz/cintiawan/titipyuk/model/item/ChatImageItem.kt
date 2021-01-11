package xyz.cintiawan.titipyuk.model.item

data class ChatImageItem(
        val image: String,
        override val timestamp: Long,
        override val senderId: String?,
        override val recipientId: String,
        override val senderName: String?,
        override val type: String
) : ChatItem {
    constructor() : this("", -1, "", "", "", "")
}