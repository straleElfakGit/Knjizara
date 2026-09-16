package com.example.knjizara.networking.apis

import co.touchlab.kermit.Logger
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend fun <T> safeApiCall(execute: suspend () -> T): Result<T, NetworkError> {
    return try {
        Result.Success(execute())
    } catch (e: ClientRequestException) {
        Logger.d("1 ${e.response.status.value}")
        Result.Error(NetworkError.HttpError(e.response.status.value))
    } catch (e: ServerResponseException) {
        Logger.d("2 ${e.response.status.value}")
        Result.Error(NetworkError.HttpError(e.response.status.value))
    } catch (e: SerializationException) {
        Logger.d("3")
        Result.Error(NetworkError.Serialization)
    } catch (e: IOException) {
        Logger.d("4")
        Result.Error(NetworkError.NoInternet)
    } catch (e: CancellationException) {
        Logger.d("5")
        throw e
    } catch (e: Exception) {
        Logger.d("6, $e")
        Result.Error(NetworkError.Unknown)
    }
}