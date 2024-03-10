package com.github.pamugk.polyhymniamusicplayer.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.github.pamugk.polyhymniamusicplayer.R

@Composable
fun AlbumListItem(
    album: MediaItem,
    modifier: Modifier = Modifier,
    onClick: (MediaItem) -> Unit = {}
) {
    val albumCover = album.mediaMetadata.artworkData?.let { albumData ->
        BitmapFactory.decodeByteArray(albumData, 0, albumData.size)
    }

    Row(
        modifier = modifier
            .clickable { onClick(album) }
            .fillMaxWidth()
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (albumCover == null) {
            Image(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = stringResource(R.string.album_cover),
                modifier = Modifier.size(48.dp)
            )
        } else {
            Image(
                bitmap = albumCover.asImageBitmap(),
                contentDescription = stringResource(R.string.album_cover),
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = album.mediaMetadata.albumTitle?.toString() ?: stringResource(R.string.no_album),
            modifier = modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}