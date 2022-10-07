package hr.dtakac.prognoza

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import hr.dtakac.prognoza.ui.common.ContentLoadingIndicatorHost
import org.junit.Rule
import org.junit.Test

class ContentLoadingIndicatorHostTest {
    @get:Rule
    val composeTestRule = createComposeRule().apply { mainClock.autoAdvance = false }

    private val showDelay = 500L
    private val minShowTime = 500L
    private val tolerance = 20L
    private val loaderText = "loader"

    @Test
    fun whenNoTimeElapsedAndStateIsTrue_loaderInvisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }
        composeTestRule.onNodeWithText(loaderText).assertDoesNotExist()
    }

    @Test
    fun whenElapsedTimeLessThanShowDelay_loaderInvisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }

        composeTestRule.mainClock.advanceTimeBy(showDelay - tolerance)
        composeTestRule.onNodeWithText(loaderText).assertDoesNotExist()
    }

    @Test
    fun whenElapsedTimeGreaterThanShowDelay_loaderVisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }

        composeTestRule.mainClock.advanceTimeBy(showDelay + tolerance)
        composeTestRule.onNodeWithText(loaderText).assertExists()
    }

    @Test
    fun whenElapsedTimeBetweenShowDelayAndMinShowTime_loaderVisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }

        composeTestRule.mainClock.advanceTimeBy(showDelay + tolerance)
        composeTestRule.onNodeWithText(loaderText).assertExists()

        isLoading.value = false

        // Even though isLoading is false, the minShowTime did not yet elapse, so the loader
        // must still remain visible
        composeTestRule.mainClock.advanceTimeBy(tolerance)
        composeTestRule.onNodeWithText(loaderText).assertExists()
    }

    @Test
    fun whenElapsedTimeGreaterThanShowDelayPlusMinShowTime_loaderInvisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }

        composeTestRule.mainClock.advanceTimeBy(showDelay + tolerance)
        composeTestRule.onNodeWithText(loaderText).assertExists()

        isLoading.value = false

        composeTestRule.mainClock.advanceTimeBy(minShowTime + tolerance)
        composeTestRule.onNodeWithText(loaderText).assertDoesNotExist()
    }

    @Test
    fun whenShowAndHideCalledMultipleTimesEndingWithHide_loaderInvisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }

        repeat(6) {
            // last idx is 5. 5 mod 2 = 1, so this sequence ends with hide
            isLoading.value = it % 2 == 0
            composeTestRule.mainClock.advanceTimeByFrame()
        }

        composeTestRule.mainClock.advanceTimeBy(showDelay + tolerance)
        composeTestRule.onNodeWithText(loaderText).assertDoesNotExist()
    }

    @Test
    fun whenShowAndHideCalledMultipleTimesEndingWithShow_loaderVisible() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }

        repeat(7) {
            // last idx is 6. 6 mod 2 = 0, so this sequence ends with show
            isLoading.value = it % 2 == 0
            composeTestRule.mainClock.advanceTimeByFrame()
        }

        composeTestRule.mainClock.advanceTimeBy(showDelay + tolerance)
        composeTestRule.onNodeWithText(loaderText).assertExists()
    }

    @Composable
    private fun TestContentLoadingIndicatorHost(
        isLoading: Boolean
    ) {
        ContentLoadingIndicatorHost(
            isLoading = isLoading,
            showDelay = showDelay,
            minShowTime = minShowTime
        ) { isVisible ->
            if (isVisible) {
                Text(loaderText)
            }
        }
    }
}