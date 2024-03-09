package com.github.pamugk.polyhymniamusicplayer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun StatusPage(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(128.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text)
    }
}