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
        lyrics: String?
    ): CreationResult {
        val maybeSong = when (lyrics) {
            null -> Song(name, artist)
            else -> Song(name, artist, lyrics)
        }

        return when (maybeSong) {
            is Created -> {
                repository.store(maybeSong.value)
                de.kairenken.songs.application.song.Created(maybeSong.value)
            }

            is InvalidArguments -> de.kairenken.songs.application.song.InvalidArguments(maybeSong.errors)
        }
    }
}

sealed class CreationResult
class Created(val song: Song) : CreationResult()
class InvalidArguments(val errors: List<String>) : CreationResult()