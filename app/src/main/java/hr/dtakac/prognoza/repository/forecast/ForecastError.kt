package hr.dtakac.prognoza.repository.forecast

sealed class ForecastError(val reason: Exception?): ForecastResult

class Throttling(reason: Exception? = null) : ForecastError(reason)
class ClientSide(reason: Exception? = null) : ForecastError(reason)
class ServerSide(reason: Exception? = null) : ForecastError(reason)
class DatabaseError(reason: Exception? = null) : ForecastError(reason)
class Unknown(reason: Exception? = null) : ForecastError(reason)