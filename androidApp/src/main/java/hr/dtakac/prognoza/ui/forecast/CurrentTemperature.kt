package hr.dtakac.prognoza.ui.forecast

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect as ViewRect
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import hr.dtakac.prognoza.R
import java.lang.Integer.min
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun CurrentTemperature(
    temperature: String,
    textColor: ComposeColor = ComposeColor.Black,
    letterSpacingFraction: Float = 0f,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val textPaint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = context.resources.getFont(R.font.manrope_bold)
            letterSpacing = letterSpacingFraction
        }
    }

    val measurePolicy = remember {
        MeasurePolicy { measurables, constraints ->
            val measurable = measurables[0]
            val placeable = measurable.measure(constraints)
            val width = min(constraints.maxWidth, placeable.width)
            val height = min(constraints.maxHeight, placeable.height)
            layout(width, height) { placeable.place(0, 0) }
        }
    }

    SubcomposeLayout(modifier = modifier) { constraints ->
        val bounds = ViewRect()
        val textSize = constraints.maxHeight.toFloat()
        val step = textSize * .1f
        textPaint.textSize = constraints.maxHeight.toFloat()
        textPaint.color = textColor.toArgb()
        textPaint.getTextBounds(temperature, 0, temperature.length, bounds)
        while (bounds.right > constraints.maxWidth || bounds.height() > constraints.maxHeight) {
            textPaint.textSize -= step
            textPaint.getTextBounds(temperature, 0, temperature.length, bounds)
        }

        val measurables = subcompose(Unit) {
            val canvasModifier = with(LocalDensity.current) {
                Modifier
                    .width(bounds.right.toDp())
                    .height(bounds.height().toDp())
            }
            ComposeCanvas(canvasModifier) {
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        temperature,
                        0f,
                        bounds.height().toFloat(),
                        textPaint
                    )
                }
            }
        }

        with(measurePolicy) { measure(measurables, constraints) }
    }
}

// Initial version that helped me figure out what I wanted to do in Compose
private class CurrentTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var text: String = ""
    private var color: Int = Color.BLACK

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = context.resources.getFont(R.font.manrope_bold)
        letterSpacing = -0.05f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(text, 0f, measuredHeight.toFloat(), textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)

        val bounds = ViewRect()
        textPaint.textSize = maxHeight.toFloat()
        textPaint.color = color
        textPaint.getTextBounds(text, 0, text.length, bounds)
        while (bounds.right > maxWidth || bounds.height() > maxHeight) {
            textPaint.textSize *= 0.9f
            textPaint.getTextBounds(text, 0, text.length, bounds)
        }

        val resolvedWidth = resolveSizeAndState(bounds.right, widthMeasureSpec, 1)
        val resolvedHeight = resolveSizeAndState(bounds.height(), heightMeasureSpec, 1)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    fun update(
        temperature: String,
        color: Int
    ) {
        this.text = temperature
        this.color = color
        requestLayout()
    }
}