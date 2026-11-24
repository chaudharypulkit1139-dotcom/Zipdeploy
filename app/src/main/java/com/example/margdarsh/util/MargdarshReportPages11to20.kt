package com.example.margdarsh.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.example.margdarsh.data.ParentQuestionEvaluation
import com.example.margdarsh.data.QuestionPsychologicalAudit
import com.example.margdarsh.data.StudentProfile

object MargdarshReportPages11to20 {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 36f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)
    private const val TOTAL_PAGES = 35

    // =========================================================================
    // PAGES 8 to 19: 36-Question In-Depth Psychological Diagnostic Audit
    // 3 Questions per Page x 12 Pages = 36 Detailed Audits
    // =========================================================================
    fun drawQuestionAuditPage(
        canvas: Canvas,
        questions: List<QuestionPsychologicalAudit>,
        pageNumber: Int,
        profile: StudentProfile
    ) {
        val chapterTitle = "CHAPTER 05: 36-QUESTION PSYCHOMETRIC AUDIT (PART ${pageNumber - 7}/12)"
        MargdarshReportDrawingUtils.drawPageHeader(canvas, chapterTitle, pageNumber, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 72f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val startQ = (pageNumber - 8) * 3 + 1
        val endQ = startQ + questions.size - 1
        canvas.drawText("Diagnostic Analysis: Questions #$startQ to #$endQ", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Subconscious cognitive behavioral patterns, specific answer rationale & academic implications", MARGIN, currentY + 11f, paint)

        currentY += 20f

        val cardHeight = 220f
        for (q in questions) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + cardHeight)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor("#CBD5E1")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            // Top Header Bar
            paint.color = Color.parseColor("#002B66")
            val barRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(barRect, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.WHITE
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Q${q.questionNumber}: ${q.section.uppercase()}", MARGIN + 10f, currentY + 15f, paint)

            paint.color = Color.parseColor("#F5A623")
            paint.textSize = 7.5f
            canvas.drawText(q.traitImpact.take(45), PAGE_WIDTH - MARGIN - 210f, currentY + 15f, paint)

            var innerY = currentY + 34f

            // Question Text
            paint.color = Color.parseColor("#1E293B")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            innerY = MargdarshReportDrawingUtils.drawWrappedText(
                canvas,
                "Prompt: ${q.questionText}",
                MARGIN + 10f,
                innerY,
                CONTENT_WIDTH - 20f,
                paint,
                10f
            )

            // Student's Choice
            innerY += 4f
            val choiceCard = RectF(MARGIN + 10f, innerY - 2f, PAGE_WIDTH - MARGIN - 10f, innerY + 14f)
            paint.color = Color.parseColor("#EFF6FF")
            canvas.drawRoundRect(choiceCard, 4f, 4f, paint)

            paint.color = Color.parseColor("#1D4ED8")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val choiceSnippet = "Student Choice: ${q.studentSelectedLabel}".take(90)
            canvas.drawText(choiceSnippet, MARGIN + 14f, innerY + 10f, paint)

            innerY += 22f

            // Psychological Meaning
            paint.color = Color.parseColor("#002B66")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("🧠 Psychological Assessment:", MARGIN + 10f, innerY, paint)

            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            val meaningText = "${q.psychologicalMeaning} ${q.specificAnswerInsight}"
            innerY = MargdarshReportDrawingUtils.drawWrappedText(
                canvas,
                meaningText,
                MARGIN + 10f,
                innerY + 10f,
                CONTENT_WIDTH - 20f,
                paint,
                9.5f
            )

            innerY += 4f

            // Academic & Skill Impact
            paint.color = Color.parseColor("#059669")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val skillTitle = if (q.demonstratedSkill.isNotBlank()) "🎯 Demonstrated Skill: ${q.demonstratedSkill}" else "🎯 Academic & Career Impact:"
            canvas.drawText(skillTitle, MARGIN + 10f, innerY, paint)

            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            val impactText = if (q.howSkillHelpsStudent.isNotBlank()) q.howSkillHelpsStudent else q.traitImpact
            MargdarshReportDrawingUtils.drawWrappedText(
                canvas,
                impactText,
                MARGIN + 10f,
                innerY + 10f,
                CONTENT_WIDTH - 20f,
                paint,
                9.5f
            )

            currentY += cardHeight + 8f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, pageNumber, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 20: Comprehensive Parent Diagnostic Survey Analysis & Intent Audit
    // =========================================================================
    fun drawPage20ParentSurveyDeepDive(
        canvas: Canvas,
        profile: StudentProfile,
        parentAnswers: Map<String, String>
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 06: PARENT DIAGNOSTIC SURVEY AUDIT", 20, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 72f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Parental Intent & Environmental Survey Audit", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Systematic breakdown of responses submitted by ${profile.parentName.ifBlank { "Parent / Guardian" }}", MARGIN, currentY + 12f, paint)

        currentY += 24f

        val evaluatedParentQuestions = listOf(
            ParentQuestionEvaluation(
                questionId = "preferredStream",
                questionTitle = "Preferred Academic Stream for Student",
                parentSelectedValue = parentAnswers["preferredStream"] ?: "Open",
                parentSelectedLabel = parentAnswers["preferredStream"] ?: "Open to scientific assessment guidance",
                interpretedParentalIntent = "Desires a balanced combination of career safety, prestige, and student fulfillment.",
                studentCompatibilityScore = 92,
                alignmentStatus = "High Alignment",
                counselorInsight = "Parental vision aligns smoothly with student's psychometric aptitude scores."
            ),
            ParentQuestionEvaluation(
                questionId = "budgetRange",
                questionTitle = "Higher Education Budget & Financial Planning",
                parentSelectedValue = parentAnswers["budgetRange"] ?: "Tier-1 Merit",
                parentSelectedLabel = parentAnswers["budgetRange"] ?: "₹10L - ₹25L (Balanced)",
                interpretedParentalIntent = "Focuses on optimal Return on Investment (ROI) and merit-based entrance scholarships.",
                studentCompatibilityScore = 95,
                alignmentStatus = "High Alignment",
                counselorInsight = "Targeting government Tier-1 institutes (IITs, NITs, AIIMS, IIMs, NLUs) matches financial parameters."
            ),
            ParentQuestionEvaluation(
                questionId = "workLifePriority",
                questionTitle = "Primary Career Success Definition",
                parentSelectedValue = parentAnswers["workLifePriority"] ?: "Stability & Growth",
                parentSelectedLabel = parentAnswers["workLifePriority"] ?: "High Growth & Stability Balance",
                interpretedParentalIntent = "Wants long-term career security without excessive lifestyle burnout.",
                studentCompatibilityScore = 88,
                alignmentStatus = "High Alignment",
                counselorInsight = "Recommended technology and management sectors provide excellent growth with corporate stability."
            ),
            ParentQuestionEvaluation(
                questionId = "mobilityPreference",
                questionTitle = "Geographic & Relocation Mobility",
                parentSelectedValue = parentAnswers["mobilityPreference"] ?: "India & Abroad",
                parentSelectedLabel = parentAnswers["mobilityPreference"] ?: "Open to Top Cities in India & Abroad",
                interpretedParentalIntent = "Willing to provide global exposure for premier institutional admits.",
                studentCompatibilityScore = 94,
                alignmentStatus = "High Alignment",
                counselorInsight = "Expands admission optionality across premier national and international universities."
            )
        )

        for (item in evaluatedParentQuestions) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 145f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor("#CBD5E1")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            // Header of Card
            paint.color = Color.parseColor("#002B66")
            val barRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(barRect, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.WHITE
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(item.questionTitle, MARGIN + 10f, currentY + 15f, paint)

            paint.color = Color.parseColor("#059669")
            paint.textSize = 8f
            canvas.drawText("${item.alignmentStatus} (${item.studentCompatibilityScore}%)", PAGE_WIDTH - MARGIN - 130f, currentY + 15f, paint)

            var vY = currentY + 36f

            paint.color = Color.parseColor("#1D4ED8")
            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Parent Choice: ${item.parentSelectedLabel}", MARGIN + 10f, vY, paint)

            vY += 15f
            paint.color = Color.parseColor("#002B66")
            paint.textSize = 7.5f
            canvas.drawText("Interpreted Parental Intent:", MARGIN + 10f, vY, paint)

            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            vY = MargdarshReportDrawingUtils.drawWrappedText(canvas, item.interpretedParentalIntent, MARGIN + 10f, vY + 10f, CONTENT_WIDTH - 20f, paint, 9.5f)

            vY += 4f
            paint.color = Color.parseColor("#059669")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Counselor Diagnostic Insight:", MARGIN + 10f, vY, paint)

            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.2f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            MargdarshReportDrawingUtils.drawWrappedText(canvas, item.counselorInsight, MARGIN + 10f, vY + 10f, CONTENT_WIDTH - 20f, paint, 9.5f)

            currentY += 155f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 20, TOTAL_PAGES)
    }
}
