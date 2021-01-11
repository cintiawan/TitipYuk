package xyz.cintiawan.titipyuk.model.item

data class ChatChannelFirebaseItem(
        val channelId: String,
        val lastMessage: String,
        val lastMessageTimestamp: Long,
        val lastMessageType: String,
        val otherUserUid: String,
        val senderUid: String,
        val unseen: Int
) {
    constructor() : this("", "", -1, "", "","", 0)
}