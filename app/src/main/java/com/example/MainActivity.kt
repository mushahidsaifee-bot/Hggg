package com.example

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography as MaterialTypography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.zIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// region Application, Activity & Navigation Shell

class BmiApplication : Application() {
  lateinit var container: AppContainer
    private set

  override fun onCreate() {
    super.onCreate()
    container = DefaultAppContainer(this)
  }
}

interface AppContainer {
  val bmiRepository: BmiRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
  private val database: BmiDatabase by lazy { BmiDatabase.getInstance(context) }
  private val preferencesDataStore: UserPreferencesDataStore by lazy {
    UserPreferencesDataStore(context)
  }

  override val bmiRepository: BmiRepository by lazy {
    LocalBmiRepository(
      bmiRecordDao = database.bmiRecordDao(),
      preferencesDataStore = preferencesDataStore,
    )
  }
}

class MainActivity : ComponentActivity() {
  private var startIoAdManager: StartIoAdManager? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val appContainer = (application as BmiApplication).container
    val initialTheme = appContainer.bmiRepository.initialThemeMode
    val isInitiallyDark =
      when (initialTheme) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM ->
          (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
      }
    window.setBackgroundDrawableResource(
      if (isInitiallyDark) R.color.window_background_dark else R.color.window_background_light
    )
    val adManager = StartIoAdManager(this).also { startIoAdManager = it }

    setContent {
      val bmiViewModel: BmiViewModel =
        viewModel(factory = BmiViewModel.provideFactory(appContainer.bmiRepository))
      val preferences by bmiViewModel.userPreferences.collectAsStateWithLifecycle()

      LaunchedEffect(adManager, preferences.calculationCount) {
        delay(600L)
        adManager.preloadInterstitialIfApproaching(preferences.calculationCount)
      }

      CompositionLocalProvider(LocalStartIoAdManager provides adManager) {
        BmiCalculatorTheme(themeMode = preferences.themeMode) {
          BmiAppShell(viewModel = bmiViewModel)
        }
      }
    }
  }

  override fun onDestroy() {
    startIoAdManager?.destroy()
    startIoAdManager = null
    super.onDestroy()
  }
}

object BmiRoutes {
  const val MAIN_TABS = "main_tabs"
  const val RESULT = "result"
}

private enum class TopLevelTab(
  val labelRes: Int,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String,
) {
  CALCULATOR(
    labelRes = R.string.nav_calculator,
    selectedIcon = Icons.Filled.Calculate,
    unselectedIcon = Icons.Outlined.Calculate,
    testTag = "nav_calculator",
  ),
  HISTORY(
    labelRes = R.string.nav_history,
    selectedIcon = Icons.Filled.History,
    unselectedIcon = Icons.Outlined.History,
    testTag = "nav_history",
  ),
  SETTINGS(
    labelRes = R.string.nav_settings,
    selectedIcon = Icons.Filled.Settings,
    unselectedIcon = Icons.Outlined.Settings,
    testTag = "nav_settings",
  ),
}

