package com.example.margdarsh.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object MargdarshReportDrawingUtils {

    const val PAGE_WIDTH = 595
    const val PAGE_HEIGHT = 842
    const val MARGIN = 36f
    const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)
    const val TOTAL_PAGES = 65

    fun drawPageHeader(
        canvas: Canvas,
        sectionTitle: String,
        pageNumber: Int,
        totalPages: Int = TOTAL_PAGES,
        candidateName: String = "Candidate"
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Navy top bar
        paint.color = Color.parseColor("#002B66")
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 42f, paint)

        // Gold accent stripe
        paint.color = Color.parseColor("#F5A623")
        canvas.drawRect(0f, 42f, PAGE_WIDTH.toFloat(), 44f, paint)

        // Header Title
        paint.color = Color.WHITE
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("MargDarsh ★ Holistic Dossier", MARGIN, 26f, paint)

        // Section Title
        paint.color = Color.parseColor("#F5A623")
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val displaySection = sectionTitle.take(54)
        canvas.drawText(displaySection, MARGIN + 145f, 26f, paint)

        // Candidate on right
        val displayName = candidateName.ifBlank { "Candidate" }.take(22)
        paint.color = Color.parseColor("#E0E7FF")
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val candStr = "Candidate: $displayName"
        val candW = paint.measureText(candStr)
        canvas.drawText(candStr, PAGE_WIDTH - MARGIN - candW, 26f, paint)
    }

    fun drawPageFooter(canvas: Canvas, pageNum: Int, totalPages: Int = TOTAL_PAGES) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val y = PAGE_HEIGHT - 16f

        paint.color = Color.parseColor("#94A3B8")
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("MargDarsh Confidential 65-Page Holistic Dossier • Institutional Evaluation & Psychometric Audit", MARGIN, y, paint)

        val pageStr = "Page $pageNum of $totalPages"
        val w = paint.measureText(pageStr)
        canvas.drawText(pageStr, PAGE_WIDTH - MARGIN - w, y, paint)
    }

    // -------------------------------------------------------------------------------------
    // VISUAL CHARTS & DIAGRAMS
    // -------------------------------------------------------------------------------------

    /**
     * Renders a circular Donut Score Gauge with central overall score and sub-quotients.
     */
    fun drawDonutGauge(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        overallScore: Int,
        iq: Int,
        pq: Int,
        sq: Int
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val strokeW = 14f

        // Outer track
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeW
        paint.color = Color.parseColor("#E2E8F0")
        val oval = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
        canvas.drawArc(oval, 135f, 270f, false, paint)

        // Arc Progress
        val sweep = (overallScore / 100f).coerceIn(0f, 1f) * 270f
        paint.color = if (overallScore >= 80) Color.parseColor("#10B981") else if (overallScore >= 65) Color.parseColor("#0284C7") else Color.parseColor("#F59E0B")
        canvas.drawArc(oval, 135f, sweep, false, paint)

        // Center Score
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#002B66")
        paint.textSize = 28f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val scoreStr = "$overallScore"
        val sw = paint.measureText(scoreStr)
        canvas.drawText(scoreStr, cx - (sw / 2f), cy + 6f, paint)

        paint.textSize = 8.5f
        paint.color = Color.parseColor("#64748B")
        val subStr = "OUT OF 100"
        val subW = paint.measureText(subStr)
        canvas.drawText(subStr, cx - (subW / 2f), cy + 20f, paint)

        // 3 Quotient Badges below
        val badgeY = cy + radius + 18f
        val qWidth = 55f
        val gap = 12f
        val startX = cx - ((qWidth * 3 + gap * 2) / 2f)

        val quotients = listOf(
            Triple("IQ: $iq", "#0284C7", "Cognitive"),
            Triple("PQ: $pq", "#10B981", "Physical"),
            Triple("SQ: $sq", "#8B5CF6", "Social")
        )

        quotients.forEachIndexed { idx, (label, colorHex, subtitle) ->
            val qX = startX + idx * (qWidth + gap)
            val rect = RectF(qX, badgeY, qX + qWidth, badgeY + 24f)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRoundRect(rect, 6f, 6f, paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor(colorHex)
            canvas.drawRoundRect(rect, 6f, 6f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            val lw = paint.measureText(label)
            canvas.drawText(label, qX + (qWidth - lw) / 2f, badgeY + 11f, paint)

            paint.textSize = 6.5f
            paint.color = Color.parseColor("#64748B")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            val stw = paint.measureText(subtitle)
            canvas.drawText(subtitle, qX + (qWidth - stw) / 2f, badgeY + 20f, paint)
        }
    }

    /**
     * Renders a 2D Quadrant Scatter Plot (Interest vs. Potential) with smart de-cluttering,
     * non-overlapping headings, and collision-free label positioning.
     */
    fun drawQuadrantPlot(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        xLabel: String,
        yLabel: String,
        points: List<Triple<String, Float, Float>>, // Name, X (0-100), Y (0-100)
        showSweetSpot: Boolean = true
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Plot Background
        val plotRect = RectF(x, y, x + width, y + height)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(plotRect, 8f, 8f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = Color.parseColor("#CBD5E1")
        canvas.drawRoundRect(plotRect, 8f, 8f, paint)

        val midX = x + width / 2f
        val midY = y + height / 2f

        // Center crosshair divider
        paint.color = Color.parseColor("#94A3B8")
        paint.strokeWidth = 1f
        canvas.drawLine(midX, y, midX, y + height, paint)
        canvas.drawLine(x, midY, x + width, midY, paint)

        // Sweet spot highlighted box in Top-Right quadrant (X >= 50 && Y >= 50)
        if (showSweetSpot) {
            val sweetRect = RectF(midX, y, x + width, midY)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#0C06B6D4") // Soft translucent cyan
            canvas.drawRect(sweetRect, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1.2f
            paint.color = Color.parseColor("#0891B2")
            paint.pathEffect = android.graphics.DashPathEffect(floatArrayOf(5f, 4f), 0f)
            canvas.drawRect(sweetRect, paint)
            paint.pathEffect = null

            // Sweet spot badge label placed safely in top right corner to prevent collisions
            val sweetBadgeRect = RectF(x + width - 152f, y + 6f, x + width - 6f, y + 20f)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#ECFDF5")
            canvas.drawRoundRect(sweetBadgeRect, 4f, 4f, paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#059669")
            canvas.drawRoundRect(sweetBadgeRect, 4f, 4f, paint)

            paint.style = Paint.Style.FILL
            paint.textSize = 6.5f
            paint.color = Color.parseColor("#065F46")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("★ THE SWEET SPOT (Optimal Synergy)", x + width - 148f, y + 16f, paint)
        }

        // Quadrant Background Subtitles placed in safe outer corners so points never collide with them
        paint.style = Paint.Style.FILL
        paint.textSize = 6f
        paint.color = Color.parseColor("#94A3B8")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        canvas.drawText("High Potential • Emerging Interest", x + 8f, y + 14f, paint)
        canvas.drawText("Developing Skill • Re-evaluate", x + 8f, y + height - 8f, paint)
        canvas.drawText("High Interest • Requires Skill Ramp", midX + 8f, y + height - 8f, paint)

        // Axis Titles
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 7.5f
        paint.color = Color.parseColor("#475569")
        canvas.drawText("$xLabel (0 to 100) →", midX - 35f, y + height + 12f, paint)

        // Data point representation with separation & de-cluttering
        data class PlottedPoint(
            val label: String,
            val origX: Float,
            val origY: Float,
            var ptX: Float,
            var ptY: Float,
            val isSweetSpot: Boolean
        )

        val plottedPoints = points.map { (label, px, py) ->
            val cxPt = x + (px.coerceIn(5f, 95f) / 100f) * width
            val cyPt = y + height - (py.coerceIn(5f, 95f) / 100f) * height
            PlottedPoint(
                label = label.take(22),
                origX = cxPt,
                origY = cyPt,
                ptX = cxPt,
                ptY = cyPt,
                isSweetSpot = px >= 50f && py >= 50f
            )
        }

        // De-clutter points: if two points are closer than 14f, gently separate them
        for (pass in 0 until 4) {
            for (i in plottedPoints.indices) {
                for (j in i + 1 until plottedPoints.size) {
                    val p1 = plottedPoints[i]
                    val p2 = plottedPoints[j]
                    val dx = p2.ptX - p1.ptX
                    val dy = p2.ptY - p1.ptY
                    val dist = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
                    val minDist = 14f
                    if (dist < minDist) {
                        val angle = if (dist > 0.001f) Math.atan2(dy.toDouble(), dx.toDouble()) else (i * 0.785)
                        val push = (minDist - dist) / 2f + 1f
                        p1.ptX -= (Math.cos(angle) * push).toFloat()
                        p1.ptY -= (Math.sin(angle) * push).toFloat()
                        p2.ptX += (Math.cos(angle) * push).toFloat()
                        p2.ptY += (Math.sin(angle) * push).toFloat()

                        // Clamp inside plot boundaries
                        p1.ptX = p1.ptX.coerceIn(x + 10f, x + width - 10f)
                        p1.ptY = p1.ptY.coerceIn(y + 10f, y + height - 10f)
                        p2.ptX = p2.ptX.coerceIn(x + 10f, x + width - 10f)
                        p2.ptY = p2.ptY.coerceIn(y + 10f, y + height - 10f)
                    }
                }
            }
        }

        // Draw points and non-overlapping label pills
        val placedLabelRects = mutableListOf<RectF>()
        // Exclude sweet spot badge area from label placement
        placedLabelRects.add(RectF(x + width - 155f, y + 4f, x + width - 4f, y + 24f))

        paint.textSize = 6.8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        plottedPoints.forEachIndexed { idx, pt ->
            // Draw circle
            paint.style = Paint.Style.FILL
            paint.color = if (pt.isSweetSpot) Color.parseColor("#0891B2") else Color.parseColor("#4F46E5")
            canvas.drawCircle(pt.ptX, pt.ptY, 4.5f, paint)

            paint.color = Color.WHITE
            canvas.drawCircle(pt.ptX, pt.ptY, 1.8f, paint)

            // Measure label text
            val textWidth = paint.measureText(pt.label)
            val pillWidth = textWidth + 8f
            val pillHeight = 12f

            // Candidate positions: Right, Left, Above, Below, Top-Right, Bottom-Right, Top-Left, Bottom-Left
            val candidates = listOf(
                Pair(pt.ptX + 7f, pt.ptY - pillHeight / 2f),
                Pair(pt.ptX - pillWidth - 7f, pt.ptY - pillHeight / 2f),
                Pair(pt.ptX - pillWidth / 2f, pt.ptY - pillHeight - 6f),
                Pair(pt.ptX - pillWidth / 2f, pt.ptY + 7f),
                Pair(pt.ptX + 7f, pt.ptY - pillHeight - 4f),
                Pair(pt.ptX + 7f, pt.ptY + 6f),
                Pair(pt.ptX - pillWidth - 7f, pt.ptY - pillHeight - 4f),
                Pair(pt.ptX - pillWidth - 7f, pt.ptY + 6f)
            )

            var bestRect = RectF(
                candidates[0].first.coerceIn(x + 2f, x + width - pillWidth - 2f),
                candidates[0].second.coerceIn(y + 2f, y + height - pillHeight - 2f),
                candidates[0].first.coerceIn(x + 2f, x + width - pillWidth - 2f) + pillWidth,
                candidates[0].second.coerceIn(y + 2f, y + height - pillHeight - 2f) + pillHeight
            )

            // Find first candidate that doesn't overlap any placed label
            for (c in candidates) {
                val clampedX = c.first.coerceIn(x + 4f, x + width - pillWidth - 4f)
                val clampedY = c.second.coerceIn(y + 4f, y + height - pillHeight - 4f)
                val testRect = RectF(clampedX, clampedY, clampedX + pillWidth, clampedY + pillHeight)

                val hasCollision = placedLabelRects.any { RectF.intersects(it, testRect) }
                if (!hasCollision) {
                    bestRect = testRect
                    break
                }
            }

            placedLabelRects.add(bestRect)

            // If label is offset from point, draw fine connector line
            val labelCenterX = (bestRect.left + bestRect.right) / 2f
            val labelCenterY = (bestRect.top + bestRect.bottom) / 2f
            val distToLabel = Math.hypot((labelCenterX - pt.ptX).toDouble(), (labelCenterY - pt.ptY).toDouble()).toFloat()
            if (distToLabel > 12f) {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 0.7f
                paint.color = Color.parseColor("#94A3B8")
                canvas.drawLine(pt.ptX, pt.ptY, labelCenterX, labelCenterY, paint)
            }

            // Draw clean background pill badge for label to prevent any visual clash
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#FAFAFA")
            canvas.drawRoundRect(bestRect, 3f, 3f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.5f
            paint.color = if (pt.isSweetSpot) Color.parseColor("#0891B2") else Color.parseColor("#CBD5E1")
            canvas.drawRoundRect(bestRect, 3f, 3f, paint)

            // Draw label text
            paint.style = Paint.Style.FILL
            paint.color = if (pt.isSweetSpot) Color.parseColor("#0891B2") else Color.parseColor("#0F172A")
            paint.textSize = 6.8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(pt.label, bestRect.left + 4f, bestRect.bottom - 3.5f, paint)
        }
    }

    /**
     * Renders an Institutional-Grade Neurological Brain Strength Map modeled after Bodhami's Holistic Report.
     * Features a central biological cerebral hemisphere with distinct anatomical lobes,
     * anchor pins, and clear radiating leader lines connected to structured callout cards on the left & right.
     */
    fun drawBrainStrengthDiagram(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        width: Float,
        height: Float,
        areas: List<Triple<String, Int, String>> // Name, Score, TierColorHex
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Outer Card Container
        val cardRect = RectF(cx - width / 2f, cy - height / 2f, cx + width / 2f, cy + height / 2f)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRoundRect(cardRect, 10f, 10f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = Color.parseColor("#CBD5E1")
        canvas.drawRoundRect(cardRect, 10f, 10f, paint)

        val leftEdge = cx - width / 2f
        val rightEdge = cx + width / 2f
        val topEdge = cy - height / 2f

        // Data map for quick access
        val areaMap = areas.associateBy { it.first.lowercase() }

        fun findArea(nameKey: String, defaultScore: Int, defaultColor: String, subtitle: String): Quadruple {
            val match = areaMap.entries.find { it.key.contains(nameKey.lowercase()) }
            return if (match != null) {
                Quadruple(match.value.first, match.value.second, match.value.third, subtitle)
            } else {
                Quadruple(nameKey, defaultScore, defaultColor, subtitle)
            }
        }

        // Left Column (5 Callouts for Anterior / Inferior Areas)
        val leftCallouts = listOf(
            findArea("Frontal Lobe", 85, "#10B981", "Executive Strategy & Planning"),
            findArea("Broca's Area", 82, "#10B981", "Verbal Expression & Speech"),
            findArea("Motor Area", 78, "#F59E0B", "Kinetic Dexterity & Control"),
            findArea("Temporal Lobe", 75, "#F59E0B", "Memory Retention & Auditory"),
            findArea("Brain Stem", 72, "#F59E0B", "Autonomic Stamina & Focus")
        )

        // Right Column (5 Callouts for Posterior / Superior Areas)
        val rightCallouts = listOf(
            findArea("Sensory Area", 80, "#10B981", "Sensory Processing & Touch"),
            findArea("Parietal Lobe", 86, "#10B981", "Spatial & Mathematical Map"),
            findArea("Wernicke's Area", 79, "#F59E0B", "Language Comprehension"),
            findArea("Occipital Lobe", 84, "#10B981", "Visual Pattern Recognition"),
            findArea("Cerebellum", 76, "#F59E0B", "Balance & Motor Precision")
        )

        // Center coordinates for the biological brain graphic
        val bx = cx - 2f
        val by = cy - 6f

        // Draw Bodhami-style Anatomical Lobes of Cerebral Hemisphere (Soft Pastel Tints)
        // 1. Frontal Lobe (Anterior superior)
        val frontalPath = android.graphics.Path().apply {
            moveTo(bx - 10f, by - 65f)
            cubicTo(bx - 45f, by - 60f, bx - 80f, by - 35f, bx - 85f, by)
            cubicTo(bx - 90f, by + 25f, bx - 75f, by + 45f, bx - 50f, by + 45f)
            cubicTo(bx - 35f, by + 45f, bx - 25f, by + 25f, bx - 20f, by + 10f)
            cubicTo(bx - 15f, by - 20f, bx - 10f, by - 45f, bx - 10f, by - 65f)
            close()
        }
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#E0F2FE") // Soft Sky
        canvas.drawPath(frontalPath, paint)

        // 2. Parietal Lobe (Superior middle)
        val parietalPath = android.graphics.Path().apply {
            moveTo(bx - 10f, by - 65f)
            cubicTo(bx + 20f, by - 70f, bx + 55f, by - 60f, bx + 70f, by - 40f)
            cubicTo(bx + 55f, by - 10f, bx + 35f, by + 10f, bx + 10f, by + 5f)
            cubicTo(bx - 5f, by + 5f, bx - 10f, by - 25f, bx - 10f, by - 65f)
            close()
        }
        paint.color = Color.parseColor("#EDE9FE") // Soft Violet
        canvas.drawPath(parietalPath, paint)

        // 3. Occipital Lobe (Posterior inferior)
        val occipitalPath = android.graphics.Path().apply {
            moveTo(bx + 70f, by - 40f)
            cubicTo(bx + 90f, by - 20f, bx + 95f, by + 15f, bx + 80f, by + 35f)
            cubicTo(bx + 60f, by + 45f, bx + 45f, by + 40f, bx + 35f, by + 25f)
            cubicTo(bx + 35f, by + 10f, bx + 55f, by - 10f, bx + 70f, by - 40f)
            close()
        }
        paint.color = Color.parseColor("#FCE7F3") // Soft Rose/Pink
        canvas.drawPath(occipitalPath, paint)

        // 4. Temporal Lobe (Inferior middle)
        val temporalPath = android.graphics.Path().apply {
            moveTo(bx - 50f, by + 45f)
            cubicTo(bx - 30f, by + 50f, bx + 15f, by + 50f, bx + 35f, by + 25f)
            cubicTo(bx + 20f, by + 10f, bx - 5f, by + 5f, bx - 20f, by + 10f)
            cubicTo(bx - 25f, by + 25f, bx - 35f, by + 45f, bx - 50f, by + 45f)
            close()
        }
        paint.color = Color.parseColor("#FEF3C7") // Soft Amber
        canvas.drawPath(temporalPath, paint)

        // 5. Cerebellum (Posterior base, below Occipital)
        val cerebellumPath = android.graphics.Path().apply {
            moveTo(bx + 35f, by + 45f)
            cubicTo(bx + 65f, by + 45f, bx + 75f, by + 65f, bx + 65f, by + 80f)
            cubicTo(bx + 50f, by + 90f, bx + 25f, by + 85f, bx + 15f, by + 65f)
            cubicTo(bx + 15f, by + 50f, bx + 25f, by + 45f, bx + 35f, by + 45f)
            close()
        }
        paint.color = Color.parseColor("#D1FAE5") // Soft Mint
        canvas.drawPath(cerebellumPath, paint)

        // 6. Brain Stem (Inferior central stalk)
        val stemPath = android.graphics.Path().apply {
            moveTo(bx - 5f, by + 52f)
            lineTo(bx + 15f, by + 52f)
            lineTo(bx + 10f, by + 92f)
            lineTo(bx - 12f, by + 92f)
            close()
        }
        paint.color = Color.parseColor("#E2E8F0") // Slate
        canvas.drawPath(stemPath, paint)

        // Whole Brain Outline & Internal Sulci Lines (clean, elegant Bodhami line art)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.parseColor("#2563EB") // Royal Blue brain contour
        canvas.drawPath(frontalPath, paint)
        canvas.drawPath(parietalPath, paint)
        canvas.drawPath(occipitalPath, paint)
        canvas.drawPath(temporalPath, paint)
        canvas.drawPath(cerebellumPath, paint)
        canvas.drawPath(stemPath, paint)

        // Sulci (Anatomical grooves)
        paint.strokeWidth = 1f
        paint.color = Color.parseColor("#93C5FD")
        canvas.drawLine(bx - 10f, by - 65f, bx - 15f, by + 10f, paint) // Central Sulcus
        canvas.drawLine(bx - 45f, by + 15f, bx + 30f, by + 25f, paint) // Sylvian Fissure
        canvas.drawLine(bx + 70f, by - 40f, bx + 35f, by + 25f, paint) // Parieto-occipital

        // Cerebellar Folia lines
        paint.color = Color.parseColor("#6EE7B7")
        canvas.drawLine(bx + 25f, by + 60f, bx + 65f, by + 60f, paint)
        canvas.drawLine(bx + 25f, by + 72f, bx + 60f, by + 72f, paint)

        // Anatomical Anchor Loci on the brain
        val anchorPoints = mapOf(
            "Frontal Lobe" to Pair(bx - 55f, by - 15f),
            "Broca's Area" to Pair(bx - 40f, by + 18f),
            "Motor Area" to Pair(bx - 18f, by - 25f),
            "Temporal Lobe" to Pair(bx - 5f, by + 32f),
            "Brain Stem" to Pair(bx - 2f, by + 76f),
            "Sensory Area" to Pair(bx + 5f, by - 30f),
            "Parietal Lobe" to Pair(bx + 35f, by - 35f),
            "Wernicke's Area" to Pair(bx + 22f, by + 15f),
            "Occipital Lobe" to Pair(bx + 68f, by + 10f),
            "Cerebellum" to Pair(bx + 45f, by + 65f)
        )

        val cardW = 126f
        val cardH = 27f
        val cardSpacing = 5f
        val leftX = leftEdge + 10f
        val rightX = rightEdge - 10f - cardW
        val startY = topEdge + 16f

        // Draw Left Callout Cards & Leader Lines
        leftCallouts.forEachIndexed { i, item ->
            val cY = startY + i * (cardH + cardSpacing)
            val anchor = anchorPoints[item.name] ?: Pair(bx - 40f, by)
            val cardTargetX = leftX + cardW
            val cardTargetY = cY + cardH / 2f

            // Leader Line with elbow
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#94A3B8")
            val elbowX = leftX + cardW + 16f
            canvas.drawLine(anchor.first, anchor.second, elbowX, cardTargetY, paint)
            canvas.drawLine(elbowX, cardTargetY, cardTargetX, cardTargetY, paint)

            // Pin on Brain
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor(item.colorHex)
            canvas.drawCircle(anchor.first, anchor.second, 4.5f, paint)
            paint.color = Color.WHITE
            canvas.drawCircle(anchor.first, anchor.second, 1.8f, paint)

            // Callout Card Box
            val boxRect = RectF(leftX, cY, leftX + cardW, cY + cardH)
            paint.style = Paint.Style.FILL
            paint.color = Color.WHITE
            canvas.drawRoundRect(boxRect, 4f, 4f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(boxRect, 4f, 4f, paint)

            // Accent Left Pill
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor(item.colorHex)
            canvas.drawRoundRect(RectF(leftX, cY, leftX + 4f, cY + cardH), 2f, 2f, paint)

            // Name & Score
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0F172A")
            canvas.drawText(item.name, leftX + 8f, cY + 11f, paint)

            paint.textSize = 8f
            paint.color = Color.parseColor(item.colorHex)
            val scoreText = "${item.score}%"
            val scoreW = paint.measureText(scoreText)
            canvas.drawText(scoreText, leftX + cardW - scoreW - 5f, cY + 11f, paint)

            // Subtitle
            paint.textSize = 6f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#64748B")
            canvas.drawText(item.subtitle, leftX + 8f, cY + 21f, paint)
        }

        // Draw Right Callout Cards & Leader Lines
        rightCallouts.forEachIndexed { i, item ->
            val cY = startY + i * (cardH + cardSpacing)
            val anchor = anchorPoints[item.name] ?: Pair(bx + 40f, by)
            val cardTargetX = rightX
            val cardTargetY = cY + cardH / 2f

            // Leader Line with elbow
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.parseColor("#94A3B8")
            val elbowX = rightX - 16f
            canvas.drawLine(anchor.first, anchor.second, elbowX, cardTargetY, paint)
            canvas.drawLine(elbowX, cardTargetY, cardTargetX, cardTargetY, paint)

            // Pin on Brain
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor(item.colorHex)
            canvas.drawCircle(anchor.first, anchor.second, 4.5f, paint)
            paint.color = Color.WHITE
            canvas.drawCircle(anchor.first, anchor.second, 1.8f, paint)

            // Callout Card Box
            val boxRect = RectF(rightX, cY, rightX + cardW, cY + cardH)
            paint.style = Paint.Style.FILL
            paint.color = Color.WHITE
            canvas.drawRoundRect(boxRect, 4f, 4f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 0.8f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRoundRect(boxRect, 4f, 4f, paint)

            // Accent Right Pill
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor(item.colorHex)
            canvas.drawRoundRect(RectF(rightX + cardW - 4f, cY, rightX + cardW, cY + cardH), 2f, 2f, paint)

            // Name & Score
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.color = Color.parseColor("#0F172A")
            canvas.drawText(item.name, rightX + 8f, cY + 11f, paint)

            paint.textSize = 8f
            paint.color = Color.parseColor(item.colorHex)
            val scoreText = "${item.score}%"
            val scoreW = paint.measureText(scoreText)
            canvas.drawText(scoreText, rightX + cardW - scoreW - 8f, cY + 11f, paint)

            // Subtitle
            paint.textSize = 6f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#64748B")
            canvas.drawText(item.subtitle, rightX + 8f, cY + 21f, paint)
        }

        // Bottom Legend (Center Below Brain Graphic)
        val legY = topEdge + height - 10f
        paint.style = Paint.Style.FILL
        paint.textSize = 7f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val legItems = listOf(
            Pair("#10B981", "≥80% Dominant Asset"),
            Pair("#F59E0B", "60–79% Strong Foundation"),
            Pair("#D946EF", "<60% Developing Pathway")
        )

        var lX = cx - 110f
        legItems.forEach { (colorHex, txt) ->
            paint.color = Color.parseColor(colorHex)
            canvas.drawCircle(lX, legY - 2.5f, 3.5f, paint)
            paint.color = Color.parseColor("#334155")
            canvas.drawText(txt, lX + 7f, legY, paint)
            lX += paint.measureText(txt) + 20f
        }
    }

    private data class Quadruple(val name: String, val score: Int, val colorHex: String, val subtitle: String)

    /**
     * Renders a Dual Polygon Radar Chart (Blue Student Line vs. Red Industry Benchmark).
     */
    fun drawDualRadarChart(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        axes: List<Triple<String, Int, Int>> // Label, Student Score (0-100), Industry Benchmark (0-100)
    ) {
        val count = axes.size
        if (count < 3) return
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Draw Concentric Reference Rings (25%, 50%, 75%, 100%)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        val ringLevels = listOf(0.25f, 0.50f, 0.75f, 1.0f)
        ringLevels.forEach { lvl ->
            paint.color = if (lvl == 1.0f) Color.parseColor("#CBD5E1") else Color.parseColor("#E2E8F0")
            val r = radius * lvl
            val ringPath = android.graphics.Path()
            for (i in 0 until count) {
                val angle = (Math.PI * 2 / count) * i - (Math.PI / 2)
                val ax = (cx + r * Math.cos(angle)).toFloat()
                val ay = (cy + r * Math.sin(angle)).toFloat()
                if (i == 0) ringPath.moveTo(ax, ay) else ringPath.lineTo(ax, ay)
            }
            ringPath.close()
            canvas.drawPath(ringPath, paint)
        }

        // Draw Axis Spoke Lines & Labels
        paint.color = Color.parseColor("#E2E8F0")
        for (i in 0 until count) {
            val angle = (Math.PI * 2 / count) * i - (Math.PI / 2)
            val ex = (cx + radius * Math.cos(angle)).toFloat()
            val ey = (cy + radius * Math.sin(angle)).toFloat()
            canvas.drawLine(cx, cy, ex, ey, paint)

            // Label at spoke tip
            val lx = (cx + (radius + 14f) * Math.cos(angle)).toFloat()
            val ly = (cy + (radius + 14f) * Math.sin(angle)).toFloat()
            val labelText = axes[i].first.take(15)
            paint.style = Paint.Style.FILL
            paint.textSize = 6.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#64748B")
            val lw = paint.measureText(labelText)
            canvas.drawText(labelText, lx - (lw / 2f), ly + 2.5f, paint)
        }

        // Draw Polygon 1: Industry Benchmark Vector (Red / Crimson)
        val industryPath = android.graphics.Path()
        for (i in 0 until count) {
            val angle = (Math.PI * 2 / count) * i - (Math.PI / 2)
            val norm = (axes[i].third.coerceIn(20, 100) / 100f) * radius
            val px = (cx + norm * Math.cos(angle)).toFloat()
            val py = (cy + norm * Math.sin(angle)).toFloat()
            if (i == 0) industryPath.moveTo(px, py) else industryPath.lineTo(px, py)
        }
        industryPath.close()

        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#15DC2626") // Translucent Red
        canvas.drawPath(industryPath, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.8f
        paint.color = Color.parseColor("#DC2626") // Crimson Red
        canvas.drawPath(industryPath, paint)

        // Draw Polygon 2: Student Score Vector (Royal Navy / Blue)
        val studentPath = android.graphics.Path()
        for (i in 0 until count) {
            val angle = (Math.PI * 2 / count) * i - (Math.PI / 2)
            val norm = (axes[i].second.coerceIn(20, 100) / 100f) * radius
            val px = (cx + norm * Math.cos(angle)).toFloat()
            val py = (cy + norm * Math.sin(angle)).toFloat()
            if (i == 0) studentPath.moveTo(px, py) else studentPath.lineTo(px, py)
        }
        studentPath.close()

        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#250284C7") // Translucent Blue
        canvas.drawPath(studentPath, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.2f
        paint.color = Color.parseColor("#0284C7") // Bright Royal Blue
        canvas.drawPath(studentPath, paint)

        // Legend below chart
        val legY = cy + radius + 28f
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#0284C7")
        canvas.drawCircle(cx - 65f, legY, 4f, paint)
        paint.textSize = 7.5f
        paint.color = Color.parseColor("#0F172A")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Student Profile", cx - 56f, legY + 2.5f, paint)

        paint.color = Color.parseColor("#DC2626")
        canvas.drawCircle(cx + 25f, legY, 4f, paint)
        paint.color = Color.parseColor("#0F172A")
        canvas.drawText("Industry Benchmark", cx + 34f, legY + 2.5f, paint)
    }

    /**
     * Renders a National Peer Benchmark comparison row with dual bars and delta badge.
     */
    fun drawPeerComparisonRow(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        skillName: String,
        studentScore: Int,
        medianScore: Int,
        percentile: Int
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Skill Name & Percentile Badge
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.parseColor("#0F172A")
        canvas.drawText(skillName.take(30), x, y + 10f, paint)

        val percStr = "${percentile}th %ile"
        paint.textSize = 7.5f
        paint.color = Color.parseColor("#0284C7")
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val pw = paint.measureText(percStr)
        canvas.drawText(percStr, x + 180f - pw, y + 10f, paint)

        // Progress Bar Track
        val barX = x + 195f
        val barW = width - 260f
        val barH = 7f

        // National Median Marker
        val trackRect = RectF(barX, y + 4f, barX + barW, y + 4f + barH)
        paint.color = Color.parseColor("#E2E8F0")
        canvas.drawRoundRect(trackRect, barH / 2f, barH / 2f, paint)

        // Student Bar
        val studentW = (barW * (studentScore / 100f)).coerceIn(barH, barW)
        val studentRect = RectF(barX, y + 4f, barX + studentW, y + 4f + barH)
        paint.color = if (studentScore >= medianScore) Color.parseColor("#10B981") else Color.parseColor("#F59E0B")
        canvas.drawRoundRect(studentRect, barH / 2f, barH / 2f, paint)

        // National Median Dash Line
        val medX = barX + (barW * (medianScore / 100f))
        paint.color = Color.parseColor("#475569")
        paint.strokeWidth = 2f
        canvas.drawLine(medX, y + 1f, medX, y + 4f + barH + 3f, paint)

        // Numbers on Right: Student vs Median (e.g. "82 vs 52 (+30)")
        val diff = studentScore - medianScore
        val diffStr = if (diff >= 0) "+$diff" else "$diff"
        val statStr = "$studentScore vs $medianScore ($diffStr)"
        paint.textSize = 7.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = if (diff >= 0) Color.parseColor("#059669") else Color.parseColor("#DC2626")
        canvas.drawText(statStr, x + width - 58f, y + 10f, paint)
    }

    /**
     * Renders a Bi-Directional Personality Continuum Slider (-100 to +100).
     */
    fun drawContinuumSlider(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        leftPole: String,
        rightPole: String,
        scoreFloat: Float, // -100 to +100
        leftDesc: String,
        rightDesc: String
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Pole Titles
        paint.textSize = 8.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = if (scoreFloat <= 0) Color.parseColor("#0284C7") else Color.parseColor("#64748B")
        canvas.drawText("◀ $leftPole", x, y + 9f, paint)

        paint.color = if (scoreFloat > 0) Color.parseColor("#0284C7") else Color.parseColor("#64748B")
        val rW = paint.measureText("$rightPole ▶")
        canvas.drawText("$rightPole ▶", x + width - rW, y + 9f, paint)

        // Slider Track
        val barY = y + 16f
        val barH = 5f
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#E2E8F0")
        val trackRect = RectF(x, barY, x + width, barY + barH)
        canvas.drawRoundRect(trackRect, barH / 2f, barH / 2f, paint)

        // Center neutral divider
        val midX = x + width / 2f
        paint.color = Color.parseColor("#94A3B8")
        canvas.drawLine(midX, barY - 2f, midX, barY + barH + 2f, paint)

        // Current Indicator Circle
        val norm = (scoreFloat.coerceIn(-100f, 100f) + 100f) / 200f
        val indicatorX = x + (norm * width)
        paint.color = Color.parseColor("#0284C7")
        canvas.drawCircle(indicatorX, barY + barH / 2f, 5.5f, paint)
        paint.color = Color.WHITE
        canvas.drawCircle(indicatorX, barY + barH / 2f, 2f, paint)

        // Explanatory descriptor
        val narrative = if (scoreFloat <= -20) leftDesc else if (scoreFloat >= 20) rightDesc else "Balanced equilibrium between $leftPole and $rightPole."
        paint.textSize = 6.8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        paint.color = Color.parseColor("#64748B")
        drawWrappedText(canvas, narrative, x, y + 30f, width, paint, 8.5f)
    }

    fun drawProgressBar(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        progress: Float,
        fillColor: Int,
        trackColor: Int
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val trackRect = RectF(x, y, x + width, y + height)
        paint.color = trackColor
        canvas.drawRoundRect(trackRect, height / 2, height / 2, paint)

        val fillWidth = (width * progress.coerceIn(0f, 1f)).coerceAtLeast(height)
        val fillRect = RectF(x, y, x + fillWidth, y + height)
        paint.color = fillColor
        canvas.drawRoundRect(fillRect, height / 2, height / 2, paint)
    }

    fun drawWrappedText(
        canvas: Canvas,
        text: String,
        x: Float,
        startY: Float,
        maxWidth: Float,
        paint: Paint,
        lineSpacing: Float
    ): Float {
        var y = startY
        val words = text.split(" ")
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = paint.measureText(testLine)
            if (width > maxWidth && currentLine.isNotEmpty()) {
                canvas.drawText(currentLine, x, y, paint)
                y += lineSpacing
                currentLine = word
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty()) {
            canvas.drawText(currentLine, x, y, paint)
            y += lineSpacing
        }

        return y
    }

    fun saveToPublicDownloads(context: Context, sourceFile: File, fileName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Margdarsh")
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { output ->
                        sourceFile.inputStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                }
            } catch (_: Exception) {}
        }
    }

    fun openPdf(context: Context, file: File) {
        try {
            val authority = "${context.packageName}.fileprovider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            val chooser = Intent.createChooser(viewIntent, "Open Official MargDarsh 35-Page PDF Dossier")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "PDF saved to Downloads! Open with any PDF reader.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
