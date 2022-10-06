@file:Suppress("IllegalIdentifier")
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

    private val deferVisibilityFor = 100L
    private val showForAtLeast = 100L

    @Test
    fun notVisibleImmediately() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }
        composeTestRule.onNodeWithText("false").assertExists()
    }

    @Test
    fun deferVisibilityFor() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }
        composeTestRule.mainClock.advanceTimeBy(deferVisibilityFor + 20L)
        composeTestRule.onNodeWithText("true").assertExists()
    }

    @Test
    fun showForAtLeast() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }
        composeTestRule.mainClock.advanceTimeBy(deferVisibilityFor + 20L)
        isLoading.value = false
        composeTestRule.onNodeWithText("true").assertExists()
    }

    @Test
    fun hideAfterShowForAtLeast() {
        val isLoading = mutableStateOf(true)
        composeTestRule.setContent {
            TestContentLoadingIndicatorHost(isLoading.value)
        }
        composeTestRule.mainClock.advanceTimeBy(deferVisibilityFor + 20L)
        isLoading.value = false
        composeTestRule.mainClock.advanceTimeBy(showForAtLeast)
        composeTestRule.onNodeWithText("false").assertExists()
    }

    @Composable
    private fun TestContentLoadingIndicatorHost(
        isLoading: Boolean
    ) {
        ContentLoadingIndicatorHost(
            isLoading = isLoading,
            deferVisibilityForMillis = deferVisibilityFor,
            showForAtLeastMillis = showForAtLeast
        ) { isVisible ->
            Text(isVisible.toString())
        }
    }
}