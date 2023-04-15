package hr.dtakac.prognoza.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object ForecastWidgetStateDefinition : GlanceStateDefinition<ForecastWidgetState> {
    private const val DATA_STORE_FILE_NAME = "forecastWidgetState"
    private val Context.datastore by dataStore(DATA_STORE_FILE_NAME, ForecastWidgetStateSerializer)

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<ForecastWidgetState> = context.datastore

    override fun getLocation(
        context: Context,
        fileKey: String
    ): File = context.dataStoreFile(DATA_STORE_FILE_NAME)

    object ForecastWidgetStateSerializer : Serializer<ForecastWidgetState> {
        override val defaultValue: ForecastWidgetState = ForecastWidgetState.Unavailable

        override suspend fun readFrom(input: InputStream): ForecastWidgetState = try {
            Json.decodeFromString(
                ForecastWidgetState.serializer(),
                input.readInts().decodeToString()
            )
        } catch (e: Exception) {
            Napier.e(message = "Widget error", e)
            ForecastWidgetState.Error
        }

        override suspend fun writeTo(t: ForecastWidgetState, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(
                        ForecastWidgetState.serializer(),
                        t
                    ).encodeToIntArray()
                )
            }
        }
    }
}