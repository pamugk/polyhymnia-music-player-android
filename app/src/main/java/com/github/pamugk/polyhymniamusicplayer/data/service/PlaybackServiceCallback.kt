package com.github.pamugk.polyhymniamusicplayer.data.service

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Rating
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionResult
import com.github.pamugk.polyhymniamusicplayer.data.datasource.MusicLibraryDatasource
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.future

internal class PlaybackServiceCallback(
    private val coroutineScope: CoroutineScope,
    private val datasource: MusicLibraryDatasource
) : MediaLibraryService.MediaLibrarySession.Callback {

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        return Futures.immediateFuture(
            LibraryResult.ofItem(
                MediaItem.Builder()
                    .setMediaId(LIBRARY_ROOT_ID)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .build()
                    )
                    .build(),
                params
            )
        )
    }

    override fun onGetItem(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> = when {
        mediaId.startsWith("albums/") -> coroutineScope.future {
            val album = datasource.getAlbum(mediaId.substringAfter("albums/"))
            if (album == null)
                LibraryResult.ofError(LibraryResult.RESULT_ERROR_UNKNOWN)
            else LibraryResult.ofItem(album, null)
        }
        mediaId.startsWith("artists/") -> coroutineScope.future {
            val artist = datasource.getArtist(mediaId.substringAfter("artists/"))
            if (artist == null)
                LibraryResult.ofError(LibraryResult.RESULT_ERROR_UNKNOWN)
            else LibraryResult.ofItem(artist, null)
        }
        mediaId.startsWith("genres/") -> coroutineScope.future {
            val genre = datasource.getGenre(mediaId.substringAfter("genres/"))
            if (genre == null)
                LibraryResult.ofError(LibraryResult.RESULT_ERROR_UNKNOWN)
            else LibraryResult.ofItem(genre, null)
        }
        mediaId.startsWith("tracks/") -> coroutineScope.future {
            val track = datasource.getTrack(mediaId.substringAfter("tracks/"))
            if (track == null)
                LibraryResult.ofError(LibraryResult.RESULT_ERROR_UNKNOWN)
            else LibraryResult.ofItem(track, null)
        }
        else -> Futures.immediateFuture(LibraryResult.ofError(LibraryResult.RESULT_ERROR_UNKNOWN))
    }

    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> =
        when (parentId) {
            LIBRARY_ROOT_ID -> Futures.immediateFuture(
                LibraryResult.ofItemList(
                    listOf(
                        MediaItem.Builder()
                            .setMediaId(LIBRARY_TRACKS_SECTION_ID)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setIsBrowsable(true)
                                    .setIsPlayable(false)
                                    .build()
                            )
                            .build(),
                        MediaItem.Builder()
                            .setMediaId(LIBRARY_ALBUMS_SECTION_ID)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setIsBrowsable(true)
                                    .setIsPlayable(false)
                                    .build()
                            )
                            .build(),
                        MediaItem.Builder()
                            .setMediaId(LIBRARY_ARTISTS_SECTION_ID)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setIsBrowsable(true)
                                    .setIsPlayable(false)
                                    .build()
                            )
                            .build(),
                        MediaItem.Builder()
                            .setMediaId(LIBRARY_GENRES_SECTION_ID)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setIsBrowsable(true)
                                    .setIsPlayable(false)
                                    .build()
                            )
                            .build(),
                    ),
                    params
                )
            )
            LIBRARY_ALBUMS_SECTION_ID -> coroutineScope.future {
                LibraryResult.ofItemList(datasource.getAlbums(), params)
            }
            LIBRARY_ARTISTS_SECTION_ID -> coroutineScope.future {
                LibraryResult.ofItemList(datasource.getArtists(), params)
            }
            LIBRARY_GENRES_SECTION_ID -> coroutineScope.future {
                LibraryResult.ofItemList(datasource.getGenres(), params)
            }
            LIBRARY_TRACKS_SECTION_ID -> coroutineScope.future {
                LibraryResult.ofItemList(datasource.getTracks(), params)
            }
            else -> when {
                parentId.startsWith("albums/") -> coroutineScope.future {
                    LibraryResult.ofItemList(
                        datasource.getAlbumTracks(parentId.substringAfter("albums/")),
                        params
                    )
                }
                parentId.startsWith("artists/") -> coroutineScope.future {
                    LibraryResult.ofItemList(
                        datasource.getArtistTracks(parentId.substringAfter("artists/")),
                        params
                    )
                }
                parentId.startsWith("genres/") -> coroutineScope.future {
                    LibraryResult.ofItemList(
                        datasource.getGenreTracks(parentId.substringAfter("genres/")),
                        params
                    )
                }
                else -> Futures.immediateFuture(LibraryResult.ofItemList(emptyList(), params))
            }
        }

    override fun onSearch(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        return super.onSearch(session, browser, query, params)
    }

    override fun onGetSearchResult(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return Futures.immediateFuture(LibraryResult.ofItemList(emptyList(), params))
    }

    override fun onSetRating(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        rating: Rating
    ): ListenableFuture<SessionResult> {
        return super.onSetRating(session, controller, rating)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val unknownItemsIds = mediaItems.filter { it.localConfiguration == null }.map { it.mediaId }
        // Every media item URI is filled,
        // no need to query DB
        if (unknownItemsIds.isEmpty()) {
            return Futures.immediateFuture(mediaItems)
        }

        return coroutineScope.future {
            val pathsByIds = datasource.getFilePathsByTrackIds(unknownItemsIds)
            mediaItems.map { mediaItem ->
                pathsByIds[mediaItem.mediaId]?.let { filePath ->
                    mediaItem.buildUpon().setUri(filePath).build()
                } ?: mediaItem
            }.toMutableList()
        }
    }

    private companion object {
        /* Library root */
        const val LIBRARY_ROOT_ID = "root"

        /* Library section */
        const val LIBRARY_ALBUMS_SECTION_ID = "albums"
        const val LIBRARY_ARTISTS_SECTION_ID = "artists"
        const val LIBRARY_GENRES_SECTION_ID = "genres"
        const val LIBRARY_TRACKS_SECTION_ID = "tracks"
    }
}