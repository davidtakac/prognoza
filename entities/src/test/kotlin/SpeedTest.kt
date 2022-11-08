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
        val expectedKmh = 7.2
        val expectedMph = 4.4738
        val expectedKt = 3.8876
        val expectedBeaufort = BeaufortScale.LIGHT_BREEZE
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = 2.0,
            expectedKmh = expectedKmh,
            expectedMph = expectedMph,
            expectedBeaufort = expectedBeaufort,
            expectedKt = expectedKt
        )
    }

    @Test
    fun `converts miles per hour to others`() {
        val speed = Speed(2.0, SpeedUnit.MPH)
        val expectedMps = 0.8940
        val expectedKmh = 3.2187
        val expectedKt = 1.7380
        val expectedBeaufort = BeaufortScale.LIGHT_AIR
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = expectedMps,
            expectedKmh = expectedKmh,
            expectedMph = 2.0,
            expectedBeaufort = expectedBeaufort,
            expectedKt = expectedKt
        )
    }

    @Test
    fun `converts kilometers per hour to others`() {
        val speed = Speed(2.0, SpeedUnit.KMH)
        val expectedMps = 0.5556
        val expectedMph = 1.2427
        val expectedKt = 1.0799
        val expectedBeaufort = BeaufortScale.LIGHT_AIR
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = expectedMps,
            expectedKmh = 2.0,
            expectedMph = expectedMph,
            expectedBeaufort = expectedBeaufort,
            expectedKt = expectedKt
        )
    }

    @Test
    fun `converts knots to others`() {
        val speed = Speed(2.0, SpeedUnit.KT)
        val expectedMps = 1.0289
        val expectedKmh = 3.7040
        val expectedMph = 2.3016
        val expectedBeaufort = BeaufortScale.LIGHT_AIR
        assertSpeedsAreAsExpected(
            speed = speed,
            expectedMps = expectedMps,
            expectedKmh = expectedKmh,
            expectedMph = expectedMph,
            expectedBeaufort = expectedBeaufort,
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