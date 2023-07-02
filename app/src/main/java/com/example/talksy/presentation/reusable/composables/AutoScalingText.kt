package com.example.talksy.presentation.reusable.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun AutoScalingText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle,
    fontSize: TextUnit?,
    text: String
) {

    //setting custom fontSize only if specified
    var mutableStyle by remember {
        if (fontSize != null) mutableStateOf(style.copy(fontSize = fontSize))
        mutableStateOf(style)
    }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        textAlign = textAlign,
        style = mutableStyle,
        overflow = TextOverflow.Clip,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        //if text height is overflowing, making the text size a bit smaller until it fits.
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowHeight) {
                mutableStyle = mutableStyle.copy(fontSize = mutableStyle.fontSize * 0.9)
            } else {
                readyToDraw = true
            }
        }
    )
}