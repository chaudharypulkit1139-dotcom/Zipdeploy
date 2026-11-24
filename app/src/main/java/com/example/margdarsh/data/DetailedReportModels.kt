package com.example.margdarsh.data

import kotlin.math.roundToInt

data class QuestionPsychologicalAudit(
    val questionId: String,
    val questionNumber: Int,
    val section: String,
    val questionText: String,
    val studentSelectedLabel: String,
    val psychologicalMeaning: String,
    val specificAnswerInsight: String,
    val traitImpact: String,
    val demonstratedSkill: String = "",
    val howSkillHelpsStudent: String = ""
)

data class StudentCoreSkillItem(
    val title: String,
    val archetypeBadge: String,
    val scorePercentage: Int,
    val whyYouExcel: String,
    val realWorldManifestation: String
)

data class GrowthEdgeSkillItem(
    val skillName: String,
    val category: String,
    val whyDevelopThis: String,
    val careerMultiplierImpact: String,
    val ninetyDayActionRoadmap: List<String>
)

data class CareerSynergyItem(
    val careerTitle: String,
    val matchPercentage: Int,
    val psychologicalSynergyRationale: String,
    val intellectualFulfillmentFactor: String
)

data class DetailedPsychometricAuditReport(
    val candidateName: String,
    val cognitiveArchetype: String,
    val archetypeTagline: String,
    val executiveSummary: String,
    val questionAudits: List<QuestionPsychologicalAudit>,
    val coreInnateSkills: List<StudentCoreSkillItem>,
    val growthEdgeSkills: List<GrowthEdgeSkillItem>,
    val careerSynergies: List<CareerSynergyItem>
)

object DetailedReportGenerator {

