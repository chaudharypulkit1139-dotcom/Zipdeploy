package com.example.margdarsh.data

import kotlin.math.roundToInt

object AssessmentEngine {

    private val BUDGET_CEILING = mapOf(
        "low" to 8f,
        "mid" to 25f,
        "high" to 60f,
        "premium" to 200f
    )

    private val MOBILITY_MAP = mapOf(
        "home" to 0,
        "state" to 1,
        "india" to 2,
        "abroad" to 3
    )

    fun scoreTraits(answers: Map<String, String>): Map<TraitKey, Int> {
        val totals = TraitKey.entries.associateWith { 0.0f }.toMutableMap()
        val max = TraitKey.entries.associateWith { 0.0f }.toMutableMap()

        for (q in STUDENT_QUESTIONS) {
            val answerValue = answers[q.id] ?: continue

            when (q.type) {
                QuestionType.LIKERT -> {
                    val raw = answerValue.toIntOrNull() ?: continue
                    val norm = (raw - 1) / 4.0f // 0..1
                    for ((key, weight) in q.weights) {
                        totals[key] = (totals[key] ?: 0.0f) + (norm * weight)
                        max[key] = (max[key] ?: 0.0f) + weight
                    }
                }
                QuestionType.MCQ_APTITUDE, QuestionType.SCENARIO_PREFERENCE -> {
                    val selectedOption = q.options.find { it.id == answerValue }
                    val maxWeightPossible = q.options.flatMap { it.weights.entries }
                    
                    // Add max possible weight for each trait touched in this question
                    for (entry in maxWeightPossible) {
                        val currentMax = max[entry.key] ?: 0.0f
                        if (entry.value > currentMax) {
                            max[entry.key] = currentMax + (entry.value * 0.7f)
                        }
                    }

                    if (selectedOption != null) {
                        for ((key, weight) in selectedOption.weights) {
                            totals[key] = (totals[key] ?: 0.0f) + weight
                            // Ensure max is at least as large as weight
                            val curMax = max[key] ?: 0.0f
                            if (curMax < weight) {
                                max[key] = weight
                            }
                        }
                    }
                }
            }
        }

        return TraitKey.entries.associateWith { key ->
            val m = max[key] ?: 0.0f
            val t = totals[key] ?: 0.0f
            if (m > 0f) {
                ((t / m) * 100f).roundToInt().coerceIn(15, 99)
            } else {
                45
            }
        }
    }

