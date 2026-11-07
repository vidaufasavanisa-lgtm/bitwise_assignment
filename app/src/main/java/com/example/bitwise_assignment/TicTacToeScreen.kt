
package com.example.bitwise_assignment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class TicTacToeMode {
    TWO_PLAYERS,
    COMPUTER
}

@Composable
fun TicTacToeScreen(onBack: () -> Unit) {

    var mode by remember {
        mutableStateOf(TicTacToeMode.TWO_PLAYERS)
    }

    var board by remember {
        mutableStateOf(List<String?>(9) { null })
    }

    var isXTurn by remember {
        mutableStateOf(true)
    }

    var winner by remember {
        mutableStateOf<String?>(null)
    }

    var isAiThinking by remember {
        mutableStateOf(false)
    }

    var gameSessionId by remember {
        mutableIntStateOf(0)
    }

    var scoreX by remember {
        mutableIntStateOf(0)
    }

    var scoreO by remember {
        mutableIntStateOf(0)
    }

    var showResult by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val isAiMode = mode == TicTacToeMode.COMPUTER

    // Menampilkan popup setelah permainan selesai
    LaunchedEffect(winner) {
        if (winner != null) {
            delay(800L)
            showResult = true
        }
    }

    // Reset permainan
    fun resetGame() {
        gameSessionId++
        board = List(9) { null }
        isXTurn = true
        winner = null
        isAiThinking = false
        showResult = false
    }

    // Mengatur hasil dan skor
    fun finishGame(result: String) {
        winner = result

        if (result == "X") scoreX++
        if (result == "O") scoreO++
    }

    // Mengganti mode permainan
    fun selectMode(selectedMode: TicTacToeMode) {
        mode = selectedMode
        scoreX = 0
        scoreO = 0
        resetGame()
    }

    // Menjalankan langkah pemain dan AI
    fun makeMove(index: Int) {

        if (
            board[index] != null ||
            winner != null ||
            isAiThinking
        ) return

        if (isAiMode && !isXTurn) return

        // Langkah pemain
        val newBoard = board.toMutableList()
        newBoard[index] = if (isXTurn) "X" else "O"
        board = newBoard

        val result = checkGameWinner(newBoard)

        if (result != null) {
            finishGame(result)
            return
        }

        isXTurn = !isXTurn

        // Langkah komputer
        if (isAiMode && !isXTurn) {

            isAiThinking = true
            val currentSession = gameSessionId

            scope.launch {
                delay(400L)

                // Memastikan sesi permainan belum berubah
                if (
                    gameSessionId != currentSession ||
                    winner != null ||
                    !isAiMode ||
                    isXTurn
                ) {
                    isAiThinking = false
                    return@launch
                }

                val aiMove = findBestMove(board)

                if (aiMove != -1) {
                    val aiBoard = board.toMutableList()
                    aiBoard[aiMove] = "O"
                    board = aiBoard

                    val aiResult = checkGameWinner(aiBoard)

                    if (aiResult != null) {
                        finishGame(aiResult)
                    } else {
                        isXTurn = true
                    }
                }

                isAiThinking = false
            }
        }
    }

    // Tampilan utama
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Judul
        Text(
            text = "Tic Tac Toe",
            color = TextDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Susun tiga simbol secara berurutan untuk menang!",
            color = TextGray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Pilihan mode permainan
        Text(
            text = "Pilih Mode Permainan",
            color = TextDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = {
                    selectMode(TicTacToeMode.TWO_PLAYERS)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == TicTacToeMode.TWO_PLAYERS)
                        Purple
                    else
                        CardBgColor,
                    contentColor = if (mode == TicTacToeMode.TWO_PLAYERS)
                        Color.White
                    else
                        TextDark
                ),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Text(
                    text = "2 Pemain",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = {
                    selectMode(TicTacToeMode.COMPUTER)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == TicTacToeMode.COMPUTER)
                        Purple
                    else
                        CardBgColor,
                    contentColor = if (mode == TicTacToeMode.COMPUTER)
                        Color.White
                    else
                        TextDark
                ),
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Text(
                    text = "Lawan Komputer",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Informasi giliran permainan
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBgColor
            ),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = when {
                        isAiThinking -> "Komputer sedang berpikir..."
                        isAiMode && isXTurn -> "Giliran Kamu"
                        isAiMode -> "Giliran Komputer"
                        else -> "Giliran Pemain ${if (isXTurn) "X" else "O"}"
                    },
                    color = Purple,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = when {
                        isAiThinking ->
                            "Tunggu sebentar, komputer sedang menentukan langkah."
                        isAiMode ->
                            "Kamu menggunakan X, komputer menggunakan O."
                        else ->
                            "Pemain X dan O bermain secara bergantian."
                    },
                    color = TextGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Papan permainan
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBgColor
            ),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                repeat(3) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        repeat(3) { column ->
                            val index = row * 3 + column
                            val mark = board[index]

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        color = if (mark != null) {
                                            Purple.copy(alpha = 0.12f)
                                        } else {
                                            if (LocalDarkTheme.current)
                                                Color(0xFF2A1D35)
                                            else
                                                Color(0xFFF7F6FC)
                                        },
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable(
                                        enabled = mark == null &&
                                                winner == null &&
                                                !isAiThinking &&
                                                (!isAiMode || isXTurn)
                                    ) {
                                        makeMove(index)
                                    },
                                contentAlignment = Alignment.Center
                            ) {

                                if (mark != null) {
                                    Text(
                                        text = mark,
                                        color = if (mark == "X")
                                            Purple
                                        else
                                            Color(0xFFE879A9),
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Keterangan simbol
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "X",
                    color = Purple,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = if (isAiMode) "Kamu" else "Pemain 1",
                    color = TextGray,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "O",
                    color = Color(0xFFE879A9),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = if (isAiMode) "Komputer" else "Pemain 2",
                    color = TextGray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol ulang permainan
        Button(
            onClick = {
                resetGame()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Main Lagi",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }

    // Popup hasil permainan
    if (showResult && winner != null) {

        val resultTitle = when {
            winner == "DRAW" -> "Permainan Seri!"
            isAiMode && winner == "X" -> "Kamu Menang!"
            isAiMode && winner == "O" -> "Kamu Kalah"
            else -> "Pemain $winner Menang!"
        }

        val resultMessage = when {
            winner == "DRAW" ->
                "Permainan berakhir imbang. Coba lagi!"

            isAiMode && winner == "X" ->
                "Hebat! Kamu berhasil mengalahkan komputer."

            isAiMode && winner == "O" ->
                "Jangan menyerah! Coba lagi dan kalahkan komputer."

            else ->
                "Selamat! Permainan yang seru."
        }

        val foxMood = when {
            winner == "DRAW" -> FoxMood.NORMAL
            winner == "X" -> FoxMood.HAPPY
            else -> FoxMood.SAD
        }

        Dialog(
            onDismissRequest = {
                // Popup tidak ditutup ketika area luar ditekan
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBgColor
                ),
                border = BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    FoxMascot(
                        mood = foxMood,
                        modifier = Modifier.size(130.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = resultTitle,
                        color = Purple,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = resultMessage,
                        color = TextGray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            resetGame()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Main Lagi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

// Memeriksa pemenang permainan
private fun checkGameWinner(board: List<String?>): String? {

    val winPatterns = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),

        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),

        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    for (pattern in winPatterns) {
        val (a, b, c) = pattern

        if (
            board[a] != null &&
            board[a] == board[b] &&
            board[a] == board[c]
        ) {
            return board[a]
        }
    }

    return if (board.all { it != null }) "DRAW" else null
}

// Menentukan langkah terbaik komputer menggunakan Minimax
private fun findBestMove(currentBoard: List<String?>): Int {

    var bestScore = Int.MIN_VALUE
    var bestMove = -1

    val mutableBoard = currentBoard.toMutableList()

    for (i in 0..8) {
        if (mutableBoard[i] == null) {

            mutableBoard[i] = "O"

            val score = minimax(
                mutableBoard = mutableBoard,
                depth = 0,
                isMaximizing = false
            )

            mutableBoard[i] = null

            if (score > bestScore) {
                bestScore = score
                bestMove = i
            }
        }
    }

    return bestMove
}

// Algoritma Minimax
private fun minimax(
    mutableBoard: MutableList<String?>,
    depth: Int,
    isMaximizing: Boolean
): Int {

    val result = checkGameWinner(mutableBoard)

    if (result == "O") return 10 - depth
    if (result == "X") return depth - 10
    if (result == "DRAW") return 0

    if (isMaximizing) {

        var best = Int.MIN_VALUE

        for (i in 0..8) {
            if (mutableBoard[i] == null) {

                mutableBoard[i] = "O"

                best = maxOf(
                    best,
                    minimax(mutableBoard, depth + 1, false)
                )

                mutableBoard[i] = null
            }
        }

        return best

    } else {

        var best = Int.MAX_VALUE

        for (i in 0..8) {
            if (mutableBoard[i] == null) {

                mutableBoard[i] = "X"

                best = minOf(
                    best,
                    minimax(mutableBoard, depth + 1, true)
                )

                mutableBoard[i] = null
            }
        }

        return best
    }
}
