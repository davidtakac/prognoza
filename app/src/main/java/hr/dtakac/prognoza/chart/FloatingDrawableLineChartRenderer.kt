package hr.dtakac.prognoza.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.SparseArray
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.renderer.LineChartRenderer
import kotlin.math.roundToInt

class FloatingDrawableLineChartRenderer(
    private val context: Context, lineChart: LineChart
) : LineChartRenderer(lineChart, lineChart.animator, lineChart.viewPortHandler) {

    private val circleCoordinates = FloatArray(2)
    private val drawablesCache = SparseArray<Drawable>()

    override fun drawExtras(c: Canvas) {
        super.drawExtras(c)
        mChart.lineData.dataSets.forEach { dataset ->
            val transformer = mChart.getTransformer(dataset.axisDependency)
            for (i in mXBounds.min..(mXBounds.min + mXBounds.range)) {
                val entry = dataset.getEntryForIndex(i) as? FloatingDrawableEntry ?: continue
                circleCoordinates[0] = entry.x
                circleCoordinates[1] = entry.y * mAnimator.phaseY

                transformer.pointValuesToPixel(circleCoordinates)

                if (!mViewPortHandler.isInBoundsRight(circleCoordinates[0])) break
                if (!mViewPortHandler.isInBoundsLeft(circleCoordinates[0]) || !mViewPortHandler.isInBoundsY(circleCoordinates[1])) continue

                val drawableDimen = dataset.circleRadius * 8
                getDrawable(entry.floatingDrawableRes)?.run {
                    val marginBottom = drawableDimen/4
                    val left = (circleCoordinates[0] - drawableDimen/2).roundToInt()
                    val right = (circleCoordinates[0] + drawableDimen/2).roundToInt()
                    val bottom = (circleCoordinates[1] - marginBottom).roundToInt()
                    val top = (circleCoordinates[1] - marginBottom - drawableDimen).roundToInt()
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