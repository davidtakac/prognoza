import hr.dtakac.prognoza.shared.entity.Angle
import hr.dtakac.prognoza.shared.entity.AngleUnit
import hr.dtakac.prognoza.shared.entity.CardinalDirection
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class AngleTest {
    private val tolerance = 0.0001

    @Test
    fun `directions are correct when angles are on compass rose`() {
        val degreesToExpectedDirections = mapOf(
            0.0 to CardinalDirection.N,
            30.0 to CardinalDirection.N,

            45.0 to CardinalDirection.NE,
            75.0 to CardinalDirection.NE,

            90.0 to CardinalDirection.E,
            120.0 to CardinalDirection.E,

            135.0 to CardinalDirection.SE,
            165.0 to CardinalDirection.SE,

            180.0 to CardinalDirection.S,
            210.0 to CardinalDirection.S,

            225.0 to CardinalDirection.SW,
            255.0 to CardinalDirection.SW,

            270.0 to CardinalDirection.W,
            300.0 to CardinalDirection.W,

            315.0 to CardinalDirection.NW,
            345.0 to CardinalDirection.NW
        )
        degreesToExpectedDirections.keys
            .map { it to Angle(it, AngleUnit.Degree).cardinalDirection }
            .forEach { (degrees, actualDirection) ->
                assertEquals(
                    expected = degreesToExpectedDirections[degrees]!!,
                    actual = actualDirection
                )
            }
    }

    @Test
    fun `direction is west when angle is -90 deg`() = assertEquals(
        expected = CardinalDirection.W,
        actual = Angle(-90.0, AngleUnit.Degree).cardinalDirection
    )

    @Test
    fun `direction is west when angle is -450 deg`() = assertEquals(
        expected = CardinalDirection.W,
        actual = Angle(-450.0, AngleUnit.Degree).cardinalDirection
    )

    @Test
    fun `direction is east when angle is 450 deg`() = assertEquals(
        expected = CardinalDirection.E,
        actual = Angle(450.0, AngleUnit.Degree).cardinalDirection
    )

    @Test
    fun `radians are pi when angle is 180 deg`() = assertEquals(
        expected = PI,
        actual = Angle(180.0, AngleUnit.Degree).radians,
        absoluteTolerance = tolerance
    )

    @Test
    fun `degrees are 180 when angle is pi rad`() = assertEquals(
        expected = 180.0,
        actual = Angle(PI, AngleUnit.Radian).degrees,
        absoluteTolerance = tolerance
    )
}