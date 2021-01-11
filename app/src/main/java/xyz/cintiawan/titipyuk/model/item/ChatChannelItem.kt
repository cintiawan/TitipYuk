package xyz.cintiawan.titipyuk.model.item

data class ChatChannelItem(
        val channelId: String,
        val lastMessage: ChatItem?,
        val otherUserUid: String,
        val senderUid: String,
        val otherUserImage: String,
        val otherUserName: String,
        val unseen: Int
) {
    constructor() : this("", null, "", "", "", "", 0)
}