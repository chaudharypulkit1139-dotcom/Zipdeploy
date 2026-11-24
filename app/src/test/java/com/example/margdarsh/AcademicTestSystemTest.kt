package com.example.margdarsh

import com.example.margdarsh.data.AcademicTest
import com.example.margdarsh.data.AcademicTestBank
import com.example.margdarsh.data.AcademicTestRepository
import com.example.margdarsh.data.StudentAccount
import com.example.margdarsh.data.TestLeaderboardEntry
import com.example.margdarsh.data.TestQuestion
import com.example.margdarsh.data.TestStatus
import com.example.margdarsh.data.TestSubmission
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AcademicTestSystemTest {

    @Test
    fun testPasswordHashingIsDeterministicAndSecure() {
        val pass = "Margdarsh@2026"
        val hash1 = AcademicTestRepository.hashPassword(pass)
        val hash2 = AcademicTestRepository.hashPassword(pass)
        val diffHash = AcademicTestRepository.hashPassword("DifferentPass")

        assertEquals(64, hash1.length) // SHA-256 produces 64 hex chars
        assertEquals(hash1, hash2)
        assertNotEquals(hash1, diffHash)
    }

    @Test
    fun testStarterBankHasValidTestsForHighSchoolClasses() {
        val tests = AcademicTestBank.getInitialTests()
        assertTrue("Starter bank should contain tests", tests.isNotEmpty())

        val classesRepresented = tests.map { it.targetClass }.toSet()
        assertTrue("Classes 9, 10, 11, 12 should be covered", classesRepresented.contains("10"))

        tests.forEach { test ->
            assertTrue("Test title should not be blank", test.title.isNotBlank())
            assertTrue("Subject should not be blank", test.subject.isNotBlank())
            assertTrue("Syllabus should not be blank", test.syllabus.isNotBlank())
            assertTrue("Duration should be >= 10 mins", test.durationMinutes >= 10)
            assertTrue("Total marks should be positive", test.totalMarks > 0)
            assertTrue("Questions should exist", test.questions.isNotEmpty())

            test.questions.forEach { q ->
                assertTrue("Question text should not be blank", q.question.isNotBlank())
                assertEquals("Question should have 4 options", 4, q.options.size)
                assertTrue("Correct index should be within bounds", q.correctIndex in 0..3)
                assertTrue("Explanation should be informative", q.explanation.isNotBlank())
            }
        }
    }

    @Test
    fun testStudentAccountModelCreationAndDefaults() {
        val account = StudentAccount(
            studentId = "STU_9876543210",
            name = "Aarav Sharma",
            phoneNumber = "9876543210",
            email = "aarav@gmail.com",
            studentClass = "10",
            passwordHash = AcademicTestRepository.hashPassword("secure123"),
            totalPoints = 120,
            testsTaken = 2,
            avgAccuracy = 85.0
        )

        assertEquals("STU_9876543210", account.studentId)
        assertEquals("Aarav Sharma", account.name)
        assertEquals("9876543210", account.phoneNumber)
        assertEquals("aarav@gmail.com", account.email)
        assertEquals("10", account.studentClass)
        assertEquals(120, account.totalPoints)
        assertEquals(2, account.testsTaken)
        assertEquals(85.0, account.avgAccuracy, 0.001)

        val updated = account.copy(totalPoints = 180, testsTaken = 3)
        assertEquals(180, updated.totalPoints)
        assertEquals(3, updated.testsTaken)
    }

    @Test
    fun testSubmissionScoringAndBonusMath() {
        val test = AcademicTestBank.getInitialTests().first { it.id == "TEST_C10_MATH_01" }
        assertNotNull(test)

        // Perfect answers map
        val perfectAnswers = mutableMapOf<String, Int>()
        test.questions.forEach { q ->
            perfectAnswers[q.id] = q.correctIndex
        }

        val totalMarks = test.totalMarks
        val score = test.questions.size
        val accuracyPct = (score.toDouble() / totalMarks.toDouble()) * 100.0

        // Base Points: score * 10
        var expectedPoints = score * 10
        // Accuracy >= 90%
        if (accuracyPct >= 90.0) expectedPoints += 50
        // Speed bonus: <= half allotted time (20 mins / 2 = 10 mins = 600s)
        val timeTakenSeconds = 300
        val speedBonus = if (timeTakenSeconds <= (test.durationMinutes * 60 / 2) && accuracyPct >= 70.0) 30 else 0
        expectedPoints += speedBonus

        val submission = TestSubmission(
            submissionId = "SUB_001",
            testId = test.id,
            studentId = "STU_9876543210",
            studentName = "Aarav",
            studentClass = "10",
            score = score,
            totalMarks = totalMarks,
            accuracyPct = accuracyPct,
            timeTakenSeconds = timeTakenSeconds,
            pointsAwarded = expectedPoints,
            speedBonus = speedBonus,
            growthBonus = 0,
            answers = perfectAnswers
        )

        assertEquals("SUB_001", submission.submissionId)
        assertEquals(score, submission.score)
        assertEquals(100.0, submission.accuracyPct, 0.001)
        assertEquals(expectedPoints, submission.pointsAwarded)
        assertEquals(30, submission.speedBonus)
        assertEquals(test.questions.size, submission.answers.size)
    }

    @Test
    fun testLeaderboardSpotlightsAndRankingOrder() {
        val entries = listOf(
            TestLeaderboardEntry(
                studentId = "STU_1",
                studentName = "Rohan",
                studentClass = "10",
                totalPoints = 300,
                testsTaken = 4,
                avgAccuracy = 95.0,
                fastestCompletionSeconds = 250,
                growthRatePct = 10.0,
                isFastest = true,
                rank = 1
            ),
            TestLeaderboardEntry(
                studentId = "STU_2",
                studentName = "Priya",
                studentClass = "10",
                totalPoints = 260,
                testsTaken = 3,
                avgAccuracy = 88.0,
                fastestCompletionSeconds = 400,
                growthRatePct = 25.0,
                isHighestGrowth = true,
                rank = 2
            ),
            TestLeaderboardEntry(
                studentId = "STU_3",
                studentName = "Aarav",
                studentClass = "10",
                totalPoints = 180,
                testsTaken = 2,
                avgAccuracy = 80.0,
                rank = 3
            )
        )

        val sorted = entries.sortedByDescending { it.totalPoints }
        assertEquals("Rohan", sorted[0].studentName)
        assertEquals("Priya", sorted[1].studentName)
        assertEquals("Aarav", sorted[2].studentName)
        assertTrue(sorted[0].isFastest)
        assertTrue(sorted[1].isHighestGrowth)
    }

    @Test
    fun testAcademicTestLifecycleStatus() {
        val now = System.currentTimeMillis()
        val ongoingTest = AcademicTest(
            id = "T1",
            targetClass = "10",
            subject = "Math",
            title = "Test 1",
            syllabus = "Ch 1",
            startTimeEpochMs = now - 10000L,
            endTimeEpochMs = now + 100000L,
            durationMinutes = 20,
            totalMarks = 5,
            questions = emptyList()
        )
        assertTrue("Test should be ongoing", ongoingTest.isOngoing)
        assertFalse("Test should not be upcoming", ongoingTest.isUpcoming)
        assertFalse("Test should not be previous", ongoingTest.isPrevious)

        val upcomingTest = ongoingTest.copy(
            startTimeEpochMs = now + 50000L,
            endTimeEpochMs = now + 150000L
        )
        assertTrue("Test should be upcoming", upcomingTest.isUpcoming)
        assertFalse("Test should not be ongoing", upcomingTest.isOngoing)

        val previousTest = ongoingTest.copy(
            startTimeEpochMs = now - 150000L,
            endTimeEpochMs = now - 50000L
        )
        assertTrue("Test should be previous", previousTest.isPrevious)
        assertFalse("Test should not be ongoing", previousTest.isOngoing)
    }
}
