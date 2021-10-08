package hr.dtakac.prognoza.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.SparseArray
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.renderer.LineChartRenderer
import hr.dtakac.prognoza.utils.toPx
import kotlin.math.roundToInt

class FloatingDrawableLineChartRenderer(
    private val context: Context, lineChart: LineChart
) : LineChartRenderer(lineChart, lineChart.animator, lineChart.viewPortHandler) {

    private val circleCoordinates = FloatArray(2)
    private val drawablesCache = SparseArray<Drawable>()
    private val drawableDimen = 32.toPx
    private val drawableMarginBottom = 8.toPx

    override fun drawExtras(c: Canvas) {
        super.drawExtras(c)
        mChart.lineData.dataSets.forEach { dataset ->
            val transformer = mChart.getTransformer(dataset.axisDependency)
            val endOfXBounds = (mXBounds.min + mXBounds.range)
            for (i in mXBounds.min..endOfXBounds) {
                val entry = dataset.getEntryForIndex(i) as? FloatingDrawableEntry ?: continue
                if (i == 0 || i == endOfXBounds || i % 2 == 0) continue

                circleCoordinates[0] = entry.x
                circleCoordinates[1] = entry.y * mAnimator.phaseY

                transformer.pointValuesToPixel(circleCoordinates)

                if (!mViewPortHandler.isInBoundsRight(circleCoordinates[0])) break
                if (!mViewPortHandler.isInBoundsLeft(circleCoordinates[0]) || !mViewPortHandler.isInBoundsY(circleCoordinates[1])) continue

                getDrawable(entry.floatingDrawableRes)?.run {
                    val left = (circleCoordinates[0] - drawableDimen/2).roundToInt()
                    val right = (circleCoordinates[0] + drawableDimen/2).roundToInt()
                    val bottom = (circleCoordinates[1] - drawableMarginBottom).roundToInt()
                    val top = (circleCoordinates[1] - drawableMarginBottom - drawableDimen).roundToInt()
                    setBounds(left, top, right, bottom)
                    draw(c)
                }
            }
        }
    }

    private fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        drawablesCache[drawableRes]?.let {
            return it
        }
        return ResourcesCompat.getDrawable(context.resources, drawableRes, null)
            .also { drawablesCache.append(drawableRes, it) }
    }
}