package app.repository.data

data class Airport(
    val code : String,
    val name: String,
    val city: String,
    val timeZone: String,
)

data class Name(
    val en : String,
    val ru : String,
)
