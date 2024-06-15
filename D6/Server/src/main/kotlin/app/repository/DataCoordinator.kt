package app.repository

import app.repository.data.Airport
import app.repository.data.Language
import app.repository.data.Name
import app.repository.data.Schedule
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.postgresql.Driver
import org.postgresql.util.PGobject
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class DataCoordinator {

    companion object {
        val shared = DataCoordinator()
    }

    private val driver = Driver()
    private val gson = Gson()
    private var connection : Connection? = null

    suspend fun init() {
        withContext(Dispatchers.IO) {
            val info = Properties()
            info.setProperty("user", "postgres")
            info.setProperty("password", "re6orn")
            info.setProperty("logLevel", "DEBUG")
            connection = driver.connect("jdbc:postgresql://localhost:5433/demo", info)
            println("Connection opened: " + !connection!!.isClosed)
        }
    }

    private fun convertList(obj : Collection<Any>) : String {
        return gson.toJson(obj)
    }

    private fun fromPSOtoKotlinObject(obj : PGobject) : Name {
        return gson.fromJson(obj.value!!, Name::class.java)
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
            val st = connection!!.prepareStatement("Select flight_no, extract(isodow from scheduled_departure) day_of_week, scheduled_arrival, departure_airport, arrival_airport from flights where arrival_airport = '$airport'",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE)
            val res = st.executeQuery()
            val list = mutableListOf<Schedule>()
            if (page != null) {
                res.relative(page * 10)
            }
            var cnt = 0
            while(res.next()) {
//                val originOrDestination = when(language) {
//                    Language.EN -> fromPSOtoKotlinObject(res.getObject("airport_name") as PGobject).en
//                    Language.RU -> fromPSOtoKotlinObject(res.getObject("airport_name") as PGobject).ru
//                }
                val originOrDestination = res.getString("departure_airport")
                list.add(
                    Schedule(
                        res.getString("flight_no"),
                        originOrDestination,
                        res.getString("day_of_week"),
                        res.getTime("scheduled_arrival")
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



}