    fun generate(
        profile: StudentProfile,
        studentAnswers: Map<String, String>,
        parentAnswers: Map<String, String>,
        traits: Map<TraitKey, Int>,
        streamResult: StreamResult?,
        careers: List<ScoredCareer>
    ): DetailedPsychometricAuditReport {
        val name = profile.studentName.ifBlank { "Candidate" }
        val sortedTraits = TraitKey.entries.sortedByDescending { traits[it] ?: 0 }
        val topTrait = sortedTraits.firstOrNull() ?: TraitKey.ANALYTICAL
        val secondTrait = sortedTraits.getOrNull(1) ?: TraitKey.SPATIAL
        val lowestTrait = sortedTraits.lastOrNull() ?: TraitKey.SOCIAL

        // Determine Archetype
        val (archetype, archetypeTagline, executiveSummary) = determineArchetype(topTrait, secondTrait, name)

        // Generate Question-by-Question Audits
        val questionAudits = mutableListOf<QuestionPsychologicalAudit>()
        STUDENT_QUESTIONS.forEachIndexed { index, q ->
            val answer = studentAnswers[q.id]
            if (answer != null) {
                var selectedLabel = ""
                var insight = ""
                var impact = ""

                var demonstratedSkill = ""
                var howSkillHelps = ""

                when (q.type) {
                    QuestionType.LIKERT -> {
                        val score = answer.toIntOrNull() ?: 3
                        selectedLabel = when (score) {
                            1 -> "Strongly Disagree"
                            2 -> "Disagree"
                            3 -> "Neutral / Balanced"
                            4 -> "Agree"
                            5 -> "Strongly Agree"
                            else -> "Option $score"
                        }
                        val traitLabels = q.weights.keys.joinToString(", ") { it.label }
                        impact = "Influenced $traitLabels (Score: $score/5)"

                        insight = when {
                            score >= 4 -> "Demonstrates high natural affinity and instinctive alignment with ${q.section}. Student experiences positive cognitive flow and self-efficacy in this dimension."
                            score <= 2 -> "Shows deliberate selectivity or aversion toward this pattern. Student actively prefers alternative cognitive pathways or structured support here."
                            else -> "Balanced and adaptable; can engage when required but does not rely on this as their primary unconscious operating system."
                        }

                        demonstratedSkill = "${q.section} Proficiency"
                        howSkillHelps = when {
                            score >= 4 -> "Provides strong intrinsic stamina in ${q.section}, enabling rapid conceptual learning, superior exam recall, and career specialization without mental fatigue."
                            score <= 2 -> "Allows student to conserve cognitive energy by delegating or using structured tools, focusing bandwidth on higher-scoring natural strengths."
                            else -> "Gives the student flexible versatility to adapt to interdisciplinary coursework without rigid cognitive bias."
                        }
                    }
                    QuestionType.MCQ_APTITUDE, QuestionType.SCENARIO_PREFERENCE -> {
                        val opt = q.options.find { it.id == answer }
                        if (opt != null) {
                            selectedLabel = opt.text
                            insight = opt.psychologicalInsight.ifBlank {
                                if (opt.isCorrect) "Demonstrates superior deductive speed and precision."
                                else "Reveals an intuitive shortcut under complex multi-variable parameters."
                            }
                            val traitsTouched = opt.weights.keys.joinToString(", ") { it.label }
                            impact = if (traitsTouched.isNotBlank()) "Bolstered: $traitsTouched" else "Situational Choice"

                            demonstratedSkill = when (q.id) {
                                "mcq1" -> "Rate-Time-Distance Algebraic Deduction"
                                "mcq2" -> "Combinatorial & Factorial Logic"
                                "mcq3" -> "Quantitative Data Interpretation & Growth Rate Analysis"
                                "mcq4" -> "Propositional Logic & Syllogistic Deduction"
                                "mcq5" -> "3D Spatial Visualization & Mental Rotation"
                                "mcq6" -> "Scientific Method & Controlled Variable Isolation"
                                "mcq7" -> "Natural Team Role Leadership Archetype"
                                "mcq8" -> "Cross-Disciplinary Analytical Lens"
                                "mcq9" -> "Tactile Troubleshooting & Empirical Debugging"
                                "mcq10" -> "Decisive Risk Management Under Uncertainty"
                                "mcq11" -> "Deep Conceptual Text Processing"
                                "mcq12" -> "Intrinsic Curiosity & Self-Directed Exploration"
                                else -> if (q.id.startsWith("HQ_")) "${q.section} Domain Competency" else "${q.section} Competency"
                            }

                            howSkillHelps = when (q.id) {
                                "mcq1" -> "Crucial for physics kinematics, mathematical problem-solving speed, and quantitative entrance exams like JEE, SAT, and CUET."
                                "mcq2" -> "Provides a competitive edge in advanced mathematics, probability, algorithm design, software engineering, and data science."
                                "mcq3" -> "Essential for financial analytics, business case competitions, economics research, and management entrance tests (IPMAT/CAT)."
                                "mcq4" -> "Directly translates to high scores in Law entrance (CLAT/AILET), critical reading, formal debate, and structured debugging."
                                "mcq5" -> "Core requirement for architecture (NATA/JEE Paper 2), mechanical engineering, organic chemistry stereochemistry, and UI/UX design."
                                "mcq6" -> "Fundamental for laboratory research, medical diagnostics (NEET), experimental physics, and hypothesis-driven problem solving."
                                "mcq7" -> "Clarifies whether the student creates maximum impact as a technical builder, enterprising founder, creative designer, or empathetic diplomat."
                                "mcq8" -> "Enables multi-dimensional problem solving by connecting technology, economics, policy ethics, and user empathy."
                                "mcq9" -> "Drives practical execution in robotics, circuit fabrication, experimental sciences, and rapid software prototyping."
                                "mcq10" -> "Prepares the student for executive leadership, operational resilience, and decisive triage in high-stakes environments."
                                "mcq11" -> "Optimizes mastery of long-form academic literature, complex legal judgments, and advanced theoretical research."
                                "mcq12" -> "Unlocks natural flow state, sustained motivation, and specialized mastery without external pressure."
                                else -> if (q.contextSnippet.isNotBlank()) "Direct application: ${q.contextSnippet}" else "Enhances cognitive agility, subject-matter comprehension, and strategic academic performance in ${q.section}."
                            }
                        }
                    }
                }

                questionAudits.add(
                    QuestionPsychologicalAudit(
                        questionId = q.id,
                        questionNumber = index + 1,
                        section = q.section,
                        questionText = q.text,
                        studentSelectedLabel = selectedLabel,
                        psychologicalMeaning = q.psychologicalMeaning.ifBlank { "Evaluates fundamental cognitive orientation in ${q.section}." },
                        specificAnswerInsight = insight,
                        traitImpact = impact,
                        demonstratedSkill = demonstratedSkill,
                        howSkillHelpsStudent = howSkillHelps
                    )
                )
            }
        }

        // Generate Core Innate Skills (What they have right now)
        val coreSkills = generateCoreSkills(traits, sortedTraits)

        // Generate Growth-Edge Skills (What to develop to 10x success)
        val growthEdgeSkills = generateGrowthEdgeSkills(sortedTraits, lowestTrait, streamResult?.stream ?: Stream.SCIENCE)

        // Generate Career Synergies
        val careerSynergies = careers.take(4).map { scored ->
            val career = scored.career
            val synergy = when (career.stream) {
                Stream.SCIENCE -> "Combines your ${topTrait.label} (${traits[topTrait]}%) and ${secondTrait.label} (${traits[secondTrait]}%) into rigorous scientific/engineering problem spaces with high intellectual autonomy."
                Stream.COMMERCE -> "Directly leverages your strategic commercial intuition and quantitative reasoning, allowing you to manage risk and build scalable market systems."
                Stream.HUMANITIES -> "Maximizes your verbal eloquence and empathetic social radar, placing you at the forefront of policy reform, legal argumentation, and cultural leadership."
            }
            CareerSynergyItem(
                careerTitle = career.title,
                matchPercentage = scored.fit,
                psychologicalSynergyRationale = synergy,
                intellectualFulfillmentFactor = "Offers high cognitive stimulation while minimizing repetitive administrative burnout."
            )
        }

        return DetailedPsychometricAuditReport(
            candidateName = name,
            cognitiveArchetype = archetype,
            archetypeTagline = archetypeTagline,
            executiveSummary = executiveSummary,
            questionAudits = questionAudits,
            coreInnateSkills = coreSkills,
            growthEdgeSkills = growthEdgeSkills,
            careerSynergies = careerSynergies
        )
    }

