package com.example.talksy.presentation.reusable.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.talksy.TalksyApp.Companion.TAG

@Composable
fun ProgressDialog(modifier: Modifier = Modifier, text: String = "") {
    Dialog(
        onDismissRequest = {},
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        if (text.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .background(
                        AlertDialogDefaults.containerColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(150.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall.copy(color = AlertDialogDefaults.titleContentColor)
                )
                CircularProgressIndicator(color = AlertDialogDefaults.iconContentColor)
            }
        } else {
            Box(
                contentAlignment = Center,
                modifier = modifier
                    .size(100.dp)
                    .background(
                        AlertDialogDefaults.containerColor,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                CircularProgressIndicator(color = AlertDialogDefaults.iconContentColor)
            }
        }
    }
}