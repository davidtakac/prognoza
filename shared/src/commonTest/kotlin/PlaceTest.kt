import hr.dtakac.prognoza.shared.entity.Place
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PlaceTest {
  @Test
  fun `throws exception when latitude out of bounds`() {
    assertFailsWith<IllegalStateException> {
      Place(name = "", details = null, latitude = -92.0, longitude = 0.0)
    }
    assertFailsWith<IllegalStateException> {
      Place(name = "", details = null, latitude = 92.0, longitude = 0.0)
    }
  }

  @Test
  fun `throws exception when longitude out of bounds`() {
    assertFailsWith<IllegalStateException> {
      Place(name = "", details = null, latitude = 0.0, longitude = -182.0)
    }
    assertFailsWith<IllegalStateException> {
      Place(name = "", details = null, latitude = 0.0, longitude = 182.0)
    }
  }

  @Test
  fun `throws exception when latitude and longitude out of bounds`() {
    assertFailsWith<IllegalStateException> {
      Place(name = "", details = null, latitude = -92.0, longitude = -182.0)
    }
    assertFailsWith<IllegalStateException> {
      Place(name = "", details = null, latitude = 92.0, longitude = 182.0)
    }
  }
}