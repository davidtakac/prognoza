package hr.dtakac.prognoza.shared.platform

import java.util.UUID

internal actual class UuidProvider {
    actual fun randomUuid(): String = UUID.randomUUID().toString()
}