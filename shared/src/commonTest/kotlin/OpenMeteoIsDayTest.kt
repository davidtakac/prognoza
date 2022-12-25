import hr.dtakac.prognoza.shared.data.openmeteo.network.calculateAreTimestampsDay
import kotlin.test.Test
import kotlin.test.assertContentEquals

class OpenMeteoIsDayTest {
    @Test
    fun `is day after sunrise`() {
        assertContentEquals(
            expected = listOf(true),
            actual = calculateAreTimestampsDay(
                timestamps = listOf(2L),
                sunrises = listOf(1L),
                sunsets = listOf(3L)
            ).values
        )
    }

    @Test
    fun `is night after sunset`() {
        val times = listOf(
            3L, // Day
            5L  // Night
        )
        val sunrises = listOf(2L)
        val sunsets = listOf(4L)
        assertContentEquals(
            expected = listOf(true, false),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `is day after second sunrise`() {
        val times = listOf(
            1L, // Night
            3L, // Day
            5L, // Night
            7L  // Day
        )
        val sunrises = listOf(2L, 6L)
        val sunsets = listOf(4L, 8L)
        assertContentEquals(
            expected = listOf(false, true, false, true),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `is night after second sunset`() {
        val times = listOf(
            1L, // Night
            3L, // Day
            5L, // Night
            7L, // Day
            9L  // Night
        )
        val sunrises = listOf(2L, 6L)
        val sunsets = listOf(4L, 8L)
        assertContentEquals(
            expected = listOf(false, true, false, true, false),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `is night between sunset and sunrise`() {
        val sunrises = listOf(1L, 4L)
        val sunsets = listOf(2L, 5L)
        val times = listOf(3L)
        assertContentEquals(
            expected = listOf(false),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `stays night indefinitely after last sunset`() {
        val sunrises = listOf(1L, 4L)
        val sunsets = listOf(2L, 5L)
        val times = listOf(3L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L)
        assertContentEquals(
            expected = times.map { false },
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `recognizes single polar night`() {
        val sunrises = listOf(0L)
        val sunsets = listOf(0L)
        val times = listOf(1L)
        assertContentEquals(
            expected = listOf(false),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `recognizes exclusively polar night`() {
        val sunrises = listOf(0L, 0L, 0L, 0L)
        val times = listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)
        assertContentEquals(
            expected = times.map { false },
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunrises
            ).values
        )
    }

    @Test
    fun `recognizes polar night at beginning`() {
        val sunrises = listOf(0L, 0L, 0L, 5L)
        val sunsets = listOf(0L, 0L, 0L, 8L)
        val times = listOf(1L, 2L, 3L, 4L, 6L, 7L, 9L, 10L)
        assertContentEquals(
            expected = listOf(false, false, false, false, true, true, false, false),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `recognizes polar night at end`() {
        val sunrises = listOf(1L, 5L, 0L, 0L, 0L)
        val sunsets = listOf(3L, 7L, 0L, 0L, 0L)
        val times = listOf(2L, 6L, 10L, 11L, 12L)
        assertContentEquals(
            expected = listOf(true, true, false, false, false),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `recognizes polar night in the middle`() {
        val sunrises = listOf(1L, 4L, 7L, 0L, 0L, 0L, 14L, 17L)
        val sunsets = listOf(3L, 6L, 9L, 0L, 0L, 0L, 16L, 19L)
        val times = listOf(2L, 5L, 8L, 10L, 11L, 12L, 13L, 15L, 18L)
        assertContentEquals(
            expected = listOf(true, true, true, false, false, false, false, true, true),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }

    @Test
    fun `recognizes multiple polar nights at all places`() {
        val sunrises = listOf(0L, 0L, 5L, 8L, 0L, 0L, 13L, 0L, 0L, 19L, 22L, 0L, 0L)
        val sunsets = listOf(0L, 0L, 7L, 10L, 0L, 0L, 15L, 0L, 0L, 21L, 24L, 0L, 0L)
        val times = listOf(
            1L, 2L, 3L, // polar night
            6L, 9L, // day
            11L, 12L, // polar night
            14L, // day
            16L, 17L, 18L, // polar night
            20L, 23L, // day
            25L, 26L, 27L // polar night
        )
        assertContentEquals(
            expected = listOf(
                false, false, false,
                true, true,
                false, false,
                true,
                false, false, false,
                true, true,
                false, false, false
            ),
            actual = calculateAreTimestampsDay(
                timestamps = times,
                sunrises = sunrises,
                sunsets = sunsets
            ).values
        )
    }
}