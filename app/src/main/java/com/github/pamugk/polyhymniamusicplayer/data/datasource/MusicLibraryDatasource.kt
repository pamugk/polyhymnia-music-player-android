package com.github.pamugk.polyhymniamusicplayer.data.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicLibraryDatasource(private val contentResolver: ContentResolver) {
    suspend fun getAlbums() = withContext(Dispatchers.IO) {
        contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.LAST_YEAR
            ),
            null,
            emptyArray(),
            MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Albums._ID)
            val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)

            generateSequence {
                if (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    MediaItem.Builder()
                        .setMediaId(cursor.getLong(idIndex).toString())
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setAlbumTitle(cursor.getStringOrNull(albumIndex))
                                .setIsBrowsable(true)
                                .setIsPlayable(true)
                                .setMediaType(MediaMetadata.MEDIA_TYPE_ALBUM)
                                .build()
                        )
                        .setUri(ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id))
                        .build()
                } else {
                    null
                }
            }.toList()
        } ?: emptyList()
    }

    suspend fun getAlbumTracks(id: String) = withContext(Dispatchers.IO) {
        getTracksInternal(
            filter = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.ALBUM_ID} = ?",
            filterParams = arrayOf(id)
        )
    }

    suspend fun getArtists() = withContext(Dispatchers.IO) {
        contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST
            ),
            null,
            emptyArray(),
            MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Artists._ID)
            val nameIndex = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)

            generateSequence {
                if (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    MediaItem.Builder()
                        .setMediaId(id.toString())
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setArtist(cursor.getStringOrNull(nameIndex))
                                .setIsBrowsable(true)
                                .setIsPlayable(true)
                                .setMediaType(MediaMetadata.MEDIA_TYPE_ARTIST)
                                .build()
                        )
                        .setUri(ContentUris.withAppendedId(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, id))
                        .build()
                } else {
                    null
                }
            }.toList()
        } ?: emptyList()
    }

    suspend fun getArtistTracks(id: String) = withContext(Dispatchers.IO) {
        getTracksInternal(
            filter = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.ARTIST_ID} = ?",
            filterParams = arrayOf(id)
        )
    }

    suspend fun getGenres() = withContext(Dispatchers.IO) {
        contentResolver.query(
            MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME
            ),
            null,
            emptyArray(),
            MediaStore.Audio.Genres.DEFAULT_SORT_ORDER
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Genres._ID)
            val nameIndex = cursor.getColumnIndex(MediaStore.Audio.Genres.NAME)

            generateSequence {
                if (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    MediaItem.Builder()
                        .setMediaId(id.toString())
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setGenre(cursor.getStringOrNull(nameIndex))
                                .setIsBrowsable(true)
                                .setIsPlayable(false)
                                .build()
                        )
                        .setUri(ContentUris.withAppendedId(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, id))
                        .build()
                } else {
                    null
                }
            }.toList()
        } ?: emptyList()
    }

    suspend fun getGenreTracks(id: String) = withContext(Dispatchers.IO) {
        getTracksInternal(
            filter = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.GENRE_ID} = ?",
            filterParams = arrayOf(id)
        )
    }

    suspend fun getTracks() = withContext(Dispatchers.IO) {
        getTracksInternal()
    }

    private fun getTracksInternal(
        filter: String = "${MediaStore.Audio.Media.IS_MUSIC} != 0",
        filterParams: Array<String> = emptyArray()
    ) = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.BITRATE,
            MediaStore.Audio.Media.CD_TRACK_NUMBER,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.DISC_NUMBER,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.NUM_TRACKS,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.WRITER,
            MediaStore.Audio.Media.YEAR
        ),
        filter,
        filterParams,
        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
    )?.use { cursor ->
        val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
        val pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
        val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        val albumArtistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST)
        val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
        val composerIndex = cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER)
        val discNumberIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISC_NUMBER)
        val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val writerIndex = cursor.getColumnIndex(MediaStore.Audio.Media.WRITER)
        val yearIndex = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)

        generateSequence {
            if (cursor.moveToNext()) {
                val item = MediaItem.Builder()
                    .setMediaId(cursor.getLong(idIndex).toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setAlbumTitle(cursor.getStringOrNull(albumIndex))
                            .setAlbumArtist(cursor.getStringOrNull(albumArtistIndex))
                            .setArtist(cursor.getStringOrNull(artistIndex))
                            .setComposer(cursor.getStringOrNull(composerIndex))
                            .setDiscNumber(cursor.getIntOrNull(discNumberIndex))
                            .setIsBrowsable(false)
                            .setIsPlayable(true)
                            .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                            .setRecordingYear(cursor.getIntOrNull(yearIndex))
                            .setTitle(cursor.getStringOrNull(titleIndex))
                            .setWriter(cursor.getStringOrNull(writerIndex))
                            .build()
                    )
                    .setUri(cursor.getString(pathIndex))
                    .build()
                item
            } else {
                null
            }
        }.toList()
    } ?: emptyList()
}