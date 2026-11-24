package com.example.margdarsh.data

import kotlin.math.roundToInt
import kotlin.math.sqrt

// -----------------------------------------------------------------------------------------
// 1. HOLISTIC DOSSIER MODELS & ENUMS
// -----------------------------------------------------------------------------------------

data class BrainAreaEvaluation(
    val area: NeurologicalBrainArea,
    val score: Int,
    val rank: Int,
    val tierLabel: String, // "Excellent", "High", "Moderate"
    val tierColorHex: String, // Green "#10B981", Orange "#F59E0B", Magenta "#D946EF"
    val whatItIs: String,
    val howItHelpsStudent: String,
    val whatItDemands: String
)

data class StreamQuadrantPoint(
    val stream: Stream,
    val displayName: String,
    val interestScore: Int,
    val potentialScore: Int,
    val isSweetSpot: Boolean,
    val strategicRecommendation: String
)

data class CareerQuadrantPoint(
    val title: String,
    val stream: Stream,
    val interestScore: Int,
    val potentialScore: Int,
    val isSweetSpot: Boolean,
    val matchRank: Int
)

data class PrescriptiveIntervention(
    val skill: HolisticSkill,
    val currentScore: Int,
    val title: String,
    val coreExercise: String,
    val dailyRoutineMinutes: String,
    val cognitiveMechanism: String,
    val recommendedTools: List<String>,
    val projectedImprovement: String
)

data class HolisticCareerProfile(
    val rank: Int,
    val title: String,
    val stream: Stream,
    val tag: CareerTag,
    val matchPercentage: Float,
    val studentVector: Map<HolisticSkill, Int>,
    val industryVector: Map<HolisticSkill, Int>,
    val coreDemands: String,
    val dayInTheLifeMorning: String,
    val dayInTheLifeAfternoon: String,
    val dayInTheLifeEvening: String,
    val topCollegesInIndia: List<String>,
    val entranceExams: List<String>,
    val degreePathways: List<String>,
    val salaryProgression: String,
    val horizonOutlook2035: String
)

data class HolisticQuestionAuditItem(
    val id: String,
    val number: Int,
    val category: String,
    val targetQuotient: HolisticQuotient,
    val primarySkill: HolisticSkill,
    val questionText: String,
    val studentAnswerText: String,
    val isCorrectOrFavorable: Boolean,
    val psychologicalMeaning: String,
    val subconsciousInsight: String,
    val skillImpactDescription: String
)

data class HolisticDossierMetrics(
    val candidateName: String,
    val iqScore: Int,
    val pqScore: Int,
    val sqScore: Int,
    val overallBrainScore: Int,
    val brainScoreTier: String,
    val skillScores: Map<HolisticSkill, Int>,
    val nationalMedians: Map<HolisticSkill, Int>,
    val skillPercentiles: Map<HolisticSkill, Int>,
    val rankedBrainAreas: List<BrainAreaEvaluation>,
    val polarScores: Map<PersonalityPolarDimension, Float>, // -100 to +100
    val topRatedSkills: List<HolisticSkill>,
    val improvementSkills: List<HolisticSkill>,
    val streamQuadrants: List<StreamQuadrantPoint>,
    val careerQuadrants: List<CareerQuadrantPoint>,
    val top20Careers: List<HolisticCareerProfile>,
    val remedialInterventions: List<PrescriptiveIntervention>,
    val questionAudits: List<HolisticQuestionAuditItem>
)

// -----------------------------------------------------------------------------------------
// 2. NATIONAL PEER BENCHMARK MEDIANS IN INDIA (Empirical Norms)
// -----------------------------------------------------------------------------------------
val NATIONAL_INDIAN_PEER_MEDIANS: Map<HolisticSkill, Int> = mapOf(
    HolisticSkill.NUMERICAL_APTITUDE to 52,
    HolisticSkill.ANALYTICAL_CRITICAL to 55,
    HolisticSkill.SPEECH_VERBAL to 48,
    HolisticSkill.LITERACY_LANGUAGE to 56,
    HolisticSkill.CREATIVE_VISUALIZATION to 50,
    HolisticSkill.IMAGINATION_INNOVATION to 48,
    HolisticSkill.MOTOR_SKILLS to 53,
    HolisticSkill.MEMORIZING_RECALL to 51,
    HolisticSkill.VISUAL_PERCEPTION to 54,
    HolisticSkill.SELF_DISCIPLINE to 50,
    HolisticSkill.SELF_MOTIVATION to 52,
    HolisticSkill.EMOTIONAL_INTELLIGENCE to 55,
    HolisticSkill.SOCIAL_COLLABORATION to 53,
    HolisticSkill.LEADING_VISION to 47
)

// -----------------------------------------------------------------------------------------
// 3. TOP 20 CAREER BENCHMARK VECTORS & INDUSTRY SPECS
// -----------------------------------------------------------------------------------------
object Top20CareersRepository {

