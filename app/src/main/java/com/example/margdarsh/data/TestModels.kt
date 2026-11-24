package com.example.margdarsh.data

import org.json.JSONArray
import org.json.JSONObject

/**
 * Data models for the MargDarsh Academic Test System.
 * Covers Student Accounts, Class-wise MCQ Tests, Submissions, and Leaderboard.
 */

data class StudentAccount(
    val studentId: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val studentClass: String = "10", // "8", "9", "10", "11", "12"
    val passwordHash: String = "",
    val totalPoints: Int = 0,
    val testsTaken: Int = 0,
    val avgAccuracy: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("studentId", studentId)
            put("name", name)
            put("phoneNumber", phoneNumber)
            put("email", email)
            put("studentClass", studentClass)
            put("passwordHash", passwordHash)
            put("totalPoints", totalPoints)
            put("testsTaken", testsTaken)
            put("avgAccuracy", avgAccuracy)
            put("createdAt", createdAt)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): StudentAccount {
            return StudentAccount(
                studentId = json.optString("studentId", ""),
                name = json.optString("name", ""),
                phoneNumber = json.optString("phoneNumber", ""),
                email = json.optString("email", ""),
                studentClass = json.optString("studentClass", "10"),
                passwordHash = json.optString("passwordHash", ""),
                totalPoints = json.optInt("totalPoints", 0),
                testsTaken = json.optInt("testsTaken", 0),
                avgAccuracy = json.optDouble("avgAccuracy", 0.0),
                createdAt = json.optLong("createdAt", System.currentTimeMillis())
            )
        }
    }
}

data class TestQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val marks: Int = 1
) {
    fun toJson(): JSONObject {
        val arr = JSONArray()
        options.forEach { arr.put(it) }
        return JSONObject().apply {
            put("id", id)
            put("question", question)
            put("options", arr)
            put("correctIndex", correctIndex)
            put("explanation", explanation)
            put("marks", marks)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): TestQuestion {
            val optArray = json.optJSONArray("options")
            val opts = mutableListOf<String>()
            if (optArray != null) {
                for (i in 0 until optArray.length()) {
                    opts.add(optArray.getString(i))
                }
            }
            return TestQuestion(
                id = json.optString("id", ""),
                question = json.optString("question", ""),
                options = opts,
                correctIndex = json.optInt("correctIndex", 0),
                explanation = json.optString("explanation", ""),
                marks = json.optInt("marks", 1)
            )
        }
    }
}

enum class TestStatus {
    ACTIVE,
    DRAFT,
    ARCHIVED
}

data class AcademicTest(
    val id: String,
    val targetClass: String, // "8", "9", "10", "11", "12"
    val subject: String,
    val title: String,
    val syllabus: String,
    val startTimeEpochMs: Long,
    val endTimeEpochMs: Long,
    val durationMinutes: Int,
    val totalMarks: Int,
    val questions: List<TestQuestion>,
    val status: TestStatus = TestStatus.ACTIVE
) {
    val isOngoing: Boolean
        get() {
            val now = System.currentTimeMillis()
            return now in startTimeEpochMs..endTimeEpochMs
        }

    val isUpcoming: Boolean
        get() {
            val now = System.currentTimeMillis()
            return now < startTimeEpochMs
        }

    val isPrevious: Boolean
        get() {
            val now = System.currentTimeMillis()
            return now > endTimeEpochMs
        }

    fun toJson(): JSONObject {
        val qArr = JSONArray()
        questions.forEach { qArr.put(it.toJson()) }
        return JSONObject().apply {
            put("id", id)
            put("targetClass", targetClass)
            put("subject", subject)
            put("title", title)
            put("syllabus", syllabus)
            put("startTimeEpochMs", startTimeEpochMs)
            put("endTimeEpochMs", endTimeEpochMs)
            put("durationMinutes", durationMinutes)
            put("totalMarks", totalMarks)
            put("questions", qArr)
            put("status", status.name)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): AcademicTest {
            val qList = mutableListOf<TestQuestion>()
            val qArr = json.optJSONArray("questions")
            if (qArr != null) {
                for (i in 0 until qArr.length()) {
                    qList.add(TestQuestion.fromJson(qArr.getJSONObject(i)))
                }
            }
            return AcademicTest(
                id = json.optString("id", ""),
                targetClass = json.optString("targetClass", "10"),
                subject = json.optString("subject", "General"),
                title = json.optString("title", "Practice Test"),
                syllabus = json.optString("syllabus", ""),
                startTimeEpochMs = json.optLong("startTimeEpochMs", 0L),
                endTimeEpochMs = json.optLong("endTimeEpochMs", Long.MAX_VALUE),
                durationMinutes = json.optInt("durationMinutes", 30),
                totalMarks = json.optInt("totalMarks", qList.sumOf { it.marks }),
                questions = qList,
                status = try {
                    TestStatus.valueOf(json.optString("status", "ACTIVE"))
                } catch (_: Exception) {
                    TestStatus.ACTIVE
                }
            )
        }
    }
}

