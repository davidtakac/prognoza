package hr.dtakac.prognoza.core.utils

import android.os.Handler
import android.os.Looper
import android.os.SystemClock

// From https://github.com/chrisbanes/tivi/blob/96e7cae7560ffd358b8c58c47267ed1024df53f6/app/src/main/java/me/banes/chris/tivi/ui/ProgressTimeLatch.kt
/**
 * A class which acts as a time latch for show progress bars UIs. It waits a minimum time to be
 * dismissed before showing. Once visible, the progress bar will be visible for
 * a minimum amount of time to avoid "flashes" in the UI when an event could take
 * a largely variable time to complete (from none, to a user perceivable amount).
 *
 * Works with an view through the lambda API.
 */
class ProgressTimeLatch(
    private val delayMs: Long = 750,
    private val minShowTime: Long = 500,
    private val viewRefreshingToggle: ((Boolean) -> Unit)
) {
    private val handler = Handler(Looper.getMainLooper())
    private var showTime = 0L

    private val delayedShow = Runnable(this::show)
    private val delayedHide = Runnable(this::hideAndReset)

    var loading = false
        set(value) {
            if (field != value) {
                field = value
                handler.removeCallbacks(delayedShow)
                handler.removeCallbacks(delayedHide)

                if (value) {
                    handler.postDelayed(delayedShow, delayMs)
                } else if (showTime >= 0) {
                    // We're already showing, lets check if we need to delay the hide
                    val showTime = SystemClock.uptimeMillis() - showTime
                    if (showTime < minShowTime) {
                        handler.postDelayed(delayedHide, minShowTime - showTime)
                    } else {
                        // We've been showing longer than the min, so hide and clean up
                        hideAndReset()
                    }
                } else {
                    // We're not currently show so just hide and clean up
                    hideAndReset()
                }
            }
        }

    private fun show() {
        viewRefreshingToggle(true)
        showTime = SystemClock.uptimeMillis()
    }

    private fun hideAndReset() {
        viewRefreshingToggle(false)
        showTime = 0
    }
}