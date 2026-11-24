package com.example.margdarsh.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.ScoredCareer
import com.example.margdarsh.data.StreamResult
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.data.TraitKey
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

object MargdarshReportPdfGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val TOTAL_PAGES = 35

    /**
     * Generates a comprehensive 35-page publication-grade PDF containing the complete
     * Psychological Assessment, RIASEC Radar, Thinking Graphs, Cognitive Stamina,
     * Innate Superpowers, Growth-Edge Skills, Stream Blueprint, 36-Question Cognitive Audit (12 pages),
     * Parent Diagnostic Survey Deep Dive, Parent vs Student Comparative Study, Mediation Playbook & Pact,
     * 10-Year Industry Horizon, 6 Dedicated Top Career Pages with vibrant "Day in the Life",
     * 4-Year High School Grade 9-12 Blueprints, Top 25 Exam Matrix, Institutions Guide,
     * and Counselor Certification Seal.
     */
    fun generateAndDownloadReportPdf(
        context: Context,
        profile: StudentProfile,
        streamResult: StreamResult?,
        traits: Map<TraitKey, Int>,
        detailedReport: DetailedPsychometricAuditReport,
        careers: List<ScoredCareer>,
        parentAnswers: Map<String, String> = emptyMap(),
        studentAnswers: Map<String, String> = emptyMap(),
        isUnlocked: Boolean = false,
        openAfterSave: Boolean = true
    ): File? {
        val candidateName = profile.studentName.ifBlank { "Candidate" }
        val safeName = candidateName.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(30)
        val dateStr = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val totalReportPages = if (isUnlocked) 65 else 35
        val fileName = if (isUnlocked) {
            "MargDarsh_Official_65Page_Premium_Holistic_Dossier_${safeName}_$dateStr.pdf"
        } else {
            "MargDarsh_Official_35Page_Dossier_${safeName}_$dateStr.pdf"
        }

        val document = PdfDocument()

        try {
            // Evaluate 14-D Holistic Dossier Metrics
            val holisticMetrics = com.example.margdarsh.data.HolisticAssessmentEngine.evaluateDossier(
                profile = profile,
                studentAnswers = studentAnswers,
                parentAnswers = parentAnswers
            )

            // PAGE 1: Profile, Recommended Stream, Cognitive Archetype & Executive Summary
            document.writePage(1) { canvas ->
                MargdarshReportPages1to10.drawPage1ProfileAndArchetype(canvas, profile, streamResult, traits, detailedReport)
            }

            // PAGE 2: 7-Dimensional Cognitive Trait Matrix & Thinking Graphs
            document.writePage(2) { canvas ->
                MargdarshReportPages1to10.drawPage2TraitMatrix(canvas, traits, detailedReport, profile)
            }

            // PAGE 3: Holland's RIASEC Vocational Radar & Big-5 Personality Factor Analysis
            document.writePage(3) { canvas ->
                MargdarshReportPages1to10.drawPage3RiasecAndBig5(canvas, traits, detailedReport, profile)
            }

            // PAGE 4: Cognitive Stamina, 24-Hour Focus Curve & Executive Function Analysis
            document.writePage(4) { canvas ->
                MargdarshReportPages1to10.drawPage4CognitiveStamina(canvas, profile)
            }

            // PAGE 5: Innate Core Superpowers (Primary Cognitive Assets)
            document.writePage(5) { canvas ->
                MargdarshReportPages1to10.drawPage5InnateSuperpowers(canvas, detailedReport, profile)
            }

            // PAGE 6: Strategic Growth-Edge Skills to Cultivate (10x Multipliers & 90-Day Roadmaps)
            document.writePage(6) { canvas ->
                MargdarshReportPages1to10.drawPage6GrowthSkills(canvas, detailedReport, profile)
            }

            // PAGE 7: Strategic Stream & Academic Trajectory Blueprint
            document.writePage(7) { canvas ->
                MargdarshReportPages1to10.drawPage7StreamBlueprint(canvas, streamResult, traits, profile)
            }

            // PAGES 8 to 19: 36-Question In-Depth Psychological Audit (3 Questions per Page x 12 Pages)
            val questionList = detailedReport.questionAudits
            for (pageIndex in 0 until 12) {
                val pageNum = 8 + pageIndex
                val startIndex = pageIndex * 3
                val endIndex = min(startIndex + 3, questionList.size)
                val pageQuestions = if (startIndex < questionList.size) {
                    questionList.subList(startIndex, endIndex)
                } else {
                    emptyList()
                }

                document.writePage(pageNum) { canvas ->
                    MargdarshReportPages11to20.drawQuestionAuditPage(canvas, pageQuestions, pageNum, profile)
                }
            }

            // PAGE 20: Comprehensive Parent Diagnostic Survey Analysis & Intent Audit
            document.writePage(20) { canvas ->
                MargdarshReportPages11to20.drawPage20ParentSurveyDeepDive(canvas, profile, parentAnswers)
            }

            // PAGE 21: Parent vs. Student Comparative Divergence Matrix & Harmony Index
            document.writePage(21) { canvas ->
                MargdarshReportPages21to30.drawPage21ComparativeMatrix(canvas, profile, traits, streamResult, detailedReport, careers, parentAnswers)
            }

            // PAGE 22: Family Mediation Playbook & Joint Educational Pact
            document.writePage(22) { canvas ->
                MargdarshReportPages21to30.drawPage22MediationPlaybook(canvas, profile)
            }

            // PAGE 23: 10-Year Career Horizon & Industry Evolution Matrix 2025–2035
            document.writePage(23) { canvas ->
                MargdarshReportPages21to30.drawPage23IndustryHorizon(canvas, profile)
            }

            // PAGES 24 to 29: Top 6 Recommended Career Pathways (1 Dedicated Full Page per Career with vibrant "Day in the Life")
            for (careerRank in 1..6) {
                val pageNum = 23 + careerRank
                val careerIndex = careerRank - 1
                val career = if (careerIndex < careers.size) careers[careerIndex] else null

                document.writePage(pageNum) { canvas ->
                    if (career != null) {
                        MargdarshReportPages21to30.drawDedicatedCareerPage(canvas, career, careerRank, pageNum, profile)
                    }
                }
            }

            // PAGE 30: 4-Year High School Master Action Plan (Grade 9 & 10 Execution)
            document.writePage(30) { canvas ->
                MargdarshReportPages21to30.drawPage30Grade9And10(canvas, streamResult, profile)
            }

            // PAGE 31: 4-Year High School Master Action Plan (Grade 11 Intensive Sprint)
            document.writePage(31) { canvas ->
                MargdarshReportPages31to35.drawPage31Grade11(canvas, streamResult, profile)
            }

            // PAGE 32: 4-Year High School Master Action Plan (Grade 12 Apex Execution)
            document.writePage(32) { canvas ->
                MargdarshReportPages31to35.drawPage32Grade12(canvas, streamResult, profile)
            }

            // PAGE 33: Top 25 Tier-1 Entrance Exam Matrix
            document.writePage(33) { canvas ->
                MargdarshReportPages31to35.drawPage33ExamMatrix(canvas, profile)
            }

            // PAGE 34: Premier Institutional Directory & National Scholarship Guide
            document.writePage(34) { canvas ->
                MargdarshReportPages31to35.drawPage34InstitutionsAndScholarships(canvas, profile)
            }

            // PAGE 35: Master Counselor Certification, Verification Seal & Next Steps
            document.writePage(35) { canvas ->
                MargdarshReportPages31to35.drawPage35CounselorCertification(canvas, profile, detailedReport)
            }

            // EXTENDED 30 PAGES (PAGES 36 TO 65) - UNLOCKED WITH PREMIUM DOSSIER
            if (isUnlocked) {
                // PAGE 36: Holistic 360° Quotient Diagnosis & Dream Score
                document.writePage(36) { canvas ->
                    MargdarshReportPages36to50.drawPage36HolisticDreamScore(canvas, holisticMetrics, profile)
                }

                // PAGE 37: Neurological Brain Strength Map & 10 Cognitive Lobes
                document.writePage(37) { canvas ->
                    MargdarshReportPages36to50.drawPage37BrainStrengthMap(canvas, holisticMetrics, profile)
                }

                // PAGE 38: Gardner's Multiple Intelligences & Applied Cognitive Strengths
                document.writePage(38) { canvas ->
                    MargdarshReportPages36to50.drawPage38MultipleIntelligences(canvas, holisticMetrics, profile)
                }

                // PAGE 39: 2D Stream Quadrant Plot (Interest vs. Potential)
                document.writePage(39) { canvas ->
                    MargdarshReportPages36to50.drawPage39StreamQuadrantPlot(canvas, holisticMetrics, profile)
                }

                // PAGE 40: 2D Career Quadrant Plot (Interest vs. Potential)
                document.writePage(40) { canvas ->
                    MargdarshReportPages36to50.drawPage40CareerQuadrantPlot(canvas, holisticMetrics, profile)
                }

                // PAGE 41: National Peer Median Benchmark in India (Part 1: Cognitive)
                document.writePage(41) { canvas ->
                    MargdarshReportPages36to50.drawPage41PeerBenchmarkPart1(canvas, holisticMetrics, profile)
                }

                // PAGE 42: National Peer Median Benchmark in India (Part 2: Behavioral & Executive)
                document.writePage(42) { canvas ->
                    MargdarshReportPages36to50.drawPage42PeerBenchmarkPart2(canvas, holisticMetrics, profile)
                }

                // PAGE 43: 10 Bi-Directional Personality Continuums (Part 1: Continuums 1 to 5)
                document.writePage(43) { canvas ->
                    MargdarshReportPages36to50.drawPage43PersonalityContinuumsPart1(canvas, holisticMetrics, profile)
                }

                // PAGE 44: 10 Bi-Directional Personality Continuums (Part 2: Continuums 6 to 10)
                document.writePage(44) { canvas ->
                    MargdarshReportPages36to50.drawPage44PersonalityContinuumsPart2(canvas, holisticMetrics, profile)
                }

                // PAGE 45: Top-Rated Skills & Innate Superpowers
                document.writePage(45) { canvas ->
                    MargdarshReportPages36to50.drawPage45TopRatedSkills(canvas, holisticMetrics, profile)
                }

                // PAGE 46: Targeted Areas of Improvement & Growth Bottlenecks
                document.writePage(46) { canvas ->
                    MargdarshReportPages36to50.drawPage46ImprovementAreas(canvas, holisticMetrics, profile)
                }

                // PAGE 47: Prescriptive Learning Interventions & High-Return Habits
                document.writePage(47) { canvas ->
                    MargdarshReportPages36to50.drawPage47PrescriptiveInterventions(canvas, holisticMetrics, profile)
                }

                // PAGES 48 to 50: Dedicated Career Deep Dives (Careers 1, 2, 3)
                for (cIdx in 0..2) {
                    val pageNum = 48 + cIdx
                    document.writePage(pageNum) { canvas ->
                        MargdarshReportPages36to50.drawDedicatedCareerPageFromDossier(canvas, cIdx, pageNum, holisticMetrics, profile)
                    }
                }

                // PAGES 51 to 61: Dedicated Career Deep Dives (Careers 4 to 14)
                for (cIdx in 3..13) {
                    val pageNum = 51 + (cIdx - 3)
                    document.writePage(pageNum) { canvas ->
                        MargdarshReportPages51to65.drawDedicatedCareerPageFromDossier(canvas, cIdx, pageNum, holisticMetrics, profile)
                    }
                }

                // PAGE 62: Comprehensive 20-Career Compatibility Matrix & Leaderboard
                document.writePage(62) { canvas ->
                    MargdarshReportPages51to65.drawPage62CareerCompatibilityMatrix(canvas, holisticMetrics, profile)
                }

                // PAGE 63: Master National Entrance Test Roadmap & Milestones
                document.writePage(63) { canvas ->
                    MargdarshReportPages51to65.drawPage63EntranceTestRoadmap(canvas, holisticMetrics, profile)
                }

                // PAGE 64: Premier Institutional Directory & Scholarship Architecture
                document.writePage(64) { canvas ->
                    MargdarshReportPages51to65.drawPage64InstitutionalDirectory(canvas, holisticMetrics, profile)
                }

                // PAGE 65: Master Certification, Family Concordat & Verification Seal
                document.writePage(65) { canvas ->
                    MargdarshReportPages51to65.drawPage65HolisticSynthesisAndSeal(canvas, holisticMetrics, profile, detailedReport)
                }
            }

            // Write output file
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: context.filesDir
            val targetFile = File(downloadsDir, fileName)

            FileOutputStream(targetFile).use { out ->
                document.writeTo(out)
            }

            // Save to public Downloads directory on Android Q+
            MargdarshReportDrawingUtils.saveToPublicDownloads(context, targetFile, fileName)

            val toastMsg = if (isUnlocked) {
                "✓ Official 65-Page Premium Holistic Dossier Generated!\nSaved to Downloads: $fileName"
            } else {
                "✓ Official 35-Page PDF Dossier Generated!\nSaved to Downloads folder: $fileName"
            }

            Toast.makeText(
                context,
                toastMsg,
                Toast.LENGTH_LONG
            ).show()

            if (openAfterSave) {
                MargdarshReportDrawingUtils.openPdf(context, targetFile)
            }

            return targetFile
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "Error generating PDF: ${e.localizedMessage}",
                Toast.LENGTH_SHORT
            ).show()
            return null
        } finally {
            document.close()
        }
    }

    /**
     * Share the generated PDF dossier directly with WhatsApp, Gmail, Drive, etc.
     */
    fun shareReportPdf(
        context: Context,
        profile: StudentProfile,
        streamResult: StreamResult?,
        traits: Map<TraitKey, Int>,
        detailedReport: DetailedPsychometricAuditReport,
        careers: List<ScoredCareer>,
        parentAnswers: Map<String, String> = emptyMap(),
        studentAnswers: Map<String, String> = emptyMap(),
        isUnlocked: Boolean = false
    ) {
        val file = generateAndDownloadReportPdf(
            context = context,
            profile = profile,
            streamResult = streamResult,
            traits = traits,
            detailedReport = detailedReport,
            careers = careers,
            parentAnswers = parentAnswers,
            studentAnswers = studentAnswers,
            isUnlocked = isUnlocked,
            openAfterSave = false
        ) ?: return

        try {
            val authority = "${context.packageName}.fileprovider"
            val uri = androidx.core.content.FileProvider.getUriForFile(context, authority, file)

            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                putExtra(
                    android.content.Intent.EXTRA_SUBJECT,
                    "Official MargDarsh 35-Page Career Guidance Dossier - ${profile.studentName}"
                )
                putExtra(
                    android.content.Intent.EXTRA_TEXT,
                    "Attached is the official 35-Page Psychometric Assessment Dossier for ${profile.studentName}, prepared by MargDarsh National Career Guidance Council."
                )
                flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val chooser = android.content.Intent.createChooser(shareIntent, "Share 35-Page Career Dossier PDF")
            chooser.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Unable to open share sheet: ${e.localizedMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private inline fun PdfDocument.writePage(pageNumber: Int, drawBlock: (Canvas) -> Unit) {
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        val page = startPage(pageInfo)
        try {
            drawBlock(page.canvas)
        } finally {
            finishPage(page)
        }
    }
}
