package com.github.pamugk.polyhymniamusicplayer.data.entity

data class Track(
    val id: Long,
    val album: String? = null,
    val artist: String? = null,
    val title: String? = null,
)