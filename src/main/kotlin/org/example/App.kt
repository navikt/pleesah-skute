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
        val loss = System.getenv("HAR_KASTET_LOSS")
        if (loss == "true") {
            log.info("Hurra! Du har kastet loss og skuta er klar for å plyndre")
            context.call.respondText(
                status = HttpStatusCode.OK,
                text = "Hurra! Du har kastet loss og skuta er klar for å plyndre"
            )
        } else {
            log.info(
                "Skuta er fortsatt fortøyd til havna og er ikke klar til å seile til sjøs. Du må kaste loss slik at skuta er klar for å seile. Kubernetes bruker en readiness probe for å sjekke om poden er klar for å ta imot trafikk. I denne oppgaven har vi gjort det slik at skuta krever at en miljøvariabel HAR_KASTET_LOSS er satt til 'true'. I den virkelige verden vil det være opp til hver enkelt å bestemme hvordan gi beskjed til kubernetes om at poden er klar."
            )
            context.call.respondText(
                status = HttpStatusCode.NotImplemented,
                text = "Skuta er fortsatt fortøyd til havna og er ikke klar til å seile til sjøs. Du må kaste loss slik at skuta er klar for å seile. Kubernetes bruker en readiness probe for å sjekke om poden er klar for å ta imot trafikk. I denne oppgaven har vi gjort det slik at skuta krever at en miljøvariabel HAR_KASTET_LOSS er satt til 'true'. I den virkelige verden vil det være opp til hver enkelt å bestemme hvordan gi beskjed til kubernetes om at poden er klar."
            )
        }
    }
}

fun sjekkAlivenessProbe(context: RoutingContext) {
    runBlocking {
        if (System.getenv("HAR_KASTET_LOSS") != "true") {
            context.call.respondText("OK")
        }

        val seil = System.getenv("HAR_SATT_SEIL")
        if (seil == "true") {
            log.info("Seilet er satt")
            context.call.respondText(status = HttpStatusCode.OK, text = "Seilet er satt")
        } else {
            log.info("HAR_SATT_SEIL secreten er ikke satt til true")
            context.call.respondText(
                status = HttpStatusCode(510, description = "Not extended"),
                text = "HAR_SATT_SEIL secreten er ikke satt til true"
            )

        }
    }
}

