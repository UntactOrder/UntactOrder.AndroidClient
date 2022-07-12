package io.github.untactorder.network.https

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.network.tls.TLSConfigBuilder

class HttpsRequestHelper {


    private val client: HttpClient = HttpClient(CIO) {
        engine {
            https {
                //trustManager = SslSettings.getTrustManager()
            }
        }
    }


    suspend fun test() {
        withContext(Dispatchers.IO) {
            val url = "https://cat-fact.herokuapp.com/facts/random"
            //val response: HttpResponse = client.get(url)
        }
    }
}