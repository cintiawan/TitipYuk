package xyz.cintiawan.titipyuk.model.item

interface ChatItem {
    val timestamp: Long
    val senderId: String?
    val recipientId: String
    val senderName: String?
    val type: String
}