package com.amr.drawing_app.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.amr.drawing_app.DrawingSpace

class Circle(var startX: Float, var startY: Float, actionPaint: Paint?) : DrawingSpace.Action {

//    var centerX = 0f
//    var centerY = 0f
//    var radius = 0f
    var endX: Float
    var endY: Float
    var actionPaint: Paint

    fun setFinalPoint(touchX: Float, touchY: Float) {
        endX = touchX
        endY = touchY
    }

//    fun setRadius(currentX: Float, currentY: Float) {
//        radius = Math.sqrt(Math.pow((startX - currentX).toDouble(), 2.0)
//                + Math.pow((startY - currentY).toDouble(), 2.0)).toFloat() / 2.0f
//        centerX = (startX + currentX) / 2.0f
//        centerY = (startY + currentY) / 2.0f
//    }

    override fun drawAction(canvas: Canvas?) {
        canvas!!.drawOval(RectF(startX, startY, endX, endY), actionPaint)
        //  canvas!!.drawCircle(centerX, centerY, radius, actionPaint)
    }

    init {
        endX = startX
        endY = startY
        this.actionPaint = Paint(actionPaint)
    }
}