package hr.dtakac.prognoza.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import hr.dtakac.prognoza.entities.forecast.units.Temperature

private val white = Color(0xFFFFFFFF)
private val black = Color(0xFF000000)

// From https://www.esri.com/arcgis-blog/products/arcgis-pro/mapping/a-meaningful-temperature-palette/
// 'on' colors determined with https://material.io/resources/color
val extreme_cold_1 = Color(0xFFE4EFFE)
val on_extreme_cold_1 = black

val extreme_cold_2 = Color(0xFFDCE9FA)
val on_extreme_cold_2 = black

val extreme_cold_3 = Color(0xFFD3E2F7)
val on_extreme_cold_3 = black

val extreme_cold_4 = Color(0xFFCBDBF3)
val on_extreme_cold_4 = black

val extreme_cold_5 = Color(0xFFC0D4ED)
val on_extreme_cold_5 = black

val below_freezing_1 = Color(0xFFB8CDE9)
val on_below_freezing_1 = black

val below_freezing_2 = Color(0xFFB0C6E6)
val on_below_freezing_2 = black

val below_freezing_3 = Color(0xFFA8BFE3)
val on_below_freezing_3 = black

val below_freezing_4 = Color(0xFF9CB8DE)
val on_below_freezing_4 = black

val below_freezing_5 = Color(0xFF94B1D6)
val on_below_freezing_5 = black

val below_freezing_6 = Color(0xFF89A5CD)
val on_below_freezing_6 = black

val below_freezing_7 = Color(0xFF7E9BC3)
val on_below_freezing_7 = black

val below_freezing_8 = Color(0xFF7691B9)
val on_below_freezing_8 = black

val freezing_1 = Color(0xFF617AA5)
val on_freezing_1 = black

val freezing_2 = Color(0xFF57709C)
val on_freezing_2 = white

val freezing_3 = Color(0xFF4C6591)
val on_freezing_3 = white

val freezing_4 = Color(0xFF415B87)
val on_freezing_4 = white

val freezing_5 = Color(0xFF39507E)
val on_freezing_5 = white

val freezing_6 = Color(0xFF2F4674)
val on_freezing_6 = white

val freezing_7 = Color(0xFF25426F)
val on_freezing_7 = white

val frigid_1 = Color(0xFF264F77)
val on_frigid_1 = white

val frigid_2 = Color(0xFF275B80)
val on_frigid_2 = white

val frigid_3 = Color(0xFF276789)
val on_frigid_3 = white

val frigid_4 = Color(0xFF277593)
val on_frigid_4 = white

val transitional_1 = Color(0xFF428190)
val on_transitional_1 = black

val transitional_2 = Color(0xFF658C89)
val on_transitional_2 = black

val transitional_3 = Color(0xFF869B84)
val on_transitional_3 = black

val mellow_1 = Color(0xFFAAA77D)
val on_mellow_1 = black

val mellow_2 = Color(0xFFC3AB75)
val on_mellow_2 = black

val mellow_3 = Color(0xFFC19C62)
val on_mellow_3 = black

val warming_1 = Color(0xFFC48953)
val on_warming_1 = black

val warming_2 = Color(0xFFBE704C)
val on_warming_2 = black

val warming_3 = Color(0xFFAF4C4D)
val on_warming_3 = white

val scorching_1 = Color(0xFF9F294C)
val on_scorching_1 = white

val scorching_2 = Color(0xFF87213E)
val on_scorching_2 = white

val scorching_3 = Color(0xFF6E1531)
val on_scorching_3 = white

val scorching_4 = Color(0xFF560B25)
val on_scorching_4 = white

val scorching_5 = Color(0xFF3C0216)
val on_scorching_5 = white

