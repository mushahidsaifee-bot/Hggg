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
    assertEquals(22.857142857142858, normal, 0.0001)
    assertEquals("22.86", BmiFormatters.formatBmi(normal))
    assertEquals(BmiCategory.NORMAL, BmiCalculator.categorize(normal))

    // Verify exact Double threshold without premature rounding (76.5 / 1.75^2 = 24.97959... < 25.0)
    val borderlineNormal = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 76.5)
    assertEquals(24.979591836734695, borderlineNormal, 0.0001)
    assertEquals(BmiCategory.NORMAL, BmiCalculator.categorize(borderlineNormal))

    // Verify exact 25.0 boundaries without 1.6 / 1.7 binary division drift
    val exactBoundary160 = BmiCalculator.calculateMetricBmi(heightCm = 160.0, weightKg = 64.0)
    assertEquals(25.0, exactBoundary160, 0.0)
    assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categorize(exactBoundary160))

    val exactBoundary170 = BmiCalculator.calculateMetricBmi(heightCm = 170.0, weightKg = 72.25)
    assertEquals(25.0, exactBoundary170, 0.0)
    assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categorize(exactBoundary170))

    val underweight = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 50.0)
    assertEquals(16.3265306122449, underweight, 0.0001)
    assertEquals(BmiCategory.UNDERWEIGHT, BmiCalculator.categorize(underweight))

    val overweight = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 80.0)
    assertEquals(26.122448979591837, overweight, 0.0001)
    assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categorize(overweight))

    val obesityClass1 = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 100.0)
    assertEquals(32.6530612244898, obesityClass1, 0.0001)
    assertEquals(BmiCategory.OBESITY_CLASS_1, BmiCalculator.categorize(obesityClass1))

    val obesityClass2 = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 112.5)
    assertEquals(36.734693877551024, obesityClass2, 0.0001)
    assertEquals(BmiCategory.OBESITY_CLASS_2, BmiCalculator.categorize(obesityClass2))

    val obesityClass3 = BmiCalculator.calculateMetricBmi(heightCm = 175.0, weightKg = 130.0)
    assertEquals(42.44897959183673, obesityClass3, 0.0001)
    assertEquals(BmiCategory.OBESITY_CLASS_3, BmiCalculator.categorize(obesityClass3))

    assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categorize(29.9))
    assertEquals(BmiCategory.OBESITY_CLASS_1, BmiCalculator.categorize(30.0))
    assertEquals(BmiCategory.OBESITY_CLASS_1, BmiCalculator.categorize(34.9))
    assertEquals(BmiCategory.OBESITY_CLASS_2, BmiCalculator.categorize(35.0))
    assertEquals(BmiCategory.OBESITY_CLASS_2, BmiCalculator.categorize(39.9))
    assertEquals(BmiCategory.OBESITY_CLASS_3, BmiCalculator.categorize(40.0))

    val legacyObeseEntity =
      BmiRecordEntity(
        id = 1L,
        bmi = 36.2,
        category = "OBESE",
        unitSystem = UnitSystem.METRIC.name,
        heightCm = 175.0,
        weightKg = 111.0,
        feet = 5.0,
        inches = 8.9,
        weightLb = 244.7,
        age = null,
        gender = GenderOption.UNSPECIFIED.name,
        timestampMillis = 1L,
      )
    assertEquals(BmiCategory.OBESITY_CLASS_2, legacyObeseEntity.toBmiCalculation().category)
  }

  @Test
  fun imperialBmi_calculatesCorrectly() {
    // 5 ft 10 in = 70 in, 160 lb -> (160 * 703) / 4900 = 22.9551...
    val bmi = BmiCalculator.calculateImperialBmi(heightInches = 70.0, weightLb = 160.0)
    assertEquals(22.955102040816328, bmi, 0.0001)
    assertEquals(BmiCategory.NORMAL, BmiCalculator.categorize(bmi))
  }

  @Test
  fun conversionsAndFormatters_preserveExactDoubleAccuracy() {
    // 160 cm (1.6 m): Min = 18.5 * 2.56 = 47.36, Max = 24.9 * 2.56 = 63.744
    val (minKg160, maxKg160) = BmiCalculator.healthyWeightRangeKg(160.0)
    assertEquals(47.36, minKg160, 0.0)
    assertEquals(63.744, maxKg160, 0.0)

    // 170 cm (1.7 m): Min = 18.5 * 2.89 = 53.465, Max = 24.9 * 2.89 = 71.961
    val (minKg170, maxKg170) = BmiCalculator.healthyWeightRangeKg(170.0)
    assertEquals(53.465, minKg170, 0.0)
    assertEquals(71.961, maxKg170, 0.0)

    // 70 in (5 ft 10 in): Min = 18.5 * 4900 / 703, Max = 24.9 * 4900 / 703
    val (minLb70, maxLb70) = BmiCalculator.healthyWeightRangeLb(70.0)
    assertEquals(128.947368421053, minLb70, 1e-12)
    assertEquals(173.556187766714, maxLb70, 1e-12)

    val cmRoundTrip =
      BmiCalculator.cmToFeetInches(175.0).let { (ft, inch) ->
        BmiCalculator.feetInchesToCm(ft, inch)
      }
    assertEquals(175.0, cmRoundTrip, 1e-9)

    val kgRoundTrip = BmiCalculator.lbToKg(BmiCalculator.kgToLb(72.35))
    assertEquals(72.35, kgRoundTrip, 1e-9)

    assertEquals("0", BmiFormatters.formatDecimal(-0.0))
    assertEquals("50", BmiFormatters.formatDecimal(50.0))
    assertEquals("2.69", BmiFormatters.formatDecimal(2.685))
    assertEquals("22.86", BmiFormatters.formatBmi(22.857142857142858))

    assertEquals(61.4, BmiCalculator.feetInchesToTotalInches(5.1, 0.2), 0.0)
    // 175 cm (1.75 m): Min = 18.5 * 3.0625 = 56.65625, Max = 24.9 * 3.0625 = 76.25625
    val (min175, max175) = BmiCalculator.healthyWeightRangeKg(175.0)
    assertEquals(56.65625, min175, 0.0)
    assertEquals(76.25625, max175, 0.0)
    assertEquals("56.66", BmiFormatters.formatDecimal(min175))
    assertEquals("76.26", BmiFormatters.formatDecimal(max175))
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
    assertEquals(22.739340474690192, result.calculation!!.bmi, 0.0001)
    assertEquals(BmiCategory.NORMAL, result.calculation!!.category)
    assertEquals(30, result.calculation!!.age)
    assertEquals(GenderOption.FEMALE, result.calculation!!.gender)

    val decimalFeet =
      BmiCalculator.validateAndCalculate(
        unitSystem = UnitSystem.IMPERIAL,
        heightCmText = "",
        feetText = "5.5",
        inchesText = "",
        weightText = "150.5",
        ageText = "",
        gender = GenderOption.UNSPECIFIED,
      )
    assertTrue(decimalFeet.isValid)
    assertNotNull(decimalFeet.calculation)
    assertEquals(5.5, decimalFeet.calculation!!.feet, 0.0001)
    assertEquals(167.64, decimalFeet.calculation!!.heightCm, 0.0001)
    assertEquals(24.28868227731864, decimalFeet.calculation!!.bmi, 0.0001)
    assertEquals("5.5 ft 0 in", BmiFormatters.formatHeight(decimalFeet.calculation!!))
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

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun validationAndInputSanitization_handleBoundariesAndEdgeInputs() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    Dispatchers.setMain(dispatcher)
    try {
      // Standalone dot should fail validation as InvalidNumber
      val dotResult =
        BmiCalculator.validateAndCalculate(
          unitSystem = UnitSystem.METRIC,
          heightCmText = ".",
          feetText = "",
          inchesText = "",
          weightText = ".",
          ageText = "",
          gender = GenderOption.UNSPECIFIED,
        )
      assertFalse(dotResult.isValid)
      assertEquals(HeightValidationError.InvalidNumber, dotResult.heightError)
      assertEquals(WeightValidationError.InvalidNumber, dotResult.weightError)

      // Exact min & max metric boundaries should succeed
      val minMetric =
        BmiCalculator.validateAndCalculate(
          unitSystem = UnitSystem.METRIC,
          heightCmText = "50",
          feetText = "",
          inchesText = "",
          weightText = "10",
          ageText = "2",
          gender = GenderOption.UNSPECIFIED,
        )
      assertTrue(minMetric.isValid)
      assertEquals(40.0, minMetric.calculation!!.bmi, 0.0)
      assertEquals(BmiCategory.OBESITY_CLASS_3, minMetric.calculation!!.category)

      val maxMetric =
        BmiCalculator.validateAndCalculate(
          unitSystem = UnitSystem.METRIC,
          heightCmText = "280",
          feetText = "",
          inchesText = "",
          weightText = "500",
          ageText = "120",
          gender = GenderOption.UNSPECIFIED,
        )
      assertTrue(maxMetric.isValid)

      // Comma decimal separator converts to dot, non-ASCII digits are ignored cleanly
      val viewModel = BmiViewModel(FakeBmiRepository())
      advanceUntilIdle()
      viewModel.onHeightCmChanged("175,5")
      viewModel.onWeightChanged("70,25")
      viewModel.onAgeChanged("2\u06658")
      assertEquals("175.5", viewModel.calculatorState.value.heightCmInput)
      assertEquals("70.25", viewModel.calculatorState.value.weightInput)
      assertEquals("28", viewModel.calculatorState.value.ageInput)
      assertEquals(CalculateOutcome.NAVIGATE_DIRECTLY, viewModel.calculateAndPrepareResultWithAdCheck())
      advanceUntilIdle()
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
