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
    ): ListenableFuture<LibraryResult<MediaItem>> {
        return super.onGetItem(session, browser, mediaId)
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
            else -> Futures.immediateFuture(LibraryResult.ofItemList(emptyList(), params))
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