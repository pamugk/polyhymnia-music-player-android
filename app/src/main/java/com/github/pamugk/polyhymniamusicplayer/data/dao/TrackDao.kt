package com.github.pamugk.polyhymniamusicplayer.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.pamugk.polyhymniamusicplayer.data.entity.Track

@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    fun getAll(): List<Track>
}