package com.example.wildnest.Widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.wildnest.R

class ButtonStyle : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.button_style) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.button_style_disable) as Drawable
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDrawForeground(canvas)

        background = if (isEnabled) enabledBackground else disabledBackground

        setText(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        "Get Started".also { text = it }
    }
}