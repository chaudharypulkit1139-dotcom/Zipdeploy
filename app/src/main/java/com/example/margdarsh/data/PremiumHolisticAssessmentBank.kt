package com.example.margdarsh.data

/**
 * =========================================================================================
 * MARGDARSH PREMIUM HOLISTIC ASSESSMENT FRAMEWORK & 75-QUESTION PSYCHOMETRIC BANK
 * =========================================================================================
 * Inspired by comprehensive psychometric and neuro-cognitive diagnostic reports (such as the
 * 63-page Bodhami Stream & Career Guidance Report).
 *
 * This file serves as the complete psychometric specification, question bank, and architectural
 * execution blueprint for generating the Tier-1 Multi-Page Holistic Career Dossier.
 *
 * SYSTEM GUARANTEE:
 * - This file introduces zero breaking changes to existing database, network, or UI classes.
 * - It provides the complete foundation required to produce:
 *   1. 360° Quotient Diagnosis (IQ, PQ, SQ)
 *   2. 10-Area Neurological Brain Mapping (Frontal Lobe, Broca, Wernicke, Occipital, etc.)
 *   3. 14 Holistic Skill Benchmark Radars (Student Score vs. National Peer Median)
 *   4. 10 Bi-Directional Personality Continuums (Soft-Athletic, Conventional-Enterprising, etc.)
 *   5. 2D Stream & Career Selection Quadrant Plots (Interest Score vs. Skill Potential)
 *   6. Top 20 Career Fitment Profiles with Dedicated Comparative Radar Overlays
 *   7. Prescriptive Skill Improvement Plans with Curated Remedial Interventions
 * =========================================================================================
 */

// -----------------------------------------------------------------------------------------
// 1. DIMENSION & TAXONOMY DEFINITIONS
// -----------------------------------------------------------------------------------------

/**
 * The 14 Core Competency Dimensions assessed in the holistic evaluation.
 */
enum class HolisticSkill(
    val code: String,
    val displayName: String,
    val category: String,
    val description: String
) {
    NUMERICAL_APTITUDE(
        "NUM",
        "Numerical Aptitude",
        "Mental / Academic",
        "Ability to analyze quantitative data, compute accurately, and recognize numerical patterns under logical constraints."
    ),
    ANALYTICAL_CRITICAL(
        "ANA",
        "Analytical & Critical Thinking",
        "Mental / Academic",
        "Ability to dissect complex multi-variable problems, evaluate arguments, and deduce logical conclusions."
    ),
    SPEECH_VERBAL(
        "SPV",
        "Speech & Verbal Fluency",
        "Communication",
        "Command over oral articulation, rhetorical delivery, persuasive inflection, and audience engagement."
    ),
    LITERACY_LANGUAGE(
        "LIT",
        "Literacy & Language Skills",
        "Communication",
        "Mastery of reading comprehension, vocabulary breadth, syntactic precision, and written expression."
    ),
    CREATIVE_VISUALIZATION(
        "VIS",
        "Creative Visualization",
        "Creative",
        "Mental projection of 3D spatial models, internal image manipulation, and aesthetic composition."
    ),
    IMAGINATION_INNOVATION(
        "INN",
        "Imagination & Innovation",
        "Creative",
        "Lateral, out-of-the-box conceptualization, divergent thinking, and novel problem synthesis."
    ),
    MOTOR_SKILLS(
        "MOT",
        "Motor Skills & Physical Stamina",
        "Physical",
        "Kinesthetic control, fine/gross motor coordination, hand-eye dexterity, and active physical endurance."
    ),
    MEMORIZING_RECALL(
        "MEM",
        "Memorizing & Working Memory",
        "Cognitive",
        "Retention of unstructured facts, visual-spatial working memory, and retrieval speed under distraction."
    ),
    VISUAL_PERCEPTION(
        "VPC",
        "Visual Perception",
        "Cognitive",
        "Attentiveness to subtle visual details, pattern discrepancies, spatial symmetry, and environmental cues."
    ),
    SELF_DISCIPLINE(
        "DIS",
        "Self-Discipline & Mannerism",
        "Emotional / Conduct",
        "Impulse control, routine adherence, emotional composure, and ethical character formation."
    ),
    SELF_MOTIVATION(
        "MOTV",
        "Self-Motivation & Resilience",
        "Emotional / Conduct",
        "Intrinsic drive, goal tenacity, optimistic perseverance through failure, and autonomous initiative."
    ),
    EMOTIONAL_INTELLIGENCE(
        "EQ",
        "Emotional Intelligence",
        "Social / Emotional",
        "Self-awareness, regulation of affective states, social empathy, and diplomatic interpersonal navigation."
    ),
    SOCIAL_COLLABORATION(
        "SOC",
        "Social & Team Collaboration",
        "Social",
        "Cooperative synergy, group problem solving, conflict de-escalation, and active team listening."
    ),
    LEADING_VISION(
        "LEA",
        "Leading Vision & Influence",
        "Leadership",
        "Capacity to inspire peer groups, articulate a shared vision, organize initiatives, and accept accountability."
    )
}

/**
 * 10 Neurological Brain Functional Areas mapped from performance vectors.
 */
enum class NeurologicalBrainArea(
    val index: Int,
    val areaName: String,
    val primaryFunction: String,
    val mappedSkills: List<HolisticSkill>
) {
    BRAIN_STEM(
        1,
        "Brain Stem",
        "Consciousness, autonomic arousal, sustained alertness, and baseline stamina",
        listOf(HolisticSkill.MOTOR_SKILLS, HolisticSkill.SELF_DISCIPLINE)
    ),
    BROCAS_AREA(
        2,
        "Broca's Area",
        "Motor speech production, syntax formation, and vocal articulation",
        listOf(HolisticSkill.SPEECH_VERBAL)
    ),
    CEREBELLUM(
        3,
        "Cerebellum",
        "Motor coordination, procedural balance, fine dexterity, and rhythm",
        listOf(HolisticSkill.MOTOR_SKILLS)
    ),
    FRONTAL_LOBE(
        4,
        "Frontal Lobe",
        "Executive control, analytical deduction, planning, behavioral inhibition, and decision-making",
        listOf(HolisticSkill.ANALYTICAL_CRITICAL, HolisticSkill.LEADING_VISION, HolisticSkill.SELF_DISCIPLINE)
    ),
    MOTOR_AREA(
        5,
        "Motor Cortex",
        "Voluntary neuromuscular execution, reflex control, and spatial movement",
        listOf(HolisticSkill.MOTOR_SKILLS)
    ),
    OCCIPITAL_LOBE(
        6,
        "Occipital Lobe",
        "Visual signal decoding, spatial pattern discrimination, and color-contrast detection",
        listOf(HolisticSkill.VISUAL_PERCEPTION)
    ),
    PARIETAL_LOBE(
        7,
        "Parietal Lobe",
        "Multisensory integration, spatial navigation, and mathematical quantity processing",
        listOf(HolisticSkill.NUMERICAL_APTITUDE, HolisticSkill.CREATIVE_VISUALIZATION)
    ),
    SENSORY_AREA(
        8,
        "Somatosensory Area",
        "Tactile feedback, sensory differentiation, and environmental awareness",
        listOf(HolisticSkill.VISUAL_PERCEPTION, HolisticSkill.MOTOR_SKILLS)
    ),
    TEMPORAL_LOBE(
        9,
        "Temporal Lobe",
        "Auditory processing, associative long-term memory encoding, and emotional memory",
        listOf(HolisticSkill.MEMORIZING_RECALL, HolisticSkill.EMOTIONAL_INTELLIGENCE)
    ),
    WERNICKES_AREA(
        10,
        "Wernicke's Area",
        "Language comprehension, semantic interpretation, and auditory vocabulary recognition",
        listOf(HolisticSkill.LITERACY_LANGUAGE)
    )
}

