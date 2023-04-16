package hr.dtakac.prognoza.shared.platform

import platform.Foundation.NSUUID

internal actual class UuidProvider {
    actual fun randomUuid(): String = NSUUID().UUIDString()
}