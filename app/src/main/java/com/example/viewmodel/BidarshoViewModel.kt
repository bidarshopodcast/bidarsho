package com.example.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BidarshoDatabase
import com.example.data.BidarshoRepository
import com.example.data.ClassEnrollmentEntity
import com.example.data.UserProfileEntity
import com.example.model.BreathingPhase
import com.example.model.BreathingState
import com.example.model.MembershipTier
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class RegistrationState(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val otpInput: String = "",
    val generatedOtp: String = "",
    val isOtpStep: Boolean = false,
    val resendSeconds: Int = 0,
    val errorMessage: String? = null,
    val isSubmitting: Boolean = false
)

data class BidarshoUiState(
    val isInitialized: Boolean = false,
    val userProfile: UserProfileEntity? = null,
    val currentTier: MembershipTier = MembershipTier.PUBLIC,
    val enrollments: Map<String, ClassEnrollmentEntity> = emptyMap(),
    val savedArticleIds: Set<String> = emptySet(),
    val upgradeDialogTier: MembershipTier? = null,
    val snackbarMessage: String? = null
)

class BidarshoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BidarshoRepository = BidarshoRepository(
        BidarshoDatabase.getInstance(application).bidarshoDao()
    )

    private val _registrationState = MutableStateFlow(RegistrationState())
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    private val _breathingState = MutableStateFlow(BreathingState())
    val breathingState: StateFlow<BreathingState> = _breathingState.asStateFlow()

    private val _upgradeDialogTier = MutableStateFlow<MembershipTier?>(null)
    private val _snackbarMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BidarshoUiState> = combine(
        repository.userProfile,
        repository.enrollments,
        repository.savedArticleIds,
        _upgradeDialogTier,
        _snackbarMessage
    ) { profile, enrollments, savedArticles, upgradeTier, snackbar ->
        val tier = profile?.let {
            MembershipTier.entries.firstOrNull { t -> t.name == it.membershipTierName }
                ?: MembershipTier.PUBLIC
        } ?: MembershipTier.PUBLIC

        BidarshoUiState(
            isInitialized = true,
            userProfile = profile,
            currentTier = tier,
            enrollments = enrollments,
            savedArticleIds = savedArticles,
            upgradeDialogTier = upgradeTier,
            snackbarMessage = snackbar
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BidarshoUiState(isInitialized = false)
    )

    private var otpCountdownJob: Job? = null
    private var breathingJob: Job? = null

    fun updateFullName(value: String) {
        _registrationState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun updateEmail(value: String) {
        _registrationState.update { it.copy(email = value, errorMessage = null) }
    }

    fun updatePhoneNumber(value: String) {
        val normalized = normalizeDigits(value).filter { it.isDigit() }.take(11)
        _registrationState.update { it.copy(phoneNumber = normalized, errorMessage = null) }
    }

    fun updateOtpInput(value: String) {
        val normalized = normalizeDigits(value).filter { it.isDigit() }.take(5)
        _registrationState.update { it.copy(otpInput = normalized, errorMessage = null) }
    }

    fun requestOtpCode() {
        val state = _registrationState.value
        val name = state.fullName.trim()
        val email = state.email.trim()
        val phone = normalizeDigits(state.phoneNumber.trim())

        if (name.length < 3) {
            _registrationState.update { it.copy(errorMessage = "لطفاً نام و نام خانوادگی را کامل وارد کنید.") }
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registrationState.update { it.copy(errorMessage = "لطفاً یک آدرس ایمیل معتبر وارد کنید.") }
            return
        }
        if (phone.length < 10) {
            _registrationState.update { it.copy(errorMessage = "لطفاً شماره تماس معتبر (حداقل ۱۰ رقم) وارد کنید.") }
            return
        }

        val code = Random.nextInt(10000, 99999).toString()
        _registrationState.update {
            it.copy(
                phoneNumber = phone,
                generatedOtp = code,
                otpInput = "",
                isOtpStep = true,
                resendSeconds = 60,
                errorMessage = null
            )
        }
        startOtpCountdown()
    }

    fun resendOtpCode() {
        if (_registrationState.value.resendSeconds > 0) return
        val code = Random.nextInt(10000, 99999).toString()
        _registrationState.update {
            it.copy(
                generatedOtp = code,
                otpInput = "",
                resendSeconds = 60,
                errorMessage = null
            )
        }
        startOtpCountdown()
    }

    fun backToRegistrationForm() {
        otpCountdownJob?.cancel()
        _registrationState.update {
            it.copy(isOtpStep = false, otpInput = "", errorMessage = null)
        }
    }

    fun verifyOtpAndEnter() {
        val state = _registrationState.value
        val entered = normalizeDigits(state.otpInput.trim())
        if (entered.length != 5) {
            _registrationState.update { it.copy(errorMessage = "کد تایید ۵ رقمی را وارد کنید.") }
            return
        }
        if (entered != state.generatedOtp) {
            _registrationState.update { it.copy(errorMessage = "کد وارد شده صحیح نیست. لطفاً کد نمایش‌داده‌شده در بالای صفحه را بررسی کنید.") }
            return
        }

        _registrationState.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            repository.registerUser(
                fullName = state.fullName,
                email = state.email,
                phoneNumber = state.phoneNumber
            )
            otpCountdownJob?.cancel()
            _registrationState.update { RegistrationState() }
        }
    }

    private fun startOtpCountdown() {
        otpCountdownJob?.cancel()
        otpCountdownJob = viewModelScope.launch {
            while (_registrationState.value.resendSeconds > 0) {
                delay(1000L)
                _registrationState.update {
                    it.copy(resendSeconds = (it.resendSeconds - 1).coerceAtLeast(0))
                }
            }
        }
    }

    fun openUpgradeDialog(requiredTier: MembershipTier) {
        _upgradeDialogTier.value = requiredTier
    }

    fun dismissUpgradeDialog() {
        _upgradeDialogTier.value = null
    }

    fun activateMembershipTier(tier: MembershipTier) {
        viewModelScope.launch {
            repository.updateMembershipTier(tier)
            _upgradeDialogTier.value = null
            _snackbarMessage.value = "سطح عضویت شما به «${tier.titleFa}» ارتقا یافت."
        }
    }

    fun enrollInClass(classId: String, classTitle: String) {
        val currentTier = uiState.value.currentTier
        if (currentTier != MembershipTier.CLASS_MEMBER) {
            _upgradeDialogTier.value = MembershipTier.CLASS_MEMBER
            return
        }
        viewModelScope.launch {
            repository.enrollInClass(classId)
            _snackbarMessage.value = "ثبت‌نام شما در کلاس «$classTitle» با موفقیت ثبت شد."
        }
    }

    fun updateSlideProgress(classId: String, slideIndex: Int) {
        viewModelScope.launch {
            repository.updateSlideProgress(classId, slideIndex)
        }
    }

    fun toggleSaveArticle(articleId: String) {
        val currentlySaved = uiState.value.savedArticleIds.contains(articleId)
        viewModelScope.launch {
            repository.toggleSaveArticle(articleId, currentlySaved)
            _snackbarMessage.value = if (currentlySaved) {
                "مقاله از فهرست نشان‌شده‌ها حذف شد."
            } else {
                "مقاله در پروفایل شما ذخیره شد."
            }
        }
    }

    fun toggleBreathingExercise() {
        if (_breathingState.value.isRunning) {
            stopBreathingExercise()
        } else {
            startBreathingExercise()
        }
    }

    fun startBreathingExercise() {
        breathingJob?.cancel()
        breathingJob = viewModelScope.launch {
            val totalCycles = 4
            for (cycle in 1..totalCycles) {
                // Phase 1: Inhale 4s
                runBreathingPhase(BreathingPhase.INHALE, cycle, totalCycles)
                // Phase 2: Hold 7s
                runBreathingPhase(BreathingPhase.HOLD, cycle, totalCycles)
                // Phase 3: Exhale 8s
                runBreathingPhase(BreathingPhase.EXHALE, cycle, totalCycles)
            }
            _breathingState.value = BreathingState(
                isRunning = false,
                phase = BreathingPhase.COMPLETED,
                secondsRemaining = 0,
                currentCycle = totalCycles,
                totalCycles = totalCycles,
                phaseProgress = 0f
            )
        }
    }

    private suspend fun runBreathingPhase(
        phase: BreathingPhase,
        cycle: Int,
        totalCycles: Int
    ) {
        val durationSec = phase.totalSeconds
        val stepMillis = 50L
        val totalSteps = (durationSec * 1000L / stepMillis).toInt()
        for (step in 0..totalSteps) {
            val elapsedMs = step * stepMillis
            val remainingSec = ((durationSec * 1000L - elapsedMs + 999L) / 1000L).toInt().coerceAtLeast(1)
            val linearProgress = elapsedMs.toFloat() / (durationSec * 1000L).toFloat()
            val visualProgress = when (phase) {
                BreathingPhase.INHALE -> linearProgress
                BreathingPhase.HOLD -> 1f
                BreathingPhase.EXHALE -> 1f - linearProgress
                else -> 0f
            }
            _breathingState.value = BreathingState(
                isRunning = true,
                phase = phase,
                secondsRemaining = remainingSec,
                currentCycle = cycle,
                totalCycles = totalCycles,
                phaseProgress = visualProgress
            )
            if (step < totalSteps) {
                delay(stepMillis)
            }
        }
    }

    fun stopBreathingExercise() {
        breathingJob?.cancel()
        _breathingState.value = BreathingState()
    }

    fun showMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun signOut() {
        stopBreathingExercise()
        viewModelScope.launch {
            repository.signOut()
        }
    }

    private fun normalizeDigits(input: String): String {
        val persian = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val arabic = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        val sb = StringBuilder(input.length)
        for (ch in input) {
            val pIdx = persian.indexOf(ch)
            val aIdx = arabic.indexOf(ch)
            when {
                pIdx >= 0 -> sb.append(('0'.code + pIdx).toChar())
                aIdx >= 0 -> sb.append(('0'.code + aIdx).toChar())
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }

    override fun onCleared() {
        super.onCleared()
        otpCountdownJob?.cancel()
        breathingJob?.cancel()
    }
}
