package com.example.noglut.utilities.loader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.sin
import androidx.core.graphics.toColorInt

class CustomLoader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var rotationAngle = 0f
    private var dotRadius = 12f
    private var centerX = 0f
    private var centerY = 0f
    private var circleRadius = 60f

    private val colors = intArrayOf(
        "#FF6B6B".toColorInt(),
        "#4ECDC4".toColorInt(),
        "#45B7D1".toColorInt(),
        "#96CEB4".toColorInt(),
        "#FFEAA7".toColorInt(),
        "#DDA0DD".toColorInt()
    )

    init {
        setupPaint()
        initAnimation()
    }

    private fun setupPaint() {
        paint.style = Paint.Style.FILL
    }

    private fun initAnimation() {
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1500
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                rotationAngle = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        circleRadius = (minOf(w, h) / 4f).coerceAtMost(80f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dotCount = colors.size
        val angleStep = 360f / dotCount

        for (i in 0 until dotCount) {
            val angle = Math.toRadians((rotationAngle + (i * angleStep)).toDouble())
            val x = centerX + circleRadius * cos(angle).toFloat()
            val y = centerY + circleRadius * sin(angle).toFloat()

            val scale =
                0.5f + 0.5f * sin(Math.toRadians((rotationAngle * 2 + i * 60).toDouble())).toFloat()
            val currentRadius = dotRadius * (0.7f + 0.3f * scale)

            paint.color = colors[i]
            paint.alpha = (255 * (0.6f + 0.4f * scale)).toInt()

            canvas.drawCircle(x, y, currentRadius, paint)
        }
    }

    fun stopAnimation() {
        animator?.cancel()
        animator = null
    }

    fun startAnimation() {
        stopAnimation()
        initAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }
}
