package tw.edu.pu.csim.s1141237.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.objectFloatMapOf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.s1141237.canvas.ui.theme.CanvasTheme

import androidx.compose.runtime.*

import androidx.compose.ui.layout.onSizeChanged

import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Canvascreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Canvascreen(modifier: Modifier = Modifier) {

    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }

    // 1. 新增：用來記錄小小兵 X 軸位移的狀態
    var minionsX by remember { mutableStateOf(0f) }
    var minionsY by remember { mutableStateOf(0f) }

    var gameover by remember { mutableStateOf(false) }

    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)

    // 2. 新增：啟動一個協程，每秒將位移增加 10f
    LaunchedEffect(gameover) {
        while (!gameover) {
            delay(50L) // 暫停 1000 毫秒 (1 秒)
            minionsX += 10
            minionsY += 10
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                // 建議修改：在這裡取得畫布大小，避免在 drawScope 中寫入 State
                .onSizeChanged { size ->
                    canvasWidth = size.width.toFloat()
                    canvasHeight = size.height.toFloat()
                }
        ) {
            // 繪製洋紅色的圓, 半徑 100, 圓心 (100,100)
            drawCircle(
                Color.Magenta,
                100f,
                Offset(100f, 100f)
            )

            // 繪製藍色空心矩形, 左上角 (300,200), 寬度與高度 200
            drawRect(
                Color.Blue,
                Offset(300f, 200f),
                Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            drawLine(
                Color.Red,
                Offset(200f, 800f),
                Offset(size.width, size.height),
                strokeWidth = 10f
            )

            // 3. 修改：將 translate 的 left (X軸) 綁定到 minionXOffset 狀態
            translate(left = minionsX, minionsY) {
                drawImage(minionsImage)
            }
        }
        Column(){
            Text("畫布寬度 $canvasWidth,長度 $canvasHeight")

            Button (
                onClick = { gameover = !gameover}
            ){
                if (gameover){Text("繼續遊戲")}
                else {Text ("停止遊戲")}
            }
        }
    }
}