    fun recommendStream(traits: Map<TraitKey, Int>, parent: Map<String, String>): StreamResult {
        val analytical = traits[TraitKey.ANALYTICAL] ?: 50
        val spatial = traits[TraitKey.SPATIAL] ?: 50
        val practical = traits[TraitKey.PRACTICAL] ?: 50
        val enterprising = traits[TraitKey.ENTERPRISING] ?: 50
        val verbal = traits[TraitKey.VERBAL] ?: 50
        val social = traits[TraitKey.SOCIAL] ?: 50
        val creative = traits[TraitKey.CREATIVE] ?: 50

        val scores = mutableMapOf<Stream, Float>(
            Stream.SCIENCE to (analytical * 0.45f + spatial * 0.35f + practical * 0.20f),
            Stream.COMMERCE to (enterprising * 0.45f + analytical * 0.30f + practical * 0.25f),
            Stream.HUMANITIES to (verbal * 0.40f + social * 0.32f + creative * 0.28f)
        )

        // Parent calibre nudges the demanding Science route
        when (parent["calibre"]) {
            "top" -> scores[Stream.SCIENCE] = (scores[Stream.SCIENCE] ?: 0f) + 6f
            "struggling" -> {
                scores[Stream.SCIENCE] = (scores[Stream.SCIENCE] ?: 0f) - 8f
                scores[Stream.HUMANITIES] = (scores[Stream.HUMANITIES] ?: 0f) + 4f
            }
        }

        // Budget realism
        if (parent["budget"] == "low" && parent["funding"] == "none") {
            scores[Stream.SCIENCE] = (scores[Stream.SCIENCE] ?: 0f) - 6f
            scores[Stream.COMMERCE] = (scores[Stream.COMMERCE] ?: 0f) + 4f
        }

        when (parent["aspiration"]) {
            "earning" -> scores[Stream.COMMERCE] = (scores[Stream.COMMERCE] ?: 0f) + 5f
            "impact" -> scores[Stream.HUMANITIES] = (scores[Stream.HUMANITIES] ?: 0f) + 5f
            "passion" -> {
                val top = Stream.entries.maxByOrNull { scores[it] ?: 0f } ?: Stream.SCIENCE
                scores[top] = (scores[top] ?: 0f) + 5f
            }
        }

        val pref = parent["preferredStream"]
        if (pref != null && pref != "open") {
            val matchingStream = Stream.entries.find { it.displayName.equals(pref, ignoreCase = true) }
            if (matchingStream != null) {
                scores[matchingStream] = (scores[matchingStream] ?: 0f) + 4f
            }
        }

        val rankedStream = Stream.entries.maxByOrNull { scores[it] ?: 0f } ?: Stream.SCIENCE
        val strongestTrait = TraitKey.entries.maxByOrNull { traits[it] ?: 0 } ?: TraitKey.ANALYTICAL

        val mobilityNote = when (parent["relocation"]) {
            "home" -> "with premier programmes accessible from home city"
            "abroad" -> "including prestigious international university pathways"
            else -> "with full flexibility across top tier Indian colleges (IITs, NLUs, SRCC, IIMs)"
        }

        val rationale = "${strongestTrait.label} is this student's highest measured cognitive trait (${traits[strongestTrait]}%), aligning directly with ${rankedStream.displayName}. Combined with family financial bandwidth and location preferences, this pathway offers optimal career growth $mobilityNote."

        return StreamResult(
            stream = rankedStream,
            scores = scores,
            rationale = rationale
        )
    }

    fun recommendCareers(
        traits: Map<TraitKey, Int>,
        parent: Map<String, String>,
        recommendedStream: Stream = Stream.SCIENCE,
        limit: Int = CAREERS.size
    ): List<ScoredCareer> {
        val ceilingBase = BUDGET_CEILING[parent["budget"] ?: "mid"] ?: 25f
        val loanMultiplier = when (parent["funding"]) {
            "full" -> 2.0f
            "some" -> 1.4f
            else -> 1.0f
        }
        val ceiling = ceilingBase * loanMultiplier
        val mobilityLevel = MOBILITY_MAP[parent["relocation"] ?: "india"] ?: 2
        val risk = parent["risk"] ?: "medium"

        val scored = CAREERS.map { career ->
            val traitSum = career.traits.sumOf { traits[it] ?: 50 }
            val traitFit = traitSum.toFloat() / career.traits.size.coerceAtLeast(1)
            var fit = traitFit

            if (career.stream == recommendedStream) {
                fit += 16f
            }

            val affordable = career.costMin <= ceiling
            val reachable = career.mobility.level <= mobilityLevel

            if (!affordable) fit -= 20f
            if (!reachable) fit -= 15f

            if (career.tag == CareerTag.UNCOMMON || career.tag == CareerTag.PREMIUM_ELITE) {
                fit += when (risk) {
                    "high" -> 10f
                    "medium" -> 4f
                    else -> -8f
                }
            }

            if (parent["aspiration"] == "stability" && career.tag == CareerTag.STABLE) {
                fit += 8f
            }
            if (parent["aspiration"] == "earning" && (career.tag == CareerTag.HIGH_GROWTH || career.tag == CareerTag.PREMIUM_ELITE)) {
                fit += 8f
            }

            ScoredCareer(
                career = career,
                fit = fit.roundToInt().coerceIn(40, 99),
                affordable = affordable,
                reachable = reachable,
                isLockedTopMatch = false
            )
        }
            .sortedByDescending { it.fit }
            .take(limit)

        // Mark the highest scored career as the locked top match teaser
        return scored.mapIndexed { index, item ->
            if (index == 0) {
                item.copy(isLockedTopMatch = true)
            } else {
                item
            }
        }
    }
}
