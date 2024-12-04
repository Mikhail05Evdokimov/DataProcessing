package app.repository.data

import java.sql.Timestamp

data class Booking(
    val user: User,
    val flights: List<Flight>,
)

 data class User(
     val name: String,
     val contactData: ContactData,
 )

data class ContactData(
    val phone: String,
    val email: String,
)

data class Flight(
    val id: String,
    val clazz: String,
)

data class FlightInfo(
    val id: String,
    val amount: Int,
    val time: Timestamp,
    val clazz: String,
)

data class BookResponse(
    val bookRef: String,
    val tickets: List<String>,
)