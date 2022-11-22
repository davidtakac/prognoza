package hr.dtakac.prognoza.shared.platform

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import hr.dtakac.prognoza.shared.data.PrognozaDatabase

internal actual class SqlDriverFactory {
    actual fun create(): SqlDriver = NativeSqliteDriver(
        schema = PrognozaDatabase.Schema,
        name = "prognoza.db"
    )
}