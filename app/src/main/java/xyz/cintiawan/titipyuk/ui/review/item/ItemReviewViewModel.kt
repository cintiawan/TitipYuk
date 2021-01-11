package xyz.cintiawan.titipyuk.ui.review.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.ReviewItem

class ItemReviewViewModel : BaseViewModel() {
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val score = MutableLiveData<Int>()
    val comment = MutableLiveData<String>()
    val createdAt = MutableLiveData<String>()

    fun bind(review: ReviewItem?) {
        userImage.value = review?.reviewer?.image
        userName.value = review?.reviewer?.name
        score.value = review?.rating
        comment.value = review?.pesan
        createdAt.value = ""
    }

}