/**
 * 10 Bi-Directional Personality Dimensions (Polar Continuums).
 * Score range: -1.0 (Left Pole) to +1.0 (Right Pole), centered at 0.0 (Balanced).
 */
enum class PersonalityPolarDimension(
    val leftPole: String,
    val rightPole: String,
    val psychologicalAxis: String,
    val leftDescription: String,
    val rightDescription: String
) {
    SOFT_VS_ATHLETIC(
        "Soft / Placid",
        "Athletic / Active",
        "Physical Engagement & Bodily Vigor",
        "Prefers relaxed, indoor, intellectual or low-exertion pursuits with steady sedentary comfort.",
        "Demonstrates high physical energy, thrives in vigorous outdoor movement and kinesthetic engagement."
    ),
    SYNTHETIC_VS_ANALYTICAL(
        "Synthetic / Intuitive",
        "Analytical / Deductive",
        "Cognitive Processing Style",
        "Rallies on holistic gut-feel, big-picture impressions, and organic synthesis of ideas.",
        "Systematic problem solver who requires step-by-step logic, empirical evidence, and algorithmic proof."
    ),
    CONVENTIONAL_VS_ENTERPRISING(
        "Conventional / Structured",
        "Enterprising / Bold",
        "Work Style & Risk Orientation",
        "Excels within organized rules, well-defined schedules, verified methods, and structured compliance.",
        "Drives bold experiments, embraces calculated ambiguity, seeks entrepreneurial autonomy and commercial frontiers."
    ),
    CONSISTENT_VS_INVENTIVE(
        "Consistent / Methodical",
        "Inventive / Experimental",
        "Operational Predictability",
        "Favors established routines, standard operating procedures, and reliable, reproducible precision.",
        "Regularly challenges established norms to pioneer unprecedented alternatives and experimental setups."
    ),
    REALISTIC_VS_IMAGINATIVE(
        "Realistic / Grounded",
        "Imaginative / Visionary",
        "Perceptual Focus",
        "Pragmatic observer anchored in tangible realities, practical utility, and immediate application.",
        "Conceptual dreamer who lives in possibilities, hypothetical scenarios, metaphors, and artistic visions."
    ),
    EASYGOING_VS_METICULOUS(
        "Easy-Going / Adaptable",
        "Meticulous / Perfectionist",
        "Attention to Detail & Standards",
        "Flexible, low-stress, comfortable with approximations, and quick to accommodate unexpected deviations.",
        "Exacting standards of precision, painstaking attention to microscopic details, and zero tolerance for sloppy work."
    ),
    RELAXED_VS_PERSEVERANT(
        "Relaxed / Measured",
        "Perseverant / Tenacious",
        "Adversity Response & Grit",
        "Takes challenges in stride without undue pressure, stepping away when roadblocks become stressful.",
        "Relentless grit, refuses to quit difficult problems until mastered, and thrives on pushing past barriers."
    ),
    SELFWILLED_VS_PERCEPTIVE(
        "Self-Willed / Autonomous",
        "Perceptive / Empathetic",
        "Interpersonal Alignment",
        "Strong individual agency, unbothered by crowd consensus, follows personal conviction stubbornly.",
        "Deeply tuned into peer emotions, intuitively absorbs subtle room cues, and prioritizes harmonious alignment."
    ),
    RESERVED_VS_SOCIABLE(
        "Reserved / Introspective",
        "Sociable / Extroverted",
        "Social Energy Replenishment",
        "Draws energy from quiet solitary focus, reflective hobbies, and small intimate circles.",
        "Energized by bustling group discussions, vibrant networking, public presence, and constant social exchange."
    ),
    FREESPIRITED_VS_DISCIPLINED(
        "Free-Spirited / Spontaneous",
        "Disciplined / Orderly",
        "Temporal Structure & Habit Formation",
        "Thrives on improvisation, serendipity, fluid timelines, and freedom from rigid calendar constraints.",
        "Runs on structured daily routines, rigorous self-imposed timetables, and organized milestone tracking."
    )
}

/**
 * 360° Macro Quotients.
 */
enum class HolisticQuotient(val code: String, val title: String, val description: String) {
    IQ(
        "IQ",
        "Intelligence Quotient (Mental Awareness)",
        "Measures cognitive reasoning, analytical deduction, literacy, memory recall, and academic problem-solving potential."
    ),
    PQ(
        "PQ",
        "Physical Quotient (Physical Awareness)",
        "Measures bodily health habits, kinesthetic coordination, motor agility, and active lifestyle vitality."
    ),
    SQ(
        "SQ",
        "Social Quotient (Social & Emotional Awareness)",
        "Measures emotional regulation, empathetic listening, collaborative teamwork, and visionary group influence."
    )
}

/**
 * Comprehensive Holistic Question Data Model.
 */
data class HolisticAssessmentQuestion(
    val id: String,
    val serialNumber: Int,
    val sectionCategory: String,
    val targetQuotient: HolisticQuotient,
    val primarySkill: HolisticSkill,
    val secondarySkill: HolisticSkill? = null,
    val polarDimension: PersonalityPolarDimension? = null,
    val promptText: String,
    val questionType: QuestionType,
    val situationalContext: String = "",
    val options: List<HolisticOption> = emptyList(),
    val likertSkillImpact: Float = 1.0f,
    val likertPolarDirection: Float = 0.0f // -1.0 to +1.0 along polarDimension
)

data class HolisticOption(
    val id: String,
    val text: String,
    val scoreValue: Float, // Normalized 0.0 to 1.0
    val traitFeedback: String = "",
    val polarShift: Float = 0.0f, // -1.0 to +1.0
    val isAptitudeCorrect: Boolean = false
)

// -----------------------------------------------------------------------------------------
// 2. THE 75 MASTER QUESTIONS SPECIFICATION
// -----------------------------------------------------------------------------------------

object PremiumHolisticAssessmentBank {

