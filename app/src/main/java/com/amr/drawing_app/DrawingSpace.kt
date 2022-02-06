package com.amr.drawing_app

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.opengl.ETC1.getHeight
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.amr.drawing_app.shapes.Arrow
import com.amr.drawing_app.shapes.Circle
import com.amr.drawing_app.shapes.Rectangle
import java.lang.ref.WeakReference

class DrawingSpace(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var mPaintColor: Int = Color.BLACK
    var mStrokeWidth: Float =  18F
    var mDrawPaint: Paint
    var mCanvasPaint: Paint
    var mMode = PENCIL_MODE
    var mDrawingEnabled = true
    var mCurrentPath: MyPath? = null
    var mCurrentCircle: Circle? = null
    var mCurrentRectangle: Rectangle? = null
    var mCurrentArrow: Arrow? = null
    var mActions: MutableList<Action?>
    lateinit var mDrawCanvas: Canvas
    lateinit var mCanvasBitmap: Bitmap

    interface Action { fun drawAction(canvas: Canvas?) }

    fun setDrawingEnabled(enabled: Boolean) { mDrawingEnabled = enabled }

    companion object {
        const val PENCIL_MODE = 1
        const val CIRCLE_MODE = 2
        const val RECTANGLE_MODE = 3
        const val ARROW_MODE = 4
    }

    init {
        mDrawPaint = Paint()
        mDrawPaint.setColor(mPaintColor)
        mDrawPaint.setAntiAlias(true)
        mDrawPaint.setStrokeWidth(mStrokeWidth)
        mDrawPaint.setStyle(Paint.Style.STROKE)
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND)
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND)
        mCurrentPath = MyPath(mDrawPaint)
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mActions = ArrayList()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mDrawCanvas = Canvas(mCanvasBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap, 0F, 0F, mCanvasPaint)
        if (mCurrentPath != null) {
            canvas.drawPath(mCurrentPath!!, mDrawPaint)
        }
        if (mCurrentCircle != null) {
            mCurrentCircle!!.drawAction(canvas)
        }
        if (mCurrentRectangle != null) {
            mCurrentRectangle!!.drawAction(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mDrawingEnabled) { mDrawingEnabled = true }
                onTouchDown(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> if (mDrawingEnabled) {
                onTouchMode(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> if (mDrawingEnabled) {
                onTouchUp()
            } else { mDrawingEnabled = true }
            else -> return super.onTouchEvent(event)
        }
        invalidate()
        return true
    }

    private fun onTouchDown(touchX: Float, touchY: Float) {
        when (mMode) {
            CIRCLE_MODE -> mCurrentCircle = Circle(touchX, touchY, mDrawPaint)
            RECTANGLE_MODE -> mCurrentRectangle = Rectangle(touchX, touchY, mDrawPaint)
            ARROW_MODE -> mCurrentArrow = Arrow(touchX, touchY, mDrawPaint)
            else -> {
                mDrawPaint.setXfermode(null)
                mCurrentPath = MyPath(mDrawPaint)
                mCurrentPath!!.reset()
                mCurrentPath!!.moveTo(touchX, touchY)
            }
        }
    }

    private fun onTouchMode(touchX: Float, touchY: Float) {
        when (mMode) {
            CIRCLE_MODE -> mCurrentCircle!!.setFinalPoint(touchX, touchY)
            RECTANGLE_MODE -> mCurrentRectangle!!.setFinalPoint(touchX, touchY)
            ARROW_MODE -> mCurrentArrow!!.setFinalPoint(touchX, touchY)
            else -> mCurrentPath!!.lineTo(touchX, touchY)
        }
    }

    private fun onTouchUp() {
        when (mMode) {
            CIRCLE_MODE -> {
                mActions.add(mCurrentCircle)
                mCurrentCircle!!.drawAction(mDrawCanvas)
                mCurrentCircle = null
            }
            RECTANGLE_MODE -> {
                mActions.add(mCurrentRectangle)
                mCurrentRectangle!!.drawAction(mDrawCanvas)
                mCurrentRectangle = null
            }
            ARROW_MODE -> {
                mActions.add(mCurrentArrow)
                mCurrentArrow!!.drawAction(mDrawCanvas)
                mCurrentArrow = null
            }
            else -> {
                mActions.add(mCurrentPath)
                mCurrentPath!!.drawAction(mDrawCanvas)
                mCurrentPath = null
            }
        }
    }

    fun setColor(newColor: Int?) {
        invalidate()
        mPaintColor = newColor!!
        mDrawPaint.setColor(mPaintColor)
    }

    fun setSize(width: Float) {
        mStrokeWidth = width
        mDrawPaint.setStrokeWidth(mStrokeWidth)
    }

    fun clearAll() {
        if (mActions.size > 0) { mActions.clear() }
    }

    fun setMode(option: Int) {
            mMode = option
    }

    inner class MyPath(paint: Paint?) : Path(), Action {
        var actionPaint: Paint

        override fun drawAction(canvas: Canvas?) {
            mDrawPaint.setXfermode(null)
            canvas!!.drawPath(this, actionPaint)
        }

        init { actionPaint = Paint(paint) }
    }

}