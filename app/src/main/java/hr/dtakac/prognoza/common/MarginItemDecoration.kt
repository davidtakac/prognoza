package hr.dtakac.prognoza.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MarginItemDecoration : RecyclerView.ItemDecoration() {
    private val eightDp = 8.toPx.roundToInt()
    private val sixteenDp = 16.toPx.roundToInt()
    private val verticalOffset = eightDp
    private val horizontalOffset = sixteenDp

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalOffset
        outRect.bottom = verticalOffset
        outRect.left = horizontalOffset
        outRect.right = horizontalOffset
    }
}