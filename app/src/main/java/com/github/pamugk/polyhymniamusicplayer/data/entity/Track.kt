package com.github.pamugk.polyhymniamusicplayer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Track(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?
)