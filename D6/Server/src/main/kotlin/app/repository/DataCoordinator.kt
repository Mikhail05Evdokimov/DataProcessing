package app.repository

import app.repository.data.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.postgresql.Driver
import org.postgresql.util.PGTime
import org.postgresql.util.PGobject
import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.ResultSet
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class DataCoordinator {

    companion object {
        val shared = DataCoordinator()
    }

    private val driver = Driver()
    private val gson = Gson()
    private var connection : Connection? = null
    private var bookRef = AtomicLong(0)
    private var ticketRef = AtomicLong(0)

    suspend fun init() {
        withContext(Dispatchers.IO) {
            val info = Properties()
            info.setProperty("user", "postgres")
            info.setProperty("password", "re6orn")
            info.setProperty("logLevel", "DEBUG")
            connection = driver.connect("jdbc:postgresql://localhost:5433/demo", info)
            println("Connection opened: " + !connection!!.isClosed)
            initRefs()
        }
    }

    private fun initRefs() {
        val st = connection!!.prepareStatement("select book_ref from bookings", ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        val res = st.executeQuery()
        res.last()
        var rawRef = res.getString("book_ref")
        while (rawRef.contains(" ")) {
            rawRef = rawRef.removeSuffix(" ")
        }
        bookRef = AtomicLong(rawRef.toLong())
        res.close()
        val st1 = connection!!.prepareStatement("select ticket_no from tickets order by ticket_no desc limit 1", ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        val res1 = st1.executeQuery()
        res1.last()
        ticketRef = AtomicLong(res1.getString("ticket_no").toLong())
        res1.close()
    }

    private fun convertList(obj : Collection<Any>) : String {
        return gson.toJson(obj)
    }

    private fun fromPSOtoKotlinObject(obj : PGobject) : Name {
        return gson.fromJson(obj.value!!, Name::class.java)
    }

    private fun bookRefGenerator() : Long {
        return bookRef.incrementAndGet()
    }

    private fun ticketRefGenerator() : String {
        var rawRef = ticketRef.incrementAndGet().toString()
        while (rawRef.length < 13) {
            rawRef = "0$rawRef"
        }
        return rawRef
    }

    suspend fun getAirports(language : Language, pagination : Int?, page : Int?, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("Select * from airports_data", ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE)
            val res = st.executeQuery()
            val list = mutableListOf<Airport>()
            if (pagination != null && page != null) {
                res.relative(page * pagination)
            }
            var cnt = 0
            while (res.next()) {
                var airportName = ""
                var cityName = ""
                when(language) {
                    Language.EN -> { airportName = fromPSOtoKotlinObject(res.getObject("airport_name") as PGobject).en
                        cityName = fromPSOtoKotlinObject(res.getObject("city") as PGobject).en }
                    Language.RU -> { airportName = fromPSOtoKotlinObject(res.getObject("airport_name") as PGobject).ru
                        cityName = fromPSOtoKotlinObject(res.getObject("city") as PGobject).ru }
                }
                list.add(
                    Airport(
                        res.getString("airport_code"),
                        airportName,
                        cityName,
                        res.getString("timezone")
                    )
                )
                if (pagination != null) {
                    cnt++
                    if (cnt >= pagination) {
                        break
                    }
                }
            }
            callback(convertList(list))
            connection!!.endRequest()
        }
    }

    suspend fun getCities(language : Language, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("Select city from airports_data group by city")
            val res = st.executeQuery()
            val list = mutableListOf<String>()
            while(res.next()) {
                val cityName = when(language) {
                    Language.EN -> fromPSOtoKotlinObject(res.getObject("city") as PGobject).en
                    Language.RU -> fromPSOtoKotlinObject(res.getObject("city") as PGobject).ru
                }
                list.add(cityName)
            }
            callback(convertList(list))
            connection!!.endRequest()
        }
    }

    suspend fun getAirportsInCity(language : Language, city : String, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("Select * from airports_data")
            val res = st.executeQuery()
            val list = mutableListOf<Airport>()
            while(res.next()) {
                val ci = fromPSOtoKotlinObject(res.getObject("city") as PGobject)
                if (ci.en == city || ci.ru == city) {
                    val airportName = when(language) {
                        Language.EN -> fromPSOtoKotlinObject(res.getObject("airport_name") as PGobject).en
                        Language.RU -> fromPSOtoKotlinObject(res.getObject("airport_name") as PGobject).ru
                    }
                    val cityName = when(language) {
                        Language.EN -> ci.en
                        Language.RU -> ci.ru
                    }
                    list.add(
                        Airport(
                            res.getString("airport_code"),
                            airportName,
                            cityName, res.getString("timezone")
                        )
                    )
                }
            }
            callback(convertList(list))
            connection!!.endRequest()
        }
    }

    suspend fun getInboundSchedules(language : Language, airport : String, page: Int?, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("Select city, flight_no, extract(isodow from scheduled_arrival) day_of_week, scheduled_arrival, departure_airport, arrival_airport from flights " +
                    "join airports_data on airports_data.airport_code = departure_airport where arrival_airport = '$airport'",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE)
            val res = st.executeQuery()
            val list = mutableListOf<InSchedule>()
            if (page != null) {
                res.relative(page * 10)
            }
            var cnt = 0
            while(res.next()) {
                val city = when(language) {
                    Language.EN -> fromPSOtoKotlinObject(res.getObject("city") as PGobject).en
                    Language.RU -> fromPSOtoKotlinObject(res.getObject("city") as PGobject).ru
                }
                val origin = res.getString("departure_airport") + ", " + city
                list.add(
                    InSchedule(
                        res.getString("flight_no"),
                        origin,
                        res.getString("day_of_week"),
                        res.getTimestamp("scheduled_arrival")
                    )
                )
                cnt++
                if (cnt >= 10) {
                    break
                }
            }
            callback(convertList(list))
            connection!!.endRequest()
        }
    }

    suspend fun getOutboundSchedules(language : Language, airport : String, page: Int?, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("Select city, flight_no, extract(isodow from scheduled_departure) day_of_week, scheduled_departure, departure_airport, arrival_airport from flights " +
                    "join airports_data on airports_data.airport_code = arrival_airport where departure_airport = '$airport'",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE)
            val res = st.executeQuery()
            val list = mutableListOf<OutSchedule>()
            if (page != null) {
                res.relative(page * 10)
            }
            var cnt = 0
            while(res.next()) {
                val city = when(language) {
                    Language.EN -> fromPSOtoKotlinObject(res.getObject("city") as PGobject).en
                    Language.RU -> fromPSOtoKotlinObject(res.getObject("city") as PGobject).ru
                }
                val destination = res.getString("arrival_airport") + ", " + city
                list.add(
                    OutSchedule(
                        res.getString("flight_no"),
                        destination,
                        res.getString("day_of_week"),
                        res.getTimestamp("scheduled_departure")
                    )
                )
                cnt++
                if (cnt >= 10) {
                    break
                }
            }
            callback(convertList(list))
            connection!!.endRequest()
        }
    }

    suspend fun book(booking : Booking, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            val flightsInfo = mutableListOf<FlightInfo>()

            for (flight in booking.flights) {
                connection!!.beginRequest()
                val st1 = connection!!.prepareStatement("select count(flight_id) from flights f join seats s on s.aircraft_code = f.aircraft_code where flight_id = ${flight.id}")
                val st2 = connection!!.prepareStatement("select count(flight_id) from boarding_passes bp where flight_id = ${flight.id}")
                val r1 = st1.executeQuery().getInt("count")
                val r2 = st2.executeQuery().getInt("count")
                if (r1 - r2 <= 0) {
                    callback("NP")
                    return@withContext
                }
                val st = connection!!.prepareStatement("select flights.flight_id, scheduled_departure, avg(amount) price from flights " +
                        "join ticket_flights on flights.flight_id = ticket_flights.flight_id where flights.flight_id = ${flight.id} and fare_conditions = '${flight.clazz}'" +
                        " group by flights.flight_id")
                val res = st.executeQuery()
                res.next()
                flightsInfo.add(FlightInfo(res.getString("flight_id"), res.getInt("price"), res.getTimestamp("scheduled_departure"), flight.clazz))
                connection!!.endRequest()
            }
            var totalPrice = 0
            flightsInfo.forEach { i -> totalPrice += i.amount }
            val bookRef1 = bookRefGenerator()
            val date1 = flightsInfo[0].time
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("insert into bookings (book_ref, book_date, total_amount) " +
                    "values ('$bookRef1', '$date1', $totalPrice)")
            st.execute()
            connection!!.endRequest()
            val tickets = mutableListOf<String>()
            for (flightInfo in flightsInfo) {
                val ticketNo = ticketRefGenerator()
                tickets.add(ticketNo)
                connection!!.beginRequest()
                val st1 = connection!!.prepareStatement("insert into tickets (ticket_no, book_ref, passenger_id, passenger_name, contact_data) " +
                    "values ('$ticketNo', '$bookRef1', '$bookRef1', '${booking.user.name}', '${gson.toJson(booking.user.contactData)}' )")
                val st2 = connection!!.prepareStatement("insert into ticket_flights (ticket_no, flight_id, fare_conditions, amount) " +
                        "values ('$ticketNo', ${flightInfo.id}, '${flightInfo.clazz}', ${flightInfo.amount} )")
                st1.execute()
                st2.execute()
                connection!!.endRequest()
            }
            val response = BookResponse(bookRef1.toString(), tickets)
            callback(gson.toJson(response))
        }
    }

    suspend fun checkIn(checkIn: CheckIn, callback : (String) -> Unit) {
        withContext(Dispatchers.IO) {
            val passes = mutableListOf<BoardingPassWithNumber>()
            val tickets = mutableListOf<String>()
            connection!!.beginRequest()
            val st = connection!!.prepareStatement("select ticket_no, book_ref from tickets where book_ref = '${checkIn.bookRef}'")
            val res = st.executeQuery()
            while (res.next()) {
                tickets.add(res.getString("ticket_no"))
            }
            val seatsAll = mutableMapOf<String, BoardingPass>()
            for (ticket in tickets) {
                val st1 = connection!!.prepareStatement("select flight_id, flight_no, scheduled_departure, departure_airport, arrival_airport, seat_no from " +
                        "(select tf.flight_id, flight_no, scheduled_departure, fare_conditions, departure_airport, arrival_airport, aircraft_code " +
                        "from ticket_flights tf join flights f on tf.flight_id = f.flight_id  where tf.ticket_no = '$ticket' limit 1) t1 " +
                        "join seats s on s.aircraft_code = t1.aircraft_code where t1.fare_conditions = s.fare_conditions ")
                val res1 = st1.executeQuery()
                while(res1.next()) {
                    seatsAll[res1.getString("seat_no")] = BoardingPass(res1.getString("flight_id"), res1.getString("flight_no"), res1.getString("seat_no"), res1.getString("departure_airport"), res1.getString("arrival_airport"), res1.getTimestamp("scheduled_departure"))
                }
                val st2 = connection!!.prepareStatement("select seat_no from " +
                        "(select flight_id from ticket_flights tf where tf.ticket_no = '$ticket' limit 1) t1 " +
                        "join boarding_passes bp on t1.flight_id = bp.flight_id")
                val res2 = st2.executeQuery()
                var cnt = 1
                while (res2.next()) {
                    seatsAll.remove(res2.getString("seat_no"))
                    cnt++
                }
                val pss = seatsAll.values.first()
                passes.add(BoardingPassWithNumber(pss.flightId, pss.flightNumber, ticket, cnt, pss.seatPlace, pss.origin, pss.destination, pss.departureTime))
            }
            val result = mutableListOf<BoardingPassResponse>()
            for (pass in passes) {
                val st1 = connection!!.prepareStatement("insert into boarding_passes (ticket_no, flight_id, boarding_no, seat_no) " +
                        "values ('${pass.ticketNumber}', ${pass.flightId}, ${pass.passNumber}, '${pass.seatPlace}')")
                st1.execute()
                result.add(BoardingPassResponse(pass.flightNumber, pass.seatPlace, pass.origin, pass.destination, pass.departureTime))
            }
            callback(convertList(result))
        }
    }

}