    private fun determineArchetype(top: TraitKey, second: TraitKey, name: String): Triple<String, String, String> {
        return when {
            top == TraitKey.ANALYTICAL && second == TraitKey.SPATIAL -> Triple(
                "The First-Principles Systems Architect",
                "Deep Structural Logic • Algorithmic Rigor • Spatial Synthesis",
                "$name possesses a highly structured, analytical mind with exceptional spatial and multi-variable reasoning. Rather than accepting superficial answers, $name naturally deconstructs complex systems into fundamental components and rebuilds them with engineering precision."
            )
            top == TraitKey.ENTERPRISING && (second == TraitKey.ANALYTICAL || second == TraitKey.VERBAL) -> Triple(
                "The Strategic Venture Catalyst",
                "Commercial Acumen • High-Stakes Negotiation • Calculated Boldness",
                "$name operates with an enterprising, opportunity-seeking lens. Combining persuasive rhetoric with rapid mental economics, $name excels at identifying market leverage, navigating calculated risks, and mobilizing people toward ambitious milestones."
            )
            top == TraitKey.SOCIAL || top == TraitKey.VERBAL -> Triple(
                "The Empathetic Diplomat & Public Advocate",
                "Emotional Intelligence • Persuasive Rhetoric • Civic Conscience",
                "$name demonstrates a potent combination of verbal dexterity and deep empathetic resonance. $name intuitively grasps unspoken human dynamics, excelling in spaces where persuasion, ethical governance, and social impact converge."
            )
            top == TraitKey.CREATIVE -> Triple(
                "The Avant-Garde Cross-Disciplinary Innovator",
                "Lateral Synthesis • Aesthetic Intuition • Unorthodox Problem Reframing",
                "$name thrives on divergent thinking and conceptual cross-pollination. Unafraid of ambiguity, $name connects ideas across distant disciplines to create novel products, narratives, and visionary systems."
            )
            else -> Triple(
                "The Pragmatic High-Execution Strategist",
                "Tactile Mastery • Methodical Problem Solving • Relentless Stamina",
                "$name is anchored in practical execution, systematic troubleshooting, and high resilience under pressure. $name bridges theoretical models into tangible real-world outcomes without getting lost in abstraction."
            )
        }
    }