    val PROFILES_20: List<HolisticCareerProfile> = listOf(
        HolisticCareerProfile(
            rank = 1,
            title = "Artificial Intelligence & Distributed Systems Architecture",
            stream = Stream.SCIENCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 97.4f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.NUMERICAL_APTITUDE to 95,
                HolisticSkill.ANALYTICAL_CRITICAL to 96,
                HolisticSkill.CREATIVE_VISUALIZATION to 85,
                HolisticSkill.MEMORIZING_RECALL to 80,
                HolisticSkill.VISUAL_PERCEPTION to 78,
                HolisticSkill.SELF_DISCIPLINE to 85,
                HolisticSkill.SELF_MOTIVATION to 90,
                HolisticSkill.IMAGINATION_INNOVATION to 88,
                HolisticSkill.SPEECH_VERBAL to 70,
                HolisticSkill.LITERACY_LANGUAGE to 75,
                HolisticSkill.SOCIAL_COLLABORATION to 72,
                HolisticSkill.LEADING_VISION to 75,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 65,
                HolisticSkill.MOTOR_SKILLS to 50
            ),
            coreDemands = "Mathematical modeling of transformer attention layers, high-throughput GPU cluster orchestration, algorithm optimization, and algorithmic fault tolerance.",
            dayInTheLifeMorning = "09:00 AM — Reviews overnight GPU cluster training logs; debugs loss spike in 70B-parameter multimodal LLM checkpoint.",
            dayInTheLifeAfternoon = "02:00 PM — Designs distributed inference pipeline handling 25,000 requests/sec with sub-20ms latency constraints.",
            dayInTheLifeEvening = "05:30 PM — Explores recent NeurIPS/ICML preprints; tests speculative decoding optimizations with core research team.",
            topCollegesInIndia = listOf("IIT Bombay", "IIT Delhi", "IIT Madras", "BITS Pilani", "IIIT Hyderabad"),
            entranceExams = listOf("JEE Advanced", "JEE Main", "BITSAT", "UGEE (IIITH)"),
            degreePathways = listOf("B.Tech in Computer Science & Engineering", "Dual Degree B.Tech/M.Tech in Data Science & AI"),
            salaryProgression = "Entry: ₹22–35 LPA • Mid (5 Yrs): ₹55–90 LPA • Principal/Staff: ₹1.4–2.5 Cr+",
            horizonOutlook2035 = "Extreme compound growth (+48% YoY). Core engine powering global industrial automation and generative enterprise software."
        ),
        HolisticCareerProfile(
            rank = 2,
            title = "Clinical Medicine & Super-Specialty Surgery (MBBS / MD / MCh)",
            stream = Stream.SCIENCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 95.8f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.MEMORIZING_RECALL to 95,
                HolisticSkill.ANALYTICAL_CRITICAL to 92,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 90,
                HolisticSkill.MOTOR_SKILLS to 88,
                HolisticSkill.SELF_DISCIPLINE to 94,
                HolisticSkill.SELF_MOTIVATION to 92,
                HolisticSkill.VISUAL_PERCEPTION to 85,
                HolisticSkill.LITERACY_LANGUAGE to 82,
                HolisticSkill.NUMERICAL_APTITUDE to 78,
                HolisticSkill.SPEECH_VERBAL to 80,
                HolisticSkill.SOCIAL_COLLABORATION to 84,
                HolisticSkill.LEADING_VISION to 80,
                HolisticSkill.CREATIVE_VISUALIZATION to 75,
                HolisticSkill.IMAGINATION_INNOVATION to 68
            ),
            coreDemands = "Diagnostic synthesis under acute time pressure, micro-motor surgical precision, empathetic patient counseling, and encyclopedic anatomical recall.",
            dayInTheLifeMorning = "07:30 AM — ICU rounds reviewing post-op vitals, blood gas panels, and ordering emergency hemodialysis.",
            dayInTheLifeAfternoon = "11:30 AM — Performs complex 4-hour laparoscopic abdominal reconstruction with robotic micro-instrumentation.",
            dayInTheLifeEvening = "05:00 PM — Outpatient consultation diagnosing complex chronic autoimmune and endocrine disorders.",
            topCollegesInIndia = listOf("AIIMS New Delhi", "CMC Vellore", "JIPMER Puducherry", "KGMU Lucknow", "AFMC Pune"),
            entranceExams = listOf("NEET-UG (680+ for Top GMCs)", "INI-CET", "NEET-PG"),
            degreePathways = listOf("MBBS (5.5 Years) followed by MD/MS (3 Years) and DM/MCh Super-Specialty"),
            salaryProgression = "Resident: ₹12–18 LPA • Consultant (PG): ₹30–60 LPA • Super-Specialist/Chief: ₹1.2–3.0 Cr+",
            horizonOutlook2035 = "Unshakable societal necessity (+22% YoY). Integration of robotic da Vinci systems and genetic pharmacotherapy."
        ),
        HolisticCareerProfile(
            rank = 3,
            title = "Quantitative Trading & Algorithmic Market Making",
            stream = Stream.SCIENCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 94.6f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.NUMERICAL_APTITUDE to 98,
                HolisticSkill.ANALYTICAL_CRITICAL to 96,
                HolisticSkill.MEMORIZING_RECALL to 85,
                HolisticSkill.SELF_DISCIPLINE to 92,
                HolisticSkill.SELF_MOTIVATION to 90,
                HolisticSkill.VISUAL_PERCEPTION to 84,
                HolisticSkill.CREATIVE_VISUALIZATION to 80,
                HolisticSkill.IMAGINATION_INNOVATION to 78,
                HolisticSkill.LITERACY_LANGUAGE to 72,
                HolisticSkill.LEADING_VISION to 70,
                HolisticSkill.SPEECH_VERBAL to 65,
                HolisticSkill.SOCIAL_COLLABORATION to 68,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 62,
                HolisticSkill.MOTOR_SKILLS to 55
            ),
            coreDemands = "Stochastic calculus, statistical arbitrage, C++ low-latency kernel optimization, order book microstructural analysis, and real-time risk control.",
            dayInTheLifeMorning = "08:15 AM — Calibrates volatility surfaces across NIFTY index options before market open; tests co-located feed latency.",
            dayInTheLifeAfternoon = "01:00 PM — Monitors live HFT algorithmic execution across 4,000 orders/sec; manages intraday delta exposure.",
            dayInTheLifeEvening = "04:30 PM — Backtests statistical cointegration trading models on 500GB tick-by-tick order book data.",
            topCollegesInIndia = listOf("IIT Bombay (CSE/EE)", "IIT Delhi (Maths & Computing)", "IIT Kanpur", "ISI Kolkata"),
            entranceExams = listOf("JEE Advanced (Top 500 AIR)", "ISI B.Math/B.Stat Admission Test"),
            degreePathways = listOf("B.Tech in Mathematics & Computing", "B.Stat / B.Math (ISI)", "B.Tech Computer Science"),
            salaryProgression = "Entry: ₹45–80 LPA (base) + ₹40L bonus • Quant Trader (4 Yrs): ₹1.5–3.5 Cr • Partner: ₹5–12 Cr+",
            horizonOutlook2035 = "High-prestige financial engineering (+34% YoY). Relentless demand for ultra-deep mathematical intuition."
        ),
        HolisticCareerProfile(
            rank = 4,
            title = "Corporate Law, M&A Strategy & International Arbitration",
            stream = Stream.HUMANITIES,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 93.9f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.LITERACY_LANGUAGE to 96,
                HolisticSkill.SPEECH_VERBAL to 95,
                HolisticSkill.ANALYTICAL_CRITICAL to 94,
                HolisticSkill.LEADING_VISION to 88,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 86,
                HolisticSkill.SOCIAL_COLLABORATION to 85,
                HolisticSkill.SELF_DISCIPLINE to 90,
                HolisticSkill.MEMORIZING_RECALL to 88,
                HolisticSkill.SELF_MOTIVATION to 86,
                HolisticSkill.IMAGINATION_INNOVATION to 75,
                HolisticSkill.VISUAL_PERCEPTION to 70,
                HolisticSkill.NUMERICAL_APTITUDE to 68,
                HolisticSkill.CREATIVE_VISUALIZATION to 60,
                HolisticSkill.MOTOR_SKILLS to 45
            ),
            coreDemands = "Synthesizing ambiguous regulatory statutory frameworks, drafting multi-billion dollar shareholder purchase agreements, and adversarial oral cross-examination.",
            dayInTheLifeMorning = "09:00 AM — Conducts legal due diligence on cross-border $450M cross-entity acquisition; reviews antitrust indemnity clauses.",
            dayInTheLifeAfternoon = "02:30 PM — Argues preliminary interim injunction before National Company Law Tribunal (NCLT) bench.",
            dayInTheLifeEvening = "06:00 PM — Redrafts contested dispute resolution covenant in Singapore International Arbitration Centre (SIAC) format.",
            topCollegesInIndia = listOf("NLSIU Bengaluru", "NALSAR Hyderabad", "WBNUJS Kolkata", "NLU Delhi", "NLU Jodhpur"),
            entranceExams = listOf("CLAT-UG (Top 250 Rank)", "AILET (NLU Delhi)"),
            degreePathways = listOf("B.A. LL.B. (Hons.) 5-Year Integrated", "B.B.A. LL.B. (Hons.)"),
            salaryProgression = "Associate (Tier-1 Law Firm): ₹16–22 LPA • Senior Associate: ₹35–55 LPA • Equity Partner: ₹1.2–3.5 Cr+",
            horizonOutlook2035 = "Critical governance domain (+28% YoY). Cross-border tech IP, cross-border corporate mergers, and ESG compliance."
        ),
        HolisticCareerProfile(
            rank = 5,
            title = "Civil Services & Diplomatic Administration (IAS / IFS / IPS)",
            stream = Stream.HUMANITIES,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 93.2f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.LEADING_VISION to 96,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 94,
                HolisticSkill.SPEECH_VERBAL to 92,
                HolisticSkill.LITERACY_LANGUAGE to 92,
                HolisticSkill.ANALYTICAL_CRITICAL to 90,
                HolisticSkill.SELF_DISCIPLINE to 95,
                HolisticSkill.SOCIAL_COLLABORATION to 90,
                HolisticSkill.MEMORIZING_RECALL to 88,
                HolisticSkill.SELF_MOTIVATION to 94,
                HolisticSkill.IMAGINATION_INNOVATION to 76,
                HolisticSkill.NUMERICAL_APTITUDE to 72,
                HolisticSkill.VISUAL_PERCEPTION to 70,
                HolisticSkill.MOTOR_SKILLS to 65,
                HolisticSkill.CREATIVE_VISUALIZATION to 62
            ),
            coreDemands = "Public sector governance over millions of citizens, crisis disaster management, bilateral foreign diplomacy, and multi-departmental fiscal execution.",
            dayInTheLifeMorning = "08:30 AM — Inspects district-wide grain procurement mandis and oversees flood relief logistics mobilization.",
            dayInTheLifeAfternoon = "02:00 PM — Presides over law and order review meeting with SP and magistrates; mediates communal dispute.",
            dayInTheLifeEvening = "06:30 PM — Drafts cabinet proposal for state industrial solar policy implementation and skill center subsidies.",
            topCollegesInIndia = listOf("St. Stephen's College Delhi", "Hindu College DU", "IITs / NLUs", "JNU New Delhi", "Miranda House"),
            entranceExams = listOf("UPSC Civil Services Examination (Prelims, Mains, Interview)"),
            degreePathways = listOf("Bachelor's Degree in any discipline (B.A., B.Tech, B.Com, MBBS, LL.B.)"),
            salaryProgression = "Sub-Divisional Magistrate: Pay Level 10 (₹56.1k basic + HRA/DA + Bungalow/Security/Vehicle) • Joint Secretary: Level 14",
            horizonOutlook2035 = "Unmatched sovereign impact. Leadership over national economic transformation, space policy, and defense pacts."
        ),
        HolisticCareerProfile(
            rank = 6,
            title = "Investment Banking, M&A & Private Equity",
            stream = Stream.COMMERCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 92.5f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.NUMERICAL_APTITUDE to 94,
                HolisticSkill.ANALYTICAL_CRITICAL to 92,
                HolisticSkill.LEADING_VISION to 88,
                HolisticSkill.SPEECH_VERBAL to 86,
                HolisticSkill.SELF_DISCIPLINE to 92,
                HolisticSkill.SELF_MOTIVATION to 92,
                HolisticSkill.LITERACY_LANGUAGE to 82,
                HolisticSkill.SOCIAL_COLLABORATION to 84,
                HolisticSkill.MEMORIZING_RECALL to 80,
                HolisticSkill.VISUAL_PERCEPTION to 75,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 78,
                HolisticSkill.CREATIVE_VISUALIZATION to 70,
                HolisticSkill.IMAGINATION_INNOVATION to 72,
                HolisticSkill.MOTOR_SKILLS to 45
            ),
            coreDemands = "Discounted cash flow (DCF) valuation, leveraged buyout (LBO) modeling, debt restructuring, syndicate pitch decks, and high-intensity transaction execution.",
            dayInTheLifeMorning = "09:00 AM — Builds 3-statement financial model for a ₹2,400 Crore IPO roadshow; stress-tests EBITDA sensitivity.",
            dayInTheLifeAfternoon = "02:30 PM — Delivers confidential transaction pitch to sovereign wealth fund directors in Mumbai.",
            dayInTheLifeEvening = "07:00 PM — Reviews legal disclosure circulars and fairness opinion documentation with lead syndicate underwriters.",
            topCollegesInIndia = listOf("SRCC Delhi", "IIM Ahmedabad (PG)", "IIM Bangalore (PG)", "IIM Calcutta (PG)", "St. Xavier's Mumbai"),
            entranceExams = listOf("CUET-UG (Top 99.8%ile for SRCC)", "CAT (99.5%+ for IIM A/B/C)", "IPMAT (IIM Indore)"),
            degreePathways = listOf("B.Com (Hons.) / B.A. Economics (Hons.)", "B.Tech + MBA", "Chartered Financial Analyst (CFA)"),
            salaryProgression = "Analyst: ₹20–32 LPA • Associate: ₹40–70 LPA • Vice President / Managing Director: ₹1.5–4.5 Cr+",
            horizonOutlook2035 = "Vibrant economic catalyst (+26% YoY). Driving Indian capital expansion, private equity buyouts, and Unicorn IPOs."
        ),
        HolisticCareerProfile(
            rank = 7,
            title = "Autonomous Robotics & Mechatronics Engineering",
            stream = Stream.SCIENCE,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 92.1f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.CREATIVE_VISUALIZATION to 94,
                HolisticSkill.ANALYTICAL_CRITICAL to 92,
                HolisticSkill.NUMERICAL_APTITUDE to 90,
                HolisticSkill.MOTOR_SKILLS to 85,
                HolisticSkill.VISUAL_PERCEPTION to 86,
                HolisticSkill.IMAGINATION_INNOVATION to 84,
                HolisticSkill.SELF_DISCIPLINE to 82,
                HolisticSkill.SELF_MOTIVATION to 84,
                HolisticSkill.MEMORIZING_RECALL to 78,
                HolisticSkill.SOCIAL_COLLABORATION to 74,
                HolisticSkill.LITERACY_LANGUAGE to 70,
                HolisticSkill.SPEECH_VERBAL to 68,
                HolisticSkill.LEADING_VISION to 70,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 60
            ),
            coreDemands = "Kinematic trajectory planning, inverse dynamics, sensor fusion (LiDAR/SLAM/IMU), embedded microcontrollers, and real-time ROS2 pipelines.",
            dayInTheLifeMorning = "09:30 AM — Calibrates stereo depth camera and LiDAR point clouds on autonomous mobile warehouse robot.",
            dayInTheLifeAfternoon = "02:00 PM — Simulates 6-axis robotic manipulator inverse kinematics in Gazebo physics engine using C++.",
            dayInTheLifeEvening = "05:00 PM — Tests torque feedback controllers on hardware prototype; optimizes brushless DC motor drivers.",
            topCollegesInIndia = listOf("IIT Madras", "IIT Bombay", "IIT Kharagpur", "BITS Pilani", "IIIT Hyderabad"),
            entranceExams = listOf("JEE Advanced", "JEE Main", "BITSAT"),
            degreePathways = listOf("B.Tech in Mechatronics / Mechanical / Electrical with Robotics Specialization"),
            salaryProgression = "Entry: ₹14–24 LPA • Lead Systems Engineer: ₹35–60 LPA • Principal Architect: ₹90L–1.6 Cr+",
            horizonOutlook2035 = "Massive growth trajectory (+38% YoY). Automated manufacturing, defense drones, and humanoid robotics."
        ),
        HolisticCareerProfile(
            rank = 8,
            title = "Aerospace & Defence Systems Engineering",
            stream = Stream.SCIENCE,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 91.6f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.NUMERICAL_APTITUDE to 94,
                HolisticSkill.ANALYTICAL_CRITICAL to 92,
                HolisticSkill.CREATIVE_VISUALIZATION to 90,
                HolisticSkill.SELF_DISCIPLINE to 92,
                HolisticSkill.VISUAL_PERCEPTION to 84,
                HolisticSkill.MEMORIZING_RECALL to 80,
                HolisticSkill.MOTOR_SKILLS to 76,
                HolisticSkill.SELF_MOTIVATION to 85,
                HolisticSkill.IMAGINATION_INNOVATION to 80,
                HolisticSkill.SOCIAL_COLLABORATION to 75,
                HolisticSkill.LITERACY_LANGUAGE to 74,
                HolisticSkill.SPEECH_VERBAL to 70,
                HolisticSkill.LEADING_VISION to 72,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 60
            ),
            coreDemands = "Compressible aerodynamics (CFD), orbital propulsion, avionics telemetry, satellite structural vibration testing, and mission flight guidance.",
            dayInTheLifeMorning = "09:00 AM — Conducts Mach 3 supersonic flow shockwave simulations on rocket booster fairing in ANSYS Fluent.",
            dayInTheLifeAfternoon = "01:30 PM — Inspects titanium alloy nozzle additive manufacturing quality control scans at cleanroom facility.",
            dayInTheLifeEvening = "05:00 PM — Analyzes orbital insertion telemetry and reaction wheel drift data from low-Earth orbit satellite.",
            topCollegesInIndia = listOf("IIST Thiruvananthapuram (ISRO Feed)", "IIT Madras", "IIT Bombay", "IIT Kanpur", "MIT Manipal"),
            entranceExams = listOf("JEE Advanced (Direct IIST Admission)", "JEE Main"),
            degreePathways = listOf("B.Tech in Aerospace Engineering", "B.Tech in Avionics / Engineering Physics"),
            salaryProgression = "Scientist/Engineer 'SC' (ISRO/DRDO): ₹12–18 LPA (Govt Perks) • Private Aerospace: ₹20–45 LPA • Chief: ₹80L–1.5 Cr+",
            horizonOutlook2035 = "Strategic national priority (+32% YoY). Private satellite constellations, deep space exploration, and hypersonic defense."
        ),
        HolisticCareerProfile(
            rank = 9,
            title = "Chartered Accountancy & Strategic CFO Advisory",
            stream = Stream.COMMERCE,
            tag = CareerTag.STABLE,
            matchPercentage = 91.2f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.NUMERICAL_APTITUDE to 92,
                HolisticSkill.SELF_DISCIPLINE to 96,
                HolisticSkill.ANALYTICAL_CRITICAL to 90,
                HolisticSkill.MEMORIZING_RECALL to 90,
                HolisticSkill.LITERACY_LANGUAGE to 84,
                HolisticSkill.SELF_MOTIVATION to 92,
                HolisticSkill.SPEECH_VERBAL to 78,
                HolisticSkill.SOCIAL_COLLABORATION to 80,
                HolisticSkill.LEADING_VISION to 82,
                HolisticSkill.VISUAL_PERCEPTION to 76,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 72,
                HolisticSkill.CREATIVE_VISUALIZATION to 60,
                HolisticSkill.IMAGINATION_INNOVATION to 62,
                HolisticSkill.MOTOR_SKILLS to 45
            ),
            coreDemands = "Statutory corporate audit, Ind-AS & IFRS financial statements, cross-border transfer pricing, GST litigation, and forensic fraud detection.",
            dayInTheLifeMorning = "09:15 AM — Leads quarterly statutory audit review for a publicly listed telecom conglomerate with Big-4 engagement team.",
            dayInTheLifeAfternoon = "02:00 PM — Structures optimal tax-exempt corporate reorganization and debt demerger under Companies Act 2013.",
            dayInTheLifeEvening = "05:45 PM — Reviews internal controls, forensic transaction trails, and SAP ERP revenue recognition reconciliations.",
            topCollegesInIndia = listOf("ICAI (Institute of Chartered Accountants of India)", "SRCC Delhi", "St. Xavier's Kolkata", "Loyola College Chennai"),
            entranceExams = listOf("CA Foundation", "CA Intermediate", "CA Final (ICAI)"),
            degreePathways = listOf("Chartered Accountancy (CA) alongside B.Com / B.Com (Hons.)"),
            salaryProgression = "Fresh CA: ₹12–18 LPA • Big-4 Manager (5 Yrs): ₹28–45 LPA • Partner / CFO: ₹1.1–3.0 Cr+",
            horizonOutlook2035 = "Bedrock of global commerce (+20% YoY). Forensic data auditing, ESG assurance, and multinational tax harmonization."
        ),
        HolisticCareerProfile(
            rank = 10,
            title = "Product Design, Industrial Innovation & Spatial UX",
            stream = Stream.HUMANITIES,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 90.8f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.CREATIVE_VISUALIZATION to 96,
                HolisticSkill.IMAGINATION_INNOVATION to 94,
                HolisticSkill.VISUAL_PERCEPTION to 92,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 88,
                HolisticSkill.ANALYTICAL_CRITICAL to 80,
                HolisticSkill.MOTOR_SKILLS to 82,
                HolisticSkill.SPEECH_VERBAL to 80,
                HolisticSkill.LITERACY_LANGUAGE to 78,
                HolisticSkill.SOCIAL_COLLABORATION to 84,
                HolisticSkill.LEADING_VISION to 80,
                HolisticSkill.SELF_MOTIVATION to 82,
                HolisticSkill.SELF_DISCIPLINE to 78,
                HolisticSkill.NUMERICAL_APTITUDE to 65,
                HolisticSkill.MEMORIZING_RECALL to 68
            ),
            coreDemands = "User empathy mapping, ergonomic physical-digital prototyping, spatial user interfaces for AR/VR, typography hierarchy, and usability labs.",
            dayInTheLifeMorning = "09:30 AM — Synthesizes user feedback recordings from 20 hospital nurses testing a handheld diagnostic interface.",
            dayInTheLifeAfternoon = "01:30 PM — Rapidly prototypes spatial 3D interactive gesture models in Figma, Blender, and Unity for AR headset.",
            dayInTheLifeEvening = "05:00 PM — Runs usability cognitive walkthrough with engineering directors to align design tokens and component libraries.",
            topCollegesInIndia = listOf("NID Ahmedabad", "IDC School of Design (IIT Bombay)", "NID Bengaluru", "Srishti Institute Bengaluru"),
            entranceExams = listOf("NID DAT (Prelims & Mains)", "UCEED (IITs)", "CEED (Postgraduate)"),
            degreePathways = listOf("B.Des in Product Design / Interaction Design", "B.Des in Industrial Design"),
            salaryProgression = "Entry: ₹12–20 LPA • Senior Product Designer: ₹32–55 LPA • VP of Design / Chief Design Officer: ₹90L–2.0 Cr+",
            horizonOutlook2035 = "Exploding frontier (+36% YoY). Hardware-software convergence, spatial computing, and AI ergonomics."
        ),
        HolisticCareerProfile(
            rank = 11,
            title = "Computational Genomics & Precision Medicine",
            stream = Stream.SCIENCE,
            tag = CareerTag.EMERGING,
            matchPercentage = 90.4f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.ANALYTICAL_CRITICAL to 94,
                HolisticSkill.NUMERICAL_APTITUDE to 90,
                HolisticSkill.MEMORIZING_RECALL to 90,
                HolisticSkill.CREATIVE_VISUALIZATION to 84,
                HolisticSkill.VISUAL_PERCEPTION to 82,
                HolisticSkill.SELF_DISCIPLINE to 88,
                HolisticSkill.SELF_MOTIVATION to 86,
                HolisticSkill.LITERACY_LANGUAGE to 82,
                HolisticSkill.IMAGINATION_INNOVATION to 82,
                HolisticSkill.SPEECH_VERBAL to 70,
                HolisticSkill.SOCIAL_COLLABORATION to 72,
                HolisticSkill.LEADING_VISION to 70,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 68,
                HolisticSkill.MOTOR_SKILLS to 65
            ),
            coreDemands = "Next-generation sequencing (NGS) analysis, CRISPR-Cas9 target prediction, structural bioinformatics, protein folding (AlphaFold), and drug discovery.",
            dayInTheLifeMorning = "09:00 AM — Analyzes whole-genome variant call format (VCF) files identifying rare oncological gene mutations.",
            dayInTheLifeAfternoon = "02:00 PM — Runs molecular dynamic simulations docking small-molecule inhibitors into mutant kinase pockets.",
            dayInTheLifeEvening = "05:30 PM — Consults with clinical oncologists to design patient-specific mRNA therapeutic vaccination schedules.",
            topCollegesInIndia = listOf("IISc Bengaluru", "IIT Delhi", "IIT Kharagpur", "NCBS Bengaluru", "BITS Pilani"),
            entranceExams = listOf("JEE Advanced", "NEET-UG (for Biotech/Genomics)", "IISc Aptitude (IAT)"),
            degreePathways = listOf("B.Tech / B.S. in Biotechnology & Bioinformatics", "Integrated M.Sc. in Biological Sciences"),
            salaryProgression = "Bioinformatics Scientist: ₹14–24 LPA • Senior Scientist: ₹32–58 LPA • Principal Director: ₹85L–1.8 Cr+",
            horizonOutlook2035 = "Transformative healthcare frontier (+40% YoY). Personalized gene editing, synthetic biology, and cancer eradication."
        ),
        HolisticCareerProfile(
            rank = 12,
            title = "Startup Venture Building & Product Leadership",
            stream = Stream.COMMERCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 89.9f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.LEADING_VISION to 96,
                HolisticSkill.SELF_MOTIVATION to 95,
                HolisticSkill.ANALYTICAL_CRITICAL to 90,
                HolisticSkill.SPEECH_VERBAL to 92,
                HolisticSkill.SOCIAL_COLLABORATION to 90,
                HolisticSkill.IMAGINATION_INNOVATION to 90,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 88,
                HolisticSkill.SELF_DISCIPLINE to 88,
                HolisticSkill.NUMERICAL_APTITUDE to 82,
                HolisticSkill.LITERACY_LANGUAGE to 82,
                HolisticSkill.CREATIVE_VISUALIZATION to 78,
                HolisticSkill.VISUAL_PERCEPTION to 74,
                HolisticSkill.MEMORIZING_RECALL to 75,
                HolisticSkill.MOTOR_SKILLS to 55
            ),
            coreDemands = "Defining 0-to-1 product market fit, venture capital fundraising, organizational hiring, customer discovery interviews, and unit economic optimization.",
            dayInTheLifeMorning = "08:30 AM — Pitches Series-A investment syndicate for $6M round; articulates retention metrics and CAC/LTV curves.",
            dayInTheLifeAfternoon = "01:30 PM — Leads cross-functional engineering and growth sprint prioritizing viral referral loops and checkout conversion.",
            dayInTheLifeEvening = "06:00 PM — Closes strategic enterprise distribution partnership with tier-1 Indian retail network.",
            topCollegesInIndia = listOf("IIT Bombay", "IIT Delhi", "BITS Pilani", "IIM Ahmedabad", "St. Stephen's Delhi"),
            entranceExams = listOf("JEE Advanced", "BITSAT", "CAT (IIMs)", "CUET-UG"),
            degreePathways = listOf("B.Tech in Any Discipline / BBA / B.Com followed by real-world venture experimentation"),
            salaryProgression = "Founder/CEO: Equity-driven (₹10–50 Cr+ exit potential) • VP Product: ₹50–90 LPA + 1% ESOPs",
            horizonOutlook2035 = "Hyper-dynamic ecosystem. India's rise to #1 global startup engine across fintech, deeptech, and agritech."
        ),
        HolisticCareerProfile(
            rank = 13,
            title = "Management Consulting & Strategic Operations",
            stream = Stream.COMMERCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 89.5f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.ANALYTICAL_CRITICAL to 94,
                HolisticSkill.SPEECH_VERBAL to 92,
                HolisticSkill.LEADING_VISION to 90,
                HolisticSkill.NUMERICAL_APTITUDE to 88,
                HolisticSkill.SELF_DISCIPLINE to 92,
                HolisticSkill.SOCIAL_COLLABORATION to 90,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 86,
                HolisticSkill.LITERACY_LANGUAGE to 88,
                HolisticSkill.SELF_MOTIVATION to 90,
                HolisticSkill.MEMORIZING_RECALL to 80,
                HolisticSkill.IMAGINATION_INNOVATION to 78,
                HolisticSkill.VISUAL_PERCEPTION to 75,
                HolisticSkill.CREATIVE_VISUALIZATION to 72,
                HolisticSkill.MOTOR_SKILLS to 45
            ),
            coreDemands = "MECE problem breakdown, corporate restructuring, cost-takeout diagnosis, market entry blueprints, and C-suite stakeholder alignment.",
            dayInTheLifeMorning = "09:00 AM — Conducts hypothesis-driven operational cost audit for Fortune 500 manufacturing client.",
            dayInTheLifeAfternoon = "02:00 PM — Builds financial waterfall scenario model quantifying supply chain savings across 14 distribution hubs.",
            dayInTheLifeEvening = "05:30 PM — Delivers executive steering committee presentation to Managing Director and CEO.",
            topCollegesInIndia = listOf("IIM Ahmedabad", "IIM Bangalore", "IIM Calcutta", "ISB Hyderabad", "SRCC Delhi"),
            entranceExams = listOf("CAT (99.7%+ for MBB Shortlist)", "GMAT (730+)", "CUET-UG"),
            degreePathways = listOf("B.Tech / B.A. Economics / B.Com followed by MBA from Tier-1 Business School"),
            salaryProgression = "Business Analyst: ₹22–32 LPA • Engagement Manager: ₹50–85 LPA • Partner (MBB): ₹1.5–4.0 Cr+",
            horizonOutlook2035 = "Enduring executive reliance (+24% YoY). Guiding corporate transformations through AI integration and global reshoring."
        ),
        HolisticCareerProfile(
            rank = 14,
            title = "Clinical Neuropsychology & Behavioral Cognitive Science",
            stream = Stream.HUMANITIES,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 88.9f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 96,
                HolisticSkill.ANALYTICAL_CRITICAL to 90,
                HolisticSkill.SPEECH_VERBAL to 90,
                HolisticSkill.LITERACY_LANGUAGE to 90,
                HolisticSkill.SOCIAL_COLLABORATION to 88,
                HolisticSkill.MEMORIZING_RECALL to 86,
                HolisticSkill.SELF_DISCIPLINE to 88,
                HolisticSkill.SELF_MOTIVATION to 86,
                HolisticSkill.LEADING_VISION to 80,
                HolisticSkill.VISUAL_PERCEPTION to 80,
                HolisticSkill.IMAGINATION_INNOVATION to 78,
                HolisticSkill.CREATIVE_VISUALIZATION to 72,
                HolisticSkill.NUMERICAL_APTITUDE to 70,
                HolisticSkill.MOTOR_SKILLS to 50
            ),
            coreDemands = "Administering neuropsychological batteries (WAIS/WISC), cognitive rehabilitation for traumatic brain injury, fMRI scan interpretation, and psychotherapeutic triage.",
            dayInTheLifeMorning = "09:00 AM — Conducts 2-hour neuropsychological cognitive assessment testing memory and executive dysfunction post-stroke.",
            dayInTheLifeAfternoon = "01:30 PM — Delivers evidence-based Cognitive Behavioral Therapy (CBT) session for severe adolescent anxiety disorder.",
            dayInTheLifeEvening = "05:00 PM — Analyzes resting-state fMRI neural connectivity maps in collaboration with hospital neurology department.",
            topCollegesInIndia = listOf("NIMHANS Bengaluru", "Lady Shri Ram College Delhi", "Christ University Bengaluru", "TISS Mumbai"),
            entranceExams = listOf("CUET-UG (Top Psychology Cuts)", "NIMHANS Entrance", "TISS-NET"),
            degreePathways = listOf("B.A./B.Sc. in Psychology followed by M.Sc./M.Phil in Clinical Psychology and RCI Licensing"),
            salaryProgression = "Clinical Psychologist: ₹8–16 LPA • Senior Consultant/Private Practice: ₹25–50 LPA • Super-Specialist: ₹70L–1.2 Cr+",
            horizonOutlook2035 = "Massive awareness explosion (+35% YoY). Mental health institutionalization, corporate wellness, and neuro-rehabilitation."
        ),
        HolisticCareerProfile(
            rank = 15,
            title = "Development Economics, Public Policy & Diplomatic Strategy",
            stream = Stream.HUMANITIES,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 88.5f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.ANALYTICAL_CRITICAL to 94,
                HolisticSkill.NUMERICAL_APTITUDE to 88,
                HolisticSkill.LITERACY_LANGUAGE to 92,
                HolisticSkill.SPEECH_VERBAL to 90,
                HolisticSkill.LEADING_VISION to 88,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 86,
                HolisticSkill.SOCIAL_COLLABORATION to 86,
                HolisticSkill.SELF_DISCIPLINE to 88,
                HolisticSkill.MEMORIZING_RECALL to 82,
                HolisticSkill.SELF_MOTIVATION to 88,
                HolisticSkill.IMAGINATION_INNOVATION to 80,
                HolisticSkill.VISUAL_PERCEPTION to 72,
                HolisticSkill.CREATIVE_VISUALIZATION to 68,
                HolisticSkill.MOTOR_SKILLS to 45
            ),
            coreDemands = "Econometric randomized controlled trials (RCTs), poverty alleviation modeling, macro-fiscal policy drafting, carbon tax incentives, and multilateral negotiation.",
            dayInTheLifeMorning = "09:00 AM — Evaluates micro-econometric panel dataset on direct cash benefit transfers across 50,000 rural households in Stata/R.",
            dayInTheLifeAfternoon = "02:00 PM — Drafts policy white paper for NITI Aayog regarding urban municipal bond financing frameworks.",
            dayInTheLifeEvening = "05:30 PM — Participates in World Bank bilateral roundtable addressing climate adaptation finance for South Asia.",
            topCollegesInIndia = listOf("Delhi School of Economics (DSE)", "St. Stephen's College", "JNU New Delhi", "ISI Delhi", "Ashoka University"),
            entranceExams = listOf("CUET-UG", "CUET-PG (DSE)", "ISI Admission Test"),
            degreePathways = listOf("B.A. (Hons.) Economics followed by M.A. / M.Sc. in Economics or Master in Public Policy (MPP)"),
            salaryProgression = "Policy Analyst: ₹12–20 LPA • Senior Economist (World Bank/IMF): ₹35–75 LPA • Chief Economic Advisor: Sovereign Impact",
            horizonOutlook2035 = "Crucial macro-stability role (+26% YoY). Sovereign debt management, global decarbonization, and demographic transitions."
        ),
        HolisticCareerProfile(
            rank = 16,
            title = "Clean Energy, Battery Storage & Electric Mobility Systems",
            stream = Stream.SCIENCE,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 88.0f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.ANALYTICAL_CRITICAL to 92,
                HolisticSkill.NUMERICAL_APTITUDE to 90,
                HolisticSkill.CREATIVE_VISUALIZATION to 86,
                HolisticSkill.MOTOR_SKILLS to 82,
                HolisticSkill.VISUAL_PERCEPTION to 84,
                HolisticSkill.SELF_DISCIPLINE to 88,
                HolisticSkill.SELF_MOTIVATION to 85,
                HolisticSkill.IMAGINATION_INNOVATION to 84,
                HolisticSkill.SOCIAL_COLLABORATION to 78,
                HolisticSkill.MEMORIZING_RECALL to 78,
                HolisticSkill.LITERACY_LANGUAGE to 74,
                HolisticSkill.LEADING_VISION to 75,
                HolisticSkill.SPEECH_VERBAL to 70,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 62
            ),
            coreDemands = "Lithium-ion & solid-state electrochemical cell modeling, battery management system (BMS) thermal runaway prevention, grid-scale solar inverters, and EV powertrain design.",
            dayInTheLifeMorning = "09:00 AM — Analyzes impedance spectroscopy and thermal dissipation curves on high-nickel pouch cells.",
            dayInTheLifeAfternoon = "02:00 PM — Codes BMS micro-controller firmware to dynamically balance voltage across 96 series battery modules.",
            dayInTheLifeEvening = "05:00 PM — Tests prototype EV regenerative braking power converters under simulated highway heat-stress conditions.",
            topCollegesInIndia = listOf("IIT Madras", "IIT Roorkee", "IIT Delhi", "BITS Pilani", "NIT Trichy"),
            entranceExams = listOf("JEE Advanced", "JEE Main", "BITSAT"),
            degreePathways = listOf("B.Tech in Electrical / Chemical / Mechanical with Energy Storage Minor"),
            salaryProgression = "Battery Engineer: ₹12–22 LPA • Senior Powertrain Architect: ₹30–55 LPA • VP Engineering: ₹80L–1.7 Cr+",
            horizonOutlook2035 = "Gigawatt-scale national transition (+42% YoY). Net-zero grid infrastructure and universal fleet electrification."
        ),
        HolisticCareerProfile(
            rank = 17,
            title = "Actuarial Science, Catastrophe Modeling & Risk Mathematics",
            stream = Stream.COMMERCE,
            tag = CareerTag.PREMIUM_ELITE,
            matchPercentage = 87.6f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.NUMERICAL_APTITUDE to 98,
                HolisticSkill.ANALYTICAL_CRITICAL to 94,
                HolisticSkill.MEMORIZING_RECALL to 88,
                HolisticSkill.SELF_DISCIPLINE to 94,
                HolisticSkill.SELF_MOTIVATION to 90,
                HolisticSkill.VISUAL_PERCEPTION to 78,
                HolisticSkill.LITERACY_LANGUAGE to 78,
                HolisticSkill.CREATIVE_VISUALIZATION to 72,
                HolisticSkill.SOCIAL_COLLABORATION to 72,
                HolisticSkill.IMAGINATION_INNOVATION to 70,
                HolisticSkill.LEADING_VISION to 72,
                HolisticSkill.SPEECH_VERBAL to 70,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 65,
                HolisticSkill.MOTOR_SKILLS to 45
            ),
            coreDemands = "Markov chain mortality modeling, climate catastrophe reinsurance pooling, extreme value theory, loss reserving, and solvency capital adequacy.",
            dayInTheLifeMorning = "09:15 AM — Formulates Generalized Linear Models (GLMs) pricing flood and monsoon property damage risks across coastal India.",
            dayInTheLifeAfternoon = "02:00 PM — Stress-tests life insurance liability reserves against pandemic longevity shift scenarios in R and Python.",
            dayInTheLifeEvening = "05:30 PM — Prepares Appointed Actuary statutory solvency certificate for submission to IRDAI regulatory body.",
            topCollegesInIndia = listOf("Institute of Actuaries of India (IAI)", "ISI Kolkata", "Delhi University (Stats/Maths)", "St. Xavier's Mumbai"),
            entranceExams = listOf("ACET (Actuarial Common Entrance Test - IAI)", "CUET-UG (Stats/Maths)"),
            degreePathways = listOf("B.Sc. (Hons.) Statistics / Mathematics alongside IAI Core Principles Series (CS1, CS2, CM1, CM2)"),
            salaryProgression = "Student Actuary: ₹10–18 LPA • Fully Qualified Fellow (FIAI): ₹35–70 LPA • Chief Actuary: ₹1.2–2.8 Cr+",
            horizonOutlook2035 = "Exceptional mathematical security (+25% YoY). Climate risk insurance, autonomous vehicle liability, and pension solvency."
        ),
        HolisticCareerProfile(
            rank = 18,
            title = "Sustainable Architecture & Resilient Urban Planning",
            stream = Stream.SCIENCE,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 87.1f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.CREATIVE_VISUALIZATION to 96,
                HolisticSkill.VISUAL_PERCEPTION to 92,
                HolisticSkill.ANALYTICAL_CRITICAL to 88,
                HolisticSkill.IMAGINATION_INNOVATION to 90,
                HolisticSkill.NUMERICAL_APTITUDE to 82,
                HolisticSkill.MOTOR_SKILLS to 82,
                HolisticSkill.SELF_DISCIPLINE to 85,
                HolisticSkill.LEADING_VISION to 82,
                HolisticSkill.SOCIAL_COLLABORATION to 82,
                HolisticSkill.SPEECH_VERBAL to 80,
                HolisticSkill.LITERACY_LANGUAGE to 78,
                HolisticSkill.SELF_MOTIVATION to 84,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 75,
                HolisticSkill.MEMORIZING_RECALL to 74
            ),
            coreDemands = "Passive solar orientation, embodied carbon life-cycle assessment, BIM Revit parametric scripting, structural load integration, and municipal zoning compliance.",
            dayInTheLifeMorning = "09:30 AM — Reviews solar daylighting heat gain simulations and rainwater harvesting yields on a 20-acre net-zero educational campus.",
            dayInTheLifeAfternoon = "02:00 PM — Develops parametric generative facade patterns in Rhino Grasshopper that reduce air conditioning cooling load by 35%.",
            dayInTheLifeEvening = "05:30 PM — Meets municipal urban planning commission to defend high-density transit-oriented master plan.",
            topCollegesInIndia = listOf("CEPT University Ahmedabad", "IIT Roorkee (Arch)", "IIT Kharagpur", "SPA New Delhi", "Sir JJ College of Architecture"),
            entranceExams = listOf("NATA (National Aptitude Test in Architecture)", "JEE Main Paper 2 (B.Arch)", "JEE Advanced AAT"),
            degreePathways = listOf("B.Arch (5-Year Professional Degree recognized by Council of Architecture)"),
            salaryProgression = "Junior Architect: ₹6–12 LPA • Senior Project Architect: ₹20–40 LPA • Principal Design Partner: ₹75L–2.0 Cr+",
            horizonOutlook2035 = "Fundamental ecological imperative (+30% YoY). Designing heat-resilient megacities and carbon-negative building infrastructure."
        ),
        HolisticCareerProfile(
            rank = 19,
            title = "Full-Stack Data Engineering & Analytics Infrastructure",
            stream = Stream.SCIENCE,
            tag = CareerTag.HIGH_GROWTH,
            matchPercentage = 86.8f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.ANALYTICAL_CRITICAL to 94,
                HolisticSkill.NUMERICAL_APTITUDE to 90,
                HolisticSkill.SELF_DISCIPLINE to 90,
                HolisticSkill.MEMORIZING_RECALL to 85,
                HolisticSkill.VISUAL_PERCEPTION to 82,
                HolisticSkill.SELF_MOTIVATION to 86,
                HolisticSkill.CREATIVE_VISUALIZATION to 78,
                HolisticSkill.IMAGINATION_INNOVATION to 76,
                HolisticSkill.SOCIAL_COLLABORATION to 78,
                HolisticSkill.LITERACY_LANGUAGE to 75,
                HolisticSkill.SPEECH_VERBAL to 70,
                HolisticSkill.LEADING_VISION to 72,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 64,
                HolisticSkill.MOTOR_SKILLS to 50
            ),
            coreDemands = "Apache Spark distributed ETL pipelines, Apache Kafka event streaming, Lakehouse data warehousing (Snowflake/BigQuery), and data schema governance.",
            dayInTheLifeMorning = "09:00 AM — Diagnoses streaming Kafka consumer group lag processing 500,000 telemetry events/minute.",
            dayInTheLifeAfternoon = "01:30 PM — Optimizes Apache Spark partition skewed joins in PySpark, reducing nightly ETL job runtime by 4 hours.",
            dayInTheLifeEvening = "05:00 PM — Implements dbt semantic layer data models to serve real-time financial reporting metrics to executive BI dashboards.",
            topCollegesInIndia = listOf("IIT Kanpur", "IIT Guwahati", "BITS Pilani", "NIT Surathkal", "IIIT Bangalore"),
            entranceExams = listOf("JEE Advanced", "JEE Main", "BITSAT"),
            degreePathways = listOf("B.Tech in Computer Science / Information Technology / Data Science"),
            salaryProgression = "Data Engineer: ₹14–25 LPA • Lead Architect: ₹35–65 LPA • VP of Data Platform: ₹95L–2.2 Cr+",
            horizonOutlook2035 = "Universal enterprise foundation (+36% YoY). Data cleanliness and reliability are the fuel for every downstream AI algorithm."
        ),
        HolisticCareerProfile(
            rank = 20,
            title = "Investigative Journalism, Documentary Media & Public Advocacy",
            stream = Stream.HUMANITIES,
            tag = CareerTag.UNCOMMON,
            matchPercentage = 86.2f,
            studentVector = emptyMap(),
            industryVector = mapOf(
                HolisticSkill.LITERACY_LANGUAGE to 96,
                HolisticSkill.SPEECH_VERBAL to 94,
                HolisticSkill.EMOTIONAL_INTELLIGENCE to 92,
                HolisticSkill.ANALYTICAL_CRITICAL to 88,
                HolisticSkill.LEADING_VISION to 86,
                HolisticSkill.SELF_MOTIVATION to 92,
                HolisticSkill.SOCIAL_COLLABORATION to 84,
                HolisticSkill.SELF_DISCIPLINE to 85,
                HolisticSkill.MEMORIZING_RECALL to 80,
                HolisticSkill.VISUAL_PERCEPTION to 82,
                HolisticSkill.IMAGINATION_INNOVATION to 84,
                HolisticSkill.CREATIVE_VISUALIZATION to 75,
                HolisticSkill.NUMERICAL_APTITUDE to 65,
                HolisticSkill.MOTOR_SKILLS to 55
            ),
            coreDemands = "Open-source intelligence (OSINT), FOI / RTI filing, forensic paper trails, whistleblower source protection, documentary video editing, and narrative longform writing.",
            dayInTheLifeMorning = "08:30 AM — Scrutinizes corporate registrar shell company records and public land registry filings for an environmental expose.",
            dayInTheLifeAfternoon = "01:30 PM — Conducts on-ground confidential interview with river conservation whistleblowers in hazardous mining zone.",
            dayInTheLifeEvening = "06:00 PM — Edits 4K documentary video narrative and fact-checks investigative report with senior media legal counsel.",
            topCollegesInIndia = listOf("Asian College of Journalism (ACJ) Chennai", "IIMC New Delhi", "St. Xavier's Mumbai", "Jamia Millia Islamia (MCRC)"),
            entranceExams = listOf("ACJ Entrance Examination", "CUET-UG (Mass Comm)", "IIMC National Entrance"),
            degreePathways = listOf("B.A. in Journalism / Mass Communication / English Literature followed by PG Diploma in Investigative Journalism"),
            salaryProgression = "Reporter/Producer: ₹7–14 LPA • Senior Investigative Editor: ₹22–45 LPA • Editor-in-Chief / Media Director: ₹60L–1.5 Cr+",
            horizonOutlook2035 = "Pivotal democratic pillar (+22% YoY). Fact-checking deepfakes, climate investigative storytelling, and holding power accountable."
        )
    )
}

