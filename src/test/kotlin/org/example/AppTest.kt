package org.example

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun `isAlive returns 200 OK when HAR_KASTET_LOSS er true`() = testApplication {
        application {
            routing {
                get("/isAlive") {
                    sjekkLivenessProbe(this, harKastetLoss = "true")
                }
            }
        }

        val response = client.get("/isAlive")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `isAlive returns 501 Not Implemented when HAR_KASTET_LOSS er ikke satt`() = testApplication {
        application {
            routing {
                get("/isAlive") {
                    sjekkLivenessProbe(this, harKastetLoss = null)
                }
            }
        }

        val response = client.get("/isAlive")

        assertEquals(HttpStatusCode.NotImplemented, response.status)
    }

    @Test
    fun `isAlive returns 501 Not Implemented when HAR_KASTET_LOSS har feil verdi`() = testApplication {
        application {
            routing {
                get("/isAlive") {
                    sjekkLivenessProbe(this, harKastetLoss = "nope")
                }
            }
        }

        val response = client.get("/isAlive")

        assertEquals(HttpStatusCode.NotImplemented, response.status)
    }

    @Test
    fun `isReady returns 200 OK when havnesjef svarer med suksess`() = testApplication {
        val mockClient = mockHttpClient { respond("[]", HttpStatusCode.OK) }

        application {
            routing {
                get("/isReady") {
                    sjekkReadinessProbe(this, baseUrl = Url("http://havnesjef.test/teams"), client = mockClient)
                }
            }
        }

        val response = client.get("/isReady")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `isReady returns 503 Service Unavailable when havnesjef svarer med feil`() = testApplication {
        val mockClient = mockHttpClient { respond("feil", HttpStatusCode.InternalServerError) }

        application {
            routing {
                get("/isReady") {
                    sjekkReadinessProbe(this, baseUrl = Url("http://havnesjef.test/teams"), client = mockClient)
                }
            }
        }

        val response = client.get("/isReady")

        assertEquals(HttpStatusCode.ServiceUnavailable, response.status)
    }

    private fun mockHttpClient(handler: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) =
        HttpClient(MockEngine) {
            engine {
                addHandler(handler)
            }
        }
}
