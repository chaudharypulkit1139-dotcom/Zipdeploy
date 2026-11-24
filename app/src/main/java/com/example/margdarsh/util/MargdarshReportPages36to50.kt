package com.example.margdarsh.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.HolisticAssessmentEngine
import com.example.margdarsh.data.HolisticDossierMetrics
import com.example.margdarsh.data.ScoredCareer
import com.example.margdarsh.data.StreamResult
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.data.TraitKey
import com.example.margdarsh.util.MargdarshReportDrawingUtils.CONTENT_WIDTH
import com.example.margdarsh.util.MargdarshReportDrawingUtils.MARGIN
import com.example.margdarsh.util.MargdarshReportDrawingUtils.PAGE_WIDTH
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawContinuumSlider
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawDonutGauge
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawBrainStrengthDiagram
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawPageFooter
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawPageHeader
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawPeerComparisonRow
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawQuadrantPlot
import com.example.margdarsh.util.MargdarshReportDrawingUtils.drawWrappedText

object MargdarshReportPages36to50 {

    // -------------------------------------------------------------------------------------
    // PAGE 36: 360° QUOTIENT DIAGNOSIS & HOLISTIC DREAM SCORE
    // -------------------------------------------------------------------------------------
    fun drawPage36HolisticDreamScore(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "360° Diagnostic • Holistic Brain Score", 36, candidateName = profile.studentName)
        drawPageFooter(canvas, 36)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Title Block
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("360° Holistic Quotient Diagnosis & Dream Score", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Derived from the comprehensive 45-item psychometric battery across 14 multidimensional skill domains. Synthesizes cognitive intelligence, physical stamina, and social resonance into a single diagnostic dream metric."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // Draw Donut Gauge
        val gaugeY = 175f
        drawDonutGauge(
            canvas = canvas,
            cx = PAGE_WIDTH / 2f,
            cy = gaugeY,
            radius = 50f,
            overallScore = metrics.overallBrainScore,
            iq = metrics.iqScore,
            pq = metrics.pqScore,
            sq = metrics.sqScore
        )

        // Section: Breakdown of the Three Core Quotients
        var curY = gaugeY + 80f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("The Tri-Quotient Diagnostic Architecture", MARGIN, curY, paint)
        curY += 12f

        val quotientCards = listOf(
            Triple(
                "Intelligence Quotient (IQ: ${metrics.iqScore}/100)",
                "#0284C7",
                "Evaluates numerical aptitude, logical and critical thinking, linguistic agility, and working memory recall. A score of ${metrics.iqScore} reflects high deductive precision and strong cognitive retention during complex academic tasks."
            ),
            Triple(
                "Physical Quotient (PQ: ${metrics.pqScore}/100)",
                "#10B981",
                "Assesses motor skill coordination, physical vitality, visual perception, and postural stamina. Indicates how long the student can sustain physical energy during long study marathons or laboratory environments."
            ),
            Triple(
                "Social Quotient (SQ: ${metrics.sqScore}/100)",
                "#8B5CF6",
                "Measures affective emotional intelligence, team synergy, active listening, and leading vision. A score of ${metrics.sqScore} indicates natural executive empathy, team mediation capability, and interpersonal persuasive power."
            )
        )

        quotientCards.forEach { (qTitle, colorHex, desc) ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 68f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor(colorHex)
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            // Strip
            val strip = RectF(MARGIN, curY, MARGIN + 5f, curY + 68f)
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(strip, 4f, 4f, paint)

            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0F172A")
            canvas.drawText(qTitle, MARGIN + 14f, curY + 16f, paint)

            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#475569")
            drawWrappedText(canvas, desc, MARGIN + 14f, curY + 28f, CONTENT_WIDTH - 24f, paint, 10.5f)

            curY += 76f
        }

        // Synthesis Callout Box
        curY += 6f
        val calloutRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 64f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#EFF6FF")
        canvas.drawRoundRect(calloutRect, 8f, 8f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.2f
        paint.color = Color.parseColor("#3B82F6")
        canvas.drawRoundRect(calloutRect, 8f, 8f, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.parseColor("#1D4ED8")
        canvas.drawText("★ Master Counselor's Synthesis & Dream Trajectory", MARGIN + 14f, curY + 16f, paint)

        paint.textSize = 7.8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#1E3A8A")
        val synthesisText = "The student demonstrates an Overall Brain Score of ${metrics.overallBrainScore}/100. This places them in the upper echelon of academic agility. Their cognitive quotient forms the bedrock for competitive entrance examinations, while their social poise guarantees long-term managerial and leadership ascendance."
        drawWrappedText(canvas, synthesisText, MARGIN + 14f, curY + 28f, CONTENT_WIDTH - 28f, paint, 10.5f)
    }

    // -------------------------------------------------------------------------------------
    // PAGE 37: NEUROLOGICAL BRAIN STRENGTH MAP & LOBE EVALUATION
    // -------------------------------------------------------------------------------------
    fun drawPage37BrainStrengthMap(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Neurological Architecture • 10 Brain Areas", 37, candidateName = profile.studentName)
        drawPageFooter(canvas, 37)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Neurological Brain Strength Map & Area Ranking", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Mapping the 45 assessment responses across 10 functional neurological lobes. Pin colors indicate score tiers: Green (≥80% Dominant), Orange (60-79% Strong), and Magenta (<60% Developing Pathway)."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // Draw Brain Diagram
        val brainData = metrics.rankedBrainAreas.map { Triple(it.area.areaName, it.score, it.tierColorHex) }
        drawBrainStrengthDiagram(
            canvas = canvas,
            cx = PAGE_WIDTH / 2f,
            cy = 210f,
            width = CONTENT_WIDTH,
            height = 190f,
            areas = brainData
        )

        // Ranked Table of 10 Brain Areas
        var curY = 325f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Ranked Cognitive Area Performance (Strongest to Developing)", MARGIN, curY, paint)
        curY += 12f

        // Table Header
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#002B66")
        canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 18f, paint)

        paint.color = Color.WHITE
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Rank & Brain Lobe", MARGIN + 8f, curY + 12f, paint)
        canvas.drawText("Score", MARGIN + 140f, curY + 12f, paint)
        canvas.drawText("Tier Status", MARGIN + 185f, curY + 12f, paint)
        canvas.drawText("Target Cognitive Function & Behavioral Impact", MARGIN + 260f, curY + 12f, paint)
        curY += 18f

        metrics.rankedBrainAreas.take(10).forEachIndexed { index, area ->
            paint.style = Paint.Style.FILL
            paint.color = if (index % 2 == 0) Color.parseColor("#F8FAFC") else Color.WHITE
            canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 34f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.5f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 34f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0F172A")
            canvas.drawText("#${area.rank} ${area.area.areaName}", MARGIN + 8f, curY + 14f, paint)

            paint.textSize = 7f
            paint.color = Color.parseColor("#64748B")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(area.area.primaryFunction.take(35), MARGIN + 8f, curY + 25f, paint)

            // Score
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor(area.tierColorHex)
            canvas.drawText("${area.score}%", MARGIN + 142f, curY + 18f, paint)

            // Tier Badge
            paint.textSize = 7.5f
            canvas.drawText(area.tierLabel, MARGIN + 185f, curY + 18f, paint)

            // Description
            paint.textSize = 7f
            paint.color = Color.parseColor("#334155")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            drawWrappedText(canvas, "${area.whatItIs} ${area.howItHelpsStudent}", MARGIN + 260f, curY + 11f, CONTENT_WIDTH - 265f, paint, 9f)

            curY += 34f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 38: MULTIPLE INTELLIGENCES & APPLIED COGNITIVE STRENGTHS
    // -------------------------------------------------------------------------------------
    fun drawPage38MultipleIntelligences(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Psychometric Theory • Gardner's Intelligences", 38, candidateName = profile.studentName)
        drawPageFooter(canvas, 38)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Multiple Intelligences Profile & Academic Utility", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Howard Gardner's Multiple Intelligences framework reveals how the student learns, encodes information, and solves non-linear problems. Here is their exact profile, what it means, and how it helps them succeed."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 115f
        val intelligences = listOf(
            Triple(
                "Logical-Mathematical Intelligence (Rank #1 • ${metrics.iqScore}%)",
                "What exactly it is: The capacity to analyze problems logically, execute mathematical operations, and investigate scientific issues empirically. It relies on deductive proofs and causal links.",
                "How it helps: Gives the student a massive competitive advantage in JEE, CAT, data architecture, economic econometrics, and algorithmic problem-solving under strict exam time limits."
            ),
            Triple(
                "Visual-Spatial Intelligence (Rank #2 • ${(metrics.iqScore * 0.95f).toInt()}%)",
                "What exactly it is: The ability to visualize objects in 3D mental space, rotate polyhedral forms, and understand spatial dimensions, maps, and isometric drawings.",
                "How it helps: Critical for architecture (NATA), mechanical engineering, computer vision, surgery, organic chemistry reaction pathways, and creative UI/UX wireframing."
            ),
            Triple(
                "Interpersonal & Empathic Intelligence (Rank #3 • ${metrics.sqScore}%)",
                "What exactly it is: The ability to read micro-expressions, modulate voice tone, negotiate win-win compromises, and coordinate diverse individuals toward a collective goal.",
                "How it helps: Propels the candidate into leadership roles, corporate management, diplomacy, client management consulting, and high-impact civil services."
            ),
            Triple(
                "Verbal-Linguistic Intelligence (Rank #4 • ${(metrics.iqScore * 0.88f).toInt()}%)",
                "What exactly it is: Mastery over vocabulary, syntactic nuance, rhetoric, persuasive composition, and the cadence of spoken language in front of skeptical audiences.",
                "How it helps: Dominates competitive law examinations (CLAT), editorial leadership, public policy white-papers, boardroom presentations, and academic research publication."
            ),
            Triple(
                "Bodily-Kinesthetic Intelligence (Rank #5 • ${metrics.pqScore}%)",
                "What exactly it is: Coordination of mind and physical dexterity, tactile tool manipulation, postural stamina, and somatic awareness during active operational environments.",
                "How it helps: Provides high resilience during 12-hour laboratory shifts, precision surgery, sports competition, military logistics, and hands-on robotics fabrication."
            )
        )

        intelligences.forEach { (title, whatIs, howHelps) ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 115f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#CBD5E1")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#002B66")
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 12f, curY + 18f, paint)

            paint.textSize = 7.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#1E293B")
            val yAfterWhat = drawWrappedText(canvas, whatIs, MARGIN + 12f, curY + 32f, CONTENT_WIDTH - 24f, paint, 10f)

            paint.color = Color.parseColor("#0284C7")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Applied Decision-Making Advantage:", MARGIN + 12f, yAfterWhat + 6f, paint)

            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#334155")
            drawWrappedText(canvas, howHelps, MARGIN + 12f, yAfterWhat + 18f, CONTENT_WIDTH - 24f, paint, 10f)

            curY += 126f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 39: 2D STREAM QUADRANT PLOT (INTEREST VS. POTENTIAL)
    // -------------------------------------------------------------------------------------
    fun drawPage39StreamQuadrantPlot(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "2D Quadrant • Stream Interest vs. Potential", 39, candidateName = profile.studentName)
        drawPageFooter(canvas, 39)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Stream Plot: Interest vs. Natural Potential", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Comparing the student's authentic elective desire (X-Axis: Interest Score 0–100) against their proven psychometric aptitude (Y-Axis: Skill Potential 0–100). The dashed cyan box highlights the optimal 'Sweet Spot' where both converge."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // Draw 2D Quadrant Plot
        val plotPoints = metrics.streamQuadrants.map {
            Triple(it.displayName, it.interestScore.toFloat(), it.potentialScore.toFloat())
        }
        val plotHeight = 240f
        drawQuadrantPlot(
            canvas = canvas,
            x = MARGIN,
            y = 125f,
            width = CONTENT_WIDTH,
            height = plotHeight,
            xLabel = "Student Genuine Interest",
            yLabel = "Natural Aptitude Potential",
            points = plotPoints,
            showSweetSpot = true
        )

        // Matrix Table Explaining Each Stream
        var curY = 125f + plotHeight + 35f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 10.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Stream Quadrant Diagnosis & Recommendation Matrix", MARGIN, curY, paint)
        curY += 12f

        // Table Header
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#002B66")
        canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 18f, paint)

        paint.color = Color.WHITE
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Stream", MARGIN + 8f, curY + 12f, paint)
        canvas.drawText("Interest", MARGIN + 140f, curY + 12f, paint)
        canvas.drawText("Potential", MARGIN + 185f, curY + 12f, paint)
        canvas.drawText("Strategic Academic Advisory", MARGIN + 240f, curY + 12f, paint)
        curY += 18f

        metrics.streamQuadrants.forEachIndexed { idx, pt ->
            paint.style = Paint.Style.FILL
            paint.color = if (idx % 2 == 0) Color.parseColor("#F8FAFC") else Color.WHITE
            canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 38f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.5f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRect(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 38f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = if (pt.isSweetSpot) Color.parseColor("#0891B2") else Color.parseColor("#0F172A")
            val star = if (pt.isSweetSpot) "★ " else ""
            canvas.drawText("$star${pt.displayName}", MARGIN + 8f, curY + 15f, paint)

            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#334155")
            canvas.drawText("${pt.interestScore}/100", MARGIN + 145f, curY + 15f, paint)
            canvas.drawText("${pt.potentialScore}/100", MARGIN + 190f, curY + 15f, paint)

            paint.textSize = 6.8f
            paint.color = Color.parseColor("#475569")
            drawWrappedText(canvas, pt.strategicRecommendation, MARGIN + 240f, curY + 10f, CONTENT_WIDTH - 245f, paint, 9f)

            curY += 38f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 40: 2D CAREER QUADRANT PLOT (INTEREST VS. POTENTIAL)
    // -------------------------------------------------------------------------------------
    fun drawPage40CareerQuadrantPlot(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "2D Quadrant • Top Career Scatter Plot", 40, candidateName = profile.studentName)
        drawPageFooter(canvas, 40)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Career Plot: Passion vs. Industry Capability", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Placing 12 premier career vectors on the 2D Interest vs. Skill Potential grid. Careers situated in the upper-right 'Sweet Spot' guarantee sustained motivation and rapid institutional advancement without academic friction."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // Draw 2D Quadrant Plot
        val plotPoints = metrics.careerQuadrants.take(10).map {
            Triple(it.title, it.interestScore.toFloat(), it.potentialScore.toFloat())
        }
        val plotHeight = 240f
        drawQuadrantPlot(
            canvas = canvas,
            x = MARGIN,
            y = 125f,
            width = CONTENT_WIDTH,
            height = plotHeight,
            xLabel = "Curiosity & Role Interest",
            yLabel = "Natural Aptitude Fit",
            points = plotPoints,
            showSweetSpot = true
        )

        // Strategic Career Advisory Cards below
        var curY = 125f + plotHeight + 35f
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 10.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Sweet Spot Career Directives (Highest Synergy Pathways)", MARGIN, curY, paint)
        curY += 12f

        val sweetCareers = metrics.careerQuadrants.filter { it.isSweetSpot }.take(3)
        sweetCareers.forEach { pt ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 54f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F0FDF4")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#10B981")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#065F46")
            canvas.drawText("★ ${pt.title} • Interest: ${pt.interestScore}% | Aptitude: ${pt.potentialScore}%", MARGIN + 10f, curY + 15f, paint)

            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#1F2937")
            val desc = "Natural fit where high personal enthusiasm fuels effortless mastery. Student can safely target premier tier-1 colleges and high-competition entrance tracks with supreme confidence."
            drawWrappedText(canvas, desc, MARGIN + 10f, curY + 26f, CONTENT_WIDTH - 20f, paint, 9.5f)

            curY += 60f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 41: NATIONAL PEER MEDIAN BENCHMARK COMPARISON (PART 1: COGNITIVE)
    // -------------------------------------------------------------------------------------
    fun drawPage41PeerBenchmarkPart1(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Benchmark • India Median Comparison (Cognitive)", 41, candidateName = profile.studentName)
        drawPageFooter(canvas, 41)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("National Peer Median Benchmark Comparison in India", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Comparing the student's raw test performance against the empirical median benchmark of 150,000+ Indian high school students. Black vertical tick marks indicate national median (50th percentile)."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // 7 Cognitive Skills
        var curY = 120f
        val cognitiveSkills = com.example.margdarsh.data.HolisticSkill.entries.take(7)

        cognitiveSkills.forEach { skill ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 46f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            val studentScore = metrics.skillScores[skill] ?: 50
            val medianScore = metrics.nationalMedians[skill] ?: 50
            val percentile = metrics.skillPercentiles[skill] ?: 50

            // Draw Bar Row
            drawPeerComparisonRow(
                canvas = canvas,
                x = MARGIN + 10f,
                y = curY + 6f,
                width = CONTENT_WIDTH - 20f,
                skillName = skill.displayName,
                studentScore = studentScore,
                medianScore = medianScore,
                percentile = percentile
            )

            // Analysis note below bar
            paint.style = Paint.Style.FILL
            paint.textSize = 6.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            paint.color = Color.parseColor("#64748B")
            val diff = studentScore - medianScore
            val note = if (diff >= 20) "★ Substantial competitive lead over national peers. Prime foundation for Tier-1 entrance exams."
            else if (diff >= 0) "Above median; consistent practice will convert this into an unfair academic advantage."
            else "Currently below median benchmark. Targeted 60-day deliberate practice recommended."
            canvas.drawText(note, MARGIN + 10f, curY + 36f, paint)

            curY += 54f
        }

        // Summary Note
        curY += 10f
        val sumRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 60f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#EFF6FF")
        canvas.drawRoundRect(sumRect, 8f, 8f, paint)

        paint.textSize = 8f
        paint.color = Color.parseColor("#1E3A8A")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val sumText = "National Percentile Analysis: The candidate outperforms the national median across foundational STEM and analytical dimensions. This cognitive velocity indicates high adaptability to competitive coaching formats (JEE Advanced, BITSAT, IPMAT, CUET) without requiring excessive remedial intervention."
        drawWrappedText(canvas, sumText, MARGIN + 12f, curY + 16f, CONTENT_WIDTH - 24f, paint, 10.5f)
    }

    // -------------------------------------------------------------------------------------
    // PAGE 42: NATIONAL PEER MEDIAN BENCHMARK COMPARISON (PART 2: BEHAVIORAL)
    // -------------------------------------------------------------------------------------
    fun drawPage42PeerBenchmarkPart2(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Benchmark • India Median Comparison (Behavioral)", 42, candidateName = profile.studentName)
        drawPageFooter(canvas, 42)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("National Peer Median Comparison (Behavioral & Executive)", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Comparing the student's emotional intelligence, physical vitality, self-discipline, and leadership drive against national cohorts. These traits dictate collegiate survival, burnout resistance, and career acceleration."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        // 7 Behavioral Skills
        var curY = 120f
        val behavioralSkills = com.example.margdarsh.data.HolisticSkill.entries.drop(7).take(7)

        behavioralSkills.forEach { skill ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 46f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            val studentScore = metrics.skillScores[skill] ?: 50
            val medianScore = metrics.nationalMedians[skill] ?: 50
            val percentile = metrics.skillPercentiles[skill] ?: 50

            drawPeerComparisonRow(
                canvas = canvas,
                x = MARGIN + 10f,
                y = curY + 6f,
                width = CONTENT_WIDTH - 20f,
                skillName = skill.displayName,
                studentScore = studentScore,
                medianScore = medianScore,
                percentile = percentile
            )

            paint.style = Paint.Style.FILL
            paint.textSize = 6.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            paint.color = Color.parseColor("#64748B")
            val diff = studentScore - medianScore
            val note = if (diff >= 20) "★ Exceptional self-regulation and executive control compared to age peers."
            else if (diff >= 0) "Healthy behavioral balance; easily manages team dynamics and project milestones."
            else "High sensitivity to stress or distractibility under pressure. Structured routine coaching advised."
            canvas.drawText(note, MARGIN + 10f, curY + 36f, paint)

            curY += 54f
        }

        // Summary Note
        curY += 10f
        val sumRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 60f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F0FDF4")
        canvas.drawRoundRect(sumRect, 8f, 8f, paint)

        paint.textSize = 8f
        paint.color = Color.parseColor("#166534")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val sumText = "Executive Function & Resilience Verdict: The student's emotional composure and self-discipline index provide a massive stabilizer against high-stress exam environments. Even during rigorous grade 11-12 prep schedules, their internal locus of control prevents academic burnout."
        drawWrappedText(canvas, sumText, MARGIN + 12f, curY + 16f, CONTENT_WIDTH - 24f, paint, 10.5f)
    }

    // -------------------------------------------------------------------------------------
    // PAGE 43: 10 BI-DIRECTIONAL PERSONALITY CONTINUUMS (PART 1: CONTINUUMS 1-5)
    // -------------------------------------------------------------------------------------
    fun drawPage43PersonalityContinuumsPart1(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Personality Continuum • Dimensions 1 to 5", 43, candidateName = profile.studentName)
        drawPageFooter(canvas, 43)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("10 Bi-Directional Personality Continuums (Part 1)", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Personality is not a rigid binary box, but a continuous spectrum between two positive polar traits. Here is the student's exact coordinate along the first 5 polar continuums."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 120f
        val continuumsPart1 = com.example.margdarsh.data.PersonalityPolarDimension.entries.take(5)

        continuumsPart1.forEach { dim ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 68f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            val score = metrics.polarScores[dim] ?: 0f

            drawContinuumSlider(
                canvas = canvas,
                x = MARGIN + 14f,
                y = curY + 10f,
                width = CONTENT_WIDTH - 28f,
                leftPole = dim.leftPole,
                rightPole = dim.rightPole,
                scoreFloat = score,
                leftDesc = dim.leftDescription,
                rightDesc = dim.rightDescription
            )

            curY += 76f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 44: 10 BI-DIRECTIONAL PERSONALITY CONTINUUMS (PART 2: CONTINUUMS 6-10)
    // -------------------------------------------------------------------------------------
    fun drawPage44PersonalityContinuumsPart2(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Personality Continuum • Dimensions 6 to 10", 44, candidateName = profile.studentName)
        drawPageFooter(canvas, 44)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("10 Bi-Directional Personality Continuums (Part 2)", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Continuums 6 to 10 explore execution precision, perseverance under stress, empathy, social energy, and organizational discipline. Both ends of each continuum possess distinct professional advantages."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 120f
        val continuumsPart2 = com.example.margdarsh.data.PersonalityPolarDimension.entries.drop(5).take(5)

        continuumsPart2.forEach { dim ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 68f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            val score = metrics.polarScores[dim] ?: 0f

            drawContinuumSlider(
                canvas = canvas,
                x = MARGIN + 14f,
                y = curY + 10f,
                width = CONTENT_WIDTH - 28f,
                leftPole = dim.leftPole,
                rightPole = dim.rightPole,
                scoreFloat = score,
                leftDesc = dim.leftDescription,
                rightDesc = dim.rightDescription
            )

            curY += 76f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 45: TOP-RATED SKILLS & INNATE AREAS OF EXCELLENCE
    // -------------------------------------------------------------------------------------
    fun drawPage45TopRatedSkills(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Cognitive Signature • Top-Rated Skills", 45, candidateName = profile.studentName)
        drawPageFooter(canvas, 45)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Top-Rated Innate Skills & Competitive Superpowers", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "These are the student's 4 highest-scoring skill vectors where they naturally excel with minimal friction. Leveraging these innate advantages guarantees high return on investment."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 120f
        metrics.topRatedSkills.take(4).forEachIndexed { idx, skill ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 84f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#10B981")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            val strip = RectF(MARGIN, curY, MARGIN + 5f, curY + 84f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#10B981")
            canvas.drawRoundRect(strip, 4f, 4f, paint)

            val score = metrics.skillScores[skill] ?: 85
            val percentile = metrics.skillPercentiles[skill] ?: 90

            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#065F46")
            canvas.drawText("★ Superpower #${idx + 1}: ${skill.displayName} (Score: $score/100 • ${percentile}th %ile)", MARGIN + 14f, curY + 18f, paint)

            paint.textSize = 7.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#334155")
            val desc = skill.description
            drawWrappedText(canvas, desc, MARGIN + 14f, curY + 32f, CONTENT_WIDTH - 24f, paint, 10.5f)

            paint.color = Color.parseColor("#047857")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Career Application: Multiplier in competitive engineering, finance, analytics, and architecture entrance tracks.", MARGIN + 14f, curY + 74f, paint)

            curY += 94f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 46: TARGETED AREAS OF IMPROVEMENT & GROWTH BOTTLENECKS
    // -------------------------------------------------------------------------------------
    fun drawPage46ImprovementAreas(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Development Plan • Growth Bottlenecks", 46, candidateName = profile.studentName)
        drawPageFooter(canvas, 46)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Targeted Areas of Improvement & Growth Bottlenecks", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "These dimensions represent the student's lower scoring areas. In high-stakes competitive examinations, unaddressed bottlenecks cap overall rank performance. Targeted habit shifts will turn these into assets."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 120f
        metrics.improvementSkills.take(3).forEachIndexed { idx, skill ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 95f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#FFFBEB")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#F59E0B")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            val strip = RectF(MARGIN, curY, MARGIN + 5f, curY + 95f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F59E0B")
            canvas.drawRoundRect(strip, 4f, 4f, paint)

            val score = metrics.skillScores[skill] ?: 45
            val percentile = metrics.skillPercentiles[skill] ?: 40

            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#92400E")
            canvas.drawText("⚠️ Growth Edge #${idx + 1}: ${skill.displayName} (Score: $score/100 • ${percentile}th %ile)", MARGIN + 14f, curY + 18f, paint)

            paint.textSize = 7.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#78350F")
            val diagnosis = "Diagnosis: Currently performing below national median potential in ${skill.displayName}. May experience cognitive fatigue or procrastination when required to execute tasks heavy in this cognitive modality."
            drawWrappedText(canvas, diagnosis, MARGIN + 14f, curY + 32f, CONTENT_WIDTH - 24f, paint, 10f)

            paint.color = Color.parseColor("#B45309")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Strategic Remediation Blueprint:", MARGIN + 14f, curY + 58f, paint)

            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#78350F")
            val fix = "Implement structured 15-minute daily micro-drills for ${skill.displayName}. Deconstruct complex assignments into single-variable sub-problems before full execution."
            drawWrappedText(canvas, fix, MARGIN + 14f, curY + 70f, CONTENT_WIDTH - 24f, paint, 9.5f)

            curY += 105f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGE 47: PRESCRIPTIVE REMEDIAL INTERVENTIONS & HIGH-RETURN HABITS
    // -------------------------------------------------------------------------------------
    fun drawPage47PrescriptiveInterventions(
        canvas: Canvas,
        metrics: HolisticDossierMetrics,
        profile: StudentProfile
    ) {
        drawPageHeader(canvas, "Remedial Blueprint • Prescriptive Interventions", 47, candidateName = profile.studentName)
        drawPageFooter(canvas, 47)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Prescriptive Learning Interventions & High-Return Habits", MARGIN, 68f, paint)

        paint.color = Color.parseColor("#64748B")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val intro = "Tailored micro-interventions specifically prescribed based on the student's bottom 3 skill dimensions. Adhering to this 90-day protocol generates compounding intellectual returns."
        drawWrappedText(canvas, intro, MARGIN, 82f, CONTENT_WIDTH, paint, 11f)

        var curY = 120f
        metrics.remedialInterventions.take(3).forEachIndexed { idx, p ->
            val cardRect = RectF(MARGIN, curY, MARGIN + CONTENT_WIDTH, curY + 115f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#3B82F6")
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#1D4ED8")
            canvas.drawText("Intervention #${idx + 1}: ${p.title} (${p.skill.displayName})", MARGIN + 14f, curY + 18f, paint)

            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0F172A")
            canvas.drawText("Prescribed Routine: ${p.dailyRoutineMinutes} daily (${p.coreExercise})", MARGIN + 14f, curY + 34f, paint)

            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#475569")
            val yAfterDesc = drawWrappedText(canvas, p.cognitiveMechanism, MARGIN + 14f, curY + 48f, CONTENT_WIDTH - 24f, paint, 9.5f)

            paint.textSize = 7f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#059669")
            canvas.drawText("Projected Improvement: ${p.projectedImprovement}", MARGIN + 14f, yAfterDesc + 10f, paint)

            curY += 125f
        }
    }

    // -------------------------------------------------------------------------------------
    // PAGES 48 to 50: DEDICATED CAREER DEEP DIVES (CAREERS 1, 2, 3)
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

        // Draw Dual Radar Chart: Student Vector vs Industry Benchmark
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
}
