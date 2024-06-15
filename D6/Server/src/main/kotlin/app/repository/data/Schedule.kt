package app.repository.data

import java.sql.Time

data class Schedule(
    val raceNumber : String,
    val originOrDestination : String,
    val dayOfWeek : String,
    val time: Time,
)
