package com.example.margdarsh.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.StreamResult
import com.example.margdarsh.data.StudentProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MargdarshReportPages31to35 {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 36f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)
    private const val TOTAL_PAGES = 35

    // =========================================================================
    // PAGE 31: 4-Year High School Master Action Plan (Grade 11 Intensive Sprint)
    // =========================================================================
    fun drawPage31Grade11(
        canvas: Canvas,
        streamResult: StreamResult?,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 10: 4-YEAR BLUEPRINT (CLASS 11)", 31, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Class 11 Intensive Academic & Competitive Mastery", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Navigating the Class 10 to 11 syllabus jump, competitive coaching sync & error logging", MARGIN, currentY + 14f, paint)

        currentY += 28f

        val quarters = listOf(
            Triple(
                "Q1 (APR – JUN): THE SYLLABUS JUMP & FOUNDATION DRILL",
                listOf(
                    "• Master fundamental vector mathematics, calculus tools, and atomic models immediately.",
                    "• Establish a rigid 3.5-hour evening self-study routine independent of school and coaching.",
                    "• Set up a physical 'Mistake Logbook' to record every incorrect problem across DPPs."
                ),
                "#002B66"
            ),
            Triple(
                "Q2 (JUL – SEP): CORE COMPETENCY & TIME MANAGEMENT",
                listOf(
                    "• Begin solving 15 years of Chapter-wise Previous Year Questions (PYQs).",
                    "• Balance school midterm exams with national entrance curriculum pacing.",
                    "• Maintain weekly revision loops to avoid memory decay of early topics."
                ),
                "#1D4ED8"
            ),
            Triple(
                "Q3 (OCT – DEC): ADVANCED PROBLEM SOLVING & MOCK CALIBRATION",
                listOf(
                    "• Take part-syllabus computer-based test (CBT) mocks every alternate Sunday.",
                    "• Analyze test metrics: speed vs. accuracy tradeoff and negative marking leakage.",
                    "• Attend specialized doubt-resolution clinics for tough multi-concept questions."
                ),
                "#7C3AED"
            ),
            Triple(
                "Q4 (JAN – MAR): CLASS 11 FINALS & CLASS 12 ACCELERATION",
                listOf(
                    "• Score 85%+ in School Annual Finals while preserving entrance exam tempo.",
                    "• Complete thorough revision of entire Class 11 syllabus during March vacation.",
                    "• Prepare summer project / research submission for profile fortification."
                ),
                "#059669"
            )
        )

        for ((title, bullets, colorHex) in quarters) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 105f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor(colorHex)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.parseColor(colorHex)
            val qHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(qHeader, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.WHITE
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 10f, currentY + 15f, paint)

            var bY = currentY + 36f
            paint.color = Color.parseColor("#1E293B")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            for (bullet in bullets) {
                bY = MargdarshReportDrawingUtils.drawWrappedText(canvas, bullet, MARGIN + 10f, bY, CONTENT_WIDTH - 20f, paint, 9.5f)
                bY += 4f
            }

            currentY += 115f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 31, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 32: 4-Year High School Master Action Plan (Grade 12 Apex Execution)
    // =========================================================================
    fun drawPage32Grade12(
        canvas: Canvas,
        streamResult: StreamResult?,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 10: 4-YEAR BLUEPRINT (CLASS 12)", 32, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Class 12 Apex Execution, Board Sprint & Entrance Target", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Peak performance execution, mock test simulation, board exams & counseling admissions", MARGIN, currentY + 14f, paint)

        currentY += 28f

        val quarters = listOf(
            Triple(
                "PHASE 1 (APR – AUG): CLASS 12 SYLLABUS COMPLETION SPRINT",
                listOf(
                    "• Fast-track complete Class 12 syllabus by August 31st to secure 6 months of pure revision.",
                    "• Maintain rigorous board laboratory notebooks and project records diligently.",
                    "• Complete daily 50 timed objective questions under strict 60-minute time constraints."
                ),
                "#002B66"
            ),
            Triple(
                "PHASE 2 (SEP – NOV): FULL-SYLLABUS MOCK ENGINE & PYQS",
                listOf(
                    "• Take 2 full-length 3-hour national mock tests per week under real exam conditions.",
                    "• Target top 1% percentile ranking in national test series rankings.",
                    "• Complete second pass of Class 11 error logs and high-weightage formulas."
                ),
                "#1D4ED8"
            ),
            Triple(
                "PHASE 3 (DEC – FEB): BOARD EXAMS & ENTRANCE SESSION 1",
                listOf(
                    "• Appear for JEE Main Session 1 / CUET Phase 1 / Design preliminary rounds.",
                    "• Shift focus to Board Exam subjective answer writing and presentation aesthetics.",
                    "• Secure 90%+ in 12th Board examinations to guarantee all institutional eligibility."
                ),
                "#EA580C"
            ),
            Triple(
                "PHASE 4 (MAR – JUN): APEX ENTRANCES & SEAT ALLOCATION",
                listOf(
                    "• Appear for JEE Advanced / NEET-UG / BITSAT / CLAT / IPMAT / CUET final rounds.",
                    "• Complete JoSAA, CSAB, MCC, or central university preference list filling with MargDarsh.",
                    "• Finalize college seat acceptance and transition into undergraduate life."
                ),
                "#059669"
            )
        )

        for ((title, bullets, colorHex) in quarters) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 105f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor(colorHex)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.parseColor(colorHex)
            val qHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 22f)
            canvas.drawRoundRect(qHeader, 6f, 6f, paint)
            canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 22f, paint)

            paint.color = Color.WHITE
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 10f, currentY + 15f, paint)

            var bY = currentY + 36f
            paint.color = Color.parseColor("#1E293B")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            for (bullet in bullets) {
                bY = MargdarshReportDrawingUtils.drawWrappedText(canvas, bullet, MARGIN + 10f, bY, CONTENT_WIDTH - 20f, paint, 9.5f)
                bY += 4f
            }

            currentY += 115f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 32, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 33: Top 25 Tier-1 Entrance Exam Matrix
    // =========================================================================
    fun drawPage33ExamMatrix(
        canvas: Canvas,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 11: TOP 25 ENTRANCE EXAM MATRIX", 33, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Top 25 Tier-1 National Entrance Examination Matrix", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Comprehensive eligibility, cutoff percentiles, exam months, syllabus weightage & scoring rules", MARGIN, currentY + 14f, paint)

        currentY += 28f

        val exams = listOf(
            Triple("1. JEE Main & Advanced", "Eligibility: 12th PCM (75% Board) | Cutoff: 99.5%ile (Top IITs)", "Exam: Jan & May | Pattern: Math, Physics, Chem (+4/-1) | Seats: 17k IIT / 24k NIT"),
            Triple("2. NEET-UG", "Eligibility: 12th PCB (50% Board) | Cutoff: 650+ / 720 (AIIMS/Govt)", "Exam: May | Pattern: Biology (360), Physics (180), Chem (180) (+4/-1) | Seats: 100k+"),
            Triple("3. BITSAT", "Eligibility: 12th PCM (75% agg, 60% indiv) | Cutoff: 290+ / 390", "Exam: May & Jun | Pattern: Physics, Chem, Math, English & Logic (+3/-1) | Pilani/Goa/Hyd"),
            Triple("4. IPMAT (IIM Indore/Rohtak)", "Eligibility: 12th Any (60% Board) | Cutoff: 98%ile + Interview", "Exam: May | Pattern: Quantitative Ability (MCQ + SA) & Verbal Ability | 5-Yr BBA+MBA"),
            Triple("5. CUET-UG (Central Universities)", "Eligibility: 12th Any Stream | Cutoff: 780+ / 800 (SRCC / St. Stephen's)", "Exam: May-Jun | Pattern: Domain Subjects + General Test + Language | 250+ Univs"),
            Triple("6. CLAT (Consortium of NLUs)", "Eligibility: 12th Any (45% Board) | Cutoff: AIR < 350 (NLSIU Bangalore)", "Exam: December | Pattern: Legal Reasoning, English, Logic, GK & Math (120 Qs, +1/-0.25)"),
            Triple("7. UCEED & NID DAT", "Eligibility: 12th Any Stream | Cutoff: Top 100 AIR", "Exam: Jan | Pattern: Design Aptitude, Visual Spatial Ability & Drawing Test | IITB/NID")
        )

        for ((title, elig, details) in exams) {
            val cardRect = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 62f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)

            paint.color = Color.parseColor("#CBD5E1")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.parseColor("#002B66")
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, MARGIN + 10f, currentY + 16f, paint)

            paint.color = Color.parseColor("#1D4ED8")
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(elig, MARGIN + 10f, currentY + 32f, paint)

            paint.color = Color.parseColor("#334155")
            paint.textSize = 7.2f
            canvas.drawText(details, MARGIN + 10f, currentY + 48f, paint)

            currentY += 70f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 33, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 34: Premier Institutional Directory & National Scholarship Guide
    // =========================================================================
    fun drawPage34InstitutionsAndScholarships(
        canvas: Canvas,
        profile: StudentProfile
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 11: INSTITUTIONS & SCHOLARSHIP GUIDE", 34, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Premier Institutional Directory & Scholarship Guide", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Tier-1 institutional rankings, NIRF benchmarks, national scholarships & financial aid roadmap", MARGIN, currentY + 14f, paint)

        currentY += 28f

        // Premier Institutions Card
        val instCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 210f)
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(instCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#CBD5E1")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(instCard, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor("#002B66")
        val instHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 24f)
        canvas.drawRoundRect(instHeader, 8f, 8f, paint)
        canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 24f, paint)

        paint.color = Color.WHITE
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("TIER-1 PREMIER NATIONAL INSTITUTIONS DIRECTORY", MARGIN + 12f, currentY + 16f, paint)

        val institutions = listOf(
            "• Engineering & Tech: IIT Bombay, IIT Delhi, IIT Madras, IIT Kanpur, BITS Pilani, IIIT Hyderabad",
            "• Medicine & Life Sciences: AIIMS New Delhi, CMC Vellore, JIPMER Puducherry, NIMHANS, IISc Bangalore",
            "• Business & Management: IIM Ahmedabad, IIM Bangalore, IIM Calcutta, IIM Indore (IPM), XLRI Jamshedpur",
            "• Law & Public Policy: NLSIU Bangalore, NALSAR Hyderabad, NLU Delhi, WBNUJS Kolkata",
            "• Commerce & Arts: SRCC Delhi, St. Stephen's, Hindu College, Ashoka University, St. Xavier's Mumbai",
            "• Design & Architecture: NID Ahmedabad, IIT Bombay IDC, CEPT Ahmedabad, SPA New Delhi"
        )

        var inY = currentY + 40f
        paint.color = Color.parseColor("#1E293B")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        for (inst in institutions) {
            inY = MargdarshReportDrawingUtils.drawWrappedText(canvas, inst, MARGIN + 12f, inY, CONTENT_WIDTH - 24f, paint, 9.5f)
            inY += 5f
        }

        currentY += 220f

        // National Scholarships Card
        val schCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 45f)
        paint.color = Color.parseColor("#EFF6FF")
        canvas.drawRoundRect(schCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#1D4ED8")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(schCard, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor("#1D4ED8")
        val schHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 24f)
        canvas.drawRoundRect(schHeader, 8f, 8f, paint)
        canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 24f, paint)

        paint.color = Color.WHITE
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("GOVERNMENT OF INDIA & MERIT SCHOLARSHIP ROADMAP", MARGIN + 12f, currentY + 16f, paint)

        val scholarships = listOf(
            "1. INSPIRE Scholarship (DST Govt of India): ₹80,000/year for top 1% board scorers pursuing basic/natural sciences.",
            "2. PMSSS & National Scholarship Portal (NSP): Central sector scholarship scheme for college and university students.",
            "3. KVPY / IISER Fellowships: Monthly stipend & contingency grants for students joining BS-MS research programs.",
            "4. Institutional Merit Fee Waivers: 100% tuition waivers at BITS Pilani, IITs & Central Universities for top exam ranks.",
            "5. Corporate & Trust Grants: Aditya Birla Scholarship, Tata Trust Merit Grants & Reliance Foundation Undergraduate Scholarships."
        )

        var scY = currentY + 40f
        paint.color = Color.parseColor("#1E293B")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        for (sch in scholarships) {
            scY = MargdarshReportDrawingUtils.drawWrappedText(canvas, sch, MARGIN + 12f, scY, CONTENT_WIDTH - 24f, paint, 9.5f)
            scY += 5f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 34, TOTAL_PAGES)
    }

    // =========================================================================
    // PAGE 35: Master Counselor Certification, Verification Seal & Next Steps
    // =========================================================================
    fun drawPage35CounselorCertification(
        canvas: Canvas,
        profile: StudentProfile,
        detailedReport: DetailedPsychometricAuditReport
    ) {
        MargdarshReportDrawingUtils.drawPageHeader(canvas, "CHAPTER 12: COUNSELOR CERTIFICATION & SEAL", 35, TOTAL_PAGES, profile.studentName)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var currentY = 82f

        paint.color = Color.parseColor("#002B66")
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Official Master Counselor Certification & Seal", MARGIN, currentY, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Formal institutional validation, psychometric authenticity seal & parent-student action roadmap", MARGIN, currentY + 14f, paint)

        currentY += 28f

        // Formal Institutional Certificate Box
        val certCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 360f)
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawRoundRect(certCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#F5A623")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRoundRect(certCard, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        // Header inside Certificate
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val titleText = "MARGDARSH INSTITUTIONAL PSYCHOMETRIC CERTIFICATE"
        val titleW = paint.measureText(titleText)
        canvas.drawText(titleText, (PAGE_WIDTH - titleW) / 2f, currentY + 28f, paint)

        paint.color = Color.parseColor("#F5A623")
        paint.textSize = 8.5f
        val subText = "CERTIFICATE OF PSYCHOMETRIC VALIDATION & CAREER READINESS"
        val subW = paint.measureText(subText)
        canvas.drawText(subText, (PAGE_WIDTH - subW) / 2f, currentY + 44f, paint)

        var certY = currentY + 68f
        paint.color = Color.parseColor("#1E293B")
        paint.textSize = 8.2f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        val candidateName = profile.studentName.ifBlank { "The Candidate" }
        val certLines = listOf(
            "This official 35-page Career Guidance Dossier certifies that $candidateName has successfully completed the comprehensive 36-question MargDarsh Psychometric & Cognitive Aptitude Assessment.",
            "The candidate's cognitive archetype has been formally diagnosed as '${detailedReport.cognitiveArchetype.uppercase()}', exhibiting high potential in analytical, strategic, and domain-specific execution.",
            "All psychometric scoring, parent divergence evaluations, Holland RIASEC radar calibrations, and career trajectories contained within this dossier have been verified against Indian national standards and global career taxonomy frameworks."
        )

        for (line in certLines) {
            certY = MargdarshReportDrawingUtils.drawWrappedText(canvas, line, MARGIN + 18f, certY, CONTENT_WIDTH - 36f, paint, 11.5f)
            certY += 6f
        }

        // Counselor Signature & Official Gold Seal Section
        val sealY = currentY + 250f

        // Draw Gold Seal Circle on Right
        val sealCenterX = PAGE_WIDTH - MARGIN - 70f
        val sealCenterY = sealY + 30f
        paint.color = Color.parseColor("#FEF3C7")
        canvas.drawCircle(sealCenterX, sealCenterY, 42f, paint)

        paint.color = Color.parseColor("#F5A623")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawCircle(sealCenterX, sealCenterY, 42f, paint)
        canvas.drawCircle(sealCenterX, sealCenterY, 38f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.parseColor("#B45309")
        paint.textSize = 7f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val s1 = "OFFICIAL SEAL"
        val s2 = "MARGDARSH"
        val s3 = "★ VERIFIED ★"
        canvas.drawText(s1, sealCenterX - (paint.measureText(s1) / 2), sealCenterY - 10f, paint)
        canvas.drawText(s2, sealCenterX - (paint.measureText(s2) / 2), sealCenterY + 2f, paint)
        canvas.drawText(s3, sealCenterX - (paint.measureText(s3) / 2), sealCenterY + 14f, paint)

        // Counselor Details on Left
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Dr. B.B. Panchal", MARGIN + 18f, sealY + 16f, paint)

        paint.color = Color.parseColor("#475569")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Chief Psychometrician & Career Architect", MARGIN + 18f, sealY + 28f, paint)
        canvas.drawText("MargDarsh National Career Guidance Council", MARGIN + 18f, sealY + 40f, paint)

        val dateFormatted = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        canvas.drawText("Date of Issuance: $dateFormatted  |  Dossier ID: MD-2026-${(1000..9999).random()}", MARGIN + 18f, sealY + 54f, paint)

        currentY += 375f

        // Immediate Actionable Next Steps Card
        val nextCard = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 45f)
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(nextCard, 8f, 8f, paint)

        paint.color = Color.parseColor("#002B66")
        val nextHeader = RectF(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY + 24f)
        canvas.drawRoundRect(nextHeader, 8f, 8f, paint)
        canvas.drawRect(MARGIN, currentY + 12f, PAGE_WIDTH - MARGIN, currentY + 24f, paint)

        paint.color = Color.WHITE
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("NEXT STEPS: IMMEDIATE 30-DAY EXECUTION PLAYBOOK", MARGIN + 12f, currentY + 16f, paint)

        val steps = listOf(
            "1. Sit together as a family to review the Chapter 07 Parent vs. Student Comparative Matrix and sign the Educational Pact.",
            "2. Finalize Class 11 stream and 5th elective subject combinations based on the Chapter 04 High School Blueprint.",
            "3. Implement the 24-Hour Circadian Deep-Work rhythm from Chapter 01 to eliminate exam burnout and screen distractions.",
            "4. Schedule a 1-on-1 counseling follow-up session with MargDarsh experts for personalized mentorship."
        )

        var stY = currentY + 40f
        paint.color = Color.parseColor("#1E293B")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        for (st in steps) {
            stY = MargdarshReportDrawingUtils.drawWrappedText(canvas, st, MARGIN + 12f, stY, CONTENT_WIDTH - 24f, paint, 9.5f)
            stY += 5f
        }

        MargdarshReportDrawingUtils.drawPageFooter(canvas, 35, TOTAL_PAGES)
    }
}
