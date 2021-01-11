package xyz.cintiawan.titipyuk.model.item

data class ChatTextItem(
        val text: String,
        override val timestamp: Long,
        override val senderId: String?,
        override val recipientId: String,
        override val senderName: String?,
        override val type: String
) : ChatItem {
    constructor() : this("", -1, "", "", "", "")
}