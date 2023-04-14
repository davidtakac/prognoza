import hr.dtakac.prognoza.shared.entity.Angle
import hr.dtakac.prognoza.shared.entity.AngleUnit
import hr.dtakac.prognoza.shared.entity.CompassDirection
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class AngleTest {
    private val tolerance = 0.0001

    @Test
    fun `directions are correct when angles are on compass rose`() {
        val degreesToExpectedDirections = mapOf(
            0.0 to CompassDirection.N,
            30.0 to CompassDirection.N,

            45.0 to CompassDirection.NE,
            75.0 to CompassDirection.NE,

            90.0 to CompassDirection.E,
            120.0 to CompassDirection.E,

            135.0 to CompassDirection.SE,
            165.0 to CompassDirection.SE,

            180.0 to CompassDirection.S,
            210.0 to CompassDirection.S,

            225.0 to CompassDirection.SW,
            255.0 to CompassDirection.SW,

            270.0 to CompassDirection.W,
            300.0 to CompassDirection.W,

            315.0 to CompassDirection.NW,
            345.0 to CompassDirection.NW
        )
        degreesToExpectedDirections.keys
            .map { it to Angle(it, AngleUnit.Degree).compassDirection }
            .forEach { (degrees, actualDirection) ->
                assertEquals(
                    expected = degreesToExpectedDirections[degrees]!!,
                    actual = actualDirection
                )
            }
    }

    @Test
    fun `direction is west when angle is -90 deg`() = assertEquals(
        expected = CompassDirection.W,
        actual = Angle(-90.0, AngleUnit.Degree).compassDirection
    )

    @Test
    fun `direction is west when angle is -450 deg`() = assertEquals(
        expected = CompassDirection.W,
        actual = Angle(-450.0, AngleUnit.Degree).compassDirection
    )

    @Test
    fun `direction is east when angle is 450 deg`() = assertEquals(
        expected = CompassDirection.E,
        actual = Angle(450.0, AngleUnit.Degree).compassDirection
    )

    @Test
    fun `radians are pi when angle is 180 deg`() = assertEquals(
        expected = PI,
        actual = Angle(180.0, AngleUnit.Degree).radian,
        absoluteTolerance = tolerance
    )

    @Test
    fun `degrees are 180 when angle is pi rad`() = assertEquals(
        expected = 180.0,
        actual = Angle(PI, AngleUnit.Radian).degree,
        absoluteTolerance = tolerance
    )
}