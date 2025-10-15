class TicTacToeModel {
    enum class Players {
        None, P1, P2
    }

    private lateinit var board: Array<Array<Players>>
    private var _winsP1 = 0
    private var _winsP2 = 0
    private var _currentPlayer = Players.None
    private var _ctrl = 9
}