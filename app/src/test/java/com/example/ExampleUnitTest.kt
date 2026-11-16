package com.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun metricBmi_calculatesAndCategorizesCorrectly() {
    val normal = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 70.0)
    assertEquals(22.9, normal, 0.01)
    assertEquals(BmiCategory.NORMAL, BmiCalculator.categorize(normal))

    val underweight = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 50.0)
    assertEquals(16.3, underweight, 0.01)
    assertEquals(BmiCategory.UNDERWEIGHT, BmiCalculator.categorize(underweight))

    val overweight = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 80.0)
    assertEquals(26.1, overweight, 0.01)
    assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categorize(overweight))

    val obese = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 100.0)
    assertEquals(32.7, obese, 0.01)
    assertEquals(BmiCategory.OBESE, BmiCalculator.categorize(obese))
  }

  @Test
  fun imperialBmi_calculatesCorrectly() {
    // 5 ft 10 in = 70 in, 160 lb -> (160 * 703) / 4900 = 22.955 -> 23.0
    val bmi = BmiCalculator.calculateImperialBmi(heightInches = 70.0, weightLb = 160.0)
    assertEquals(23.0, bmi, 0.01)
    assertEquals(BmiCategory.NORMAL, BmiCalculator.categorize(bmi))
  }

  @Test
  fun validation_rejectsMissingAndOutOfRangeInputs() {
    val empty =
      BmiCalculator.validateAndCalculate(
        unitSystem = UnitSystem.METRIC,
        heightCmText = "",
        feetText = "",
        inchesText = "",
        weightText = "",
        ageText = "",
        gender = GenderOption.UNSPECIFIED,
      )
    assertFalse(empty.isValid)
    assertEquals(HeightValidationError.Required, empty.heightError)
    assertEquals(WeightValidationError.Required, empty.weightError)

    val outOfRange =
      BmiCalculator.validateAndCalculate(
        unitSystem = UnitSystem.METRIC,
        heightCmText = "20",
        feetText = "",
        inchesText = "",
        weightText = "700",
        ageText = "150",
        gender = GenderOption.MALE,
      )
    assertFalse(outOfRange.isValid)
    assertEquals(HeightValidationError.OutOfRangeMetric, outOfRange.heightError)
    assertEquals(WeightValidationError.OutOfRangeMetric, outOfRange.weightError)
    assertEquals(AgeValidationError.OutOfRange, outOfRange.ageError)
  }

  @Test
  fun validation_imperialFeetAndInches_succeeds() {
    val result =
      BmiCalculator.validateAndCalculate(
        unitSystem = UnitSystem.IMPERIAL,
        heightCmText = "",
        feetText = "5",
        inchesText = "9",
        weightText = "154",
        ageText = "30",
        gender = GenderOption.FEMALE,
      )
    assertTrue(result.isValid)
    assertNotNull(result.calculation)
    assertEquals(22.7, result.calculation!!.bmi, 0.01)
    assertEquals(BmiCategory.NORMAL, result.calculation!!.category)
    assertEquals(30, result.calculation!!.age)
    assertEquals(GenderOption.FEMALE, result.calculation!!.gender)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun calculateButton_showsInterstitialEveryEightValidCalculations() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    Dispatchers.setMain(dispatcher)
    try {
      val fakeRepo = FakeBmiRepository()
      val viewModel = BmiViewModel(fakeRepo)
      advanceUntilIdle()

      // Invalid calculation does not count toward 8
      assertEquals(CalculateOutcome.INVALID, viewModel.calculateAndPrepareResultWithAdCheck())

      viewModel.onHeightCmChanged("175")
      viewModel.onWeightChanged("70")

      for (i in 1..16) {
        val outcome = viewModel.calculateAndPrepareResultWithAdCheck()
        val expected =
          if (i % 8 == 0) CalculateOutcome.SHOW_INTERSTITIAL else CalculateOutcome.NAVIGATE_DIRECTLY
        assertEquals("Failed at calculation #$i", expected, outcome)
      }
      advanceUntilIdle()
      assertEquals(16, fakeRepo.prefsFlow.value.calculationCount)
    } finally {
      Dispatchers.resetMain()
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun saveCurrentResult_ignoresDuplicateTaps() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    Dispatchers.setMain(dispatcher)
    try {
      val fakeRepo = FakeBmiRepository()
      val viewModel = BmiViewModel(fakeRepo)
      advanceUntilIdle()

      viewModel.onHeightCmChanged("175")
      viewModel.onWeightChanged("70")
      assertEquals(CalculateOutcome.NAVIGATE_DIRECTLY, viewModel.calculateAndPrepareResultWithAdCheck())

      viewModel.saveCurrentResult()
      viewModel.saveCurrentResult()
      advanceUntilIdle()

      assertEquals(1, fakeRepo.saveCount)
      assertTrue(viewModel.resultState.value.isSaved)
    } finally {
      Dispatchers.resetMain()
    }
  }

  private class FakeBmiRepository : BmiRepository {
    val prefsFlow = MutableStateFlow(UserPreferences())
    var saveCount = 0
    override val allRecords: Flow<List<BmiRecordEntity>> = flowOf(emptyList())
    override val userPreferences: Flow<UserPreferences> = prefsFlow

    override fun getRecordById(id: Long): Flow<BmiRecordEntity?> = flowOf(null)

    override suspend fun saveCalculation(calculation: BmiCalculation): Long {
      saveCount += 1
      return saveCount.toLong()
    }

    override suspend fun deleteRecordById(id: Long) = Unit

    override suspend fun clearAllRecords() = Unit

    override suspend fun setThemeMode(themeMode: ThemeMode) {
      prefsFlow.value = prefsFlow.value.copy(themeMode = themeMode)
    }

    override suspend fun setDefaultUnitSystem(unitSystem: UnitSystem) {
      prefsFlow.value = prefsFlow.value.copy(defaultUnitSystem = unitSystem)
    }

    override suspend fun setCalculationCount(count: Int) {
      prefsFlow.value = prefsFlow.value.copy(calculationCount = count)
    }
  }
}
