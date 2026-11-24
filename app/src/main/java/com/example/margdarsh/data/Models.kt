package com.example.margdarsh.data

enum class TraitKey(val label: String) {
    ANALYTICAL("Analytical Thinking"),
    SPATIAL("Spatial & Numeric Reasoning"),
    VERBAL("Verbal Aptitude"),
    CREATIVE("Creative Expression"),
    SOCIAL("Social & Empathic Insight"),
    PRACTICAL("Practical Execution"),
    ENTERPRISING("Enterprising Drive")
}

enum class Stream(val displayName: String, val subtitle: String) {
    SCIENCE("Science", "Physics, Chemistry, Maths / Biology"),
    COMMERCE("Commerce", "Accountancy, Business Studies, Economics"),
    HUMANITIES("Humanities", "History, Political Science, Psychology, Literature")
}

enum class CareerTag(val displayName: String) {
    HIGH_GROWTH("High Growth"),
    EMERGING("Emerging"),
    STABLE("Stable"),
    UNCOMMON("Uncommon"),
    PREMIUM_ELITE("Elite / Tier-1")
}

enum class MobilityRank(val level: Int, val label: String) {
    HOME(0, "Home City"),
    STATE(1, "Within State"),
    INDIA(2, "Anywhere in India"),
    ABROAD(3, "India or Abroad")
}

enum class QuestionType {
    LIKERT,
    MCQ_APTITUDE,
    SCENARIO_PREFERENCE
}

data class McqOption(
    val id: String,
    val text: String,
    val explanation: String = "",
    val psychologicalInsight: String = "",
    val weights: Map<TraitKey, Float> = emptyMap(),
    val isCorrect: Boolean = false
)

data class StudentQuestion(
    val id: String,
    val section: String,
    val type: QuestionType,
    val text: String,
    val contextSnippet: String = "",
    val psychologicalMeaning: String = "",
    val weights: Map<TraitKey, Float> = emptyMap(), // For Likert
    val options: List<McqOption> = emptyList() // For MCQ & Scenario
)

data class StudentProfile(
    val studentName: String = "",
    val parentName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val studentClass: String = "Class 10",
    val schoolName: String = "",
    val city: String = "",
    val isGoogleVerified: Boolean = false
)

data class ParentOption(
    val value: String,
    val label: String
)

data class ParentChoiceQuestion(
    val id: String,
    val section: String,
    val text: String,
    val options: List<ParentOption>
)

data class Career(
    val id: String,
    val title: String,
    val stream: Stream,
    val tag: CareerTag,
    val traits: List<TraitKey>,
    val salaryEntry: String,
    val salaryExperienced: String,
    val salaryLabel: String,
    val workType: String,
    val costMin: Int, // in Lakhs
    val costMax: Int, // in Lakhs
    val costLabel: String,
    val scope: String,
    val mobility: MobilityRank,
    val growthRate: String,
    val topRecruiters: String,
    val description: String,
    val benefits: String,
    val roadmap: String = ""
)

data class ScoredCareer(
    val career: Career,
    val fit: Int,
    val affordable: Boolean,
    val reachable: Boolean,
    val isLockedTopMatch: Boolean = false
)

data class StreamResult(
    val stream: Stream,
    val scores: Map<Stream, Float>,
    val rationale: String
)

val LIKERT_LABELS = listOf(
    "Disagree",
    "Somewhat disagree",
    "Neutral",
    "Somewhat agree",
    "Agree"
)

val STUDENT_QUESTIONS: List<StudentQuestion> = PremiumHolisticAssessmentBank.get45StudentQuestions()

