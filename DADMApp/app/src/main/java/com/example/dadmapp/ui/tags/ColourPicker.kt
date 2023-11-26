package com.example.dadmapp.ui.tags

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dadmapp.ui.theme.BgDark
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColourPicker(
    onCancel: () -> Unit,
    onColorChange: (newVal: String) -> Unit
) {
    val controller = rememberColorPickerController()

    var colour by remember {
        mutableStateOf<ColorEnvelope?>(null)
    }

    val boxSize = 50.dp
    val textStyle = TextStyle(
        color = Color.White,
        fontSize = 18.sp
    )

    fun onSelect() {
        if (colour != null) {
            val hexCode = "#" + colour!!.hexCode
            onColorChange(hexCode)
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(BgDark, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Row {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        colour = colorEnvelope
                    }
                )
            }
            Row(
                modifier = Modifier.padding(horizontal = 15.dp).padding(top = 26.dp)
            ) {
                BrightnessSlider(
                    controller = controller,
                    modifier = Modifier.fillMaxWidth().height(20.dp),
                    borderSize = 0.dp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 26.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .height(boxSize)
                    .width(boxSize)
                    .background(
                        colour?.color ?: Color.Black
                    )
                )
            }
            Row {
                ClickableText(text = AnnotatedString("Select"), onClick = { onSelect() }, style = textStyle, modifier = Modifier.padding(end = 20.dp))
                ClickableText(text = AnnotatedString("Cancel"), onClick = { onCancel() }, style = textStyle.copy(color = Color.Gray))
            }
        }
    }
}