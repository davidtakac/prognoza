package hr.dtakac.prognoza.core.model.repository

sealed class ForecastError(val reason: Exception?) {
    class Throttling(reason: Exception?) : ForecastError(reason)
    class Client(reason: Exception?) : ForecastError(reason)
    class Server(reason: Exception?) : ForecastError(reason)
    class Io(reason: Exception?) : ForecastError(reason)
    class Database(reason: Exception?) : ForecastError(reason)
    class Unknown(reason: Exception?) : ForecastError(reason)
    object NoSelectedPlace : ForecastError(null)
}