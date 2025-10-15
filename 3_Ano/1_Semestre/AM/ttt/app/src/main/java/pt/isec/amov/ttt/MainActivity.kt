package pt.isec.amov.ttt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.isec.amov.ttt.databinding.ActivityMainBinding // generated binding class for the layout XML activity_main.xml. First change build.gradle.kts in the android {} scope with "buildFeatures  { viewBinding= true}"

class MainActivity : AppCompatActivity() {
    lateinit var tttView: TicTacToeView // custom view to draw the TicTacToe board (late-initialized var)
    lateinit var binding: ActivityMainBinding // binding to access UI elements from activity_main.xml
    private val viewModel: GameViewModel by viewModels() // initializes GameViewModel using Android Jetpack's viewModels() delegate, providing lifecycle-aware data.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // inflates view binding for activity_main.xml
        setContentView(binding.root) // sets the root view as the activity content
        tttView = TicTacToeView(this) // creates an instance of custom TicTacToeView, passing the activity context
        binding.GameBoardContainer.addView(tttView) // adds the tttView dynamically to FrameLayout view (ID GameBoardContainer) in the constraintlayout
        binding.newGameButton.setOnClickListener {
            viewModel.start() // method on ViewModel to begin or reset the game
        }
        viewModel.winsP1.observe(this) { // observe LiveData property winsP1 from ViewModel
            binding.player1Score.text= it // updates TextView player1Score when scores change
        }
        viewModel.winsP2.observe(this)
        {
            binding.player2Score.text= it //it palavra reservada que representa o objeto que esta a ser observado
        }
        tttView.setOnPlayListener { // sets a listener on the custom view for play actions
            y,x -> viewModel.play(y,x) // user taps a cell, calls ViewModel's play(y, x) with row and column
        }
        viewModel.board.observe(this) { newBoard ->
            tttView.board = newBoard
        }
        viewModel.gameFinishedEvent.observe(this) { winner ->
           when(winner) {
               TicTacToeModel.Players.P1 -> {
                   Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show()
               }

               TicTacToeModel.Players.P2 -> {
                   Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show()
               }

               TicTacToeModel.Players.None -> {}

               null -> TODO()
           }
      }
    }
}

// Custom View - extending Android View to draw the tic tac toe board
class TicTacToeView (context: Context): View(context){
    private var cellSize=0f // size of one cell (square) of the 3x3 grid (float). Assigned later

    var board: Array<Array<TicTacToeModel.Players>>? = null // 2D array of Players (None, P1, P2)
        set(value){
            field = value
            invalidate()
        }

    private val paint = Paint (Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply { // initializes a Paint object for drawing grid lines
        color= Color.BLACK
        strokeWidth=10f
        style=Paint.Style.STROKE
    }

    private val paintP1 = Paint (Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply { // initializes a Paint object for drawing player 1's marks
        color = Color.RED
        strokeWidth = 15f
    }

    private val paintP2 = Paint (Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply { // initializes a Paint object for drawing player 1's marks
        color = Color.BLUE
        strokeWidth = 15f
    }

    init {
        setBackgroundColor(Color.rgb(255,224,32))
    }

    override fun onDraw (canvas: Canvas)
    {
        super.onDraw(canvas)
        cellSize= width / 3f // Calculates the size of a cell as 1/3 of the view width (creates 3 columns)
        for (i in 1..2) { // draw 2 rows and 2 columns
            canvas.drawLine(0f, cellSize * i, width.toFloat(), cellSize * i, paint)  // Row
            canvas.drawLine(cellSize * i, 0f, cellSize * i, height.toFloat(), paint) // Column
        }

        board?.forEachIndexed { row, cols ->
            cols.forEachIndexed { col, player ->
                val centerx = col * cellSize + cellSize / 2
                val centerY = row * cellSize + cellSize / 2
                val pieceSize = cellSize * 0.3f

                when (player) {
                    TicTacToeModel.Players.P1 -> {
                        canvas.drawLine(
                            centerx - pieceSize,
                            centerY - pieceSize,
                            centerx + pieceSize,
                            centerY + pieceSize,
                            paintP1
                        )
                        canvas.drawLine(
                            centerx + pieceSize,
                            centerY - pieceSize,
                            centerx - pieceSize,
                            centerY + pieceSize,
                            paintP1
                        )
                    }

                    TicTacToeModel.Players.P2 -> {
                        canvas.drawCircle(centerx, centerY, pieceSize, paintP2)
                    }

                    TicTacToeModel.Players.None -> {//nao desenha nada }
                    }
                }
            }
        }
    }


    override fun onTouchEvent (event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event) // returns super if event is null
        if (event.action == MotionEvent.ACTION_UP) { // on touch release ACTION_UP, calculates which cell was touched by dividing x,y coordinates by cell size
            val col = (event.x / cellSize).toInt()
            val row = (event.y / cellSize).toInt()
            if (row in 0..2 && col in 0..2) // if valid cell (0 to 2 indices)
                _onPlay?.play(row, col) // triggers _onPlay callback with row and column
        }
        return true // event handled
    }

    fun interface OnPlayListener { // defines a functional interface for play actions
        fun play(row: Int, col: Int)  // with method play(row, col)
    }

    private var _onPlay: OnPlayListener? = null // private nullable variable _onPlay
    val onPlay: OnPlayListener? // public getter for onPlay
        get() = _onPlay

    fun setOnPlayListener(callback: OnPlayListener) { // setter method to register the play listener from outside
        _onPlay = callback
    }
}

// Custom ViewModel - extending Android Jetpack ViewModel for game data
class GameViewModel : ViewModel() {
    val game = TicTacToeModel() // holds game logic instance
    val winsP1 =
        MutableLiveData<String>() // LiveData for player 1 win count as string (to be observed by UI)
    val winsP2 = MutableLiveData<String>()
    val board =
        MutableLiveData<Array<Array<TicTacToeModel.Players>>>() // LiveData holding the game board state: a 2D array of Players (None, P1, P2)

    val gameFinishedEvent = MutableLiveData<TicTacToeModel.Players>()
    private fun UpdateBoardState() {
        winsP1.value = "P1: ${game.winsP1}"
        winsP2.value = "P2: ${game.winsP2}"
        board.value = game.getBoard()
        gameFinishedEvent.value = game.Winner
    }

    fun start() { // method to initialize or reset the game
        // To change later ...
        game.start()
        UpdateBoardState() // updates UI with current game state
        Log.i("TTT", "Game started")
    }

    fun play(y: Int, x: Int) { // method triggered by user moves
        // To change later ...
        val played = game.play(y, x);
        if (played) {
            UpdateBoardState()
        }
        Log.i("TTT", "Move: $y $x")
    }
    }
