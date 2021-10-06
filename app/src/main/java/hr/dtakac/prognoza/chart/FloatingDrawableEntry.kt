package hr.dtakac.prognoza.chart

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.github.mikephil.charting.data.Entry

class FloatingDrawableEntry @JvmOverloads constructor(
    @DrawableRes val floatingDrawableRes: Int,
    x: Float,
    y: Float,
    icon: Drawable? = null,
    data: Any? = null
) : Entry(x, y, icon, data)