@Composable
fun BmiAppShell(
  viewModel: BmiViewModel,
  modifier: Modifier = Modifier,
) {
  val navController = rememberNavController()

  var selectedTab by rememberSaveable { mutableStateOf(TopLevelTab.CALCULATOR) }

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  val adManager = LocalStartIoAdManager.current

  fun selectTab(tab: TopLevelTab) {
    if (selectedTab != tab) {
      snackbarHostState.currentSnackbarData?.dismiss()
      selectedTab = tab
    }
  }

  fun navigateToResultSingleTop() {
    if (navController.currentBackStackEntry?.destination?.route != BmiRoutes.RESULT) {
      snackbarHostState.currentSnackbarData?.dismiss()
      navController.navigate(BmiRoutes.RESULT) { launchSingleTop = true }
    }
  }

  NavHost(
    navController = navController,
    startDestination = BmiRoutes.MAIN_TABS,
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
    modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
  ) {
    composable(BmiRoutes.MAIN_TABS) {
      BackHandler(enabled = selectedTab != TopLevelTab.CALCULATOR) {
        selectTab(TopLevelTab.CALCULATOR)
      }

      var isMainTabsAtTop by remember { mutableStateOf(true) }
      LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
          isMainTabsAtTop = entry.destination.route == BmiRoutes.MAIN_TABS
        }
      }

      BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
          containerColor = MaterialTheme.colorScheme.background,
          contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
          snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
          bottomBar = {
            if (!useNavigationRail) {
              NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
                TopLevelTab.entries.forEach { tab ->
                  val selected = selectedTab == tab
                  NavigationBarItem(
                    selected = selected,
                    onClick = { selectTab(tab) },
                    icon = {
                      Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = stringResource(tab.labelRes),
                      )
                    },
                    label = { Text(stringResource(tab.labelRes)) },
                    modifier = Modifier.testTag(tab.testTag),
                  )
                }
              }
            }
          },
        ) { innerPadding ->
          Row(
            modifier =
              Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding)
          ) {
            if (useNavigationRail) {
              NavigationRail(containerColor = MaterialTheme.colorScheme.background) {
                TopLevelTab.entries.forEach { tab ->
                  val selected = selectedTab == tab
                  NavigationRailItem(
                    selected = selected,
                    onClick = { selectTab(tab) },
                    icon = {
                      Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = stringResource(tab.labelRes),
                      )
                    },
                    label = { Text(stringResource(tab.labelRes)) },
                    modifier = Modifier.testTag(tab.testTag),
                  )
                }
              }
            }

            Box(
              modifier =
                Modifier.weight(1f)
                  .fillMaxSize()
                  .clipToBounds()
                  .background(MaterialTheme.colorScheme.background),
            ) {
              TopLevelTab.entries.forEach { tab ->
                key(tab) {
                  val isSelected = selectedTab == tab
                  val isSettledActive = isMainTabsAtTop && isSelected

                  val tabModifier =
                    Modifier.fillMaxSize()
                      .zIndex(if (isSelected) 1f else 0f)
                      .graphicsLayer {
                        if (isSelected) {
                          alpha = 1f
                          translationX = 0f
                        } else {
                          alpha = 0f
                          translationX = 100_000f
                        }
                      }
                      .let { base -> if (!isSelected) base.clearAndSetSemantics {} else base }

                  Box(modifier = tabModifier) {
                    when (tab) {
                      TopLevelTab.CALCULATOR -> {
                        val calculatorState by
                          viewModel.calculatorState.collectAsStateWithLifecycle()
                        CalculatorScreen(
                          state = calculatorState,
                          onUnitSystemSelected = viewModel::onUnitSystemSelected,
                          onHeightCmChanged = viewModel::onHeightCmChanged,
                          onFeetChanged = viewModel::onFeetChanged,
                          onInchesChanged = viewModel::onInchesChanged,
                          onWeightChanged = viewModel::onWeightChanged,
                          onAgeChanged = viewModel::onAgeChanged,
                          onGenderSelected = viewModel::onGenderSelected,
                          onCalculateClicked = {
                            if (
                              navController.currentBackStackEntry?.destination?.route !=
                                BmiRoutes.MAIN_TABS
                            ) {
                              return@CalculatorScreen
                            }
                            when (viewModel.calculateAndPrepareResultWithAdCheck()) {
                              CalculateOutcome.INVALID -> Unit
                              CalculateOutcome.NAVIGATE_DIRECTLY -> {
                                adManager?.preloadInterstitialIfApproaching(
                                  viewModel.latestCalculationCount()
                                )
                                navigateToResultSingleTop()
                              }
                              CalculateOutcome.SHOW_INTERSTITIAL -> {
                                if (adManager != null) {
                                  adManager.showInterstitialIfReady {
                                    navigateToResultSingleTop()
                                  }
                                } else {
                                  navigateToResultSingleTop()
                                }
                              }
                            }
                          },
                          onResetClicked = viewModel::resetCalculator,
                        )
                      }

                      TopLevelTab.HISTORY -> {
                        val historyRecords by
                          viewModel.historyRecords.collectAsStateWithLifecycle()
                        HistoryScreen(
                          records = historyRecords,
                          isTabActive = isSettledActive,
                          onRecordClicked = { record ->
                            if (
                              navController.currentBackStackEntry?.destination?.route ==
                                BmiRoutes.MAIN_TABS
                            ) {
                              viewModel.openHistoryRecord(record)
                              navigateToResultSingleTop()
                            }
                          },
                          onDeleteRecord = { recordId ->
                            viewModel.deleteRecord(recordId)
                            scope.launch {
                              snackbarHostState.showSnackbar(
                                context.getString(R.string.message_record_deleted)
                              )
                            }
                          },
                          onClearAll = {
                            viewModel.clearAllHistory()
                            scope.launch {
                              snackbarHostState.showSnackbar(
                                context.getString(R.string.message_history_cleared)
                              )
                            }
                          },
                          onGoToCalculator = { selectTab(TopLevelTab.CALCULATOR) },
                        )
                      }

                      TopLevelTab.SETTINGS -> {
                        val preferences by
                          viewModel.userPreferences.collectAsStateWithLifecycle()
                        SettingsScreen(
                          preferences = preferences,
                          onThemeSelected = viewModel::setThemeMode,
                          onDefaultUnitSelected = viewModel::setDefaultUnitSystem,
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    composable(BmiRoutes.RESULT) {
      val resultState by viewModel.resultState.collectAsStateWithLifecycle()
      Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      ) { innerPadding ->
        ResultScreen(
          state = resultState,
          modifier = Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
          onSaveClicked = {
            viewModel.saveCurrentResult()
            scope.launch {
              snackbarHostState.showSnackbar(context.getString(R.string.message_result_saved))
            }
          },
          onRecalculateClicked = {
            if (resultState.isFromHistory) {
              selectTab(TopLevelTab.CALCULATOR)
            }
            if (navController.currentBackStackEntry?.destination?.route == BmiRoutes.RESULT) {
              navController.popBackStack()
            }
          },
          onDeleteRecordClicked = { recordId ->
            viewModel.deleteRecord(recordId)
            if (navController.currentBackStackEntry?.destination?.route == BmiRoutes.RESULT) {
              navController.popBackStack()
            }
            scope.launch {
              snackbarHostState.showSnackbar(context.getString(R.string.message_record_deleted))
            }
          },
          onBackClicked = {
            if (navController.currentBackStackEntry?.destination?.route == BmiRoutes.RESULT) {
              navController.popBackStack()
            }
          },
        )
      }
    }
  }
}

// endregion

// region Domain Models, Calculator & Formatters

enum class UnitSystem {
  METRIC,
  IMPERIAL,
}

enum class GenderOption {
  UNSPECIFIED,
  MALE,
  FEMALE,
}

enum class ThemeMode {
  SYSTEM,
  LIGHT,
  DARK,
}

enum class BmiCategory {
  UNDERWEIGHT,
  NORMAL,
  OVERWEIGHT,
  OBESITY_CLASS_1,
  OBESITY_CLASS_2,
  OBESITY_CLASS_3,
}

data class BmiCalculation(
  val bmi: Double,
  val category: BmiCategory,
  val unitSystem: UnitSystem,
  val heightCm: Double,
  val weightKg: Double,
  val feet: Double,
  val inches: Double,
  val weightLb: Double,
  val healthyMinKg: Double,
  val healthyMaxKg: Double,
  val healthyMinLb: Double,
  val healthyMaxLb: Double,
  val age: Int?,
  val gender: GenderOption,
  val timestampMillis: Long,
  val savedRecordId: Long? = null,
)

sealed class HeightValidationError {
  data object Required : HeightValidationError()

  data object InvalidNumber : HeightValidationError()

  data object OutOfRangeMetric : HeightValidationError()

  data object OutOfRangeImperial : HeightValidationError()

  data object InvalidInches : HeightValidationError()
}

sealed class WeightValidationError {
  data object Required : WeightValidationError()

  data object InvalidNumber : WeightValidationError()

  data object OutOfRangeMetric : WeightValidationError()

  data object OutOfRangeImperial : WeightValidationError()
}

sealed class AgeValidationError {
  data object InvalidNumber : AgeValidationError()

  data object OutOfRange : AgeValidationError()
}

data class BmiValidationResult(
  val heightError: HeightValidationError? = null,
  val inchesError: HeightValidationError? = null,
  val weightError: WeightValidationError? = null,
  val ageError: AgeValidationError? = null,
  val calculation: BmiCalculation? = null,
) {
  val isValid: Boolean
    get() =
      heightError == null &&
        inchesError == null &&
        weightError == null &&
        ageError == null &&
        calculation != null
}

object BmiCalculator {
  const val MIN_HEIGHT_CM = 50.0
  const val MAX_HEIGHT_CM = 280.0
  const val MIN_HEIGHT_IN = 20.0
  const val MAX_HEIGHT_IN = 110.0
  const val MIN_WEIGHT_KG = 10.0
  const val MAX_WEIGHT_KG = 500.0
  const val MIN_WEIGHT_LB = 22.0
  const val MAX_WEIGHT_LB = 1100.0
  const val MIN_AGE = 2
  const val MAX_AGE = 120

  fun calculateMetricBmi(heightCm: Double, weightKg: Double): Double {
    val numerator = BigDecimal.valueOf(weightKg).multiply(BigDecimal("10000"))
    val denominator = BigDecimal.valueOf(heightCm).pow(2)
    return numerator.divide(denominator, 12, RoundingMode.HALF_UP).toDouble()
  }

  fun calculateImperialBmi(heightInches: Double, weightLb: Double): Double {
    val numerator = BigDecimal.valueOf(weightLb).multiply(BigDecimal("703"))
    val denominator = BigDecimal.valueOf(heightInches).pow(2)
    return numerator.divide(denominator, 12, RoundingMode.HALF_UP).toDouble()
  }

  fun categorize(bmi: Double): BmiCategory {
    return when {
      bmi < 18.5 -> BmiCategory.UNDERWEIGHT
      bmi < 25.0 -> BmiCategory.NORMAL
      bmi < 30.0 -> BmiCategory.OVERWEIGHT
      bmi < 35.0 -> BmiCategory.OBESITY_CLASS_1
      bmi < 40.0 -> BmiCategory.OBESITY_CLASS_2
      else -> BmiCategory.OBESITY_CLASS_3
    }
  }

  fun healthyWeightRangeKg(heightCm: Double): Pair<Double, Double> {
    val cm2 = BigDecimal.valueOf(heightCm).pow(2)
    val divisor = BigDecimal("10000")
    val minKg = BigDecimal("18.5").multiply(cm2).divide(divisor, 2, RoundingMode.CEILING)
    val maxBoundary = BigDecimal("25.0").multiply(cm2).divide(divisor)
    var maxKg = maxBoundary.setScale(2, RoundingMode.FLOOR)
    if (maxKg.compareTo(maxBoundary) >= 0) {
      maxKg = maxKg.subtract(BigDecimal("0.01"))
    }
    if (maxKg.compareTo(minKg) < 0) {
      maxKg = minKg
    }
    return minKg.toDouble() to maxKg.toDouble()
  }

  fun healthyWeightRangeLb(heightInches: Double): Pair<Double, Double> {
    val in2 = BigDecimal.valueOf(heightInches).pow(2)
    val factor = BigDecimal("703")
    val minLb = BigDecimal("18.5").multiply(in2).divide(factor, 2, RoundingMode.CEILING)
    val maxNumerator = BigDecimal("25.0").multiply(in2)
    var maxLb = maxNumerator.divide(factor, 2, RoundingMode.FLOOR)
    if (maxLb.multiply(factor).compareTo(maxNumerator) >= 0) {
      maxLb = maxLb.subtract(BigDecimal("0.01"))
    }
    if (maxLb.compareTo(minLb) < 0) {
      maxLb = minLb
    }
    return minLb.toDouble() to maxLb.toDouble()
  }

  fun cmToFeetInches(cm: Double): Pair<Double, Double> {
    val totalInches =
      BigDecimal.valueOf(cm).divide(BigDecimal("2.54"), 12, RoundingMode.HALF_UP)
    val twelve = BigDecimal("12")
    var feet = totalInches.divide(twelve, 0, RoundingMode.FLOOR)
    var inches = totalInches.subtract(feet.multiply(twelve)).coerceAtLeast(BigDecimal.ZERO)
    if (inches >= twelve) {
      feet = feet.add(BigDecimal.ONE)
      inches = inches.subtract(twelve)
    }
    return feet.toDouble() to inches.toDouble()
  }

  fun feetInchesToTotalInches(feet: Double, inches: Double): Double =
    BigDecimal.valueOf(feet).multiply(BigDecimal("12")).add(BigDecimal.valueOf(inches)).toDouble()

  fun feetInchesToCm(feet: Double, inches: Double): Double {
    val totalInches =
      BigDecimal.valueOf(feet).multiply(BigDecimal("12")).add(BigDecimal.valueOf(inches))
    return totalInches.multiply(BigDecimal("2.54")).toDouble()
  }

  fun kgToLb(kg: Double): Double =
    BigDecimal.valueOf(kg).divide(BigDecimal("0.45359237"), 12, RoundingMode.HALF_UP).toDouble()

  fun lbToKg(lb: Double): Double =
    BigDecimal.valueOf(lb).multiply(BigDecimal("0.45359237")).toDouble()

  fun validateAndCalculate(
    unitSystem: UnitSystem,
    heightCmText: String,
    feetText: String,
    inchesText: String,
    weightText: String,
    ageText: String,
    gender: GenderOption,
    timestampMillis: Long = System.currentTimeMillis(),
  ): BmiValidationResult {
    var heightError: HeightValidationError? = null
    var inchesError: HeightValidationError? = null
    var weightError: WeightValidationError? = null
    var ageError: AgeValidationError? = null

    var heightCm = 0.0
    var feet = 0.0
    var inches = 0.0

    when (unitSystem) {
      UnitSystem.METRIC -> {
        val trimmed = heightCmText.trim()
        if (trimmed.isEmpty()) {
          heightError = HeightValidationError.Required
        } else {
          val parsed = trimmed.toDoubleOrNull()
          if (parsed == null || parsed.isNaN() || parsed.isInfinite()) {
            heightError = HeightValidationError.InvalidNumber
          } else if (parsed < MIN_HEIGHT_CM || parsed > MAX_HEIGHT_CM) {
            heightError = HeightValidationError.OutOfRangeMetric
          } else {
            heightCm = parsed
            val converted = cmToFeetInches(heightCm)
            feet = converted.first
            inches = converted.second
          }
        }
      }
      UnitSystem.IMPERIAL -> {
        val trimmedFeet = feetText.trim()
        val trimmedInches = inchesText.trim()
        if (trimmedFeet.isEmpty() && trimmedInches.isEmpty()) {
          heightError = HeightValidationError.Required
        } else {
          val parsedFeet = if (trimmedFeet.isEmpty()) 0.0 else trimmedFeet.toDoubleOrNull()
          val parsedInches = if (trimmedInches.isEmpty()) 0.0 else trimmedInches.toDoubleOrNull()

          if (
            parsedFeet == null || parsedFeet.isNaN() || parsedFeet.isInfinite() || parsedFeet < 0.0
          ) {
            heightError = HeightValidationError.InvalidNumber
          }
          if (parsedInches == null || parsedInches.isNaN() || parsedInches.isInfinite()) {
            inchesError = HeightValidationError.InvalidNumber
          } else if (parsedInches < 0.0 || parsedInches >= 12.0) {
            inchesError = HeightValidationError.InvalidInches
          }

          if (
            heightError == null &&
              inchesError == null &&
              parsedFeet != null &&
              parsedInches != null
          ) {
            val totalInches = feetInchesToTotalInches(parsedFeet, parsedInches)
            if (totalInches < MIN_HEIGHT_IN || totalInches > MAX_HEIGHT_IN) {
              heightError = HeightValidationError.OutOfRangeImperial
            } else {
              feet = parsedFeet
              inches = parsedInches
              heightCm = feetInchesToCm(feet, inches)
            }
          }
        }
      }
    }

    var weightKg = 0.0
    var weightLb = 0.0
    val trimmedWeight = weightText.trim()
    if (trimmedWeight.isEmpty()) {
      weightError = WeightValidationError.Required
    } else {
      val parsedWeight = trimmedWeight.toDoubleOrNull()
      if (parsedWeight == null || parsedWeight.isNaN() || parsedWeight.isInfinite()) {
        weightError = WeightValidationError.InvalidNumber
      } else if (unitSystem == UnitSystem.METRIC) {
        if (parsedWeight < MIN_WEIGHT_KG || parsedWeight > MAX_WEIGHT_KG) {
          weightError = WeightValidationError.OutOfRangeMetric
        } else {
          weightKg = parsedWeight
          weightLb = kgToLb(weightKg)
        }
      } else {
        if (parsedWeight < MIN_WEIGHT_LB || parsedWeight > MAX_WEIGHT_LB) {
          weightError = WeightValidationError.OutOfRangeImperial
        } else {
          weightLb = parsedWeight
          weightKg = lbToKg(weightLb)
        }
      }
    }

    var age: Int? = null
    val trimmedAge = ageText.trim()
    if (trimmedAge.isNotEmpty()) {
      val parsedAge = trimmedAge.toIntOrNull()
      if (parsedAge == null) {
        ageError = AgeValidationError.InvalidNumber
      } else if (parsedAge < MIN_AGE || parsedAge > MAX_AGE) {
        ageError = AgeValidationError.OutOfRange
      } else {
        age = parsedAge
      }
    }

    if (heightError != null || inchesError != null || weightError != null || ageError != null) {
      return BmiValidationResult(
        heightError = heightError,
        inchesError = inchesError,
        weightError = weightError,
        ageError = ageError,
        calculation = null,
      )
    }

    val totalInches = feetInchesToTotalInches(feet, inches)
    val bmi =
      if (unitSystem == UnitSystem.METRIC) {
        calculateMetricBmi(heightCm, weightKg)
      } else {
        calculateImperialBmi(totalInches, weightLb)
      }
    val category = categorize(bmi)
    val (minKg, maxKg) = healthyWeightRangeKg(heightCm)
    val (minLb, maxLb) = healthyWeightRangeLb(totalInches)

    return BmiValidationResult(
      calculation =
        BmiCalculation(
          bmi = bmi,
          category = category,
          unitSystem = unitSystem,
          heightCm = heightCm,
          weightKg = weightKg,
          feet = feet,
          inches = inches,
          weightLb = weightLb,
          healthyMinKg = minKg,
          healthyMaxKg = maxKg,
          healthyMinLb = minLb,
          healthyMaxLb = maxLb,
          age = age,
          gender = gender,
          timestampMillis = timestampMillis,
        )
    )
  }
}

object BmiFormatters {
  fun formatBmi(bmi: Double): String {
    if (!bmi.isFinite()) return "0.00"
    return BigDecimal.valueOf(bmi).setScale(2, RoundingMode.HALF_UP).toPlainString()
  }

  fun formatDecimal(value: Double): String {
    if (!value.isFinite()) return "0"
    val scaled = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP)
    if (scaled.compareTo(BigDecimal.ZERO) == 0) return "0"
    return scaled.stripTrailingZeros().toPlainString()
  }

  fun formatHeight(calc: BmiCalculation): String =
    when (calc.unitSystem) {
      UnitSystem.METRIC -> "${formatDecimal(calc.heightCm)} cm"
      UnitSystem.IMPERIAL -> "${formatDecimal(calc.feet)} ft ${formatDecimal(calc.inches)} in"
    }

  fun formatWeight(calc: BmiCalculation): String =
    when (calc.unitSystem) {
      UnitSystem.METRIC -> "${formatDecimal(calc.weightKg)} kg"
      UnitSystem.IMPERIAL -> "${formatDecimal(calc.weightLb)} lb"
    }

  fun formatHealthyWeightRange(calc: BmiCalculation): String =
    when (calc.unitSystem) {
      UnitSystem.METRIC ->
        "${formatDecimal(calc.healthyMinKg)} – ${formatDecimal(calc.healthyMaxKg)} kg"
      UnitSystem.IMPERIAL ->
        "${formatDecimal(calc.healthyMinLb)} – ${formatDecimal(calc.healthyMaxLb)} lb"
    }

  fun formatWeightSuggestion(context: Context, calc: BmiCalculation): String {
    val (current, min, max, unit) =
      when (calc.unitSystem) {
        UnitSystem.METRIC ->
          Quadruple(calc.weightKg, calc.healthyMinKg, calc.healthyMaxKg, "kg")
        UnitSystem.IMPERIAL ->
          Quadruple(calc.weightLb, calc.healthyMinLb, calc.healthyMaxLb, "lb")
      }
    return when {
      calc.bmi < 18.5 -> {
        val gain =
          BigDecimal.valueOf(min)
            .subtract(BigDecimal.valueOf(current))
            .setScale(2, RoundingMode.CEILING)
            .toDouble()
            .coerceAtLeast(0.01)
        context.getString(R.string.healthy_weight_gain_format, "${formatDecimal(gain)} $unit")
      }
      calc.bmi >= 25.0 -> {
        val lose =
          BigDecimal.valueOf(current)
            .subtract(BigDecimal.valueOf(max))
            .setScale(2, RoundingMode.CEILING)
            .toDouble()
            .coerceAtLeast(0.01)
        context.getString(R.string.healthy_weight_lose_format, "${formatDecimal(lose)} $unit")
      }
      else -> context.getString(R.string.healthy_weight_status_normal)
    }
  }

  private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

  private val cachedTimestampFormatter =
    object : ThreadLocal<Pair<Locale, SimpleDateFormat>>() {}

  fun formatTimestamp(timestampMillis: Long): String {
    val locale = Locale.getDefault()
    val existing = cachedTimestampFormatter.get()
    val sdf =
      if (existing != null && existing.first == locale) {
        existing.second
      } else {
        SimpleDateFormat("MMM d, yyyy · h:mm a", locale).also {
          cachedTimestampFormatter.set(locale to it)
        }
      }
    return sdf.format(Date(timestampMillis))
  }

  fun categoryLabel(context: Context, category: BmiCategory): String =
    when (category) {
      BmiCategory.UNDERWEIGHT -> context.getString(R.string.category_underweight)
      BmiCategory.NORMAL -> context.getString(R.string.category_normal)
      BmiCategory.OVERWEIGHT -> context.getString(R.string.category_overweight)
      BmiCategory.OBESITY_CLASS_1 -> context.getString(R.string.category_obesity_class_1)
      BmiCategory.OBESITY_CLASS_2 -> context.getString(R.string.category_obesity_class_2)
      BmiCategory.OBESITY_CLASS_3 -> context.getString(R.string.category_obesity_class_3)
    }

  fun genderLabel(context: Context, gender: GenderOption): String =
    when (gender) {
      GenderOption.UNSPECIFIED -> context.getString(R.string.gender_unspecified)
      GenderOption.MALE -> context.getString(R.string.gender_male)
      GenderOption.FEMALE -> context.getString(R.string.gender_female)
    }

  fun heightErrorMessage(context: Context, error: HeightValidationError?): String? =
    when (error) {
      null -> null
      HeightValidationError.Required -> context.getString(R.string.error_height_required)
      HeightValidationError.InvalidNumber -> context.getString(R.string.error_invalid_number)
      HeightValidationError.OutOfRangeMetric ->
        context.getString(R.string.error_height_range_metric)
      HeightValidationError.OutOfRangeImperial ->
        context.getString(R.string.error_height_range_imperial)
      HeightValidationError.InvalidInches -> context.getString(R.string.error_inches_range)
    }

  fun weightErrorMessage(context: Context, error: WeightValidationError?): String? =
    when (error) {
      null -> null
      WeightValidationError.Required -> context.getString(R.string.error_weight_required)
      WeightValidationError.InvalidNumber -> context.getString(R.string.error_invalid_number)
      WeightValidationError.OutOfRangeMetric ->
        context.getString(R.string.error_weight_range_metric)
      WeightValidationError.OutOfRangeImperial ->
        context.getString(R.string.error_weight_range_imperial)
    }

  fun ageErrorMessage(context: Context, error: AgeValidationError?): String? =
    when (error) {
      null -> null
      AgeValidationError.InvalidNumber -> context.getString(R.string.error_invalid_number)
      AgeValidationError.OutOfRange -> context.getString(R.string.error_age_range)
    }
}

// endregion

// region Room Database, DataStore & Repository

@Entity(tableName = "bmi_records")
data class BmiRecordEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val bmi: Double,
  val category: String,
  val unitSystem: String,
  val heightCm: Double,
  val weightKg: Double,
  val feet: Double,
  val inches: Double,
  val weightLb: Double,
  val age: Int?,
  val gender: String,
  val timestampMillis: Long,
) {
  fun toBmiCalculation(): BmiCalculation {
    val parsedUnit =
      runCatching { UnitSystem.valueOf(unitSystem) }.getOrDefault(UnitSystem.METRIC)
    val parsedCategory =
      runCatching { BmiCategory.valueOf(category) }
        .getOrDefault(BmiCalculator.categorize(bmi))
    val parsedGender =
      runCatching { GenderOption.valueOf(gender) }.getOrDefault(GenderOption.UNSPECIFIED)
    val totalInches = BmiCalculator.feetInchesToTotalInches(feet, inches)
    val (minKg, maxKg) = BmiCalculator.healthyWeightRangeKg(heightCm)
    val (minLb, maxLb) =
      if (totalInches > 0.0) BmiCalculator.healthyWeightRangeLb(totalInches)
      else BmiCalculator.kgToLb(minKg) to BmiCalculator.kgToLb(maxKg)

    return BmiCalculation(
      bmi = bmi,
      category = parsedCategory,
      unitSystem = parsedUnit,
      heightCm = heightCm,
      weightKg = weightKg,
      feet = feet,
      inches = inches,
      weightLb = weightLb,
      healthyMinKg = minKg,
      healthyMaxKg = maxKg,
      healthyMinLb = minLb,
      healthyMaxLb = maxLb,
      age = age,
      gender = parsedGender,
      timestampMillis = timestampMillis,
      savedRecordId = id,
    )
  }

  companion object {
    fun fromCalculation(calc: BmiCalculation): BmiRecordEntity =
      BmiRecordEntity(
        bmi = calc.bmi,
        category = calc.category.name,
        unitSystem = calc.unitSystem.name,
        heightCm = calc.heightCm,
        weightKg = calc.weightKg,
        feet = calc.feet,
        inches = calc.inches,
        weightLb = calc.weightLb,
        age = calc.age,
        gender = calc.gender.name,
        timestampMillis = calc.timestampMillis,
      )
  }
}

@Dao
interface BmiRecordDao {
  @Query("SELECT * FROM bmi_records ORDER BY timestampMillis DESC, id DESC")
  fun getAllRecords(): Flow<List<BmiRecordEntity>>

  @Query("SELECT * FROM bmi_records WHERE id = :id")
  fun getRecordById(id: Long): Flow<BmiRecordEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecord(record: BmiRecordEntity): Long

  @Query("DELETE FROM bmi_records WHERE id = :id")
  suspend fun deleteRecordById(id: Long)

  @Query("DELETE FROM bmi_records")
  suspend fun deleteAllRecords()
}

@Database(entities = [BmiRecordEntity::class], version = 2, exportSchema = false)
abstract class BmiDatabase : RoomDatabase() {
  abstract fun bmiRecordDao(): BmiRecordDao

  companion object {
    @Volatile private var INSTANCE: BmiDatabase? = null

    private val MIGRATION_1_2 =
      object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
          db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `bmi_records_new` (
              `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
              `bmi` REAL NOT NULL,
              `category` TEXT NOT NULL,
              `unitSystem` TEXT NOT NULL,
              `heightCm` REAL NOT NULL,
              `weightKg` REAL NOT NULL,
              `feet` REAL NOT NULL,
              `inches` REAL NOT NULL,
              `weightLb` REAL NOT NULL,
              `age` INTEGER,
              `gender` TEXT NOT NULL,
              `timestampMillis` INTEGER NOT NULL
            )
            """.trimIndent()
          )
          db.execSQL(
            """
            INSERT INTO `bmi_records_new` (
              `id`, `bmi`, `category`, `unitSystem`, `heightCm`, `weightKg`,
              `feet`, `inches`, `weightLb`, `age`, `gender`, `timestampMillis`
            )
            SELECT
              `id`, `bmi`, `category`, `unitSystem`, `heightCm`, `weightKg`,
              CAST(`feet` AS REAL), `inches`, `weightLb`, `age`, `gender`, `timestampMillis`
            FROM `bmi_records`
            """.trimIndent()
          )
          db.execSQL("DROP TABLE `bmi_records`")
          db.execSQL("ALTER TABLE `bmi_records_new` RENAME TO `bmi_records`")
        }
      }

    fun getInstance(context: Context): BmiDatabase {
      return INSTANCE
        ?: synchronized(this) {
          INSTANCE
            ?: Room.databaseBuilder(
                context.applicationContext,
                BmiDatabase::class.java,
                "bmi_calculator_db",
              )
              .addMigrations(MIGRATION_1_2)
              .build()
              .also { INSTANCE = it }
        }
    }
  }
}

private val Context.userPreferencesDataStore: DataStore<Preferences> by
  preferencesDataStore(name = "bmi_user_preferences")

data class UserPreferences(
  val themeMode: ThemeMode = ThemeMode.LIGHT,
  val defaultUnitSystem: UnitSystem = UnitSystem.METRIC,
  val calculationCount: Int = 0,
)

class UserPreferencesDataStore(private val context: Context) {

  private object Keys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val DEFAULT_UNIT_SYSTEM = stringPreferencesKey("default_unit_system")
    val CALCULATION_COUNT = intPreferencesKey("bmi_calculation_count")
  }

  private val bootPrefs by lazy {
    context.applicationContext.getSharedPreferences("bmi_boot_cache", Context.MODE_PRIVATE)
  }

  fun getCachedThemeMode(): ThemeMode {
    val raw = bootPrefs.getString("theme_mode", null) ?: return ThemeMode.LIGHT
    return runCatching { ThemeMode.valueOf(raw) }.getOrDefault(ThemeMode.LIGHT)
  }

  private fun cacheThemeMode(themeMode: ThemeMode) {
    bootPrefs.edit().putString("theme_mode", themeMode.name).apply()
  }

  val preferencesFlow: Flow<UserPreferences> =
    context.userPreferencesDataStore.data.map { prefs ->
      val theme =
        prefs[Keys.THEME_MODE]?.let { raw ->
          runCatching { ThemeMode.valueOf(raw) }.getOrNull()
        } ?: ThemeMode.LIGHT

      val unit =
        prefs[Keys.DEFAULT_UNIT_SYSTEM]?.let { raw ->
          runCatching { UnitSystem.valueOf(raw) }.getOrNull()
        } ?: UnitSystem.METRIC

      val calcCount = (prefs[Keys.CALCULATION_COUNT] ?: 0).coerceAtLeast(0)

      UserPreferences(
        themeMode = theme,
        defaultUnitSystem = unit,
        calculationCount = calcCount,
      )
    }

  suspend fun setThemeMode(themeMode: ThemeMode) {
    cacheThemeMode(themeMode)
    context.userPreferencesDataStore.edit { prefs -> prefs[Keys.THEME_MODE] = themeMode.name }
  }

  suspend fun setDefaultUnitSystem(unitSystem: UnitSystem) {
    context.userPreferencesDataStore.edit { prefs ->
      prefs[Keys.DEFAULT_UNIT_SYSTEM] = unitSystem.name
    }
  }

  suspend fun setCalculationCount(count: Int) {
    context.userPreferencesDataStore.edit { prefs ->
      prefs[Keys.CALCULATION_COUNT] = count.coerceAtLeast(0)
    }
  }
}

interface BmiRepository {
  val initialThemeMode: ThemeMode
    get() = ThemeMode.LIGHT

  val allRecords: Flow<List<BmiRecordEntity>>
  val userPreferences: Flow<UserPreferences>

  fun getRecordById(id: Long): Flow<BmiRecordEntity?>

  suspend fun saveCalculation(calculation: BmiCalculation): Long

  suspend fun deleteRecordById(id: Long)

  suspend fun clearAllRecords()

  suspend fun setThemeMode(themeMode: ThemeMode)

  suspend fun setDefaultUnitSystem(unitSystem: UnitSystem)

  suspend fun setCalculationCount(count: Int)
}

class LocalBmiRepository(
  private val bmiRecordDao: BmiRecordDao,
  private val preferencesDataStore: UserPreferencesDataStore,
) : BmiRepository {

  override val initialThemeMode: ThemeMode by lazy { preferencesDataStore.getCachedThemeMode() }

  override val allRecords: Flow<List<BmiRecordEntity>> = bmiRecordDao.getAllRecords()

  override val userPreferences: Flow<UserPreferences> = preferencesDataStore.preferencesFlow

  override fun getRecordById(id: Long): Flow<BmiRecordEntity?> = bmiRecordDao.getRecordById(id)

  override suspend fun saveCalculation(calculation: BmiCalculation): Long {
    return bmiRecordDao.insertRecord(BmiRecordEntity.fromCalculation(calculation))
  }

  override suspend fun deleteRecordById(id: Long) {
    bmiRecordDao.deleteRecordById(id)
  }

  override suspend fun clearAllRecords() {
    bmiRecordDao.deleteAllRecords()
  }

  override suspend fun setThemeMode(themeMode: ThemeMode) {
    preferencesDataStore.setThemeMode(themeMode)
  }

  override suspend fun setDefaultUnitSystem(unitSystem: UnitSystem) {
    preferencesDataStore.setDefaultUnitSystem(unitSystem)
  }

  override suspend fun setCalculationCount(count: Int) {
    preferencesDataStore.setCalculationCount(count)
  }
}

// endregion

// region ViewModel & UI States

data class CalculatorUiState(
  val unitSystem: UnitSystem = UnitSystem.METRIC,
  val heightCmInput: String = "",
  val feetInput: String = "",
  val inchesInput: String = "",
  val weightInput: String = "",
  val ageInput: String = "",
  val gender: GenderOption = GenderOption.UNSPECIFIED,
  val showErrors: Boolean = false,
  val heightError: HeightValidationError? = null,
  val inchesError: HeightValidationError? = null,
  val weightError: WeightValidationError? = null,
  val ageError: AgeValidationError? = null,
) {
  val hasAnyInput: Boolean
    get() =
      heightCmInput.isNotBlank() ||
        feetInput.isNotBlank() ||
        inchesInput.isNotBlank() ||
        weightInput.isNotBlank() ||
        ageInput.isNotBlank() ||
        gender != GenderOption.UNSPECIFIED
}

data class ResultUiState(
  val calculation: BmiCalculation? = null,
  val isFromHistory: Boolean = false,
  val isSaved: Boolean = false,
)

enum class CalculateOutcome {
  INVALID,
  NAVIGATE_DIRECTLY,
  SHOW_INTERSTITIAL,
}

class BmiViewModel(private val repository: BmiRepository) : ViewModel() {

  private val _calculatorState = MutableStateFlow(CalculatorUiState())
  val calculatorState: StateFlow<CalculatorUiState> = _calculatorState.asStateFlow()

  private val _resultState = MutableStateFlow(ResultUiState())
  val resultState: StateFlow<ResultUiState> = _resultState.asStateFlow()

  val historyRecords: StateFlow<List<BmiRecordEntity>> =
    repository.allRecords.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList(),
    )

  val userPreferences: StateFlow<UserPreferences> =
    repository.userPreferences.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = UserPreferences(themeMode = repository.initialThemeMode),
    )

  private var hasInitializedUnitFromPrefs = false
  private var currentCalculationCount = 0

  init {
    viewModelScope.launch {
      repository.userPreferences.collect { prefs ->
        currentCalculationCount = maxOf(currentCalculationCount, prefs.calculationCount)
        if (!hasInitializedUnitFromPrefs) {
          hasInitializedUnitFromPrefs = true
          if (!_calculatorState.value.hasAnyInput) {
            _calculatorState.update { it.copy(unitSystem = prefs.defaultUnitSystem) }
          }
        }
      }
    }
  }

  fun latestCalculationCount(): Int = currentCalculationCount

  fun onUnitSystemSelected(newUnit: UnitSystem) {
    val current = _calculatorState.value
    if (current.unitSystem == newUnit) return

    var newHeightCm = current.heightCmInput
    var newFeet = current.feetInput
    var newInches = current.inchesInput
    var newWeight = current.weightInput

    if (current.unitSystem == UnitSystem.METRIC && newUnit == UnitSystem.IMPERIAL) {
      val cm = current.heightCmInput.trim().toDoubleOrNull()
      if (cm != null && cm in BmiCalculator.MIN_HEIGHT_CM..BmiCalculator.MAX_HEIGHT_CM) {
        val (rawFt, rawInch) = BmiCalculator.cmToFeetInches(cm)
        var ft = rawFt
        var roundedInch =
          BigDecimal.valueOf(rawInch).setScale(2, RoundingMode.HALF_UP).toDouble()
        if (roundedInch >= 12.0) {
          ft += 1.0
          roundedInch = 0.0
        }
        newFeet = BmiFormatters.formatDecimal(ft)
        newInches = BmiFormatters.formatDecimal(roundedInch)
      } else {
        newFeet = ""
        newInches = ""
      }
      val kg = current.weightInput.trim().toDoubleOrNull()
      newWeight =
        if (kg != null && kg in BmiCalculator.MIN_WEIGHT_KG..BmiCalculator.MAX_WEIGHT_KG) {
          BmiFormatters.formatDecimal(BmiCalculator.kgToLb(kg))
        } else {
          ""
        }
    } else if (current.unitSystem == UnitSystem.IMPERIAL && newUnit == UnitSystem.METRIC) {
      val ft =
        if (current.feetInput.isBlank()) 0.0 else current.feetInput.trim().toDoubleOrNull()
      val inch =
        if (current.inchesInput.isBlank()) 0.0 else current.inchesInput.trim().toDoubleOrNull()
      if (
        (current.feetInput.isNotBlank() || current.inchesInput.isNotBlank()) &&
          ft != null &&
          !ft.isNaN() &&
          !ft.isInfinite() &&
          ft >= 0.0 &&
          inch != null &&
          !inch.isNaN() &&
          !inch.isInfinite() &&
          inch >= 0.0 &&
          inch < 12.0
      ) {
        val totalIn = ft * 12.0 + inch
        newHeightCm =
          if (totalIn in BmiCalculator.MIN_HEIGHT_IN..BmiCalculator.MAX_HEIGHT_IN) {
            BmiFormatters.formatDecimal(BmiCalculator.feetInchesToCm(ft, inch))
          } else {
            ""
          }
      } else {
        newHeightCm = ""
      }
      val lb = current.weightInput.trim().toDoubleOrNull()
      newWeight =
        if (lb != null && lb in BmiCalculator.MIN_WEIGHT_LB..BmiCalculator.MAX_WEIGHT_LB) {
          BmiFormatters.formatDecimal(BmiCalculator.lbToKg(lb))
        } else {
          ""
        }
    }

    updateCalculatorInputs(
      current.copy(
        unitSystem = newUnit,
        heightCmInput = newHeightCm,
        feetInput = newFeet,
        inchesInput = newInches,
        weightInput = newWeight,
        showErrors = false,
      )
    )
  }

  fun onHeightCmChanged(value: String) {
    val filtered = sanitizeDecimalInput(value)
    updateCalculatorInputs(_calculatorState.value.copy(heightCmInput = filtered))
  }

  fun onFeetChanged(value: String) {
    val filtered = sanitizeDecimalInput(value)
    updateCalculatorInputs(_calculatorState.value.copy(feetInput = filtered))
  }

  fun onInchesChanged(value: String) {
    val filtered = sanitizeDecimalInput(value)
    updateCalculatorInputs(_calculatorState.value.copy(inchesInput = filtered))
  }

  fun onWeightChanged(value: String) {
    val filtered = sanitizeDecimalInput(value)
    updateCalculatorInputs(_calculatorState.value.copy(weightInput = filtered))
  }

  fun onAgeChanged(value: String) {
    val filtered = value.filter { it.isDigit() }.take(3)
    updateCalculatorInputs(_calculatorState.value.copy(ageInput = filtered))
  }

  fun onGenderSelected(gender: GenderOption) {
    val nextGender =
      if (_calculatorState.value.gender == gender) GenderOption.UNSPECIFIED else gender
    updateCalculatorInputs(_calculatorState.value.copy(gender = nextGender))
  }

  fun resetCalculator() {
    val defaultUnit = userPreferences.value.defaultUnitSystem
    _calculatorState.value = CalculatorUiState(unitSystem = defaultUnit)
  }

  fun calculateAndPrepareResultWithAdCheck(): CalculateOutcome {
    val state = _calculatorState.value
    val validation =
      BmiCalculator.validateAndCalculate(
        unitSystem = state.unitSystem,
        heightCmText = state.heightCmInput,
        feetText = state.feetInput,
        inchesText = state.inchesInput,
        weightText = state.weightInput,
        ageText = state.ageInput,
        gender = state.gender,
      )

    if (!validation.isValid || validation.calculation == null) {
      _calculatorState.update {
        it.copy(
          showErrors = true,
          heightError = validation.heightError,
          inchesError = validation.inchesError,
          weightError = validation.weightError,
          ageError = validation.ageError,
        )
      }
      return CalculateOutcome.INVALID
    }

    _calculatorState.update {
      it.copy(
        showErrors = false,
        heightError = null,
        inchesError = null,
        weightError = null,
        ageError = null,
      )
    }

    _resultState.value =
      ResultUiState(
        calculation = validation.calculation,
        isFromHistory = false,
        isSaved = false,
      )

    currentCalculationCount += 1
    val updatedCount = currentCalculationCount
    viewModelScope.launch { repository.setCalculationCount(updatedCount) }

    return if (updatedCount > 0 && updatedCount % 8 == 0) {
      CalculateOutcome.SHOW_INTERSTITIAL
    } else {
      CalculateOutcome.NAVIGATE_DIRECTLY
    }
  }

  fun openHistoryRecord(record: BmiRecordEntity) {
    _resultState.value =
      ResultUiState(
        calculation = record.toBmiCalculation(),
        isFromHistory = true,
        isSaved = true,
      )
  }

  fun saveCurrentResult() {
    val currentResult = _resultState.value
    val calculation = currentResult.calculation ?: return
    if (currentResult.isSaved) return

    // Guard against rapid double-taps inserting duplicate records before DB completion.
    _resultState.update { it.copy(isSaved = true) }

    viewModelScope.launch {
      runCatching { repository.saveCalculation(calculation) }
        .onSuccess { id ->
          _resultState.update { state ->
            if (state.calculation == calculation) {
              state.copy(calculation = calculation.copy(savedRecordId = id), isSaved = true)
            } else {
              state
            }
          }
        }
        .onFailure {
          _resultState.update { state ->
            if (state.calculation == calculation) state.copy(isSaved = false) else state
          }
        }
    }
  }

  fun deleteRecord(id: Long) {
    viewModelScope.launch {
      repository.deleteRecordById(id)
      _resultState.update { state ->
        val calc = state.calculation
        if (calc?.savedRecordId == id) {
          state.copy(calculation = calc.copy(savedRecordId = null), isSaved = false)
        } else {
          state
        }
      }
    }
  }

  fun clearAllHistory() {
    viewModelScope.launch {
      repository.clearAllRecords()
      _resultState.update { state ->
        val calc = state.calculation
        if (calc != null && state.isSaved) {
          state.copy(calculation = calc.copy(savedRecordId = null), isSaved = false)
        } else {
          state
        }
      }
    }
  }

  fun setThemeMode(themeMode: ThemeMode) {
    viewModelScope.launch { repository.setThemeMode(themeMode) }
  }

  fun setDefaultUnitSystem(unitSystem: UnitSystem) {
    viewModelScope.launch {
      repository.setDefaultUnitSystem(unitSystem)
      if (!_calculatorState.value.hasAnyInput) {
        _calculatorState.update { it.copy(unitSystem = unitSystem) }
      }
    }
  }

  private fun updateCalculatorInputs(updated: CalculatorUiState) {
    if (!updated.showErrors) {
      _calculatorState.value =
        updated.copy(
          heightError = null,
          inchesError = null,
          weightError = null,
          ageError = null,
        )
      return
    }
    val validation =
      BmiCalculator.validateAndCalculate(
        unitSystem = updated.unitSystem,
        heightCmText = updated.heightCmInput,
        feetText = updated.feetInput,
        inchesText = updated.inchesInput,
        weightText = updated.weightInput,
        ageText = updated.ageInput,
        gender = updated.gender,
      )
    _calculatorState.value =
      updated.copy(
        heightError = validation.heightError,
        inchesError = validation.inchesError,
        weightError = validation.weightError,
        ageError = validation.ageError,
      )
  }

  private fun sanitizeDecimalInput(raw: String): String {
    var dotSeen = false
    val sb = StringBuilder()
    for (ch in raw) {
      if (ch.isDigit()) {
        sb.append(ch)
      } else if ((ch == '.' || ch == ',') && !dotSeen) {
        dotSeen = true
        sb.append('.')
      }
    }
    return sb.toString().take(8)
  }

  companion object {
    fun provideFactory(repository: BmiRepository): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return BmiViewModel(repository) as T
        }
      }
  }
}

// endregion

// region Material 3 Theme & Colors

val TealPrimaryLight = Color(0xFF0D7A82)
val TealOnPrimaryLight = Color(0xFFFFFFFF)
val TealPrimaryContainerLight = Color(0xFFD2F1EE)
val TealOnPrimaryContainerLight = Color(0xFF063E44)

val SlateSecondaryLight = Color(0xFF3E6368)
val SlateOnSecondaryLight = Color(0xFFFFFFFF)
val SlateSecondaryContainerLight = Color(0xFFD9F0EE)
val SlateOnSecondaryContainerLight = Color(0xFF0B3B40)

val SkyTertiaryLight = Color(0xFF2B6CB0)
val SkyOnTertiaryLight = Color(0xFFFFFFFF)
val SkyTertiaryContainerLight = Color(0xFFDFEEFF)
val SkyOnTertiaryContainerLight = Color(0xFF0E3561)

val BackgroundLight = Color(0xFFF3FAF9)
val OnBackgroundLight = Color(0xFF0F1E22)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFFFFFFF)
val SurfaceContainerLight = Color(0xFFEAF5F4)
val OnSurfaceLight = Color(0xFF0F1E22)
val OnSurfaceVariantLight = Color(0xFF4B6368)
val OutlineLight = Color(0xFFAED0CE)
val OutlineVariantLight = Color(0xFFD3E7E5)
val ErrorLight = Color(0xFFDC2626)

val TealPrimaryDark = Color(0xFF4FD1C5)
val TealOnPrimaryDark = Color(0xFF003738)
val TealPrimaryContainerDark = Color(0xFF113F42)
val TealOnPrimaryContainerDark = Color(0xFFB2F5EA)

val SlateSecondaryDark = Color(0xFF94A3B8)
val SlateOnSecondaryDark = Color(0xFF0F172A)
val SlateSecondaryContainerDark = Color(0xFF1E262C)
val SlateOnSecondaryContainerDark = Color(0xFFE2E8F0)

val SkyTertiaryDark = Color(0xFF7DD3FC)
val SkyOnTertiaryDark = Color(0xFF082F49)
val SkyTertiaryContainerDark = Color(0xFF162B3A)
val SkyOnTertiaryContainerDark = Color(0xFFBAE6FD)

val BackgroundDark = Color(0xFF101417)
val OnBackgroundDark = Color(0xFFF1F5F9)
val SurfaceDark = Color(0xFF101417)
val SurfaceVariantDark = Color(0xFF171C20)
val OnSurfaceDark = Color(0xFFF1F5F9)
val OnSurfaceVariantDark = Color(0xFF94A3B8)
val OutlineDark = Color(0xFF334155)
val OutlineVariantDark = Color(0xFF232B32)
val ErrorDark = Color(0xFFF87171)

val BmiUnderweightLight = Color(0xFF0284C7)
val BmiNormalLight = Color(0xFF0D9488)
val BmiOverweightLight = Color(0xFFD97706)
val BmiObesityClass1Light = Color(0xFFEA580C)
val BmiObesityClass2Light = Color(0xFFDC2626)
val BmiObesityClass3Light = Color(0xFF991B1B)

val BmiUnderweightDark = Color(0xFF38BDF8)
val BmiNormalDark = Color(0xFF2DD4BF)
val BmiOverweightDark = Color(0xFFFBBF24)
val BmiObesityClass1Dark = Color(0xFFFB923C)
val BmiObesityClass2Dark = Color(0xFFF87171)
val BmiObesityClass3Dark = Color(0xFFF43F5E)

val AppTypography =
  MaterialTypography(
    displaySmall =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
      ),
    headlineSmall =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
      ),
    titleLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
      ),
    titleMedium =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp,
      ),
    titleSmall =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
      ),
    bodyLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp,
      ),
    bodyMedium =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
      ),
    bodySmall =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp,
      ),
    labelLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
      ),
    labelMedium =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
      ),
    labelSmall =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp,
      ),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TealPrimaryLight,
    onPrimary = TealOnPrimaryLight,
    primaryContainer = TealPrimaryContainerLight,
    onPrimaryContainer = TealOnPrimaryContainerLight,
    secondary = SlateSecondaryLight,
    onSecondary = SlateOnSecondaryLight,
    secondaryContainer = SlateSecondaryContainerLight,
    onSecondaryContainer = SlateOnSecondaryContainerLight,
    tertiary = SkyTertiaryLight,
    onTertiary = SkyOnTertiaryLight,
    tertiaryContainer = SkyTertiaryContainerLight,
    onTertiaryContainer = SkyOnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceContainerLow = SurfaceVariantLight,
    surfaceContainer = SurfaceContainerLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = ErrorLight,
  )

private val DarkColorScheme =
  darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = TealOnPrimaryDark,
    primaryContainer = TealPrimaryContainerDark,
    onPrimaryContainer = TealOnPrimaryContainerDark,
    secondary = SlateSecondaryDark,
    onSecondary = SlateOnSecondaryDark,
    secondaryContainer = SlateSecondaryContainerDark,
    onSecondaryContainer = SlateOnSecondaryContainerDark,
    tertiary = SkyTertiaryDark,
    onTertiary = SkyOnTertiaryDark,
    tertiaryContainer = SkyTertiaryContainerDark,
    onTertiaryContainer = SkyOnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceContainerLow = SurfaceVariantDark,
    surfaceContainer = SurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorDark,
  )

@Immutable
data class BmiCategoryColors(
  val underweight: Color,
  val normal: Color,
  val overweight: Color,
  val obesityClass1: Color,
  val obesityClass2: Color,
  val obesityClass3: Color,
) {
  fun colorFor(category: BmiCategory): Color =
    when (category) {
      BmiCategory.UNDERWEIGHT -> underweight
      BmiCategory.NORMAL -> normal
      BmiCategory.OVERWEIGHT -> overweight
      BmiCategory.OBESITY_CLASS_1 -> obesityClass1
      BmiCategory.OBESITY_CLASS_2 -> obesityClass2
      BmiCategory.OBESITY_CLASS_3 -> obesityClass3
    }
}

val LocalBmiCategoryColors = staticCompositionLocalOf {
  BmiCategoryColors(
    underweight = BmiUnderweightLight,
    normal = BmiNormalLight,
    overweight = BmiOverweightLight,
    obesityClass1 = BmiObesityClass1Light,
    obesityClass2 = BmiObesityClass2Light,
    obesityClass3 = BmiObesityClass3Light,
  )
}

private val AppShapes =
  Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
  )

@Composable
fun BmiCalculatorTheme(
  themeMode: ThemeMode = ThemeMode.LIGHT,
  content: @Composable () -> Unit,
) {
  val isDark =
    when (themeMode) {
      ThemeMode.SYSTEM -> isSystemInDarkTheme()
      ThemeMode.LIGHT -> false
      ThemeMode.DARK -> true
    }

  val view = LocalView.current
  if (!view.isInEditMode) {
    LaunchedEffect(isDark) {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        WindowCompat.getInsetsController(window, view).apply {
          isAppearanceLightStatusBars = !isDark
          isAppearanceLightNavigationBars = !isDark
        }
        window.setBackgroundDrawableResource(
          if (isDark) R.color.window_background_dark else R.color.window_background_light
        )
      }
    }
  }

  val colorScheme = if (isDark) DarkColorScheme else LightColorScheme
  val categoryColors =
    if (isDark) {
      BmiCategoryColors(
        underweight = BmiUnderweightDark,
        normal = BmiNormalDark,
        overweight = BmiOverweightDark,
        obesityClass1 = BmiObesityClass1Dark,
        obesityClass2 = BmiObesityClass2Dark,
        obesityClass3 = BmiObesityClass3Dark,
      )
    } else {
      BmiCategoryColors(
        underweight = BmiUnderweightLight,
        normal = BmiNormalLight,
        overweight = BmiOverweightLight,
        obesityClass1 = BmiObesityClass1Light,
        obesityClass2 = BmiObesityClass2Light,
        obesityClass3 = BmiObesityClass3Light,
      )
    }

  CompositionLocalProvider(LocalBmiCategoryColors provides categoryColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      shapes = AppShapes,
      content = content,
    )
  }
}

// endregion

// region Start.io Ad Manager & Banner Composable

val LocalStartIoAdManager = staticCompositionLocalOf<StartIoAdManager?> { null }

class BannerSlot(
  val container: FrameLayout,
  val banner: Banner,
) {
  @Volatile
  var hasLoadedOnce: Boolean = false
    internal set

  @Volatile
  var hasRequestedLoad: Boolean = false
    internal set

  @Volatile
  var requestedWidthDp: Int = 320
    internal set

  private val _isLoaded = MutableStateFlow(false)
  val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

  private val _hasFailedWithoutLoad = MutableStateFlow(false)
  val hasFailedWithoutLoad: StateFlow<Boolean> = _hasFailedWithoutLoad.asStateFlow()

  internal fun markLoaded() {
    hasLoadedOnce = true
    _hasFailedWithoutLoad.value = false
    _isLoaded.value = true
    container.visibility = View.VISIBLE
    banner.visibility = View.VISIBLE
    runCatching { banner.showBanner() }
  }

  internal fun markFailed() {
    // Preserve already-loaded banners so they never disappear on background refresh failures.
    if (!hasLoadedOnce) {
      _hasFailedWithoutLoad.value = true
      container.visibility = View.GONE
    }
  }
}

class StartIoAdManager(private val activity: Activity) {

  private val applicationContext: Context = activity.applicationContext
  private val mainHandler = Handler(Looper.getMainLooper())
  private val bannerSlots = LinkedHashMap<String, BannerSlot>()
  private val pendingSdkReadyCallbacks = ArrayList<() -> Unit>()

  private val _isNetworkOnline = MutableStateFlow(false)
  val isNetworkOnline: StateFlow<Boolean> = _isNetworkOnline.asStateFlow()

  private var interstitialAd: StartAppAd? = null
  private var isInterstitialLoaded = false
  private var isInterstitialLoading = false
  private var isSdkInitializing = false
  private var isDestroyed = false

  private var connectivityManager: ConnectivityManager? = null
  private var networkCallback: ConnectivityManager.NetworkCallback? = null

  init {
    if (!isRobolectricEnvironment()) {
      _isNetworkOnline.value = checkValidatedNetwork(applicationContext)
      registerNetworkObserver()
    }
  }

  private fun registerNetworkObserver() {
    val cm =
      applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: return
    connectivityManager = cm

    val callback =
      object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
          updateConnectivityAsync()
        }

        override fun onLost(network: Network) {
          updateConnectivityAsync()
        }

        override fun onCapabilitiesChanged(
          network: Network,
          networkCapabilities: NetworkCapabilities,
        ) {
          updateConnectivityAsync()
        }
      }

    networkCallback = callback
    val request =
      NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    runCatching { cm.registerNetworkCallback(request, callback) }
  }

  private fun updateConnectivityAsync() {
    mainHandler.post {
      if (isDestroyed) return@post
      val online = checkValidatedNetwork(applicationContext)
      val wasOffline = !_isNetworkOnline.value
      _isNetworkOnline.value = online
      if (online && wasOffline) {
        retryUnloadedBanners()
      }
    }
  }

  private fun withSdkReady(action: () -> Unit) {
    if (isDestroyed || isRobolectricEnvironment() || !_isNetworkOnline.value) return
    if (isSdkInitialized) {
      action()
      return
    }

    pendingSdkReadyCallbacks.add(action)
    if (isSdkInitializing) return

    val appId =
      BuildConfig.STARTIO_APP_ID.trim().ifEmpty {
        activity.getString(R.string.startio_app_id).trim()
      }
    if (appId.isEmpty()) {
      pendingSdkReadyCallbacks.clear()
      return
    }

    isSdkInitializing = true
    runCatching {
      StartAppAd.disableSplash()
      StartAppSDK.initParams(activity.application, appId)
        .setReturnAdsEnabled(false)
        .setCallback {
          mainHandler.post {
            isSdkInitializing = false
            isSdkInitialized = true
            if (BuildConfig.DEBUG) {
              runCatching { StartAppSDK.setTestAdsEnabled(true) }
            }
            val callbacks = pendingSdkReadyCallbacks.toList()
            pendingSdkReadyCallbacks.clear()
            if (!isDestroyed) {
              callbacks.forEach { it() }
            }
          }
        }
        .init()
    }.onFailure {
      isSdkInitializing = false
      pendingSdkReadyCallbacks.clear()
    }
  }

  private fun retryUnloadedBanners() {
    if (!_isNetworkOnline.value || isDestroyed) return
    bannerSlots.values.forEach { slot ->
      ensureBannerLoaded(slot)
    }
  }

  fun ensureBannerLoaded(slot: BannerSlot) {
    if (isDestroyed || isRobolectricEnvironment() || !_isNetworkOnline.value) return
    if (slot.hasLoadedOnce || slot.hasRequestedLoad) return

    withSdkReady {
      slot.container.post {
        if (
          !isDestroyed &&
            !slot.hasLoadedOnce &&
            !slot.hasRequestedLoad &&
            _isNetworkOnline.value &&
            slot.container.isAttachedToWindow
        ) {
          slot.hasRequestedLoad = true
          runCatching { slot.banner.loadAd(slot.requestedWidthDp, 50) }
            .onFailure { slot.markFailed() }
        }
      }
    }
  }

  fun preloadInterstitialIfApproaching(calculationCount: Int) {
    // Preload when approaching the 8th calculation (6th or 7th) to avoid unnecessary startup WebViews.
    val mod = calculationCount % 8
    if (mod >= 6) {
      ensureInterstitialPreloaded()
    }
  }

  fun ensureInterstitialPreloaded() {
    if (isDestroyed || isRobolectricEnvironment() || !_isNetworkOnline.value) return
    if (isInterstitialLoaded || isInterstitialLoading) return

    withSdkReady {
      if (isDestroyed || isInterstitialLoaded || isInterstitialLoading || !_isNetworkOnline.value) {
        return@withSdkReady
      }
      isInterstitialLoading = true
      runCatching {
        val ad = StartAppAd(activity)
        interstitialAd = ad
        ad.loadAd(
          object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
              if (isDestroyed) return
              isInterstitialLoading = false
              isInterstitialLoaded = true
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
              if (isDestroyed) return
              isInterstitialLoading = false
              isInterstitialLoaded = false
            }
          }
        )
      }.onFailure {
        isInterstitialLoading = false
        isInterstitialLoaded = false
      }
    }
  }

  fun showInterstitialIfReady(onComplete: () -> Unit) {
    val completed = AtomicBoolean(false)
    fun finishOnce() {
      if (completed.compareAndSet(false, true)) {
        mainHandler.post { onComplete() }
      }
    }

    if (isDestroyed || isRobolectricEnvironment() || !isSdkInitialized) {
      finishOnce()
      return
    }

    val adToShow = interstitialAd
    if (!isInterstitialLoaded || adToShow == null) {
      finishOnce()
      return
    }

    interstitialAd = null
    isInterstitialLoaded = false

    val fallbackTimeout = Runnable { finishOnce() }
    mainHandler.postDelayed(fallbackTimeout, 1_500L)

    val shown =
      runCatching {
          adToShow.showAd(
            object : AdDisplayListener {
              override fun adHidden(ad: Ad) {
                mainHandler.removeCallbacks(fallbackTimeout)
                finishOnce()
              }

              override fun adDisplayed(ad: Ad) {
                mainHandler.removeCallbacks(fallbackTimeout)
              }

              override fun adClicked(ad: Ad) = Unit

              override fun adNotDisplayed(ad: Ad) {
                mainHandler.removeCallbacks(fallbackTimeout)
                finishOnce()
              }
            }
          )
        }
        .getOrDefault(false)

    if (!shown) {
      mainHandler.removeCallbacks(fallbackTimeout)
      finishOnce()
    }
  }

  fun getOrCreateBannerSlot(slotKey: String, widthDp: Int): BannerSlot? {
    bannerSlots[slotKey]?.let { existing ->
      existing.requestedWidthDp = widthDp.coerceIn(250, 320)
      return existing
    }

    if (isDestroyed || isRobolectricEnvironment() || !_isNetworkOnline.value) {
      return null
    }

    val container =
      FrameLayout(activity).apply {
        visibility = View.GONE
        layoutParams =
          ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
          )
      }

    lateinit var slot: BannerSlot

    val banner =
      Banner(
        activity,
        object : BannerListener {
          override fun onReceiveAd(view: View) {
            if (!isDestroyed) {
              mainHandler.post { slot.markLoaded() }
            }
          }

          override fun onFailedToReceiveAd(view: View) {
            if (!isDestroyed) {
              mainHandler.post { slot.markFailed() }
            }
          }

          override fun onImpression(view: View) = Unit

          override fun onClick(view: View) = Unit
        },
      )

    val bannerParams =
      FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
        Gravity.CENTER_HORIZONTAL,
      )
    container.addView(banner, bannerParams)

    slot = BannerSlot(container = container, banner = banner)
    slot.requestedWidthDp = widthDp.coerceIn(250, 320)
    bannerSlots[slotKey] = slot

    ensureBannerLoaded(slot)
    return slot
  }

  fun getExistingSlot(slotKey: String): BannerSlot? = bannerSlots[slotKey]

  fun destroy() {
    isDestroyed = true
    mainHandler.removeCallbacksAndMessages(null)
    pendingSdkReadyCallbacks.clear()
    interstitialAd = null
    isInterstitialLoaded = false
    isInterstitialLoading = false

    val callback = networkCallback
    if (callback != null) {
      runCatching { connectivityManager?.unregisterNetworkCallback(callback) }
      networkCallback = null
    }
    connectivityManager = null

    bannerSlots.values.forEach { slot ->
      runCatching {
        slot.banner.hideBanner()
        (slot.container.parent as? ViewGroup)?.removeView(slot.container)
        destroyChildWebViews(slot.container)
        slot.container.removeAllViews()
      }
    }
    bannerSlots.clear()
  }

  private fun destroyChildWebViews(view: View) {
    if (view is WebView) {
      runCatching {
        view.stopLoading()
        view.onPause()
        view.removeAllViews()
        view.destroy()
      }
    } else if (view is ViewGroup) {
      for (i in 0 until view.childCount) {
        destroyChildWebViews(view.getChildAt(i))
      }
    }
  }

  companion object {
    @Volatile
    var isSdkInitialized: Boolean = false
      private set

    fun isRobolectricEnvironment(): Boolean {
      return Build.FINGERPRINT.equals("robolectric", ignoreCase = true)
    }

    fun checkValidatedNetwork(context: Context): Boolean {
      val cm =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
          ?: return false
      val network = cm.activeNetwork ?: return false
      val capabilities = cm.getNetworkCapabilities(network) ?: return false
      return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
  }
}

@Composable
fun StartIoBannerAd(
  slotKey: String,
  modifier: Modifier = Modifier,
  topSpacingWhenLoaded: Dp = 0.dp,
  viewportBounds: Rect? = null,
  isActive: Boolean = true,
) {
  val adManager = LocalStartIoAdManager.current
  if (adManager == null || StartIoAdManager.isRobolectricEnvironment()) {
    return
  }

  val isNetworkOnline by adManager.isNetworkOnline.collectAsStateWithLifecycle()
  val existingSlot = remember(adManager, slotKey) { adManager.getExistingSlot(slotKey) }
  var isFailedWithoutLoad by remember(slotKey) {
    mutableStateOf(existingSlot?.hasFailedWithoutLoad?.value == true)
  }
  val alreadyLoaded = existingSlot?.hasLoadedOnce == true

  // Collapse cleanly if offline (and not yet loaded) or initial load failed.
  if ((isFailedWithoutLoad && !alreadyLoaded) || (!isNetworkOnline && !alreadyLoaded)) {
    return
  }

  val density = LocalDensity.current
  val bannerHeightPx = remember(density) { with(density) { 50.dp.toPx() } }

  var isUnclippedInViewport by remember(slotKey) {
    mutableStateOf(viewportBounds == null || alreadyLoaded)
  }
  var isSlotLoaded by remember(slotKey) { mutableStateOf(alreadyLoaded) }
  var hasSettledForAd by remember(slotKey) { mutableStateOf(alreadyLoaded) }

  LaunchedEffect(isActive) {
    if (isActive && !hasSettledForAd) {
      withFrameNanos {}
      hasSettledForAd = true
    }
  }

  BoxWithConstraints(
    modifier =
      modifier
        .fillMaxWidth()
        .padding(top = if (isSlotLoaded) topSpacingWhenLoaded else 0.dp)
        .heightIn(min = if (isSlotLoaded) 50.dp else 1.dp)
        .onGloballyPositioned { coords ->
          if (!isUnclippedInViewport) {
            val vp = viewportBounds
            if (vp == null) {
              isUnclippedInViewport = true
            } else {
              val pos = coords.positionInWindow()
              val slotTop = pos.y
              val slotBottom = pos.y + bannerHeightPx
              if (slotTop >= vp.top && slotBottom <= vp.bottom && coords.size.width > 0) {
                isUnclippedInViewport = true
              }
            }
          }
        },
    contentAlignment = Alignment.Center,
  ) {
    val availableWidthDp = maxWidth.value.toInt().coerceIn(250, 320)

    if ((!isUnclippedInViewport || !hasSettledForAd) && !alreadyLoaded) {
      return@BoxWithConstraints
    }

    val slot =
      remember(adManager, slotKey, isNetworkOnline, isUnclippedInViewport, hasSettledForAd) {
        adManager.getOrCreateBannerSlot(slotKey, availableWidthDp)
      } ?: return@BoxWithConstraints

    val isLoaded by slot.isLoaded.collectAsStateWithLifecycle()
    val hasFailed by slot.hasFailedWithoutLoad.collectAsStateWithLifecycle()

    LaunchedEffect(hasFailed, isLoaded) {
      isSlotLoaded = isLoaded
      isFailedWithoutLoad = hasFailed && !isLoaded
    }

    LaunchedEffect(isNetworkOnline, isUnclippedInViewport, hasSettledForAd, slot) {
      if (isNetworkOnline && isUnclippedInViewport && hasSettledForAd) {
        adManager.ensureBannerLoaded(slot)
      }
    }

    if ((hasFailed && !isLoaded) || (!isNetworkOnline && !isLoaded)) {
      return@BoxWithConstraints
    }

    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val shouldShowNativeBanner = isActive && lifecycleState.isAtLeast(Lifecycle.State.RESUMED)

    Box(
      modifier = Modifier.fillMaxWidth().wrapContentHeight(),
      contentAlignment = Alignment.Center,
    ) {
      AndroidView(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        factory = {
          (slot.container.parent as? ViewGroup)?.removeView(slot.container)
          slot.container
        },
        onRelease = { view ->
          (view.parent as? ViewGroup)?.removeView(view)
        },
        update = {
          if (slot.hasLoadedOnce) {
            // Hide native WebView immediately when navigating away so it cannot bleed over transitions.
            slot.container.visibility = if (shouldShowNativeBanner) View.VISIBLE else View.GONE
            slot.banner.visibility = if (shouldShowNativeBanner) View.VISIBLE else View.GONE
            if (shouldShowNativeBanner) {
              runCatching { slot.banner.showBanner() }
            }
          } else {
            slot.container.visibility = View.GONE
          }
        },
      )
    }
  }
}

// endregion

// region Shared UI Components & Dialogs

@Composable
fun FlatSectionCard(
  modifier: Modifier = Modifier,
  containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
  borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
  content: @Composable () -> Unit,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    color = containerColor,
    border = BorderStroke(1.dp, borderColor),
    tonalElevation = 0.dp,
    shadowElevation = 0.dp,
  ) {
    content()
  }
}

@Composable
fun BmiCategoryBadge(
  category: BmiCategory,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val categoryColor = LocalBmiCategoryColors.current.colorFor(category)
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(8.dp),
    color = categoryColor.copy(alpha = 0.14f),
    border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.35f)),
  ) {
    Text(
      text = BmiFormatters.categoryLabel(context, category),
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.SemiBold,
      color = categoryColor,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BmiRangeIndicator(
  bmi: Double,
  category: BmiCategory,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val categoryColors = LocalBmiCategoryColors.current
  val activeColor = categoryColors.colorFor(category)
  val markerRingColor = MaterialTheme.colorScheme.surface
  val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
  val scaleDescription =
    "${stringResource(R.string.bmi_scale_label)}: ${BmiFormatters.formatBmi(bmi)}, " +
      BmiFormatters.categoryLabel(context, category)

  val minScale = 12.0f
  val maxScale = 45.0f
  val span = maxScale - minScale
  val clampedBmi = bmi.toFloat().coerceIn(minScale, maxScale)
  val targetFraction = (clampedBmi - minScale) / span
  val markerFraction by
    animateFloatAsState(
      targetValue = targetFraction,
      animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
      label = "bmi_marker_fraction",
    )

  Column(
    modifier =
      modifier.fillMaxWidth().semantics(mergeDescendants = true) {
        contentDescription = scaleDescription
      }
  ) {
    Canvas(modifier = Modifier.fillMaxWidth().height(28.dp)) {
      val barHeight = 10.dp.toPx()
      val barTop = (size.height - barHeight) / 2f
      val gap = 2.5.dp.toPx()
      val corner = CornerRadius(4.dp.toPx(), 4.dp.toPx())

      val f1 = (18.5f - minScale) / span
      val f2 = (25.0f - minScale) / span
      val f3 = (30.0f - minScale) / span
      val f4 = (35.0f - minScale) / span
      val f5 = (40.0f - minScale) / span

      val x0 = 0f
      val x1 = size.width * f1
      val x2 = size.width * f2
      val x3 = size.width * f3
      val x4 = size.width * f4
      val x5 = size.width * f5
      val x6 = size.width

      drawRoundRect(
        color = categoryColors.underweight,
        topLeft = Offset(x0, barTop),
        size = Size((x1 - x0 - gap / 2f).coerceAtLeast(0f), barHeight),
        cornerRadius = corner,
      )
      drawRoundRect(
        color = categoryColors.normal,
        topLeft = Offset(x1 + gap / 2f, barTop),
        size = Size((x2 - x1 - gap).coerceAtLeast(0f), barHeight),
        cornerRadius = corner,
      )
      drawRoundRect(
        color = categoryColors.overweight,
        topLeft = Offset(x2 + gap / 2f, barTop),
        size = Size((x3 - x2 - gap).coerceAtLeast(0f), barHeight),
        cornerRadius = corner,
      )
      drawRoundRect(
        color = categoryColors.obesityClass1,
        topLeft = Offset(x3 + gap / 2f, barTop),
        size = Size((x4 - x3 - gap).coerceAtLeast(0f), barHeight),
        cornerRadius = corner,
      )
      drawRoundRect(
        color = categoryColors.obesityClass2,
        topLeft = Offset(x4 + gap / 2f, barTop),
        size = Size((x5 - x4 - gap).coerceAtLeast(0f), barHeight),
        cornerRadius = corner,
      )
      drawRoundRect(
        color = categoryColors.obesityClass3,
        topLeft = Offset(x5 + gap / 2f, barTop),
        size = Size((x6 - x5 - gap / 2f).coerceAtLeast(0f), barHeight),
        cornerRadius = corner,
      )

      val indicatorX =
        (markerFraction * size.width).coerceIn(8.dp.toPx(), size.width - 8.dp.toPx())
      val centerY = size.height / 2f
      drawCircle(
        color = markerRingColor,
        radius = 9.dp.toPx(),
        center = Offset(indicatorX, centerY),
      )
      drawCircle(
        color = activeColor,
        radius = 6.dp.toPx(),
        center = Offset(indicatorX, centerY),
      )
    }

    val tickFractions = remember {
      listOf(
        0f,
        (18.5f - minScale) / span,
        (25.0f - minScale) / span,
        (30.0f - minScale) / span,
        (35.0f - minScale) / span,
        (40.0f - minScale) / span,
        1f,
      )
    }
    Layout(
      modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
      content = {
        Text("12", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
        Text("18.5", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
        Text("25", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
        Text("30", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
        Text("35", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
        Text("40", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
        Text("45", style = MaterialTheme.typography.labelSmall, color = labelColor, maxLines = 1)
      },
    ) { measurables, constraints ->
      val childConstraints = constraints.copy(minWidth = 0)
      val placeables = measurables.map { it.measure(childConstraints) }
      val maxHeight = placeables.maxOfOrNull { it.height } ?: 0
      val totalWidth = constraints.maxWidth
      val minGapPx = 4.dp.roundToPx()
      val endLabelStart = (totalWidth - (placeables.lastOrNull()?.width ?: 0)).coerceAtLeast(0)
      var lastRight = Int.MIN_VALUE
      layout(totalWidth, maxHeight) {
        placeables.forEachIndexed { index, placeable ->
          val fraction = tickFractions[index]
          val rawX =
            when (index) {
              0 -> 0
              placeables.lastIndex -> endLabelStart
              else ->
                ((totalWidth * fraction) - (placeable.width / 2f))
                  .roundToInt()
                  .coerceIn(0, (totalWidth - placeable.width).coerceAtLeast(0))
            }
          when (index) {
            0 -> {
              if (placeable.width <= totalWidth) {
                placeable.placeRelative(rawX, 0)
                lastRight = rawX + placeable.width
              }
            }
            placeables.lastIndex -> {
              if (rawX >= lastRight + minGapPx) {
                placeable.placeRelative(rawX, 0)
              }
            }
            else -> {
              if (rawX >= lastRight + minGapPx && rawX + placeable.width + minGapPx <= endLabelStart) {
                placeable.placeRelative(rawX, 0)
                lastRight = rawX + placeable.width
              }
            }
          }
        }
      }
    }

    FlowRow(
      modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      ScaleLegendDot(
        color = categoryColors.underweight,
        label = stringResource(R.string.category_underweight),
      )
      ScaleLegendDot(
        color = categoryColors.normal,
        label = stringResource(R.string.category_normal),
      )
      ScaleLegendDot(
        color = categoryColors.overweight,
        label = stringResource(R.string.category_overweight),
      )
      ScaleLegendDot(
        color = categoryColors.obesityClass1,
        label = stringResource(R.string.category_obesity_class_1),
      )
      ScaleLegendDot(
        color = categoryColors.obesityClass2,
        label = stringResource(R.string.category_obesity_class_2),
      )
      ScaleLegendDot(
        color = categoryColors.obesityClass3,
        label = stringResource(R.string.category_obesity_class_3),
      )
    }
  }
}

@Composable
private fun ScaleLegendDot(color: Color, label: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(5.dp),
  ) {
    Canvas(modifier = Modifier.size(8.dp)) {
      drawCircle(color = color, radius = size.minDimension / 2f)
    }
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
fun ClearHistoryConfirmDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = stringResource(R.string.dialog_clear_history_title)) },
    text = { Text(text = stringResource(R.string.dialog_clear_history_message)) },
    confirmButton = {
      TextButton(onClick = onConfirm) {
        Text(
          text = stringResource(R.string.action_clear),
          color = MaterialTheme.colorScheme.error,
        )
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(text = stringResource(R.string.action_cancel))
      }
    },
  )
}

@Composable
fun DeleteRecordConfirmDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = stringResource(R.string.dialog_delete_record_title)) },
    text = { Text(text = stringResource(R.string.dialog_delete_record_message)) },
    confirmButton = {
      TextButton(
        onClick = onConfirm,
        modifier = Modifier.testTag("confirm_delete_record_button"),
      ) {
        Text(
          text = stringResource(R.string.action_yes),
          color = MaterialTheme.colorScheme.error,
        )
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("cancel_delete_record_button"),
      ) {
        Text(text = stringResource(R.string.action_cancel))
      }
    },
  )
}

@Composable
fun InfoDocumentDialog(
  title: String,
  body: String,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = title) },
    text = {
      Column(
        modifier = Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState()),
      ) {
        Text(
          text = body,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("info_dialog_close_button"),
      ) {
        Text(text = stringResource(R.string.action_close))
      }
    },
  )
}

// endregion

// region Calculator Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
  state: CalculatorUiState,
  onUnitSystemSelected: (UnitSystem) -> Unit,
  onHeightCmChanged: (String) -> Unit,
  onFeetChanged: (String) -> Unit,
  onInchesChanged: (String) -> Unit,
  onWeightChanged: (String) -> Unit,
  onAgeChanged: (String) -> Unit,
  onGenderSelected: (GenderOption) -> Unit,
  onCalculateClicked: () -> Unit,
  onResetClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current

  val heightErrorMsg = BmiFormatters.heightErrorMessage(context, state.heightError)
  val inchesErrorMsg = BmiFormatters.heightErrorMessage(context, state.inchesError)
  val weightErrorMsg = BmiFormatters.weightErrorMessage(context, state.weightError)
  val ageErrorMsg = BmiFormatters.ageErrorMessage(context, state.ageError)

  Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    TopAppBar(
      title = {
        Text(
          text = stringResource(R.string.app_name),
          style = MaterialTheme.typography.titleLarge,
        )
      },
      expandedHeight = 56.dp,
      windowInsets = WindowInsets(0, 0, 0, 0),
      colors =
        TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          scrolledContainerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
    )

    Box(
      modifier = Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()),
      contentAlignment = Alignment.TopCenter,
    ) {
      Column(
        modifier =
          Modifier.fillMaxWidth()
            .widthIn(max = 600.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
          SegmentedButton(
            selected = state.unitSystem == UnitSystem.METRIC,
            onClick = { onUnitSystemSelected(UnitSystem.METRIC) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            modifier = Modifier.testTag("unit_metric_button").heightIn(min = 48.dp),
          ) {
            Text(text = stringResource(R.string.unit_metric))
          }
          SegmentedButton(
            selected = state.unitSystem == UnitSystem.IMPERIAL,
            onClick = { onUnitSystemSelected(UnitSystem.IMPERIAL) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            modifier = Modifier.testTag("unit_imperial_button").heightIn(min = 48.dp),
          ) {
            Text(text = stringResource(R.string.unit_imperial))
          }
        }

        val fieldColors =
          OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
          )

        if (state.unitSystem == UnitSystem.METRIC) {
          OutlinedTextField(
            value = state.heightCmInput,
            onValueChange = onHeightCmChanged,
            label = { Text(stringResource(R.string.label_height_cm)) },
            suffix = { Text(stringResource(R.string.suffix_cm)) },
            singleLine = true,
            isError = heightErrorMsg != null,
            supportingText = heightErrorMsg?.let { { Text(it) } },
            colors = fieldColors,
            keyboardOptions =
              KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
              ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("height_cm_input"),
          )
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              OutlinedTextField(
                value = state.feetInput,
                onValueChange = onFeetChanged,
                label = { Text(stringResource(R.string.label_height_ft)) },
                suffix = { Text(stringResource(R.string.suffix_ft)) },
                singleLine = true,
                isError = heightErrorMsg != null,
                colors = fieldColors,
                keyboardOptions =
                  KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                  ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).testTag("height_feet_input"),
              )
              OutlinedTextField(
                value = state.inchesInput,
                onValueChange = onInchesChanged,
                label = { Text(stringResource(R.string.label_height_in)) },
                suffix = { Text(stringResource(R.string.suffix_in)) },
                singleLine = true,
                isError = inchesErrorMsg != null || heightErrorMsg != null,
                colors = fieldColors,
                keyboardOptions =
                  KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                  ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).testTag("height_inches_input"),
              )
            }
            val combinedImperialError = inchesErrorMsg ?: heightErrorMsg
            if (combinedImperialError != null) {
              Text(
                text = combinedImperialError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp),
              )
            }
          }
        }

        OutlinedTextField(
          value = state.weightInput,
          onValueChange = onWeightChanged,
          label = {
            Text(
              if (state.unitSystem == UnitSystem.METRIC) {
                stringResource(R.string.label_weight_kg)
              } else {
                stringResource(R.string.label_weight_lb)
              }
            )
          },
          suffix = {
            Text(
              if (state.unitSystem == UnitSystem.METRIC) {
                stringResource(R.string.suffix_kg)
              } else {
                stringResource(R.string.suffix_lb)
              }
            )
          },
          singleLine = true,
          isError = weightErrorMsg != null,
          supportingText = weightErrorMsg?.let { { Text(it) } },
          colors = fieldColors,
          keyboardOptions =
            KeyboardOptions(
              keyboardType = KeyboardType.Decimal,
              imeAction = ImeAction.Next,
            ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("weight_input"),
        )

        OutlinedTextField(
          value = state.ageInput,
          onValueChange = onAgeChanged,
          label = { Text(stringResource(R.string.label_age_optional)) },
          suffix = { Text(stringResource(R.string.suffix_yrs)) },
          singleLine = true,
          isError = ageErrorMsg != null,
          supportingText = ageErrorMsg?.let { { Text(it) } },
          colors = fieldColors,
          keyboardOptions =
            KeyboardOptions(
              keyboardType = KeyboardType.Number,
              imeAction = ImeAction.Done,
            ),
          keyboardActions =
            KeyboardActions(
              onDone = {
                focusManager.clearFocus()
                onCalculateClicked()
              }
            ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("age_input"),
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = stringResource(R.string.label_gender_optional),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            FilterChip(
              selected = state.gender == GenderOption.MALE,
              onClick = { onGenderSelected(GenderOption.MALE) },
              label = { Text(stringResource(R.string.gender_male)) },
              modifier = Modifier.heightIn(min = 48.dp).testTag("gender_male_chip"),
            )
            FilterChip(
              selected = state.gender == GenderOption.FEMALE,
              onClick = { onGenderSelected(GenderOption.FEMALE) },
              label = { Text(stringResource(R.string.gender_female)) },
              modifier = Modifier.heightIn(min = 48.dp).testTag("gender_female_chip"),
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          OutlinedButton(
            onClick = {
              focusManager.clearFocus()
              onResetClicked()
            },
            enabled = state.hasAnyInput,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("reset_button"),
          ) {
            Text(text = stringResource(R.string.action_reset))
          }

          Button(
            onClick = {
              focusManager.clearFocus()
              onCalculateClicked()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(2f).heightIn(min = 48.dp).testTag("calculate_bmi_button"),
          ) {
            Text(text = stringResource(R.string.action_calculate_bmi))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

// endregion

// region Result Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
  state: ResultUiState,
  onSaveClicked: () -> Unit,
  onRecalculateClicked: () -> Unit,
  onDeleteRecordClicked: (Long) -> Unit,
  onBackClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val calculation = state.calculation
  var showDeleteDialog by remember { mutableStateOf(false) }

  if (showDeleteDialog && calculation?.savedRecordId != null) {
    DeleteRecordConfirmDialog(
      onConfirm = {
        showDeleteDialog = false
        onDeleteRecordClicked(calculation.savedRecordId)
      },
      onDismiss = { showDeleteDialog = false },
    )
  }

  Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    TopAppBar(
      title = {
        Text(
          text =
            if (state.isFromHistory) stringResource(R.string.title_previous_result)
            else stringResource(R.string.title_bmi_result),
          style = MaterialTheme.typography.titleLarge,
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBackClicked,
          modifier = Modifier.testTag("result_back_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
          )
        }
      },
      actions = {
        if (state.isFromHistory && calculation?.savedRecordId != null) {
          IconButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.testTag("result_delete_button"),
          ) {
            Icon(
              imageVector = Icons.Outlined.Delete,
              contentDescription = stringResource(R.string.action_delete_record),
            )
          }
        }
      },
      expandedHeight = 56.dp,
      windowInsets = WindowInsets(0, 0, 0, 0),
      colors =
        TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          scrolledContainerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
          navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
          actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        ),
    )

    if (calculation == null) {
      Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Icon(
            imageVector = Icons.Outlined.Calculate,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp),
          )
          Text(
            text = stringResource(R.string.result_unavailable_message),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
          )
          OutlinedButton(
            onClick = onRecalculateClicked,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.heightIn(min = 48.dp),
          ) {
            Text(text = stringResource(R.string.action_recalculate))
          }
        }
      }
      return
    }

    val categoryColor = LocalBmiCategoryColors.current.colorFor(calculation.category)

    Box(
      modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
      contentAlignment = Alignment.TopCenter,
    ) {
      Column(
        modifier =
          Modifier.fillMaxWidth()
            .widthIn(max = 600.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        FlatSectionCard {
          Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Text(
              text = BmiFormatters.formatBmi(calculation.bmi),
              style =
                MaterialTheme.typography.displaySmall.copy(
                  fontFeatureSettings = "tnum",
                ),
              fontWeight = FontWeight.SemiBold,
              color = categoryColor,
              modifier = Modifier.testTag("result_bmi_value"),
            )

            BmiCategoryBadge(
              category = calculation.category,
              modifier = Modifier.testTag("result_bmi_category"),
            )

            Spacer(modifier = Modifier.height(4.dp))

            BmiRangeIndicator(
              bmi = calculation.bmi,
              category = calculation.category,
            )
          }
        }

        FlatSectionCard(
          containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
          borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.22f),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            ResultDetailRow(
              label = stringResource(R.string.healthy_bmi_range_label),
              value = stringResource(R.string.healthy_bmi_range_value),
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            ResultDetailRow(
              label = stringResource(R.string.healthy_weight_for_height_label),
              value = BmiFormatters.formatHealthyWeightRange(calculation),
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            ResultDetailRow(
              label = stringResource(R.string.healthy_weight_status_label),
              value = BmiFormatters.formatWeightSuggestion(context, calculation),
            )
          }
        }

        FlatSectionCard {
          Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Text(
              text = stringResource(R.string.inputs_summary_label),
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ResultDetailRow(
              label = stringResource(R.string.label_height),
              value = BmiFormatters.formatHeight(calculation),
            )
            ResultDetailRow(
              label = stringResource(R.string.label_weight),
              value = BmiFormatters.formatWeight(calculation),
            )
            if (calculation.age != null) {
              ResultDetailRow(
                label = stringResource(R.string.label_age),
                value = "${calculation.age} ${stringResource(R.string.suffix_yrs)}",
              )
            }
            if (calculation.gender != GenderOption.UNSPECIFIED) {
              ResultDetailRow(
                label = stringResource(R.string.label_gender),
                value = BmiFormatters.genderLabel(context, calculation.gender),
              )
            }
            if (state.isFromHistory) {
              ResultDetailRow(
                label = stringResource(R.string.label_recorded),
                value = BmiFormatters.formatTimestamp(calculation.timestampMillis),
              )
            }
          }
        }

        Text(
          text =
            if (calculation.age != null && calculation.age < 20) {
              stringResource(R.string.age_under_20_notice)
            } else {
              stringResource(R.string.adult_bmi_disclaimer_short)
            },
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 4.dp),
        )

        if (!state.isFromHistory) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            OutlinedButton(
              onClick = onRecalculateClicked,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("recalculate_button"),
            ) {
              Text(text = stringResource(R.string.action_recalculate))
            }

            Button(
              onClick = onSaveClicked,
              enabled = !state.isSaved,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("save_result_button"),
            ) {
              if (state.isSaved) {
                Icon(
                  imageVector = Icons.Filled.Check,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = stringResource(R.string.action_saved))
              } else {
                Text(text = stringResource(R.string.action_save_result))
              }
            }
          }
        } else {
          OutlinedButton(
            onClick = onRecalculateClicked,
            shape = RoundedCornerShape(12.dp),
            modifier =
              Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("recalculate_button"),
          ) {
            Text(text = stringResource(R.string.action_recalculate))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun ResultDetailRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.weight(1f),
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum"),
      fontWeight = FontWeight.Medium,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.End,
    )
  }
}

// endregion

// region History Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
  records: List<BmiRecordEntity>,
  onRecordClicked: (BmiRecordEntity) -> Unit,
  onDeleteRecord: (Long) -> Unit,
  onClearAll: () -> Unit,
  onGoToCalculator: () -> Unit,
  modifier: Modifier = Modifier,
  isTabActive: Boolean = true,
) {
  var showClearDialog by remember { mutableStateOf(false) }
  var pendingDeleteRecordId by remember { mutableStateOf<Long?>(null) }

  if (showClearDialog) {
    ClearHistoryConfirmDialog(
      onConfirm = {
        showClearDialog = false
        onClearAll()
      },
      onDismiss = { showClearDialog = false },
    )
  }

  pendingDeleteRecordId?.let { recordIdToDelete ->
    DeleteRecordConfirmDialog(
      onConfirm = {
        pendingDeleteRecordId = null
        onDeleteRecord(recordIdToDelete)
      },
      onDismiss = { pendingDeleteRecordId = null },
    )
  }

  Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    TopAppBar(
      title = {
        Text(
          text = stringResource(R.string.title_history),
          style = MaterialTheme.typography.titleLarge,
        )
      },
      actions = {
        if (records.isNotEmpty()) {
          TextButton(
            onClick = { showClearDialog = true },
            modifier = Modifier.heightIn(min = 48.dp).testTag("clear_all_history_button"),
          ) {
            Text(
              text = stringResource(R.string.action_clear_all),
              color = MaterialTheme.colorScheme.error,
            )
          }
        }
      },
      expandedHeight = 56.dp,
      windowInsets = WindowInsets(0, 0, 0, 0),
      colors =
        TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          scrolledContainerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
    )

    if (records.isEmpty()) {
      Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp),
          )
          Text(
            text = stringResource(R.string.history_empty_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
          )
          OutlinedButton(
            onClick = onGoToCalculator,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.heightIn(min = 48.dp).testTag("empty_history_calculate_button"),
          ) {
            Text(text = stringResource(R.string.history_empty_action))
          }
        }
      }
    } else {
      var historyViewportBounds by remember { mutableStateOf<Rect?>(null) }

      Box(
        modifier =
          Modifier.fillMaxSize().onGloballyPositioned {
            historyViewportBounds = it.boundsInWindow()
          },
        contentAlignment = Alignment.TopCenter,
      ) {
        LazyColumn(
          modifier = Modifier.fillMaxSize().widthIn(max = 600.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          itemsIndexed(
            items = records,
            key = { _, record -> record.id },
          ) { index, record ->
            Column(
              modifier =
                Modifier.fillMaxWidth()
                  .animateItem(
                    fadeInSpec = tween(durationMillis = 140),
                    fadeOutSpec = tween(durationMillis = 140),
                    placementSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
                  )
            ) {
              HistoryRecordRow(
                record = record,
                onClick = { onRecordClicked(record) },
                onDelete = { pendingDeleteRecordId = record.id },
              )
              if ((index + 1) % 10 == 0) {
                val adSlotIndex = (index + 1) / 10
                StartIoBannerAd(
                  slotKey = "history_banner_$adSlotIndex",
                  topSpacingWhenLoaded = 10.dp,
                  viewportBounds = historyViewportBounds,
                  isActive = isTabActive,
                  modifier = Modifier.testTag("history_banner_ad_$adSlotIndex"),
                )
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HistoryRecordRow(
  record: BmiRecordEntity,
  onClick: () -> Unit,
  onDelete: () -> Unit,
) {
  val calc = remember(record) { record.toBmiCalculation() }

  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    tonalElevation = 0.dp,
    shadowElevation = 0.dp,
    modifier = Modifier.fillMaxWidth().testTag("history_item_${record.id}"),
  ) {
    Row(
      modifier =
        Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Text(
            text = BmiFormatters.formatBmi(calc.bmi),
            style = MaterialTheme.typography.headlineSmall.copy(fontFeatureSettings = "tnum"),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterVertically),
          )
          BmiCategoryBadge(
            category = calc.category,
            modifier = Modifier.align(Alignment.CenterVertically),
          )
        }
        Text(
          text = "${BmiFormatters.formatHeight(calc)} · ${BmiFormatters.formatWeight(calc)}",
          style = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum"),
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = BmiFormatters.formatTimestamp(record.timestampMillis),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      IconButton(
        onClick = onDelete,
        modifier = Modifier.testTag("delete_history_item_${record.id}"),
      ) {
        Icon(
          imageVector = Icons.Outlined.Delete,
          contentDescription = stringResource(R.string.cd_delete_record),
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

// endregion

// region Settings Screen

private fun openExternalUrl(context: Context, url: String) {
  val intent =
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
      if (context !is Activity) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
    }
  runCatching { context.startActivity(intent) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  preferences: UserPreferences,
  onThemeSelected: (ThemeMode) -> Unit,
  onDefaultUnitSelected: (UnitSystem) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val privacyPolicyUrl = stringResource(R.string.url_privacy_policy)
  val aboutAppUrl = stringResource(R.string.url_about_app)
  val termsConditionsUrl = stringResource(R.string.url_terms_conditions)

  Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    TopAppBar(
      title = {
        Text(
          text = stringResource(R.string.title_settings),
          style = MaterialTheme.typography.titleLarge,
        )
      },
      expandedHeight = 56.dp,
      windowInsets = WindowInsets(0, 0, 0, 0),
      colors =
        TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background,
          scrolledContainerColor = MaterialTheme.colorScheme.background,
          titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
    )

    Box(
      modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
      contentAlignment = Alignment.TopCenter,
    ) {
      Column(
        modifier =
          Modifier.fillMaxWidth()
            .widthIn(max = 600.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = stringResource(R.string.settings_section_appearance),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
          )
          FlatSectionCard {
            Column(
              modifier = Modifier.fillMaxWidth().padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Text(
                text = stringResource(R.string.settings_theme_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
              )
              SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                  selected = preferences.themeMode == ThemeMode.SYSTEM,
                  onClick = { onThemeSelected(ThemeMode.SYSTEM) },
                  shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                  modifier = Modifier.heightIn(min = 48.dp).testTag("theme_system_option"),
                ) {
                  Text(stringResource(R.string.theme_system))
                }
                SegmentedButton(
                  selected = preferences.themeMode == ThemeMode.LIGHT,
                  onClick = { onThemeSelected(ThemeMode.LIGHT) },
                  shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                  modifier = Modifier.heightIn(min = 48.dp).testTag("theme_light_option"),
                ) {
                  Text(stringResource(R.string.theme_light))
                }
                SegmentedButton(
                  selected = preferences.themeMode == ThemeMode.DARK,
                  onClick = { onThemeSelected(ThemeMode.DARK) },
                  shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                  modifier = Modifier.heightIn(min = 48.dp).testTag("theme_dark_option"),
                ) {
                  Text(stringResource(R.string.theme_dark))
                }
              }
            }
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = stringResource(R.string.settings_section_units),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
          )
          FlatSectionCard {
            Column(
              modifier = Modifier.fillMaxWidth().padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Text(
                text = stringResource(R.string.settings_default_unit_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
              )
              SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                  selected = preferences.defaultUnitSystem == UnitSystem.METRIC,
                  onClick = { onDefaultUnitSelected(UnitSystem.METRIC) },
                  shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                  modifier =
                    Modifier.heightIn(min = 48.dp).testTag("default_unit_metric_option"),
                ) {
                  Text(stringResource(R.string.unit_metric))
                }
                SegmentedButton(
                  selected = preferences.defaultUnitSystem == UnitSystem.IMPERIAL,
                  onClick = { onDefaultUnitSelected(UnitSystem.IMPERIAL) },
                  shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                  modifier =
                    Modifier.heightIn(min = 48.dp).testTag("default_unit_imperial_option"),
                ) {
                  Text(stringResource(R.string.unit_imperial))
                }
              }
            }
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = stringResource(R.string.settings_section_data),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
          )

          SettingsInfoRow(
            title = stringResource(R.string.settings_privacy_policy_title),
            icon = Icons.Outlined.Lock,
            testTag = "settings_privacy_policy_item",
            onClick = { openExternalUrl(context, privacyPolicyUrl) },
          )

          SettingsInfoRow(
            title = stringResource(R.string.settings_about_app_title),
            icon = Icons.Outlined.Info,
            testTag = "settings_about_app_item",
            onClick = { openExternalUrl(context, aboutAppUrl) },
          )

          SettingsInfoRow(
            title = stringResource(R.string.settings_terms_conditions_title),
            icon = Icons.Outlined.Description,
            testTag = "settings_terms_conditions_item",
            onClick = { openExternalUrl(context, termsConditionsUrl) },
          )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = stringResource(R.string.about_methodology_section),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
          )
          FlatSectionCard(modifier = Modifier.testTag("settings_methodology_container")) {
            Column(
              modifier = Modifier.fillMaxWidth().padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Text(
                text = stringResource(R.string.methodology_overview),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

              MethodologyFormulaBlock(
                label = stringResource(R.string.about_metric_formula_label),
                formula = stringResource(R.string.about_metric_formula),
                note = stringResource(R.string.methodology_metric_note),
              )

              MethodologyFormulaBlock(
                label = stringResource(R.string.about_imperial_formula_label),
                formula = stringResource(R.string.about_imperial_formula),
                note = stringResource(R.string.methodology_imperial_note),
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

              Text(
                text = stringResource(R.string.about_categories_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
              )
              MethodologyCategoryRow(
                range = stringResource(R.string.about_cat_underweight_range),
                category = stringResource(R.string.category_underweight),
              )
              MethodologyCategoryRow(
                range = stringResource(R.string.about_cat_normal_range),
                category = stringResource(R.string.category_normal),
              )
              MethodologyCategoryRow(
                range = stringResource(R.string.about_cat_overweight_range),
                category = stringResource(R.string.category_overweight),
              )
              MethodologyCategoryRow(
                range = stringResource(R.string.about_cat_obesity_class_1_range),
                category = stringResource(R.string.category_obesity_class_1),
              )
              MethodologyCategoryRow(
                range = stringResource(R.string.about_cat_obesity_class_2_range),
                category = stringResource(R.string.category_obesity_class_2),
              )
              MethodologyCategoryRow(
                range = stringResource(R.string.about_cat_obesity_class_3_range),
                category = stringResource(R.string.category_obesity_class_3),
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

              Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                  text = stringResource(R.string.methodology_healthy_weight_label),
                  style = MaterialTheme.typography.titleSmall,
                  color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                  text = stringResource(R.string.methodology_healthy_weight_note),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

              Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                  text = stringResource(R.string.methodology_disclaimer_title),
                  style = MaterialTheme.typography.titleSmall,
                  color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                  text = stringResource(R.string.methodology_disclaimer_body),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = stringResource(R.string.settings_section_app),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
          )
          FlatSectionCard {
            Row(
              modifier =
                Modifier.fillMaxWidth()
                  .heightIn(min = 56.dp)
                  .padding(horizontal = 16.dp, vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
              Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Text(
                text = stringResource(R.string.settings_version_label),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
              )
              Text(
                text = BuildConfig.VERSION_NAME,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun SettingsInfoRow(
  title: String,
  icon: ImageVector,
  testTag: String,
  onClick: () -> Unit,
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    tonalElevation = 0.dp,
    shadowElevation = 0.dp,
    modifier = Modifier.testTag(testTag).fillMaxWidth(),
  ) {
    Row(
      modifier =
        Modifier.fillMaxWidth()
          .heightIn(min = 56.dp)
          .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
      )
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f),
      )
      Icon(
        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun MethodologyFormulaBlock(
  label: String,
  formula: String,
  note: String,
) {
  Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
      text = formula,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Text(
      text = note,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun MethodologyCategoryRow(range: String, category: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = range,
      style = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum"),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.weight(1f),
    )
    Text(
      text = category,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Medium,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.End,
      modifier = Modifier.weight(1f),
    )
  }
}

// endregion