// -----------------------------------------------------------------------------------------
// 4. HOLISTIC ASSESSMENT ENGINE (The Core Mathematical Evaluator)
// -----------------------------------------------------------------------------------------
object HolisticAssessmentEngine {

    fun evaluateDossier(
        profile: StudentProfile,
        studentAnswers: Map<String, String>,
        parentAnswers: Map<String, String>
    ): HolisticDossierMetrics {
        val name = profile.studentName.ifBlank { "Candidate" }

        // Step 1: Calculate 14 Holistic Skill Scores (0 to 100)
        val skillPoints = HolisticSkill.entries.associateWith { 0.0f }.toMutableMap()
        val skillMaxPoints = HolisticSkill.entries.associateWith { 0.0f }.toMutableMap()

        // Polar Accumulators (-100 to +100)
        val polarSum = PersonalityPolarDimension.entries.associateWith { 0.0f }.toMutableMap()
        val polarCounts = PersonalityPolarDimension.entries.associateWith { 0 }.toMutableMap()

        // Question-by-question audit accumulator
        val auditItems = mutableListOf<HolisticQuestionAuditItem>()

        PremiumHolisticAssessmentBank.QUESTIONS_45.forEachIndexed { index, q ->
            val answer = studentAnswers[q.id]
            var scoreNorm = 0.5f
            var isCorrect = false
            var answerText = "Unanswered"
            var insight = "Standard neutral response."

            if (answer != null) {
                when (q.questionType) {
                    QuestionType.LIKERT -> {
                        val numeric = answer.toIntOrNull() ?: 3
                        scoreNorm = (numeric - 1) / 4.0f
                        isCorrect = numeric >= 4
                        answerText = when (numeric) {
                            1 -> "Strongly Disagree (1/5)"
                            2 -> "Disagree (2/5)"
                            3 -> "Neutral / Balanced (3/5)"
                            4 -> "Agree (4/5)"
                            5 -> "Strongly Agree (5/5)"
                            else -> "Score $numeric"
                        }
                        insight = when {
                            numeric >= 4 -> "Demonstrates high innate affinity and instinctive flow in ${q.primarySkill.displayName}."
                            numeric <= 2 -> "Shows selective boundary or lower unconscious reliance on this cognitive pathway."
                            else -> "Balanced and adaptable; engages moderately without cognitive friction."
                        }

                        // Polar shift
                        val polarDirection = q.likertPolarDirection
                        if (q.polarDimension != null && polarDirection != 0.0f) {
                            val polarVal = ((numeric - 3) / 2.0f) * polarDirection // -1.0 to +1.0
                            polarSum[q.polarDimension] = (polarSum[q.polarDimension] ?: 0f) + polarVal
                            polarCounts[q.polarDimension] = (polarCounts[q.polarDimension] ?: 0) + 1
                        }
                    }
                    QuestionType.MCQ_APTITUDE, QuestionType.SCENARIO_PREFERENCE -> {
                        val opt = q.options.find { it.id == answer }
                        if (opt != null) {
                            scoreNorm = opt.scoreValue
                            isCorrect = opt.isAptitudeCorrect || opt.scoreValue >= 0.8f
                            answerText = "[Option ${opt.id}] ${opt.text}"
                            insight = opt.traitFeedback.ifBlank {
                                if (opt.isAptitudeCorrect) "Correct deductive application." else "Subconscious heuristic shortcut."
                            }

                            if (q.polarDimension != null && opt.polarShift != 0.0f) {
                                polarSum[q.polarDimension] = (polarSum[q.polarDimension] ?: 0f) + opt.polarShift
                                polarCounts[q.polarDimension] = (polarCounts[q.polarDimension] ?: 0) + 1
                            }
                        }
                    }
                }
            }

            // Accumulate Skill Scores
            val impact = q.likertSkillImpact.coerceIn(0.5f, 2.0f)
            skillPoints[q.primarySkill] = (skillPoints[q.primarySkill] ?: 0f) + (scoreNorm * impact)
            skillMaxPoints[q.primarySkill] = (skillMaxPoints[q.primarySkill] ?: 0f) + impact

            if (q.secondarySkill != null) {
                val secImpact = impact * 0.6f
                skillPoints[q.secondarySkill] = (skillPoints[q.secondarySkill] ?: 0f) + (scoreNorm * secImpact)
                skillMaxPoints[q.secondarySkill] = (skillMaxPoints[q.secondarySkill] ?: 0f) + secImpact
            }

            // Add to audit
            auditItems.add(
                HolisticQuestionAuditItem(
                    id = q.id,
                    number = index + 1,
                    category = q.sectionCategory,
                    targetQuotient = q.targetQuotient,
                    primarySkill = q.primarySkill,
                    questionText = q.promptText,
                    studentAnswerText = answerText,
                    isCorrectOrFavorable = isCorrect,
                    psychologicalMeaning = q.situationalContext.ifBlank { "${q.primarySkill.displayName} Aptitude & Response Pattern" },
                    subconsciousInsight = insight,
                    skillImpactDescription = "Targeted ${q.primarySkill.displayName} (+${(scoreNorm * 100).roundToInt()}%)"
                )
            )
        }

        // Final Normalized Skill Scores (0 to 100)
        val finalSkillScores = HolisticSkill.entries.associateWith { skill ->
            val max = skillMaxPoints[skill] ?: 1.0f
            val pts = skillPoints[skill] ?: 0.0f
            if (max > 0f) {
                ((pts / max) * 100f).roundToInt().coerceIn(25, 99)
            } else {
                50
            }
        }

        // Step 2: 360° Quotient Diagnosis
        val num = finalSkillScores[HolisticSkill.NUMERICAL_APTITUDE] ?: 50
        val ana = finalSkillScores[HolisticSkill.ANALYTICAL_CRITICAL] ?: 50
        val lit = finalSkillScores[HolisticSkill.LITERACY_LANGUAGE] ?: 50
        val mem = finalSkillScores[HolisticSkill.MEMORIZING_RECALL] ?: 50
        val mot = finalSkillScores[HolisticSkill.MOTOR_SKILLS] ?: 50
        val vpc = finalSkillScores[HolisticSkill.VISUAL_PERCEPTION] ?: 50
        val dis = finalSkillScores[HolisticSkill.SELF_DISCIPLINE] ?: 50
        val eq = finalSkillScores[HolisticSkill.EMOTIONAL_INTELLIGENCE] ?: 50
        val soc = finalSkillScores[HolisticSkill.SOCIAL_COLLABORATION] ?: 50
        val lea = finalSkillScores[HolisticSkill.LEADING_VISION] ?: 50

        val iqScore = (0.35f * num + 0.35f * ana + 0.15f * lit + 0.15f * mem).roundToInt().coerceIn(30, 99)
        val pqScore = (0.70f * mot + 0.15f * vpc + 0.15f * dis).roundToInt().coerceIn(30, 99)
        val sqScore = (0.35f * eq + 0.35f * soc + 0.30f * lea).roundToInt().coerceIn(30, 99)
        val overallBrainScore = (0.50f * iqScore + 0.20f * pqScore + 0.30f * sqScore).roundToInt().coerceIn(30, 99)

        val brainTier = when {
            overallBrainScore >= 85 -> "Apex Mastermind (Tier-1 Elite)"
            overallBrainScore >= 75 -> "Advanced Multi-Dimensional Achiever"
            overallBrainScore >= 65 -> "Strong Progressive Competency"
            else -> "Foundation Development Phase"
        }

        // Step 3: 10 Neurological Brain Functional Areas
        val rawBrainScores = NeurologicalBrainArea.entries.associateWith { area ->
            val mapped = area.mappedSkills
            val avg = mapped.map { finalSkillScores[it] ?: 50 }.average().toFloat()
            avg.roundToInt().coerceIn(30, 99)
        }

        val sortedBrainEntries = rawBrainScores.entries.sortedByDescending { it.value }
        val rankedBrainAreas = sortedBrainEntries.mapIndexed { rankIdx, entry ->
            val area = entry.key
            val sc = entry.value
            val (tierLabel, tierColor) = when {
                sc >= 80 -> "Excellent" to "#10B981"
                sc >= 60 -> "High" to "#F59E0B"
                else -> "Moderate" to "#D946EF"
            }

            val whatItIs = when (area) {
                NeurologicalBrainArea.FRONTAL_LOBE -> "Executive Command Center: Governs logical deduction, multi-step problem planning, behavioral impulse inhibition, and strategic long-term vision."
                NeurologicalBrainArea.BROCAS_AREA -> "Motor Speech & Oratory Engine: Coordinates vocal cadence, persuasive rhetoric, spontaneous debate delivery, and syntactic sentence construction."
                NeurologicalBrainArea.WERNICKES_AREA -> "Semantic & Language Comprehension Center: Decodes complex written and spoken text, vocabulary breadth, and nuances of tone and irony."
                NeurologicalBrainArea.OCCIPITAL_LOBE -> "Visual Processing & Pattern Discrimination: Analyzes spatial contours, visual anomalies, diagrammatic maps, and color-contrast signals."
                NeurologicalBrainArea.PARIETAL_LOBE -> "Sensory Integration & Quantitative Spatial Hub: Coordinates 3D spatial rotation, mental isometric modeling, and numerical calculation."
                NeurologicalBrainArea.CEREBELLUM -> "Motor Rhythm & Procedural Dexterity: Governs fine-motor surgical control, athletic hand-eye coordination, balance, and physical reflex timing."
                NeurologicalBrainArea.MOTOR_AREA -> "Voluntary Neuromuscular Cortex: Executes bodily physical movement, stamina endurance, and kinesthetic agility."
                NeurologicalBrainArea.TEMPORAL_LOBE -> "Auditory & Associative Memory Encoder: Converts sensory input into durable memory recall, fact associations, and emotional listening empathy."
                NeurologicalBrainArea.SENSORY_AREA -> "Somatosensory Environmental Radar: Receives tactile and environmental feedback, maintaining sharp environmental attentiveness."
                NeurologicalBrainArea.BRAIN_STEM -> "Autonomic Alertness & Baseline Stamina: Manages central nervous system arousal, sleep-wake cycles, and sustained exam alertness."
            }

            val howItHelps = when (area) {
                NeurologicalBrainArea.FRONTAL_LOBE -> "Enables you to remain calm under difficult academic exams, deconstruct multi-variable competitive problems, and lead peer groups with structured clarity."
                NeurologicalBrainArea.BROCAS_AREA -> "Powers effortless viva presentations, inter-school debates, boardroom pitching, and assertive conflict resolution without vocal hesitation."
                NeurologicalBrainArea.WERNICKES_AREA -> "Accelerates reading comprehension speed in long case studies, legal judgments, comprehension passages, and research whitepapers."
                NeurologicalBrainArea.OCCIPITAL_LOBE -> "Allows you to instantly detect discrepancies in charts, blueprints, microscope slides, and digital user interfaces."
                NeurologicalBrainArea.PARIETAL_LOBE -> "Gives you a massive natural advantage in geometry, physics kinematics, architectural blueprints, and algorithmic data structures."
                NeurologicalBrainArea.CEREBELLUM -> "Supports high-dexterity professions such as laparoscopic surgery, precision robotics, competitive sports, and digital product craftsmanship."
                NeurologicalBrainArea.MOTOR_AREA -> "Provides active physical stamina, preventing fatigue during 12-hour high school study routines or laboratory experiments."
                NeurologicalBrainArea.TEMPORAL_LOBE -> "Allows rapid retention of vast medical/legal terminology, formulas, historical dates, and empathetic active listening."
                NeurologicalBrainArea.SENSORY_AREA -> "Keeps you attuned to subtle shifts in room dynamics, non-verbal social cues, and hands-on laboratory equipment troubleshooting."
                NeurologicalBrainArea.BRAIN_STEM -> "Maintains physiological baseline energy, preventing cognitive burnout during multi-day competitive examination sprints."
            }

            val whatItDemands = when (area) {
                NeurologicalBrainArea.FRONTAL_LOBE -> "Demands mentally rigorous puzzles, strategic chess/algorithm challenges, and ownership of major school or community initiatives."
                NeurologicalBrainArea.BROCAS_AREA -> "Requires frequent public speaking, Model UN participation, impromptu speaking drills, and active classroom discourse."
                NeurologicalBrainArea.WERNICKES_AREA -> "Flourishes when immersed in classical literature, global geopolitics, philosophy essays, and advanced legal analysis."
                NeurologicalBrainArea.OCCIPITAL_LOBE -> "Benefits from visual arts, graphical data visualization, photography, and UI/UX aesthetic composition."
                NeurologicalBrainArea.PARIETAL_LOBE -> "Stimulated by 3D CAD modeling, geometry theorems, origami folding, and spatial origami/isometric sketching."
                NeurologicalBrainArea.CEREBELLUM -> "Maintained through musical instruments, martial arts, badminton/table tennis, and precision craft hobbies."
                NeurologicalBrainArea.MOTOR_AREA -> "Requires daily 45 minutes of vigorous cardiovascular movement, sports training, and dynamic postural breaks."
                NeurologicalBrainArea.TEMPORAL_LOBE -> "Strengthened via visual mnemonic peg systems, spaced repetition flashcards, and deep, distraction-free audiobooks."
                NeurologicalBrainArea.SENSORY_AREA -> "Grounded through hands-on laboratory workshops, outdoor wilderness navigation, and mindfulness sensory grounding."
                NeurologicalBrainArea.BRAIN_STEM -> "Demands strict 8-hour sleep hygiene, morning hydration, sunlight exposure, and disciplined screen curfews."
            }

            BrainAreaEvaluation(
                area = area,
                score = sc,
                rank = rankIdx + 1,
                tierLabel = tierLabel,
                tierColorHex = tierColor,
                whatItIs = whatItIs,
                howItHelpsStudent = howItHelps,
                whatItDemands = whatItDemands
            )
        }

        // Step 4: 10 Bi-Directional Personality Continuums (-100 to +100)
        val polarScores = PersonalityPolarDimension.entries.associateWith { dim ->
            val count = polarCounts[dim] ?: 0
            val sum = polarSum[dim] ?: 0f
            if (count > 0) {
                ((sum / count) * 100f).coerceIn(-100f, 100f)
            } else {
                0.0f
            }
        }

        // Step 5: National Peer Benchmark Medians & Percentiles
        val skillPercentiles = finalSkillScores.mapValues { (skill, studentScore) ->
            val median = NATIONAL_INDIAN_PEER_MEDIANS[skill] ?: 50
            val diff = studentScore - median
            // Approximate percentile from normal distribution with sigma ~ 15
            val z = diff / 15.0
            val p = (1.0 / (1.0 + Math.exp(-1.7 * z))) * 100.0
            p.roundToInt().coerceIn(15, 99)
        }

        // Step 6: Top Rated Skills (Superpowers) & Improvement Areas
        val sortedSkills = finalSkillScores.entries.sortedByDescending { it.value }
        val topRatedSkills = sortedSkills.take(4).map { it.key }
        val improvementSkills = sortedSkills.takeLast(3).map { it.key }

        // Prescriptive Remedial Interventions for bottom skills
        val remedialInterventions = improvementSkills.map { skill ->
            val curScore = finalSkillScores[skill] ?: 45
            when (skill) {
                HolisticSkill.MEMORIZING_RECALL -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Superbrain Yoga & Visual Peg Mnemonic Encoding",
                    coreExercise = "Practice 14 repetitions of Superbrain Yoga daily (earlobe cross-grip with deep squats) followed by 15-minute visual peg-memory drills associating formulas with vivid spatial objects.",
                    dailyRoutineMinutes = "20 Mins / Day",
                    cognitiveMechanism = "Stimulates bilateral hemispheric synchronization and strengthens hippocampus LTP (Long-Term Potentiation) for rapid factual retrieval.",
                    recommendedTools = listOf("Anki Spaced Repetition", "BrainHQ Memory Matrix", "Mind Palace Mnemonic Guides"),
                    projectedImprovement = "+24% Exam Retention & Zero Blank-Outs Under Pressure"
                )
                HolisticSkill.ANALYTICAL_CRITICAL -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Algorithmic Tree Deconstruction & Daily KenKen / Sudoku",
                    coreExercise = "Solve 2 medium-to-hard Sudoku grids and 1 logical syllogism grid daily before studying complex theoretical subjects. Force step-by-step written variable isolation.",
                    dailyRoutineMinutes = "25 Mins / Day",
                    cognitiveMechanism = "Strengthens dorsolateral prefrontal cortex neuroplasticity, training the brain to resist impulsive guesswork in multi-variable problems.",
                    recommendedTools = listOf("Brilliant.org Logic Series", "The New York Times Hard KenKen", "Polya's Problem Solving Manual"),
                    projectedImprovement = "+28% Competitive Exam Problem Accuracy (JEE/NEET/CLAT)"
                )
                HolisticSkill.SPEECH_VERBAL -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "2-Minute Impromptu Pecha-Kucha & Cadence Modulation",
                    coreExercise = "Pick a random daily newspaper headline, set a 60-second timer to organize 3 talking points (Hook, Argument, Call to Action), and speak aloud to a mirror without filler words.",
                    dailyRoutineMinutes = "15 Mins / Day",
                    cognitiveMechanism = "Conditions Broca's area motor speech circuits to bridge spontaneous internal conceptual thought with rapid, articulate vocal delivery.",
                    recommendedTools = listOf("Orai AI Speech Coach", "Toastmasters Youth Gavel Club", "Voice Memo Mirror Audits"),
                    projectedImprovement = "+35% Interview, Viva, and Presentation Confidence"
                )
                HolisticSkill.EMOTIONAL_INTELLIGENCE -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Daily Reflective Empathy Journaling & Non-Verbal Triage",
                    coreExercise = "Spend 10 minutes nightly noting 3 emotional triggers encountered during the day, analyzing the underlying root need, and writing an alternative empathetic perspective.",
                    dailyRoutineMinutes = "15 Mins / Day",
                    cognitiveMechanism = "Enhances anterior cingulate cortex regulation over the amygdala, dampening defensive reactivity and deepening interpersonal attunement.",
                    recommendedTools = listOf("Day One Guided EQ Journal", "MoodMeter App by Yale Center", "Active Listening Roleplays"),
                    projectedImprovement = "+30% Team Harmony & Conflict De-escalation Mastery"
                )
                HolisticSkill.SELF_DISCIPLINE -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "25-Minute Pomodoro Timeboxing & Physical Workspace Decluttering",
                    coreExercise = "Execute all study blocks in strict 25-minute Pomodoro sprints with zero smartphone notifications. Keep desk completely clear of everything except current subject material.",
                    dailyRoutineMinutes = "Structured Daily Habit",
                    cognitiveMechanism = "Lowers dopamine baseline volatility and trains cognitive endurance through clear micro-milestone completion rewards.",
                    recommendedTools = listOf("Forest Productivity Timer", "Physical Analog Desk Clock", "Daily Habit Trackers"),
                    projectedImprovement = "+40% Study Hours Efficiency & Minimal Procrastination"
                )
                HolisticSkill.CREATIVE_VISUALIZATION -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Isometric 3D Grid Sketching & Dual-Axis Mental Rotation",
                    coreExercise = "Sketch 3 complex geometric objects from top, side, and 45-degree isometric viewpoints daily without using a ruler.",
                    dailyRoutineMinutes = "20 Mins / Day",
                    cognitiveMechanism = "Builds parietal lobe spatial coordinate mapping, directly aiding chemistry molecular geometry and mechanical visualization.",
                    recommendedTools = listOf("Isometric Grid Sketchbooks", "Tinkercad 3D Design", "Mental Rotation Test Exercises"),
                    projectedImprovement = "+25% 3D Spatial Acuity & Architectural Geometry Mastery"
                )
                HolisticSkill.NUMERICAL_APTITUDE -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Vedic Mental Arithmetic Shortcuts & Successive Ratios",
                    coreExercise = "Drill 20 mental arithmetic problems daily using base-100 complement multiplication, rapid fraction-to-percentage conversion, and split compounding.",
                    dailyRoutineMinutes = "20 Mins / Day",
                    cognitiveMechanism = "Automates parietal-frontal numerical retrieval loops, eliminating mental fatigue in fast-paced quantitative sections.",
                    recommendedTools = listOf("Speed Math Pro", "Khan Academy Arithmetic Foundations", "Vedic Math Quick Manual"),
                    projectedImprovement = "+32% Calculation Speed & Zero Silliest Arithmetic Errors"
                )
                HolisticSkill.MOTOR_SKILLS -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Daily 45-Minute Aerobic Plyometrics & Agility Ladder Drills",
                    coreExercise = "Engage in 45 minutes of vigorous cardiovascular exercise (badminton, swimming, cycling, or agility footwork) 5 days a week.",
                    dailyRoutineMinutes = "45 Mins / Day",
                    cognitiveMechanism = "Boosts BDNF (Brain-Derived Neurotrophic Factor) in the cerebellum and motor cortex, enhancing mental stamina and neurogenesis.",
                    recommendedTools = listOf("Agility Floor Ladder", "Strava GPS Tracker", "Jump Rope Interval Routines"),
                    projectedImprovement = "+30% Physical Stamina & High-Stress Exam Endurance"
                )
                else -> PrescriptiveIntervention(
                    skill = skill,
                    currentScore = curScore,
                    title = "Targeted Deliberate Practice & Mentor Feedback Sprint",
                    coreExercise = "Spend 20 minutes daily reviewing exemplar solutions in ${skill.displayName}, identifying core principles, and recreating them independently.",
                    dailyRoutineMinutes = "20 Mins / Day",
                    cognitiveMechanism = "Systematically builds myelin insulation along relevant neural circuits through progressive deliberate repetition.",
                    recommendedTools = listOf("MargDarsh Guided Worksheets", "Peer Study Circles", "Targeted Skill Trackers"),
                    projectedImprovement = "+25% Demonstrated Competency & Confidence"
                )
            }
        }

        // Step 7: 2D Quadrant Plots (Interest vs. Potential)
        // X-Axis = Interest Score (0 to 100)
        // Y-Axis = Skill Potential (0 to 100)
        // Sweet Spot: X >= 50 && Y >= 50
        val sciencePotential = ((num * 0.40f + ana * 0.40f + mot * 0.20f)).roundToInt().coerceIn(30, 98)
        val commercePotential = ((num * 0.35f + ana * 0.30f + lea * 0.35f)).roundToInt().coerceIn(30, 98)
        val humanitiesPotential = ((lit * 0.35f + finalSkillScores[HolisticSkill.SPEECH_VERBAL]!! * 0.30f + eq * 0.35f)).roundToInt().coerceIn(30, 98)
        val vocationalPotential = ((finalSkillScores[HolisticSkill.CREATIVE_VISUALIZATION]!! * 0.40f + mot * 0.35f + finalSkillScores[HolisticSkill.IMAGINATION_INNOVATION]!! * 0.25f)).roundToInt().coerceIn(30, 98)

        val parentPref = parentAnswers["preferredStream"] ?: "open"
        val parentAsp = parentAnswers["aspiration"] ?: "balance"

        val scienceInterest = (if (parentPref.contains("Science", true)) 85 else if (parentAsp == "passion") 75 else 62).coerceIn(40, 95)
        val commerceInterest = (if (parentPref.contains("Commerce", true)) 85 else if (parentAsp == "earning") 82 else 60).coerceIn(40, 95)
        val humanitiesInterest = (if (parentPref.contains("Humanities", true)) 85 else if (parentAsp == "impact") 80 else 58).coerceIn(40, 95)
        val vocationalInterest = 55

        val streamQuadrants = listOf(
            StreamQuadrantPoint(
                stream = Stream.SCIENCE,
                displayName = "Science & Technology",
                interestScore = scienceInterest,
                potentialScore = sciencePotential,
                isSweetSpot = scienceInterest >= 50 && sciencePotential >= 50,
                strategicRecommendation = "Exceptional quantitative and analytical synergy. Prime trajectory for Tier-1 Engineering (IITs), Medicine (AIIMS), or Advanced Research."
            ),
            StreamQuadrantPoint(
                stream = Stream.COMMERCE,
                displayName = "Commerce & Finance",
                interestScore = commerceInterest,
                potentialScore = commercePotential,
                isSweetSpot = commerceInterest >= 50 && commercePotential >= 50,
                strategicRecommendation = "Powerful blend of commercial acumen, enterprise leadership, and quantitative calculation. Direct fit for Investment Banking, CA, or Corporate Strategy."
            ),
            StreamQuadrantPoint(
                stream = Stream.HUMANITIES,
                displayName = "Humanities & Social Sciences",
                interestScore = humanitiesInterest,
                potentialScore = humanitiesPotential,
                isSweetSpot = humanitiesInterest >= 50 && humanitiesPotential >= 50,
                strategicRecommendation = "Eloquent verbal command, high emotional empathy, and critical essay composition. Optimal for Top Law Schools (NLSIU), Civil Services (IAS), or Policy."
            )
        )

        // Step 8: Top 20 Career Radar Comparisons & Match Scoring
        // Compute Euclidean distance / Cosine similarity between student vector and industry vector
        val top20WithScores = Top20CareersRepository.PROFILES_20.map { career ->
            var sumDiffSq = 0.0
            var sumProduct = 0.0
            var magStudent = 0.0
            var magIndustry = 0.0

            HolisticSkill.entries.forEach { skill ->
                val sVal = (finalSkillScores[skill] ?: 50).toDouble()
                val iVal = (career.industryVector[skill] ?: 50).toDouble()

                sumProduct += (sVal * iVal)
                magStudent += (sVal * sVal)
                magIndustry += (iVal * iVal)

                val diff = sVal - iVal
                sumDiffSq += (diff * diff)
            }

            val cosine = if (magStudent > 0 && magIndustry > 0) {
                (sumProduct / (sqrt(magStudent) * sqrt(magIndustry)))
            } else 0.85

            // Normalized match percentage between 75% and 98.8%
            val matchPercent = (cosine * 100.0).toFloat().coerceIn(75.0f, 98.8f)

            career.copy(
                studentVector = finalSkillScores,
                matchPercentage = (matchPercent * 10f).roundToInt() / 10f
            )
        }.sortedByDescending { it.matchPercentage }

        // Assign ordinal rank badges (#1 to #20)
        val rankedTop20 = top20WithScores.mapIndexed { idx, career ->
            career.copy(rank = idx + 1)
        }

        // Generate Career Quadrant Points
        val careerQuadrants = rankedTop20.map { c ->
            val interestVal = (c.matchPercentage * 0.95f).roundToInt().coerceIn(45, 96)
            val potentialVal = (c.matchPercentage).roundToInt().coerceIn(50, 98)
            CareerQuadrantPoint(
                title = c.title,
                stream = c.stream,
                interestScore = interestVal,
                potentialScore = potentialVal,
                isSweetSpot = interestVal >= 50 && potentialVal >= 50,
                matchRank = c.rank
            )
        }

        return HolisticDossierMetrics(
            candidateName = name,
            iqScore = iqScore,
            pqScore = pqScore,
            sqScore = sqScore,
            overallBrainScore = overallBrainScore,
            brainScoreTier = brainTier,
            skillScores = finalSkillScores,
            nationalMedians = NATIONAL_INDIAN_PEER_MEDIANS,
            skillPercentiles = skillPercentiles,
            rankedBrainAreas = rankedBrainAreas,
            polarScores = polarScores,
            topRatedSkills = topRatedSkills,
            improvementSkills = improvementSkills,
            streamQuadrants = streamQuadrants,
            careerQuadrants = careerQuadrants,
            top20Careers = rankedTop20,
            remedialInterventions = remedialInterventions,
            questionAudits = auditItems
        )
    }
}
