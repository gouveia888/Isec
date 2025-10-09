package pt.isec.a2019112767.jogogalo
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TicTacToeView : View {

    constructor(context : Context) : super(context)
    constructor(context : Context,
                attrs : AttributeSet?) :
            super(context,attrs)
    constructor(context : Context,
                attrs : AttributeSet?,
                defStyleAttr:Int) :
            super(context,attrs,defStyleAttr)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        invalidate() //obrigatorio para desenhar
    }

    //fazer as linhas do tabuleiro com o paint
    override fun onDraw(canvas : Canvas){
        super.onDraw(canvas)
        val CellWidth = width/3f
        val CellHeight = height/3f
        canvas.drawLine(0f,CellHeight,width.toFloat(),CellHeight,paint)
        canvas.drawLine(0f,2*CellHeight,width.toFloat(),2*CellHeight,paint)
        canvas.drawLine(CellWidth,0f,CellWidth,height.toFloat(),paint)
        canvas.drawLine(2*CellWidth,0f,2*CellWidth,height.toFloat(),paint)

    }
}