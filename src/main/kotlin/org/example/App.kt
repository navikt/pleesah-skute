package org.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    embeddedServer(Netty, 8080) {
        routing {
            get("/isAlive") {
                call.respondText("OK")
            }
            get("/isReady") {
                sjekkReadinessProbe(this)
            }
        }
    }.start(wait = true)
}

val log: Logger = LoggerFactory.getLogger("main")

fun sjekkReadinessProbe(context: RoutingContext) {
    runBlocking {
        val anker = System.getenv("HEV_ANKER")
        if (anker == "true") {
            log.info("Ankeret er hevet")
            context.call.respondText("Ankeret er hevet")
        } else {
            log.info("HEV_ANKER er ikke satt til true")
            context.call.respondText(status = HttpStatusCode.TooEarly, text = "HEV_ANKER er ikke satt til true")
        }
    }
}


