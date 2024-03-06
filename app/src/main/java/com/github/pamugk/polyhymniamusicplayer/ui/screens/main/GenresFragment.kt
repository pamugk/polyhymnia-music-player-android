package com.github.pamugk.polyhymniamusicplayer.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.pamugk.polyhymniamusicplayer.data.datasource.getGenres
import com.github.pamugk.polyhymniamusicplayer.data.entity.Genre

@Composable
internal fun GenresFragment(padding: PaddingValues = PaddingValues()) {
    val context = LocalContext.current
    val genres by produceState(initialValue = emptyList<Genre>()) {
        value = context.contentResolver.getGenres()
    }
    
    LazyColumn(modifier = Modifier.padding(padding)) {
        items(genres) { genre ->
            Text(text = genre.name ?: "Unknown genre")
        }
    }
}