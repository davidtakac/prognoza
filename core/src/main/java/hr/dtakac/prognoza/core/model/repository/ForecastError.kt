package hr.dtakac.prognoza.core.model.repository

sealed class ForecastError(val reason: Exception?)

class ThrottlingForecastError(reason: Exception? = null) : ForecastError(reason)
class ClientForecastError(reason: Exception? = null) : ForecastError(reason)
class ServerForecastError(reason: Exception? = null) : ForecastError(reason)
class IoForecastError(reason: Exception? = null) : ForecastError(reason)
class DatabaseForecastError(reason: Exception? = null) : ForecastError(reason)
class UnknownForecastError(reason: Exception? = null) : ForecastError(reason)
object NoSelectedPlaceForecastError : ForecastError(null)