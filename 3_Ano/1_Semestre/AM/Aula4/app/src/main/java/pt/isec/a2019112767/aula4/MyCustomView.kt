package pt.isec.a2019112767.aula4
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MyCustomView : View {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var corDoBotao = Color.BLACK

    init {
        isClickable = true
        setOnClickListener {
            corDoBotao = if(corDoBotao == Color.BLACK) Color.RED else Color.BLACK
            paint.color=corDoBotao
            Log.i("CustomView", "cor do botão alterada")
            invalidate()
        }

    }

    constructor(context : Context) : super(context)
    constructor(context : Context,
                attrs : AttributeSet?) :
            super(context,attrs)
    constructor(context : Context,
                attrs : AttributeSet?,
                defStyleAttr:Int) :
            super(context,attrs,defStyleAttr)

    //Override OnDraw

    override fun onDraw(canvas : Canvas) {

        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2f)

        paint.color = corDoBotao
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)

        //desenhar o texto
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = radius/3f
        var textY = centerY - (paint.descent() + paint.ascent()) / 2
        canvas.drawText("Porto é ...", centerX, textY, paint)

    }

}