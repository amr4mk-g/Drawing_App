package com.amr.drawing_app

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_buttons.*
import kotlinx.android.synthetic.main.menu_colors.*

class MainActivity : AppCompatActivity() {

    private var lastMode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeMode(lastMode)

        pencil.setOnClickListener {
            lastMode = 1
            changeMode(1)
        }
        arrow.setOnClickListener {
            lastMode = 2
            changeMode(2)
        }
        rectangle.setOnClickListener {
            lastMode = 3
            changeMode(3)
        }
        circle.setOnClickListener {
            lastMode = 4
            changeMode(4)
        }

        colors.setOnClickListener { changeMode(5) }
        cRed.setOnClickListener { changeColor(1) }
        cGreen.setOnClickListener { changeColor(2) }
        cBlue.setOnClickListener { changeColor(3) }
        cBlack.setOnClickListener { changeColor(4) }

        seekBarSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(s: SeekBar, p: Int, f: Boolean) {}
        override fun onStartTrackingTouch(s: SeekBar) {}
        override fun onStopTrackingTouch(s: SeekBar) { saveChange() }
        })
    }

    private fun changeMode(type: Int){
        pencil.setBackgroundColor(Color.TRANSPARENT)
        arrow.setBackgroundColor(Color.TRANSPARENT)
        rectangle.setBackgroundColor(Color.TRANSPARENT)
        circle.setBackgroundColor(Color.TRANSPARENT)
        colors.setBackgroundColor(Color.TRANSPARENT)
        l_colors.visibility = View.GONE
        when (type) {
            1 -> {
                pencil.setBackgroundColor(Color.LTGRAY)
                drawing_space.setMode(DrawingSpace.PENCIL_MODE)
            }
            2 -> {
                arrow.setBackgroundColor(Color.LTGRAY)
                drawing_space.setMode(DrawingSpace.ARROW_MODE)
            }
            3 -> {
                rectangle.setBackgroundColor(Color.LTGRAY)
                drawing_space.setMode(DrawingSpace.RECTANGLE_MODE)
            }
            4 -> {
                circle.setBackgroundColor(Color.LTGRAY)
                drawing_space.setMode(DrawingSpace.CIRCLE_MODE)
            }
            5 -> {
                colors.setBackgroundColor(Color.LTGRAY)
                l_colors.visibility = View.VISIBLE
            }
        }
    }

    private fun changeColor(type: Int){
        when (type) {
            1 -> drawing_space.setColor(Color.RED)
            2 -> drawing_space.setColor(Color.GREEN)
            3 -> drawing_space.setColor(Color.BLUE)
            4 -> drawing_space.setColor(Color.BLACK)
        }
        saveChange()
    }

    private fun saveChange(){
        drawing_space.setSize(seekBarSize.progress.toFloat())
        l_colors.visibility = View.GONE
        changeMode(lastMode)
    }

}