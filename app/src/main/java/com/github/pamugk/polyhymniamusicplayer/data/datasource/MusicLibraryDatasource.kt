package com.github.pamugk.polyhymniamusicplayer.data.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

fun ContentResolver.getAlbums(): List<MediaItem> {
    return this.query(
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR
        ),
        null,
        emptyArray(),
        MediaStore.Audio.Albums.DEFAULT_SORT_ORDER,
    )?.use { cursor ->
        val idIndex = cursor.getColumnIndex(MediaStore.Audio.Albums._ID)
        val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)

        val albums = mutableListOf<MediaItem>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idIndex)
            albums.add(MediaItem.Builder()
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
            )
        }
        return albums
    } ?: emptyList()
}

fun ContentResolver.getArtists(): List<MediaItem> {
    return this.query(
        MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST
        ),
        null,
        emptyArray(),
        MediaStore.Audio.Artists.DEFAULT_SORT_ORDER,
    )?.use { cursor ->
        val idIndex = cursor.getColumnIndex(MediaStore.Audio.Artists._ID)
        val nameIndex = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)

        val artists = mutableListOf<MediaItem>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idIndex)
            artists.add(MediaItem.Builder()
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
            )
        }
        return artists
    } ?: emptyList()
}

fun ContentResolver.getGenres(): List<MediaItem> {
    return this.query(
        MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
        ),
        null,
        emptyArray(),
        MediaStore.Audio.Genres.DEFAULT_SORT_ORDER,
    )?.use { cursor ->
        val idIndex = cursor.getColumnIndex(MediaStore.Audio.Genres._ID)
        val nameIndex = cursor.getColumnIndex(MediaStore.Audio.Genres.NAME)

        val genres = mutableListOf<MediaItem>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idIndex)
            genres.add(
                MediaItem.Builder()
                    .setMediaId(id.toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setGenre(cursor.getStringOrNull(nameIndex))
                            .build()
                    )
                    .setUri(ContentUris.withAppendedId(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, id))
                    .build()
            )
        }
        return genres
    } ?: emptyList()
}

fun ContentResolver.getTracks(): List<MediaItem> {
    return this.query(
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
        "${MediaStore.Audio.Media.IS_MUSIC} != 0",
        emptyArray(),
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

        val tracks = mutableListOf<MediaItem>()
        while (cursor.moveToNext()) {
            tracks.add(
                MediaItem.Builder()
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
            )
        }
        return tracks
    } ?: emptyList()
}