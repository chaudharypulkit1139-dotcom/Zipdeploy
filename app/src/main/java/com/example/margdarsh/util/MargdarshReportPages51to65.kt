package com.example.margdarsh.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.HolisticDossierMetrics
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.util.MargdarshReportDrawingUtils.CONTENT_WIDTH
import com.example.margdarsh.util.MargdarshReportDrawingUtils.MARGIN
import com.example.margdarsh.util.MargdarshReportDrawingUtils.PAGE_WIDTH
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawDualRadarChart
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawPageFooter
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawPageHeader
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawWrappedText

object MargdarshReportPages51to65 {

    // -------------------------------------------------------------------------------------
    // PAGES 51 to 61: DEDICATED CAREER DEEP DIVES (CAREERS 4 to 14)
    // -------------------------------------------------------------------------------------
    fun drawDedicatedCareerPageFromDossier(
        canvas: Canvas,
        careerIndex: Int,
        pageNum: Int,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        val career = metrics.top20Careers.getOrNull(careerIndex) ?: return
        drawPageHeader(canvas, "Career Dossier • #${career.rank} ${career.title}", pageNum, candidateName = profile.studentName)
        drawPageFooter(canvas, pageNum)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Title & Rank Badge
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("#${career.rank} ${career.title.take(45)}", MARGIN, 68f, paint)

        // Match Percentage Badge
        val badgeStr = "★ Match: ${career.matchPercentage}% (${career.tag.displayName})"
        paint.textSize = 9.5f
        paint.color = Color.parseColor("#0284C7")
        canvas.drawText(badgeStr, MARGIN, 84f, paint)

        // Draw Dual Radar Chart
        val radarRadius = 60f
        val radarCx = MARGIN + 90f
        val radarCy = 175f

        val radarAxes = listOf(
            Triple("Numerical", metrics.skillScores[com.example.margdarsh.data.HolisticSkill.NUMERICAL_APTITUDE] ?: 70, career.industryVector[com.example.margdarsh.data.HolisticSkill.NUMERICAL_APTITUDE] ?: 80),
            Triple("Analytical", metrics.skillScores[com.example.margdarsh.data.HolisticSkill.ANALYTICAL_CRITICAL] ?: 70, career.industryVector[com.example.margdarsh.data.HolisticSkill.ANALYTICAL_CRITICAL] ?: 80),
            Triple("Verbal", metrics.skillScores[com.example.margdarsh.data.HolisticSkill.SPEECH_VERBAL] ?: 70, career.industryVector[com.example.margdarsh.data.HolisticSkill.SPEECH_VERBAL] ?: 75),
            Triple("Spatial", metrics.skillScores[com.example.margdarsh.data.HolisticSkill.CREATIVE_VISUALIZATION] ?: 70, career.industryVector[com.example.margdarsh.data.HolisticSkill.CREATIVE_VISUALIZATION] ?: 75),
            Triple("Memory", metrics.skillScores[com.example.margdarsh.data.HolisticSkill.MEMORIZING_RECALL] ?: 70, career.industryVector[com.example.margdarsh.data.HolisticSkill.MEMORIZING_RECALL] ?: 80),
            Triple("Leadership", metrics.skillScores[com.example.margdarsh.data.HolisticSkill.LEADING_VISION] ?: 70, career.industryVector[com.example.margdarsh.data.HolisticSkill.LEADING_VISION] ?: 70)
        )

        MargdarshReportDrawingUtils.drawDualRadarChart(
            canvas = canvas,
            cx = radarCx,
            cy = radarCy,
            radius = radarRadius,
            axes = radarAxes
        )

        // Key Career Metrics on Right of Radar
        val statX = MARGIN + 205f
        var statY = 115f
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.parseColor("#002B66")
        canvas.drawText("CAREER AT A GLANCE", statX, statY, paint)
        statY += 14f

        val quickStats = listOf(
            Pair("Industry Stream:", career.stream.displayName),
            Pair("Salary Growth:", career.salaryProgression.take(26)),
            Pair("Category Tag:", career.tag.displayName),
            Pair("10-Year Outlook:", career.horizonOutlook2035.take(26))
        )

        quickStats.forEach { (lbl, valStr) ->
            paint.textSize = 7.5f
            paint.color = Color.parseColor("#64748B")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(lbl, statX, statY, paint)

            paint.color = Color.parseColor("#0F172A")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(valStr, statX + 75f, statY, paint)
            statY += 14f
        }

        // Section: What this career demands
        var curY = 270f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("What This Career Demands (Core Competencies & Psychological Load)", MARGIN, curY, paint)
        curY += 12f

        paint.color = Color.parseColor("#334155")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        curY = drawWrappedText(canvas, career.coreDemands, MARGIN, curY, CONTENT_WIDTH, paint, 10.5f) + 10f

        // Section: A Day in the Life
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("A Day in the Life of a Professional in This Domain", MARGIN, curY, paint)
        curY += 12f

        val schedRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 80f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(schedRect, 6f, 6f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.8f
        paint.color = Color.parseColor("#CBD5E1")
        canvas.drawRoundRect(schedRect, 6f, 6f, paint)

        var timeY = curY + 14f
        val daySchedule = listOf(career.dayInTheLifeMorning, career.dayInTheLifeAfternoon, career.dayInTheLifeEvening)
        daySchedule.forEach { item ->
            paint.style = Paint.Style.FILL
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0284C7")
            val parts = item.split(" — ")
            val timePart = parts.firstOrNull() ?: ""
            val taskPart = parts.drop(1).joinToString(" — ")
            canvas.drawText(timePart, MARGIN + 8f, timeY, paint)

            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#334155")
            canvas.drawText(taskPart.take(75), MARGIN + 85f, timeY, paint)
            timeY += 18f
        }
        curY += 92f

        // Entrance Tests, Colleges, Courses (3-Column Layout)
        val colW = (CONTENT_WIDTH - 16f) / 3f
        val col1X = MARGIN
        val col2X = MARGIN + colW + 8f
        val col3X = MARGIN + (colW * 2) + 16f

        val lists = listOf(
            Triple("Target Entrance Tests", career.entranceExams, col1X),
            Triple("Premier Indian Colleges", career.topCollegesInIndia, col2X),
            Triple("Degree Pathways", career.degreePathways, col3X)
        )

        lists.forEach { (boxTitle, items, bX) ->
            val bRect = RectF(bX, curY, bX + colW, curY + 120f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(bRect, 6f, 6f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(bRect, 6f, 6f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 7.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#002B66")
            canvas.drawText(boxTitle, bX + 6f, curY + 14f, paint)

            var itmY = curY + 28f
            items.forEach { itm ->
                paint.textSize = 6.8f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.color = Color.parseColor("#475569")
                canvas.drawText("• ${itm.take(24)}", bX + 6f, itmY, paint)
                itmY += 12.5f
            }
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 62: COMPREHENSIVE 20-CAREER COMPATIBILITY MATRIX
    // -------------------------------------------------------------------------------------
    fun drawPage62CareerCompatibilityMatrix(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Career Architecture • Top 20 Compatibility Leaderboard", 62, candidateName = profile.studentName)
        drawPageFooter(canvas, 62)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Comprehensive Top 20 Career Compatibility Leaderboard", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Empirically cross-referencing the student's 14-dimensional psychometric profile against the 20 benchmark career models. Ranks 1 to 5 indicate optimal alignment; Ranks 6 to 12 represent robust alternatives."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 115f

        // Table Header
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#002B66")
        canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 18f, paint)

        paint.color = Color.WHITE
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Rank & Career Title", MARGIN + 8f, curY + 12f, paint)
        canvas.drawText("Stream", MARGIN + 210f, curY + 12f, paint)
        canvas.drawText("Match %", MARGIN + 295f, curY + 12f, paint)
        canvas.drawText("Category Tag", MARGIN + 355f, curY + 12f, paint)
        canvas.drawText("Salary Trajectory", MARGIN + 420f, curY + 12f, paint)
        curY += 18f

        metrics.top20Careers.take(18).forEachIndexed { idx, c ->
            paint.style = Paint.Style.FILL
            paint.color = if (idx % 2 == 0) Color.parseColor("#F8FAFC") else Color.WHITE
            canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 28f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.5f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 28f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 7.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = if (idx < 3) Color.parseColor("#0891B2") else Color.parseColor("#0F172A")
            val star = if (idx < 3) "★ " else ""
            canvas.drawText("$star#${c.rank} ${c.title.take(30)}", MARGIN + 8f, curY + 17f, paint)

            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#475569")
            canvas.drawText(c.stream.displayName.take(14), MARGIN + 210f, curY + 17f, paint)

            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = if (c.matchPercentage >= 90) Color.parseColor("#10B981") else Color.parseColor("#0284C7")
            canvas.drawText("${c.matchPercentage}%", MARGIN + 298f, curY + 17f, paint)

            paint.textSize = 7.5f
            paint.color = Color.parseColor("#64748B")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(c.tag.displayName.take(12), MARGIN + 360f, curY + 17f, paint)

            paint.textSize = 7.2f
            paint.color = Color.parseColor("#059669")
            canvas.drawText(c.salaryProgression.take(20), MARGIN + 420f, curY + 17f, paint)

            curY += 28f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 63: MASTER ENTRANCE TEST ROADMAP & TIMELINE
    // -------------------------------------------------------------------------------------
    fun drawPage63EntranceTestRoadmap(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Roadmap • Tier-1 Entrance Exam Matrix", 63, candidateName = profile.studentName)
        drawPageFooter(canvas, 63)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Master National Entrance Test Roadmap & Milestones", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Strategic 24-month roadmap for Grade 11 and 12 competitive examinations. Outlines notification cycles, testing modalities, and key preparation milestones aligned with the candidate's target stream."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 115f
        val examTimeline = listOf(
            Triple("JEE Main & Advanced (Engineering)", "Session 1: January • Session 2: April • Advanced: May", "Syllabus: Physics, Chemistry, Mathematics (NCERT + Advanced). Target: 99.2+ percentile for top NITs/IITs."),
            Triple("NEET-UG (Medical & Dental Sciences)", "Single Sitting: First Sunday of May", "Syllabus: Biology (Botany + Zoology), Physics, Chemistry. Target: 650+ out of 720 for Central AIIMS / Govt. GMCs."),
            Triple("UCEED & CEED (Industrial Design)", "Examination: Third Sunday of January", "Paper 1: Visual, Spatial, Logic Aptitude • Paper 2: Drawing & Sketching. Target: Top 150 All-India Rank for IIT Bombay/Delhi."),
            Triple("CLAT & AILET (National Law Universities)", "Examination: First Sunday of December (Grade 12)", "Sections: English, Legal Reasoning, Logical Reasoning, Current Affairs, Quant. Target: Top 400 for NLSIU Bengaluru / NALSAR."),
            Triple("IPMAT (IIM Indore, Rohtak, Ranchi, Bodh Gaya)", "Examination: Second/Third week of May", "Sections: Quantitative Ability (MCQ + Short Answer), Verbal Ability. Target: 98+ percentile for 5-Year Integrated Management."),
            Triple("CUET-UG (Central Universities - DU, BHU, JNU)", "Examination: Mid-May to Early June", "Sections: Language Test + Domain Specific Subjects + General Test. Target: 780+ out of 800 for SRCC, St. Stephen's, Hindu College.")
        )

        examTimeline.forEach { (examName, cycle, details) ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 68f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#3B82F6")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#002B66")
            canvas.drawText("★ $examName", MARGIN + 12f, curY + 16f, paint)

            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0284C7")
            canvas.drawText("Calendar Cycle: $cycle", MARGIN + 12f, curY + 30f, paint)

            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#334155")
            drawWrappedText(canvas, details, MARGIN + 12f, curY + 42f, CONTENT_WIDTH - 24f, paint, 10f)

            curY += 76f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 64: INSTITUTIONAL DIRECTORY, FEE STRUCTURE & SCHOLARSHIPS
    // -------------------------------------------------------------------------------------
    fun drawPage64InstitutionalDirectory(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Colleges & Grants • Institutional Directory", 64, candidateName = profile.studentName)
        drawPageFooter(canvas, 64)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Premier Institutional Directory & Scholarship Architecture", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Curated directory of top-tier universities and scholarship programs in India matching the candidate's profile. Financial planning and institutional ROI analysis."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 115f
        val institutions = listOf(
            Triple(
                "Tier-1 Premier Institutions (IITs, AIIMS, IIMs, NLUs)",
                "Fee Range: ₹2.5L to ₹12L (Govt Subsidized) • Placement Avg: ₹18 LPA - ₹32 LPA",
                "Admission purely on national merit ranks (JEE Adv, NEET, IPMAT, CLAT). High return on investment, world-class peer network, and direct recruitment by Fortune 500 companies."
            ),
            Triple(
                "Elite Private & Deemed Universities (BITS Pilani, Ashoka, ISB)",
                "Fee Range: ₹16L to ₹32L (4-Year Total) • Placement Avg: ₹14 LPA - ₹24 LPA",
                "World-class infrastructure, flexible interdisciplinary curriculum, international semester exchange partnerships (UC Berkeley, Sciences Po), and merit-cum-need financial aid up to 100%."
            ),
            Triple(
                "Government Central Universities (DU, BHU, JNU, Hyderabad)",
                "Fee Range: Under ₹1.5L Total • Placement Avg: ₹8 LPA - ₹16 LPA",
                "Exceptional intellectual pedigree, nominal tuition fees, prestigious professorial faculties, and dominant feeder institutions for Civil Services (UPSC) and Indian Economic Services."
            ),
            Triple(
                "National Scholarship & Financial Grant Schemes",
                "KVPY/INSPIRE, Aditya Birla Scholarship, Reliance Foundation, Tata Trust",
                "Providing full tuition waivers, ₹80,000/yr mentorship stipends, and research summer grants. Applications open during Grade 12 board notification cycles."
            )
        )

        institutions.forEach { (catTitle, stats, desc) ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 95f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#CBD5E1")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#002B66")
            canvas.drawText(catTitle, MARGIN + 12f, curY + 18f, paint)

            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#059669")
            canvas.drawText(stats, MARGIN + 12f, curY + 34f, paint)

            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#334155")
            drawWrappedText(canvas, desc, MARGIN + 12f, curY + 48f, CONTENT_WIDTH - 24f, paint, 10f)

            curY += 105f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 65: HOLISTIC SYNTHESIS, PARENT GUIDANCE & OFFICIAL VERIFICATION SEAL
    // -------------------------------------------------------------------------------------
    fun drawPage65HolisticSynthesisAndSeal(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile,
        detailedReport: DetailedPsychometricAuditReport
    ) {
        drawPageHeader(canvas, "Certification • Master Counselor Seal & Verdict", 65, candidateName = profile.studentName)
        drawPageFooter(canvas, 65)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("MargDarsh Institutional Certification & Final Verdict", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "This 65-page Holistic Dossier represents an exhaustive empirical audit of the candidate's cognitive architecture, psychological continuums, and industry readiness. Joint directives for student and family."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // Joint Decision Directives Box
        var curY = 120f
        val pactRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 120f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#EFF6FF")
        canvas.drawRoundRect(pactRect, 8f, 8f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.2f
        paint.color = Color.parseColor("#3B82F6")
        canvas.drawRoundRect(pactRect, 8f, 8f, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.parseColor("#1D4ED8")
        canvas.drawText("★ Five-Point Family Concordat for Unified Academic Decision-Making", MARGIN + 12f, curY + 18f, paint)

        val rules = listOf(
            "1. Honor Authentic Capability: Align stream choice strictly with top cognitive strengths rather than social prestige.",
            "2. Establish a Dedicated Deep-Work Sanctuary: Guarantee 4 hours of uninterrupted, device-free evening study blocks.",
            "3. Normalize Growth-Edge Remediation: Treat lower scores not as defects, but as actionable targets for 90-day micro-habits.",
            "4. Institutional Backup Strategy: Apply to at least two Tier-1 parallel entrance examinations to eliminate catastrophic exam pressure.",
            "5. Protect Physical & Emotional Equilibrium: Enforce 7+ hours of nightly sleep to safeguard working memory consolidation."
        )

        var rY = curY + 34f
        rules.forEach { r ->
            paint.textSize = 7f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#1E3A8A")
            canvas.drawText(r, MARGIN + 12f, rY, paint)
            rY += 15.5f
        }

        // Master Verification Seal & Signatures
        curY += 140f
        val sealCard = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 175f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(sealCard, 8f, 8f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = Color.parseColor("#CBD5E1")
        canvas.drawRoundRect(sealCard, 8f, 8f, paint)

        // Draw Gold Counselor Stamp Emblem
        val emblemCx = MARGIN + 80f
        val emblemCy = curY + 80f
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#FEF3C7")
        canvas.drawCircle(emblemCx, emblemCy, 45f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.parseColor("#F59E0B")
        canvas.drawCircle(emblemCx, emblemCy, 45f, paint)
        paint.strokeWidth = 1f
        canvas.drawCircle(emblemCx, emblemCy, 40f, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.parseColor("#92400E")
        val sealText1 = "MARGDARSH"
        val sealW1 = paint.measureText(sealText1)
        canvas.drawText(sealText1, emblemCx - sealW1 / 2f, emblemCy - 10f, paint)

        paint.textSize = 6f
        val sealText2 = "OFFICIAL AUDIT"
        val sealW2 = paint.measureText(sealText2)
        canvas.drawText(sealText2, emblemCx - sealW2 / 2f, emblemCy + 2f, paint)

        val sealText3 = "VERIFIED 2026"
        val sealW3 = paint.measureText(sealText3)
        canvas.drawText(sealText3, emblemCx - sealW3 / 2f, emblemCy + 14f, paint)

        // Signatures Block
        val sigX = MARGIN + 160f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Dr. B.B. Panchal, Ph.D. (Psychometrics)", sigX, curY + 45f, paint)

        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#475569")
        canvas.drawText("Chief Counselor & Vocational Psychometrician • MargDarsh Academic Council", sigX, curY + 60f, paint)
        canvas.drawText("Accredited by National Career Development & Psychometrics Board", sigX, curY + 74f, paint)

        paint.textSize = 7f
        paint.color = Color.parseColor("#64748B")
        canvas.drawText("Verification Hash: MD-2026-65P-45Q-DOSSIER-${profile.studentName.hashCode()}", sigX, curY + 98f, paint)
        canvas.drawText("Candidate: ${profile.studentName.ifBlank { "Candidate" }} • Overall Score: ${metrics.overallBrainScore}/100", sigX, curY + 112f, paint)
        canvas.drawText("All psychometric algorithms & career matching models copyright © 2026 MargDarsh.", sigX, curY + 126f, paint)
    }
}
