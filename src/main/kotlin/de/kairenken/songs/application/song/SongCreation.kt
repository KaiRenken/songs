package de.kairenken.songs.application.song

import de.kairenken.songs.domain.common.CreateDsl
import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.domain.song.SongRepository
import org.springframework.stereotype.Service

@Service
class SongCreation(private val repository: SongRepository) {

    fun createSong(
        name: String,
        artist: String,
        lyrics: String? = null
    ) = CreateDsl(Song::class, SongCreationResult::class, SongCreated::class, InvalidSongArguments::class) {
        create(
            when (lyrics) {
                null -> Song(Song.Name(name), Song.Artist(artist))
                else -> Song(Song.Name(name), Song.Artist(artist), Song.Lyrics(lyrics))
            }
        )

        checkAfterCreation(SongAlreadyExists) {
            repository.exists(it.name, it.artist)
        }

        doFinally { repository.store(it) }
    }
}

sealed class SongCreationResult
class SongCreated(val song: Song) : SongCreationResult()
class InvalidSongArguments(val errors: List<String>) : SongCreationResult()
data object SongAlreadyExists : SongCreationResult()