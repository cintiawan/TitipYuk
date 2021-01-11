package xyz.cintiawan.titipyuk.model.filter

data class CityFilter(
        val id: Int,
        val name: String
) {
    constructor() : this(0, "")
    constructor(id: Int) : this(id, "")
    constructor(name: String) : this(0, name)
}