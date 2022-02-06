package com.amr.drawing_app.shapes

import android.graphics.Canvas
import android.graphics.Paint
import com.amr.drawing_app.DrawingSpace

class Rectangle(var startX: Float, var startY: Float, actionPaint: Paint?) : DrawingSpace.Action {

    var endX: Float
    var endY: Float
    var actionPaint: Paint

    fun setFinalPoint(touchX: Float, touchY: Float) {
        endX = touchX
        endY = touchY
    }

    override fun drawAction(canvas: Canvas?) {
        canvas!!.drawRect(startX, startY, endX, endY, actionPaint)
    }

    init {
        endX = startX
        endY = startY
        this.actionPaint = Paint(actionPaint)
    }
}