private val LEGACY_STUDENT_QUESTIONS: List<StudentQuestion> = listOf(
    // --- SECTION 1: LOGICAL, QUANTITATIVE & ANALYTICAL APTITUDE MCQs ---
    StudentQuestion(
        id = "mcq1",
        section = "Quantitative & Rate Logic",
        type = QuestionType.MCQ_APTITUDE,
        text = "A train running at 72 km/h crosses a 200m long platform in 20 seconds. What is the length of the train?",
        contextSnippet = "Speed = 72 km/h = 20 m/s. Distance = Speed × Time.",
        psychologicalMeaning = "Evaluates working memory, mental mathematical agility, and ability to decompose multi-variable speed-time-distance rate mechanics without feeling overwhelmed.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "180 meters", 
                explanation = "Incorrect (calculated partial distance)", 
                psychologicalInsight = "Shows hasty approximation without accounting for the combined train and platform length.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.4f)
            ),
            McqOption(
                id = "b", 
                text = "200 meters", 
                explanation = "Correct! (72 km/h = 20 m/s; 20m/s × 20s = 400m total; 400m - 200m platform = 200m train)", 
                psychologicalInsight = "Demonstrates precise algebraic clarity, structured multi-step deduction, and strong quantitative execution.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.5f, TraitKey.SPATIAL to 1.0f), 
                isCorrect = true
            ),
            McqOption(
                id = "c", 
                text = "240 meters", 
                explanation = "Incorrect conversion multiplier applied", 
                psychologicalInsight = "Indicates intuition-led guessing when faced with unit conversion friction.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.2f)
            ),
            McqOption(
                id = "d", 
                text = "300 meters", 
                explanation = "Incorrect arithmetic deduction", 
                psychologicalInsight = "Points to difficulty maintaining parallel parameters in working memory under test conditions.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.0f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq2",
        section = "Pattern & Spatial Sequence",
        type = QuestionType.MCQ_APTITUDE,
        text = "Find the missing number in the sequence: 3, 7, 15, 31, 63, ?",
        contextSnippet = "Look for geometric doubling (×2 + 1) or step power addition (+4, +8, +16, +32).",
        psychologicalMeaning = "Measures abstract pattern recognition, inductive mathematical reasoning, and non-verbal IQ capacity.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "95", 
                explanation = "Incorrect constant increment", 
                psychologicalInsight = "Assumes linear progression rather than recognizing exponential acceleration.",
                weights = mapOf(TraitKey.SPATIAL to 0.2f)
            ),
            McqOption(
                id = "b", 
                text = "112", 
                explanation = "Incorrect step interval calculation", 
                psychologicalInsight = "Notices growth but miscalculates the power of two multiplier.",
                weights = mapOf(TraitKey.SPATIAL to 0.3f)
            ),
            McqOption(
                id = "c", 
                text = "127", 
                explanation = "Correct! Each step is (2n + 1) or +4, +8, +16, +32, +64 -> 63 + 64 = 127", 
                psychologicalInsight = "Exhibits superior non-linear pattern deduction and instant mathematical schema extrapolation.",
                weights = mapOf(TraitKey.SPATIAL to 1.5f, TraitKey.ANALYTICAL to 1.2f), 
                isCorrect = true
            ),
            McqOption(
                id = "d", 
                text = "128", 
                explanation = "Close, missed the minus 1 offset in 2^(k)-1", 
                psychologicalInsight = "Recognizes power of two (2^7 = 128) but skips the structural baseline subtraction.",
                weights = mapOf(TraitKey.SPATIAL to 0.5f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq3",
        section = "Deductive & Critical Syllogisms",
        type = QuestionType.MCQ_APTITUDE,
        text = "Statement: 'All successful entrepreneurs are risk-takers. Some risk-takers are innovators.' Which of the following MUST be logically true?",
        contextSnippet = "Formal Venn-diagram syllogistic logic.",
        psychologicalMeaning = "Measures verbal-logical rigor, critical skepticism, and resistance to making unwarranted emotional assumptions.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "All innovators are successful entrepreneurs", 
                explanation = "False generalization", 
                psychologicalInsight = "Tends toward optimistic over-generalization beyond empirical premise facts.",
                weights = mapOf(TraitKey.VERBAL to 0.1f)
            ),
            McqOption(
                id = "b", 
                text = "No entrepreneur can fail without taking risks", 
                explanation = "Unwarranted negative converse", 
                psychologicalInsight = "Confuses the definition of risk with absolute causation.",
                weights = mapOf(TraitKey.VERBAL to 0.3f)
            ),
            McqOption(
                id = "c", 
                text = "Some people who take risks are entrepreneurs", 
                explanation = "Correct! If all A are B, then some B must be A.", 
                psychologicalInsight = "Possesses airtight deductive discipline, essential for legal, diagnostic, and systems analysis.",
                weights = mapOf(TraitKey.VERBAL to 1.5f, TraitKey.ANALYTICAL to 1.2f), 
                isCorrect = true
            ),
            McqOption(
                id = "d", 
                text = "All entrepreneurs are innovators", 
                explanation = "Logical leap without direct premise link", 
                psychologicalInsight = "Relies on colloquial associations rather than strict formal boundaries.",
                weights = mapOf(TraitKey.VERBAL to 0.2f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq4",
        section = "Commercial & Financial Logic",
        type = QuestionType.MCQ_APTITUDE,
        text = "A store buys an item for ₹800 and lists it at a 25% markup. During a festive sale, they offer a 10% discount on the marked price. What is the net profit percentage?",
        contextSnippet = "Cost Price = 800. Marked Price = 1000. Sale Price = 900. Profit = 100 on 800.",
        psychologicalMeaning = "Evaluates practical commercial acumen, commercial margin intuition, and multi-layered percentage calculations.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "10%", 
                explanation = "Subtracted 25% - 10% incorrectly", 
                psychologicalInsight = "Prone to simplified arithmetic subtraction instead of compounding base price shifts.",
                weights = mapOf(TraitKey.ENTERPRISING to 0.4f)
            ),
            McqOption(
                id = "b", 
                text = "12.5%", 
                explanation = "Correct! CP=₹800, MP=₹1000, SP=₹900. Profit = ₹100 / ₹800 = 12.5%", 
                psychologicalInsight = "Shows strong financial calculation instincts, pricing savvy, and practical economic modeling.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.5f, TraitKey.SPATIAL to 1.2f), 
                isCorrect = true
            ),
            McqOption(
                id = "c", 
                text = "15%", 
                explanation = "Direct subtraction error", 
                psychologicalInsight = "Miscalculates base cost denominator under timed testing.",
                weights = mapOf(TraitKey.ENTERPRISING to 0.3f)
            ),
            McqOption(
                id = "d", 
                text = "17.5%", 
                explanation = "Incorrect profit calculation", 
                psychologicalInsight = "Struggles with compounding margins in business scenarios.",
                weights = mapOf(TraitKey.ENTERPRISING to 0.1f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq5",
        section = "Algorithmic & Systems Logic",
        type = QuestionType.MCQ_APTITUDE,
        text = "An automated sorting robotic arm can process 60 packages in 4 minutes. If two such robotic arms work together, how many minutes will it take to process 300 packages?",
        contextSnippet = "1 arm = 15 pkgs/min. 2 arms = 30 pkgs/min. 300 / 30 = 10 mins.",
        psychologicalMeaning = "Evaluates parallel computing intuition, systems throughput estimation, and resource allocation logic.",
        options = listOf(
            McqOption(
                id = "a",
                text = "8 minutes",
                explanation = "Incorrect speed scaling",
                psychologicalInsight = "Underestimates total capacity requirements in parallel workflows.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.3f)
            ),
            McqOption(
                id = "b",
                text = "10 minutes",
                explanation = "Correct! Combined rate = 30 pkgs/min. 300 ÷ 30 = 10 minutes.",
                psychologicalInsight = "Reflects effortless computational thinking, pipeline scaling, and engineering efficiency.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.5f, TraitKey.PRACTICAL to 1.2f),
                isCorrect = true
            ),
            McqOption(
                id = "c",
                text = "12 minutes",
                explanation = "Arithmetic scaling miscalculation",
                psychologicalInsight = "Slightly overestimates bottleneck latency in multi-agent workflows.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.4f)
            ),
            McqOption(
                id = "d",
                text = "15 minutes",
                explanation = "Calculated time for a single arm without parallel acceleration",
                psychologicalInsight = "Forgot to divide by the dual-worker multiplier.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.2f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq6",
        section = "Scientific Hypothesis & Experimental Logic",
        type = QuestionType.MCQ_APTITUDE,
        text = "A scientist hypothesizes that a new natural fertilizer doubles crop yield. To scientifically prove this beyond doubt, what MUST the control group have?",
        contextSnippet = "Scientific methodology and isolated control variables.",
        psychologicalMeaning = "Measures empirical mindset, experimental rigor, and understanding of causality vs correlation.",
        options = listOf(
            McqOption(
                id = "a",
                text = "Different soil, extra sunlight, and no fertilizer",
                explanation = "Invalid: Introduces confounding variables",
                psychologicalInsight = "Conflates multiple variables, missing the core concept of isolated control.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.2f)
            ),
            McqOption(
                id = "b",
                text = "Identical soil, sunlight, water, and climate, but with standard/no fertilizer",
                explanation = "Correct! Isolates the fertilizer as the single independent variable.",
                psychologicalInsight = "Possesses deep scientific inquiry rigor, isolating cause-and-effect with high empirical integrity.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.5f, TraitKey.PRACTICAL to 1.1f),
                isCorrect = true
            ),
            McqOption(
                id = "c",
                text = "Double the water and half the sunlight",
                explanation = "Invalid test setup",
                psychologicalInsight = "Lacks clarity on baseline scientific experimental protocols.",
                weights = mapOf(TraitKey.ANALYTICAL to 0.1f)
            ),
            McqOption(
                id = "d",
                text = "A different crop species entirely to compare biodiversity",
                explanation = "Irrelevant cross-species comparison",
                psychologicalInsight = "Shifts focus to divergent curiosity rather than rigorous controlled validation.",
                weights = mapOf(TraitKey.CREATIVE to 0.6f, TraitKey.ANALYTICAL to 0.2f)
            )
        )
    ),

    // --- SECTION 2: SITUATIONAL & BEHAVIORAL SCENARIOS ---
    StudentQuestion(
        id = "mcq7",
        section = "Situational Problem Solving",
        type = QuestionType.SCENARIO_PREFERENCE,
        text = "Your school team is given 48 hours to create a project for a prestigious national innovation expo. What role do you instinctively take?",
        psychologicalMeaning = "Pinpoints student's natural team role archetype, intrinsic motivation center, and pressure-response default.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "Architect the technical code, system circuit, or mathematical proof.", 
                psychologicalInsight = "Natural Deep Specialist & Technical Builder; derives confidence from core domain mastery and logic.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.5f, TraitKey.SPATIAL to 1.2f)
            ),
            McqOption(
                id = "b", 
                text = "Lead the pitch presentation, manage sponsor relations, and define the financial viability.", 
                psychologicalInsight = "Charismatic Enterprising Catalyst; energized by persuasion, high-stakes communication, and commercial growth.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.5f, TraitKey.VERBAL to 1.0f)
            ),
            McqOption(
                id = "c", 
                text = "Design the visual UI/UX branding, aesthetic posters, and storytelling narrative.", 
                psychologicalInsight = "Creative Visionary & Human-Centric Stylist; interprets the world through aesthetics, meaning, and user experience.",
                weights = mapOf(TraitKey.CREATIVE to 1.5f, TraitKey.SPATIAL to 0.8f)
            ),
            McqOption(
                id = "d", 
                text = "Interview users/citizens to understand their pain points and ensure the solution is ethical and accessible.", 
                psychologicalInsight = "Empathetic Humanist & Social Ethicist; champions user advocacy, societal impact, and emotional well-being.",
                weights = mapOf(TraitKey.SOCIAL to 1.5f, TraitKey.VERBAL to 1.0f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq8",
        section = "Technology & Ethics Dilemma",
        type = QuestionType.SCENARIO_PREFERENCE,
        text = "When reading news about a breakthrough in Generative AI or genetic editing, what question crosses your mind first?",
        psychologicalMeaning = "Identifies student's intellectual lens: mechanistic (how it works), commercial (how to scale), societal (who it affects), or creative (what can be created).",
        options = listOf(
            McqOption(
                id = "a", 
                text = "How does the algorithm or molecular mechanism work under the hood?", 
                psychologicalInsight = "Inquisitive First-Principles Investigator; wants to open the black box and understand underlying science.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.4f, TraitKey.PRACTICAL to 1.0f)
            ),
            McqOption(
                id = "b", 
                text = "How will startups monetize this and what market disruption will it cause?", 
                psychologicalInsight = "Strategic Commercial Visionary; looks ahead at market ecosystems, economic value capture, and business disruption.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.5f, TraitKey.ANALYTICAL to 0.7f)
            ),
            McqOption(
                id = "c", 
                text = "How will this impact ordinary citizens, inequality, and human rights?", 
                psychologicalInsight = "Societal Conscience & Policy Mind; instinctively protects vulnerable communities and weighs moral implications.",
                weights = mapOf(TraitKey.SOCIAL to 1.4f, TraitKey.VERBAL to 1.2f)
            ),
            McqOption(
                id = "d", 
                text = "How can this technology be applied to create novel art, cinema, or user interfaces?", 
                psychologicalInsight = "Avant-Garde Innovator; looks for creative crossovers, novel emotional expressions, and generative beauty.",
                weights = mapOf(TraitKey.CREATIVE to 1.5f, TraitKey.SPATIAL to 0.9f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq9",
        section = "Hands-On Execution & Troubleshooting",
        type = QuestionType.SCENARIO_PREFERENCE,
        text = "You are given a broken mechanical device or a bugged software program. How do you tackle it?",
        psychologicalMeaning = "Measures problem decomposition methodology: tactile deduction vs theoretical modeling vs interpersonal outsourcing vs creative re-engineering.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "Dismantle it piece by piece with screwdrivers/debuggers until I isolate the physical/software fault.", 
                psychologicalInsight = "Tactile Problem Solver; learns by doing, tactile exploration, and incremental troubleshooting.",
                weights = mapOf(TraitKey.PRACTICAL to 1.5f, TraitKey.SPATIAL to 1.0f)
            ),
            McqOption(
                id = "b", 
                text = "Study the theoretical schematic manual to understand the overarching math and circuit equations.", 
                psychologicalInsight = "Theoretical Abstract Thinker; needs complete conceptual clarity before touching execution.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.5f, TraitKey.VERBAL to 0.8f)
            ),
            McqOption(
                id = "c", 
                text = "Call the expert manufacturer and negotiate a replacement or warranty upgrade.", 
                psychologicalInsight = "Resourceful Pragmatist; leverages institutional channels, relationships, and efficiency over brute effort.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.2f, TraitKey.SOCIAL to 1.2f)
            ),
            McqOption(
                id = "d", 
                text = "Repurpose its parts into a completely new artistic sculpture or modified gadget.", 
                psychologicalInsight = "Divergent Alchemist; sees potential in discarded elements and transforms constraints into art.",
                weights = mapOf(TraitKey.CREATIVE to 1.5f, TraitKey.PRACTICAL to 0.8f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq10",
        section = "Debate & Persuasion Style",
        type = QuestionType.SCENARIO_PREFERENCE,
        text = "In a heated debate competition on a controversial public bill, what is your winning strategy?",
        psychologicalMeaning = "Reveals student's primary persuasion mechanism: empirical facts, empathetic emotion, economic logic, or paradigm-shifting philosophy.",
        options = listOf(
            McqOption(
                id = "a", 
                text = "Construct an airtight, fact-based legal argument citing empirical statistics and precedents.", 
                psychologicalInsight = "Rigor-Driven Advocate; excels in law, policy debate, forensic analysis, and data journalism.",
                weights = mapOf(TraitKey.VERBAL to 1.4f, TraitKey.ANALYTICAL to 1.2f)
            ),
            McqOption(
                id = "b", 
                text = "Use powerful emotive metaphors and human stories to connect deeply with the audience's empathy.", 
                psychologicalInsight = "Storyteller & Emotional Orator; influences crowds through deep emotional resonance and shared humanity.",
                weights = mapOf(TraitKey.SOCIAL to 1.4f, TraitKey.VERBAL to 1.2f)
            ),
            McqOption(
                id = "c", 
                text = "Analyze the financial and trade cost-benefit tradeoffs to show economic reality.", 
                psychologicalInsight = "Economic Strategist; grounds debates in fiscal realities, incentives, and return on investment.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.4f, TraitKey.ANALYTICAL to 1.0f)
            ),
            McqOption(
                id = "d", 
                text = "Present an unexpected out-of-the-box philosophical perspective that re-frames the entire issue.", 
                psychologicalInsight = "Philosophical Provocateur; challenges unspoken assumptions and introduces fresh cognitive frameworks.",
                weights = mapOf(TraitKey.CREATIVE to 1.4f, TraitKey.VERBAL to 1.1f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq11",
        section = "Crisis & Venture Pivot",
        type = QuestionType.SCENARIO_PREFERENCE,
        text = "Your student enterprise project loses its primary vendor 3 days before launching. What do you do?",
        psychologicalMeaning = "Measures composure under volatility, crisis leadership, and strategic flexibility.",
        options = listOf(
            McqOption(
                id = "a",
                text = "Stay up all night building a homegrown alternative using internal tools and raw grit.",
                psychologicalInsight = "Resilient Hardcore Builder; responds to external blockers with unstoppable work ethic and craft.",
                weights = mapOf(TraitKey.PRACTICAL to 1.4f, TraitKey.ANALYTICAL to 1.0f)
            ),
            McqOption(
                id = "b",
                text = "Negotiate emergency partnerships with competitors or backup suppliers immediately.",
                psychologicalInsight = "High-Stakes Dealmaker; thrives in high-stress relationship negotiation and commercial deal structuring.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.5f, TraitKey.SOCIAL to 1.0f)
            ),
            McqOption(
                id = "c",
                text = "Pivot the product offering into a lean digital service that bypasses physical vendors completely.",
                psychologicalInsight = "Agile Strategic Pivot Master; rapidly redesigns value chains to turn constraints into competitive advantages.",
                weights = mapOf(TraitKey.CREATIVE to 1.3f, TraitKey.ENTERPRISING to 1.3f)
            ),
            McqOption(
                id = "d",
                text = "Convene the team to de-escalate anxiety, ensure everyone feels heard, and vote on a collective solution.",
                psychologicalInsight = "Servant Leader & Culture Custodian; stabilizes team psychology and fosters collective resilience under fire.",
                weights = mapOf(TraitKey.SOCIAL to 1.5f, TraitKey.VERBAL to 1.0f)
            )
        )
    ),
    StudentQuestion(
        id = "mcq12",
        section = "Interpersonal Conflict & Mediation",
        type = QuestionType.SCENARIO_PREFERENCE,
        text = "Two key classmates on your research team have a sharp personal dispute and stop speaking to each other. How do you step in?",
        psychologicalMeaning = "Assesses conflict resolution style, emotional intelligence, and interpersonal mediation ability.",
        options = listOf(
            McqOption(
                id = "a",
                text = "Listen privately to both sides to understand their emotional pain points, then facilitate a quiet reconciliation.",
                psychologicalInsight = "Master Mediator & Empath; dissolves personal friction through deep active listening and emotional diplomacy.",
                weights = mapOf(TraitKey.SOCIAL to 1.6f, TraitKey.VERBAL to 1.1f)
            ),
            McqOption(
                id = "b",
                text = "Create an objective division of duties so they don't have to interact while still meeting deadlines.",
                psychologicalInsight = "Structural Systems Realist; solves interpersonal friction by architecting independent functional lanes.",
                weights = mapOf(TraitKey.ANALYTICAL to 1.2f, TraitKey.PRACTICAL to 1.2f)
            ),
            McqOption(
                id = "c",
                text = "Remind them firmly of the shared goal, grades, and reputation at stake to rally their professional commitment.",
                psychologicalInsight = "Outcome-Oriented Commander; aligns team members around high-stakes external goals and accountability.",
                weights = mapOf(TraitKey.ENTERPRISING to 1.4f, TraitKey.VERBAL to 1.0f)
            ),
            McqOption(
                id = "d",
                text = "Organize an informal fun outing or creative brainstorming game to break the ice naturally.",
                psychologicalInsight = "Atmospheric Harmonizer; creates psychological safety through shared levity, play, and organic connection.",
                weights = mapOf(TraitKey.CREATIVE to 1.2f, TraitKey.SOCIAL to 1.2f)
            )
        )
    ),

    // --- SECTION 3: PSYCHOMETRIC DEPTH & PERSONALITY INVENTORY (LIKERT SCALE) ---
    StudentQuestion(
        id = "s1",
        section = "Cognitive Processing Style",
        type = QuestionType.LIKERT,
        text = "I prefer solving problems that have a single clear mathematical answer over exploring multiple subjective interpretations.",
        psychologicalMeaning = "Measures preference for Convergent (exact, deterministic) vs Divergent (open-ended, exploratory) cognition.",
        weights = mapOf(TraitKey.ANALYTICAL to 1.2f, TraitKey.SPATIAL to 0.6f)
    ),
    StudentQuestion(
        id = "s2",
        section = "Cognitive Processing Style",
        type = QuestionType.LIKERT,
        text = "When I read a complex or dense text, I can quickly restate its overarching thesis in crisp, clear words of my own.",
        psychologicalMeaning = "Evaluates verbal synthesis, cognitive compression, and semantic clarity.",
        weights = mapOf(TraitKey.VERBAL to 1.2f, TraitKey.ANALYTICAL to 0.4f)
    ),
    StudentQuestion(
        id = "s3",
        section = "Cognitive Processing Style",
        type = QuestionType.LIKERT,
        text = "I enjoy breaking a complicated situation into smaller, sequential logical steps before taking any action.",
        psychologicalMeaning = "Assesses algorithmic problem decomposition and structured mental planning.",
        weights = mapOf(TraitKey.ANALYTICAL to 1.2f, TraitKey.PRACTICAL to 0.5f)
    ),
    StudentQuestion(
        id = "s4",
        section = "Spatial & Abstract Modeling",
        type = QuestionType.LIKERT,
        text = "I can effortlessly picture how a complex 3D object or geometric architecture would look if it were rotated or taken apart in space.",
        psychologicalMeaning = "Measures mental rotation, visual-spatial working memory, and architectural aptitude.",
        weights = mapOf(TraitKey.SPATIAL to 1.3f, TraitKey.CREATIVE to 0.4f)
    ),
    StudentQuestion(
        id = "s5",
        section = "Spatial & Abstract Modeling",
        type = QuestionType.LIKERT,
        text = "When looking at graphs, circuit diagrams, or topographic maps, I grasp the hidden trends faster than reading paragraphs.",
        psychologicalMeaning = "Evaluates non-verbal diagrammatic literacy and multi-dimensional trend extrapolation.",
        weights = mapOf(TraitKey.SPATIAL to 1.2f, TraitKey.ANALYTICAL to 0.7f)
    ),
    StudentQuestion(
        id = "s6",
        section = "Deep Work & Persistence",
        type = QuestionType.LIKERT,
        text = "I will keep working on a stubborn bug, math theorem, or design flaw until I conquer it, even without any teacher or parent watching.",
        psychologicalMeaning = "Measures intrinsic grit, intellectual endurance, and self-directed mastery drive.",
        weights = mapOf(TraitKey.PRACTICAL to 0.9f, TraitKey.ANALYTICAL to 0.9f)
    ),
    StudentQuestion(
        id = "s7",
        section = "Deep Work & Persistence",
        type = QuestionType.LIKERT,
        text = "I can maintain deep uninterrupted concentration on difficult technical tasks for hours without checking social media.",
        psychologicalMeaning = "Evaluates executive attention control, cognitive stamina, and flow-state accessibility.",
        weights = mapOf(TraitKey.PRACTICAL to 1.0f, TraitKey.ANALYTICAL to 0.7f)
    ),
    StudentQuestion(
        id = "s8",
        section = "Hands-On & Tactile Learning",
        type = QuestionType.LIKERT,
        text = "I learn concepts 5x faster when I can physically build, assemble, code, or experiment rather than just read theoretical textbooks.",
        psychologicalMeaning = "Measures kinesthetic learning index and practical execution preference over abstract theory.",
        weights = mapOf(TraitKey.PRACTICAL to 1.3f, TraitKey.SPATIAL to 0.5f)
    ),
    StudentQuestion(
        id = "s9",
        section = "Scientific & Quantitative Fascination",
        type = QuestionType.LIKERT,
        text = "Advanced mathematics, algorithms, chemistry formulas, or astrophysics feel like exciting puzzles I genuinely want to solve.",
        psychologicalMeaning = "Measures intrinsic STEM affinity and passion for formal quantitative sciences.",
        weights = mapOf(TraitKey.ANALYTICAL to 1.3f, TraitKey.SPATIAL to 0.8f)
    ),
    StudentQuestion(
        id = "s10",
        section = "Commercial & Enterprise Drive",
        type = QuestionType.LIKERT,
        text = "I actively follow news about venture capital, stock markets, startups, business models, or personal financial compounding.",
        psychologicalMeaning = "Measures commercial curiosity, business awareness, and market-driven enterprising instincts.",
        weights = mapOf(TraitKey.ENTERPRISING to 1.3f, TraitKey.ANALYTICAL to 0.5f)
    ),
    StudentQuestion(
        id = "s11",
        section = "Commercial & Enterprise Drive",
        type = QuestionType.LIKERT,
        text = "I am naturally comfortable pitching ideas, selling products, negotiating deals, and persuading skeptical stakeholders.",
        psychologicalMeaning = "Evaluates commercial persuasion, assertiveness, and entrepreneurial hustle.",
        weights = mapOf(TraitKey.ENTERPRISING to 1.3f, TraitKey.VERBAL to 0.8f)
    ),
    StudentQuestion(
        id = "s12",
        section = "Social & Civic Impact",
        type = QuestionType.LIKERT,
        text = "Constitutional law, international diplomacy, sociology, public healthcare, or human rights genuinely stir my passions.",
        psychologicalMeaning = "Measures civic consciousness, public policy interest, and humanitarian motivation.",
        weights = mapOf(TraitKey.VERBAL to 1.1f, TraitKey.SOCIAL to 1.1f)
    ),
    StudentQuestion(
        id = "s13",
        section = "Social & Civic Impact",
        type = QuestionType.LIKERT,
        text = "Classmates and friends regularly seek me out for thoughtful counsel, emotional comfort, and unbiased conflict resolution.",
        psychologicalMeaning = "Evaluates interpersonal trust, empathy index, and natural peer counselling capability.",
        weights = mapOf(TraitKey.SOCIAL to 1.4f)
    ),
    StudentQuestion(
        id = "s14",
        section = "Creative & Aesthetic Expression",
        type = QuestionType.LIKERT,
        text = "I spend a meaningful amount of leisure time writing stories, video editing, illustrating, sketching, or composing original music.",
        psychologicalMeaning = "Measures creative productivity index and artistic / narrative expression frequency.",
        weights = mapOf(TraitKey.CREATIVE to 1.4f, TraitKey.SPATIAL to 0.4f)
    ),
    StudentQuestion(
        id = "s15",
        section = "Creative & Aesthetic Expression",
        type = QuestionType.LIKERT,
        text = "I frequently synthesize unorthodox ideas, connecting two totally unrelated fields in ways that surprise teachers and peers.",
        psychologicalMeaning = "Evaluates lateral thinking capacity, conceptual cross-pollination, and innovation potential.",
        weights = mapOf(TraitKey.CREATIVE to 1.3f, TraitKey.ENTERPRISING to 0.6f)
    ),
    StudentQuestion(
        id = "s16",
        section = "Leadership & Initiative",
        type = QuestionType.LIKERT,
        text = "In group projects, I naturally take charge of the roadmap, assign responsibilities, and keep everyone accountable to milestones.",
        psychologicalMeaning = "Measures executive leadership drive, project coordination capability, and accountability comfort.",
        weights = mapOf(TraitKey.ENTERPRISING to 1.2f, TraitKey.SOCIAL to 0.8f)
    ),
    StudentQuestion(
        id = "s17",
        section = "Risk Tolerance & Resilience",
        type = QuestionType.LIKERT,
        text = "I am comfortable taking calculated risks and facing possible failure when the potential upside and learning are massive.",
        psychologicalMeaning = "Assesses risk appetite, asymmetric upside mindset, and psychological fear-of-failure resilience.",
        weights = mapOf(TraitKey.ENTERPRISING to 1.2f, TraitKey.PRACTICAL to 0.5f)
    ),
    StudentQuestion(
        id = "s18",
        section = "Work Environment & Sensory Temperament",
        type = QuestionType.LIKERT,
        text = "I feel most energized when working in quiet isolation with high-level data models, code, or instruments rather than managing crowds.",
        psychologicalMeaning = "Distinguishes Deep Specialist / Individual Contributor temperament from People-Facing / Extroverted roles.",
        weights = mapOf(TraitKey.ANALYTICAL to 1.0f, TraitKey.SPATIAL to 0.8f, TraitKey.PRACTICAL to 0.5f)
    ),
    StudentQuestion(
        id = "s19",
        section = "Work Environment & Sensory Temperament",
        type = QuestionType.LIKERT,
        text = "Giving speeches, participating in MUNs, staging plays, or hosting events gives me a tremendous surge of energy.",
        psychologicalMeaning = "Evaluates public performance comfort, charisma, and auditory-verbal extroversion.",
        weights = mapOf(TraitKey.VERBAL to 1.2f, TraitKey.ENTERPRISING to 1.0f, TraitKey.SOCIAL to 0.6f)
    ),
    StudentQuestion(
        id = "s20",
        section = "Detail Precision & Quality Auditing",
        type = QuestionType.LIKERT,
        text = "I instinctively spot subtle grammatical errors, numeric discrepancies, and micro-flaws that others routinely gloss over.",
        psychologicalMeaning = "Measures cognitive precision, forensic attention to detail, and quality assurance temperament.",
        weights = mapOf(TraitKey.ANALYTICAL to 1.1f, TraitKey.PRACTICAL to 0.7f)
    ),
    StudentQuestion(
        id = "s21",
        section = "Cognitive Agility & Quantitative Speed",
        type = QuestionType.LIKERT,
        text = "Word problems combining ratios, profit margins, growth rates, and geometry feel intuitive and enjoyable rather than confusing.",
        psychologicalMeaning = "Assesses intuitive quantitative reasoning and fluid mathematical confidence.",
        weights = mapOf(TraitKey.SPATIAL to 1.1f, TraitKey.ANALYTICAL to 0.9f)
    ),
    StudentQuestion(
        id = "s22",
        section = "Persuasive Written Rhetoric",
        type = QuestionType.LIKERT,
        text = "When I write an essay or report, I construct arguments with such logical precision that readers rarely find loopholes.",
        psychologicalMeaning = "Evaluates structured written rhetoric, argumentation rigor, and logical thesis building.",
        weights = mapOf(TraitKey.VERBAL to 1.3f, TraitKey.ANALYTICAL to 0.5f)
    ),
    StudentQuestion(
        id = "s23",
        section = "Social Intuition & Non-Verbal EQ",
        type = QuestionType.LIKERT,
        text = "I can immediately sense the subtle mood shifts, unspoken tensions, and unsaid emotions in a room or conversation.",
        psychologicalMeaning = "Measures social radar, non-verbal emotional intelligence (EQ), and interpersonal sensitivity.",
        weights = mapOf(TraitKey.SOCIAL to 1.4f, TraitKey.CREATIVE to 0.4f)
    ),
    StudentQuestion(
        id = "s24",
        section = "Long-Term Vision & Strategic Planning",
        type = QuestionType.LIKERT,
        text = "I frequently think 5 to 10 years ahead, mapping out multi-step educational, career, and life strategies rather than living day-to-day.",
        psychologicalMeaning = "Evaluates strategic foresight, delayed gratification capacity, and long-range career architecture mindset.",
        weights = mapOf(TraitKey.ENTERPRISING to 1.2f, TraitKey.ANALYTICAL to 0.8f)
    )
)

val PARENT_QUESTIONS: List<ParentChoiceQuestion> = listOf(
    ParentChoiceQuestion(
        id = "budget",
        section = "Financial Context",
        text = "What total budget can your family comfortably allocate for the child's entire higher education?",
        options = listOf(
            ParentOption("low", "Under ₹8 lakh"),
            ParentOption("mid", "₹8 lakh – ₹25 lakh"),
            ParentOption("high", "₹25 lakh – ₹60 lakh"),
            ParentOption("premium", "Above ₹60 lakh (Global/Elite Tier)")
        )
    ),
    ParentChoiceQuestion(
        id = "funding",
        section = "Financial Context",
        text = "How open is your family to education loans or merit scholarship-dependent routes?",
        options = listOf(
            ParentOption("none", "Prefer to self-fund completely (no debt)"),
            ParentOption("some", "Open to moderate education loan (₹10L - ₹25L)"),
            ParentOption("full", "Comfortable with high-leverage loans or international fellowships")
        )
    ),
    ParentChoiceQuestion(
        id = "relocation",
        section = "Mobility",
        text = "How far are you willing to let the child relocate for university & early career?",
        options = listOf(
            ParentOption("home", "Stay within our home city / metro"),
            ParentOption("state", "Any premier institution within our state"),
            ParentOption("india", "Any top tier college anywhere in India (IITs/NLUs/IIMs)"),
            ParentOption("abroad", "Open to India and top global universities abroad")
        )
    ),
    ParentChoiceQuestion(
        id = "region",
        section = "Mobility",
        text = "Which region is your family primarily based in?",
        options = listOf(
            ParentOption("north", "North India (Delhi NCR, Punjab, UP, Rajasthan, etc.)"),
            ParentOption("south", "South India (Bengaluru, Chennai, Hyderabad, Kerala)"),
            ParentOption("west", "West India (Mumbai, Pune, Gujarat)"),
            ParentOption("east", "East & North-East India (Kolkata, Assam, Odisha, Bihar)")
        )
    ),
    ParentChoiceQuestion(
        id = "calibre",
        section = "Academic Assessment",
        text = "How would you describe your child's natural academic exam performance & study stamina?",
        options = listOf(
            ParentOption("top", "Consistently top 5% of class, self-driven study habits"),
            ParentOption("above", "Above average (80-90%), excels when focused"),
            ParentOption("average", "Average (65-80%), requires structured mentoring & coaching"),
            ParentOption("struggling", "Struggles with rote exams, excels in hands-on / creative tasks")
        )
    ),
    ParentChoiceQuestion(
        id = "aspiration",
        section = "Family Aspirations",
        text = "Which long-term outcome is the highest priority for your family?",
        options = listOf(
            ParentOption("earning", "Maximum Earning Potential & Fast Wealth Creation"),
            ParentOption("stability", "Job Security, Brand Stability & Work-Life Balance"),
            ParentOption("impact", "High Social Respect, Governance & Institutional Power"),
            ParentOption("passion", "Personal Fulfillment & Creative Freedom")
        )
    ),
    ParentChoiceQuestion(
        id = "preferredStream",
        section = "Stream Leanings",
        text = "Does the family currently have a specific Class 11 stream in mind?",
        options = listOf(
            ParentOption("Science", "Science (PCM / PCB / PCMB)"),
            ParentOption("Commerce", "Commerce (With/Without Maths)"),
            ParentOption("Humanities", "Humanities & Social Sciences"),
            ParentOption("open", "Completely open to scientific assessment guidance")
        )
    ),
    ParentChoiceQuestion(
        id = "risk",
        section = "Risk Appetite",
        text = "How open are you to high-growth, modern emerging career tracks vs classical routes?",
        options = listOf(
            ParentOption("low", "Prefer classical proven routes (MBBS, CA, Govt/Civil Services)"),
            ParentOption("medium", "Open to modern tech/finance if college placement is strong"),
            ParentOption("high", "Very open to emerging AI, venture capital, design, or global tracks")
        )
    )
)

val CAREERS: List<Career> = listOf(
    // --- SCIENCE CAREERS ---
    Career(
        id = "ai-systems",
        title = "Artificial Intelligence & Large System Architecture",
        stream = Stream.SCIENCE,
        tag = CareerTag.PREMIUM_ELITE,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.SPATIAL),
        salaryEntry = "₹18 LPA – ₹32 LPA",
        salaryExperienced = "₹45 LPA – ₹90+ LPA",
        salaryLabel = "₹18L – ₹90L+ PA",
        workType = "Algorithmic Engineering, Neural Nets & Cloud Scale Infrastructure",
        costMin = 10,
        costMax = 25,
        costLabel = "₹10L – ₹25L (B.Tech IIT/NIT/BITS)",
        scope = "Global tech hubs (Bengaluru, SF, London, Singapore)",
        mobility = MobilityRank.ABROAD,
        growthRate = "+48% YoY High Demand",
        topRecruiters = "Google DeepMind, Microsoft, NVIDIA, OpenAI, Meta",
        description = "Designing distributed machine learning pipelines, LLM fine-tuning clusters, and autonomous software platforms powering next-generation global software.",
        benefits = "Exceptional salary progression, global visa sponsorships, remote work flexibility, and immense technical leverage."
    ),
    Career(
        id = "data-arch",
        title = "Full-Stack Data Engineering & Analytics",
        stream = Stream.SCIENCE,
        tag = CareerTag.HIGH_GROWTH,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.PRACTICAL),
        salaryEntry = "₹12 LPA – ₹20 LPA",
        salaryExperienced = "₹30 LPA – ₹65 LPA",
        salaryLabel = "₹12L – ₹65L PA",
        workType = "Data Warehousing, Pipeline Automation & Business Intelligence",
        costMin = 8,
        costMax = 18,
        costLabel = "₹8L – ₹18L",
        scope = "India tech corridors & Remote Global",
        mobility = MobilityRank.STATE,
        growthRate = "+34% YoY",
        topRecruiters = "Amazon, Flipkart, Snowflake, Uber, Databricks",
        description = "Transforming petabytes of raw behavioral data into real-time decision infrastructure for enterprise platforms and fintech apps.",
        benefits = "High entry compensation, rapid promotions into Data Leadership, and high demand across banking, retail, and tech."
    ),
    Career(
        id = "robotics",
        title = "Autonomous Robotics & Mechatronics Engineering",
        stream = Stream.SCIENCE,
        tag = CareerTag.EMERGING,
        traits = listOf(TraitKey.SPATIAL, TraitKey.PRACTICAL),
        salaryEntry = "₹10 LPA – ₹18 LPA",
        salaryExperienced = "₹28 LPA – ₹55 LPA",
        salaryLabel = "₹10L – ₹55L PA",
        workType = "Hardware-Software Co-Design, Embedded Firmware & Control Systems",
        costMin = 12,
        costMax = 25,
        costLabel = "₹12L – ₹25L",
        scope = "EV, Defense & Aerospace hubs (Pune, Chennai, Bengaluru)",
        mobility = MobilityRank.INDIA,
        growthRate = "+28% YoY",
        topRecruiters = "ISRO, DRDO, Tata Motors EV, Tesla, Boston Dynamics, GreyOrange",
        description = "Architecting robotic manipulators, autonomous drones, and smart EV powertrain electronics combining mechanical physics and real-time C++ code.",
        benefits = "Hands-on engineering craft, national PLI scheme expansion, and massive industrial patent upside."
    ),
    Career(
        id = "medicine",
        title = "Clinical Medicine & Super-Specialty Surgery (MBBS/MD)",
        stream = Stream.SCIENCE,
        tag = CareerTag.STABLE,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.SOCIAL),
        salaryEntry = "₹9 LPA – ₹16 LPA (Residency)",
        salaryExperienced = "₹35 LPA – ₹80+ LPA (Consultant)",
        salaryLabel = "₹9L – ₹80L+ PA",
        workType = "Clinical Patient Care, Diagnostic Medicine & Surgical Procedures",
        costMin = 8,
        costMax = 80,
        costLabel = "₹8L (Govt AIIMS/GMC) – ₹75L+ (Private)",
        scope = "Universal, all cities and international medical boards",
        mobility = MobilityRank.INDIA,
        growthRate = "Permanent Resilient Demand",
        topRecruiters = "Apollo Hospitals, Fortis, Max Healthcare, AIIMS, Private Practice",
        description = "The gold-standard clinical path via NEET into MBBS and MD/MS specializations (Cardiology, Neurosurgery, Radiology).",
        benefits = "Unmatched societal prestige, 100% lifelong job security, and complete professional autonomy with personal clinics."
    ),
    Career(
        id = "bioinfo",
        title = "Computational Genomics & Precision Medicine",
        stream = Stream.SCIENCE,
        tag = CareerTag.UNCOMMON,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.CREATIVE),
        salaryEntry = "₹11 LPA – ₹18 LPA",
        salaryExperienced = "₹26 LPA – ₹60 LPA",
        salaryLabel = "₹11L – ₹60L PA",
        workType = "DNA Sequencing Analysis, Drug Target Discovery & Molecular Modeling",
        costMin = 12,
        costMax = 30,
        costLabel = "₹12L – ₹30L",
        scope = "Pharma R&D, Genomic Labs (Hyderabad, Boston, Basel)",
        mobility = MobilityRank.ABROAD,
        growthRate = "+38% YoY",
        topRecruiters = "Biocon, Dr. Reddy's, Illumina, Pfizer, Novartis",
        description = "Intersecting molecular biology, statistical genetics, and AI models to engineer personalized cancer therapies and genetic vaccines.",
        benefits = "Global PhD and research mobility, high IP royalties, and solving humanity's hardest disease puzzles."
    ),
    Career(
        id = "clim",
        title = "Clean Energy & Battery Storage Infrastructure",
        stream = Stream.SCIENCE,
        tag = CareerTag.UNCOMMON,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.PRACTICAL),
        salaryEntry = "₹9 LPA – ₹15 LPA",
        salaryExperienced = "₹24 LPA – ₹50 LPA",
        salaryLabel = "₹9L – ₹50L PA",
        workType = "Grid Modernization, Solar/Wind Engineering & Chemical Cell Design",
        costMin = 10,
        costMax = 22,
        costLabel = "₹10L – ₹22L",
        scope = "Green Energy Corridors, Policy & Climate Funds",
        mobility = MobilityRank.INDIA,
        growthRate = "+35% YoY",
        topRecruiters = "Adani Green, Tata Power, ReNew Power, Tesla Energy, Enphase",
        description = "Developing multi-gigawatt renewable power grids, microgrid battery storage, and green hydrogen plants for India's 2070 Net-Zero plan.",
        benefits = "Vast green investment inflow, lower competition than traditional IT, and early managerial leadership."
    ),

    // --- COMMERCE CAREERS ---
    Career(
        id = "quant",
        title = "Quantitative Trading & Algorithmic Finance",
        stream = Stream.COMMERCE,
        tag = CareerTag.PREMIUM_ELITE,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.ENTERPRISING),
        salaryEntry = "₹25 LPA – ₹45 LPA",
        salaryExperienced = "₹60 LPA – ₹1.2 Cr+ PA",
        salaryLabel = "₹25L – ₹1.2 Cr+ PA",
        workType = "Stochastic Mathematical Modeling, HFT Strategies & Derivative Pricing",
        costMin = 12,
        costMax = 35,
        costLabel = "₹12L – ₹35L (Eco/Maths/B.Tech + MFE)",
        scope = "Financial Capitals (Mumbai, Singapore, London, New York)",
        mobility = MobilityRank.ABROAD,
        growthRate = "+42% YoY Top Tier Pay",
        topRecruiters = "Citadel, Jane Street, Tower Research, Goldman Sachs, DE Shaw",
        description = "Engineering ultra-low-latency statistical models and algorithmic arbitrage execution across global equity, commodity, and currency markets.",
        benefits = "Highest entry compensation across all undergraduate domains, performance bonuses reaching 100-300% of base salary."
    ),
    Career(
        id = "ib-pe",
        title = "Investment Banking & Private Equity",
        stream = Stream.COMMERCE,
        tag = CareerTag.HIGH_GROWTH,
        traits = listOf(TraitKey.ENTERPRISING, TraitKey.ANALYTICAL),
        salaryEntry = "₹16 LPA – ₹28 LPA",
        salaryExperienced = "₹45 LPA – ₹95 LPA",
        salaryLabel = "₹16L – ₹95L PA",
        workType = "M&A Deal Execution, LBO Financial Valuation & Capital Advisory",
        costMin = 10,
        costMax = 30,
        costLabel = "₹10L – ₹30L (SRCC/IIM/St. Stephen's)",
        scope = "Mumbai (BKC/Nariman Point), Delhi NCR, London",
        mobility = MobilityRank.INDIA,
        growthRate = "+25% YoY",
        topRecruiters = "Morgan Stanley, J.P. Morgan, Blackstone, KKR, Avendus Capital",
        description = "Advising Fortune 500 CEOs and unicorn founders on billion-dollar acquisitions, initial public offerings (IPOs), and leveraged buyouts.",
        benefits = "Steep wealth generation curve, high executive exposure, and seamless exit routes into C-Suite or Venture Funds."
    ),
    Career(
        id = "ca",
        title = "Chartered Accountancy & Corporate Tax Strategy",
        stream = Stream.COMMERCE,
        tag = CareerTag.STABLE,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.PRACTICAL),
        salaryEntry = "₹10 LPA – ₹16 LPA",
        salaryExperienced = "₹25 LPA – ₹60+ LPA",
        salaryLabel = "₹10L – ₹60L+ PA",
        workType = "Statutory Auditing, Forensic Accounting & International Tax Structuring",
        costMin = 2,
        costMax = 6,
        costLabel = "₹2L – ₹6L (ICAI Route)",
        scope = "Pan-India in every commercial hub & Big 4 Advisory",
        mobility = MobilityRank.HOME,
        growthRate = "+18% Stable Growth",
        topRecruiters = "Deloitte, PwC, EY, KPMG, BDO, Independent Audit Practice",
        description = "The premier professional qualification regulating corporate balance sheets, forensic investigation, and international transfer pricing.",
        benefits = "Lowest education cost to ROI ratio, flexibility to practice from home city, and legal signatory powers under the Companies Act."
    ),
    Career(
        id = "founder",
        title = "Startup Venture Building & Product Leadership",
        stream = Stream.COMMERCE,
        tag = CareerTag.EMERGING,
        traits = listOf(TraitKey.ENTERPRISING, TraitKey.CREATIVE),
        salaryEntry = "₹12 LPA – ₹22 LPA",
        salaryExperienced = "₹35 LPA – ₹80L+ / Equity Upside",
        salaryLabel = "₹12L – ₹80L+ PA",
        workType = "Product-Market Fit, Growth Loops & Tech-Commercial Strategy",
        costMin = 6,
        costMax = 25,
        costLabel = "₹6L – ₹25L",
        scope = "Startup hubs (Bengaluru, Gurugram, Mumbai)",
        mobility = MobilityRank.INDIA,
        growthRate = "+30% High Disruption",
        topRecruiters = "Zomato, Swiggy, Razorpay, CRED, Peak XV Backed Startups",
        description = "Driving high-velocity growth from 0 to 1 as an Associate Product Manager (APM) or founding venture operator with early equity stakes.",
        benefits = "Unlimited wealth creation via ESOPs, extreme autonomy, and transferable general-management capabilities."
    ),
    Career(
        id = "actuary",
        title = "Actuarial Science & Risk Mathematics",
        stream = Stream.COMMERCE,
        tag = CareerTag.UNCOMMON,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.SPATIAL),
        salaryEntry = "₹11 LPA – ₹18 LPA",
        salaryExperienced = "₹30 LPA – ₹70 LPA",
        salaryLabel = "₹11L – ₹70L PA",
        workType = "Statistical Risk Modeling, Longevity & Catastrophe Insurance Pricing",
        costMin = 4,
        costMax = 10,
        costLabel = "₹4L – ₹10L (IAI / IFoA exams)",
        scope = "Mumbai, Gurugram, Zurich, London Reinsurance",
        mobility = MobilityRank.HOME,
        growthRate = "+24% Niche Scarcity",
        topRecruiters = "Swiss Re, Munich Re, HDFC Life, LIC, Milliman, Aon",
        description = "Applying advanced probability to price mega-risks for global insurance, pension funds, and climate reinsurance balance sheets.",
        benefits = "Extremely small qualified talent pool (<1000 Fellows in India), guaranteed salary jumps with every exam passed."
    ),

    // --- HUMANITIES CAREERS ---
    Career(
        id = "corp-law",
        title = "Corporate Law, M&A & International Arbitration",
        stream = Stream.HUMANITIES,
        tag = CareerTag.PREMIUM_ELITE,
        traits = listOf(TraitKey.VERBAL, TraitKey.ANALYTICAL),
        salaryEntry = "₹16 LPA – ₹24 LPA",
        salaryExperienced = "₹45 LPA – ₹1 Cr+ PA (Partner)",
        salaryLabel = "₹16L – ₹1 Cr+ PA",
        workType = "Contract Negotiation, Cross-Border Litigation & Regulatory Advisory",
        costMin = 10,
        costMax = 25,
        costLabel = "₹10L – ₹25L (NLUs via CLAT)",
        scope = "Tier-1 Law Firms (Delhi, Mumbai, Bengaluru, London)",
        mobility = MobilityRank.INDIA,
        growthRate = "+26% YoY",
        topRecruiters = "Shardul Amarchand Mangaldas, Cyril Amarchand, AZB & Partners, Khaitan",
        description = "Drafting complex cross-border corporate mergers, IP licensing contracts, and representing global conglomerates in commercial arbitration tribunals.",
        benefits = "High societal status, premier entry packages comparable to top tech firms, and paths to Equity Partnership."
    ),
    Career(
        id = "civil-services",
        title = "Civil Services & Public Administration (IAS/IPS/IFS)",
        stream = Stream.HUMANITIES,
        tag = CareerTag.STABLE,
        traits = listOf(TraitKey.VERBAL, TraitKey.SOCIAL),
        salaryEntry = "₹10 LPA + Housing & Governance Perks",
        salaryExperienced = "₹22 LPA – ₹30 LPA (Cabinet Secretary Level)",
        salaryLabel = "₹10L – ₹30L PA + Perks",
        workType = "District Governance, Policy Formulation & National Security/Diplomacy",
        costMin = 4,
        costMax = 12,
        costLabel = "₹4L – ₹12L (UPSC Route)",
        scope = "Central & State Governments, District Magistrate Offices",
        mobility = MobilityRank.INDIA,
        growthRate = "Permanent Institutional Power",
        topRecruiters = "Government of India, State Civil Services, Ministry of External Affairs",
        description = "Direct administrative control over districts, formulating public welfare schemes, maintaining law & order, and representing India in foreign embassies.",
        benefits = "Pinnacle of executive authority, immense direct citizen impact, high societal respect, and lifelong security."
    ),
    Career(
        id = "ux-design",
        title = "Product Design & Spatial UX Strategy",
        stream = Stream.HUMANITIES,
        tag = CareerTag.HIGH_GROWTH,
        traits = listOf(TraitKey.CREATIVE, TraitKey.SPATIAL),
        salaryEntry = "₹12 LPA – ₹20 LPA",
        salaryExperienced = "₹32 LPA – ₹70 LPA",
        salaryLabel = "₹12L – ₹70L PA",
        workType = "Human-Centered Design, Interaction Flows & Design Systems",
        costMin = 8,
        costMax = 22,
        costLabel = "₹8L – ₹22L (NID / IIT IDC / UCEED)",
        scope = "Global Digital Product Teams, Remote & Design Agencies",
        mobility = MobilityRank.INDIA,
        growthRate = "+36% YoY High Demand",
        topRecruiters = "Apple, Figma, Airbnb, Swiggy, Microsoft, Spotify",
        description = "Crafting intuitive digital interfaces, mobile app ergonomics, and spatial design systems used by hundreds of millions of consumers.",
        benefits = "Merit-based portfolio hiring regardless of board percentages, lucrative freelance consulting, and high creative freedom."
    ),
    Career(
        id = "clinical-psych",
        title = "Clinical Neuropsychology & Behavioral Science",
        stream = Stream.HUMANITIES,
        tag = CareerTag.EMERGING,
        traits = listOf(TraitKey.SOCIAL, TraitKey.VERBAL),
        salaryEntry = "₹8 LPA – ₹14 LPA",
        salaryExperienced = "₹22 LPA – ₹50 LPA",
        salaryLabel = "₹8L – ₹50L PA",
        workType = "Cognitive Behavioral Therapy, Diagnostic Assessment & Mental Health",
        costMin = 6,
        costMax = 18,
        costLabel = "₹6L – ₹18L (BA/MA + M.Phil/PsyD RCI)",
        scope = "Hospitals, Corporate Wellness & Private Practice",
        mobility = MobilityRank.STATE,
        growthRate = "+40% Rapid Expansion",
        topRecruiters = "NIMHANS, Medanta, Corporate Tech Wellness Programs, Elite Clinics",
        description = "Diagnosing neurocognitive patterns, trauma intervention, and providing evidence-backed psychotherapy for individuals and corporate leadership.",
        benefits = "Rapidly expanding mental healthcare awareness in India, personal clinic consultation fees (₹2,000 – ₹5,000/hr), and immense humanitarian impact."
    ),
    Career(
        id = "policy-econ",
        title = "Development Economics & Global Public Policy",
        stream = Stream.HUMANITIES,
        tag = CareerTag.UNCOMMON,
        traits = listOf(TraitKey.VERBAL, TraitKey.ANALYTICAL),
        salaryEntry = "₹11 LPA – ₹18 LPA",
        salaryExperienced = "₹28 LPA – ₹65 LPA",
        salaryLabel = "₹11L – ₹65L PA",
        workType = "Econometric Evaluation, Think-Tank Policy Briefs & Impact Assessment",
        costMin = 8,
        costMax = 24,
        costLabel = "₹8L – ₹24L (DSE / Ashoka / J-PAL / Oxford)",
        scope = "Think Tanks (NITI Aayog, Brookings, World Bank, J-PAL)",
        mobility = MobilityRank.ABROAD,
        growthRate = "+28% YoY",
        topRecruiters = "World Bank, IMF, NITI Aayog, Gates Foundation, Dalberg Advisory",
        description = "Evaluating nationwide poverty alleviation programs, fiscal tax policies, and climate transition models for global governance organizations.",
        benefits = "International diplomatic mobility, advisory access to ministers and cabinet secretariats, and intellectually stimulating research."
    ),
    Career(
        id = "mgmt-consulting",
        title = "Management Consulting & Strategic Operations",
        stream = Stream.COMMERCE,
        tag = CareerTag.PREMIUM_ELITE,
        traits = listOf(TraitKey.ANALYTICAL, TraitKey.ENTERPRISING, TraitKey.VERBAL),
        salaryEntry = "₹22 LPA – ₹32 LPA",
        salaryExperienced = "₹50 LPA – ₹90 LPA",
        salaryLabel = "₹22L – ₹90L PA",
        workType = "C-Suite Strategy, Cost Restructuring & Market Entry Blueprints",
        costMin = 14,
        costMax = 32,
        costLabel = "₹14L – ₹32L (IIM / ISB / SRCC / IIT)",
        scope = "Global Top Tier (McKinsey, BCG, Bain, Big 4 Strategy)",
        mobility = MobilityRank.ABROAD,
        growthRate = "+24% YoY Sustained",
        topRecruiters = "McKinsey & Co., Boston Consulting Group, Bain & Co., Kearney, Strategy&",
        description = "Advising enterprise CEOs and government boards on multi-billion dollar strategic transformations and operational agility.",
        benefits = "Rapid career progression, global executive exposure, premium corporate expense accounts, and top MBA sponsorship."
    ),
    Career(
        id = "investigative-journalism",
        title = "Investigative Journalism & Digital Media Production",
        stream = Stream.HUMANITIES,
        tag = CareerTag.UNCOMMON,
        traits = listOf(TraitKey.VERBAL, TraitKey.SOCIAL, TraitKey.CREATIVE),
        salaryEntry = "₹7 LPA – ₹14 LPA",
        salaryExperienced = "₹22 LPA – ₹50 LPA",
        salaryLabel = "₹7L – ₹50L PA",
        workType = "Open-Source Intelligence (OSINT), Documentary Filmmaking & Public Advocacy",
        costMin = 5,
        costMax = 16,
        costLabel = "₹5L – ₹16L (ACJ / IIMC / Jamia / DU)",
        scope = "Independent Media, Investigative Collectives & Global News Agencies",
        mobility = MobilityRank.INDIA,
        growthRate = "+22% Steady",
        topRecruiters = "The Hindu, Reuters, BBC, The Wire, Newslaundry, ProPublica",
        description = "Uncovering systemic financial and environmental corruption through forensic document analysis, field reporting, and narrative multimedia.",
        benefits = "Immense democratic impact, international fellowships (Pulitzer, Ramnath Goenka), and intellectual independence."
    )
)
