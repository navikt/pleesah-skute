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
            sjekkSecret()
            context.call.respond(HttpStatusCode.OK)
        } else {
            log.info("Oppgave 3: Skuta er fortsatt fortøyd til havna og er ikke klar til å seile til sjøs. Du må kaste loss slik at skuta er klar for å seile. Skuta krever at en miljøvariabel HAR_KASTET_LOSS er satt til 'true'. Gå tilbake til oppgavearket og følg instruksjonene.")
            context.call.respond(HttpStatusCode.NotImplemented)
        }
    }
}

fun sjekkSecret() {
    val hemmelighet = System.getenv("HAR_SATT_KURS")
    if (hemmelighet == "59.9124° N, 10.7962° E") {
        log.info("Kursen er satt og du er endelig på vei til din destinasjon!")
    } else {
        log.info("Oppgave 4: Hurra! Du har kastet loss og er klar til å plyndre! Men hvor skal vi, egentlig? Koordinatene finner du i en hemmelighet! I K8s kan hemmeligheter lagres i ressurstypen secrets. Disse kan inneholde forskjellig typer data, men i dette tilfellet finnes det kun én nøkkel skuta trenger for å sette kurs mot riktig destinasjon. Gå tilbake til oppgavearket og følg instruksjonene. Skip o’hoi!")
    }
}