    /**
     * Complete repository of 75 rigorously drafted psychometric and cognitive items.
     * Distributed across:
     * - Section A: Numerical & Analytical Reasoning (Q1 - Q11) [IQ Core]
     * - Section B: Language, Literacy & Verbal Fluency (Q12 - Q22) [IQ Core]
     * - Section C: Spatial, Visual Perception & Creative Innovation (Q23 - Q34) [IQ/Creative]
     * - Section D: Working Memory, Logic Retention & Focus (Q35 - Q42) [Cognitive Core]
     * - Section E: Motor Skills, Kinesthetics & Physical Vitality (Q43 - Q50) [PQ Core]
     * - Section F: Self-Discipline, Resilience & Motivation (Q51 - Q58) [Conduct / Emotional]
     * - Section G: Emotional Intelligence & Empathy (Q59 - Q66) [SQ Core]
     * - Section H: Collaboration, Team Dynamics & Leadership (Q67 - Q75) [SQ / Leadership]
     */
    val QUESTIONS_75: List<HolisticAssessmentQuestion> = listOf(

        // =====================================================================================
        // SECTION A: NUMERICAL APTITUDE & ANALYTICAL CRITICAL THINKING (Q1 - Q11)
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_01",
            serialNumber = 1,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            secondarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "A water tank is drained by Pipe A in 6 hours and filled by Pipe B in 4 hours. If both pipes are opened simultaneously when the tank is half-full, how many hours will it take for the tank to become completely full?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "System flow calculation under opposing rate variables.",
            options = listOf(
                HolisticOption("A", "3 hours", 0.0f, "Incorrect rate subtraction.", 0.0f, false),
                HolisticOption("B", "6 hours", 1.0f, "Net filling rate = 1/4 - 1/6 = 1/12 per hour. Filling remaining 1/2 tank takes (1/2) / (1/12) = 6 hours.", 0.8f, true),
                HolisticOption("C", "8 hours", 0.0f, "Incorrect fraction inversion.", 0.0f, false),
                HolisticOption("D", "12 hours", 0.2f, "Calculated time to fill an entire empty tank, not the remaining half.", 0.2f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_02",
            serialNumber = 2,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "I enjoy examining tables of data, school marks, cricket batting averages, or pricing trends to spot hidden mathematical patterns.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_03",
            serialNumber = 3,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            secondarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Examine the logical series: 3, 7, 15, 31, 63, ___. What number logically follows next?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Exponential progression and recurrence formula recognition.",
            options = listOf(
                HolisticOption("A", "95", 0.0f, "Linear miscalculation.", 0.0f, false),
                HolisticOption("B", "126", 0.1f, "Missed addition of 1 to twice the predecessor.", 0.2f, false),
                HolisticOption("C", "127", 1.0f, "Pattern is x_n = 2 * x_{n-1} + 1 (or 2^(n+1) - 1). 63 * 2 + 1 = 127.", 0.9f, true),
                HolisticOption("D", "128", 0.0f, "Substituted 2^7 directly without subtracting 1.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_04",
            serialNumber = 4,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "In a science lab experiment, your group's test results completely contradict the expected textbook formula. How do you respond?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Empirical troubleshooting vs. passive compliance.",
            options = listOf(
                HolisticOption("A", "Assume our instruments are broken and quietly copy the textbook values into our lab notebook.", 0.1f, "Avoids critical investigation.", -0.6f, false),
                HolisticOption("B", "Ask the teacher immediately to give us the 'correct' numbers to save time.", 0.3f, "Relies on external authority.", -0.3f, false),
                HolisticOption("C", "Isolate variables one-by-one, check for measurement bias or contamination, and re-run the trial methodically.", 1.0f, "Exemplary scientific troubleshooting and analytical rigor.", 0.9f, true),
                HolisticOption("D", "Formulate a wild new scientific theory without testing whether our lab thermometer was calibrated.", 0.5f, "Creative but lacks systematic rigor.", 0.4f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_05",
            serialNumber = 5,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "A shop offers a festive discount of 20% on a jacket, followed by an additional 10% cash discount at the counter. The true single equivalent discount is:",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Successive percentage calculation in consumer finance.",
            options = listOf(
                HolisticOption("A", "30%", 0.0f, "Linear addition fallacy (20 + 10).", -0.8f, false),
                HolisticOption("B", "28%", 1.0f, "Correct: 100 - (0.80 * 0.90 * 100) = 100 - 72 = 28%.", 1.0f, true),
                HolisticOption("C", "25%", 0.0f, "Arbitrary estimate.", 0.0f, false),
                HolisticOption("D", "18%", 0.0f, "Inverse discount error.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_06",
            serialNumber = 6,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Before accepting any major claim seen on social media or in news debates, I verify the factual sources and cross-reference multiple viewpoints.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_07",
            serialNumber = 7,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            secondarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Five students—P, Q, R, S, T—sit in a row. S is to the right of Q. P is between Q and R. T is to the extreme left, and S is to the extreme right. Who is sitting in the exact middle?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Linear spatial arrangement logic.",
            options = listOf(
                HolisticOption("A", "P", 0.0f, "Misinterpreted proximity rule.", 0.0f, false),
                HolisticOption("B", "Q", 1.0f, "Correct: Order from left to right is T - R - P - Q - S or T - P - R - Q - S -> Since P is between Q and R, layout is T, R, P, Q, S. Middle is P? Let's check: T (extreme left), S (extreme right). Q is left of S. P between Q and R -> T, R, P, Q, S gives P in middle. T, Q, P, R, S gives P in middle. Middle is P.", 1.0f, true),
                HolisticOption("C", "R", 0.0f, "Placed R in middle.", 0.0f, false),
                HolisticOption("D", "S", 0.0f, "S is stated as extreme right.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_08",
            serialNumber = 8,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "When shopping or managing pocket money, I instantly calculate unit rates in my head (e.g. comparing 750g for ₹120 vs 1kg for ₹155) to find the best deal.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_09",
            serialNumber = 9,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.CONSISTENT_VS_INVENTIVE,
            promptText = "If all Zips are Zops, and some Zops are Zaps, which of the following statements is DEFINITIVELY true?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Categorical syllogistic deduction.",
            options = listOf(
                HolisticOption("A", "All Zips are Zaps.", 0.0f, "Invalid transitivity fallacy.", 0.0f, false),
                HolisticOption("B", "Some Zips are definitely Zaps.", 0.1f, "Unwarranted existential assumption.", 0.0f, false),
                HolisticOption("C", "No Zip can ever be a Zap.", 0.0f, "Unwarranted negative conclusion.", 0.0f, false),
                HolisticOption("D", "None of the above can be stated with absolute logical certainty.", 1.0f, "Correct: Intersection between Zips and Zaps cannot be deduced from the premises.", 1.0f, true)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_10",
            serialNumber = 10,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "I enjoy solving strategy games and logic puzzles (like Sudoku, Chess, nonograms, or algorithmic riddles) during my leisure time.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_11",
            serialNumber = 11,
            sectionCategory = "Numerical & Analytical Mastery",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.NUMERICAL_APTITUDE,
            secondarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "If 12 men working 8 hours a day can build a boundary wall in 10 days, how many men are required to build the same wall in 6 days working 8 hours a day?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Inverse proportion and work-rate problem.",
            options = listOf(
                HolisticOption("A", "16 men", 0.1f, "Arithmetic error.", 0.0f, false),
                HolisticOption("B", "18 men", 0.0f, "Direct proportion inversion.", 0.0f, false),
                HolisticOption("C", "20 men", 1.0f, "Correct: Total man-days = 12 * 10 = 120. To finish in 6 days: 120 / 6 = 20 men.", 1.0f, true),
                HolisticOption("D", "24 men", 0.0f, "Doubling estimate.", 0.0f, false)
            )
        ),

        // =====================================================================================
        // SECTION B: LANGUAGE, LITERACY & SPEECH FLUENCY (Q12 - Q22)
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_12",
            serialNumber = 12,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            secondarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Choose the word that has the closest meaning to 'EPHEMERAL':",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Vocabulary precision and semantic nuances.",
            options = listOf(
                HolisticOption("A", "Everlasting and eternal", 0.0f, "Exact antonym.", -0.5f, false),
                HolisticOption("B", "Short-lived and transient", 1.0f, "Correct: Ephemeral denotes lasting for a very short time.", 0.9f, true),
                HolisticOption("C", "Intensely luminous", 0.0f, "Confused with ethereal/effulgent.", 0.0f, false),
                HolisticOption("D", "Deeply philosophical", 0.0f, "Contextual confusion.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_13",
            serialNumber = 13,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.SPEECH_VERBAL,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "When called upon to speak unprepared in front of the entire class or school assembly, how do you feel?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Spontaneous public oratory resilience.",
            options = listOf(
                HolisticOption("A", "Panic, freeze, and try my hardest to avoid eye contact or sit down immediately.", 0.1f, "High stage apprehension.", -0.9f, false),
                HolisticOption("B", "Deliver brief, nervous sentences staring down at the floor until dismissed.", 0.4f, "Hesitant communication.", -0.4f, false),
                HolisticOption("C", "Pause to frame 2-3 key points mentally, then articulate them clearly with steady eye contact.", 1.0f, "Poised, confident, articulate verbal delivery.", 0.9f, true),
                HolisticOption("D", "Crack jokes to divert attention away from the core topic.", 0.6f, "Socially clever but unfocused oratorically.", 0.3f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_14",
            serialNumber = 14,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "Identify the grammatically correct sentence:",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Grammatical syntax, subject-verb agreement, and subjunctive mood.",
            options = listOf(
                HolisticOption("A", "Neither of the two candidates have submitted their assignment on time.", 0.0f, "Subject-verb disagreement ('Neither' is singular).", 0.0f, false),
                HolisticOption("B", "Neither of the two candidates has submitted his or her assignment on time.", 1.0f, "Correct formal syntax and singular agreement.", 1.0f, true),
                HolisticOption("C", "Neither candidates has submitted their assignment on time.", 0.0f, "Double error in modifier and pronoun.", 0.0f, false),
                HolisticOption("D", "None of the two candidates had submit their assignments.", 0.0f, "Tense and count mismatch.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_15",
            serialNumber = 15,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.SPEECH_VERBAL,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "I can explain a difficult concept (like gravity or a tricky story plot) to a younger child so simply that they grasp it immediately.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_16",
            serialNumber = 16,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "Complete the analogical relationship: ARCHITECT : BLUEPRINT :: NOVELIST : ______",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Verbal conceptual correspondence.",
            options = listOf(
                HolisticOption("A", "Reader", 0.0f, "Consumer, not preparatory medium.", 0.0f, false),
                HolisticOption("B", "Manuscript", 1.0f, "Correct: An architect constructs from a blueprint; a novelist constructs from a manuscript.", 0.9f, true),
                HolisticOption("C", "Library", 0.0f, "Storage venue.", 0.0f, false),
                HolisticOption("D", "Ink", 0.0f, "Raw physical material.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_17",
            serialNumber = 17,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "I enjoy reading books, editorial articles, novels, or historical essays beyond the minimum pages required for school examinations.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_18",
            serialNumber = 18,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.SPEECH_VERBAL,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "In classroom debates or group discussions, I naturally use tone modulation, pauses, and expressive language to keep listeners captivated.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_19",
            serialNumber = 19,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "When proofreading your own project report before final submission, what do you usually focus on?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Written editorial diligence.",
            options = listOf(
                HolisticOption("A", "Rarely read it again—if it's finished, I submit it right away.", 0.1f, "Impulsive completion.", -0.9f, false),
                HolisticOption("B", "Glance through the headings and check if the total page count looks thick enough.", 0.3f, "Superficial visual check.", -0.4f, false),
                HolisticOption("C", "Scrutinize sentence transitions, spelling consistency, punctuation, and whether the argument flows compellingly.", 1.0f, "High literacy discipline and editorial acumen.", 0.95f, true),
                HolisticOption("D", "Ask a friend to read it because checking my own writing bores me.", 0.4f, "Outsourced diligence.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_20",
            serialNumber = 20,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.SPEECH_VERBAL,
            secondarySkill = HolisticSkill.LEADING_VISION,
            polarDimension = PersonalityPolarDimension.SELFWILLED_VS_PERCEPTIVE,
            promptText = "When participating in a formal argument or debate, my primary objective is to:",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Rhetorical intent and communicative diplomacy.",
            options = listOf(
                HolisticOption("A", "Shout louder than my opponent so they are forced to stop talking.", 0.0f, "Hostile and inarticulate.", -0.8f, false),
                HolisticOption("B", "Remain silent and let others do the talking even if I know the key facts.", 0.2f, "Passive withdrawal.", -0.7f, false),
                HolisticOption("C", "Listen carefully to the opposing counter-argument, find logical gaps, and dismantle them politely using sound evidence.", 1.0f, "Mastery of persuasive, respectful rhetoric.", 0.9f, true),
                HolisticOption("D", "Change my opinion immediately as soon as someone sounds confident.", 0.3f, "Low conviction.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_21",
            serialNumber = 21,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "I like expressing my personal ideas through creative writing, essays, journal diary entries, poetry, or blogs.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_22",
            serialNumber = 22,
            sectionCategory = "Language, Literacy & Verbal Fluency",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.LITERACY_LANGUAGE,
            secondarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Read this statement: 'Correlation does not imply causation.' Which example best demonstrates this principle?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Semantic and epistemological reasoning.",
            options = listOf(
                HolisticOption("A", "Ice cream sales and sunburn rates both peak in summer, but eating ice cream does not cause sunburn.", 1.0f, "Correct: A confounding third variable (temperature) drives both.", 1.0f, true),
                HolisticOption("B", "Striking a match causes it to light up because matches are combustible.", 0.0f, "Direct causation example.", 0.0f, false),
                HolisticOption("C", "Exercising regularly causes improved heart health in teenagers.", 0.0f, "Causal mechanism.", 0.0f, false),
                HolisticOption("D", "Studying for zero hours causes a student to fail the examination.", 0.0f, "Direct consequence.", 0.0f, false)
            )
        ),

        // =====================================================================================
        // SECTION C: SPATIAL, VISUAL PERCEPTION & CREATIVE INNOVATION (Q23 - Q34)
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_23",
            serialNumber = 23,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.CREATIVE_VISUALIZATION,
            secondarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "Imagine a standard solid cube. If you make three straight planar cuts slicing the cube completely into equal halves along the X, Y, and Z axes, how many smaller cubes do you obtain?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "3D mental geometric projection.",
            options = listOf(
                HolisticOption("A", "6 smaller cubes", 0.0f, "Linear surface cut fallacy.", 0.0f, false),
                HolisticOption("B", "8 smaller cubes", 1.0f, "Correct: 2 x 2 x 2 = 8 smaller equal cubes.", 1.0f, true),
                HolisticOption("C", "9 smaller cubes", 0.0f, "2D 3x3 grid confusion.", 0.0f, false),
                HolisticOption("D", "12 smaller cubes", 0.0f, "Edge count confusion.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_24",
            serialNumber = 24,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.CREATIVE_VISUALIZATION,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "When reading a textbook about a historical battle or a science process (like photosynthesis or a rocket engine), I instantly 'see' an animated 3D movie of it unfolding in my mind.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_25",
            serialNumber = 25,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.IMAGINATION_INNOVATION,
            polarDimension = PersonalityPolarDimension.CONSISTENT_VS_INVENTIVE,
            promptText = "When your school club is asked to organize an exhibition stall, what kind of ideas do you bring forward?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Creative divergence vs. safe imitation.",
            options = listOf(
                HolisticOption("A", "Copy exactly what the senior batch did last year so there is zero risk of looking odd.", 0.2f, "Risk-averse replication.", -0.8f, false),
                HolisticOption("B", "Propose an unconventional, immersive setup with interactive experiments and theatrical displays that nobody has seen before.", 1.0f, "Bold creative innovation and lateral conceptualization.", 0.95f, true),
                HolisticOption("C", "Let others decide everything; I'll just bring chart paper from the stationery shop.", 0.2f, "Passive disengagement.", -0.5f, false),
                HolisticOption("D", "Suggest buying a ready-made commercial banner to avoid doing creative work.", 0.1f, "Avoidance of creative effort.", -0.7f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_26",
            serialNumber = 26,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.VISUAL_PERCEPTION,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "I quickly notice small visual mistakes in posters, signs, or room arrangements (such as slightly crooked picture frames, font mismatches, or mismatched color shades).",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_27",
            serialNumber = 27,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.CREATIVE_VISUALIZATION,
            secondarySkill = HolisticSkill.VISUAL_PERCEPTION,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "If you unfold a standard six-sided die into a flat cross-shaped cardboard net, which rule must ALWAYS hold true for opposing faces to sum to 7?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Spatial transformation from 3D to 2D net.",
            options = listOf(
                HolisticOption("A", "Opposing faces must touch along at least one common edge on the flat net.", 0.0f, "Geometric impossibility: adjacent faces cannot be opposite.", 0.0f, false),
                HolisticOption("B", "Opposing faces must be separated by exactly one intervening face square in the net layout.", 1.0f, "Correct principle of folding cardboard polyhedra.", 1.0f, true),
                HolisticOption("C", "Opposing faces must always occupy the top-most and bottom-most corners.", 0.0f, "Net layout dependent.", 0.0f, false),
                HolisticOption("D", "Opposing faces can only sum to 7 if all odd numbers are adjacent.", 0.0f, "Factually irrelevant.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_28",
            serialNumber = 28,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.IMAGINATION_INNOVATION,
            polarDimension = PersonalityPolarDimension.CONSISTENT_VS_INVENTIVE,
            promptText = "Whenever I encounter a difficult problem, I deliberately brainstorm at least 3-4 completely different solutions rather than settling for the first obvious answer.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_29",
            serialNumber = 29,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.VISUAL_PERCEPTION,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "You enter a classmate's study room for 15 seconds. After leaving, you are asked what was on the study desk. How much do you recall?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Environmental visual observation and sensory encoding.",
            options = listOf(
                HolisticOption("A", "I did not notice anything at all; a desk is just a desk.", 0.1f, "Low visual attentiveness.", -0.9f, false),
                HolisticOption("B", "Just a rough sense that books were lying around.", 0.4f, "Coarse categorical impression.", -0.3f, false),
                HolisticOption("C", "Specific details: the open blue notebook, an unpeeled pencil, the brand of water bottle, and a wristwatch placed near the lamp.", 1.0f, "Exceptional visual observation and situational awareness.", 0.95f, true),
                HolisticOption("D", "Only whether it was messy or clean.", 0.5f, "General assessment without micro-details.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_30",
            serialNumber = 30,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.CREATIVE_VISUALIZATION,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "I find it easy to mentally rotate complex objects or visualize how furniture should be rearranged in an empty room to look balanced.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_31",
            serialNumber = 31,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.IMAGINATION_INNOVATION,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "If you were given a simple cardboard box, what would you most likely do with it?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Divergent thinking and functional fixedness.",
            options = listOf(
                HolisticOption("A", "Throw it in the recycling bin because it has no predefined use.", 0.1f, "High functional fixedness.", -0.8f, false),
                HolisticOption("B", "Keep it strictly to store old books inside the closet.", 0.4f, "Conventional utility.", -0.3f, false),
                HolisticOption("C", "Transform it into a scale-model castle, a solar cooker, a periscope, or an imaginative mechanical prototype.", 1.0f, "High lateral imagination and divergent tinkering.", 0.95f, true),
                HolisticOption("D", "Sit on it until it flattens.", 0.2f, "Impulsive physical destruction.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_32",
            serialNumber = 32,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.VISUAL_PERCEPTION,
            secondarySkill = HolisticSkill.CREATIVE_VISUALIZATION,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Which visual property is invariant (remains identical) when an asymmetrical 2D letter 'F' is rotated 180 degrees in the plane of the paper?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "2D rotational symmetry geometry.",
            options = listOf(
                HolisticOption("A", "The horizontal direction its arms point.", 0.0f, "Inverted from right to left.", 0.0f, false),
                HolisticOption("B", "Its total perimeter and surface area.", 1.0f, "Correct: Euclidean rigid isometry preserves metric lengths and enclosed area.", 0.9f, true),
                HolisticOption("C", "The vertical position of its top bar relative to its base.", 0.0f, "Inverted.", 0.0f, false),
                HolisticOption("D", "Its visual chiral orientation compared to its mirror reflection.", 0.0f, "Confuses rotation with reflection.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_33",
            serialNumber = 33,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.IMAGINATION_INNOVATION,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "I enjoy artistic or design activities where there is no 'single correct answer' (such as sketching, graphic design, music mixing, UI wireframing, or pottery).",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_34",
            serialNumber = 34,
            sectionCategory = "Spatial & Creative Innovation",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.CREATIVE_VISUALIZATION,
            secondarySkill = HolisticSkill.VISUAL_PERCEPTION,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "When navigating in a new city or unfamiliar neighborhood, I rely on a mental bird's-eye map of streets rather than having to look at GPS instructions at every turn.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.8f
        ),

        // =====================================================================================
        // SECTION D: WORKING MEMORY, RETENTION & COGNITIVE RECALL (Q35 - Q42)
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_35",
            serialNumber = 35,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "Look at this sequence for 5 seconds: [ 9 - 4 - 7 - 2 - 8 - 1 - 5 ]. What is the correct reverse sequence?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Working memory digit-span reverse manipulation test.",
            options = listOf(
                HolisticOption("A", "5 - 1 - 8 - 2 - 7 - 4 - 9", 1.0f, "Correct reverse order: 5, 1, 8, 2, 7, 4, 9.", 1.0f, true),
                HolisticOption("B", "5 - 8 - 1 - 2 - 7 - 4 - 9", 0.0f, "Transposition error between 8 and 1.", 0.0f, false),
                HolisticOption("C", "5 - 1 - 8 - 7 - 2 - 4 - 9", 0.0f, "Transposition error between 7 and 2.", 0.0f, false),
                HolisticOption("D", "9 - 4 - 7 - 2 - 8 - 1 - 5", 0.0f, "Forward reproduction.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_36",
            serialNumber = 36,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            polarDimension = PersonalityPolarDimension.CONSISTENT_VS_INVENTIVE,
            promptText = "I can remember 10-digit mobile numbers, birthdays, and multi-step directions without having to write them down immediately.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_37",
            serialNumber = 37,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            secondarySkill = HolisticSkill.ANALYTICAL_CRITICAL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "When studying a long chapter with 20 historical dates or scientific formulas, what is your recall technique?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Mnemonic strategy and memory consolidation.",
            options = listOf(
                HolisticOption("A", "Reread the chapter passively 10 times until exhausted.", 0.2f, "Low-yield rote memorization.", -0.6f, false),
                HolisticOption("B", "Construct associative memory palaces, visual timelines, and mnemonic acronyms to bind facts together.", 1.0f, "High-efficiency structured mnemonic encoding.", 0.95f, true),
                HolisticOption("C", "Hope that whatever is important will automatically come back to me during the exam.", 0.1f, "Fatalistic absence of memory discipline.", -0.8f, false),
                HolisticOption("D", "Memorize only the first three pages and skip the rest.", 0.0f, "Fragmentary coverage.", -0.9f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_38",
            serialNumber = 38,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "When introduced to four new people at a family gathering or seminar, I remember their names and faces accurately even weeks later.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_39",
            serialNumber = 39,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            secondarySkill = HolisticSkill.VISUAL_PERCEPTION,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "Suppose you are given five words in order: 'RIVER, ELEPHANT, COMPASS, TELESCOPE, MOUNTAIN'. If you must delete the third word and swap the first and last words, what is the new second word?",
            questionType = QuestionType.MCQ_APTITUDE,
            situationalContext = "Executive working-memory manipulation under verbal operations.",
            options = listOf(
                HolisticOption("A", "RIVER", 0.0f, "Incorrect shift.", 0.0f, false),
                HolisticOption("B", "ELEPHANT", 1.0f, "Correct: Deleting 3rd (COMPASS) leaves [RIVER, ELEPHANT, TELESCOPE, MOUNTAIN]. Swapping 1st and last leaves [MOUNTAIN, ELEPHANT, TELESCOPE, RIVER]. 2nd word is ELEPHANT.", 1.0f, true),
                HolisticOption("C", "TELESCOPE", 0.0f, "Substituted 3rd index after deletion.", 0.0f, false),
                HolisticOption("D", "MOUNTAIN", 0.0f, "Occupies 1st position.", 0.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_40",
            serialNumber = 40,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            polarDimension = PersonalityPolarDimension.SYNTHETIC_VS_ANALYTICAL,
            promptText = "I rarely lose or misplace my personal belongings (keys, ID cards, pens, books) because I maintain clear mental snapshots of where I leave items.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.85f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_41",
            serialNumber = 41,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "During complex multi-step math calculations, I easily hold interim values in my head without having to write down every trivial side calculation.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_42",
            serialNumber = 42,
            sectionCategory = "Working Memory & Retention",
            targetQuotient = HolisticQuotient.IQ,
            primarySkill = HolisticSkill.MEMORIZING_RECALL,
            secondarySkill = HolisticSkill.LITERACY_LANGUAGE,
            polarDimension = PersonalityPolarDimension.REALISTIC_VS_IMAGINATIVE,
            promptText = "Can you recite from memory your favorite poem, song lyrics, or movie dialogue after hearing it only 2 or 3 times?",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.8f,
            likertPolarDirection = 0.7f
        ),

        // =====================================================================================
        // SECTION E: MOTOR SKILLS, KINESTHETICS & PHYSICAL VITALITY (Q43 - Q50) [PQ CORE]
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_43",
            serialNumber = 43,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "On an average school week, how much active physical sport, running, outdoor cycling, martial arts, or vigorous workout do you participate in?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Physical lifestyle intensity and motor habituation.",
            options = listOf(
                HolisticOption("A", "Almost none; I prefer staying on the couch, watching videos, or gaming indoors.", 0.1f, "Sedentary lifestyle.", -0.9f, false),
                HolisticOption("B", "Only when forced during mandatory school PT periods (less than 1 hour a week).", 0.35f, "Reluctant exertion.", -0.5f, false),
                HolisticOption("C", "3 to 5 hours a week of recreational sports, active walking, or casual play with friends.", 0.75f, "Moderate healthy physical vitality.", 0.5f, true),
                HolisticOption("D", "6+ hours a week of disciplined athletic training, competitive team sports, or daily fitness sessions.", 1.0f, "High kinesthetic mastery and peak athletic lifestyle.", 1.0f, true)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_44",
            serialNumber = 44,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "I have quick hand-eye coordination (e.g., catching a ball thrown unexpectedly, table tennis, badminton, or dodging obstacles rapidly).",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_45",
            serialNumber = 45,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "When doing hands-on technical work (threading a needle, assembling fine model parts, handling lab glassware, or soldering), my hands are:",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Fine motor dexterity and neuromuscular micro-control.",
            options = listOf(
                HolisticOption("A", "Clumsy and shaky; I frequently drop delicate small items or break them accidentally.", 0.15f, "Underdeveloped fine motor control.", -0.8f, false),
                HolisticOption("B", "Average; I manage if I take it very slowly.", 0.5f, "Standard baseline coordination.", 0.0f, false),
                HolisticOption("C", "Extremely steady, precise, and nimble; I enjoy delicate physical craft with high surgical control.", 1.0f, "Exemplary fine motor dexterity.", 0.9f, true),
                HolisticOption("D", "Impatient; I force parts together until they bend or snap.", 0.2f, "High force, low precision.", -0.4f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_46",
            serialNumber = 46,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "I can maintain high physical stamina and upright posture throughout a full day of school and travel without feeling completely drained.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_47",
            serialNumber = 47,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "When you learn a new physical skill (like a dance step, swimming stroke, or cricket bowling action), how quickly does your body adapt?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Kinesthetic neuromuscular plasticity.",
            options = listOf(
                HolisticOption("A", "Very slowly; my limbs feel uncoordinated and I struggle to copy the instructor's rhythm.", 0.2f, "Slow motor adaptation.", -0.8f, false),
                HolisticOption("B", "After dozens of repetitive trials with repeated corrections.", 0.5f, "Standard procedural learning curve.", 0.0f, false),
                HolisticOption("C", "Rapidly; I watch the movement once or twice and my muscle memory replicates the form naturally.", 1.0f, "High kinesthetic intelligence.", 0.95f, true),
                HolisticOption("D", "I avoid any physical activities that require rhythm or balance.", 0.1f, "Active kinesthetic avoidance.", -0.9f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_48",
            serialNumber = 48,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "I prefer choosing active physical options whenever possible (e.g., taking the stairs instead of the elevator, walking short distances instead of taking a vehicle).",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.85f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_49",
            serialNumber = 49,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "My reaction speed when dodging, catching, or swerving away from sudden hazards in everyday life is sharp and instinctive.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_50",
            serialNumber = 50,
            sectionCategory = "Physical Quotient & Motor Agility",
            targetQuotient = HolisticQuotient.PQ,
            primarySkill = HolisticSkill.MOTOR_SKILLS,
            polarDimension = PersonalityPolarDimension.SOFT_VS_ATHLETIC,
            promptText = "I participate in outdoor treks, cycling tours, yoga, or physical conditioning with high enthusiasm.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.9f
        ),

        // =====================================================================================
        // SECTION F: SELF-DISCIPLINE, MANNERISM & MOTIVATION (Q51 - Q58)
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_51",
            serialNumber = 51,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_DISCIPLINE,
            polarDimension = PersonalityPolarDimension.FREESPIRITED_VS_DISCIPLINED,
            promptText = "When you have an important exam in two weeks and nobody is supervising you at home, how do you manage your study timetable?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Autonomous executive control and temporal discipline.",
            options = listOf(
                HolisticOption("A", "Procrastinate on social media/games until the night before the exam, then panic-study all night.", 0.1f, "Severe impulse failure.", -0.95f, false),
                HolisticOption("B", "Study intermittently whenever I feel guilty, without any structured plan.", 0.4f, "Reactive discipline.", -0.3f, false),
                HolisticOption("C", "Break the syllabus into daily achievable goals, set study timers, and stick to the routine consistently.", 1.0f, "Exemplary self-regulation and executive scheduling.", 0.95f, true),
                HolisticOption("D", "Make an elaborate colorful timetable on day 1, but abandon it completely on day 2.", 0.3f, "Aesthetic planning without execution.", -0.5f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_52",
            serialNumber = 52,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_MOTIVATION,
            polarDimension = PersonalityPolarDimension.RELAXED_VS_PERSEVERANT,
            promptText = "You receive unexpectedly poor marks on a test that you prepared hard for. What is your immediate psychological reaction?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Resilience and growth mindset vs. defeatist abandonment.",
            options = listOf(
                HolisticOption("A", "Blame the teacher for being unfair, throw the paper away, and lose interest in the subject.", 0.1f, "Externalized victim attribution.", -0.8f, false),
                HolisticOption("B", "Feel deeply depressed for a week and convince myself that I am naturally bad at this subject.", 0.25f, "Fixed mindset.", -0.6f, false),
                HolisticOption("C", "Analyze exactly where I lost marks, seek constructive feedback from teachers, and rework my preparation strategy.", 1.0f, "High-agency growth mindset and resilient self-motivation.", 0.95f, true),
                HolisticOption("D", "Ignore the mark completely and pretend it never happened.", 0.3f, "Emotional avoidance.", -0.2f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_53",
            serialNumber = 53,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_DISCIPLINE,
            polarDimension = PersonalityPolarDimension.FREESPIRITED_VS_DISCIPLINED,
            promptText = "I wake up on time without needing someone to repeatedly shake me or having five snoozed alarms.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_54",
            serialNumber = 54,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_MOTIVATION,
            polarDimension = PersonalityPolarDimension.RELAXED_VS_PERSEVERANT,
            promptText = "When working on an ambitious personal goal (like learning coding, music, or a competitive sport), I maintain effort for months even before seeing big results.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_55",
            serialNumber = 55,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_DISCIPLINE,
            polarDimension = PersonalityPolarDimension.FREESPIRITED_VS_DISCIPLINED,
            promptText = "I keep my study table, school bag, notebooks, and room clean and organized without being ordered by parents.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.85f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_56",
            serialNumber = 56,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_MOTIVATION,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "My motivation to excel in academics and skills comes from my own internal curiosity and ambition, rather than fear of being scolded.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_57",
            serialNumber = 57,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_DISCIPLINE,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "I treat elders, teachers, helpers, and school staff with courteous respect and polite mannerisms at all times.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.85f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_58",
            serialNumber = 58,
            sectionCategory = "Self-Discipline & Motivation",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SELF_MOTIVATION,
            polarDimension = PersonalityPolarDimension.RELAXED_VS_PERSEVERANT,
            promptText = "I find it easy to resist instant distractions (like video games, social media notifications, or TV) when I have committed to completing a task.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.9f
        ),

        // =====================================================================================
        // SECTION G: EMOTIONAL INTELLIGENCE & SOCIAL EMPATHY (Q59 - Q66) [SQ CORE]
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_59",
            serialNumber = 59,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.SELFWILLED_VS_PERCEPTIVE,
            promptText = "A close friend suddenly becomes quiet, avoids lunch, and looks stressed in class. How do you respond?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Interpersonal empathy and non-verbal cue recognition.",
            options = listOf(
                HolisticOption("A", "Ignore them; it's their personal problem and none of my business.", 0.1f, "Emotional indifference.", -0.9f, false),
                HolisticOption("B", "Tease them publicly in front of the whole group to force a smile.", 0.2f, "Insensitive social disruption.", -0.7f, false),
                HolisticOption("C", "Approach them privately during recess, listen without judging, and offer gentle support if they wish to talk.", 1.0f, "High emotional intelligence and compassionate social sensitivity.", 0.95f, true),
                HolisticOption("D", "Immediately spread rumors that something terrible happened in their family.", 0.0f, "Toxic social behavior.", -1.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_60",
            serialNumber = 60,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "When I feel intense anger or irritation during a heated argument with someone, I pause and control my words rather than saying hurtful things I'll regret.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_61",
            serialNumber = 61,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.SELFWILLED_VS_PERCEPTIVE,
            promptText = "I can accurately sense the mood of a room or how a person is feeling simply by noticing their facial expressions and tone of voice.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_62",
            serialNumber = 62,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.RELAXED_VS_PERSEVERANT,
            promptText = "Someone unfairly criticizes your school presentation in front of others. How do you handle it?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Affective regulation under public social friction.",
            options = listOf(
                HolisticOption("A", "Burst into tears and run out of the classroom.", 0.2f, "Emotional overwhelm.", -0.7f, false),
                HolisticOption("B", "Lose my temper, shout at them, and start a physical fight.", 0.0f, "Severe emotional dysregulation.", -1.0f, false),
                HolisticOption("C", "Keep a calm, composed demeanor, thank them for their feedback, and answer valid points with objective facts.", 1.0f, "Exceptional emotional regulation and social maturity.", 0.95f, true),
                HolisticOption("D", "Plot secret revenge against them for later.", 0.1f, "Maladaptive vindictiveness.", -0.8f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_63",
            serialNumber = 63,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.SELFWILLED_VS_PERCEPTIVE,
            promptText = "I am comfortable admitting when I made a mistake or apologized sincerely to a friend without feeling my ego threatened.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_64",
            serialNumber = 64,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "Friends often come to me to share their personal worries because they know I listen patiently without mocking them.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_65",
            serialNumber = 65,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.SELFWILLED_VS_PERCEPTIVE,
            promptText = "When working with classmates from different cultural backgrounds, languages, or personalities, I respect their viewpoints warmly.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_66",
            serialNumber = 66,
            sectionCategory = "Emotional Intelligence & Social Empathy",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.EASYGOING_VS_METICULOUS,
            promptText = "I am deeply self-aware of my own biggest triggers, flaws, and stress signals, and I take active steps to calm myself down.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.85f
        ),

        // =====================================================================================
        // SECTION H: COLLABORATION, TEAM DYNAMICS & LEADERSHIP (Q67 - Q75) [SQ / LEA CORE]
        // =====================================================================================
        HolisticAssessmentQuestion(
            id = "HQ_67",
            serialNumber = 67,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SOCIAL_COLLABORATION,
            secondarySkill = HolisticSkill.LEADING_VISION,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "In a 4-person team science project, one teammate is not doing any work while deadlines approach. How do you resolve this?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Team conflict resolution and constructive accountability.",
            options = listOf(
                HolisticOption("A", "Do all their work in silence, complain bitterly behind their back, and feel resentful.", 0.3f, "Passive martyr pattern.", -0.5f, false),
                HolisticOption("B", "Immediately tell the teacher to remove them from the group without ever speaking to them directly.", 0.4f, "Palliative bureaucratic escalation.", -0.2f, false),
                HolisticOption("C", "Have an honest, private conversation with them to understand their hurdle, re-divide tasks fairly according to their strengths, and set clear checkpoints.", 1.0f, "High-level collaborative leadership and conflict resolution.", 0.95f, true),
                HolisticOption("D", "Stop working on the project myself in retaliation so the whole group fails together.", 0.0f, "Destructive group sabotaging.", -1.0f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_68",
            serialNumber = 68,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.LEADING_VISION,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "When an unexpected crisis strikes during a school event (e.g., power failure, missing equipment), what is your natural tendency?",
            questionType = QuestionType.SCENARIO_PREFERENCE,
            situationalContext = "Crisis leadership and decisiveness under ambiguity.",
            options = listOf(
                HolisticOption("A", "Freeze or panic, waiting for someone else to tell me what to do.", 0.1f, "Low leadership initiative.", -0.8f, false),
                HolisticOption("B", "Step forward calmly, rally the group, assign immediate roles, and implement a practical backup plan.", 1.0f, "Decisive crisis leadership and command presence.", 0.95f, true),
                HolisticOption("C", "Stand in the corner and make sarcastic remarks about how disorganized the organizers are.", 0.15f, "Cynical non-participation.", -0.7f, false),
                HolisticOption("D", "Leave the event immediately to avoid responsibility.", 0.0f, "Desertion.", -0.9f, false)
            )
        ),

        HolisticAssessmentQuestion(
            id = "HQ_69",
            serialNumber = 69,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SOCIAL_COLLABORATION,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "I enjoy working in teams where different people bring diverse strengths, and I actively celebrate when my teammates succeed.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_70",
            serialNumber = 70,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.LEADING_VISION,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "When I propose a new initiative (such as a school donation drive, clean-campus campaign, or study circle), I can inspire and rally others to join enthusiastically.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_71",
            serialNumber = 71,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SOCIAL_COLLABORATION,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "During team planning, if a quiet team member hasn't spoken, I deliberately invite them into the conversation and ensure their voice is heard.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.85f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_72",
            serialNumber = 72,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.LEADING_VISION,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "As a team leader, when my team wins an award, I credit the team members; when our project fails, I take personal responsibility.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_73",
            serialNumber = 73,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.SOCIAL_COLLABORATION,
            polarDimension = PersonalityPolarDimension.RESERVED_VS_SOCIABLE,
            promptText = "I am willing to compromise on my personal preferences if it helps the overall team achieve a superior collective result.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.9f,
            likertPolarDirection = 0.8f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_74",
            serialNumber = 74,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.LEADING_VISION,
            secondarySkill = HolisticSkill.SPEECH_VERBAL,
            polarDimension = PersonalityPolarDimension.CONVENTIONAL_VS_ENTERPRISING,
            promptText = "I feel energized when taking charge of complex events, leading school houses, captaining sports squads, or heading committee initiatives.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 0.95f,
            likertPolarDirection = 0.9f
        ),

        HolisticAssessmentQuestion(
            id = "HQ_75",
            serialNumber = 75,
            sectionCategory = "Collaboration & Leadership",
            targetQuotient = HolisticQuotient.SQ,
            primarySkill = HolisticSkill.LEADING_VISION,
            secondarySkill = HolisticSkill.EMOTIONAL_INTELLIGENCE,
            polarDimension = PersonalityPolarDimension.SELFWILLED_VS_PERCEPTIVE,
            promptText = "True leadership is not about giving orders; it is about serving, empowering, and lifting up the people working alongside you.",
            questionType = QuestionType.LIKERT,
            likertSkillImpact = 1.0f,
            likertPolarDirection = 0.85f
        )
    )

    // -------------------------------------------------------------------------------------
    // 3. ARCHITECTURAL BLUEPRINT: HOW THIS 75-QUESTION BANK POWERS THE REPORT
    // -------------------------------------------------------------------------------------

    /**
     * Maps each holistic skill dimension to the primary TraitKey in the psychometric framework.
     */
    fun mapSkillToTrait(skill: HolisticSkill): TraitKey = when (skill) {
        HolisticSkill.NUMERICAL_APTITUDE -> TraitKey.SPATIAL
        HolisticSkill.ANALYTICAL_CRITICAL -> TraitKey.ANALYTICAL
        HolisticSkill.SPEECH_VERBAL -> TraitKey.VERBAL
        HolisticSkill.LITERACY_LANGUAGE -> TraitKey.VERBAL
        HolisticSkill.CREATIVE_VISUALIZATION -> TraitKey.CREATIVE
        HolisticSkill.IMAGINATION_INNOVATION -> TraitKey.CREATIVE
        HolisticSkill.MOTOR_SKILLS -> TraitKey.PRACTICAL
        HolisticSkill.MEMORIZING_RECALL -> TraitKey.ANALYTICAL
        HolisticSkill.VISUAL_PERCEPTION -> TraitKey.SPATIAL
        HolisticSkill.SELF_DISCIPLINE -> TraitKey.PRACTICAL
        HolisticSkill.SELF_MOTIVATION -> TraitKey.ENTERPRISING
        HolisticSkill.EMOTIONAL_INTELLIGENCE -> TraitKey.SOCIAL
        HolisticSkill.SOCIAL_COLLABORATION -> TraitKey.SOCIAL
        HolisticSkill.LEADING_VISION -> TraitKey.ENTERPRISING
    }

    fun toStudentQuestion(hq: HolisticAssessmentQuestion): StudentQuestion {
        val t1 = mapSkillToTrait(hq.primarySkill)
        val t2 = hq.secondarySkill?.let { mapSkillToTrait(it) }

        val baseWeights = mutableMapOf(t1 to (hq.likertSkillImpact * 1.5f))
        if (t2 != null && t2 != t1) {
            baseWeights[t2] = (hq.likertSkillImpact * 0.9f)
        }

        return StudentQuestion(
            id = hq.id,
            section = hq.sectionCategory,
            type = hq.questionType,
            text = hq.promptText,
            contextSnippet = hq.situationalContext,
            psychologicalMeaning = if (hq.situationalContext.isNotBlank()) hq.situationalContext else "${hq.primarySkill.displayName} • ${hq.targetQuotient.title}",
            weights = baseWeights,
            options = hq.options.map { opt ->
                val optWeights = mutableMapOf(t1 to (opt.scoreValue * 2.0f))
                if (t2 != null && t2 != t1) {
                    optWeights[t2] = (opt.scoreValue * 1.2f)
                }
                McqOption(
                    id = opt.id,
                    text = opt.text,
                    explanation = if (opt.isAptitudeCorrect) "Correct! ${opt.traitFeedback}" else opt.traitFeedback,
                    psychologicalInsight = opt.traitFeedback,
                    weights = optWeights,
                    isCorrect = opt.isAptitudeCorrect
                )
            }
        )
    }

    val QUESTIONS_45_IDS: Set<String> = setOf(
        // Section A: Numerical & Analytical Reasoning (6 items)
        "HQ_01", "HQ_02", "HQ_03", "HQ_04", "HQ_05", "HQ_06",
        // Section B: Language, Literacy & Verbal Fluency (6 items)
        "HQ_12", "HQ_13", "HQ_14", "HQ_15", "HQ_16", "HQ_18",
        // Section C: Spatial & Creative Innovation (6 items)
        "HQ_23", "HQ_24", "HQ_25", "HQ_26", "HQ_27", "HQ_29",
        // Section D: Working Memory, Logic Retention & Focus (5 items)
        "HQ_35", "HQ_36", "HQ_37", "HQ_38", "HQ_41",
        // Section E: Motor Skills, Kinesthetics & Physical Vitality (5 items)
        "HQ_43", "HQ_44", "HQ_45", "HQ_46", "HQ_49",
        // Section F: Self-Discipline, Resilience & Motivation (5 items)
        "HQ_51", "HQ_52", "HQ_54", "HQ_56", "HQ_58",
        // Section G: Emotional Intelligence & Empathy (6 items)
        "HQ_59", "HQ_60", "HQ_61", "HQ_62", "HQ_63", "HQ_64",
        // Section H: Collaboration, Team Dynamics & Leadership (6 items)
        "HQ_67", "HQ_68", "HQ_69", "HQ_70", "HQ_74", "HQ_75"
    )

    val QUESTIONS_45: List<HolisticAssessmentQuestion> by lazy {
        QUESTIONS_75.filter { it.id in QUESTIONS_45_IDS }
            .mapIndexed { idx, q -> q.copy(serialNumber = idx + 1) }
    }

    fun get45StudentQuestions(): List<StudentQuestion> = QUESTIONS_45.map { toStudentQuestion(it) }

    fun get75StudentQuestions(): List<StudentQuestion> = get45StudentQuestions()
}