data class TestSubmission(
    val submissionId: String,
    val testId: String,
    val studentId: String,
    val studentName: String,
    val studentClass: String,
    val score: Int,
    val totalMarks: Int,
    val accuracyPct: Double,
    val timeTakenSeconds: Int,
    val pointsAwarded: Int,
    val speedBonus: Int = 0,
    val growthBonus: Int = 0,
    val answers: Map<String, Int> = emptyMap(), // Question ID -> selected option index
    val submittedAt: Long = System.currentTimeMillis()
) {
    fun toJson(): JSONObject {
        val answersObj = JSONObject()
        answers.forEach { (k, v) -> answersObj.put(k, v) }
        return JSONObject().apply {
            put("submissionId", submissionId)
            put("testId", testId)
            put("studentId", studentId)
            put("studentName", studentName)
            put("studentClass", studentClass)
            put("score", score)
            put("totalMarks", totalMarks)
            put("accuracyPct", accuracyPct)
            put("timeTakenSeconds", timeTakenSeconds)
            put("pointsAwarded", pointsAwarded)
            put("speedBonus", speedBonus)
            put("growthBonus", growthBonus)
            put("answers", answersObj)
            put("submittedAt", submittedAt)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): TestSubmission {
            val ansMap = mutableMapOf<String, Int>()
            val ansObj = json.optJSONObject("answers")
            if (ansObj != null) {
                ansObj.keys().forEach { k ->
                    ansMap[k] = ansObj.getInt(k)
                }
            }
            return TestSubmission(
                submissionId = json.optString("submissionId", ""),
                testId = json.optString("testId", ""),
                studentId = json.optString("studentId", ""),
                studentName = json.optString("studentName", ""),
                studentClass = json.optString("studentClass", "10"),
                score = json.optInt("score", 0),
                totalMarks = json.optInt("totalMarks", 0),
                accuracyPct = json.optDouble("accuracyPct", 0.0),
                timeTakenSeconds = json.optInt("timeTakenSeconds", 0),
                pointsAwarded = json.optInt("pointsAwarded", 0),
                speedBonus = json.optInt("speedBonus", 0),
                growthBonus = json.optInt("growthBonus", 0),
                answers = ansMap,
                submittedAt = json.optLong("submittedAt", System.currentTimeMillis())
            )
        }
    }
}

data class TestLeaderboardEntry(
    val studentId: String,
    val studentName: String,
    val studentClass: String,
    val totalPoints: Int,
    val testsTaken: Int,
    val avgAccuracy: Double,
    val fastestCompletionSeconds: Int = 0,
    val growthRatePct: Double = 0.0,
    val isFastest: Boolean = false,
    val isHighestGrowth: Boolean = false,
    val rank: Int = 1
)
