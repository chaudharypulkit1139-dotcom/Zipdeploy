package com.example.margdarsh.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.ParentStudentComparativeDashboardData
import com.example.margdarsh.data.ParentStudentDivergenceEvaluator
import com.example.margdarsh.data.ScoredCareer
import com.example.margdarsh.data.StreamResult
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.data.TraitKey

object MargdarshReportPages21to30 {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 36f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)
    private const val TOTAL_PAGES = 35

    // =========================================================================
    // PAGE 21: Parent vs. Student Divergence Matrix & Comparative Study
    // =========================================================================
    fun drawPage21ComparativeMatrix(
        canvas: Canvas,
        profile: StudentProfile,
        traits: Map<TraitKey, Int>,
        streamResult: StreamResult?,
        detailedReport: DetailedPsychometricAuditReport,
        careers: List<ScoredCareer>,
        parentAnswers: Map<String, String>
    ) {
        val comparativeData: ParentStudentComparativeDashboardData = ParentStudentDivergenceEvaluator.evaluate(
            profile = profile,
            traits = traits,
            streamResult = streamResult,
            parentAnswers = parentAnswers,
            careers = careers
        )

        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 07: PARENT VS STUDENT COMPARATIVE STUDY", 21, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 72f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Parent vs. Student Comparative Divergence Matrix", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Empirical comparison between parental aspirations and student innate cognitive aptitude", MARGIN, currentY + 12f, paint)

        currentY += 24f

        // Cognitive Harmony Index Hero Card
        val harmonyCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 70f)
        paint.color = Color.parseColor("#002B66")
        canvas.drawRoundRect(harmonyCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#F5A623")
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("COGNITIVE HARMONY INDEX", MARGIN + 14f, currentY + 18f, paint)

        paint.color = Color.WHITE
        paint.textSize = 18f
        canvas.drawText("${comparativeData.overallHarmonyScore}%", MARGIN + 14f, currentY + 44f, paint)

        paint.textSize = 10f
        paint.color = Color.parseColor("#E0E7FF")
        val statusText = when {
            comparativeData.overallHarmonyScore >= 80 -> "High Synergy & Natural Consensus"
            comparativeData.overallHarmonyScore >= 60 -> "Moderate Alignment with Constructive Growth Vectors"
            else -> "Significant Divergence — Family Mediation Recommended"
        }
        canvas.drawText(statusText, MARGIN + 68f, currentY + 42f, paint)

        // Progress bar inside banner
        MargdarshReportDrawingUtils.drawProgressBar(
            canvas = canvas,
            x = MARGIN + 14f,
            y = currentY + 54f,
            width = CONTENT_WIDTH - 28f,
            height = 6f,
            progress = comparativeData.overallHarmonyScore / 100f,
            fillColor = Color.parseColor("#F5A623"),
            trackColor = Color.parseColor("#1E3A8A")
        )

        currentY += 80f

        // Executive Comparative Synthesis Box
        val synCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 75f)
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(synCard, 6f, 6f, paint)

        paint.color = Color.parseColor("#CBD5E1")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(synCard, 6f, 6f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("EXECUTIVE DIVERGENCE DIAGNOSTIC SYNTHESIS", MARGIN + 10f, currentY + 16f, paint)

        paint.color = Color.parseColor("#334155")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        MargdarshReportDrawingUtils.drawWrappedText(
            canvas,
            comparativeData.primaryDivergenceSummary,
            MARGIN + 10f,
            currentY + 30f,
            CONTENT_WIDTH - 20f,
            paint,
            10.5f
        )

        currentY += 85f

        // 4 Comparative Divergence Vectors
        val points = comparativeData.divergencePoints.take(4)
        for (dp in points) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 128f)
            paint.color = Color.parseColor("#FFFFFF")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            val statusColor = try {
                Color.parseColor(dp.divergenceColorHex)
            } catch (e: Exception) {
                Color.parseColor("#1D4ED8")
            }

            paint.color = statusColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1.2f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            // Dimension Header Bar
            paint.color = Color.parseColor("#F1F5F9")
            val barRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(barRect, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.parseColor("#002B66")
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(dp.dimension, MARGIN + 10f, currentY + 15f, paint)

            paint.color = statusColor
            paint.textSize = 8f
            canvas.drawText(dp.divergenceLevel, PAGE_WIDTH - MARGIN - 130f, currentY + 15f, paint)

            var vY = currentY + 34f

            // Parent vs Student Row
            paint.color = Color.parseColor("#1D4ED8")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Parent Expectation: ${dp.parentExpectation}", MARGIN + 10f, vY, paint)

            vY += 14f
            paint.color = Color.parseColor("#059669")
            canvas.drawText("Student Aptitude: ${dp.studentAptitude}", MARGIN + 10f, vY, paint)

            vY += 16f
            paint.color = Color.parseColor("#002B66")
            paint.textSize = 7.5f
            canvas.drawText("Counselor Bridging Strategy:", MARGIN + 10f, vY, paint)

            paint.color = Color.parseColor("#475569")
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            MargdarshReportDrawingUtils.drawWrappedText(canvas, dp.counselorBridgingStrategy, MARGIN + 10f, vY + 10f, CONTENT_WIDTH - 20f, paint, 9.5f)

            currentY += 136f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 21, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 22: Family Mediation Playbook & Educational Pact
    // =========================================================================
    fun drawPage22MediationPlaybook(
        canvas: Canvas,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 07: FAMILY MEDIATION PLAYBOOK & PACT", 22, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Family Mediation Playbook & Joint Educational Pact", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Structured conversation frameworks, psychological empathy scripts & mutual commitments", MARGIN, currentY + 14f, paint)

        currentY += 28f

        // Conversation scripts card
        val scripts = listOf(
            Triple(
                "Conversation 1: Handling Stream Disagreement",
                "Parent Approach: 'We want you to take Science for safety, but we hear your passion.'\nStudent Response: 'Let me show you the data from MargDarsh on high-growth careers in this stream.'",
                "Goal: Ground discussion in empirical psychometric benchmarks rather than emotional assumptions."
            ),
            Triple(
                "Conversation 2: Dealing with Exam Pressure & Mock Fluctuations",
                "Parent Approach: 'One mock test does not define your future; let's analyze the concept error log.'\nStudent Response: 'I appreciate your calm support; I need 45 minutes of silent error review.'",
                "Goal: Transform anxiety into structured iterative problem-solving."
            ),
            Triple(
                "Conversation 3: Budget & Long-Term Higher Education Alignment",
                "Parent Approach: 'Let us plan the scholarship and entrance exam targets for Tier-1 merit seats.'\nStudent Response: 'I commit to keeping my percentile in the top bracket to earn merit waivers.'",
                "Goal: Foster shared ownership of financial and academic milestones."
            )
        )

        for ((title, dialog, goal) in scripts) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 125f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor("#CBD5E1")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.parseColor("#002B66")
            val scHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(scHeader, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.WHITE
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 10f, currentY + 15f, paint)

            var inY = currentY + 34f
            paint.color = Color.parseColor("#1E293B")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            for (line in dialog.split("\n")) {
                canvas.drawText(line, MARGIN + 10f, inY, paint)
                inY += 12f
            }

            inY += 4f
            paint.color = Color.parseColor("#059669")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("🎯 $goal", MARGIN + 10f, inY, paint)

            currentY += 135f
        }

        currentY += 6f

        // Formal Joint Student-Parent Educational Pact Certificate Box
        val pactCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 45f)
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawRoundRect(pactCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#F5A623")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(pactCard, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("OFFICIAL FAMILY EDUCATIONAL CHARTER & PACT", MARGIN + 14f, currentY + 22f, paint)

        val commitments = listOf(
            "1. We agree to make high school academic decisions based on data, intrinsic strengths, and mental health.",
            "2. The student commits to 100% effort, consistent daily deep work, and proactive communication when feeling overwhelmed.",
            "3. The parents commit to creating an emotionally supportive home sanctuary free from destructive comparison.",
            "4. Both parties commit to reviewing this MargDarsh 35-Page Dossier quarterly to track milestone evolution."
        )

        var pactY = currentY + 40f
        paint.color = Color.parseColor("#334155")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        for (com in commitments) {
            pactY = MargdarshReportDrawingUtils.drawWrappedText(canvas, com, MARGIN + 14f, pactY, CONTENT_WIDTH - 28f, paint, 10.5f)
            pactY += 4f
        }

        // Signatures Line
        val sigY = PAGE_HEIGHT - 65f
        paint.color = Color.parseColor("#94A3B8")
        canvas.drawLine(MARGIN + 20f, sigY, MARGIN + 180f, sigY, paint)
        canvas.drawLine(PAGE_WIDTH - MARGIN - 180f, sigY, PAGE_WIDTH - MARGIN - 20f, sigY, paint)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Student Signature: ${profile.studentName.ifBlank { "Candidate" }}", MARGIN + 20f, sigY + 12f, paint)
        canvas.drawText("Parent Signature: ${profile.parentName.ifBlank { "Parent / Guardian" }}", PAGE_WIDTH - MARGIN - 180f, sigY + 12f, paint)

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 22, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 23: 10-Year Career Horizon & Industry Evolution Matrix 2025–2035
    // =========================================================================
    fun drawPage23IndustryHorizon(
        canvas: Canvas,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 08: 10-YEAR INDUSTRY EVOLUTION HORIZON", 23, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("10-Year Career Landscape & AI Disruption Matrix (2025–2035)", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Macroeconomic forecasting, AI automation immunity & sunrise domain emergence in India & globally", MARGIN, currentY + 14f, paint)

        currentY += 28f

        val domains = listOf(
            Triple(
                "1. Generative AI, Robotics & Quantum Systems",
                "AI Automation Risk: Low (Creator/Architect) • 10-Yr Growth: +340%",
                "Demand for algorithm designers, physical AI roboticists, and high-performance computing architects will dominate Tier-1 tech compensation."
            ),
            Triple(
                "2. Precision Medicine, Genomics & Biotechnology",
                "AI Automation Risk: Minimal (Physical/Clinical) • 10-Yr Growth: +280%",
                "Integration of CRISPR, bioinformatics, computational oncology, and healthcare AI creates massive demand for hybrid bio-tech leaders."
            ),
            Triple(
                "3. Clean Energy, Electric Mobility & Climate Tech",
                "AI Automation Risk: Low (Infrastructure/Policy) • 10-Yr Growth: +310%",
                "Transition to green hydrogen, EV powertrain engineering, battery chemistry, and carbon policy trading drives 100k+ high-paying engineering roles."
            ),
            Triple(
                "4. Fintech, Algorithmic Trading & Global Corporate Law",
                "AI Automation Risk: Moderate-Low (Complex Judgment) • 10-Yr Growth: +210%",
                "AI will automate basic spreadsheet work, elevating quantitative financial engineers, cross-border M&A lawyers, and sovereign fund analysts."
            ),
            Triple(
                "5. Human-Centered Design, AR/VR & Multidisciplinary Creative Tech",
                "AI Automation Risk: Low (Empathy & Taste) • 10-Yr Growth: +190%",
                "High-value design roles requiring spatial UX, cinematic storytelling, and aesthetic curation will see soaring prestige."
            )
        )

        for ((title, tag, desc) in domains) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 120f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor("#CBD5E1")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.parseColor("#002B66")
            val dHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(dHeader, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.WHITE
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 10f, currentY + 15f, paint)

            var inY = currentY + 36f
            paint.color = Color.parseColor("#059669")
            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(tag, MARGIN + 10f, inY, paint)

            inY += 14f
            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            MargdarshReportDrawingUtils.drawWrappedText(canvas, desc, MARGIN + 10f, inY, CONTENT_WIDTH - 20f, paint, 10.5f)

            currentY += 130f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 23, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGES 24 to 29: Top 6 Recommended Career Pathways (1 Dedicated Full Page per Career)
    // Features motivating, beautiful hour-by-hour "Day in the Life", compensation ladder,
    // recruiters & college trajectories with distinctive color palettes
    // =========================================================================
    fun drawDedicatedCareerPage(
        canvas: Canvas,
        career: ScoredCareer,
        rank: Int,
        pageNumber: Int,
        profile: StudentProfile
    ) {
        val chapterTitle = "CHAPTER 09: CAREER #$rank DEEP DIVE & DAY IN THE LIFE"
        MargdarshReportDrawingUtils.drawPageHeader(canvas, chapterTitle, pageNumber, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Select Distinct Theme Color per Career
        val (themePrimary, themeSecondary, themeBg) = when (rank) {
            1 -> Triple("#002B66", "#059669", "#ECFDF5") // Navy + Emerald Green
            2 -> Triple("#1E3A8A", "#2563EB", "#EFF6FF") // Sapphire + Royal Blue
            3 -> Triple("#9A3412", "#EA580C", "#FFF7ED") // Amber + Vibrant Coral
            4 -> Triple("#581C87", "#7C3AED", "#FAF5FF") // Royal Purple + Violet
            5 -> Triple("#115E59", "#0D9488", "#F0FDFA") // Deep Teal + Aquamarine
            else -> Triple("#831843", "#BE123C", "#FFF1F2") // Rose + Crimson
        }

        var currentY = 72f

        // Career Hero Header Card
        val heroRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 70f)
        paint.color = Color.parseColor(themePrimary)
        canvas.drawRoundRect(heroRect, 8f, 8f, paint)

        paint.color = Color.parseColor("#F5A623")
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("RANK #$rank RECOMMENDED CAREER PATHWAY", MARGIN + 14f, currentY + 18f, paint)

        paint.color = Color.WHITE
        paint.textSize = 15f
        canvas.drawText(career.career.title, MARGIN + 14f, currentY + 40f, paint)

        paint.color = Color.parseColor("#E0E7FF")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Tag: ${career.career.tag.displayName}  |  Fit Match: ${career.fit}%  |  10-Yr Outlook: High Growth", MARGIN + 14f, currentY + 56f, paint)

        currentY += 80f

        // Motivating, Beautiful "Day in the Life of a 2030 Professional" Card
        val dayRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 285f)
        paint.color = Color.parseColor(themeBg)
        canvas.drawRoundRect(dayRect, 8f, 8f, paint)

        paint.color = Color.parseColor(themeSecondary)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.2f
        canvas.drawRoundRect(dayRect, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        // Header
        paint.color = Color.parseColor(themePrimary)
        val dayHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 24f)
        canvas.drawRoundRect(dayHeader, 8f, 8f, paint)
        canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 24f, paint)

        paint.color = Color.WHITE
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("⚡ INSPIRATIONAL DAY IN THE LIFE (A GLIMPSE INTO YOUR 2030 SUCCESS)", MARGIN + 12f, currentY + 16f, paint)

        val schedule = listOf(
            Triple("08:30 AM", "Morning Strategy & Cognitive Priming", "Reviewing breakthrough telemetry, strategic priorities, and team sync over artisanal coffee."),
            Triple("10:00 AM", "Deep-Work Architecture & High-Stakes Focus", "Designing core system architecture, solving multi-variable problems, or conducting clinical trials."),
            Triple("01:30 PM", "Cross-Functional Collaboration & Mentorship", "Leading global design reviews, mentoring young researchers, and aligning with executive stakeholders."),
            Triple("04:00 PM", "Innovation Lab & Prototyping Experiments", "Testing novel AI models, running simulation iterations, and stress-testing new deployment pipelines."),
            Triple("07:30 PM", "Reflection, Continuous Mastery & Work-Life Equilibrium", "Reading industry whitepapers, athletic rejuvenation, and spending quality downtime with family.")
        )

        var schedY = currentY + 40f
        for ((time, title, desc) in schedule) {
            // Time Badge
            val timeBadge = RectF(MARGIN + 10f, schedY - 10f, MARGIN + 68f, schedY + 8f)
            paint.color = Color.parseColor(themeSecondary)
            canvas.drawRoundRect(timeBadge, 4f, 4f, paint)

            paint.color = Color.WHITE
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(time, MARGIN + 14f, schedY + 3f, paint)

            // Activity
            paint.color = Color.parseColor("#0F172A")
            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 76f, schedY + 2f, paint)

            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            schedY = MargdarshReportDrawingUtils.drawWrappedText(canvas, desc, MARGIN + 76f, schedY + 14f, CONTENT_WIDTH - 86f, paint, 9.5f)
            schedY += 8f
        }

        currentY += 295f

        // Salary & Compensation Progression Card
        val salaryRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 120f)
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(salaryRect, 6f, 6f, paint)

        paint.color = Color.parseColor("#CBD5E1")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(salaryRect, 6f, 6f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor(themePrimary)
        val salHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
        canvas.drawRoundRect(salHeader, 6f, 6f, paint)
        canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

        paint.color = Color.WHITE
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("COMPENSATION TRAJECTORY & CAREER LADDER (INDIA & GLOBAL)", MARGIN + 10f, currentY + 15f, paint)

        val entrySalary = career.career.salaryEntry.ifBlank { "₹8 – 18 LPA" }
        val expSalary = career.career.salaryExperienced.ifBlank { "₹35 – 70 LPA" }

        val salaries = listOf(
            Triple("Entry (0-2 Yrs)", entrySalary, "Associate / Junior Specialist"),
            Triple("Mid-Level (3-6 Yrs)", "₹20 – 45 LPA", "Senior Specialist / Lead Consultant"),
            Triple("Principal (7-11 Yrs)", expSalary, "Principal Architect / Practice Head"),
            Triple("Executive (12+ Yrs)", "₹1.2 – 3.5 Cr+", "VP / Managing Director / Partner")
        )

        var salColX = MARGIN + 12f
        val colWidth = (CONTENT_WIDTH - 24f) / 4f
        for ((tier, pay, role) in salaries) {
            paint.color = Color.parseColor("#475569")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(tier, salColX, currentY + 40f, paint)

            paint.color = Color.parseColor(themeSecondary)
            paint.textSize = 9.5f
            canvas.drawText(pay, salColX, currentY + 56f, paint)

            paint.color = Color.parseColor("#1E293B")
            paint.textSize = 7f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            MargdarshReportDrawingUtils.drawWrappedText(canvas, role, salColX, currentY + 70f, colWidth - 8f, paint, 9f)

            salColX += colWidth
        }

        currentY += 130f

        // Degree Roadmap & Top Indian Recruiters Box
        val degRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 45f)
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawRoundRect(degRect, 6f, 6f, paint)

        paint.color = Color.parseColor("#CBD5E1")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(degRect, 6f, 6f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor(themePrimary)
        val degHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
        canvas.drawRoundRect(degHeader, 6f, 6f, paint)
        canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

        paint.color = Color.WHITE
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("ACADEMIC DEGREES & PREMIER INDIAN RECRUITERS", MARGIN + 10f, currentY + 15f, paint)

        var degY = currentY + 36f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("🎓 Recommended Educational Roadmap:", MARGIN + 10f, degY, paint)

        val degreeText = if (career.career.roadmap.isNotBlank()) career.career.roadmap else "B.Tech / B.S. / B.Des / MBBS / BBA from Tier-1 Institutions (IITs, NITs, BITS, AIIMS, IIMs, NLUs) followed by optional specialized Master's / MBA."

        paint.color = Color.parseColor("#334155")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        degY = MargdarshReportDrawingUtils.drawWrappedText(
            canvas,
            degreeText,
            MARGIN + 10f,
            degY + 11f,
            CONTENT_WIDTH - 20f,
            paint,
            10f
        )

        degY += 6f
        paint.color = Color.parseColor("#059669")
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("🏢 Top Indian & Global Hiring Employers:", MARGIN + 10f, degY, paint)

        val recruiterText = if (career.career.topRecruiters.isNotBlank()) career.career.topRecruiters else "Google, Microsoft, ISRO, McKinsey & Company, Goldman Sachs, Apollo Hospitals, Tata Group, Reliance Industries, Adobe, Apple & high-growth venture-backed unicorns."

        paint.color = Color.parseColor("#334155")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        MargdarshReportDrawingUtils.drawWrappedText(
            canvas,
            recruiterText,
            MARGIN + 10f,
            degY + 11f,
            CONTENT_WIDTH - 20f,
            paint,
            10f
        )

        MargdarshReportDrawingUtils.drawPageFooter(canvas, pageNumber, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 30: 4-Year High School Master Action Plan (Grade 9 & 10 Execution)
    // =========================================================================
    fun drawPage30Grade9And10(
        canvas: Canvas,
        streamResult: StreamResult?,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 10: 4-YEAR BLUEPRINT (CLASS 9 & 10)", 30, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Class 9 & 10 Foundation & Discovery Blueprint", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Laying fundamental conceptual bedrock, Olympiad exposure & exploring intellectual passions", MARGIN, currentY + 14f, paint)

        currentY += 28f

        val grades = listOf(
            Triple(
                "CLASS 9: EXPLORATION, FOUNDATION & HABIT FORMATION",
                listOf(
                    "• Academic Foundation: Master NCERT Class 9 Math & Science fundamentals with zero conceptual gaps.",
                    "• Olympiads & Competitions: Appear for SOF NSO/IMO, SilverZone, and National Talent Search preliminary rounds.",
                    "• Cognitive Habit: Develop 90 minutes of daily distraction-free deep work reading outside school syllabus.",
                    "• Extracurricular: Participate in Robotics clubs, Model United Nations (MUN), or creative design portfolios."
                ),
                "#1D4ED8"
            ),
            Triple(
                "CLASS 10: BOARD EXCELLENCE & STREAM CALIBRATION",
                listOf(
                    "• Board Exam Mastery: Target 90%+ in 10th CBSE/ICSE Board with disciplined mock paper solving.",
                    "• Advanced Bridging: Begin Class 11 prerequisite concepts (Calculus basics, Vectors, Basic Accounting).",
                    "• Profile Building: Lead a school club initiative or complete a verified Coursera/edX beginner specialization.",
                    "• Final Stream Lock: Validate high school subject combinations with MargDarsh counseling by February."
                ),
                "#059669"
            )
        )

        for ((title, bullets, colorHex) in grades) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 160f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.color = Color.parseColor(colorHex)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1.2f
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.parseColor(colorHex)
            val gHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 26f)
            canvas.drawRoundRect(gHeader, 8f, 8f, paint)
            canvas.drawRect(MARGIN, currentY + 16f, PAGE_WIDTH - MARGIN, currentY + 26f, paint)

            paint.color = Color.WHITE
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 12f, currentY + 17f, paint)

            var bY = currentY + 44f
            paint.color = Color.parseColor("#1E293B")
            paint.textSize = 7.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            for (bullet in bullets) {
                bY = MargdarshReportDrawingUtils.drawWrappedText(canvas, bullet, MARGIN + 12f, bY, CONTENT_WIDTH - 24f, paint, 10.5f)
                bY += 6f
            }

            currentY += 174f
        }

        // Counselor Milestone Check Box
        val checkCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 45f)
        paint.color = Color.parseColor("#EFF6FF")
        canvas.drawRoundRect(checkCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#1D4ED8")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(checkCard, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor("#1D4ED8")
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("💡 Class 9-10 Key Milestone Checklist:", MARGIN + 12f, currentY + 20f, paint)

        paint.color = Color.parseColor("#1E293B")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val note = "Do not overload the student with hyper-intense competitive coaching in Class 9. Focus on cultivating intrinsic curiosity, scientific inquiry, and disciplined sleep rhythms. Class 10 is the year to build timed test-taking resilience."
        MargdarshReportDrawingUtils.drawWrappedText(canvas, note, MARGIN + 12f, currentY + 36f, CONTENT_WIDTH - 24f, paint, 11f)

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 30, TOTAL_PAGES)
    }
}
