package app

import app.repository.DataCoordinator
import app.repository.data.Booking
import app.repository.data.CheckIn
import app.repository.data.Language
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import java.util.*


fun main() {
    embeddedServer(Netty, port = 8080) {
        val gson = Gson()
        this.launch(this.coroutineContext) {
            DataCoordinator.shared.init()
        }
        routing {
            get("locations/airports") {
                println("Try to get airports")
                var language = Language.EN
                if (call.parameters["language"]?.uppercase(Locale.getDefault()) == "RU") {
                    language = Language.RU
                }
                DataCoordinator.shared.getAirports(language, call.parameters["pagination"]?.toInt(),
                    call.parameters["page"]?.toInt()) { str : String  ->
                    this.launch(this.coroutineContext) {
                        call.respond(status = HttpStatusCode.OK, str)
                    }
                }
            }
            get("locations/cities") {
                println("Try to get cities")
                var language = Language.EN
                if (call.parameters["language"]?.uppercase(Locale.getDefault()) == "RU") {
                    language = Language.RU
                }
                DataCoordinator.shared.getCities(language) { str : String  ->
                    this.launch(this.coroutineContext) {
                        call.respond(status = HttpStatusCode.OK, str)
                    }
                }
            }
            get("locations/airports/{city}") {
                println("Try to get airports in a city")
                var language = Language.EN
                if (call.parameters["language"]?.uppercase(Locale.getDefault()) == "RU") {
                    language = Language.RU
                }
                if (call.parameters.contains("city")) {
                    DataCoordinator.shared.getAirportsInCity(language, call.parameters["city"]!!) { str : String  ->
                        this.launch(this.coroutineContext) {
                            call.respond(status = HttpStatusCode.OK, str)
                        }
                    }
                }
                else {
                    call.respond(status = HttpStatusCode.BadRequest, "Bad parametrs")
                }
            }
            get("schedules/inbound/{airport}") {
                println("Try to get inbound schedules")
                var language = Language.EN
                if (call.parameters["language"]?.uppercase(Locale.getDefault()) == "RU") {
                    language = Language.RU
                }
                if (call.parameters.contains("airport")) {
                    DataCoordinator.shared.getInboundSchedules(language, call.parameters["airport"]!!, call.parameters["page"]?.toInt()) { str : String  ->
                        this.launch(this.coroutineContext) {
                            call.respond(status = HttpStatusCode.OK, str)
                        }
                    }
                }
                else {
                    call.respond(status = HttpStatusCode.BadRequest, "Bad parametrs")
                }
            }
            get("schedules/outbound/{airport}") {
                println("Try to get outbound schedules")
                var language = Language.EN
                if (call.parameters["language"]?.uppercase(Locale.getDefault()) == "RU") {
                    language = Language.RU
                }
                if (call.parameters.contains("airport")) {
                    DataCoordinator.shared.getOutboundSchedules(language, call.parameters["airport"]!!, call.parameters["page"]?.toInt()) { str : String  ->
                        this.launch(this.coroutineContext) {
                            call.respond(status = HttpStatusCode.OK, str)
                        }
                    }
                }
                else {
                    call.respond(status = HttpStatusCode.BadRequest, "Bad parametrs")
                }
            }
            post("book") {
                println("Try to book")
                val body = gson.fromJson(call.receiveText(), Booking::class.java)
                DataCoordinator.shared.book(body) { str : String  ->
                    this.launch(this.coroutineContext) {
                        if (str == "NP") {
                            call.respond(status = HttpStatusCode.Conflict, "No free places :(")
                        }
                        else {
                            call.respond(status = HttpStatusCode.OK, str)
                        }
                    }
                }
            }
            post("check-in") {
                println("Try to check-in")
                val body = gson.fromJson(call.receiveText(), CheckIn::class.java)
                DataCoordinator.shared.checkIn(body) { str : String  ->
                    this.launch(this.coroutineContext) {

                        call.respond(status = HttpStatusCode.OK, str)

                    }
                }
            }
        }
    }.start(wait = true)
}