    private fun generateCoreSkills(
        traits: Map<TraitKey, Int>,
        sortedTraits: List<TraitKey>
    ): List<StudentCoreSkillItem> {
        val list = mutableListOf<StudentCoreSkillItem>()
        sortedTraits.take(3).forEach { trait ->
            val score = traits[trait] ?: 75
            when (trait) {
                TraitKey.ANALYTICAL -> list.add(
                    StudentCoreSkillItem(
                        title = "First-Principles Decomposition",
                        archetypeBadge = "Cognitive Precision",
                        scorePercentage = score,
                        whyYouExcel = "You instinctively isolate independent variables and refuse to accept logical fallacies or emotional assertions without empirical proof.",
                        realWorldManifestation = "Effortlessly debugs complex codebases, mathematical rate systems, legal logic puzzles, and forensic data discrepancies."
                    )
                )
                TraitKey.SPATIAL -> list.add(
                    StudentCoreSkillItem(
                        title = "Visual-Spatial Mental Simulation",
                        archetypeBadge = "Abstract Architecture",
                        scorePercentage = score,
                        whyYouExcel = "You have high working-memory capacity for rotating 3D structures, multi-layered data graphs, and geometric systems in your head.",
                        realWorldManifestation = "Essential superpower for CAD design, aerospace aerodynamics, neural network topology, and financial technical charting."
                    )
                )
                TraitKey.ENTERPRISING -> list.add(
                    StudentCoreSkillItem(
                        title = "Commercial Margin & Risk Modeling",
                        archetypeBadge = "Strategic Leverage",
                        scorePercentage = score,
                        whyYouExcel = "You spot economic opportunities, pricing asymmetries, and leverage points where minimal inputs yield massive exponential returns.",
                        realWorldManifestation = "Excels at venture pitching, high-stakes trade negotiations, capital allocation, and startup leadership under uncertainty."
                    )
                )
                TraitKey.VERBAL -> list.add(
                    StudentCoreSkillItem(
                        title = "High-Bandwidth Persuasive Rhetoric",
                        archetypeBadge = "Executive Influence",
                        scorePercentage = score,
                        whyYouExcel = "You articulate intricate ideas with razor-sharp linguistic precision, structuring arguments that disarm skepticism and persuade crowds.",
                        realWorldManifestation = "Supreme asset in appellate courtrooms, boardrooms, diplomatic negotiations, and high-impact investigative writing."
                    )
                )
                TraitKey.SOCIAL -> list.add(
                    StudentCoreSkillItem(
                        title = "Non-Verbal Emotional Radar (EQ)",
                        archetypeBadge = "Empathic Diplomacy",
                        scorePercentage = score,
                        whyYouExcel = "You perceive unspoken subtext, hidden tensions, and underlying human motivations long before others articulate them.",
                        realWorldManifestation = "Masterful in team conflict de-escalation, patient psychiatric diagnostic care, customer discovery, and organizational harmony."
                    )
                )
                TraitKey.CREATIVE -> list.add(
                    StudentCoreSkillItem(
                        title = "Lateral Conceptual Cross-Pollination",
                        archetypeBadge = "Divergent Innovation",
                        scorePercentage = score,
                        whyYouExcel = "You synthesize unorthodox connections between two completely distant fields that conventional linear thinkers fail to notice.",
                        realWorldManifestation = "Powers breakthrough UI/UX innovations, original artistic creations, patentable engineering mechanisms, and disruptive brand stories."
                    )
                )
                TraitKey.PRACTICAL -> list.add(
                    StudentCoreSkillItem(
                        title = "Tactile Troubleshooting & Deep Grit",
                        archetypeBadge = "Execution Power",
                        scorePercentage = score,
                        whyYouExcel = "You possess high stamina for continuous hands-on experimentation, refusing to stop until a physical or software prototype works flawlessly.",
                        realWorldManifestation = "Critical for robotics bench testing, surgical precision, hardware prototyping, and high-volume product shipping."
                    )
                )
            }
        }
        return list
    }

