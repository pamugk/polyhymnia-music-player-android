package com.github.pamugk.polyhymniamusicplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.pamugk.polyhymniamusicplayer.data.dao.TrackDao
import com.github.pamugk.polyhymniamusicplayer.data.entity.Track

@Database(entities = [Track::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}
