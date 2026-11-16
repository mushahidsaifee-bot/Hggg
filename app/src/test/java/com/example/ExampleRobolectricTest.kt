package com.example

import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun readAppNameFromContext() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BMI Calculator", appName)
  }

  @Test
  fun calculateSaveAndViewHistoryCuj() {
    composeTestRule.waitForIdle()

    // Enter Metric height and weight
    composeTestRule.onNodeWithTag("height_cm_input").performScrollTo().performTextInput("175")
    composeTestRule.onNodeWithTag("weight_input").performScrollTo().performTextInput("70")
    composeTestRule.waitForIdle()

    // No real-time BMI preview appears before clicking Calculate BMI
    assertEquals(0, composeTestRule.onAllNodesWithText("22.86").fetchSemanticsNodes().size)
    assertEquals(0, composeTestRule.onAllNodesWithText("Instant BMI").fetchSemanticsNodes().size)

    // Calculate BMI -> navigates to Result screen
    composeTestRule.onNodeWithTag("calculate_bmi_button").performScrollTo().performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("result_bmi_value").assertIsDisplayed()
    composeTestRule.onNodeWithText("22.86").assertIsDisplayed()
    composeTestRule.onNodeWithTag("save_result_button").performScrollTo().performClick()
    composeTestRule.waitForIdle()

    // Go back to Calculator and switch to History tab
    composeTestRule.onNodeWithTag("result_back_button").performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("nav_history").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      composeTestRule.onAllNodesWithText("22.86").fetchSemanticsNodes().isNotEmpty()
    }
    composeTestRule.onNodeWithText("22.86").assertIsDisplayed()

    // Navigate to Settings -> verify link containers and BMI Calculation Methodology container
    composeTestRule.onNodeWithTag("nav_settings").performClick()
    composeTestRule.waitForIdle()

    composeTestRule
      .onNodeWithTag("settings_privacy_policy_item")
      .performScrollTo()
      .assertIsDisplayed()
      .performClick()
    composeTestRule.waitForIdle()
    val privacyIntent = Shadows.shadowOf(composeTestRule.activity).nextStartedActivity
    assertEquals(Intent.ACTION_VIEW, privacyIntent?.action)
    assertEquals("https://www.google.com", privacyIntent?.dataString)

    composeTestRule
      .onNodeWithTag("settings_about_app_item")
      .performScrollTo()
      .assertIsDisplayed()
      .performClick()
    composeTestRule.waitForIdle()
    val aboutIntent = Shadows.shadowOf(composeTestRule.activity).nextStartedActivity
    assertEquals(Intent.ACTION_VIEW, aboutIntent?.action)
    assertEquals("https://www.google.com", aboutIntent?.dataString)

    composeTestRule
      .onNodeWithTag("settings_terms_conditions_item")
      .performScrollTo()
      .assertIsDisplayed()
      .performClick()
    composeTestRule.waitForIdle()
    val termsIntent = Shadows.shadowOf(composeTestRule.activity).nextStartedActivity
    assertEquals(Intent.ACTION_VIEW, termsIntent?.action)
    assertEquals("https://www.google.com", termsIntent?.dataString)
    composeTestRule
      .onNodeWithTag("settings_methodology_container")
      .performScrollTo()
      .assertIsDisplayed()
    composeTestRule
      .onNodeWithText("Disclaimer & Limitations")
      .performScrollTo()
      .assertIsDisplayed()
  }

  @Test
  fun deleteHistoryItemShowsConfirmationDialogWithYesAndCancel() {
    composeTestRule.waitForIdle()

    // Calculate and save a distinct record (180 cm, 81 kg -> BMI 25.0)
    composeTestRule.onNodeWithTag("height_cm_input").performScrollTo().performTextInput("180")
    composeTestRule.onNodeWithTag("weight_input").performScrollTo().performTextInput("81")
    composeTestRule.onNodeWithTag("calculate_bmi_button").performScrollTo().performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("save_result_button").performScrollTo().performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("result_back_button").performClick()
    composeTestRule.waitForIdle()

    // Open History tab
    composeTestRule.onNodeWithTag("nav_history").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      composeTestRule.onAllNodesWithText("25.00").fetchSemanticsNodes().isNotEmpty()
    }

    // Open saved record and check delete confirmation from Result screen
    composeTestRule.onNodeWithText("25.00").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("result_delete_button").performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithText("Are you sure you want to delete?").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cancel_delete_record_button").performClick()
    composeTestRule.waitForIdle()

    // Record should still be open; go back to History
    composeTestRule.onNodeWithTag("result_bmi_value").assertIsDisplayed()
    composeTestRule.onNodeWithTag("result_back_button").performClick()
    composeTestRule.waitForIdle()

    val countBeforeDelete =
      composeTestRule.onAllNodesWithContentDescription("Delete BMI record").fetchSemanticsNodes().size

    // Tap delete icon in History -> Cancel keeps record
    composeTestRule.onAllNodesWithContentDescription("Delete BMI record").onFirst().performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Are you sure you want to delete?").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cancel_delete_record_button").performClick()
    composeTestRule.waitForIdle()
    assertEquals(
      countBeforeDelete,
      composeTestRule.onAllNodesWithContentDescription("Delete BMI record").fetchSemanticsNodes().size,
    )

    // Tap delete icon again -> Yes removes record
    composeTestRule.onAllNodesWithContentDescription("Delete BMI record").onFirst().performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("confirm_delete_record_button").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      composeTestRule
        .onAllNodesWithContentDescription("Delete BMI record")
        .fetchSemanticsNodes()
        .size == countBeforeDelete - 1
    }
    composeTestRule.onAllNodesWithText("25.00").fetchSemanticsNodes().also { nodes ->
      assertEquals(0, nodes.size)
    }
  }
}