@Immutable
data class PrognozaColors(
    val background: Color,
    val onBackground: Color
) {
    companion object {
        fun forTemperature(temperature: Temperature): PrognozaColors = when (temperature.fahrenheit) {
            in Double.MIN_VALUE..-60.0 -> PrognozaColors(
                background = extreme_cold_1,
                onBackground = on_extreme_cold_1
            )
            in -60.0..-55.0 -> PrognozaColors(
                background = extreme_cold_2,
                onBackground = on_extreme_cold_2
            )
            in -55.0..-50.0 -> PrognozaColors(
                background = extreme_cold_3,
                onBackground = on_extreme_cold_3
            )
            in -50.0..-45.0 -> PrognozaColors(
                background = extreme_cold_4,
                onBackground = on_extreme_cold_4
            )
            in -45.0..-40.0 -> PrognozaColors(
                background = extreme_cold_5,
                onBackground = on_extreme_cold_5
            )
            in -40.0..-35.0 -> PrognozaColors(
                background = below_freezing_1,
                onBackground = on_below_freezing_1
            )
            in -35.0..-30.0 -> PrognozaColors(
                background = below_freezing_2,
                onBackground = on_below_freezing_2
            )
            in -30.0..-25.0 -> PrognozaColors(
                background = below_freezing_3,
                onBackground = on_below_freezing_3
            )
            in -25.0..-20.0 -> PrognozaColors(
                background = below_freezing_4,
                onBackground = on_below_freezing_4
            )
            in -20.0..-15.0 -> PrognozaColors(
                background = below_freezing_5,
                onBackground = on_below_freezing_5
            )
            in -15.0..-10.0 -> PrognozaColors(
                background = below_freezing_6,
                onBackground = on_below_freezing_6
            )
            in -10.0..-5.0 -> PrognozaColors(
                background = below_freezing_7,
                onBackground = on_below_freezing_7
            )
            in -5.0..0.0 -> PrognozaColors(
                background = below_freezing_8,
                onBackground = on_below_freezing_8
            )
            in 0.0..5.0 -> PrognozaColors(
                background = freezing_1,
                onBackground = on_freezing_1
            )
            in 5.0..10.0 -> PrognozaColors(
                background = freezing_2,
                onBackground = on_freezing_2
            )
            in 10.0..15.0 -> PrognozaColors(
                background = freezing_3,
                onBackground = on_freezing_3
            )
            in 15.0..20.0 -> PrognozaColors(
                background = freezing_4,
                onBackground = on_freezing_4
            )
            in 20.0..25.0 -> PrognozaColors(
                background = freezing_5,
                onBackground = on_freezing_5
            )
            in 25.0..30.0 -> PrognozaColors(
                background = freezing_6,
                onBackground = on_freezing_6
            )
            in 30.0..35.0 -> PrognozaColors(
                background = freezing_7,
                onBackground = on_freezing_7
            )
            in 35.0..40.0 -> PrognozaColors(
                background = frigid_1,
                onBackground = on_frigid_1
            )
            in 40.0..45.0 -> PrognozaColors(
                background = frigid_2,
                onBackground = on_frigid_2
            )
            in 45.0..50.0 -> PrognozaColors(
                background = frigid_3,
                onBackground = on_frigid_3
            )
            in 50.0..55.0 -> PrognozaColors(
                background = frigid_4,
                onBackground = on_frigid_4
            )
            in 55.0..60.0 -> PrognozaColors(
                background = transitional_1,
                onBackground = on_transitional_1
            )
            in 60.0..65.0 -> PrognozaColors(
                background = transitional_2,
                onBackground = on_transitional_2
            )
            in 65.0..70.0 -> PrognozaColors(
                background = transitional_3,
                onBackground = on_transitional_3
            )
            in 70.0..75.0 -> PrognozaColors(
                background = mellow_1,
                onBackground = on_mellow_1
            )
            in 75.0..80.0 -> PrognozaColors(
                background = mellow_2,
                onBackground = on_mellow_2
            )
            in 80.0..85.0 -> PrognozaColors(
                background = mellow_3,
                onBackground = on_mellow_3
            )
            in 85.0..90.0 -> PrognozaColors(
                background = warming_1,
                onBackground = on_warming_1
            )
            in 90.0..95.0 -> PrognozaColors(
                background = warming_2,
                onBackground = on_warming_2
            )
            in 95.0..100.0 -> PrognozaColors(
                background = warming_3,
                onBackground = on_warming_3
            )
            in 100.0..105.0 -> PrognozaColors(
                background = scorching_1,
                onBackground = on_scorching_1
            )
            in 105.0..110.0 -> PrognozaColors(
                background = scorching_2,
                onBackground = on_scorching_2
            )
            in 110.0..115.0 -> PrognozaColors(
                background = scorching_3,
                onBackground = on_scorching_3
            )
            in 115.0..120.0 -> PrognozaColors(
                background = scorching_4,
                onBackground = on_scorching_4
            )
            else -> PrognozaColors(
                background = scorching_5,
                onBackground = on_scorching_5
            )
        }
    }
}