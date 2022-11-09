import hr.dtakac.prognoza.entities.forecast.units.Length
import hr.dtakac.prognoza.entities.forecast.units.LengthUnit
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LengthTest {
    @Test
    fun `throws when less than zero`() {
        assertFailsWith<IllegalStateException> {
            Length(-1.0, LengthUnit.MILLIMETRE)
        }
    }

    @Test
    fun `converts inches to others`() {
        val length = Length(2.0, LengthUnit.INCH)
        assertLengthsAreAsExpected(
            length = length,
            expectedMm = 50.8,
            expectedCm = 5.08,
            expectedIn = 2.0
        )
    }

    @Test
    fun `converts millimeters to others`() {
        val length = Length(2.0, LengthUnit.MILLIMETRE)
        assertLengthsAreAsExpected(
            length = length,
            expectedMm = 2.0,
            expectedIn = 0.0787,
            expectedCm = 0.2
        )
    }

    @Test
    fun `converts centimeters to others`() {
        val length = Length(2.0, LengthUnit.CENTIMETRE)
        assertLengthsAreAsExpected(
            length = length,
            expectedMm = 20.0,
            expectedIn = 0.7874,
            expectedCm = 2.0
        )
    }

    private fun assertLengthsAreAsExpected(
        length: Length,
        expectedMm: Double,
        expectedIn: Double,
        expectedCm: Double
    ) {
        val tolerance = 0.0001
        assertEquals(
            expected = expectedCm,
            actual = length.centimeters,
            absoluteTolerance = tolerance
        )
        assertEquals(
            expected = expectedMm,
            actual = length.millimeters,
            absoluteTolerance = tolerance
        )
        assertEquals(
            expected = expectedIn,
            actual = length.inches,
            absoluteTolerance = tolerance
        )
    }
}