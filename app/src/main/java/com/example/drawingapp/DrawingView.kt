package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs:AttributeSet) :View(context,attrs){
    private var drawPath: CustomPath? = null
    private var drawPaint: Paint? = null
    private var canvasBitmap:Bitmap? = null
    private var canvasPaint: Paint? = null
    private var canvas:Canvas? = null
    private var brushSize:Float = 0.toFloat()
    private var color = Color.BLACK


    init{
        setUpDrawing()
    }

    private fun setUpDrawing(){
        drawPaint = Paint()
        drawPath = CustomPath(color,brushSize)
        drawPaint!!.color = color
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
        brushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        if(!drawPath!!.isEmpty){
            drawPaint!!.strokeWidth = drawPath!!.brushThickness
            drawPaint!!.color = drawPath!!.color
            canvas.drawPath(drawPath!!,drawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                drawPath!!.color = color
                drawPath!!.brushThickness = brushSize
                drawPath!!.reset()
                drawPath!!.moveTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_MOVE->{
                drawPath!!.lineTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_UP->{
                drawPath = CustomPath(color,brushSize)
            }
            else->return false
        }
        invalidate()
        return true

        return super.onTouchEvent(event)

    }

    internal inner class CustomPath(var color:Int,var brushThickness:Float):Path(){


    }
}