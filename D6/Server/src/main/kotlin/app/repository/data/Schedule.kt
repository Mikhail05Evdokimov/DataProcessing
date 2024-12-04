package app.repository.data

import java.sql.Timestamp

data class InSchedule(
    val raceNumber : String,
    val origin : String,
    val dayOfWeek : String,
    val time: Timestamp,
)

data class OutSchedule(
    val raceNumber : String,
    val destination : String,
    val dayOfWeek : String,
    val time: Timestamp,
)
