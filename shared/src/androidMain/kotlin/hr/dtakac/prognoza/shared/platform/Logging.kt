package hr.dtakac.prognoza.shared.platform

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

actual fun initLogging() {
    Napier.base(DebugAntilog())
}