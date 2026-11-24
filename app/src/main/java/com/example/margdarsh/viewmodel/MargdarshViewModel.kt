package com.example.margdarsh.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.margdarsh.data.AssessmentEngine
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.DetailedReportGenerator
import com.example.margdarsh.data.PARENT_QUESTIONS
import com.example.margdarsh.data.STUDENT_QUESTIONS
import com.example.margdarsh.data.ScoredCareer
import com.example.margdarsh.data.StreamResult
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.data.TraitKey
import com.example.margdarsh.network.DispatchStatus
import com.example.margdarsh.network.DossierAutoDispatcher
import com.example.margdarsh.util.PaymentService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

data class MargdarshUiState(
    val studentProfile: StudentProfile = StudentProfile(
        studentName = "",
        parentName = "",
        phoneNumber = "",
        email = "",
        studentClass = "Class 10",
        schoolName = "",
        city = ""
    ),
    val isLoggedIn: Boolean = false,
    val showCounsellorReminderDialog: Boolean = false,
    val studentAnswers: Map<String, String> = emptyMap(),
    val currentStudentIndex: Int = 0,
    val parentAnswers: Map<String, String> = emptyMap(),
    val traitScores: Map<TraitKey, Int> = emptyMap(),
    val streamResult: StreamResult? = null,
    val recommendedCareers: List<ScoredCareer> = emptyList(),
    val isStudentDone: Boolean = false,
    val isParentDone: Boolean = false,
    val isReportReady: Boolean = false,
    val isCounsellorDialogVisible: Boolean = false,
    val isProfileSurveyOpen: Boolean = true,
    val showPsychometricIntroGate: Boolean = false,
    val isResourcesUnlocked: Boolean = false,
    val isDetailedAuditUnlocked: Boolean = false,
    val isCounsellingBooked: Boolean = false,
    val counsellingBookingUtr: String = "",
    val personalUpiId: String = PaymentService.UPI_ID,
    val unlockedResourceIds: Set<String> = emptySet(),
    val enteredUtrCode: String = "",
    val hasDispatchedProfile: Boolean = false,
    val hasDispatchedStudentAnswers: Boolean = false,
    val hasDispatchedParentAnswers: Boolean = false,
    val hasDispatchedSuggestedJobs: Boolean = false,
    val dispatchStatus: DispatchStatus = DispatchStatus.Idle,
    val isPaywallDialogOpen: Boolean = false
)

class MargdarshViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("margdarsh_user_session", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(MargdarshUiState())
    val uiState: StateFlow<MargdarshUiState> = _uiState.asStateFlow()

    init {
        DossierAutoDispatcher.init(application)
        loadPersistedSession()
    }

    private fun loadPersistedSession() {
        val sName = prefs.getString("student_name", "") ?: ""
        val pName = prefs.getString("parent_name", "") ?: ""
        val phone = prefs.getString("phone_number", "") ?: ""
        val email = prefs.getString("email", "") ?: ""
        val sClass = prefs.getString("student_class", "Class 10") ?: "Class 10"
        val school = prefs.getString("school_name", "") ?: ""
        val city = prefs.getString("city", "") ?: ""
        val isGoogleVerified = prefs.getBoolean("is_google_verified", false)

        // Clear mock profiles if present from testing sessions to ensure launch readiness
        if (sName.contains("Aarav", ignoreCase = true) || email.contains("sample", ignoreCase = true)) {
            prefs.edit().clear().apply()
            _uiState.value = MargdarshUiState(isProfileSurveyOpen = true)
            return
        }

        val dispatchedProfile = prefs.getBoolean("has_dispatched_profile", false)
        val dispatchedStudent = prefs.getBoolean("has_dispatched_student", false)
        val dispatchedParent = prefs.getBoolean("has_dispatched_parent", false)
        val dispatchedJobs = prefs.getBoolean("has_dispatched_jobs", false)
        val resourcesUnlocked = prefs.getBoolean("is_resources_unlocked", false)
        val detailedAuditUnlocked = prefs.getBoolean("is_detailed_audit_unlocked", false)
        val isCounsellingBooked = prefs.getBoolean("is_counselling_booked", false)
        val counsellingUtr = prefs.getString("counselling_booking_utr", "") ?: ""
        val personalUpi = prefs.getString("personal_upi_id", PaymentService.UPI_ID) ?: PaymentService.UPI_ID
        val unlockedIdsSet = prefs.getStringSet("unlocked_resource_ids", emptySet()) ?: emptySet()

        val studentAnswersJson = prefs.getString("student_answers_json", "{}") ?: "{}"
        val parentAnswersJson = prefs.getString("parent_answers_json", "{}") ?: "{}"

        val sAnswers = mutableMapOf<String, String>()
        try {
            val jsonObj = JSONObject(studentAnswersJson)
            jsonObj.keys().forEach { key ->
                sAnswers[key] = jsonObj.getString(key)
            }
        } catch (_: Exception) {}

        val pAnswers = mutableMapOf<String, String>()
        try {
            val jsonObj = JSONObject(parentAnswersJson)
            jsonObj.keys().forEach { key ->
                pAnswers[key] = jsonObj.getString(key)
            }
        } catch (_: Exception) {}

        val profile = StudentProfile(
            studentName = sName,
            parentName = pName,
            phoneNumber = phone,
            email = email,
            studentClass = sClass,
            schoolName = school,
            city = city,
            isGoogleVerified = isGoogleVerified
        )

        val isDetailsFulfilled = sName.isNotBlank() && phone.isNotBlank()
        val isStudentComplete = STUDENT_QUESTIONS.all { q -> sAnswers[q.id] != null }
        val isParentComplete = PARENT_QUESTIONS.all { q -> pAnswers[q.id] != null }

        val traits = AssessmentEngine.scoreTraits(sAnswers)
        val bothDone = isStudentComplete && isParentComplete
        val streamRes = if (bothDone) AssessmentEngine.recommendStream(traits, pAnswers) else null
        val careers = if (bothDone && streamRes != null) {
            AssessmentEngine.recommendCareers(traits, pAnswers, streamRes.stream)
        } else emptyList()

        val hasDismissedReminder = prefs.getBoolean("has_dismissed_counsellor_reminder", false)

        _uiState.value = MargdarshUiState(
            studentProfile = profile,
            isLoggedIn = isDetailsFulfilled,
            isProfileSurveyOpen = !isDetailsFulfilled,
            showPsychometricIntroGate = isDetailsFulfilled && !isStudentComplete,
            showCounsellorReminderDialog = isDetailsFulfilled && !hasDismissedReminder && !isCounsellingBooked,
            isResourcesUnlocked = resourcesUnlocked,
            isDetailedAuditUnlocked = detailedAuditUnlocked,
            isCounsellingBooked = isCounsellingBooked,
            counsellingBookingUtr = counsellingUtr,
            personalUpiId = personalUpi,
            unlockedResourceIds = unlockedIdsSet,
            studentAnswers = sAnswers,
            currentStudentIndex = 0,
            parentAnswers = pAnswers,
            traitScores = traits,
            streamResult = streamRes,
            recommendedCareers = careers,
            isStudentDone = isStudentComplete,
            isParentDone = isParentComplete,
            isReportReady = bothDone,
            hasDispatchedProfile = dispatchedProfile,
            hasDispatchedStudentAnswers = dispatchedStudent,
            hasDispatchedParentAnswers = dispatchedParent,
            hasDispatchedSuggestedJobs = dispatchedJobs
        )

        if (isDetailsFulfilled && (!dispatchedProfile || (bothDone && !dispatchedJobs))) {
            retryAutoDispatch()
        }
    }

    private fun persistProfile(profile: StudentProfile) {
        prefs.edit()
            .putString("student_name", profile.studentName)
            .putString("parent_name", profile.parentName)
            .putString("phone_number", profile.phoneNumber)
            .putString("email", profile.email)
            .putString("student_class", profile.studentClass)
            .putString("school_name", profile.schoolName)
            .putString("city", profile.city)
            .putBoolean("is_google_verified", profile.isGoogleVerified)
            .putBoolean("is_logged_in", profile.studentName.isNotBlank() && profile.phoneNumber.isNotBlank())
            .apply()
    }

    private fun persistAnswers(studentAns: Map<String, String>, parentAns: Map<String, String>) {
        val sJson = JSONObject(studentAns).toString()
        val pJson = JSONObject(parentAns).toString()
        prefs.edit()
            .putString("student_answers_json", sJson)
            .putString("parent_answers_json", pJson)
            .apply()
    }

    private fun persistDispatchFlags(profile: Boolean, student: Boolean, parent: Boolean, jobs: Boolean) {
        prefs.edit()
            .putBoolean("has_dispatched_profile", profile)
            .putBoolean("has_dispatched_student", student)
            .putBoolean("has_dispatched_parent", parent)
            .putBoolean("has_dispatched_jobs", jobs)
            .apply()
    }

    fun dismissCounsellorReminderDialog() {
        prefs.edit().putBoolean("has_dismissed_counsellor_reminder", true).apply()
        _uiState.update { it.copy(showCounsellorReminderDialog = false) }
    }

    fun showCounsellorReminderDialog() {
        _uiState.update { it.copy(showCounsellorReminderDialog = true) }
    }

    fun setPaywallDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(isPaywallDialogOpen = visible) }
    }

    fun updateProfile(profile: StudentProfile) {
        persistProfile(profile)

        var shouldSendUnified = false
        val isNowLoggedIn = profile.studentName.isNotBlank() && profile.phoneNumber.isNotBlank()

        _uiState.update { current ->
            val sendProfile = isNowLoggedIn
            if (sendProfile) shouldSendUnified = true

            val newDispatchedProfile = current.hasDispatchedProfile || sendProfile
            persistDispatchFlags(newDispatchedProfile, current.hasDispatchedStudentAnswers, current.hasDispatchedParentAnswers, current.hasDispatchedSuggestedJobs)

            current.copy(
                studentProfile = profile,
                isLoggedIn = isNowLoggedIn,
                isProfileSurveyOpen = false,
                showPsychometricIntroGate = isNowLoggedIn && !current.isStudentDone,
                hasDispatchedProfile = newDispatchedProfile
            )
        }

        if (shouldSendUnified) {
            val cur = _uiState.value
            // Silently dispatch registration to Sheet 1
            viewModelScope.launch {
                DossierAutoDispatcher.dispatchRegistrationUnified(profile)
            }
            // Only auto-sync if report is already generated for this profile
            if (cur.isReportReady) {
                viewModelScope.launch {
                    _uiState.update { it.copy(dispatchStatus = DispatchStatus.Sending) }
                    val res = DossierAutoDispatcher.syncCandidateUnified(
                        stage = "Profile Updated",
                        profile = profile,
                        studentAnswers = cur.studentAnswers,
                        parentAnswers = cur.parentAnswers,
                        traits = cur.traitScores,
                        streamResult = cur.streamResult,
                        topCareers = cur.recommendedCareers,
                        paymentStatus = if (cur.isDetailedAuditUnlocked) "Paid Premium Audit" else "Free Assessment"
                    )
                    _uiState.update { it.copy(dispatchStatus = res) }
                }
            }
        }
    }

    fun setProfileSurveyOpen(open: Boolean) {
        _uiState.update { it.copy(isProfileSurveyOpen = open) }
    }

    fun setPsychometricIntroGateVisible(visible: Boolean) {
        _uiState.update { it.copy(showPsychometricIntroGate = visible) }
    }

    fun setCounsellorDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(isCounsellorDialogVisible = visible) }
    }

    fun updatePersonalUpiId(upiId: String) {
        val clean = upiId.trim()
        if (clean.isNotBlank()) {
            prefs.edit().putString("personal_upi_id", clean).apply()
            _uiState.update { it.copy(personalUpiId = clean) }
        }
    }

    fun unlockDetailedAudit(
        utr: String = "MANUAL_VERIFIED",
        amount: String = "499",
        couponCode: String? = null,
        screenshotUri: Uri? = null
    ) {
        prefs.edit()
            .putBoolean("is_detailed_audit_unlocked", true)
            .apply()

        val cur = _uiState.value
        _uiState.update {
            it.copy(
                isDetailedAuditUnlocked = true,
                enteredUtrCode = utr,
                isPaywallDialogOpen = false
            )
        }

        val is100 = amount == "0" || utr.contains("100") || couponCode?.contains("100") == true
        val is50 = amount == "249" || utr.contains("50") || couponCode?.contains("50") == true
        val effectiveAmount = when {
            is100 -> "0"
            is50 -> "249"
            else -> amount
        }
        val effectiveCoupon = couponCode ?: if (utr.startsWith("COUPON_")) utr.removePrefix("COUPON_") else null

        val appCtx = getApplication<Application>()
        viewModelScope.launch {
            // 1. Record payment transaction & upload screenshot to Google Drive
            DossierAutoDispatcher.recordPaymentTransaction(
                context = appCtx,
                profile = cur.studentProfile,
                serviceName = "Comprehensive 65-Page Diagnostic Dossier",
                amount = effectiveAmount,
                originalPrice = "499",
                couponCode = effectiveCoupon,
                utr = utr,
                screenshotUri = screenshotUri,
                notes = if (effectiveAmount == "0") "100% Scholarship Dossier Voucher ($effectiveCoupon)" else "65-Page Dossier Unlock"
            )

            // 2. Sync consolidated student dossier
            DossierAutoDispatcher.syncCandidateUnified(
                stage = "Unlocked 65-Page Dossier Audit",
                profile = cur.studentProfile,
                studentAnswers = cur.studentAnswers,
                parentAnswers = cur.parentAnswers,
                traits = cur.traitScores,
                streamResult = cur.streamResult,
                topCareers = cur.recommendedCareers,
                paymentStatus = "Paid Premium Audit (₹$amount, UTR: $utr)",
                paymentUtr = utr
            )
        }
    }

    fun unlockResources(
        utr: String = "MANUAL_VERIFIED",
        amount: String = "70",
        itemName: String = "All Strategic Career Dossiers",
        screenshotUri: Uri? = null,
        couponCode: String? = null
    ) {
        val allIds = setOf("p1_college_cutoff_vault", "p2_degree_financial_roi_matrix", "p3_high_income_frontier_careers", "p4_stream_switch_risk_mitigation")
        prefs.edit()
            .putBoolean("is_resources_unlocked", true)
            .putStringSet("unlocked_resource_ids", allIds)
            .apply()

        val cur = _uiState.value
        val currentProfile = cur.studentProfile
        _uiState.update {
            it.copy(
                isResourcesUnlocked = true,
                unlockedResourceIds = allIds,
                enteredUtrCode = utr,
                isPaywallDialogOpen = false
            )
        }

        val appCtx = getApplication<Application>()
        viewModelScope.launch {
            // 1. Record payment transaction & upload screenshot to Google Drive
            DossierAutoDispatcher.recordPaymentTransaction(
                context = appCtx,
                profile = currentProfile,
                serviceName = "All 4 Strategic Career Handbooks (Complete Bundle)",
                amount = amount,
                originalPrice = "70",
                couponCode = couponCode,
                utr = utr,
                screenshotUri = screenshotUri,
                notes = itemName
            )

            // 2. Dispatch payment unlocked
            DossierAutoDispatcher.dispatchPaymentUnlockedUnified(
                profile = currentProfile,
                studentAnswers = cur.studentAnswers,
                parentAnswers = cur.parentAnswers,
                traits = cur.traitScores,
                streamResult = cur.streamResult,
                topCareers = cur.recommendedCareers,
                utrNumber = utr
            )
        }
    }

    fun unlockSingleResource(
        resourceId: String,
        resourceTitle: String,
        utr: String = "MANUAL_VERIFIED",
        screenshotUri: Uri? = null
    ) {
        val currentUnlocked = _uiState.value.unlockedResourceIds.toMutableSet()
        currentUnlocked.add(resourceId)
        prefs.edit().putStringSet("unlocked_resource_ids", currentUnlocked).apply()

        val cur = _uiState.value
        val currentProfile = cur.studentProfile
        _uiState.update { it.copy(unlockedResourceIds = currentUnlocked, enteredUtrCode = utr) }

        val appCtx = getApplication<Application>()
        viewModelScope.launch {
            DossierAutoDispatcher.recordPaymentTransaction(
                context = appCtx,
                profile = currentProfile,
                serviceName = "Career Handbook: $resourceTitle",
                amount = "18",
                originalPrice = "18",
                couponCode = null,
                utr = utr,
                screenshotUri = screenshotUri,
                notes = "Single Handbook: $resourceId"
            )

            DossierAutoDispatcher.syncCandidateUnified(
                stage = "Unlocked Single Resource: $resourceTitle",
                profile = currentProfile,
                studentAnswers = cur.studentAnswers,
                parentAnswers = cur.parentAnswers,
                traits = cur.traitScores,
                streamResult = cur.streamResult,
                topCareers = cur.recommendedCareers,
                paymentStatus = "Paid Single Resource (₹18)",
                paymentUtr = utr
            )
        }
    }

    fun answerStudent(questionId: String, value: String, autoAdvance: Boolean = true) {
        var shouldSyncCandidate = false

        _uiState.update { current ->
            val updatedAnswers = current.studentAnswers + (questionId to value)
            persistAnswers(updatedAnswers, current.parentAnswers)

            val isDone = STUDENT_QUESTIONS.all { q -> updatedAnswers[q.id] != null }
            val nextIndex = if (autoAdvance && current.currentStudentIndex < STUDENT_QUESTIONS.size - 1) {
                current.currentStudentIndex + 1
            } else {
                current.currentStudentIndex
            }

            val traits = AssessmentEngine.scoreTraits(updatedAnswers)
            val bothDone = isDone && current.isParentDone
            val streamRes = if (bothDone) {
                AssessmentEngine.recommendStream(traits, current.parentAnswers)
            } else null

            val careers = if (bothDone && streamRes != null) {
                AssessmentEngine.recommendCareers(traits, current.parentAnswers, streamRes.stream)
            } else emptyList()

            // Trigger sync ONLY once all test is done and report is generated
            if (bothDone) {
                shouldSyncCandidate = true
            }

            val newDispatchedStudent = current.hasDispatchedStudentAnswers || isDone
            val newDispatchedJobs = current.hasDispatchedSuggestedJobs || (bothDone && streamRes != null)

            persistDispatchFlags(current.hasDispatchedProfile, newDispatchedStudent, current.hasDispatchedParentAnswers, newDispatchedJobs)

            current.copy(
                studentAnswers = updatedAnswers,
                currentStudentIndex = nextIndex,
                isStudentDone = isDone,
                showPsychometricIntroGate = false,
                traitScores = traits,
                streamResult = streamRes,
                recommendedCareers = careers,
                isReportReady = bothDone,
                hasDispatchedStudentAnswers = newDispatchedStudent,
                hasDispatchedSuggestedJobs = newDispatchedJobs
            )
        }

        val state = _uiState.value
        val profile = state.studentProfile

        if (shouldSyncCandidate && (profile.studentName.isNotBlank() || profile.phoneNumber.isNotBlank())) {
            viewModelScope.launch {
                val stageText = when {
                    state.isReportReady -> "Assessment Completed (36/36 Questions & Parent Survey)"
                    state.isStudentDone -> "Student Assessment Completed (36/36 Questions)"
                    else -> "Student Assessment In Progress (${state.studentAnswers.size}/36 Questions)"
                }
                val res = DossierAutoDispatcher.syncCandidateUnified(
                    stage = stageText,
                    profile = profile,
                    studentAnswers = state.studentAnswers,
                    parentAnswers = state.parentAnswers,
                    traits = state.traitScores,
                    streamResult = state.streamResult,
                    topCareers = state.recommendedCareers,
                    paymentStatus = if (state.isDetailedAuditUnlocked) "Paid Premium Audit" else "Free Assessment"
                )
                _uiState.update { it.copy(dispatchStatus = res) }
            }
        }
    }

    fun setStudentIndex(index: Int) {
        _uiState.update {
            it.copy(currentStudentIndex = index.coerceIn(0, STUDENT_QUESTIONS.size - 1))
        }
    }

    fun answerParent(questionId: String, value: String) {
        var shouldSyncCandidate = false

        _uiState.update { current ->
            val updatedAnswers = current.parentAnswers + (questionId to value)
            persistAnswers(current.studentAnswers, updatedAnswers)

            val isDone = PARENT_QUESTIONS.all { q -> updatedAnswers[q.id] != null }
            val bothDone = current.isStudentDone && isDone

            val streamRes = if (bothDone) {
                AssessmentEngine.recommendStream(current.traitScores, updatedAnswers)
            } else null

            val careers = if (bothDone && streamRes != null) {
                AssessmentEngine.recommendCareers(current.traitScores, updatedAnswers, streamRes.stream)
            } else emptyList()

            // Trigger sync ONLY once all test is done and report is generated
            if (bothDone) {
                shouldSyncCandidate = true
            }

            val newDispatchedParent = current.hasDispatchedParentAnswers || isDone
            val newDispatchedJobs = current.hasDispatchedSuggestedJobs || (bothDone && streamRes != null)

            persistDispatchFlags(current.hasDispatchedProfile, current.hasDispatchedStudentAnswers, newDispatchedParent, newDispatchedJobs)

            current.copy(
                parentAnswers = updatedAnswers,
                isParentDone = isDone,
                streamResult = streamRes,
                recommendedCareers = careers,
                isReportReady = bothDone,
                hasDispatchedParentAnswers = newDispatchedParent,
                hasDispatchedSuggestedJobs = newDispatchedJobs
            )
        }

        val state = _uiState.value
        val profile = state.studentProfile

        if (shouldSyncCandidate && (profile.studentName.isNotBlank() || profile.phoneNumber.isNotBlank())) {
            viewModelScope.launch {
                val stageText = when {
                    state.isReportReady -> "Assessment Completed (36/36 Questions & Parent Survey)"
                    state.isParentDone -> "Parent Survey Completed (${state.parentAnswers.size}/${PARENT_QUESTIONS.size})"
                    else -> "Parent Survey In Progress (${state.parentAnswers.size}/${PARENT_QUESTIONS.size})"
                }
                val res = DossierAutoDispatcher.syncCandidateUnified(
                    stage = stageText,
                    profile = profile,
                    studentAnswers = state.studentAnswers,
                    parentAnswers = state.parentAnswers,
                    traits = state.traitScores,
                    streamResult = state.streamResult,
                    topCareers = state.recommendedCareers,
                    paymentStatus = if (state.isDetailedAuditUnlocked) "Paid Premium Audit" else "Free Assessment"
                )
                _uiState.update { it.copy(dispatchStatus = res) }
            }
        }
    }

    fun dispatchFullDossierNow() {
        val state = _uiState.value
        val profile = state.studentProfile
        if (profile.studentName.isBlank() && profile.phoneNumber.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(dispatchStatus = DispatchStatus.Sending) }
            val res = DossierAutoDispatcher.syncCandidateUnified(
                stage = "Manual Synchronize Dossier Request",
                profile = profile,
                studentAnswers = state.studentAnswers,
                parentAnswers = state.parentAnswers,
                traits = state.traitScores,
                streamResult = state.streamResult,
                topCareers = state.recommendedCareers,
                paymentStatus = if (state.isDetailedAuditUnlocked) "Paid Premium Audit" else "Free Assessment",
                paymentUtr = state.enteredUtrCode
            )
            _uiState.update { it.copy(dispatchStatus = res) }
        }
    }

    fun retryAutoDispatch() {
        val state = _uiState.value
        val profile = state.studentProfile
        if (profile.studentName.isBlank() && profile.phoneNumber.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(dispatchStatus = DispatchStatus.Sending) }
            val res = DossierAutoDispatcher.syncCandidateUnified(
                stage = if (state.isReportReady) "Assessment Completed" else "Profile Synchronized",
                profile = profile,
                studentAnswers = state.studentAnswers,
                parentAnswers = state.parentAnswers,
                traits = state.traitScores,
                streamResult = state.streamResult,
                topCareers = state.recommendedCareers,
                paymentStatus = if (state.isDetailedAuditUnlocked) "Paid Premium Audit" else "Free Assessment",
                paymentUtr = state.enteredUtrCode
            )
            _uiState.update { it.copy(hasDispatchedProfile = true, hasDispatchedStudentAnswers = true, hasDispatchedParentAnswers = true, hasDispatchedSuggestedJobs = true, dispatchStatus = res) }
        }
    }

    fun getDetailedReport(): DetailedPsychometricAuditReport {
        val state = _uiState.value
        return DetailedReportGenerator.generate(
            profile = state.studentProfile,
            studentAnswers = state.studentAnswers,
            parentAnswers = state.parentAnswers,
            traits = state.traitScores,
            streamResult = state.streamResult,
            careers = state.recommendedCareers
        )
    }

    /**
     * Records a Paid 1-on-1 Counselling Appointment (with Coupon or Full Payment).
     * Synchronizes the candidate to:
     * 1. The dedicated "Paid Counselling Students" Excel/Google Sheet
     * 2. The main dossier sheet unified row
     * 3. Local persistent Excel CSV roster
     * 4. Instant high-priority counselor ntfy alert
     */
    fun recordCounsellingPayment(
        utr: String,
        notes: String = "1-on-1 Personal Counselling",
        bookedProfile: StudentProfile? = null,
        amount: String = "999",
        couponCode: String? = null,
        screenshotUri: Uri? = null
    ) {
        val current = _uiState.value
        // Guard against duplicate rapid clicks for the same UTR
        if (current.dispatchStatus == DispatchStatus.Sending && current.counsellingBookingUtr == utr) {
            return
        }

        val effectiveProfile = if (bookedProfile != null && bookedProfile.studentName.isNotBlank()) {
            current.studentProfile.copy(
                studentName = bookedProfile.studentName,
                phoneNumber = bookedProfile.phoneNumber,
                parentName = if (bookedProfile.parentName.isNotBlank()) bookedProfile.parentName else current.studentProfile.parentName,
                city = if (bookedProfile.city.isNotBlank()) bookedProfile.city else current.studentProfile.city,
                schoolName = if (bookedProfile.schoolName.isNotBlank()) bookedProfile.schoolName else current.studentProfile.schoolName,
                email = if (bookedProfile.email.isNotBlank()) bookedProfile.email else current.studentProfile.email
            )
        } else current.studentProfile

        val effectiveAmount = if (amount.isNotBlank()) amount else if (utr.startsWith("COUPON_")) "0" else "999"
        val cleanPassedCoupon = couponCode?.trim()?.replace("^COUPON_".toRegex(), "")?.split("_")?.getOrNull(0)?.ifBlank { null }
        val effectiveCoupon = cleanPassedCoupon ?: if (utr.startsWith("COUPON_")) utr.removePrefix("COUPON_").split("_")[0] else null

        val paymentDescription = when {
            effectiveAmount == "0" || effectiveCoupon?.contains("100") == true || utr.startsWith("COUPON_") ->
                "100% Scholarship (${effectiveCoupon ?: "SCHOLARSHIP"}) - Free ₹0"
            effectiveAmount == "499" || effectiveCoupon?.contains("50") == true ->
                "Paid ₹499 (50% Coupon: ${effectiveCoupon ?: "COUPON50"})"
            else -> "Paid ₹$effectiveAmount Counselling Booking"
        }

        persistProfile(effectiveProfile)
        prefs.edit()
            .putBoolean("is_counselling_booked", true)
            .putString("counselling_booking_utr", utr)
            .putString("counselling_booking_status", paymentDescription)
            .apply()

        _uiState.update {
            it.copy(
                studentProfile = effectiveProfile,
                isCounsellingBooked = true,
                counsellingBookingUtr = utr,
                dispatchStatus = DispatchStatus.Sending
            )
        }

        val appCtx = getApplication<Application>()
        viewModelScope.launch {
            // 1. Record payment transaction & upload screenshot to Google Drive
            val paymentResult = DossierAutoDispatcher.recordPaymentTransaction(
                context = appCtx,
                profile = effectiveProfile,
                serviceName = "1-on-1 Personal Career Counselling",
                amount = effectiveAmount,
                originalPrice = "999",
                couponCode = effectiveCoupon,
                utr = utr,
                screenshotUri = screenshotUri,
                notes = notes
            )

            // 2. Record counselling appointment in dedicated roster with screenshot
            val res = DossierAutoDispatcher.recordCounsellingAppointment(
                profile = effectiveProfile,
                utr = utr,
                amount = effectiveAmount,
                couponCode = effectiveCoupon,
                notes = notes,
                streamResult = current.streamResult,
                topCareers = current.recommendedCareers,
                context = appCtx,
                screenshotUri = screenshotUri,
                driveUrl = paymentResult.driveUrl
            )

            // 3. Update candidate in main dossier sheet so payment status is unified
            DossierAutoDispatcher.syncCandidateUnified(
                stage = "Paid 1-on-1 Counselling Call Booked",
                profile = effectiveProfile,
                studentAnswers = current.studentAnswers,
                parentAnswers = current.parentAnswers,
                traits = current.traitScores,
                streamResult = current.streamResult,
                topCareers = current.recommendedCareers,
                paymentStatus = paymentDescription,
                paymentUtr = utr
            )
            _uiState.update { it.copy(dispatchStatus = res) }
        }
    }

    fun retakeAssessment() {
        prefs.edit()
            .remove("student_answers_json")
            .remove("parent_answers_json")
            .remove("has_dispatched_student")
            .remove("has_dispatched_parent")
            .remove("has_dispatched_jobs")
            .apply()

        _uiState.update { current ->
            current.copy(
                studentAnswers = emptyMap(),
                currentStudentIndex = 0,
                parentAnswers = emptyMap(),
                traitScores = emptyMap(),
                streamResult = null,
                recommendedCareers = emptyList(),
                isStudentDone = false,
                isParentDone = false,
                isReportReady = false,
                hasDispatchedStudentAnswers = false,
                hasDispatchedParentAnswers = false,
                hasDispatchedSuggestedJobs = false,
                showPsychometricIntroGate = true
            )
        }
    }

    fun resetAll() {
        prefs.edit().clear().apply()
        DossierAutoDispatcher.clearPersistentRefId()
        _uiState.value = MargdarshUiState(isProfileSurveyOpen = true)
    }

    fun generateComprehensiveReportText(): String {
        val state = _uiState.value
        val profile = state.studentProfile
        val stream = state.streamResult
        val traits = state.traitScores
        val careers = state.recommendedCareers
        val report = getDetailedReport()

        val sb = StringBuilder()
        sb.append("══════════════════════════════════════════════════\n")
        sb.append("   MARGDARSH MASTER CAREER DOSSIER & PSYCHOMETRIC AUDIT\n")
        sb.append("   Master Counsellor: Dr. B.B. Panchal (+91 79098 06620)\n")
        sb.append("══════════════════════════════════════════════════\n\n")

        sb.append("1. CANDIDATE & FAMILY DETAILS\n")
        sb.append("--------------------------------------------------\n")
        sb.append("Student Name    : ${profile.studentName.ifBlank { "N/A" }}\n")
        sb.append("Student ID      : ${DossierAutoDispatcher.getStudentRefId()}\n")
        sb.append("Parent/Guardian : ${profile.parentName.ifBlank { "N/A" }}\n")
        sb.append("Phone / WhatsApp: ${profile.phoneNumber.ifBlank { "N/A" }}\n")
        sb.append("Email Address   : ${profile.email.ifBlank { "N/A" }}\n")
        sb.append("Class / Grade   : ${profile.studentClass.ifBlank { "N/A" }}\n")
        sb.append("School          : ${profile.schoolName.ifBlank { "N/A" }}\n")
        sb.append("City            : ${profile.city.ifBlank { "N/A" }}\n\n")

        sb.append("2. COGNITIVE ARCHETYPE & PSYCHOLOGICAL PROFILE\n")
        sb.append("--------------------------------------------------\n")
        sb.append("Archetype       : ${report.cognitiveArchetype}\n")
        sb.append("Tagline         : ${report.archetypeTagline}\n")
        sb.append("Executive Audit : ${report.executiveSummary}\n\n")

        sb.append("3. RECOMMENDED ACADEMIC STREAM\n")
        sb.append("--------------------------------------------------\n")
        if (stream != null) {
            sb.append("Primary Stream  : ${stream.stream.displayName}\n")
            sb.append("Rationale       : ${stream.rationale}\n")
            sb.append("Stream Scores   : ${stream.scores.entries.joinToString { "${it.key.displayName}: ${it.value.toInt()}%" }}\n\n")
        } else {
            sb.append("Stream recommendation pending completion of assessments.\n\n")
        }

        sb.append("4. PSYCHOMETRIC & COGNITIVE TRAITS\n")
        sb.append("--------------------------------------------------\n")
        if (traits.isNotEmpty()) {
            traits.forEach { (trait, score) ->
                sb.append("• ${trait.label.padEnd(26)} : $score / 100\n")
            }
            sb.append("\n")
        }

        sb.append("5. CORE INNATE SUPERPOWERS (Skills You Have Right Now)\n")
        sb.append("--------------------------------------------------\n")
        report.coreInnateSkills.forEachIndexed { i, s ->
            sb.append("${i + 1}. ${s.title.uppercase()} (${s.archetypeBadge} - ${s.scorePercentage}%)\n")
            sb.append("   Why You Excel  : ${s.whyYouExcel}\n")
            sb.append("   Real World     : ${s.realWorldManifestation}\n\n")
        }

        sb.append("6. GROWTH-EDGE SKILLS TO CULTIVATE (10x Career Multipliers)\n")
        sb.append("--------------------------------------------------\n")
        report.growthEdgeSkills.forEachIndexed { i, g ->
            sb.append("${i + 1}. ${g.skillName.uppercase()} [${g.category}]\n")
            sb.append("   Why Develop    : ${g.whyDevelopThis}\n")
            sb.append("   Impact         : ${g.careerMultiplierImpact}\n")
            sb.append("   90-Day Roadmap :\n")
            g.ninetyDayActionRoadmap.forEach { step ->
                sb.append("     • $step\n")
            }
            sb.append("\n")
        }

        sb.append("7. RECOMMENDED CAREERS & PSYCHOLOGICAL SYNERGY\n")
        sb.append("--------------------------------------------------\n")
        if (careers.isNotEmpty()) {
            careers.forEachIndexed { i, scored ->
                val c = scored.career
                sb.append("${i + 1}. ${c.title.uppercase()} (Fit: ${scored.fit}%)\n")
                sb.append("   Stream Category    : ${c.stream.displayName}\n")
                sb.append("   Expected Freshers  : ${c.salaryEntry}\n")
                sb.append("   5-8 Year Potential : ${c.salaryExperienced}\n")
                sb.append("   Nature of Work     : ${c.workType}\n")
                sb.append("   Top Recruiters     : ${c.topRecruiters}\n\n")
            }
        }

        sb.append("══════════════════════════════════════════════════\n")
        sb.append("Advisory Note: Prepared under the supervision of Dr. B.B. Panchal.\n")
        sb.append("WhatsApp/Call: +91 79098 06620 | Email: rankmakersindore@gmail.com\n")
        sb.append("══════════════════════════════════════════════════\n")

        return sb.toString()
    }

    // -------------------------------------------------------------
    // ACADEMIC TESTING & STUDENT ACCOUNT ENGINE
    // -------------------------------------------------------------
    val testRepo = com.example.margdarsh.data.AcademicTestRepository.getInstance(getApplication())
    val currentAccount: StateFlow<com.example.margdarsh.data.StudentAccount?> = testRepo.currentAccount
    val allTests: StateFlow<List<com.example.margdarsh.data.AcademicTest>> = testRepo.allTests
    val submissions: StateFlow<List<com.example.margdarsh.data.TestSubmission>> = testRepo.submissions

    fun registerStudentAccount(
        name: String,
        phone: String,
        email: String,
        targetClass: String,
        password: String
    ): Result<com.example.margdarsh.data.StudentAccount> {
        val res = testRepo.registerAccount(name, phone, email, targetClass, password)
        if (res.isSuccess) {
            val acc = res.getOrNull()
            if (acc != null) {
                // Keep candidate profile in sync
                updateProfile(
                    _uiState.value.studentProfile.copy(
                        studentName = acc.name,
                        phoneNumber = acc.phoneNumber,
                        email = acc.email,
                        studentClass = "Class ${acc.studentClass}"
                    )
                )
            }
        }
        return res
    }

    fun loginStudentAccount(phoneOrEmail: String, password: String): Result<com.example.margdarsh.data.StudentAccount> {
        val res = testRepo.loginAccount(phoneOrEmail, password)
        if (res.isSuccess) {
            val acc = res.getOrNull()
            if (acc != null) {
                updateProfile(
                    _uiState.value.studentProfile.copy(
                        studentName = acc.name,
                        phoneNumber = acc.phoneNumber,
                        email = acc.email,
                        studentClass = "Class ${acc.studentClass}"
                    )
                )
            }
        }
        return res
    }

    fun logoutStudentAccount() {
        testRepo.logout()
    }

    fun updateStudentClass(newClass: String) {
        testRepo.updateAccountClass(newClass)
        updateProfile(
            _uiState.value.studentProfile.copy(studentClass = "Class $newClass")
        )
    }

    fun submitAcademicTest(
        test: com.example.margdarsh.data.AcademicTest,
        answers: Map<String, Int>,
        timeTakenSeconds: Int
    ): com.example.margdarsh.data.TestSubmission {
        return testRepo.submitTest(test, answers, timeTakenSeconds)
    }

    fun getClassLeaderboard(studentClass: String): List<com.example.margdarsh.data.TestLeaderboardEntry> {
        return testRepo.getLeaderboardForClass(studentClass)
    }
}
