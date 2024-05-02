package com.ardakazanci.porterduffmodelist

import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ardakazanci.porterduffmodelist.ui.theme.PorterDuffModeListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PorterDuffModeListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PorterDuffCanvasExample()
                }
            }
        }
    }
}

@Composable
fun PorterDuffCanvasExample() {
    val modes = PorterDuff.Mode.entries.toTypedArray()
    val selectedMode = remember { mutableStateOf(PorterDuff.Mode.CLEAR) }

    Column(modifier = Modifier.padding(16.dp)) {
        DrawingCanvas(selectedMode)
        ModeSelector(modes, selectedMode)
    }
}

@Composable
fun DrawingCanvas(selectedMode: MutableState<PorterDuff.Mode>) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        val size = RectF(0f, 0f, this.size.width, this.size.height)
        drawIntoCanvas {
            drawShapes(it.nativeCanvas, size, selectedMode.value)
        }
    }
}

@Composable
fun ModeSelector(modes: Array<PorterDuff.Mode>, selectedMode: MutableState<PorterDuff.Mode>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(modes.size) { index ->
            ModeItem(modes[index], selectedMode)
        }
    }
}

@Composable
fun ModeItem(mode: PorterDuff.Mode, selectedMode: MutableState<PorterDuff.Mode>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        RadioButton(
            selected = selectedMode.value == mode,
            onClick = { selectedMode.value = mode }
        )
        Text(text = mode.name, fontSize = 12.sp)
    }
}

fun drawShapes(canvas: Canvas, size: RectF, selectedMode: PorterDuff.Mode) {
    val paintRect = android.graphics.Paint().apply {
        color = android.graphics.Color.BLUE
        style = android.graphics.Paint.Style.FILL
    }

    val paintCircle = android.graphics.Paint().apply {
        color = android.graphics.Color.GREEN
        style = android.graphics.Paint.Style.FILL
        xfermode = PorterDuffXfermode(selectedMode)
    }

    with(size) {
        canvas.saveLayer(this, null)
        canvas.drawRect(width() * 0.4f, height() * 0.4f, width() * 0.6f, height() * 0.8f, paintRect)
        canvas.drawCircle(
            width() * 0.5f,
            height() * 0.5f,
            width().coerceAtMost(height()) * 0.25f,
            paintCircle
        )
        canvas.restore()
    }
}



