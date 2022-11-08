import hr.dtakac.prognoza.entities.forecast.units.BeaufortScale
import hr.dtakac.prognoza.entities.forecast.units.Speed
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SpeedTest { // Not Ookla ;)
    @Test
    fun `throws exception when less than 0`() {
        assertFailsWith<IllegalStateException> {
            Speed(-20.0, SpeedUnit.MPS)
        }
    }

    @Test
    fun `converts meters per second to others`() {
        val speed = Speed(2.0, SpeedUnit.MPS)
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = 2.0,
            expectedKmh = 7.2,
            expectedMph = 4.4738,
            expectedBeaufort = BeaufortScale.LIGHT_BREEZE,
            expectedKt = 3.8876
        )
    }

    @Test
    fun `converts miles per hour to others`() {
        val speed = Speed(2.0, SpeedUnit.MPH)
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = 0.8940,
            expectedKmh = 3.2187,
            expectedMph = 2.0,
            expectedBeaufort = BeaufortScale.LIGHT_AIR,
            expectedKt = 1.7380
        )
    }

    @Test
    fun `converts kilometers per hour to others`() {
        val speed = Speed(2.0, SpeedUnit.KMH)
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = 0.5556,
            expectedKmh = 2.0,
            expectedMph = 1.2427,
            expectedBeaufort = BeaufortScale.LIGHT_AIR,
            expectedKt = 1.0799
        )
    }

    @Test
    fun `converts knots to others`() {
        val speed = Speed(2.0, SpeedUnit.KN)
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = 1.0289,
            expectedKmh = 3.7040,
            expectedMph = 2.3016,
            expectedBeaufort = BeaufortScale.LIGHT_AIR,
            expectedKt = 2.0
        )
    }

    private fun assertSpeedsAreAsExpected(
        speed: Speed,
        expectedMps: Double,
        expectedKmh: Double,
        expectedMph: Double,
        expectedKt: Double,
        expectedBeaufort: BeaufortScale
    ) {
        val tolerance = 0.0001
        assertEquals(
            expected = expectedKt,
            actual = speed.knots,
            absoluteTolerance = tolerance
        )
        assertEquals(
            expected = expectedMph,
            actual = speed.milesPerHour,
            absoluteTolerance = tolerance
        )
        assertEquals(
            expected = expectedMps,
            actual = speed.metersPerSecond,
            absoluteTolerance = tolerance
        )
        assertEquals(
            expected = expectedKmh,
            actual = speed.kilometersPerHour,
            absoluteTolerance = tolerance
        )
        assertEquals(
            expected = expectedBeaufort,
            actual = speed.beaufort
        )
    }
}