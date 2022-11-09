import hr.dtakac.prognoza.entities.Place
import org.testng.annotations.Test
import kotlin.test.assertFailsWith

class PlaceTest {
    @Test
    fun `when latitude out of bounds, throws exception`() {
        assertFailsWith<IllegalStateException> {
            Place(name = "", details = null, latitude = -92.0, longitude = 0.0)
        }
        assertFailsWith<IllegalStateException> {
            Place(name = "", details = null, latitude = 92.0, longitude = 0.0)
        }
    }

    @Test
    fun `when longitude out of bounds, throws exception`() {
        assertFailsWith<IllegalStateException> {
            Place(name = "", details = null, latitude = 0.0, longitude = -182.0)
        }
        assertFailsWith<IllegalStateException> {
            Place(name = "", details = null, latitude = 0.0, longitude = 182.0)
        }
    }

    @Test
    fun `when latitude and longitude out of bounds, throws exception`() {
        assertFailsWith<IllegalStateException> {
            Place(name = "", details = null, latitude = -92.0, longitude = -182.0)
        }
        assertFailsWith<IllegalStateException> {
            Place(name = "", details = null, latitude = 92.0, longitude = 182.0)
        }
    }
}