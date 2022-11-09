import hr.dtakac.prognoza.entities.forecast.units.Percentage
import hr.dtakac.prognoza.entities.forecast.units.PercentageUnit
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PercentageTest {
    private val tolerance = 0.0001

    @Test
    fun `throws exception when negative`() {
        assertFailsWith<IllegalStateException> {
            Percentage(-5.0, PercentageUnit.PERCENT)
        }
    }

    @Test
    fun `throws exception when greater than 100 percent`() {
        assertFailsWith<IllegalStateException> {
            Percentage(1.01, PercentageUnit.FRACTION)
        }
    }

    @Test
    fun `converts from percent to fraction`() = assertEquals(
        expected = 0.7891,
        actual = Percentage(78.91, PercentageUnit.PERCENT).fraction,
        absoluteTolerance = tolerance
    )

    @Test
    fun `converts from fraction to percent`() = assertEquals(
        expected = 28.71,
        actual = Percentage(0.2871, PercentageUnit.FRACTION).percent,
        absoluteTolerance = tolerance
    )
}