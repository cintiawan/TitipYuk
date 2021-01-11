package xyz.cintiawan.titipyuk.model.item

data class UserFirebaseItem(
        val email: String,
        val name: String,
        val image: String
) {
    constructor() : this("", "", "")
}