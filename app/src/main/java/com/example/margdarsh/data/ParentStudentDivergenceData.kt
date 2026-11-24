package com.example.margdarsh.data

data class ParentQuestionEvaluation(
    val questionId: String,
    val questionTitle: String,
    val parentSelectedValue: String,
    val parentSelectedLabel: String,
    val interpretedParentalIntent: String,
    val studentCompatibilityScore: Int, // 0 to 100
    val alignmentStatus: String, // "High Alignment", "Constructive Gap", "Divergent Vector"
    val counselorInsight: String
)

data class DivergencePoint(
    val dimension: String,
    val studentAptitude: String,
    val parentExpectation: String,
    val divergenceLevel: String, // "High Divergence", "Moderate Gap", "Strong Alignment"
    val divergenceColorHex: String,
    val rootCauseExplanation: String,
    val counselorBridgingStrategy: String
)

data class ParentStudentComparativeDashboardData(
    val overallHarmonyScore: Int,
    val primaryDivergenceSummary: String,
    val evaluatedParentQuestions: List<ParentQuestionEvaluation>,
    val divergencePoints: List<DivergencePoint>,
    val jointActionPlan: List<String>,
    val familyMediationScript: List<Pair<String, String>>
)

object ParentStudentDivergenceEvaluator {

    fun evaluate(
        profile: StudentProfile,
        traits: Map<TraitKey, Int>,
        streamResult: StreamResult?,
        parentAnswers: Map<String, String>,
        careers: List<ScoredCareer>
    ): ParentStudentComparativeDashboardData {
        val studentName = profile.studentName.ifBlank { "Student" }
        val parentName = profile.parentName.ifBlank { "Parent" }

        val topTrait = TraitKey.entries.maxByOrNull { traits[it] ?: 0 } ?: TraitKey.ANALYTICAL
        val analyticalScore = traits[TraitKey.ANALYTICAL] ?: 60
        val creativeScore = traits[TraitKey.CREATIVE] ?: 50
        val enterprisingScore = traits[TraitKey.ENTERPRISING] ?: 50
        val verbalScore = traits[TraitKey.VERBAL] ?: 50
        val spatialScore = traits[TraitKey.SPATIAL] ?: 50

        val recommendedStream = streamResult?.stream ?: Stream.SCIENCE
        val topCareerTitle = careers.firstOrNull()?.career?.title ?: "Artificial Intelligence & Computational Engineering"

        // 1. Evaluate Individual Parent Questions
        val evaluatedQuestions = mutableListOf<ParentQuestionEvaluation>()

        // Q1: Preferred Stream
        val parentStreamChoice = parentAnswers["preferredStream"] ?: "open"
        val (streamLabel, streamIntent, streamCompat, streamStatus, streamCounsel) = when (parentStreamChoice) {
            "Science" -> {
                val match = recommendedStream == Stream.SCIENCE
                Tuple5(
                    "Science (PCM / PCB / PCMB)",
                    "Aims for traditional technical/medical credentials and broad entrance exam optionality.",
                    if (match) 95 else 60,
                    if (match) "High Alignment" else "Divergent Vector",
                    if (match) "Parent and psychometric data both converge on rigorous STEM fields."
                    else "Student aptitude points towards ${recommendedStream.displayName}, but parent strongly leans Science. Facilitate objective aptitude review."
                )
            }
            "Commerce" -> {
                val match = recommendedStream == Stream.COMMERCE
                Tuple5(
                    "Commerce (With/Without Maths)",
                    "Desires strong corporate, financial, taxation, and business management trajectories.",
                    if (match) 92 else 65,
                    if (match) "High Alignment" else "Constructive Gap",
                    if (match) "Both candidate profile and family goals align on corporate finance/commerce."
                    else "Student's recommended stream is ${recommendedStream.displayName}. Show how interdisciplinary tracks (e.g. Fintech/Economics) bridge both."
                )
            }
            "Humanities" -> {
                val match = recommendedStream == Stream.HUMANITIES
                Tuple5(
                    "Humanities & Social Sciences",
                    "Values civil governance, law, public policy, psychological insight, and communication.",
                    if (match) 94 else 60,
                    if (match) "High Alignment" else "Constructive Gap",
                    if (match) "Complete harmony on law, public policy, and humanities."
                    else "Student's test leans ${recommendedStream.displayName}. Explore law/policy electives alongside."
                )
            }
            else -> Tuple5(
                "Open to scientific assessment guidance",
                "Progressive, outcome-driven mindset deferring to student's innate psychometric strengths.",
                96,
                "High Alignment",
                "Exceptional open-minded parental posture; highest probability of optimal stream placement."
            )
        }
        evaluatedQuestions.add(
            ParentQuestionEvaluation(
                questionId = "preferredStream",
                questionTitle = "Stream Leanings & Preference",
                parentSelectedValue = parentStreamChoice,
                parentSelectedLabel = streamLabel,
                interpretedParentalIntent = streamIntent,
                studentCompatibilityScore = streamCompat,
                alignmentStatus = streamStatus,
                counselorInsight = streamCounsel
            )
        )

        // Q2: Risk Appetite
        val parentRisk = parentAnswers["risk"] ?: "medium"
        val (riskLabel, riskIntent, riskCompat, riskStatus, riskCounsel) = when (parentRisk) {
            "low" -> {
                val highInnovation = enterprisingScore >= 70 || creativeScore >= 70
                Tuple5(
                    "Prefer classical proven routes (MBBS, CA, Govt/UPSC)",
                    "Prioritizes generational downside protection, institutional permanence, and brand safety.",
                    if (highInnovation) 52 else 85,
                    if (highInnovation) "Divergent Vector" else "High Alignment",
                    "Child craves dynamic 21st-century problem solving; parent seeks zero-risk tracks. Adopt a 'Barbell Strategy' of premier degree + tech skills."
                )
            }
            "high" -> Tuple5(
                "Very open to emerging AI, venture capital, design, global tracks",
                "Venture-growth mindset willing to back high-upside meritocracies.",
                90,
                "High Alignment",
                "High parental risk tolerance empowers the student to pursue top ₹30L+ LPA frontier sectors without fear of failure."
            )
            else -> Tuple5(
                "Open to modern tech/finance if college placement is strong",
                "Pragmatic ROI mindset: values innovation as long as institutional placement records are verifiable.",
                88,
                "High Alignment",
                "Constructive pragmatic posture. Pair the student with Tier-1 colleges having audited campus placement data."
            )
        }
        evaluatedQuestions.add(
            ParentQuestionEvaluation(
                questionId = "risk",
                questionTitle = "Career Risk & Innovation Tolerance",
                parentSelectedValue = parentRisk,
                parentSelectedLabel = riskLabel,
                interpretedParentalIntent = riskIntent,
                studentCompatibilityScore = riskCompat,
                alignmentStatus = riskStatus,
                counselorInsight = riskCounsel
            )
        )

        // Q3: Family Aspirations
        val parentAspiration = parentAnswers["aspiration"] ?: "stability"
        val (aspLabel, aspIntent, aspCompat, aspStatus, aspCounsel) = when (parentAspiration) {
            "earning" -> Tuple5(
                "Maximum Earning Potential & Fast Wealth Creation",
                "Calculates career success through high ROI, market compensation velocity, and global tech packages.",
                if (enterprisingScore >= 65 || analyticalScore >= 65) 90 else 72,
                "High Alignment",
                "Focus on Tier-1 Engineering, Product Design, and Quantitative Finance yielding ₹25L+ entry CTCs."
            )
            "impact" -> Tuple5(
                "High Social Respect, Governance & Institutional Power",
                "Values societal esteem, policy influence, civil authority, and prestige.",
                if (verbalScore >= 65) 88 else 70,
                "High Alignment",
                "Target National Law Universities (NLUs), UPSC, or Public Policy institutes."
            )
            "passion" -> Tuple5(
                "Personal Fulfillment & Creative Freedom",
                "Desires psychological joy, intellectual freedom, and avoidance of burnout.",
                if (creativeScore >= 60) 95 else 80,
                "High Alignment",
                "Aligns strongly with student's intrinsic motivation and creative experimentation."
            )
            else -> Tuple5(
                "Job Security, Brand Stability & Work-Life Balance",
                "Focuses on predictable tenure, established conglomerates, and lifelong family security.",
                if (enterprisingScore >= 75) 60 else 82,
                if (enterprisingScore >= 75) "Constructive Gap" else "High Alignment",
                "Ensure child pursues degrees with strong institutional recognition while nurturing individual initiative."
            )
        }
        evaluatedQuestions.add(
            ParentQuestionEvaluation(
                questionId = "aspiration",
                questionTitle = "Primary Family Success Metric",
                parentSelectedValue = parentAspiration,
                parentSelectedLabel = aspLabel,
                interpretedParentalIntent = aspIntent,
                studentCompatibilityScore = aspCompat,
                alignmentStatus = aspStatus,
                counselorInsight = aspCounsel
            )
        )

        // Q4: Budget & Higher Education Finance
        val parentBudget = parentAnswers["budget"] ?: "mid"
        val (budLabel, budIntent, budCompat, budStatus, budCounsel) = when (parentBudget) {
            "low" -> Tuple5(
                "Under ₹8 lakh total",
                "Prudent family capital preservation; mandates merit-based public university admissions.",
                80,
                "Constructive Gap",
                "Target top government institutions (IITs, NITs, DU, AIIMS, NIDs) where total 4-year tuition is under ₹6-8 Lakhs."
            )
            "high" -> Tuple5(
                "₹25 lakh – ₹60 lakh",
                "Substantial investment readiness for premier private institutions (BITS, Ashoka, Symbiosis, Manipal).",
                92,
                "High Alignment",
                "Unlocks top premier private and semi-government design and technology universities nationwide."
            )
            "premium" -> Tuple5(
                "Above ₹60 lakh (Global/Elite Tier)",
                "Global elite education orientation capable of funding Ivy League, Oxbridge, or US/UK undergrad.",
                95,
                "High Alignment",
                "Comprehensive global educational runway without financial constraints."
            )
            else -> Tuple5(
                "₹8 lakh – ₹25 lakh",
                "Standard middle-to-upper middle class investment horizon covering top Indian universities.",
                88,
                "High Alignment",
                "Ideal funding range for all premier Indian institutions, IIT/IIM integrated tracks, and top law colleges."
            )
        }
        evaluatedQuestions.add(
            ParentQuestionEvaluation(
                questionId = "budget",
                questionTitle = "Higher Education Budget Horizon",
                parentSelectedValue = parentBudget,
                parentSelectedLabel = budLabel,
                interpretedParentalIntent = budIntent,
                studentCompatibilityScore = budCompat,
                alignmentStatus = budStatus,
                counselorInsight = budCounsel
            )
        )

        // Q5: Relocation & Geographic Mobility
        val parentReloc = parentAnswers["relocation"] ?: "india"
        val (relocLabel, relocIntent, relocCompat, relocStatus, relocCounsel) = when (parentReloc) {
            "home" -> Tuple5(
                "Stay within our home city / metro",
                "High protective proximity; prioritizes family support system during teenage years.",
                62,
                "Constructive Gap",
                "Ensure local premier options are targeted (e.g. top city universities) to prevent missing out on national Tier-1 campuses."
            )
            "state" -> Tuple5(
                "Any premier institution within our state",
                "Balanced regional mobility maintaining reasonable travel connectivity.",
                78,
                "High Alignment",
                "Encompasses state engineering, medical, and central universities."
            )
            "abroad" -> Tuple5(
                "Open to India and top global universities abroad",
                "Unrestricted global ambition and international cultural readiness.",
                94,
                "High Alignment",
                "Enables SAT/ACT preparation and international admissions portfolios."
            )
            else -> Tuple5(
                "Any top tier college anywhere in India (IITs/NLUs/IIMs)",
                "Merit-first national perspective prioritizing brand quality over geographic distance.",
                95,
                "High Alignment",
                "Optimal mindset for national entrance exams (JEE, NEET, CLAT, CUET, IPMAT)."
            )
        }
        evaluatedQuestions.add(
            ParentQuestionEvaluation(
                questionId = "relocation",
                questionTitle = "Geographic Mobility & Relocation Scope",
                parentSelectedValue = parentReloc,
                parentSelectedLabel = relocLabel,
                interpretedParentalIntent = relocIntent,
                studentCompatibilityScore = relocCompat,
                alignmentStatus = relocStatus,
                counselorInsight = relocCounsel
            )
        )

        // 2. Synthesize 4 In-Depth Divergence Points
        val points = listOf(
            DivergencePoint(
                dimension = "Stream & Vocational Domain Alignment",
                studentAptitude = "Psychometric cognitive testing demonstrates prime affinity for ${recommendedStream.displayName} with top focus in $topCareerTitle (${topTrait.label}).",
                parentExpectation = "Parental response leans towards $streamLabel with an emphasis on $streamIntent",
                divergenceLevel = if (streamCompat >= 85) "Strong Alignment (92%)" else "Constructive Divergence (68%)",
                divergenceColorHex = if (streamCompat >= 85) "#059669" else "#D97706",
                rootCauseExplanation = "Parents naturally associate traditional stream designations with generational job security, whereas the student's aptitude requires real-world applied learning.",
                counselorBridgingStrategy = "Present the 20-Page MargDarsh empirical aptitude audit to demonstrate how $topCareerTitle achieves both parental status goals and student intellectual engagement."
            ),
            DivergencePoint(
                dimension = "Risk Tolerance vs. Institutional Stability",
                studentAptitude = "Student demonstrates modern problem-solving resilience and comfort with emerging, fast-growing tech/design skill meritocracies.",
                parentExpectation = "Parent priority: $riskLabel. Seeking predictable career trajectory and downside protection.",
                divergenceLevel = if (riskCompat >= 80) "Constructive Harmony (85%)" else "Significant Gap (72%)",
                divergenceColorHex = if (riskCompat >= 80) "#059669" else "#E11D48",
                rootCauseExplanation = "Economic cycles experienced by parents foster security-first prioritization, while student view static roles as mentally suffocating.",
                counselorBridgingStrategy = "Adopt the Counselor 'Barbell Strategy': secure admission in a Tier-1 recognized university (satisfying parental safety) while cultivating high-income modern portfolio skills."
            ),
            DivergencePoint(
                dimension = "Study Modality & Performance Stamina",
                studentAptitude = "Thrives in focused deep-work sprints, conceptual logic, and project-based validation rather than endless mechanical memorization.",
                parentExpectation = "Desires high daily coaching timetable compliance, visible desk hours, and standard mock exam percentiles.",
                divergenceLevel = "Moderate Gap (58%)",
                divergenceColorHex = "#D97706",
                rootCauseExplanation = "Parents measure dedication by visible seated hours, whereas 21st-century cognitive stamina thrives on intense deep focus blocks with clear milestones.",
                counselorBridgingStrategy = "Transition family tracking from 'hours at the desk' to 'weekly milestones mastered'. Hold a structured 15-minute weekly review instead of daily micromanagement."
            ),
            DivergencePoint(
                dimension = "Geographic & Financial Horizon Consensus",
                studentAptitude = "Ready to compete on national/global stages at premier institutes regardless of city location.",
                parentExpectation = "Budget: $budLabel | Relocation: $relocLabel. Balancing family cash flow and parental proximity.",
                divergenceLevel = if (relocCompat >= 80 && budCompat >= 80) "Strong Alignment (88%)" else "Moderate Gap (65%)",
                divergenceColorHex = if (relocCompat >= 80 && budCompat >= 80) "#059669" else "#D97706",
                rootCauseExplanation = "Logistical and financial realities dictate family comfort zones, which must be proactively reconciled with target college fees.",
                counselorBridgingStrategy = "Pre-filter target college lists by fee structures and merit scholarships, confirming ROI timelines before Grade 11 coaching begins."
            )
        )

        val totalScoreSum = evaluatedQuestions.map { it.studentCompatibilityScore }.sum()
        val calculatedHarmonyScore = (totalScoreSum / evaluatedQuestions.size.coerceAtLeast(1)).coerceIn(60, 96)

        val scriptList = listOf(
            "1. Discussing Modern Careers without Conflict" to "Student: 'I reviewed the MargDarsh audit report. Tier-1 graduates in $topCareerTitle are earning ₹25L+ starting compensation with institutional backing. Let's look at the 4-year roadmap together.'",
            "2. Aligning Study Habits & Desk Time" to "Parent: 'We want to support your focus. Instead of checking your room every hour, let's set 3 clear weekly milestone targets every Sunday evening.'",
            "3. Resolving College Location & Budget Concerns" to "Counselor Bridge: 'Every recommended path has premier government options under ₹8 Lakhs (IITs/NITs/DU) alongside top scholarships at leading institutions.'"
        )

        return ParentStudentComparativeDashboardData(
            overallHarmonyScore = calculatedHarmonyScore,
            primaryDivergenceSummary = "Candidate exhibits strong 21st-century aptitude in ${topTrait.label} and ${recommendedStream.displayName}, while family values ${parentAspiration.lowercase()} and ${parentRisk.lowercase()} risk pathways. Complete alignment is achievable via structured milestones.",
            evaluatedParentQuestions = evaluatedQuestions,
            divergencePoints = points,
            jointActionPlan = listOf(
                "Hold a 30-minute Family Consensus Session using the 20-Page MargDarsh Dossier to align on Class 11 stream selection.",
                "Establish a 'Milestone Contract' replacing seated-hour policing with weekly project and test reviews.",
                "Review the Top 25 Tier-1 Entrance Exam Matrix together to select 3 primary and 2 safety national entrance targets.",
                "Schedule a 1-on-1 counseling validation session with Dr. B.B. Panchal before final school stream registration."
            ),
            familyMediationScript = scriptList
        )
    }
}

private data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

