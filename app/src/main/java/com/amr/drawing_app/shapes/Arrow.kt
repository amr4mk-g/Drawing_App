package com.amr.drawing_app.shapes

import android.graphics.Canvas
import android.graphics.Paint
import com.amr.drawing_app.DrawingSpace

class Arrow(var startX: Float, var startY: Float, actionPaint: Paint?) : DrawingSpace.Action {

    var endX: Float
    var endY: Float
    var actionPaint: Paint

    fun calculateDegree(): Double {
        var startRadians = Math.atan(((endY - startY) / (endX - startX)).toDouble()).toFloat()
        startRadians += ((if (endX >= startX) 90 else -90) * Math.PI / 180).toFloat()
        return Math.toDegrees(startRadians.toDouble())
    }

    fun setFinalPoint(touchX: Float, touchY: Float) {
        endX = touchX
        endY = touchY
    }

    fun endX1(degree: Double): Float {
        return endX+ (50 * Math.cos(Math.toRadians((degree-30) + 90))).toFloat()
    }

    fun endY1(degree: Double): Float {
        return endY+ (50 * Math.sin(Math.toRadians((degree-30) + 90))).toFloat()
    }

    fun endX2(degree: Double): Float {
        return endX+ (50 * Math.cos(Math.toRadians((degree-60) + 180))).toFloat()
    }

    fun endY2(degree: Double): Float {
        return endY+ (50 * Math.sin(Math.toRadians((degree-60) + 180))).toFloat()
    }

    override fun drawAction(canvas: Canvas?) {
        val degree = calculateDegree()
        canvas!!.drawLine(startX, startY, endX, endY, actionPaint)
        canvas.drawLine(endX, endY, endX1(degree), endY1(degree), actionPaint)
        canvas.drawLine(endX, endY, endX2(degree), endY2(degree), actionPaint)
    }

    init {
        endX = startX
        endY = startY
        this.actionPaint = Paint(actionPaint)
    }
}