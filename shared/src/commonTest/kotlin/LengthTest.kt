import hr.dtakac.prognoza.shared.entity.Length
import hr.dtakac.prognoza.shared.entity.LengthUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LengthTest {
  @Test
  fun `throws when less than zero`() {
    assertFailsWith<IllegalStateException> {
      Length(-1.0, LengthUnit.Millimetre)
    }
  }

  @Test
  fun `converts inches to others`() {
    val length = Length(2.0, LengthUnit.Inch)
    assertLengthsAreAsExpected(
      length = length,
      expectedMm = 50.8,
      expectedCm = 5.08,
      expectedIn = 2.0
    )
  }

  @Test
  fun `converts millimeters to others`() {
    val length = Length(2.0, LengthUnit.Millimetre)
    assertLengthsAreAsExpected(
      length = length,
      expectedMm = 2.0,
      expectedIn = 0.0787,
      expectedCm = 0.2
    )
  }

  @Test
  fun `converts centimeters to others`() {
    val length = Length(2.0, LengthUnit.Centimetre)
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
      actual = length.centimetres,
      absoluteTolerance = tolerance
    )
    assertEquals(
      expected = expectedMm,
      actual = length.millimetres,
      absoluteTolerance = tolerance
    )
    assertEquals(
      expected = expectedIn,
      actual = length.inches,
      absoluteTolerance = tolerance
    )
  }
}