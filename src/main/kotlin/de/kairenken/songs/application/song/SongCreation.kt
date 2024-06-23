package de.kairenken.songs.application.song

import de.kairenken.songs.domain.common.CreateUseCaseDsl.Companion.createUseCase
import de.kairenken.songs.domain.song.Song
import de.kairenken.songs.domain.song.SongRepository
import org.springframework.stereotype.Service

@Service
class SongCreation(private val repository: SongRepository) {

    fun createSong(
        name: String,
        artist: String,
        lyrics: String? = null
    ) = createUseCase(SongCreated::class, InvalidSongArguments::class) {
        create(
            when (lyrics) {
                null -> Song(name, artist)
                else -> Song(name, artist, lyrics)
            }
        )

        checkAfterCreation(SongAlreadyExists) {
            repository.exists((it as Song).name, (it as Song).artist)
        }

        doFinally { repository.store(it) }
    }
}

sealed class SongCreationResult
class SongCreated(val song: Song) : SongCreationResult()
class InvalidSongArguments(val errors: List<String>) : SongCreationResult()
data object SongAlreadyExists : SongCreationResult()