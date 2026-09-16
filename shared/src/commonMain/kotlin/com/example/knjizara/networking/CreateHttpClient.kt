package com.example.knjizara.networking

import com.example.knjizara.features.auth.token_manager.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(
    engine: HttpClientEngine,
    tokenStorage: TokenStorage
): HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(Auth) {
            bearer {
                loadTokens {
                    tokenStorage.getToken()?.let { BearerTokens(it, "") }
                }
                sendWithoutRequest { request ->
                    val path = request.url.encodedPath
                            !path.endsWith("/auth/register") &&
                            !path.endsWith("/auth/login")
                }
            }
        }

        defaultRequest {
            url(NetworkConfig.BASE_URL)
            contentType(ContentType.Application.Json)
        }

        expectSuccess = true
    }
}