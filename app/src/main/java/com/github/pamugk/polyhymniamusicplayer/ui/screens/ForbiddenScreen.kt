package com.github.pamugk.polyhymniamusicplayer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.pamugk.polyhymniamusicplayer.R

@Composable
fun ForbiddenScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = Icons.Default.Block,
            contentDescription = stringResource(R.string.no_permission),
            modifier = Modifier.size(128.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
        )
        Text(stringResource(R.string.not_enough_permissions))
    }
}

@Composable
@Preview
private fun ForbiddenPreview() {
    ForbiddenScreen()
}