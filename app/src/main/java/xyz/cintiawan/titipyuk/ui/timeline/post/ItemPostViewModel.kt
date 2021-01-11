package xyz.cintiawan.titipyuk.ui.timeline.post

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.PostItem

class ItemPostViewModel : BaseViewModel() {
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val likes = MutableLiveData<String>()
    val comments = MutableLiveData<String>()
    val shares = MutableLiveData<String>()

    fun bind(post: PostItem) {
        userImage.value = post.user.image
        userName.value = post.user.name
        if(post.likes > 0) likes.value = post.likes.toString()
        if(post.comments > 0) comments.value = post.comments.toString()
        if(post.shares > 0) shares.value = post.shares.toString()
    }

}