    private fun generateGrowthEdgeSkills(
        sortedTraits: List<TraitKey>,
        lowestTrait: TraitKey,
        stream: Stream
    ): List<GrowthEdgeSkillItem> {
        val list = mutableListOf<GrowthEdgeSkillItem>()

        // Skill 1: Bridge based on lowest trait
        when (lowestTrait) {
            TraitKey.ENTERPRISING -> list.add(
                GrowthEdgeSkillItem(
                    skillName = "Commercial Value Translation & Pitching",
                    category = "Career Multiplier",
                    whyDevelopThis = "Even the most brilliant technical code or scientific discovery remains unnoticed unless translated into clear commercial value for investors and stakeholders.",
                    careerMultiplierImpact = "5x salary growth by elevating you from an individual executor to an executive who commands funding and strategic influence.",
                    ninetyDayActionRoadmap = listOf(
                        "Month 1: Read 'Pitch Anything' by Oren Klaff and study 5 Y-Combinator demo day pitch decks.",
                        "Month 2: Practice presenting complex academic concepts in 60-second layperson elevator pitches.",
                        "Month 3: Build a mock business case with cost-benefit analysis for your favourite technical project."
                    )
                )
            )
            TraitKey.VERBAL -> list.add(
                GrowthEdgeSkillItem(
                    skillName = "Structured Executive Writing & Rhetorical Framing",
                    category = "Communication Edge",
                    whyDevelopThis = "Senior leadership in Tier-1 firms (Google, McKinsey, NLUs, AI Labs) communicate through concise 1-page written memos.",
                    careerMultiplierImpact = "Guarantees your ideas are approved faster and protects you from being misunderstood in high-stakes environments.",
                    ninetyDayActionRoadmap = listOf(
                        "Month 1: Adopt the Minto Pyramid Principle (Lead with conclusion, follow with structured supporting arguments).",
                        "Month 2: Write 1 analytical op-ed or technical review per week summarizing complex news.",
                        "Month 3: Seek feedback from mentors on eliminating filler words and tightening sentence density."
                    )
                )
            )
            TraitKey.SOCIAL -> list.add(
                GrowthEdgeSkillItem(
                    skillName = "Multi-Stakeholder Active Listening & EQ Mediation",
                    category = "Interpersonal Mastery",
                    whyDevelopThis = "As career complexity scales, technical difficulty decreases while human coordination friction increases exponentially.",
                    careerMultiplierImpact = "Prevents team bottlenecks and positions you as a trusted leader who inspires peak performance.",
                    ninetyDayActionRoadmap = listOf(
                        "Month 1: Practice reflective questioning in daily conversations (rephrase what others say before responding).",
                        "Month 2: Participate in student club governance or community volunteer initiatives.",
                        "Month 3: Study negotiation frameworks from Harvard PON (Program on Negotiation)."
                    )
                )
            )
            else -> list.add(
                GrowthEdgeSkillItem(
                    skillName = "Data Modeling & Algorithmic Literacy",
                    category = "Future-Proofing",
                    whyDevelopThis = "Every industry—from constitutional law to healthcare and marketing—is becoming data-centric and AI-accelerated.",
                    careerMultiplierImpact = "Unlocks elite compensation brackets by giving you dual fluency in domain knowledge and data systems.",
                    ninetyDayActionRoadmap = listOf(
                        "Month 1: Complete Python for Data Science basics or advanced Excel/SQL modeling.",
                        "Month 2: Analyze real-world open datasets (Kaggle / RBI / World Bank) to discover hidden statistical patterns.",
                        "Month 3: Deploy an interactive dashboard visualizing public trend insights."
                    )
                )
            )
        }

        // Skill 2: Strategic Ambiguity Navigation
        list.add(
            GrowthEdgeSkillItem(
                skillName = "Managing Ambiguity & Rapid Prototyping",
                category = "High-Growth Leadership",
                whyDevelopThis = "Real-world leadership never comes with complete instructions or predetermined answers. Thriving in uncertainty is the hallmark of the top 1%.",
                careerMultiplierImpact = "Allows you to lead pioneering zero-to-one projects that define new industry frontiers.",
                ninetyDayActionRoadmap = listOf(
                    "Month 1: Set a 24-hour sprint deadline to build and test imperfect minimum viable projects.",
                    "Month 2: Get comfortable making decisions with only 70% of the desired information.",
                    "Month 3: Conduct post-mortems on failed experiments to extract rapid compounding learnings."
                )
            )
        )

        return list
    }
}
