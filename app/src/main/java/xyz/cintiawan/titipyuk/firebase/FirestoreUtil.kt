package xyz.cintiawan.titipyuk.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun usersCollectionRef() = firestoreInstance.collection("users")

    fun currentUserDocRef() = usersCollectionRef().document(FirebaseAuth.getInstance().currentUser?.uid ?: throw NullPointerException("UID is NULL"))
    fun otherUserDocRef(userId: String): DocumentReference = usersCollectionRef().document(userId)

    fun currentUserEngagedChannelsCollectionRef() = currentUserDocRef().collection("engagedChatChannel")
    fun otherUserEngagedChannelsCollectionRef(userId: String): CollectionReference = otherUserDocRef(userId).collection("engagedChatChannel")

    fun chatChannelsCollectionRef() = firestoreInstance.collection("chatChannels")
    fun chatChannelMessagesCollectionRef(channelId: String): CollectionReference = chatChannelsCollectionRef().document(channelId).collection("messages")
}