package app.repository.data

import java.sql.Timestamp

data class CheckIn(
    val bookRef : String,
    val phoneNumber : String,
)

data class BoardingPass(
    val flightId: String,
    val flightNumber: String,
    val seatPlace: String,
    val origin: String,
    val destination: String,
    val departureTime: Timestamp,
)

data class BoardingPassWithNumber(
    val flightId: String,
    val flightNumber: String,
    val ticketNumber: String,
    val passNumber: Int,
    val seatPlace: String,
    val origin: String,
    val destination: String,
    val departureTime: Timestamp,
)

data class BoardingPassResponse(
    val flightNumber: String,
    val seatPlace: String,
    val origin: String,
    val destination: String,
    val departureTime: Timestamp,
)

data class FlightFull(
    val flightNumber: String,

)