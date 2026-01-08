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
                call.respond(HttpStatusCode.OK)
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
            log.info("Hurra! Du har kastet loss og er klar til å plyndre!")
            sjekkSecret()
            context.call.respond(HttpStatusCode.OK)
        } else {
            log.info("Oppgave 3: Skuta er fortsatt fortøyd til havna og er ikke klar til å seile til sjøs. Du må kaste loss slik at skuta er klar for å seile. Kubernetes bruker en readiness probe for å sjekke om poden er klar for å ta imot trafikk. I denne oppgaven har vi gjort det slik at skuta krever at en miljøvariabel HAR_KASTET_LOSS er satt til 'true'. I den virkelige verden vil det være opp til hver enkelt å bestemme hvordan gi beskjed til kubernetes om at poden er klar.")
            context.call.respond(HttpStatusCode.NotImplemented)
        }
    }
}

fun sjekkSecret() {
    val hemmelighet = System.getenv("HAR_SATT_KURS")
    if (hemmelighet == "59.9124° N, 10.7962° E") {
        log.info("Kursen er satt og du er endelig på vei til din destinasjon!")
    } else {
        log.info("Oppgave 4: Men hvor skal vi, egentlig? Koordinatene finner du i en hemmelighet! I K8s kan hemmeligheter lagres i ressurstypen secrets. Disse kan inneholde forskjellig typer data, men i vår instans finnes det kun én nøkkel skuta trenger for å sette kurs mot riktig desinasjon")
    }
}


