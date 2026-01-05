package org.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, 8080) {
        routing {
            get("/isAlive") {
                call.respondText("OK")
            }
            get("/isReady") {
                call.respondText("OK")
            }
        }
    }.start(wait = true)
}
