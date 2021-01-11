package xyz.cintiawan.titipyuk.model.filter

data class ReviewFilter(
        val auth: Boolean = true,
        val userId: Int = 0
)