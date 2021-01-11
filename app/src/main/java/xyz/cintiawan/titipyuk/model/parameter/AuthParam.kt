package xyz.cintiawan.titipyuk.model.parameter

data class AuthParam(
        val email: String,
        val uid: String?,
        val name: String,
        val telepon: String,
        val password: String,
        val passwordConf: String
) {
    constructor(email: String, password: String) : this(email, "", "", "", password, "")
}