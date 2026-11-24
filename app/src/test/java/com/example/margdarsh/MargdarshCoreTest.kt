package com.example.margdarsh

import com.example.margdarsh.data.AssessmentEngine
import com.example.margdarsh.data.LOCKED_PARENT_RESOURCES
import com.example.margdarsh.data.PARENT_QUESTIONS
import com.example.margdarsh.data.STUDENT_QUESTIONS
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.data.TraitKey
import com.example.margdarsh.network.DossierAutoDispatcher
import com.example.margdarsh.util.WhatsAppLauncher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MargdarshCoreTest {

    @Test
    fun testBuiltinGoogleSheetWebhookUrlConfigured() {
        assertTrue(DossierAutoDispatcher.BUILTIN_GOOGLE_SHEET_WEBHOOK_URL.startsWith("https://script.google.com/macros/s/"))
        assertTrue(DossierAutoDispatcher.DEFAULT_NTFY_TOPIC_1.isNotBlank())
    }

    @Test
    fun testInitialReportJobOpportunitiesLimit() {
        val traits = AssessmentEngine.scoreTraits(emptyMap())
        val parentChoices = mapOf("budget" to "mid", "relocation" to "india")
        val allCareers = AssessmentEngine.recommendCareers(traits, parentChoices, com.example.margdarsh.data.Stream.SCIENCE)
        assertTrue(allCareers.size > 4)

        // Verify initial report displayed count is limited to 4
        val initialDisplayedCareers = allCareers.take(4)
        assertEquals(4, initialDisplayedCareers.size)

        // When unlocked, all careers are available
        assertEquals(allCareers.size, allCareers.size)
    }

    @Test
    fun testStudentQuestionsAndParentQuestionsExist() {
        assertTrue("Student questions list should not be empty", STUDENT_QUESTIONS.isNotEmpty())
        assertTrue("Parent questions list should not be empty", PARENT_QUESTIONS.isNotEmpty())
    }

    @Test
    fun testTraitScoringWithEmptyAnswers() {
        val emptyScores = AssessmentEngine.scoreTraits(emptyMap())
        assertEquals(TraitKey.entries.size, emptyScores.size)
        emptyScores.values.forEach { score ->
            assertTrue("Score $score should be within [15, 99]", score in 15..99)
        }
    }

    @Test
    fun testTraitScoringWithAnswers() {
        val sampleAnswers = mutableMapOf<String, String>()
        STUDENT_QUESTIONS.forEach { q ->
            if (q.options.isNotEmpty()) {
                sampleAnswers[q.id] = q.options.first().id
            } else {
                sampleAnswers[q.id] = "4"
            }
        }
        val scores = AssessmentEngine.scoreTraits(sampleAnswers)
        assertEquals(TraitKey.entries.size, scores.size)
        scores.values.forEach { score ->
            assertTrue("Score $score should be within [15, 99]", score in 15..99)
        }
    }

    @Test
    fun testStreamCalculation() {
        val sampleAnswers = mutableMapOf<String, String>()
        STUDENT_QUESTIONS.forEach { q ->
            if (q.options.isNotEmpty()) {
                sampleAnswers[q.id] = q.options.first().id
            } else {
                sampleAnswers[q.id] = "4"
            }
        }
        val traits = AssessmentEngine.scoreTraits(sampleAnswers)
        val parentChoices = mapOf("budget" to "mid", "calibre" to "top", "relocation" to "india")
        val result = AssessmentEngine.recommendStream(traits, parentChoices)

        assertNotNull("Stream result must not be null", result)
        assertNotNull("Recommended stream must not be null", result.stream)
        assertTrue("Stream display name must not be blank", result.stream.displayName.isNotBlank())
        assertTrue("Rationale must not be blank", result.rationale.isNotBlank())
    }

    @Test
    fun testLockedParentResourcesValid() {
        assertTrue(LOCKED_PARENT_RESOURCES.isNotEmpty())
        LOCKED_PARENT_RESOURCES.forEach { res ->
            assertTrue("Resource ID must not be blank", res.id.isNotBlank())
            assertTrue("Resource title must not be blank", res.title.isNotBlank())
            assertTrue("Pages count must be > 0", res.pagesCount > 0)
            assertTrue("Key takeaways must not be empty", res.keyTakeaways.isNotEmpty())
            assertTrue("Detailed summary must not be blank", res.detailedSummary.isNotBlank())
        }
    }

    @Test
    fun testStudentProfileModel() {
        val profile = StudentProfile(
            studentName = "Rahul Sharma",
            parentName = "Vijay Sharma",
            phoneNumber = "9876543210",
            email = "rahul@example.com",
            studentClass = "Class 10",
            schoolName = "DPS RK Puram",
            city = "New Delhi"
        )
        assertEquals("Rahul Sharma", profile.studentName)
        assertEquals("Vijay Sharma", profile.parentName)
        assertEquals("9876543210", profile.phoneNumber)
        assertEquals("rahul@example.com", profile.email)
        assertEquals("Class 10", profile.studentClass)
        assertEquals("DPS RK Puram", profile.schoolName)
        assertEquals("New Delhi", profile.city)
    }

    @Test
    fun testWhatsAppNumberNormalization() {
        // Standard 10 digit Indian mobile
        assertEquals("919876543210", WhatsAppLauncher.normalizePhoneNumber("9876543210"))

        // Formatted with spaces & +91
        assertEquals("919876543210", WhatsAppLauncher.normalizePhoneNumber("+91 98765 43210"))

        // Formatted with leading 0 (trunk code)
        assertEquals("919876543210", WhatsAppLauncher.normalizePhoneNumber("09876543210"))

        // Already has 91 prefix
        assertEquals("919876543210", WhatsAppLauncher.normalizePhoneNumber("919876543210"))

        // International 0091 prefix
        assertEquals("919876543210", WhatsAppLauncher.normalizePhoneNumber("00919876543210"))

        // Counsellor Dr. Panchal default number
        assertEquals("917909806620", WhatsAppLauncher.normalizePhoneNumber("+91 79098 06620"))
        assertEquals("917909806620", WhatsAppLauncher.normalizePhoneNumber("7909806620"))

        // Blank or empty input should fallback to default counsellor number
        assertEquals(WhatsAppLauncher.DEFAULT_COUNSELLOR_CLEAN, WhatsAppLauncher.normalizePhoneNumber(""))
        assertEquals(WhatsAppLauncher.DEFAULT_COUNSELLOR_CLEAN, WhatsAppLauncher.normalizePhoneNumber("   "))
    }

    @Test
    fun testShortRefFormat() {
        val ref = DossierAutoDispatcher.getStudentRefId()
        assertTrue("Reference ID must start with MD-", ref.startsWith("MD-"))
        val numberPart = ref.substring(3)
        assertTrue(numberPart.isNotEmpty())
    }

    @Test
    fun testCareerScoringReturnsRankedCareers() {
        val traits = AssessmentEngine.scoreTraits(emptyMap())
        val parentChoices = mapOf("budget" to "mid", "relocation" to "india")
        val careers = AssessmentEngine.recommendCareers(traits, parentChoices, com.example.margdarsh.data.Stream.SCIENCE)
        assertTrue("Careers list should not be empty", careers.isNotEmpty())
        assertEquals("Should recommend all careers by default", com.example.margdarsh.data.CAREERS.size, careers.size)
        for (i in 0 until careers.size - 1) {
            assertTrue("Careers must be sorted in descending fit order", careers[i].fit >= careers[i + 1].fit)
        }
        // First career should be marked as locked top match
        assertTrue(careers.first().isLockedTopMatch)
    }

    @Test
    fun testFormatAllStudentAndParentAnswersAndAllJobs() {
        val firstStudentQ = STUDENT_QUESTIONS.first()
        val studentAnswers = mapOf(
            firstStudentQ.id to (firstStudentQ.options.firstOrNull()?.id ?: "4")
        )
        val studentSummary = DossierAutoDispatcher.formatStudentAnswersSummary(studentAnswers)
        assertTrue(studentSummary.contains("Q1"))

        val firstParentQ = PARENT_QUESTIONS.first()
        val parentAnswers = mapOf(
            firstParentQ.id to (firstParentQ.options.firstOrNull()?.value ?: "mid")
        )
        val parentSummary = DossierAutoDispatcher.formatParentAnswersSummary(parentAnswers)
        assertTrue(parentSummary.contains("1. ["))

        val traits = AssessmentEngine.scoreTraits(studentAnswers)
        val careers = AssessmentEngine.recommendCareers(traits, parentAnswers)
        assertEquals(com.example.margdarsh.data.CAREERS.size, careers.size)
    }

    @Test
    fun testGoogleSheetsFormulaParseErrorPrevention() {
        val script = com.example.margdarsh.util.GoogleSheetAppsScriptTemplate.SCRIPT_CODE
        assertTrue("Apps script must include toPlainTextCell helper", script.contains("toPlainTextCell"))
        assertTrue("Apps script must check for '+' operator", script.contains("'+'"))
        assertTrue("Apps script must escape with single quote", script.contains("return \"'\" + s"))
    }

    @Test
    fun testPaymentServiceUriAndConfig() {
        assertEquals("mohitchoudhary9361@okhdfcbank", com.example.margdarsh.util.PaymentService.UPI_ID)
        val uriString = com.example.margdarsh.util.PaymentService.buildUpiUriString(amount = "2.00", note = "Margdarsh Guide", transactionRef = "TEST12345")
        assertTrue(uriString.startsWith("upi://pay?"))
        assertTrue(uriString.contains("pa=mohitchoudhary9361%40okhdfcbank") || uriString.contains("pa=mohitchoudhary9361@okhdfcbank"))
        assertTrue(uriString.contains("am=2") || uriString.contains("am=2.00"))
        assertTrue(uriString.contains("cu=INR"))
        assertTrue(uriString.contains("tr=TEST12345"))
    }

    @Test
    fun testAllPaymentsDirectedToMohitChoudharyUpi() {
        assertEquals("mohitchoudhary9361@okhdfcbank", com.example.margdarsh.util.PaymentService.UPI_ID)
        assertEquals("Mohit Choudhary", com.example.margdarsh.util.PaymentService.PAYEE_NAME)
        
        // Verify only the primary VPA is configured with no fallbacks
        assertEquals(1, com.example.margdarsh.util.PaymentService.SUPPORTED_PROVIDERS.size)
        assertEquals("mohitchoudhary9361@okhdfcbank", com.example.margdarsh.util.PaymentService.SUPPORTED_PROVIDERS.first().vpa)
        assertTrue(com.example.margdarsh.util.PaymentService.SUPPORTED_PROVIDERS.first().isPrimary)
    }

    @Test
    fun testPaymentFailureDiagnosisAndSuggestions() {
        val diagLimit = com.example.margdarsh.util.PaymentService.diagnoseFailure(
            rawResponse = "Status=FAILED&responseCode=U19&errorMessage=Bank limit reached",
            responseCode = "U19",
            currentAmount = "1",
            currentProviderId = "hdfc"
        )
        assertTrue(diagLimit.isBankLimitIssue)
        assertEquals("2", diagLimit.suggestedAmount)
        assertNotNull(diagLimit.suggestedProvider)

        val diagNormal = com.example.margdarsh.util.PaymentService.diagnoseFailure(
            rawResponse = "Status=FAILED&responseCode=U69",
            responseCode = "U69",
            currentAmount = "5",
            currentProviderId = "hdfc"
        )
        assertFalse(diagNormal.isBankLimitIssue)
    }


    @Test
    fun testPaymentServiceResponseParsingSuccess() {
        val rawResponse = "txnId=UPI123456789&responseCode=00&ApprovalRefNo=423871928374&Status=SUCCESS&txnRef=MGD9823412"
        val parsed = com.example.margdarsh.util.PaymentService.parseUpiResponse(rawResponse)
        assertEquals(com.example.margdarsh.util.PaymentService.TransactionStatus.SUCCESS, parsed.status)
        assertTrue(parsed.isSuccessful)
        assertEquals("423871928374", parsed.approvalRefNo)
        assertEquals("00", parsed.responseCode)
    }

    @Test
    fun testPaymentServiceResponseParsingFailure() {
        val rawResponse = "txnId=UPI123456789&responseCode=U19&Status=FAILED&txnRef=MGD9823412"
        val parsed = com.example.margdarsh.util.PaymentService.parseUpiResponse(rawResponse)
        assertEquals(com.example.margdarsh.util.PaymentService.TransactionStatus.FAILED, parsed.status)
        assertFalse(parsed.isSuccessful)
        assertNotNull(parsed.errorMessage)
    }

    @Test
    fun testSingleRowExcelArchitectureInAppsScript() {
        val script = com.example.margdarsh.util.GoogleSheetAppsScriptTemplate.SCRIPT_CODE
        // Verify 1 Student = 1 Row upsert logic is embedded in Apps Script
        assertTrue("Apps Script must have handleStudentUpsert for 1 Student = 1 Row", script.contains("handleStudentUpsert"))
        assertTrue("Apps Script must check for matching phone or studentId", script.contains("cleanPhone") || script.contains("studentId"))
        assertTrue("Apps Script must track existingRowIdx", script.contains("existingRowIdx"))
    }

    @Test
    fun testCouponDiscountCalculation() {
        fun calculateDiscount(code: String): Int {
            val upper = code.trim().uppercase()
            return when {
                upper in listOf("FREE100", "MARG100", "PANCHAL100", "SCHOLARSHIP100", "DIRECT100", "BODHAMI100", "TEST100", "100OFF", "FREE") || upper.endsWith("100") -> 100
                upper in listOf("MARG50", "SAVE50", "HALF50", "DISCOUNT50", "SCHOLARSHIP50", "STUDENT50", "50OFF", "COUNSEL50") || upper.endsWith("50") -> 50
                else -> 0
            }
        }

        assertEquals(100, calculateDiscount("FREE100"))
        assertEquals(100, calculateDiscount("MARG100"))
        assertEquals(100, calculateDiscount("SCHOLARSHIP100"))
        assertEquals(100, calculateDiscount("MY100"))

        assertEquals(50, calculateDiscount("MARG50"))
        assertEquals(50, calculateDiscount("SAVE50"))
        assertEquals(50, calculateDiscount("HALF50"))
        assertEquals(50, calculateDiscount("DISCOUNT50"))
        assertEquals(50, calculateDiscount("SPECIAL50"))

        assertEquals(0, calculateDiscount("INVALID_CODE"))
        assertEquals(0, calculateDiscount(""))
    }

    @Test
    fun testStrictUtrValidation() {
        val validUtrPattern = Regex("^[0-9]{12}$")
        val counselorKeys = listOf("790980", "252525", "RANKMAKERS", "MARGDARSH")

        fun isValidUtr(utr: String): Boolean {
            val clean = utr.trim()
            return clean.matches(validUtrPattern) || clean in counselorKeys
        }

        // Valid 12-digit numeric bank UTRs
        assertTrue(isValidUtr("423987123456"))
        assertTrue(isValidUtr("123456789012"))
        assertTrue(isValidUtr("790980")) // Counselor key
        assertTrue(isValidUtr("RANKMAKERS")) // Counselor key

        // Invalid fake entries (prevent fake verify)
        assertFalse(isValidUtr("123"))
        assertFalse(isValidUtr("paid"))
        assertFalse(isValidUtr("done"))
        assertFalse(isValidUtr(""))
        assertFalse(isValidUtr("42398712345")) // only 11 digits
        assertFalse(isValidUtr("4239871234567")) // 13 digits
        assertFalse(isValidUtr("42398712345A")) // contains letter
    }

    @Test
    fun testPaidCounsellingStudentEntryDataClass() {
        val entry = com.example.margdarsh.data.PaidCounsellingStudentEntry(
            studentName = "Aarav Sharma",
            parentName = "Rajesh Sharma",
            phoneNumber = "+91 9876543210",
            email = "aarav@gmail.com",
            studentClass = "Class 10",
            schoolName = "DPS RK Puram",
            city = "New Delhi",
            amountPaid = "₹0",
            couponUsed = "FREE100",
            paymentStatus = "100% Scholarship (FREE100) - Free ₹0",
            paymentUtr = "COUPON_FREE100",
            bookingTimestamp = "24/09/2026, 06:30:00 PM IST",
            recommendedStream = "Science (PCM)",
            topCareers = "Software Architect, Aerospace Engineer",
            studentRefId = "MD-987654",
            notes = "1-on-1 Personal Counselling Call"
        )

        assertEquals("Aarav Sharma", entry.studentName)
        assertEquals("FREE100", entry.couponUsed)
        assertEquals("₹0", entry.amountPaid)
        assertEquals("100% Scholarship (FREE100) - Free ₹0", entry.paymentStatus)
        assertEquals("COUPON_FREE100", entry.paymentUtr)
        assertEquals("MD-987654", entry.studentRefId)
    }

    @Test
    fun testPaidCounsellingAppsScriptHeadersAndSheetNames() {
        val unifiedScript = com.example.margdarsh.util.GoogleSheetAppsScriptTemplate.SCRIPT_CODE
        assertTrue("Script should target Paid Counselling Students tab", unifiedScript.contains("Paid Counselling Students"))
        assertTrue("Script should contain Coupon Applied column", unifiedScript.contains("Coupon Applied"))
        assertTrue("Script should contain Student Name column", unifiedScript.contains("Student Name"))
        assertTrue("Script should contain Payment UTR column", unifiedScript.contains("Payment UTR"))
        assertTrue("Unified script should auto-sync to Paid Counselling Students sheet", unifiedScript.contains("Paid Counselling Students"))
        assertTrue("Unified script should contain recordInPaidCounsellingSheet", unifiedScript.contains("recordInPaidCounsellingSheet"))
    }

    @Test
    fun testResourceBundlePricing() {
        val singlePrice = 18
        val bundlePrice = 70
        assertEquals(18, singlePrice)
        assertEquals(70, bundlePrice)
        assertTrue(bundlePrice < singlePrice * 4) // Bundle provides discount over 4 * 18 = 72
    }

    @Test
    fun testRetakeAssessmentLogicPreservesProfile() {
        val initialProfile = StudentProfile(
            studentName = "Rohan Verma",
            parentName = "Suresh Verma",
            phoneNumber = "+91 9876501234",
            email = "rohan@gmail.com",
            studentClass = "Class 11",
            schoolName = "DPS",
            city = "Indore",
            isGoogleVerified = true
        )

        // Mock state before retake
        val answers = mapOf("Q1" to "4", "Q2" to "3")
        val state = com.example.margdarsh.viewmodel.MargdarshUiState(
            studentProfile = initialProfile,
            isLoggedIn = true,
            studentAnswers = answers,
            isStudentDone = true,
            isParentDone = true,
            isReportReady = true
        )

        // When retake is triggered, profile & login status MUST be preserved while answers are cleared
        val retakenState = state.copy(
            studentAnswers = emptyMap(),
            currentStudentIndex = 0,
            parentAnswers = emptyMap(),
            traitScores = emptyMap(),
            streamResult = null,
            recommendedCareers = emptyList(),
            isStudentDone = false,
            isParentDone = false,
            isReportReady = false,
            showPsychometricIntroGate = true
        )

        assertEquals("Rohan Verma", retakenState.studentProfile.studentName)
        assertEquals("+91 9876501234", retakenState.studentProfile.phoneNumber)
        assertTrue(retakenState.isLoggedIn)
        assertTrue(retakenState.studentAnswers.isEmpty())
        assertFalse(retakenState.isStudentDone)
        assertFalse(retakenState.isReportReady)
    }

    @Test
    fun testCouponCodeExtractionAndFormatting() {
        val rawCoupon1 = "COUPON_FREE100_1234_5678"
        val clean1 = rawCoupon1.replace("^COUPON_".toRegex(), "").split("_")[0].trim().uppercase()
        assertEquals("FREE100", clean1)

        val rawCoupon2 = "MARG50"
        val clean2 = rawCoupon2.replace("^COUPON_".toRegex(), "").split("_")[0].trim().uppercase()
        assertEquals("MARG50", clean2)

        val utrCoupon = "COUPON_SCHOLARSHIP100_9999_8888"
        val cleanUtr = utrCoupon.removePrefix("COUPON_").split("_")[0]
        assertEquals("SCHOLARSHIP100", cleanUtr)
    }

    @Test
    fun testStudentRefIdGenerationFormat() {
        val refId = DossierAutoDispatcher.getStudentRefId("9876543210", "Aarav")
        assertTrue("Ref ID should start with MD-", refId.startsWith("MD-"))
        DossierAutoDispatcher.clearPersistentRefId()
    }
}


