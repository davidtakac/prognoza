package hr.dtakac.prognoza.shared.platform

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import hr.dtakac.prognoza.shared.data.PrognozaDatabase

internal actual class SqlDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver = AndroidSqliteDriver(
        schema = PrognozaDatabase.Schema,
        context = context,
        name = "prognoza.db"
    )
}