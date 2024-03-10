package com.github.pamugk.polyhymniamusicplayer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.github.pamugk.polyhymniamusicplayer.R

@Composable
fun GenreListItem(
    genre: MediaItem,
    modifier: Modifier = Modifier,
    onClick: (MediaItem) -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable { onClick(genre) }
            .fillMaxWidth()
            .height(54.dp)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = genre.mediaMetadata.genre?.toString() ?: stringResource(R.string.no_genre),
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}