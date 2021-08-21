package hr.dtakac.prognoza.repomodel

sealed class ForecastError(val reason: Exception?): ForecastResult

class ThrottlingError(reason: Exception? = null) : ForecastError(reason)
class ClientError(reason: Exception? = null) : ForecastError(reason)
class ServerError(reason: Exception? = null) : ForecastError(reason)
class IOError(reason: Exception? = null) : ForecastError(reason)
class DatabaseError(reason: Exception? = null) : ForecastError(reason)
class UnknownError(reason: Exception? = null) : ForecastError(reason)