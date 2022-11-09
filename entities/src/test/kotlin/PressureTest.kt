import hr.dtakac.prognoza.entities.forecast.units.Pressure
import hr.dtakac.prognoza.entities.forecast.units.PressureUnit
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PressureTest {
    private val tolerance = 0.0001

    @Test
    fun `throws exception when negative`() {
        assertFailsWith<IllegalStateException> {
            Pressure(-98.0, PressureUnit.MILLIBAR)
        }
    }

    @Test
    fun `converts from millxkibar to inches of mercury`() = assertEquals(
        expected = 0.0886,
        actual = Pressure(3.0, PressureUnit.MILLIBAR).inchesOfMercury,
        absoluteTolerance = tolerance
    )

    @Test
    fun `converts from inches of mercury to millibar`() = assertEquals(
        expected = 101.5917,
        actual = Pressure(3.0, PressureUnit.INCH_OF_MERCURY).millibar,
        absoluteTolerance = tolerance
    )
}