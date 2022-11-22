package hr.dtakac.prognoza.shared.platform

import com.squareup.sqldelight.db.SqlDriver

internal expect class SqlDriverFactory {
    fun create(): SqlDriver
}