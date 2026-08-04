package com.example.tetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TetrisGame()
            }
        }
    }
}

const val BOARD_WIDTH = 10
const val BOARD_HEIGHT = 20

val PieceColors = listOf(
    Color.Transparent,
    Color.Cyan,         // I
    Color.Blue,         // J
    Color(0xFFFFA500),  // L
    Color.Yellow,       // O
    Color.Green,        // S
    Color.Magenta,      // T
    Color.Red           // Z
)

val Shapes = listOf(
    emptyList(),
    listOf(listOf(1, 1, 1, 1)),                  // I
    listOf(listOf(1, 0, 0), listOf(1, 1, 1)),   // J
    listOf(listOf(0, 0, 1), listOf(1, 1, 1)),   // L
    listOf(listOf(1, 1), listOf(1, 1)),         // O
    listOf(listOf(0, 1, 1), listOf(1, 1, 0)),   // S
    listOf(listOf(0, 1, 0), listOf(1, 1, 1)),   // T
    listOf(listOf(1, 1, 0), listOf(0, 1, 1))    // Z
)

@Composable
fun TetrisGame() {
    var board by remember { mutableStateOf(Array(BOARD_HEIGHT) { IntArray(BOARD_WIDTH) }) }
    var currentPieceIndex by remember { mutableStateOf(Random.nextInt(1, 8)) }
    var currentShape by remember { mutableStateOf(Shapes[currentPieceIndex]) }
    var posX by remember { mutableStateOf(BOARD_WIDTH / 2 - currentShape[0].size / 2) }
    var posY by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    fun checkCollision(newX: Int, newY: Int, shape: List<List<Int>>): Boolean {
        for (r in shape.indices) {
            for (c in shape[r].indices) {
                if (shape[r][c] != 0) {
                    val targetX = newX + c
                    val targetY = newY + r

                    if (targetX !in 0 until BOARD_WIDTH || targetY >= BOARD_HEIGHT) {
                        return true
                    }
                    if (targetY >= 0 && board[targetY][targetX] != 0) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun rotateShape(shape: List<List<Int>>): List<List<Int>> {
        val rows = shape.size
        val cols = shape[0].size
        val rotated = List(cols) { MutableList(rows) { 0 } }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                rotated[c][rows - 1 - r] = shape[r][c]
            }
        }
        return rotated
    }

    fun lockPiece() {
        val newBoard = board.map { it.clone() }.toTypedArray()
        for (r in currentShape.indices) {
            for (c in currentShape[r].indices) {
                if (currentShape[r][c] != 0) {
                    val targetY = posY + r
                    val targetX = posX + c
                    if (targetY in 0 until BOARD_HEIGHT && targetX in 0 until BOARD_WIDTH) {
                        newBoard[targetY][targetX] = currentPieceIndex
                    }
                }
            }
        }

        var clearedLines = 0
        val updatedBoard = newBoard.filter { row ->
            val isFull = row.all { it != 0 }
            if (isFull) clearedLines++
            !isFull
        }.toMutableList()

        while (updatedBoard.size < BOARD_HEIGHT) {
            updatedBoard.add(0, IntArray(BOARD_WIDTH))
        }

        board = updatedBoard.toTypedArray()
        score += clearedLines * 100

        currentPieceIndex = Random.nextInt(1, 8)
        currentShape = Shapes[currentPieceIndex]
        posX = BOARD_WIDTH / 2 - currentShape[0].size / 2
        posY = 0

        if (checkCollision(posX, posY, currentShape)) {
            isGameOver = true
        }
    }

    fun step() {
        if (isGameOver) return
        if (!checkCollision(posX, posY + 1, currentShape)) {
            posY++
        } else {
            lockPiece()
        }
    }

    LaunchedEffect(isGameOver) {
        while (!isGameOver) {
            delay(500L)
            step()
        }
    }

    fun restartGame() {
        board = Array(BOARD_HEIGHT) { IntArray(BOARD_WIDTH) }
        score = 0
        isGameOver = false
        currentPieceIndex = Random.nextInt(1, 8)
        currentShape = Shapes[currentPieceIndex]
        posX = BOARD_WIDTH / 2 - currentShape[0].size / 2
        posY = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("امتیاز: $score", color = Color.White, fontSize = 22.sp)
            Button(onClick = { restartGame() }) {
                Text("شروع مجدد")
            }
        }

        // صفحه بازی مجهز به کنترلر حرکتی (Gestures)
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(0.5f)
                .background(Color.Black, shape = RoundedCornerShape(8.dp))
                .padding(4.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (!isGameOver) {
                                val rotated = rotateShape(currentShape)
                                if (!checkCollision(posX, posY, rotated)) {
                                    currentShape = rotated
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    var accumulatedX = 0f
                    var accumulatedY = 0f
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            if (!isGameOver) {
                                accumulatedX += dragAmount.x
                                accumulatedY += dragAmount.y

                                // جابجایی افقی
                                if (accumulatedX > 40f) {
                                    if (!checkCollision(posX + 1, posY, currentShape)) posX++
                                    accumulatedX = 0f
                                } else if (accumulatedX < -40f) {
                                    if (!checkCollision(posX - 1, posY, currentShape)) posX--
                                    accumulatedX = 0f
                                }

                                // سقوط سریع با کشیدن به پایین
                                if (accumulatedY > 50f) {
                                    step()
                                    accumulatedY = 0f
                                }
                            }
                        },
                        onDragEnd = {
                            accumulatedX = 0f
                            accumulatedY = 0f
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellWidth = size.width / BOARD_WIDTH
                val cellHeight = size.height / BOARD_HEIGHT

                for (r in 0 until BOARD_HEIGHT) {
                    for (c in 0 until BOARD_WIDTH) {
                        val colorIdx = board[r][c]
                        if (colorIdx != 0) {
                            drawRect(
                                color = PieceColors[colorIdx],
                                topLeft = Offset(c * cellWidth, r * cellHeight),
                                size = Size(cellWidth - 1, cellHeight - 1)
                            )
                        }
                    }
                }

                if (!isGameOver) {
                    for (r in currentShape.indices) {
                        for (c in currentShape[r].indices) {
                            if (currentShape[r][c] != 0) {
                                drawRect(
                                    color = PieceColors[currentPieceIndex],
                                    topLeft = Offset((posX + c) * cellWidth, (posY + r) * cellHeight),
                                    size = Size(cellWidth - 1, cellHeight - 1)
                                )
                            }
                        }
                    }
                }
            }

            if (isGameOver) {
                Card(
                    modifier = Modifier.align(Alignment.Center),
                    colors = CardDefaults.cardColors(containerColor = Color(0xCC000000))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("بازی تمام شد!", color = Color.Red, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("امتیاز نهایی: $score", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // راهنمای نحوه بازی
        Text(
            text = "راهنمای کنترل: ضربه = چرخش | کشیدن چپ/راست = حرکت | کشیدن پایین = سقوط",
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
