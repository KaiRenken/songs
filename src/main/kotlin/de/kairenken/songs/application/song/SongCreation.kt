package de.kairenken.songs.application.song

import de.kairenken.songs.domain.common.Created
import de.kairenken.songs.domain.common.InvalidArguments
import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.domain.song.SongRepository
import org.springframework.stereotype.Service

@Service
class SongCreation(private val repository: SongRepository) {

    fun createSong(
        name: String,
        artist: String,
        lyrics: String? = null
    ): SongCreationResult = when (val songCreationResult = createSongObject(lyrics, name, artist)) {
        is Created -> {
            checkForDuplicatesAndStore(songCreationResult)
        }

        is InvalidArguments -> InvalidSongArguments(songCreationResult.errors)
    }

    private fun createSongObject(
        lyrics: String?,
        name: String,
        artist: String
    ) = when (lyrics) {
        null -> Song(name, artist)
        else -> Song(name, artist, lyrics)
    }

    private fun checkForDuplicatesAndStore(songCreationResult: Created<Song>) =
        if (!repository.exists(songCreationResult.value.name, songCreationResult.value.artist)) {
            repository.store(songCreationResult.value)
            SongCreated(songCreationResult.value)
        } else {
            SongAlreadyExists
        }
}

sealed class SongCreationResult
class SongCreated(val song: Song) : SongCreationResult()
class InvalidSongArguments(val errors: List<String>) : SongCreationResult()
data object SongAlreadyExists : SongCreationResult()