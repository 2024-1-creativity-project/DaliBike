package com.example.dali_bike.api

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.dali_bike.R

class OutLineTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

        private var strokeColor: Int
        private var strokeWidthVal: Float

        init {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OutLineTextView)
                strokeWidthVal = typedArray.getFloat(R.styleable.OutLineTextView_textStrokeWidth, 3f)
                strokeColor = typedArray.getColor(R.styleable.OutLineTextView_textStrokeColor, Color.RED)
                typedArray.recycle()
        }

        override fun onDraw(canvas: Canvas) {
                // 외곽선 그리기
                val states: ColorStateList = textColors
                val originalTextColor = currentTextColor

                paint.style = Paint.Style.STROKE
                paint.strokeWidth = strokeWidthVal
                setTextColor(strokeColor)
                super.onDraw(canvas)

                // 채우기 그리기
                paint.style = Paint.Style.FILL
                setTextColor(originalTextColor)
                super.onDraw(canvas)
        }
}
