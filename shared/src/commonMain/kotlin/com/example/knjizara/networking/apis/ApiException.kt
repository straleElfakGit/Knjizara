package com.example.knjizara.networking.apis

import co.touchlab.kermit.Logger
import com.example.knjizara.networking.network_utils.ApiErrorDto
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

private suspend inline fun parseHttpError(response: HttpResponse): NetworkError.HttpError {
    val errorDto = try {
        response.body<ApiErrorDto>()
    } catch (_: Exception) {
        null
    }
    return NetworkError.HttpError(
        code = response.status.value,
        serverMessage = errorDto?.detail ?: errorDto?.title,
        errorCode = errorDto?.status?.toString(),
        errors = errorDto?.errors
    )
}

suspend fun <T> safeApiCall(execute: suspend () -> T): Result<T, NetworkError> {
    return try {
        Result.Success(execute())
    } catch (e: ClientRequestException) {
        Logger.d("ClientRequestException: ${e.response.status.value}")
        Result.Error(parseHttpError(e.response))
    } catch (e: ServerResponseException) {
        Logger.d("ServerResponseException ${e.response.status.value}")
        Result.Error(parseHttpError(e.response))
    } catch (e: SerializationException) {
        Logger.d("SerializationException")
        Result.Error(NetworkError.Serialization)
    } catch (e: IOException) {
        Logger.d("IOException")
        Result.Error(NetworkError.NoInternet)
    } catch (e: CancellationException) {
        Logger.d("CancellationException")
        throw e
    } catch (e: Exception) {
        Logger.d("Exception, $e")
        Result.Error(NetworkError.Unknown)
    }
}