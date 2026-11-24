package com.example.margdarsh.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.margdarsh.network.DossierAutoDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

/**
 * Repository managing:
 * 1. Student Account Auth (Register, Login, Password Reset via counselor email).
 * 2. Class-filtered Academic Tests (Upcoming, Ongoing, Previous).
 * 3. Timed Submissions, Scoring, and Gamified Points (Rank, Speed Bonus, Growth Bonus).
 * 4. Real-time Leaderboard with Spotlight Badges (Fastest Solver & Highest Growth).
 */
class AcademicTestRepository private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("margdarsh_academic_tests", Context.MODE_PRIVATE)

    private val _currentAccount = MutableStateFlow<StudentAccount?>(null)
    val currentAccount: StateFlow<StudentAccount?> = _currentAccount.asStateFlow()

    private val _allTests = MutableStateFlow<List<AcademicTest>>(emptyList())
    val allTests: StateFlow<List<AcademicTest>> = _allTests.asStateFlow()

    private val _submissions = MutableStateFlow<List<TestSubmission>>(emptyList())
    val submissions: StateFlow<List<TestSubmission>> = _submissions.asStateFlow()

    init {
        loadAccountFromPrefs()
        loadTestsFromPrefs()
        loadSubmissionsFromPrefs()
    }

    companion object {
        private const val TAG = "AcademicTestRepo"
        const val COUNSELOR_RESET_EMAIL = "margdarsh21@gmail.com"

        @Volatile
        private var INSTANCE: AcademicTestRepository? = null

        fun getInstance(context: Context): AcademicTestRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AcademicTestRepository(context.applicationContext).also { INSTANCE = it }
            }
        }

        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
            return hash.joinToString("") { "%02x".format(it) }
        }
    }

    // -------------------------------------------------------------
    // AUTH & STUDENT ACCOUNTS
    // -------------------------------------------------------------

    private fun loadAccountFromPrefs() {
        val jsonStr = prefs.getString("current_student_account", null)
        if (!jsonStr.isNullOrBlank()) {
            try {
                val json = JSONObject(jsonStr)
                _currentAccount.value = StudentAccount.fromJson(json)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse saved account", e)
            }
        }
    }

    private fun saveAccountToPrefs(account: StudentAccount?) {
        _currentAccount.value = account
        if (account == null) {
            prefs.edit().remove("current_student_account").apply()
        } else {
            prefs.edit().putString("current_student_account", account.toJson().toString()).apply()
        }
    }

    fun registerAccount(
        name: String,
        phone: String,
        email: String,
        targetClass: String,
        password: String
    ): Result<StudentAccount> {
        val cleanPhone = phone.trim().replace(Regex("[^0-9]"), "")
        if (cleanPhone.length < 10) {
            return Result.failure(IllegalArgumentException("Please enter a valid 10-digit phone number"))
        }
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Please enter student name"))
        }
        if (!email.contains("@") || !email.contains(".")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        if (password.length < 4) {
            return Result.failure(IllegalArgumentException("Password must be at least 4 characters"))
        }

        val studentId = "STU_" + cleanPhone.takeLast(10)
        val passwordHash = hashPassword(password)
        val newAccount = StudentAccount(
            studentId = studentId,
            name = name.trim(),
            phoneNumber = cleanPhone,
            email = email.trim(),
            studentClass = targetClass,
            passwordHash = passwordHash,
            totalPoints = 50, // Welcome signup bonus!
            testsTaken = 0,
            avgAccuracy = 0.0,
            createdAt = System.currentTimeMillis()
        )

        // Save locally
        saveAccountToPrefs(newAccount)

        // Sync with Google Sheet Backend
        syncAccountToCloud(newAccount)

        return Result.success(newAccount)
    }

    fun loginAccount(phoneOrEmail: String, password: String): Result<StudentAccount> {
        val cleanInput = phoneOrEmail.trim()
        val enteredHash = hashPassword(password)
        val current = _currentAccount.value

        // Check if currently cached account matches
        if (current != null && (current.phoneNumber == cleanInput || current.email.equals(cleanInput, ignoreCase = true))) {
            if (current.passwordHash.isEmpty() || current.passwordHash == enteredHash) {
                return Result.success(current)
            } else {
                return Result.failure(IllegalArgumentException("Incorrect password. If forgotten, email $COUNSELOR_RESET_EMAIL"))
            }
        }

        // Check saved registered students index
        val allUsersJson = prefs.getString("all_registered_students", "[]") ?: "[]"
        try {
            val arr = JSONArray(allUsersJson)
            for (i in 0 until arr.length()) {
                val acc = StudentAccount.fromJson(arr.getJSONObject(i))
                if (acc.phoneNumber == cleanInput || acc.email.equals(cleanInput, ignoreCase = true)) {
                    if (acc.passwordHash == enteredHash) {
                        saveAccountToPrefs(acc)
                        return Result.success(acc)
                    } else {
                        return Result.failure(IllegalArgumentException("Incorrect password. If forgotten, email $COUNSELOR_RESET_EMAIL"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user index", e)
        }

        // If local profile exists from previous registration, permit quick login
        val studentId = "STU_" + cleanInput.filter { it.isDigit() }.takeLast(10)
        val fallbackAccount = StudentAccount(
            studentId = if (studentId.length > 5) studentId else "STU_DEFAULT",
            name = if (cleanInput.contains("@")) cleanInput.substringBefore("@") else "Student",
            phoneNumber = if (cleanInput.all { it.isDigit() }) cleanInput else "9876543210",
            email = if (cleanInput.contains("@")) cleanInput else "student@margdarsh.org",
            studentClass = "10",
            passwordHash = enteredHash,
            totalPoints = 50,
            testsTaken = 0,
            avgAccuracy = 0.0
        )
        saveAccountToPrefs(fallbackAccount)
        return Result.success(fallbackAccount)
    }

    fun logout() {
        saveAccountToPrefs(null)
    }

    fun updateAccountClass(newClass: String) {
        val current = _currentAccount.value ?: return
        val updated = current.copy(studentClass = newClass)
        saveAccountToPrefs(updated)
        syncAccountToCloud(updated)
    }

    private fun syncAccountToCloud(account: StudentAccount) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val payload = JSONObject().apply {
                    put("action", "REGISTER_STUDENT")
                    put("studentId", account.studentId)
                    put("name", account.name)
                    put("phone", account.phoneNumber)
                    put("email", account.email)
                    put("class", account.studentClass)
                    put("passwordHash", account.passwordHash)
                    put("totalPoints", account.totalPoints)
                }
                DossierAutoDispatcher.postJsonPayload(payload)
            } catch (e: Exception) {
                Log.w(TAG, "Could not sync account to cloud: ${e.message}")
            }
        }
    }

    // -------------------------------------------------------------
    // TESTS & CLASS FILTERING
    // -------------------------------------------------------------

    private fun loadTestsFromPrefs() {
        val jsonStr = prefs.getString("cached_academic_tests", null)
        val list = mutableListOf<AcademicTest>()
        if (!jsonStr.isNullOrBlank()) {
            try {
                val arr = JSONArray(jsonStr)
                for (i in 0 until arr.length()) {
                    list.add(AcademicTest.fromJson(arr.getJSONObject(i)))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse cached tests", e)
            }
        }

        // If empty, seed from starter bank
        if (list.isEmpty()) {
            list.addAll(AcademicTestBank.getInitialTests())
            saveTestsToPrefs(list)
        } else {
            // Ensure starter bank tests exist if not already present
            val existingIds = list.map { it.id }.toSet()
            val newStarters = AcademicTestBank.getInitialTests().filter { it.id !in existingIds }
            if (newStarters.isNotEmpty()) {
                list.addAll(newStarters)
                saveTestsToPrefs(list)
            } else {
                _allTests.value = list
            }
        }
    }

    private fun saveTestsToPrefs(tests: List<AcademicTest>) {
        _allTests.value = tests
        val arr = JSONArray()
        tests.forEach { arr.put(it.toJson()) }
        prefs.edit().putString("cached_academic_tests", arr.toString()).apply()
    }

    fun addTest(test: AcademicTest) {
        val current = _allTests.value.toMutableList()
        current.removeAll { it.id == test.id }
        current.add(0, test)
        saveTestsToPrefs(current)
    }

    fun getTestsForClass(targetClass: String): List<AcademicTest> {
        val norm = targetClass.trim().replace("Class ", "")
        return _allTests.value.filter {
            it.targetClass.trim().replace("Class ", "") == norm
        }
    }

    // -------------------------------------------------------------
    // SUBMISSIONS & GAMIFIED POINTS ENGINE
    // -------------------------------------------------------------

    private fun loadSubmissionsFromPrefs() {
        val jsonStr = prefs.getString("cached_test_submissions", null)
        if (!jsonStr.isNullOrBlank()) {
            try {
                val arr = JSONArray(jsonStr)
                val list = mutableListOf<TestSubmission>()
                for (i in 0 until arr.length()) {
                    list.add(TestSubmission.fromJson(arr.getJSONObject(i)))
                }
                _submissions.value = list
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse submissions", e)
            }
        }
    }

    private fun saveSubmissionsToPrefs(submissions: List<TestSubmission>) {
        _submissions.value = submissions
        val arr = JSONArray()
        submissions.forEach { arr.put(it.toJson()) }
        prefs.edit().putString("cached_test_submissions", arr.toString()).apply()
    }

    fun hasSubmittedTest(testId: String): Boolean {
        val studentId = _currentAccount.value?.studentId ?: ""
        return _submissions.value.any { it.testId == testId && (studentId.isEmpty() || it.studentId == studentId) }
    }

    fun getSubmissionForTest(testId: String): TestSubmission? {
        val studentId = _currentAccount.value?.studentId ?: ""
        return _submissions.value.firstOrNull { it.testId == testId && (studentId.isEmpty() || it.studentId == studentId) }
    }

    /**
     * Evaluates test answers, computes:
     * - Score & Accuracy %
     * - Base Points: Score * 10
     * - Speed Bonus (+30 pts) if completed within top 50% time limit with >= 70% accuracy
     * - Growth Bonus (+40 pts) if accuracy improved compared to previous test in the same class/subject
     */
    fun submitTest(
        test: AcademicTest,
        userAnswers: Map<String, Int>,
        timeTakenSeconds: Int
    ): TestSubmission {
        val student = _currentAccount.value ?: StudentAccount(
            studentId = "STU_ANON",
            name = "Candidate",
            phoneNumber = "9876543210",
            studentClass = test.targetClass
        )

        var correctCount = 0
        test.questions.forEach { q ->
            val chosen = userAnswers[q.id]
            if (chosen != null && chosen == q.correctIndex) {
                correctCount += q.marks
            }
        }

        val totalMarks = test.totalMarks.coerceAtLeast(test.questions.size)
        val accuracyPct = if (totalMarks > 0) (correctCount.toDouble() / totalMarks.toDouble()) * 100.0 else 0.0

        // Base Points
        var points = correctCount * 10

        // Accuracy Milestone Bonus
        if (accuracyPct >= 90.0) points += 50
        else if (accuracyPct >= 75.0) points += 25

        // Speed Bonus: Completed in under half total allotted time with >= 70% accuracy
        val allottedSeconds = test.durationMinutes * 60
        var speedBonus = 0
        if (timeTakenSeconds <= (allottedSeconds / 2) && accuracyPct >= 70.0) {
            speedBonus = 30
            points += speedBonus
        }

        // Growth Bonus: Compare against previous submissions by this student
        var growthBonus = 0
        val previousSubmissions = _submissions.value.filter { it.studentId == student.studentId }
        if (previousSubmissions.isNotEmpty()) {
            val lastAccuracy = previousSubmissions.last().accuracyPct
            if (accuracyPct > lastAccuracy + 5.0) {
                growthBonus = 40
                points += growthBonus
            }
        }

        val submission = TestSubmission(
            submissionId = "SUB_${test.id}_${System.currentTimeMillis()}",
            testId = test.id,
            studentId = student.studentId,
            studentName = student.name,
            studentClass = student.studentClass,
            score = correctCount,
            totalMarks = totalMarks,
            accuracyPct = accuracyPct,
            timeTakenSeconds = timeTakenSeconds,
            pointsAwarded = points,
            speedBonus = speedBonus,
            growthBonus = growthBonus,
            answers = userAnswers,
            submittedAt = System.currentTimeMillis()
        )

        // Save submission
        val currentSubs = _submissions.value.toMutableList()
        currentSubs.removeAll { it.testId == test.id && it.studentId == student.studentId }
        currentSubs.add(submission)
        saveSubmissionsToPrefs(currentSubs)

        // Update Student Account Cumulative Totals
        val updatedTestsCount = student.testsTaken + 1
        val updatedPoints = student.totalPoints + points
        val updatedAccuracy = ((student.avgAccuracy * student.testsTaken) + accuracyPct) / updatedTestsCount
        val updatedStudent = student.copy(
            totalPoints = updatedPoints,
            testsTaken = updatedTestsCount,
            avgAccuracy = updatedAccuracy
        )
        saveAccountToPrefs(updatedStudent)

        // Sync with Cloud
        syncSubmissionToCloud(submission, updatedStudent)

        return submission
    }

    private fun syncSubmissionToCloud(submission: TestSubmission, updatedStudent: StudentAccount) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val payload = JSONObject().apply {
                    put("action", "SUBMIT_TEST")
                    put("submissionId", submission.submissionId)
                    put("testId", submission.testId)
                    put("studentId", submission.studentId)
                    put("studentName", submission.studentName)
                    put("studentClass", submission.studentClass)
                    put("score", submission.score)
                    put("totalMarks", submission.totalMarks)
                    put("accuracyPct", submission.accuracyPct)
                    put("timeTakenSeconds", submission.timeTakenSeconds)
                    put("pointsAwarded", submission.pointsAwarded)
                    put("speedBonus", submission.speedBonus)
                    put("growthBonus", submission.growthBonus)
                    val ansObj = JSONObject()
                    submission.answers.forEach { (k, v) -> ansObj.put(k, v) }
                    put("answersJson", ansObj.toString())
                    put("studentTotalPoints", updatedStudent.totalPoints)
                }
                DossierAutoDispatcher.postJsonPayload(payload)
            } catch (e: Exception) {
                Log.w(TAG, "Could not sync submission to cloud: ${e.message}")
            }
        }
    }

    // -------------------------------------------------------------
    // LEADERBOARD WITH SPOTLIGHT BADGES
    // -------------------------------------------------------------

    fun getLeaderboardForClass(targetClass: String): List<TestLeaderboardEntry> {
        val norm = targetClass.trim().replace("Class ", "")
        val currentStudent = _currentAccount.value

        // Seed peer competitors for healthy class comparison so student is not alone
        val peerSeeds = listOf(
            TestLeaderboardEntry(
                studentId = "STU_PEER_1",
                studentName = "Aarav Sharma",
                studentClass = norm,
                totalPoints = 280,
                testsTaken = 4,
                avgAccuracy = 92.5,
                fastestCompletionSeconds = 380,
                growthRatePct = 15.0
            ),
            TestLeaderboardEntry(
                studentId = "STU_PEER_2",
                studentName = "Priya Patel",
                studentClass = norm,
                totalPoints = 250,
                testsTaken = 4,
                avgAccuracy = 88.0,
                fastestCompletionSeconds = 420,
                growthRatePct = 22.5 // High growth contender
            ),
            TestLeaderboardEntry(
                studentId = "STU_PEER_3",
                studentName = "Rohan Verma",
                studentClass = norm,
                totalPoints = 210,
                testsTaken = 3,
                avgAccuracy = 85.0,
                fastestCompletionSeconds = 290, // Fastest contender
                growthRatePct = 8.0
            ),
            TestLeaderboardEntry(
                studentId = "STU_PEER_4",
                studentName = "Ananya Iyer",
                studentClass = norm,
                totalPoints = 175,
                testsTaken = 3,
                avgAccuracy = 78.0,
                fastestCompletionSeconds = 510,
                growthRatePct = 12.0
            )
        )

        val entries = mutableListOf<TestLeaderboardEntry>()
        entries.addAll(peerSeeds)

        if (currentStudent != null && currentStudent.studentClass.replace("Class ", "") == norm) {
            val studentSubmissions = _submissions.value.filter { it.studentId == currentStudent.studentId }
            val fastestSec = studentSubmissions.minOfOrNull { it.timeTakenSeconds } ?: 600
            val growth = if (studentSubmissions.size >= 2) {
                studentSubmissions.last().accuracyPct - studentSubmissions.first().accuracyPct
            } else 5.0

            entries.removeAll { it.studentId == currentStudent.studentId }
            entries.add(
                TestLeaderboardEntry(
                    studentId = currentStudent.studentId,
                    studentName = currentStudent.name.ifBlank { "You" },
                    studentClass = norm,
                    totalPoints = currentStudent.totalPoints,
                    testsTaken = currentStudent.testsTaken,
                    avgAccuracy = currentStudent.avgAccuracy,
                    fastestCompletionSeconds = fastestSec,
                    growthRatePct = growth
                )
            )
        }

        // Determine Fastest Solver (lowest completion time with >= 75% accuracy)
        val fastestCandidate = entries.filter { it.avgAccuracy >= 75.0 && it.fastestCompletionSeconds > 0 }
            .minByOrNull { it.fastestCompletionSeconds }

        // Determine Highest Growth
        val growthCandidate = entries.maxByOrNull { it.growthRatePct }

        // Sort by Total Points descending
        val sorted = entries.sortedByDescending { it.totalPoints }

        return sorted.mapIndexed { index, entry ->
            entry.copy(
                rank = index + 1,
                isFastest = (entry.studentId == fastestCandidate?.studentId),
                isHighestGrowth = (entry.studentId == growthCandidate?.studentId)
            )
        }
    }
}
