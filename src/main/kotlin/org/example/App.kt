package org.example

import io.ktor.http.*
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
                sjekkAlivenessProbe(this)
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
            context.call.respondText(status = HttpStatusCode.OK, text = "Ankeret er hevet")
        } else {
            log.info("HEV_ANKER er ikke satt til true")
            context.call.respondText(
                status = HttpStatusCode.NotImplemented,
                text = "HEV_ANKER er ikke satt til true"
            )
        }
    }
}

fun sjekkAlivenessProbe(context: RoutingContext) {
    runBlocking {
        val seil = System.getenv("SETT_SEIL")
        if (seil == "true") {
            log.info("Seilet er satt")
            context.call.respondText(status = HttpStatusCode.OK, text = "Seilet er hevet")
        } else {
            log.info("SETT_SEIL secreten er ikke satt til true")
            context.call.respondText(
                status = HttpStatusCode(510, description = "Not extended"),
                text = "SETT_SEIL secreten er ikke satt til true"
            )
        }
    }
}

