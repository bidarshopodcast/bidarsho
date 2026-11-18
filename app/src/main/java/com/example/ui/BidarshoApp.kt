package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.BidarshoCatalog
import com.example.model.MembershipTier
import com.example.ui.components.SubscriptionUpgradeDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ClassDetailScreen
import com.example.ui.screens.ClassesScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CounselingScreen
import com.example.ui.screens.EpisodesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnMoreScreen
import com.example.ui.screens.MeditationScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold
import com.example.viewmodel.BidarshoViewModel

private object BidarshoRoutes {
    const val PROFILE = "profile"
    const val COUNSELING = "counseling"
    const val HOME = "home"
    const val CONTACT = "contact"
    const val ABOUT = "about"

    const val CLASSES = "classes"
    const val CLASS_DETAIL = "class_detail/{classId}"
    const val EPISODES = "episodes"
    const val MEDITATION = "meditation"
    const val LEARN_MORE = "learn_more"

    fun classDetail(classId: String): String = "class_detail/$classId"
}

private data class BottomNavTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

// Ordered from Right to Left in RTL layout:
// پروفایل من | نوبت مشاوره | خانه | تماس با ما | درباره ما
private val bottomNavTabs = listOf(
    BottomNavTab(
        route = BidarshoRoutes.PROFILE,
        label = "پروفایل من",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_profile"
    ),
    BottomNavTab(
        route = BidarshoRoutes.COUNSELING,
        label = "نوبت مشاوره",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        testTag = "nav_counseling"
    ),
    BottomNavTab(
        route = BidarshoRoutes.HOME,
        label = "خانه",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    BottomNavTab(
        route = BidarshoRoutes.CONTACT,
        label = "تماس با ما",
        selectedIcon = Icons.Filled.Phone,
        unselectedIcon = Icons.Outlined.Phone,
        testTag = "nav_contact"
    ),
    BottomNavTab(
        route = BidarshoRoutes.ABOUT,
        label = "درباره ما",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        testTag = "nav_about"
    )
)

@Composable
fun BidarshoApp(
    viewModel: BidarshoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val registrationState by viewModel.registrationState.collectAsStateWithLifecycle()
    val breathingState by viewModel.breathingState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    if (!uiState.isInitialized) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCream),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = DeepTeal)
        }
        return
    }

    if (uiState.userProfile == null) {
        Scaffold(
            containerColor = WarmCream,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = DeepTeal,
                        contentColor = WarmCream,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        ) { innerPadding ->
            AuthScreen(
                state = registrationState,
                onFullNameChange = viewModel::updateFullName,
                onEmailChange = viewModel::updateEmail,
                onPhoneChange = viewModel::updatePhoneNumber,
                onOtpChange = viewModel::updateOtpInput,
                onRequestOtp = viewModel::requestOtpCode,
                onResendOtp = viewModel::resendOtpCode,
                onBackToForm = viewModel::backToRegistrationForm,
                onVerifyAndEnter = viewModel::verifyOtpAndEnter,
                modifier = Modifier.padding(innerPadding)
            )
        }
        return
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    uiState.upgradeDialogTier?.let { requiredTier ->
        SubscriptionUpgradeDialog(
            requiredTier = requiredTier,
            currentTier = uiState.currentTier,
            context = context,
            onActivateTier = viewModel::activateMembershipTier,
            onDismiss = viewModel::dismissUpgradeDialog,
            onError = viewModel::showMessage
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 720.dp

        Scaffold(
            containerColor = WarmCream,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = DeepTeal,
                        contentColor = WarmCream,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            },
            bottomBar = {
                if (!useRail) {
                    NavigationBar(
                        containerColor = MistAqua.copy(alpha = 0.45f),
                        tonalElevation = 0.dp
                    ) {
                        bottomNavTabs.forEach { tab ->
                            val selected = currentRoute == tab.route ||
                                (tab.route == BidarshoRoutes.HOME &&
                                    currentRoute in listOf(
                                        BidarshoRoutes.CLASSES,
                                        BidarshoRoutes.CLASS_DETAIL,
                                        BidarshoRoutes.EPISODES,
                                        BidarshoRoutes.MEDITATION,
                                        BidarshoRoutes.LEARN_MORE
                                    ))
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepTeal,
                                    selectedTextColor = DeepTeal,
                                    indicatorColor = WarmGold,
                                    unselectedIconColor = SageTeal,
                                    unselectedTextColor = SageTeal
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useRail) {
                    NavigationRail(
                        containerColor = MistAqua.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        bottomNavTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = DeepTeal,
                                    selectedTextColor = DeepTeal,
                                    indicatorColor = WarmGold,
                                    unselectedIconColor = SageTeal,
                                    unselectedTextColor = SageTeal
                                )
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = BidarshoRoutes.HOME,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(BidarshoRoutes.HOME) {
                        HomeScreen(
                            userName = uiState.userProfile?.fullName ?: "دوست عزیز",
                            currentTier = uiState.currentTier,
                            onOpenClasses = { navController.navigate(BidarshoRoutes.CLASSES) },
                            onOpenEpisodes = { navController.navigate(BidarshoRoutes.EPISODES) },
                            onOpenMeditation = { navController.navigate(BidarshoRoutes.MEDITATION) },
                            onOpenLearnMore = { navController.navigate(BidarshoRoutes.LEARN_MORE) },
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.VIP) }
                        )
                    }

                    composable(BidarshoRoutes.PROFILE) {
                        ProfileScreen(
                            userProfile = uiState.userProfile,
                            currentTier = uiState.currentTier,
                            enrolledCount = uiState.enrollments.size,
                            savedArticlesCount = uiState.savedArticleIds.size,
                            onSelectTier = viewModel::activateMembershipTier,
                            onSignOut = viewModel::signOut,
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.CLASS_MEMBER) }
                        )
                    }

                    composable(BidarshoRoutes.COUNSELING) {
                        CounselingScreen(
                            currentTier = uiState.currentTier,
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.VIP) },
                            onShowMessage = viewModel::showMessage
                        )
                    }

                    composable(BidarshoRoutes.CONTACT) {
                        ContactScreen(
                            currentTier = uiState.currentTier,
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.VIP) },
                            onShowMessage = viewModel::showMessage
                        )
                    }

                    composable(BidarshoRoutes.ABOUT) {
                        AboutScreen(
                            currentTier = uiState.currentTier,
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.VIP) }
                        )
                    }

                    composable(BidarshoRoutes.CLASSES) {
                        ClassesScreen(
                            currentTier = uiState.currentTier,
                            enrollments = uiState.enrollments,
                            onBack = { navController.popBackStack() },
                            onOpenClassDetail = { classItem ->
                                navController.navigate(BidarshoRoutes.classDetail(classItem.id))
                            },
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.CLASS_MEMBER) },
                            onLockedClassTapped = { lockedClass ->
                                viewModel.showMessage(
                                    "کلاس «${lockedClass.title}» هنوز باز نشده است (${lockedClass.scheduledDateFa})."
                                )
                            }
                        )
                    }

                    composable(
                        route = BidarshoRoutes.CLASS_DETAIL,
                        arguments = listOf(navArgument("classId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val classId = backStackEntry.arguments?.getString("classId") ?: ""
                        val classItem = BidarshoCatalog.onlineClasses.firstOrNull { it.id == classId }
                            ?: BidarshoCatalog.onlineClasses.first()
                        val enrollment = uiState.enrollments[classItem.id]

                        ClassDetailScreen(
                            classItem = classItem,
                            currentTier = uiState.currentTier,
                            isEnrolled = enrollment != null,
                            lastSlideIndex = enrollment?.lastViewedSlideIndex ?: 0,
                            onBack = { navController.popBackStack() },
                            onEnrollClick = { viewModel.enrollInClass(classItem.id, classItem.title) },
                            onSlideChanged = { idx -> viewModel.updateSlideProgress(classItem.id, idx) },
                            onOpenTierUpgrade = { viewModel.openUpgradeDialog(MembershipTier.CLASS_MEMBER) }
                        )
                    }

                    composable(BidarshoRoutes.EPISODES) {
                        EpisodesScreen(
                            currentTier = uiState.currentTier,
                            onBack = { navController.popBackStack() },
                            onRequestUpgrade = viewModel::openUpgradeDialog
                        )
                    }

                    composable(BidarshoRoutes.MEDITATION) {
                        MeditationScreen(
                            currentTier = uiState.currentTier,
                            breathingState = breathingState,
                            onToggleBreathing = viewModel::toggleBreathingExercise,
                            onBack = { navController.popBackStack() },
                            onRequestUpgrade = viewModel::openUpgradeDialog
                        )
                    }

                    composable(BidarshoRoutes.LEARN_MORE) {
                        LearnMoreScreen(
                            currentTier = uiState.currentTier,
                            savedArticleIds = uiState.savedArticleIds,
                            onToggleSaveArticle = viewModel::toggleSaveArticle,
                            onBack = { navController.popBackStack() },
                            onRequestUpgrade = viewModel::openUpgradeDialog
                        )
                    }
                }
            }
        }
    }
}
