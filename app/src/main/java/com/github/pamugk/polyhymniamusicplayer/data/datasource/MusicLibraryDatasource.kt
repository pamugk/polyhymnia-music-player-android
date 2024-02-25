package com.github.pamugk.polyhymniamusicplayer.data.datasource

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.github.pamugk.polyhymniamusicplayer.data.entity.Album
import com.github.pamugk.polyhymniamusicplayer.data.entity.Artist
import com.github.pamugk.polyhymniamusicplayer.data.entity.Genre
import com.github.pamugk.polyhymniamusicplayer.data.entity.Track

fun ContentResolver.getAlbums(): List<Album> {
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

        val albums = mutableListOf<Album>()
        while (cursor.moveToNext()) {
            albums.add(Album(
                id = cursor.getLong(idIndex),
                title = cursor.getStringOrNull(albumIndex)
            ))
        }
        return albums
    } ?: emptyList()
}

fun ContentResolver.getArtists(): List<Artist> {
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

        val artists = mutableListOf<Artist>()
        while (cursor.moveToNext()) {
            artists.add(Artist(
                id = cursor.getLong(idIndex),
                name = cursor.getStringOrNull(nameIndex)
            ))
        }
        return artists
    } ?: emptyList()
}

fun ContentResolver.getGenres(): List<Genre> {
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

        val genres = mutableListOf<Genre>()
        while (cursor.moveToNext()) {
            genres.add(Genre(
                id = cursor.getLong(idIndex),
                name = cursor.getStringOrNull(nameIndex)
            ))
        }
        return genres
    } ?: emptyList()
}

fun ContentResolver.getTracks(): List<Track> {
    return this.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Media._ID,
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
            MediaStore.Audio.Media.YEAR
        ),
        "${MediaStore.Audio.Media.IS_MUSIC} != 0",
        emptyArray(),
        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
    )?.use { cursor ->
        val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
        val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        val albumArtistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST)
        val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
        val bitrateIndex = cursor.getColumnIndex(MediaStore.Audio.Media.BITRATE)
        val cdTrackNumberIndex = cursor.getColumnIndex(MediaStore.Audio.Media.CD_TRACK_NUMBER)
        val composerIndex = cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER)
        val discNumberIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISC_NUMBER)
        val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
        val numTracksIndex = cursor.getColumnIndex(MediaStore.Audio.Media.NUM_TRACKS)
        val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val trackIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)
        val yearIndex = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)

        val tracks = mutableListOf<Track>()
        while (cursor.moveToNext()) {
            tracks.add(
                Track(
                    id = cursor.getLong(idIndex),
                    album = cursor.getStringOrNull(albumIndex),
                    artist = cursor.getStringOrNull(artistIndex),
                    title = cursor.getStringOrNull(titleIndex)
                )
            )
        }
        return tracks
    } ?: emptyList()
}